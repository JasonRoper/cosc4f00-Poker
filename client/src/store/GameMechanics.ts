/**
 * Game Mechanics Class Requests a TableView
 * A game is joined through subscribing to the necessary STOMP endpoints.
 * The Game Mechanics provides all of the information necessary to show a table
 * Through the subscriptions from game Mechanics it receives new states and can
 * send user actions.
 */

/**
 * GameMechanics uses Game service to send and receive updates
 */
import {
  Event,
  GameAction,
  GameActionType,
  GameError,
  GameFinished,
  GameService,
  GameState,
  Player,
  UserCards
} from '@/api/gameservice'

/**
 * Import Card Suite that holds all Card Varients
 */
import { Card } from '@/types/cards'
import { Action } from 'vuex'

/**
 * Manages all interactions between the game and the server
 * @class - Game Mechanics Class
 */
export default class GameMech {
  public gameId: number

  // Defines how many players can be at a table
  public numberOfPlayers: number = 5
  public bigBlind: number = 0
  public hasBet: boolean = false
  public turn: number = 0
  public multiplePlayers: Player[] = []
  public potSum: number = 0
  public communityCards: string[] = []
  public userId: number | null
  public userAction: GameAction | null = null

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
  public location: number
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
    this.setDefaultTransport()
    this.gameId = gameId
    this.userId = userId ? userId : null
    this.location = Math.round(Math.random() * 5)
    // alert('UserID ' + this.location)
    this.gameService.onGameUpdated(this.setGameTransport.bind(this))
    this.gameService.onGameFinished(this.onGameFinishedEvent.bind(this))
    this.gameService.onGameError(this.onGameError.bind(this))

    // Verifies if a user Id was passed
    if (userId) {
      // Allows user to receive cards
      this.gameService.onUserCards(this.onUserCardsEvent.bind(this))
      this.lobby = false
    } else {
      this.lobby = true
    }
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
      name: '3333333',
      action: GameActionType.CALL,
      card1: Card.BLANK_CARD,
      card2: Card.BLANK_CARD,
      currentBet: 600,
      isPlayer: true,
      isDealer: true,
      isFold: true,
      winnings: 0
    }, {
      id: 1,
      money: 888,
      name: 'test',
      action: null,
      card1: Card.CLUBS_ACE,
      card2: Card.CLUBS_TWO,
      currentBet: 0,
      isFold: false,
      winnings: 0,
      isPlayer: false,
      isDealer: false
    }, this.defaultPlayer(), this.defaultPlayer() ,this.defaultPlayer(),this.defaultPlayer()]
    this.gameId = 0
<<<<<<< Updated upstream
    this.pot = 0
    this.communityCards = [Card.CLUBS_ACE, Card.CLUBS_EIGHT, 'three', 'four', 'five']
    this.userId = 0
=======
    this.potSum = 0
    this.communityCards = [Card.CLUBS_ACE, Card.CLUBS_EIGHT, 'three', 'four', 'five']
