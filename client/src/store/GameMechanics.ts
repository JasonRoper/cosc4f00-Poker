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
import GameRequest from '@/store/GameRequest'
import { Action } from 'vuex'

import { Card } from '@/types/cards'
import state from '../store/users'

/**
 * Axios - Imports axios which is used to send POST and GET requests
 */

/**
 * Manages all interactions between the game and the server
 * @class - Game Mechanics Class
 */
export default class GameMech {
  public gameId: number
  public leaveGame: boolean = false
  // Defines how many players can be at a table
  public numberOfPlayers: number = 5
  public bigBlind: number = 0
  public hasBet: boolean = false
  public turn: number = 0
  public multiplePlayers: Player[] = []
  public potSum: number = 0
  public communityCards: string[] = []
  public playerId: number = 0
  public userAction: GameAction | null = null
  public roundNumber: number = -1
  public gameStatus: string = 'Game Pending....'
  public isHandFinsihed: boolean = false
  public hasGameStarted: boolean = false
  public username: string = ''
  public disable: number = 1
  public enableButton: number = 0
  public foldAction: number = 0
  public callAction: number = 0
  public gotHand: boolean = false
  public betAction: number = 0
  public raiseAction: number = 0
  public checkAction: number = 0
  public possibleAction: GameActionType [][] = [[GameActionType.BET, GameActionType.FOLD, GameActionType.CHECK],
    [GameActionType.CALL, GameActionType.RAISE, GameActionType.FOLD], [GameActionType.FOLD]]
  public cardOne: string = ''
  public cardTwo: string = ''
  private gameService: GameService

  /**
   * Creates a Game Mechanics that the user uses to play the game
   * @class Creates Game Mechanics class
   * @param gameId attaches user to listen to gameId
   * @param playerId Creates table for user or for a lobby
   */
  constructor (gameId: number, username: string) {
    console.log('creating a new game mechanics')
    this.gameService = new GameService(gameId)
    this.gameId = gameId
    this.gameService.onUserCards(this.onUserCardsEvent.bind(this))
    this.gameService.onGameUpdated(this.setGameTransport.bind(this))
    this.gameService.onGameFinished(this.onGameFinishedEvent.bind(this))
    this.gameService.onGameError(this.onGameError.bind(this))
    this.username = username

    axios.get(API_V1 + '/games/' + this.gameId).then((responce) => {
      console.log('GAME MECHANICS CONSTRUCTOR - ASKING TO JOIN GAME')
      this.setGameTransport(responce.data)
      if (this.multiplePlayers.length > 0) {
        this.multiplePlayers.forEach((player: Player, index: number) => {
          if (player.name === this.username) {
            this.playerId = index
          }
        })
        this.setGameCards()
      }
    }).catch((error) => {
      alert('having an error IN JOINING GAME')
      console.log(error)
    })
  }
  /**
   * Gets Player location within array
   * @returns player location in the Multiplayer Array
   */

  public getUser (): Player {
    return this.multiplePlayers[this.playerId]
  }

