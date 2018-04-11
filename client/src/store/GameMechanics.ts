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
import { setupComplete } from '../store'
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
  public roundNumber: number = 0
  public gameStatus: string = 'Game Pending....'
  public isHandFinished: boolean = false
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

    Promise.all([axios.get(API_V1 + '/games/' + this.gameId),
      setupComplete,
      axios.get(API_V1 + '/games/' + this.gameId + '/cards')]).then((results) => {
        console.log('GAME MECHANICS CONSTRUCTOR - ASKING TO JOIN GAME')
        const responce = results[0]
        this.setGameTransport(responce.data)
        console.log(responce.data)
        console.log('the current username:', state.state.username)
        if (this.multiplePlayers.length > 0) {
          switch (this.communityCards.length) {
            case 0: {
              this.roundNumber = 0
              break
            }
            case 1:
            case 2:
            case 3: {
              this.roundNumber = 1
              break
            }
            case 4: {
              this.roundNumber = 2
              break
            }
            case 5: {
              this.roundNumber = 3
              break
            }
            default: {
              console.log('length of community cards exceeds round numbers')
            }
          }
          this.hasGameStarted = true
          console.log('Updated the player location')
          this.multiplePlayers.forEach((player: Player, index: number) => {
            if (player.name === state.state.username) {
              this.playerId = index
            }
          })
          console.log(results[2])
          console.log(results[2].data)
          if (results[2].data.cardOne !== null) {
            this.cardOne = results[2].data.cardOne
            this.cardTwo = results[2].data.cardTwo
            this.setGameCards()
          }
        }
      }).catch((error) => {
        console.log('having an error IN JOINING GAME')
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
    this.gameStatus = 'Hand Finished'
    console.log('The finished Event has been called')
    console.log(gameFinished)
    this.setFinishedPlayer(gameFinished)
    console.log('This game finished event does nothing')
    this.isHandFinished = true
  }

  /**
   * Handles when a Game Error is sent
   * @event ErrorEvent
   * @param gameError When the game Sends and Error
   */
  public onGameError (gameError: GameError) {
    this.gameStatus = 'Error: ' + gameError.error
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
   *  Sets the Game Transport in the Game Mechanics
   * @param GameState
   */
  public setGameTransport (gameTransport: any) {
    console.log('CurrentGameTransport')
    console.log(gameTransport)
    if (gameTransport.event !== null) {
      if (gameTransport.event.message) {
        this.gameStatus = gameTransport.event.message
      }
      switch (gameTransport.event.action) {
        case Event.GAME_STARTED: {
          this.gameStatus = 'Game Start!'
          this.hasGameStarted = true
          this.roundNumber = 0
          this.gameStarted(gameTransport)
          this.setGameCards()
          this.hasBet = false
          this.isHandFinished = false
          break
        }
        case Event.HAND_FINISHED: {
          console.log('HAND_FINISHED event triggered')
          this.handFinished(gameTransport)
          this.setGameCards()
          this.gameStatus = 'Hand Finished'
          break
        }
        case Event.ROUND_FINISHED: {
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
          this.gameFinished(gameTransport)
          this.gameStatus = 'Game Finished'
          break
        }
        default: {
          this.gameStatus = 'GAMETRANSPORT EVENT is ' +
          gameTransport.event.action + ' there is not proper case for this'
          console.log('GAMETRANSPORT EVENT is ' + gameTransport.event.action + ' there is not proper case for this')
          this.defaultGameTransport(gameTransport)
          this.setGameCards()
        }
      }
    } else {
      console.log('GAMETRANSPORT EVENT is nothing')
      this.defaultGameTransport(gameTransport)
    }
    this.hasSomeoneBet()
    this.bigBlind = gameTransport.bigBlind
    this.turn = gameTransport.nextPlayer
    this.potSum = gameTransport.potSum
    console.log('User: ' + this.multiplePlayers[this.playerId].name)
    console.log('Turn: ' + this.multiplePlayers[this.turn].name)

    // If its your turn and you have a premove - it will send it
    this.sendAction()

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

  public gameFinished (gameTransport: any) {
    // this.setCommunityCards(gameTransport)
    // this.setPlayers(gameTransport)
    console.log('The Game has finished')
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
    this.setCommunityCards(gameTransport)
    this.roundNumber = 3
    this.gameStatus = this.username + ' The HAND_FINISHED  was called'
    this.setPlayers(gameTransport)
    // this.setCommunityCards(gameTransport)
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
          isUser: user,
          isWinner: false
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
      const win: boolean = (item.winnings > 0)
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
        isUser: user,
        isWinner: win
      }
      this.multiplePlayers.push(player)
    })
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
