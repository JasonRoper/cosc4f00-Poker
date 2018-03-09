
import {
  GameAction,
  GameActionType,
  GameError,
  GameFinished,
  GameService,
  // GameStarted,
  GameState,
  Player,
  UserCards
  // GameEventType
} from '@/api/gameservice'

import actions from '@/types/actions'
import CardSuite from '@/types/cards'
import { Action } from 'vuex'

export default class GameMech {
  public gameId: number

  // Defines how many players can be at a table
  public numberOfPlayers: number = 5

  public hasBet: boolean = false
  public turn: number = 0
  public multiplePlayers: Player[] = []
  public pot: number = 0
  public communityCards: string[] = []
  public userId: number | null
  public playerLocation: number = -1
  public userAction: GameAction | null = null

  public tableAction: GameActionType[] = []

  public endGame: boolean = false

  public disable: number = 1
  public enableButton: number = 0
  public foldAction: number = 0
  public callAction: number = 0
  public betAction: number = 0
  public raiseAction: number = 0
  public checkAction: number = 0
  public possibleAction: GameActionType [][] = [[GameActionType.BET, GameActionType.FOLD, GameActionType.CHECK],
    [GameActionType.CALL, GameActionType.RAISE, GameActionType.FOLD], [GameActionType.FOLD]]

  private lobby: boolean
  private gameService: GameService

  /**
   * Creates a Game Mechanics that the user uses to play the game
   * @class Creates Game Mechanics class
   * @param gameId attaches user to listen to gameId
   * @param userId Creates table for user or for a lobby
   */
  constructor (gameId: number, userId?: number) {
    this.gameService = new GameService(gameId, userId)
    this.gameId = gameId

    this.userId = userId ? userId : null

    this.gameService.onGameUpdated(this.setGameTransport)
    // this.gameService.onGameFinished(this.onGameFinishedEvent)
    // this.gameService.onGameStarted(this.onGameStartedEvent)
    this.gameService.onGameError(this.onGameError)
    /*
    public GAME_UPDATES: string
    public GAME_FINISHED: string // Displays the winners and losers of the hand - and cards

    public GAME_ERROR: string
    public USER_CARDS: string
    public USER_ACTIONS: string
    */
    if (userId) {
      this.gameService.onUserCards(this.onUserCardsEvent)
      this.lobby = false
    } else {
      this.gameService.onGameUpdated(this.setGameTransport)
      this.lobby = true
    }

    this.setDefaultTransport()
  }

  /**
   * Sets a default table for the player
   * @returns Default Player Object
   */
  public setDefaultTransport () {
    this.hasBet = false
    this.turn = 0
    this.multiplePlayers = [{
      id: 0,
      money: 500,
      name: 'javon',
      action: GameActionType.CALL,
      card1: CardSuite.BLANK_CARD,
      card2: CardSuite.BLANK_CARD,
      currentBet: 600,
      isPlayer: true,
      isDealer: true
    }, {
      id: 1,
      money: 888,
      name: 'test',
      action: null,
      card1: CardSuite.CLUBS_ACE,
      card2: CardSuite.CLUBS_TWO,
      currentBet: 0,
      isPlayer: false,
      isDealer: false
    }, this.defaultPlayer(), this.defaultPlayer() ,this.defaultPlayer(),this.defaultPlayer()]
    this.gameId = 0
    this.pot = 0
    this.communityCards = [CardSuite.CLUBS_ACE, CardSuite.CLUBS_EIGHT, 'three', 'four', 'five']
    this.userId = 0
    this.playerLocation = this.playerLoc()
    this.userAction = null
  }

  /**
   * Sets a default player
   */
  public defaultPlayer (): Player {
    const player: Player = {
      id: -1,
      money: 0,
      name: 'defaultPlayer',
      action: null,
      card1: CardSuite.BLANK_CARD,
      card2: CardSuite.BLANK_CARD,
      currentBet: 0,
      isPlayer: false,
      isDealer: false
    }
    return player
  }

  /**
   * Gets Player location within array
   * @returns player location in the Multiplayer Array
   */
  public playerLoc (): number {
    for (const play of this.multiplePlayers) {
      if (play.id === this.userId) {
        return play.id
      }
    }
    return -1
  }

  /**
   * @returns The possible table actions that the user can use
   */
  public getTableAction () {
    return this.tableAction
  }

  /**
   * Handles Game Started Event
   * @event GameStartedEvent
   * @param GameStarted the information that you need to start a game
   * public onGameStartedEvent (gameStarted: GameStarted) {
   * this.multiplePlayers = gameStarted.multiplePlayers
   * }
   */

  /**
   * Handles Game finished event
   * @event GameFinishedEvent
   * @param gameFinished The inforamtion that you need to finish a game
   */
  public onGameFinishedEvent (gameFinished: GameFinished) {
    for (const player of this.multiplePlayers) {
      if (player.id === gameFinished.winner) {
        alert('You are the winner')
      } else {
        alert('You are the loser')
      }
    }
    this.endGame = true
    return this.endGame
  }

