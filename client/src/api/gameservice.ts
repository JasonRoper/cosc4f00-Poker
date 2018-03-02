import PokerClient from '@/api/pokerclient'

/**
 * Helper class that will build the paths used to access a game
 * with the given id.
 */
class GamePaths {
  public GAME_UPDATES: string
  public GAME_EVENTS: string
  public GAME_ACTIONS: string
  public GAME_STARTED: string
  public GAME_FINISHED: string
  public GAME_ERROR: string
  /**
   * Generate the websocket paths used for the given gameID
   * @param gameId - the id of the game
   */
  constructor (gameId: number) {
    this.GAME_UPDATES = '/messages/game/' + gameId
    this.GAME_EVENTS = '/messages/game/' + gameId + '/events'
    this.GAME_ACTIONS = '/app/game/' + gameId + '/play'
    this.GAME_STARTED = '/messages/game/' + gameId + '/start'
    this.GAME_FINISHED = '/messages/game/' + gameId + '/finish'
    this.GAME_ERROR = '/messages/game/' + gameId + '/error'
  }
}
/**
 * GameStateStarted - which will hold the information when a new game has started
 */
export interface GameStarted {
  multiplePlayers: Player[]
  time: number
}

/**
 * Game Error - which will hold the information when a game has an error
 */
export interface GameError {
  error: string
}

/**
 * Game Finished - which will hold the information when a game has ended
 */
export interface GameFinished {
  winner: number
  time: number
}

/**
 * GameState which will hold the state of the game
 */
export interface GameState {
  hasBet: boolean
  turn: number
  multiplePlayers: Player[]
  gameId: number
  valid: boolean
  pot: Pot []
  deck: Card[] | null
  communityCards: string[]
}
/**
 * Defines a Card Object
 */
export interface Card {
  suit: string
}

/**
 * Defines a Player Object
 */
export interface Player {
  money: number
  id: number | null
  name: string
  tableAction: GameActionType[] | GameActionType
  premove: GameAction | null
  card1: string | null
  card2: string | null
  playing: boolean
  isDealer: boolean
}
/**
 * Defines the Pot object
 */
export interface Pot {
  pot: number
}

export enum GameEventType {
  GAME_STARTED = 'GAME_STARTED',
  GAME_FINISHED = 'GAME_FINISHED',
  GAME_ERROR = 'GAME_ERROR'
}

export interface GameEvent {
  event: GameEventType,
  payload: any
}

export enum GameActionType {
  BET = 'BET',
  CALL = 'CALL',
  CHECK = 'CHECK',
  FOLD = 'FOLD',
  RAISE = 'RAISE'
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

  public onGameStarted (callback: GameEventCallback) {
    PokerClient.switchCallback(
      this.gamePaths.GAME_STARTED,
      this.onGameEventCallback,
      callback)
    this.onGameEventCallback = callback

  }
  public onGameFinished (callback: GameEventCallback) {
    PokerClient.switchCallback(
      this.gamePaths.GAME_FINISHED,
      this.onGameEventCallback,
      callback)
    this.onGameEventCallback = callback
  }
  public onGameError (callback: GameEventCallback) {
    PokerClient.switchCallback(
      this.gamePaths.GAME_ERROR,
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

  public connected () {
    return PokerClient.connected()
  }

  /**
   * Stop listening for events.
   */
  public finish () {
    PokerClient.unsubscribeOn(this.gamePaths.GAME_UPDATES,this.onGameUpdatedCallback)
    PokerClient.unsubscribeOn(this.gamePaths.GAME_EVENTS,this.onGameEventCallback)
  }

}