>>>>>>> Stashed changes
    this.userAction = null
  }

  /**
   * Sets a default player
   * @returns Default Player object
   */
  public defaultPlayer (): Player {
    const player: Player = {
      id: -1,
      money: 0,
      name: 'defaultPlayer',
      action: null,
      card1: Card.BLANK_CARD,
      card2: Card.BLANK_CARD,
      currentBet: 0,
      isFold: false,
      winnings: 0,
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
   * Handles Game finished event
   * @event GameFinishedEvent
   * @param gameFinished The inforamtion that you need to finish a game
   */
  public onGameFinishedEvent (gameFinished: GameFinished) {
    Object.assign(this.multiplePlayers, gameFinished.multiplePlayers)
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

    this.multiplePlayers[this.playerLoc()].card1 = userCards.card1
    this.multiplePlayers[this.playerLoc()].card2 = userCards.card2
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
      alert('Premove was validated')
      this.disableButton(action)
      this.userAction = move
      this.sendAction()
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
    if (this.multiplePlayers[this.playerLoc()].money < move.bet) {
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
   *  Sets the Game Transport in the Game Mechanics
   * @param GameState
   */
  public setGameTransport (gameTransport: any) {
    this.communityCards[0] = 'SPADES_QUEEN'
    // CardSuite.CLUBS_THREE
    alert('GameTransport')
    this.multiplePlayers[this.location].name = 'test123'
    // alert(gameTransport.communityCards[0])
    // {"communityCards":["SPADES_QUEEN","SPADES_SEVEN","SPADES_KING"],"potSum"
    // :30,"bigBlind":10,"nextPlayer":2,"event":null,"players":[{"id":1,"money":200
    // ,"action":{"type":"BET","bet":1},"player":true,"dealer":true},{"id":2,"money":10000,"
    // action":{"type":"BET","bet":1},"player":true,"dealer":false}]}
    /*this.userId = gameTransport.
    this.multiplePlayers = gameTransport.
    //Players
      //id
      //money
      //action
      //isPlayer
      //isDealer
    // bigBlind
    //pot
    //community cards
    gameState.setPlayers(new GameStateTransport.PlayerTransport[]{
            new GameStateTransport.PlayerTransport(
                    1,
                    200,
                    new GameAction(GameActionType.BET, 1),
                    true,
                    true
            ), // admin
            new GameStateTransport.PlayerTransport(
                    2,
                    10000,
                    new GameAction(GameActionType.BET, 1),
                    true,
                    false
            )}); // jason
    gameState.setBigBlind(10);
    gameState.setCommunityCards(Arrays.asList(new Card[]{Card.SPADES_QUEEN, Card.SPADES_SEVEN, Card.SPADES_KING}));
    gameState.setPotSum(30);
*/
/*
    switch (gameTransport.gameStateType) {
      // Case for when a hand has just started
      case GameStateType.HAND_STARTED: {
        this.hasBet = gameTransport.hasBet
        Object.assign(this.multiplePlayers, gameTransport.multiplePlayers)
        this.pot = gameTransport.pot
        Object.assign(this.communityCards, gameTransport.communityCards)
        break
      }
      // Case for when a player makes an action
      case GameStateType.USER_ACTION: {
        this.hasBet = gameTransport.hasBet
        Object.assign(this.multiplePlayers, gameTransport.multiplePlayers)
        this.pot = gameTransport.pot
        break
      }
      // Case for when a hand is finished and a winner is decided for that hand
      case GameStateType.HAND_FINISHED: {
        this.hasBet = gameTransport.hasBet
        Object.assign(this.multiplePlayers, gameTransport.multiplePlayers)
        this.pot = gameTransport.pot
        Object.assign(this.communityCards, gameTransport.communityCards)
        break
      }
      // Case for when a new Community card is added to the game
      case GameStateType.ROUND_FINISHED: {
        this.hasBet = gameTransport.hasBet
        Object.assign(this.multiplePlayers, gameTransport.multiplePlayers)
        this.pot = gameTransport.pot
        Object.assign(this.communityCards, gameTransport.communityCards)
        break
      }
      // Case for when a new player is added into the game
      case GameStateType.USER_JOIN: {
        this.hasBet = gameTransport.hasBet
        Object.assign(this.multiplePlayers, gameTransport.multiplePlayers)
        this.pot = gameTransport.pot
        Object.assign(this.communityCards, gameTransport.communityCards)
        break
      }
      // Case for when a player has left the game
      case GameStateType.USER_LEAVE: {
        Object.assign(this.multiplePlayers, gameTransport.multiplePlayers)
        break
      }
    }*/

    // If the player has premove staged then they will move it
<<<<<<< Updated upstream
    this.sendAction()
<<<<<<< HEAD
=======

>>>>>>> 6e28e6cb3ed422e1bfa815e8f0b9bf4c5dc9df83
    this.setTableActions()
=======
    // this.sendAction()
    // this.setTableActions()

>>>>>>> Stashed changes
  }

  /**
   * Confirms that the GameService connection is still running
   * @returns Boolean
   */
  public isGameRunning () {
    return this.gameService.connected()
  }
  public testSend (action: GameActionType, money: number) {
    const move: GameAction = { type: action, bet: money }
    this.gameService.userSendAction(move)
  }
  /**
   * Sends action to the server
   * @param gameAction - Action to sent to the server
   */
  private send (gameAction: GameAction) {
    this.gameService.userSendAction(gameAction)
  }

  /**
   * Validates users action and sends it to the server
   */
  private sendAction () {
    // Confirms that you have an action to send
    if (this.userAction !== null) {
      // Confirms that it is your turn
      if (this.playerLoc() === this.turn) {
        // Confirms that this is a valid move
        if (this.validatePreMove(this.userAction)) {
          this.send(this.userAction)
          this.userAction = null
        }
      }
    }
  }

}