  public getOpponent (username: string): Player[] {
    const opponents: Player[] = []
    this.multiplePlayers.forEach((player: Player, index: number) => {
      if (player.name !== username) {
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
  public onGameFinishedEvent (gameFinished: any) {
    alert('GAME FINISHED')
    console.log('The finished Event has been called')
    console.log(gameFinished)
    this.setFinishedPlayer(gameFinished)
    console.log('This game finished event does nothing')
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
    console.log('The users cards are: ' + userCards.cardOne + ' and ' + userCards.cardTwo)
    this.multiplePlayers[this.playerId].card1 = userCards.cardOne
    this.multiplePlayers[this.playerId].card2 = userCards.cardTwo
    this.cardOne = userCards.cardOne
    this.cardTwo = userCards.cardTwo
    this.gotHand = true
  }

  /**
   * Stores the Premove for the user
   * @param action - holds users GameActionType
   * @param money - holds how much money the user bet with a particular action
   * @returns validity of Action that user is attempting to store
   */
  public storePremove (action: GameActionType, money: number): boolean {
    const move: GameAction = { type: action, bet: money }
    this.userAction = move
    if (this.sendAction()) {
      return true
    } else {
      return false
    }
  }

  /**
   * Disables the TableActions based on a users previous action
   * @param action holds the action of the user
   */
  /*
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
   */
  /**
   * Disables specific tableActions based on if there has been a bet at the table
   */
  /*
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
   */

  /**
   * ValidatePreMove
   */
  /*
    public validatePreMove (move: GameAction) {
      // Confirm that you made a valid move
      if (this.hasBet) {
        if (this.possibleAction[1].indexOf(move.type) === -1) {
          console.log(this.username + ' ' + move + 'you have not made a valid move possibleAction[1]')
          return false
        }
      } else {
        if (this.possibleAction[0].indexOf(move.type) === -1) {
          console.log(this.username + ' ' + move + 'you have not made a valid move possibleAction[0]')
          return false
        }
      }
      return true
    }
  */

  /**
   *  Sets the Game Transport in the Game Mechanics
   * @param GameState
   */
  public setGameTransport (gameTransport: any) {
    console.log('CurrentGameTransport')
    console.log(gameTransport)
    if (gameTransport.event !== null) {
      if (gameTransport.event.message) {
        // this.gameStatus = gameTransport.event.message
        // alert(gameTransport.event.message + ' The gamestatus is')
      }
      switch (gameTransport.event.action) {
        case Event.GAME_STARTED: {
          alert('GAME_STARTED event triggered')
          this.gameStatus = 'Game Start!'
          this.hasGameStarted = true
          this.roundNumber = 0
          this.gameStarted(gameTransport)
          this.setGameCards()
          this.hasBet = false
          break
        }
        case Event.HAND_FINISHED: {
          alert('HAND_FINISHED event triggered')
          console.log('HAND_FINISHED event triggered')
          this.roundNumber++
          this.handFinished(gameTransport)
          this.setGameCards()
          this.gameStatus = 'Game Finished'
          break
        }
        case Event.ROUND_FINISHED: {
          alert('ROUND FINISHED event triggered')
          this.roundFinished(gameTransport)
          this.setGameCards()
          this.gameStatus = 'Round Finished'
          break
        }
        case Event.USER_ACTION: {
          this.playerAction(gameTransport)
          this.setGameCards()
          break
        }
        case Event.USER_JOIN: {
          this.playerJoined(gameTransport)
          this.gameStatus = 'Here comes a new Challenger'
          break
        }
        case Event.GAME_FINISHED: {
          this.leaveGame = true
          break
        }
        default: {
          alert('GAMETRANSPORT EVENT is ' + gameTransport.event.action + ' there is not proper case for this')
          console.log('GAMETRANSPORT EVENT is ' + gameTransport.event.action + ' there is not proper case for this')
          this.defaultGameTransport(gameTransport)
          this.setGameCards()
        }
      }
    } else {
      alert('GAMETRANSPORT EVENT is NULL')
      this.defaultGameTransport(gameTransport)
    }
    this.hasSomeoneBet()
    this.bigBlind = gameTransport.bigBlind
    this.turn = gameTransport.nextPlayer
    this.potSum = gameTransport.potSum
    console.log('User: ' + this.multiplePlayers[this.playerId].name)
    console.log('Turn: ' + this.multiplePlayers[this.turn].name)
    /*
      HAND_STARTED = 'HAND_STARTED', // USER Joins the GAME
      USER_ACTION = 'PLAYER_ACTION', // This is sent after a player makes an action
      HAND_FINISHED = 'HAND_FINISHED', // Equivalent for a winnder being determined from a hand
      ROUND_FINISHED = 'ROUND_FINISHED', // All players have bet - this results in a new Community cards
      USER_JOIN = 'PLAYER_JOINED',// A new player has been added to the game
      USER_LEAVE = 'PLAYER_LEAVE' //
    */
    // If the player has premove staged then they will move it
    // this.sendAction()
    // this.setTableActions()
    console.log('Username: ' + state.state.username + ' UserId: ' + state.state.userId)
  }

  public playerJoined (gameTransport: any) {
    this.setPlayers(gameTransport)
  }

  public playerAction (gameTransport: any) {
    this.setPlayers(gameTransport)
    this.setCommunityCards(gameTransport)
    // this.setTableActions()
  }

  public roundFinished (gameTransport: any) {
    this.setCommunityCards(gameTransport)
    this.roundNumber++
    this.hasBet = false
  }

  public gameStarted (gameTransport: any) {
    console.log(this.username + ' The game has started GAME_STARTED TRIGGERED')
    this.setPlayers(gameTransport)
    this.multiplePlayers.forEach((player: Player, index: number) => {
      if (player.name === this.username) {
        this.playerId = index
      }
    })
    this.setCommunityCards(gameTransport)
  }

  public handFinished (gameTransport: any) {
    alert(this.username + ' The HAND_FINISHED  was called')
    this.setPlayers(gameTransport)
    this.hasGameStarted = false
  }

  public defaultGameTransport (gameTransport: any) {
    console.log('DEFAULT GAMETRANSPORT')

    this.setPlayers(gameTransport)
    this.setCommunityCards(gameTransport)
  }

  public hasSomeoneBet () {
    this.multiplePlayers.forEach((player: Player) => {
      if (player.action !== null) {
        if (player.action.type === GameActionType.BET || player.action.type === GameActionType.RAISE) {
          this.hasBet = true
        }
      }
    })
  }

  public setPlayers (gameTransport: any) {
    this.multiplePlayers = []
    if (gameTransport.players.length !== 0) {
      gameTransport.players.forEach((item: any, index: number) => {
        let act: GameAction | null = null
        if (item.action !== null) {
          if (item.action.type !== null) {
            act = { type: item.action.type, bet: item.action.bet }
          }
        }
        // console.log('This is for the is it your turn' + index + ' ' + gameTransport.nextPlayer)
        const userTurn: boolean = (index === gameTransport.nextPlayer)
        const user: boolean = (item.name === this.username)
        const player: Player = {
          id: item.id,
          money: item.money,
          name: item.name,
          action: act,
          card1: Card.BLANK_CARD,
          card2: Card.BLANK_CARD,
          isFold: item.fold,
          winnings: 0,
          isPlayer: item.player,
          isDealer: item.dealer,
          isTurn: userTurn,
          isUser: user
        }
        this.multiplePlayers.push(player)
      })
    }
  }

  public setFinishedPlayer (gameTransport: any) {
    this.multiplePlayers = []
    gameTransport.players.forEach((item: any, index: number) => {
      let act: GameAction | null = null
      if (item.action !== null) {
        if (item.action.type !== null) {
          act = { type: item.action.type, bet: item.action.bet }
        }
      }
      // console.log('This is for the is it your turn' + index + ' ' + gameTransport.nextPlayer)
      const userTurn: boolean = (index === gameTransport.nextPlayer)
      const user: boolean = (item.name === this.username)
      const player: Player = {
        id: item.id,
        money: item.money,
        name: item.name,
        action: act,
        card1: item.cardOne,
        card2: item.cardTwo,
        isFold: item.fold,
        winnings: item.winnings,
        isPlayer: item.player,
        isDealer: item.dealer,
        isTurn: userTurn,
        isUser: user
      }
      this.multiplePlayers.push(player)
    })

    // :0,"id":0,"player":true,"fold":true,"dealer":true},{"money":136,"name":"AIPlayer1",
    // "action":{"type":"CHECK","bet":0},"cardOne":"SPADES_THREE","cardTwo":"HEARTS_FOUR",
    // "winnings":72,"id":1,"player":true,"fold":false,"dealer":false},{"money":64,"name":
    // "javon","action":{"type":"CHECK","bet":0},"cardOne":"CLUBS_SEVEN","cardTwo":"CLUBS_A
    // CE","winnings":0,"id":2,"player":false,"fold":false,"dealer":false},{"money":100,"name":
    // "jason","action":{"type":"FOLD","bet":0},"cardOne":"CLUBS_KING","cardTwo":"HEARTS_NINE",
    // "winnings":0,"id":3,"player":false,"fold":true,"dealer":false}]}
  }

  public setCommunityCards (gameTransport: any) {
    this.communityCards = []
    Array.from(gameTransport.communityCards).forEach((card: any) => {
      if (card === null) {
        this.communityCards.push(Card.BLANK_CARD)
      } else {
        this.communityCards.push(card)
      }
    })
  }

  public setGameCards () {
    this.multiplePlayers.forEach((value: Player, index: number) => {
      if (index === this.playerId) {
        if (this.cardOne !== '' && this.cardTwo !== '') {
          // console.log('set cards worked')
          value.card1 = this.cardOne
          value.card2 = this.cardTwo
        } else {
          // console.log('set cards DIDN"T work')
          value.card1 = Card.BLANK_CARD
          value.card2 = Card.BLANK_CARD
        }
      } else {
        value.card1 = Card.BLANK_CARD
        value.card2 = Card.BLANK_CARD
      }
    })
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
        this.send(this.userAction)
        console.log(this.username + ' sent the move')
        this.userAction = null
        return true
      }
    }
    return false
  }

}
