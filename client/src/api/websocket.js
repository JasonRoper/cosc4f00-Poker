import webstomp from 'webstomp-client'

/**
 * Callback used to listen for events on a stomp endpoint
 *
 * @callback eventCallback
 * @param {*} payload - the value that was posted to the stomp endpoint
 */

/**
 * Used to manage multiple subscriptions to the websocket.
 */
class Subscription {

  /**
   * Create a Subscription to the given path. If the socket has not connected
   * {@Link Subscription#doSubscribe} will need to be called with the socket
   * that will be subscribed to.
   * @param {String} path - the path that this subscription will be managing
   * @param {webstomp.Client} socket - the socket that will be subscribed to
   */
  constructor (path, socket) {
    this.path = path
    this.callbacks = new Set()
    this.doSubscribe(path, socket)
  }

  /**
   * If the Subscription has not already subscribed to the websocket,
   * try to subscribe again
   * @param {webstomp.Client} socket - the socket that will be subscribed to
   */
  doSubscribe (socket) {
    if (this.handle) {
      return
    }

    const callback = (payload) => {
      this.callbacks.forEach(callback => callback(payload))
    }
    if (socket.connected) {
      this.handle = socket.subscribe(this.path, callback)
    }
  }

  /**
   * Add a callback to the path that this subscription is listening to.
   * @param {eventCallback} callback - the callback that will be added to the subscription list
   */
  addCallback (callback) {
    this.callbacks.add(callback)
  }

  /**
   * Remove a callback that was listening to this subscription
   * @param {eventCallback} callback - the callback that will be removed from the subscription list
   */
  removeCallback (callback) {
    this.callbacks.delete(callback)
  }

  /**
   * @returns {Number} - the number of callbacks that are currently subscribed to this subscription
   */
  subscribers () {
    return this.callbacks.size()
  }

  /**
   * Send a message to all subscribers to this Subscription.
   * @param {*} payload - the message to be sent to all callbacks listening to this subscription
   */
  notifyAll (payload) {
    this.callbacks.forEach(callback => callback(payload))
  }

  /**
   * Unsubscribe from the websocket. {@Link Subscription#doSubscribe} will need to be called
   * to resubscribe to a websocket.
   */
  unsubscribeSocket () {
    if (this.handle) {
      this.handle.unsubscribe()
      delete this.handle
    }
  }
}

/**
 * Manage multiple subscriptions to different paths in a websocket.
 */
class Websocket {

  /**
   * Possible states the websocket can be in.
   */
  WebsocketState = {
    CONNECTED: 'CONNECTED',
    DISCONNECTED: 'DISCONNECTED',
    ERROR: 'ERROR'
  }

  /**
   * Creates a Stomp websocket client.
   * @param {String} websocketPath - the path of the websocket to be connected to
   */
  constructor (websocketPath) {
    this.websocketPath = websocketPath
    this.socket = webstomp.client(websocketPath)
    this.subscriberMap = new Map()
    this.unsentMessages = []
    this.state = this.WebsocketState.DISCONNECTED

    const connectCallback = () => {
      this.state = this.WebsocketState.CONNECTED
      console.log('connected to websocket at %s', websocketPath)

      // send all messages that were enqueued while we were connecting
      // TODO: check to see if rate limiting is required
      this.unsentMessages.forEach(element => {
        this.socket.send(element.path, element.payload)
      })

      // Subscribe to all paths that were subscribed to while connecting
      this.subscriberMap.forEach((sub, key, map) => {
        sub.doSubscribe(this.socket)
      })
    }

    // No error handling right now - if you fail to connect, you're just
    // fucked
    const errorCallback = () => {
      this.state = this.WebsocketState.ERROR
      console.log('failed to connect to websocket at %s', websocketPath)
    }

    this.socket.connect({}, connectCallback, errorCallback)
  }

  /**
   * True if the websocket has connected with the server
   */
  connected () {
    return this.state === this.WebsocketState.CONNECTED
  }

  /**
   * True if the websocket connection failed
   */
  errorOccurred () {
    return this.state === this.WebsocketState.ERROR
  }

  /**
   * Send the payload to the given path.
   * If the socket hasn't connected yet, enqueue it to be sent
   * on success
   * @param {String} path - the path that will be posted to
   * @param {*} payload - the payload that will be sent to the path
   */
  send (path, payload) {
    if (this.connected()) {
      this.socket.send(path, payload)
    } else if (this.errorOccurred) {
      console.log('Websocket(%s): socket in error state, cannot send data {path: %s, payload: %s}',
        this.websocketPath, path, payload)
    } else {
      console.log('Websocket(%s): socket not connected yet, enqueuing message', this.websocketPath)
      this.unsentMessages.push({path: path, payload: payload})
    }
  }

  /**
   * Subscribe to events from the given path.
   * @param {String} path - the path to subscribe to
   * @param {eventCallback} callback - the callback that will be run
   */
  subscribeOn (path, callback) {
    var subscription = this.subscriberMap.get(path)
    if (!subscription) {
      subscription = new Subscription(path, this.socket)
      this.subscriberMap.set(path, subscription)
    }
    subscription.addCallback(callback)
  }

  /**
   * Remove a callback from the subscriber list for the given path.
   * @param {String} path - the path that will be unsubscribed from
   * @param {eventCallback} callback - the callback that will be unsubscribed
   */
  unsubscribeOn (path, callback) {
    var subscription = this.subscriberMap.get(path)
    if (subscription) {
      subscription.removeCallback(callback)
      if (subscription.callbacks() === 0) {
        subscription.unsubscribeSocket()
        this.subscriberMap.delete(path)
      }
    }
  }

  /**
   * Unsubscribe from one path, and then resubscribe to another.
   * @param {String} prevPath - the previous path that was subscribed to
   * @param {eventCallback} prevCallback - the callback that was subscribed to prevPath
   * @param {String} newPath - the new path that will be subscribed to
   * @param {eventCallback} newCallback - the new callback that will be called upon
   * events at newPath
   */
  switchSubscription (prevPath, prevCallback, newPath, newCallback) {
    this.unsubscribeOn(prevPath, prevCallback)
    this.subscribeOn(newPath, newCallback)
  }

  /**
   * Move a callback from one path to another.
   * @param {String} prevPath - the previous path that was subscribed to
   * @param {String} newPath - the new path that will be subscribed to
   * @param {eventCallback} callback - the previous callback
   */
  switchPath (prevPath, newPath, callback) {
    this.switchSubscription(prevPath, callback, newPath, callback)
  }

  /**
   * Replace the callback on a path with another.
   * @param {String} path - the path that prevCallback is subscribed to
   * @param {eventCallback} prevCallback - the callback that was subscribed to
   * @param {eventCallback} newCallback - the new callback that will replace the old callback
   */
  switchCallback (path, prevCallback, newCallback) {
    this.switchSubscription(path, prevCallback, path, newCallback)
  }
}

export default new Websocket('ws://localhost:8080/live')
