/**
 * The game serivce wraps all communication over the websocket that relates to playing a
 * game.
 *
 * All objects that are communicated over the websocket have interfaces defined in this module.
 *
 * Currently the STOMP paths:
 * ```
 * /app/game/{id}/play recieves a GameAction
 * /messages/game/{id} sends a GameState
 * /messages/game/{id}/status sends a GameFinished
 * /messages/game/{id}/error sends a GameError
 * /messages/game/{id}/{userid}/cards sends the players cards
 * ```
 * have an interface provided.
 *
 * The GameService can be used by:
 * @example
 * ```typescript
 * import { GameService } from '@/api/gameservice'
 * function connectToGame(gameId: number): GameService {
 *   let gameService = new GameService(gameId)
 *   gameService.onGameUpdated(handleGameUpdated)
 *   gameService.onGameFinished(handleGameFinished)
 *   gameService.onGameError(handleGameError)
 *   gameService.onUserCards(handleUserCards)
 *   return gameService
 * }
 *
 * function exitGame(gameService: GameService) {
 *   gameService.finish()
 *   // ...handle other exit conditions
 * }
 * ```
 */

/**
 * The gameservice uses PokerClient to send and recieve
 * messages to the websocket.
 */
import PokerClient from '@/api/pokerclient.ts'
import { Card } from '@/types/cards.ts'

/**
 * GamePaths is a helper class that will build the paths used to access a game
 * with the given id.
 */
export class GamePaths {
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
    this.GAME_FINISHED = messageGame + gameId + 'status'

    this.GAME_ERROR = messageGame + gameId + 'error'

    this.USER_ACTIONS = '/app/game/' + gameId
    // this.destinationPrefix + user + destination
    this.USER_CARDS = '/user' + messageGame + gameId

  }
}

/**
 * UserCards - which will hold the cards to the user
 */
export interface UserCards {
  card1: Card
  card2: Card
}

/**
 * The GameFinished interface is sent whenever a hand is finished.
 */
export interface GameFinished {
  multiplePlayers: Player
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
  multiplePlayers: PlayerWithoutCards[]
  nextPlayer: number // Players who turn it is
  bigBlind: number
  potSum: number
  communityCards: Card[]
  event: Event // This is every time that the GAME has UPDATED
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
  isFold: boolean
  isTurn: boolean
  winnings: number
}

/**
 * Defines a Player without cards
 */
export interface PlayerWithoutCards {
  money: number
  playerID: number // Location within the array
  name: string
  action: GameAction | null
  currentBet: number // Total amount of money that has been transfered
  isPlayer: boolean
  isDealer: boolean
  isFold: boolean
}

/**
 * GameStateType is sent with a GameState object to tell the client why the GameState
 * was sent.
 */
export enum Event {
  GAME_STARTED = 'GAME_STARTED', // Game should be startable and give hands.
  HAND_STARTED = 'HAND_STARTED', // USER Joins the GAME
  USER_ACTION = 'PLAYER_ACTION', // This is sent after a player makes an action
  HAND_FINISHED = 'HAND_FINISHED', // Equivalent for a winnder being determined from a hand
  ROUND_FINISHED = 'ROUND_FINISHED', // All players have bet - this results in a new Community cards
  USER_JOIN = 'PLAYER_JOIN',// A new player has been added to the game
  USER_LEAVE = 'PLAYER_LEAVE' // A new player has left the game
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

/**
 * The GameUpdatedCallback is the type of function that will
 * be called when the GameService recieves a GameState
 */
export type GameUpdatedCallback = (newState: any) => void

/**
 * The GameFinishedCallback is the type of function that will be
 * called when the GameService recieves a GameFinished update
 */
export type GameFinishedCallback = (gameFinished: GameFinished) => void

/**
 * The GameErrorCallback is the type of function that will be
 * called when the GameService recieves a GameError update
 */
export type GameErrorCallback = (gameError: GameError) => void

/**
 * The GameFinishedCallback is the type of function that will be
 * called when the GameService recieves the users cards
 */
export type UserCardsCallback = (userCards: any) => void

/**
 * The GameService manages access to a games events on the server.
 */
export class GameService {
  public gamePaths: GamePaths

  private gameId: number
  private onGameUpdatedCallback: GameUpdatedCallback | null = null
  private onGameFinishedCallback: GameFinishedCallback | null = null
  private onGameErrorCallback: GameErrorCallback | null = null
  private onUserCardsCallback: UserCardsCallback | null = null

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
      this.onUserCardsCallback,
      callback)
    this.onUserCardsCallback = callback
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
      this.onGameErrorCallback
    )

    PokerClient.switchPath(
      this.gamePaths.USER_CARDS,
      newPaths.USER_CARDS,
      this.onUserCardsCallback
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
    PokerClient.unsubscribeOn(this.gamePaths.GAME_FINISHED, this.onGameFinishedCallback)

    PokerClient.unsubscribeOn(this.gamePaths.GAME_ERROR, this.onGameErrorCallback)
    PokerClient.unsubscribeOn(this.gamePaths.USER_CARDS, this.onUserCards)

  }
}

export default GameService