  /**
   * Handles when a Community Card is sent to table
   * @event GameCommunityCardsEvent
   * @param card The card that is added to the Community Cards
   */
  public onGameCommunityCardsEvent (card: string) {
    this.communityCards.push(card)
  }

  /**
   * Handles when a Game Error is sent
   * @event ErrorEvent
   * @param gameError When the game Sends and Error
   */
  public onGameError (gameError: GameError) {
    alert('Error: ' + gameError.error)
    console.log('Error: ' + gameError.error)
  }

  /**
   * Handles when user receives cards
   * @event UserCardsEvent
   * @param userCards
   */
  public onUserCardsEvent (userCards: UserCards) {
    this.multiplePlayers[this.playerLocation].card1 = userCards.card1
    this.multiplePlayers[this.playerLocation].card2 = userCards.card2
  }

  /**
   * Adds cards to the Comminity Cards
   * @param card
   */
  public onGameComminityCardsEvent (card: string) {
    if (this.communityCards.length <= 5) {
      this.communityCards.push(card)
    }
  }

  /**
   * Stores the Premove for the user
   * @param action - holds users GameActionType
   * @param money - holds how much money the user bet with a particular action
   * @returns validity of Action that user is attempting to store
   */
  public storePremove (action: GameActionType, money: number): boolean {
    const move: GameAction = { type: action, bet: money }
    if (this.validatePreMove(move) && this.userAction === null) {
      this.disableButton(action)
      this.userAction = move
      return true
    } else {
      this.setTableActions()
      this.userAction = null
      return false
    }
  }

  /**
   * Disables the TableActions based on a users previous action
   * @param action holds the action of the user
   */
  public disableButton (action: GameActionType) {
    switch (action) {
      case GameActionType.CHECK:
        this.checkAction = this.disable
        break
      case GameActionType.BET:
        this.betAction = this.disable
        break
      case GameActionType.CALL:
        this.callAction = this.disable
        break
      case GameActionType.RAISE:
        this.raiseAction = this.disable
        break
      case GameActionType.FOLD:
        this.foldAction = this.disable
        break
      default:
        alert('disable Button went wrong')
    }
  }
  /**
   * Disables specific tableActions based on if there has been a bet at the table
   */
  public setTableActions () {
    if (this.hasBet) {
      this.foldAction = 0
      this.betAction = 1
      this.checkAction = 1
      this.callAction = 0
      this.raiseAction = 0
    } else {
      this.foldAction = 0
      this.betAction = 0
      this.checkAction = 0
      this.callAction = 1
      this.raiseAction = 1
    }
  }

  /**
   * ValidatePreMove
   */
  public validatePreMove (move: GameAction) {
    // Confirm that you have enough money
    if (this.multiplePlayers[this.playerLocation].money < move.bet) {
      alert('you do not have enough money')
      return false
    }

    // Confirm that you made a valid move
    if (this.hasBet) {
      if (this.possibleAction[1].indexOf(move.type) === -1) {
        alert('you have not made a valid move')
        return false
      }
    } else {
      if (this.possibleAction[0].indexOf(move.type) === -1) {
        alert('you have not made a valid move')
        return false
      }
    }
    return true
  }

  /**
   * Validates users action and sends it to the server
   */
  public sendAction () {
    if (this.userAction !== null) {
      if (this.storePremove(this.userAction.type, this.userAction.bet)) {
        if (this.playerLocation === this.turn) {
          this.send(this.userAction)
          this.userAction = null
        }
      }
    }
  }

  /**
   *  Sets the Game Transport in the Game Mechanics
   * @param GameState
   */
  public setGameTransport (gameTransport: GameState) {
    switch (gameTransport.gameEventType) {
      /*
      case GameEventType.HAND_STARTED: { break }
      case GameEventType.USER_ACTION: { break }
      case GameEventType.HAND_FINISHED: { break }
      case GameEventType.ROUND_FINISHED: { break }
      case GameEventType.USER_JOIN: { break }
      case GameEventType.USER_LEAVE: { break }
      */
    }

    this.communityCards = gameTransport.communityCards
    this.hasBet = gameTransport.hasBet
    this.turn = gameTransport.turn

    Object.assign(this.multiplePlayers, gameTransport.multiplePlayers)

    this.pot = gameTransport.pot
    // if there is a premove
    if (this.playerLocation === this.turn) {
      if (this.userAction !== null) {
        if (this.validatePreMove(this.userAction)) {
          const action = (this.userAction).type
          const bet = (this.userAction).bet
          this.sendAction()
          alert('send the action')
        }
      }
    }
    this.setTableActions()

  }

  public isGameRunning () {
    return this.gameService.connected()
  }
  // can this game accecpt action from this player
  private send (gameAction: GameAction) {
    this.gameService.onUserSendAction(gameAction)
  }

}
