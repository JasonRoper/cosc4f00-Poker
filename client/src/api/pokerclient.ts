/**
 * PockerClient is connects to the websocket and provides an interface that
 * allows any number of subscriptions to any number of STOMP paths. When a
 * message is recieved on a path, every subscriber is messaged with the object
 * that was received.
 *
 * PokerClient is a singleton. It is used by importing this module as default.
 * @example
 * ```typescript
 * import PokerClient from '@/api/pokerclient'
 *
 * PokerClient.subscribeOn("/a/STOMP/path", function(result) {
 *   console.log("A message was received: ", result)
 *   handleResult(result)
 * })
 *
 * PokerClient.send("/another/STOMP/path", {message: "this is a message"})
 * ```
 *
 * Various helper methods are provided to allow easy switching between paths,
 * and swapping out subscribing functions.
 */

/**
 * The path that we connect to is defined in the frontend configuration.
 */
import { WEBSOCKET_PATH } from '@/config'
import webstomp from 'webstomp-client'

type EventCallback = (payload: any) => void

/**
 * Subscription is used to manage multiple subscriptions to the websocket.
 */
class Subscription {
  private path: string
  private callbacks: Set<EventCallback>
  private handle: webstomp.Subscription | undefined

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
  CONNECTED = 'CONNECTED',
  DISCONNECTED = 'DISCONNECTED',
  ERROR = 'ERROR'
}

/**
 * PokerHeaders contains all of the context information needed to be sent along
 * with a message through the websocket
 */
class PokerHeaders {
  public user: string

  /**
   * Create a new PokerHeaders with the given username
   * @param username - the username to send to the server
   */
  public constructor (username: string) {
    this.user = username
  }
}

/**
 * Manage multiple subscriptions to different paths in a websocket.
 */
class PokerClient {
  private websocketPath: string
  private socket: webstomp.Client
  private subscriberMap: Map<string, Subscription>
  private state: WebsocketState
  private unsentMessages: Array<{ path: string, payload: any }>
  private headers: PokerHeaders | undefined

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
    this.headers = undefined
    this.connect()
  }

  /**
   * connect is a helper function that will connet to the websocket.
   */
  public connect (doAfter?: (frame: webstomp.Frame | undefined) => void) {
    // if we're already connected, do nothing
    if (this.connected()) {
      if (doAfter) doAfter(undefined)
      return
    }

    const connectCallback = (response: webstomp.Frame | undefined) => {
      this.state = WebsocketState.CONNECTED
      console.log('connected to websocket at %s', this.websocketPath)

      // Subscribe to all paths that were subscribed to while connecting
      this.subscriberMap.forEach((sub, key, map) => {
        sub.doSubscribe(this.socket)
      })

      // send all messages that were enqueued while we were connecting
      // do this after subscriptions, just in case we are waiting on a
      // response that is triggered due to a enqueued message.
      // TODO: check to see if rate limiting is required
      this.unsentMessages.forEach((element) => {

        this.socket.send(element.path, element.payload, { ...(this.headers) })
      })

      if (doAfter) doAfter(response)
    }

    // No error handling right now - if you fail to connect, you're just
    // fucked
    const errorCallback = () => {
      this.state = WebsocketState.ERROR
      console.log('failed to connect to websocket at %s', this.websocketPath)
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
   * disconnect the websocket.
   *
   * @param doAfter the function that will be called after the client has disconnected
   */
  public disconnect (doAfter: () => void) {
    this.state = WebsocketState.DISCONNECTED
    this.socket.disconnect(doAfter)
  }

  /**
   * Reconnect with the websocket.
   */
  public reconnect () {
    this.disconnect(() => {
      this.socket = webstomp.client(this.websocketPath)
      this.connect()
    })
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
      this.socket.send(path, JSON.stringify(payload), { ...(this.headers) })
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
  public unsubscribeOn (path: string, callback: EventCallback | null) {
    const subscription = this.subscriberMap.get(path)
    if (subscription) {
      if (callback !== null) {
        subscription.removeCallback(callback)
      }
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
  public switchSubscription (
    prevPath: string,
    prevCallback: EventCallback | null,
    newPath: string,
    newCallback: EventCallback | null) {

    if (prevCallback !== null) {
      this.unsubscribeOn(prevPath, prevCallback)
    }
    if (newCallback !== null) {
      this.subscribeOn(newPath, newCallback)
    }
  }

  /**
   * Move a callback from one path to another.
   * @param prevPath - the previous path that was subscribed to
   * @param newPath - the new path that will be subscribed to
   * @param callback - the previous callback
   */
  public switchPath (prevPath: string, newPath: string, callback: EventCallback | null) {
    if (callback !== null) {
      this.switchSubscription(prevPath, callback, newPath, callback)
    }
  }

  /**
   * Replace the callback on a path with another.
   * @param path - the path that prevCallback is subscribed to
   * @param prevCallback - the callback that was subscribed to
   * @param newCallback - the new callback that will replace the old callback
   */
  public switchCallback (path: string, prevCallback: EventCallback | null, newCallback: EventCallback | null) {
    this.switchSubscription(path, prevCallback, path, newCallback)
  }
}

export default new PokerClient(WEBSOCKET_PATH)
