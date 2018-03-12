import PokerClient from '@/api/pokerclient'
/**
 * Helper class that will build the paths used to access a game
 * with the given id.
 */
export default class GamePaths {
  public GAME_UPDATES: string
  public GAME_FINISHED: string // Displays the winners and losers of the hand - and cards
  public GAME_ERROR: string
  public USER_CARDS: string
  public USER_ACTIONS: string

  /**
   * Generate the websocket paths used for the given gameID
   * @param gameId - the id of the game
   */
  constructor (gameId: number, userId?: number) {
    const messageGame = '/messages/game/'

    this.GAME_UPDATES = messageGame + gameId
    this.GAME_FINISHED = messageGame + gameId + '/status'

    this.GAME_ERROR = messageGame + gameId + '/error'

    this.USER_ACTIONS = '/app/game/' + gameId + '/play'

    this.USER_CARDS = userId ? messageGame + gameId + '/' + userId + '/cards' : ''
  }
}

/**
 * UserCards - which will hold the cards to the user
 */
export interface UserCards {
  card1: string
  card2: string
}

export interface GameFinished {
  winner: number
  time: number
}

/**
 * Game Error - which will hold the information when a game has an error
 */
export interface GameError {
  error: string
}

/**
 * GameState which will hold the state of the game
 */
export interface GameState {
  hasBet: boolean
  multiplePlayers: PlayerWithoutCards[]
  gameId: number
  pot: number
  communityCards: string[]
  gameStateType: GameStateType // This is every time that the GAME has UPDATED
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
  isTurn: boolean
  isPlayer: boolean
  isDealer: boolean
}

/**
 * Defines a Player without cards
 */
export interface PlayerWithoutCards {
  money: number
  id: number
  name: string
  action: GameAction | null
  currentBet: number
  isTurn: boolean
  isPlayer: boolean
  isDealer: boolean
}

/**
 * GameEventType always sends GameState objecct and determinds what ty
 */

export enum GameStateType {
  HAND_STARTED = 'HAND_STARTED', // USER Joins the GAME
  USER_ACTION = 'USER_ACTION', // This is sent after a player makes an action
  HAND_FINISHED = 'HAND_FINISHED', // Equivalent for a winnder being determined from a hand
  ROUND_FINISHED = 'ROUND_FINISHED', // All players have bet - this results in a new Community cards
  USER_JOIN = 'USER_JOIN',// A new player has been added to the game
  USER_LEAVE = 'USER_LEAVE' // A new player has left the game
}

/**
 * The Event the delivers the users cards
 * @event - deals users cards
 */
export enum UserEventType {
  USER_CARDS = 'USER_CARDS'
}
/**
 * Holds different types of Game Actions that a user can make
 */
export enum GameActionType {
  BET = 'BET',
  CALL = 'CALL',
  CHECK = 'CHECK',
  FOLD = 'FOLD',
  RAISE = 'RAISE'
}
/**
 * Holds Game Actions that a user can make
 */
export interface GameAction {
  type: GameActionType,
  bet: number
}

export type GameUpdatedCallback = (newState: GameState) => void
export type GameFinishedCallback = (gameFinished: GameFinished) => void
export type GameErrorCallback = (gameError: GameError) => void
export type UserCardsCallback = (userCards: UserCards) => void
export type UserActionsCallback = (action: GameAction) => void

/**
 * Manages all access to games on the server
 */
export class GameService {
  private gameId: number
  private gamePaths: GamePaths

  private onGameUpdatedCallback: GameUpdatedCallback
  private onGameFinishedCallback: GameFinishedCallback
  private onGameErrorCallback: GameErrorCallback
  private onUserEventCallback: UserCardsCallback
  private onUserActionsCallback: UserActionsCallback

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
   * Register a callback to be called when the hand is finished
   * @param callback - will be called when the hand is finished
   */
  public onGameFinished (callback: GameFinishedCallback) {
    PokerClient.switchCallback(
      this.gamePaths.GAME_FINISHED,
      this.onGameFinishedCallback,
      callback)
    this.onGameFinishedCallback = callback
  }

  /**
   * Register a callback to be called when the game returns and error
   * @param callback - will be called when an error is sent
   */
  public onGameError (callback: GameErrorCallback) {
    PokerClient.switchCallback(
      this.gamePaths.GAME_ERROR,
      this.onGameErrorCallback,
      callback)
    this.onGameErrorCallback = callback
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
  public userSendAction (action: GameAction) {
    PokerClient.send(this.gamePaths.USER_ACTIONS, action)
  }

  /**
   * Switch the GameService to begin listening to a different game
   * @param {Number} gameId - the id of the new game to be watched
   */
  public switchGame (gameId: number) {
    const newPaths = new GamePaths(gameId)

    PokerClient.switchPath(
      this.gamePaths.GAME_UPDATES,
      newPaths.GAME_UPDATES,
      this.onGameUpdatedCallback
    )

    PokerClient.switchPath(
      this.gamePaths.GAME_FINISHED,
      newPaths.GAME_FINISHED,
      this.onGameFinishedCallback
    )

    PokerClient.switchPath(
      this.gamePaths.GAME_ERROR,
      newPaths.GAME_ERROR,
      this.onGameError
    )

    PokerClient.switchPath(
      this.gamePaths.USER_CARDS,
      newPaths.USER_CARDS,
      this.onUserCards
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
    PokerClient.unsubscribeOn(this.gamePaths.GAME_UPDATES, this.onGameUpdatedCallback)
    PokerClient.unsubscribeOn(this.gamePaths.GAME_FINISHED, this.onGameFinished)

    PokerClient.unsubscribeOn(this.gamePaths.GAME_ERROR, this.onGameError)
    PokerClient.unsubscribeOn(this.gamePaths.USER_CARDS, this.onUserCards)

  }
}
