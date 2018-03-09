import PokerClient from '@/api/pokerclient'
/**
 * Helper class that will build the paths used to access a game
 * with the given id.
 */
export default class GamePaths {
  public GAME_UPDATES: string
  public GAME_EVENTS: string
  public GAME_ACTIONS: string
  public GAME_STARTED: string
  public GAME_FINISHED: string
  public GAME_ERROR: string
  public GAME_JOIN: string

  public USER_CARDS: string = ''

  /**
   * Generate the websocket paths used for the given gameID
   * @param gameId - the id of the game
   */
  constructor (gameId: number, userId?: number) {
    this.GAME_UPDATES = '/messages/game/' + gameId
    this.GAME_EVENTS = '/messages/game/' + gameId + '/events'
    this.GAME_ACTIONS = '/app/game/' + gameId + '/play'
    this.GAME_STARTED = '/messages/game/' + gameId + '/start'
    this.GAME_FINISHED = '/messages/game/' + gameId + '/finish'
    this.GAME_ERROR = '/messages/game/' + gameId + '/error'
    this.GAME_JOIN = '/messages/game/' + gameId + '/join'

    if (userId) {
      this.USER_CARDS = '/messages/game/' + gameId + '/' + userId + '/cards'
    }
  }
}

/**
 * UserCards - which will hold the cards to the user
 */
export interface UserCards {
  card1: string
  card2: string
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
  pot: number
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
  id: number
  name: string
  action: GameActionType | null
  card1: string
  card2: string
  currentBet: number
  isPlayer: boolean
  isDealer: boolean
}

export enum GameEventType {
  GAME_STARTED = 'GAME_STARTED',
  GAME_FINISHED = 'GAME_FINISHED',
  GAME_ERROR = 'GAME_ERROR'
}

export enum UserEventType {
  USER_CARDS = 'USER_CARDS'
}

export interface GameEvent {
  event: GameEventType,
  payload: any
}

export interface UserEvent {
  event: UserEventType,
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
export type GameFinishedCallback = (gameFinished: GameFinished) => void
export type GameStartedCallback = (gameStarted: GameStarted) => void
export type GameErrorCallback = (gameError: GameError) => void
export type GameEventCallback = (event: GameEvent) => void
export type UserCardsCallback = (userCards: UserCards) => void

/**
 * Manages all access to games on the server
 */
export class GameService {
  private gameId: number
  private gamePaths: GamePaths
  private onGameEventCallback: GameEventCallback
  private onGameUpdatedCallback: GameUpdatedCallback
  private onGameFinishedCallback: GameFinishedCallback
  private onGameStartedCallback: GameStartedCallback
  private onGameErrorCallback: GameErrorCallback
  private onUserEventCallback: UserCardsCallback

  /**
   * Create a GameService to manage access to the game at gameId
   * @param gameId - the id of the game
   */
  constructor (gameId: number, userId?: number) {
    this.gameId = gameId
    this.gamePaths = new GamePaths(gameId, userId)
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

  public onGameStarted (callback: GameStartedCallback) {
    PokerClient.switchCallback(
      this.gamePaths.GAME_STARTED,
      this.onGameStartedCallback,
      callback)
    this.onGameStartedCallback = callback

  }
  public onGameFinished (callback: GameFinishedCallback) {
    PokerClient.switchCallback(
      this.gamePaths.GAME_FINISHED,
      this.onGameFinishedCallback,
      callback)
    this.onGameFinishedCallback = callback
  }
  public onGameError (callback: GameEventCallback) {
    PokerClient.switchCallback(
      this.gamePaths.GAME_ERROR,
      this.onGameEventCallback,
      callback)
    this.onGameEventCallback = callback
  }
  /**
   * Register a callback to be called when the user is sent cards
   * @param callback - will be called when the user is sent cards
   */
  public onUserCards (callback: UserCardsCallback) {
    PokerClient.switchCallback(
      this.gamePaths.USER_CARDS,
      this.onUserEventCallback,
      callback)
    this.onUserEventCallback = callback
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
