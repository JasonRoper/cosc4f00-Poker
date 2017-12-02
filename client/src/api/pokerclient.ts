import webstomp from 'webstomp-client'

type EventCallback = (payload: any) => void

/**
 * Used to manage multiple subscriptions to the websocket.
 */
class Subscription {
  private path: string
  private callbacks: Set<EventCallback>
  private handle: webstomp.Subscription

  /**
   * Create a Subscription to the given path. If the socket has not connected
   * {@Link Subscription#doSubscribe} will need to be called with the socket
   * that will be subscribed to.
   * @param path - the path that this subscription will be managing
   * @param socket - the socket that will be subscribed to
   */
  constructor (path: string, socket: webstomp.Client) {
    this.path = path
    this.callbacks = new Set()
    this.doSubscribe(socket)
  }

  /**
   * If the Subscription has not already subscribed to the websocket,
   * try to subscribe again
   * @param socket - the socket that will be subscribed to
   */
  public doSubscribe (socket: webstomp.Client) {
    if (this.handle) {
      return
    }

    const callback = (payload: any) => this.notifyAll(JSON.parse(payload.body))

    if (socket.connected) {
      this.handle = socket.subscribe(this.path, callback)
    }
  }

  /**
   * Add a callback to the path that this subscription is listening to.
   * @param callback - the callback that will be added to the subscription list
   */
  public addCallback (callback: EventCallback) {
    this.callbacks.add(callback)
  }

  /**
   * Remove a callback that was listening to this subscription
   * @param callback - the callback that will be removed from the subscription list
   */
  public removeCallback (callback: EventCallback) {
    this.callbacks.delete(callback)
  }

  /**
   * @returns - the number of callbacks that are currently subscribed to this subscription
   */
  public subscribers (): number {
    return this.callbacks.size
  }

  /**
   * Send a message to all subscribers to this Subscription.
   * @param payload - the message to be sent to all callbacks listening to this subscription
   */
  public notifyAll (payload: any) {
    this.callbacks.forEach((callback: EventCallback) => callback(payload))
  }

  /**
   * Unsubscribe from the websocket. {@Link Subscription#doSubscribe} will need to be called
   * to resubscribe to a websocket.
   */
  public unsubscribeSocket () {
    if (this.handle) {
      this.handle.unsubscribe()
      delete this.handle
    }
  }
}

/**
 * Possible states the websocket can be in.
 */
enum WebsocketState {
  CONNECTED,
  DISCONNECTED,
  ERROR
}

/**
 * Manage multiple subscriptions to different paths in a websocket.
 */
class PokerClient {
  private websocketPath: string
  private socket: webstomp.Client
  private subscriberMap: Map<string, Subscription>
  private state: WebsocketState
  private unsentMessages: Array<{path: string, payload: any}>

  /**
   * Creates a Stomp websocket client.
   * @param websocketPath - the path of the websocket to be connected to
   */
  constructor (websocketPath: string) {
    this.websocketPath = websocketPath
    this.socket = webstomp.client(websocketPath)
    this.subscriberMap = new Map()
    this.unsentMessages = []
    this.state = WebsocketState.DISCONNECTED

    const connectCallback = () => {
      this.state = WebsocketState.CONNECTED
      console.log('connected to websocket at %s', websocketPath)

      // Subscribe to all paths that were subscribed to while connecting
      this.subscriberMap.forEach((sub, key, map) => {
        sub.doSubscribe(this.socket)
      })

      // send all messages that were enqueued while we were connecting
      // do this after subscriptions, just in case we are waiting on a
      // response that is triggered due to a enqueued message.
      // TODO: check to see if rate limiting is required
      this.unsentMessages.forEach((element) => {
        this.socket.send(element.path, element.payload)
      })
    }

    // No error handling right now - if you fail to connect, you're just
    // fucked
    const errorCallback = () => {
      this.state = WebsocketState.ERROR
      console.log('failed to connect to websocket at %s', websocketPath)
    }

    this.socket.connect({}, connectCallback, errorCallback)
  }

  /**
   * True if the websocket has connected with the server
   */
  public connected (): boolean {
    return this.state === WebsocketState.CONNECTED
  }

  /**
   * True if the websocket connection failed
   */
  public errorOccurred (): boolean {
    return this.state === WebsocketState.ERROR
  }

  /**
   * Send the payload to the given path.
   * If the socket hasn't connected yet, enqueue it to be sent
   * on success
   * @param path - the path that will be posted to
   * @param payload - the payload that will be sent to the path
   */
  public send (path: string, payload: any) {
    if (this.connected()) {
      this.socket.send(path, JSON.stringify(payload))
    } else if (this.errorOccurred()) {
      console.log('Websocket(%s): socket in error state, cannot send data {path: %s, payload: %s}',
      this.websocketPath, path, payload)
    } else {
      console.log('Websocket(%s): socket not connected yet, enqueuing message', this.websocketPath)
      this.unsentMessages.push({ path, payload: JSON.stringify(payload) })
    }
  }

  /**
   * Subscribe to events from the given path.
   * @param path - the path to subscribe to
   * @param callback - the callback that will be run
   */
  public subscribeOn (path: string, callback: EventCallback) {
    let subscription = this.subscriberMap.get(path)
    if (!subscription) {
      subscription = new Subscription(path, this.socket)
      this.subscriberMap.set(path, subscription)
    }
    subscription.addCallback(callback)
  }

  /**
   * Remove a callback from the subscriber list for the given path.
   * @param path - the path that will be unsubscribed from
   * @param callback - the callback that will be unsubscribed
   */
  public unsubscribeOn (path: string, callback: EventCallback) {
    const subscription = this.subscriberMap.get(path)
    if (subscription) {
      subscription.removeCallback(callback)
      if (subscription.subscribers() === 0) {
        subscription.unsubscribeSocket()
        this.subscriberMap.delete(path)
      }
    }
  }

  /**
   * Unsubscribe from one path, and then resubscribe to another.
   * @param prevPath - the previous path that was subscribed to
   * @param prevCallback - the callback that was subscribed to prevPath
   * @param newPath - the new path that will be subscribed to
   * @param newCallback - the new callback that will be called upon
   * events at newPath
   */
  public switchSubscription (prevPath: string, prevCallback: EventCallback,
                             newPath: string, newCallback: EventCallback) {
    this.unsubscribeOn(prevPath, prevCallback)
    this.subscribeOn(newPath, newCallback)
  }

  /**
   * Move a callback from one path to another.
   * @param prevPath - the previous path that was subscribed to
   * @param newPath - the new path that will be subscribed to
   * @param callback - the previous callback
   */
  public switchPath (prevPath: string, newPath: string, callback: EventCallback) {
    this.switchSubscription(prevPath, callback, newPath, callback)
  }

  /**
   * Replace the callback on a path with another.
   * @param path - the path that prevCallback is subscribed to
   * @param prevCallback - the callback that was subscribed to
   * @param newCallback - the new callback that will replace the old callback
   */
  public switchCallback (path: string, prevCallback: EventCallback, newCallback: EventCallback) {
    this.switchSubscription(path, prevCallback, path, newCallback)
  }
}

export default new PokerClient('ws://localhost:8080/live')
