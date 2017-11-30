import Websocket from '@/api/websocket.js'

/**
 * Helper class that will build the paths used to access a game
 * with the given id.
 */
class GamePaths {
  /**
   * Generate the websocket paths used for the given gameID
   * @param {Integer} gameId - the id of the game
   */
  constructor (gameId) {
    this.GAME_UPDATES = '/messages/game/' + gameId
    this.GAME_EVENTS = '/messages/game/' + gameId + '/events'
    this.GAME_ACTIONS = '/app/game/' + gameId + '/play'
  }
}

/**
 * Manages all access to games on the server
 */
class GameService {

  /**
   * Create a GameService to manage access to the game at gameId
   * @param {Integer} gameId - the id of the game
   */
  constructor (gameId) {
    this.gameId = gameId
    this.gamePaths = new GamePaths(gameId)
    this.switchGame(gameId)
  }

  /**
   * Callback used to notify when the GameState has changed
   * @callback gameStateCallback
   * @param {GameState} - the new game state
   */

  /**
   * Register a callback to be called when the game is updated
   * @param {gameStateCallback} callback
   */
  onGameUpdated (callback) {
    Websocket.switchCallback(
      this.gamePaths.GAME_UPDATES,
      this.onGameUpdatedCallback,
      callback)
    this.onGameUpdatedCallback = callback
  }

  /**
   * Callback used to notify when a GameEvent has occurred
   * @callback gameEventCallback
   * @param {GameEvent} - the event that occurred
   */

  /**
   * Register a callback to be called when a game event is issued from the server
   * @param {gameEventCallback} callback
   */
  onGameEvent (callback) {
    Websocket.switchCallback(
      this.gamePaths.GAME_EVENTS,
      this.onGameEventCallback,
      callback)
    this.onGameEventCallback = callback
  }

  /**
   * Send an action to the server. (Note: this does not manage permissions)
   * @param {GameAction} action - the action that is being taken
   */
  sendAction (action) {
    Websocket.send(this.gamePaths.GAME_ACTIONS, action)
  }

  /**
   * Switch the GameService to begin listening to a different game
   * @param {Integer} gameId - the id of the new game to be watched
   */
  switchGame (gameId) {
    var newPaths = new GamePaths(gameId)

    Websocket.switchPath(
      this.gamePaths.GAME_UPDATES,
      newPaths.GAME_UPDATES,
      this.onGameUpdatedCallback
    )
    Websocket.switchPath(
      this.gamePaths.GAME_EVENTS,
      newPaths.GAME_EVENTS,
      this.onGameUpdatedCallback
    )

    this.gamePaths = newPaths
    this.gameId = gameId
  }

  /**
   * Stop listening for events.
   */
  finish () {
    Websocket.unsubscribeOn(
      this.gamePaths.GAME_UPDATES,
      this.onGameUpdatedCallback
    )
    Websocket.unsubscribeOn(
      this.gamePaths.GAME_EVENTS,
      this.onGameEventCallback
    )
  }
}

export default GameService
