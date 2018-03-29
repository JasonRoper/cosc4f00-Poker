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
import axios from '@/api/axios'
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

import { API_V1 } from '@/config'

/**
 * Import Card Suite that holds all Card Varients
 */
import { Action } from 'vuex'

import { Card } from '@/types/cards'

/**
 * Axios - Imports axios which is used to send POST and GET requests
 */

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
  public playerId: number = -1
  public userAction: GameAction | null = null

  public endGame: boolean = false
  public username: string = ''
  public disable: number = 1
  public enableButton: number = 0
  public foldAction: number = 0
  public callAction: number = 0
  public betAction: number = 0
  public raiseAction: number = 0
  public checkAction: number = 0
  public possibleAction: GameActionType [][] = [[GameActionType.BET, GameActionType.FOLD, GameActionType.CHECK],
    [GameActionType.CALL, GameActionType.RAISE, GameActionType.FOLD], [GameActionType.FOLD]]
  private gameService: GameService

  /**
   * Creates a Game Mechanics that the user uses to play the game
   * @class Creates Game Mechanics class
   * @param gameId attaches user to listen to gameId
   * @param playerId Creates table for user or for a lobby
   */
  constructor (gameId: number, username: string) {
    this.gameService = new GameService(gameId)
    this.gameId = gameId
    this.gameService.onGameUpdated(this.setGameTransport.bind(this))
    this.gameService.onGameFinished(this.onGameFinishedEvent.bind(this))
    this.gameService.onGameError(this.onGameError.bind(this))

    this.gameService.onUserCards(this.onUserCardsEvent.bind(this))
    this.username = username
    axios.get(API_V1 + '/games/' + this.gameId).then((responce) => {
      console.log('Got Game State')
      responce.data.players.forEach((item: any, index: number) => {
        if (username === item.name) {
          this.playerId = index
        }
      })
      this.setGameTransport(responce.data)
    }).catch((error) => {
      alert('having an error')
      alert(error)
    })
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
      isTurn: false,
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
      isTurn: false,
      isDealer: false
    }, this.defaultPlayer(), this.defaultPlayer() ,this.defaultPlayer(),this.defaultPlayer()]
    this.gameId = 0
    this.potSum = 0
    this.communityCards = [Card.CLUBS_ACE, Card.CLUBS_EIGHT, 'three', 'four', 'five']
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
      isTurn: false,
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

  public getUser (): Player {
    return this.multiplePlayers[this.playerId]
  }

  public getOpponent (): Player[] {
    const opponents: Player[] = []
    this.multiplePlayers.forEach((player: Player, index: number) => {
      if (index !== this.playerId) {
        opponents.push(player)
      }
    })
    return opponents
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
  public onUserCardsEvent (userCards: any) {
    alert('CardEvent was called')
    // Vue.set(vm.userProfile, 'age', 27)
    this.multiplePlayers[this.playerId].card1 = userCards.cardOne
    this.multiplePlayers[this.playerId].card2 = userCards.cardTwo
  }

  /**
   * Adds cards to the Comminity Cards
   * @param card
   */
  public onGameComminityCardsEvent (card: string) {
    // if (this.communityCards.length <= 5) {
    //  this.communityCards.push(card)
    // }
  }

  /**
   * Stores the Premove for the user
   * @param action - holds users GameActionType
   * @param money - holds how much money the user bet with a particular action
   * @returns validity of Action that user is attempting to store
   */
  public storePremove (action: GameActionType, money: number): boolean {
    const move: GameAction = { type: action, bet: money }
    if (this.validatePreMove(move)) {
      console.log(this.username + ' Premove was validated')
      this.disableButton(action)
      this.userAction = move
      if (this.sendAction()) {
        return true
      } else {
        return false
      }
    } else {
      console.log(this.username + ' The move you attempted to send was invalid')
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
    if (this.multiplePlayers[this.playerId].money < move.bet) {
      console.log(move + 'you do not have enough money')
      return false
    }
    // Confirm that you made a valid move
    if (this.hasBet) {
      if (this.possibleAction[1].indexOf(move.type) === -1) {
        console.log('you have not made a valid move')
        return false
      }
    } else {
      if (this.possibleAction[0].indexOf(move.type) === -1) {
        console.log('you have not made a valid move')
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
    if (gameTransport.event) {
      if (gameTransport.event.message) {
        alert(gameTransport.event.message)
      } else {
        alert('There is no message')
      }
    }
    if (gameTransport.event) {
      switch (gameTransport.event.action) {
        case Event.GAME_STARTED: {
          this.gameStarted(gameTransport)
          break
        }
        case Event.HAND_FINISHED: {
          this.handFinished(gameTransport)
          break
        }
        default: {
          this.defaultGameTransport(gameTransport)
        }
      }
    } else {
      this.defaultGameTransport(gameTransport)
    }
    /*
      HAND_STARTED = 'HAND_STARTED', // USER Joins the GAME
      USER_ACTION = 'PLAYER_ACTION', // This is sent after a player makes an action
      HAND_FINISHED = 'HAND_FINISHED', // Equivalent for a winnder being determined from a hand
      ROUND_FINISHED = 'ROUND_FINISHED', // All players have bet - this results in a new Community cards
      USER_JOIN = 'PLAYER_JOIN',// A new player has been added to the game
      USER_LEAVE = 'PLAYER_LEAVE' //
    */
    // If the player has premove staged then they will move it
    // this.sendAction()
    // this.setTableActions()
  }

  public gameStarted (gameTransport: any) {
    alert('The Game has started')
    alert('You should be receiving your hands')
    console.log(gameTransport)
    // this.multiplePlayers[0].action = gameTransport.players[0].action
  }

  public handFinished (gameTransport: any) {
    this.multiplePlayers = []
    this.communityCards = []
    alert('handFinished Transport')
    console.log(gameTransport)
    // this.multiplePlayers[0].action = gameTransport.players[0].action
    gameTransport.players.forEach((item: any, index: number) => {
      const act: GameActionType | null = (item.action.type !== null) ? item.action : null
      const userTurn: boolean = (index === gameTransport.nextPlayer)
      const player: Player = {
        id: item.id,
        money: item.money,
        name: item.name,
        action: act,
        card1: gameTransport.cardOne,
        card2: gameTransport.cardTwo,
        currentBet: 0,
        isFold: item.fold,
        winnings: gameTransport.winnings,
        isPlayer: item.player,
        isDealer: item.dealer,
        isTurn: userTurn
      }
      this.multiplePlayers.push(player)
    })
    Array.from(gameTransport.communityCards).forEach((card: any) => {
      if (card === null) {
        this.communityCards.push(Card.CLUBS_ACE)
      } else {
        this.communityCards.push(card)
      }
    })
    this.bigBlind = gameTransport.bigBlind
    this.turn = gameTransport.nextPlayer
    this.potSum = gameTransport.potSum
  }

  public defaultGameTransport (gameTransport: any) {
    this.multiplePlayers = []
    this.communityCards = []
    alert('GameTransport')
    console.log(gameTransport)
    // this.multiplePlayers[0].action = gameTransport.players[0].action
    gameTransport.players.forEach((item: any, index: number) => {
      const act: GameActionType | null = (item.action.type !== null) ? item.action : null
      const userTurn: boolean = (index === gameTransport.nextPlayer)
      const player: Player = {
        id: item.id,
        money: item.money,
        name: item.name,
        action: act,
        card1: Card.BLANK_CARD,
        card2: Card.BLANK_CARD,
        currentBet: 0,
        isFold: item.fold,
        winnings: 0,
        isPlayer: item.player,
        isDealer: item.dealer,
        isTurn: userTurn
      }
      this.multiplePlayers.push(player)
    })
    Array.from(gameTransport.communityCards).forEach((card: any) => {
      if (card === null) {
        this.communityCards.push(Card.CLUBS_ACE)
      } else {
        this.communityCards.push(card)
      }
    })
    this.bigBlind = gameTransport.bigBlind
    this.turn = gameTransport.nextPlayer
    this.potSum = gameTransport.potSum
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
  private sendAction (): boolean {
    // Confirms that you have an action to send
    if (this.userAction !== null) {
      // Confirms that it is your turn
      if (this.playerId === this.turn) {
        // Confirms that this is a valid move
        if (this.validatePreMove(this.userAction)) {
          this.send(this.userAction)
          console.log('Themove was sent')
          this.userAction = null
          return true
        }
      }
    }
    return false
  }

}
