import PokerClient from '@/api/pokerclient'

/**
 * Helper class that will build the paths used to access a game
 * with the given id.
 */
class GamePaths {
  public GAME_UPDATES: string
  public GAME_EVENTS: string
  public GAME_ACTIONS: string
  /**
   * Generate the websocket paths used for the given gameID
   * @param gameId - the id of the game
   */
  constructor (gameId: number) {
    this.GAME_UPDATES = '/messages/game/' + gameId
    this.GAME_EVENTS = '/messages/game/' + gameId + '/events'
    this.GAME_ACTIONS = '/app/game/' + gameId + '/play'
  }
}

export interface GameState {
  pot: number,
  nextId: number
}

export enum GameEventType {
  GAME_STARTED = 'GAME_STARTED',
  GAME_FINISHED = 'GAME_FINISHED'
}

export interface GameEvent {
  event: GameEventType,
  payload: any
}

export enum GameActionType {
  BET = 'BET',
  CALL = 'CALL',
  CHECK = 'CHECK',
  FOLD = 'FOLD'
}

export interface GameAction {
  type: GameActionType,
  bet: number
}

export type GameUpdatedCallback = (newState: GameState) => void

export type GameEventCallback = (event: GameEvent) => void

/**
 * Manages all access to games on the server
 */
export class GameService {
  private gameId: number
  private gamePaths: GamePaths
  private onGameUpdatedCallback: GameUpdatedCallback
  private onGameEventCallback: GameEventCallback

  /**
   * Create a GameService to manage access to the game at gameId
   * @param gameId - the id of the game
   */
  constructor (gameId: number) {
    this.gameId = gameId
    this.gamePaths = new GamePaths(gameId)
    this.switchGame(gameId)
  }

  /**
   * Register a callback to be called when the game is updated
   * @param callback - will be called when the game updates
   */
  public onGameUpdated (callback: GameUpdatedCallback) {
    PokerClient.switchCallback(
      this.gamePaths.GAME_UPDATES,
      this.onGameUpdatedCallback,
      callback)
    this.onGameUpdatedCallback = callback
  }

  /**
   * Register a callback to be called when a game event is issued from the server
   * @param callback - will be called when a game event occurs
   */
  public onGameEvent (callback: GameEventCallback) {
    PokerClient.switchCallback(
      this.gamePaths.GAME_EVENTS,
      this.onGameEventCallback,
      callback)
    this.onGameEventCallback = callback
  }

  /**
   * Send an action to the server. (Note: this does not manage permissions)
   * @param {GameAction} action - the action that is being taken
   */
  public sendAction (action: GameAction) {
    PokerClient.send(this.gamePaths.GAME_ACTIONS, action)
  }

  /**
   * Switch the GameService to begin listening to a different game
   * @param {Integer} gameId - the id of the new game to be watched
   */
  public switchGame (gameId: number) {
    const newPaths = new GamePaths(gameId)

    PokerClient.switchPath(
      this.gamePaths.GAME_UPDATES,
      newPaths.GAME_UPDATES,
      this.onGameUpdatedCallback
    )
    PokerClient.switchPath(
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
  public finish () {
    PokerClient.unsubscribeOn(
      this.gamePaths.GAME_UPDATES,
      this.onGameUpdatedCallback
    )
    PokerClient.unsubscribeOn(
      this.gamePaths.GAME_EVENTS,
      this.onGameEventCallback
    )
  }
}