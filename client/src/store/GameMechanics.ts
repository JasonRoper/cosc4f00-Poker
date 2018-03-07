
import {
  GameAction,
  GameActionType,
  GameError,
  GameFinished,
  GameService,
  GameStarted,
  GameState,
  Player,
  UserCards } from '@/api/gameservice'

import actions from '@/types/actions'
import CardSuite from '@/types/cards'
import { Action } from 'vuex'

export default class GameMech {
  public numberOfPlayers: number = 5
  public hasBet: boolean = false
  public turn: number = 0
  public multiplePlayers: Player[] = []
  public gameId: number = 0
  public pot: number = 0
  public communityCards: string[] = []
  public userId: number | null = 0
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

  // Generate the websocket paths used for the given gameId
  // @param gameId - the id of the game
  constructor (gameId: number, userId: number | null) {
    this.gameService = new GameService(gameId)
    if (userId !== null) {
      this.gameService.onGameUpdated(this.setGameTransport)
      this.gameService.onGameFinished(this.onGameFinishedEvent)
      this.gameService.onGameStarted(this.onGameStartedEvent)
      this.gameService.onUserCards(this.onUserCardsEvent)
      // this.gameService.onGameError(this.onGameError)

      this.setDefaultTransport()
      this.lobby = false
    } else {
      this.gameService.onGameUpdated(this.setGameTransport)
      this.lobby = true
    }
  }
  public setDefaultTransport () {
    this.hasBet = false
    this.turn = 0
    this.multiplePlayers = [{
      id: 0,
      money: 500,
      name: 'javon',
      action: null,
      card1: CardSuite.BLANK_CARD,
      card2: CardSuite.BLANK_CARD,
      currentBet: 0,
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

  public playerLoc (): number {
    for (const play of this.multiplePlayers) {
      if (play.id === this.userId) {
        return play.id
      }
    }
    return -1
  }

  public getTableAction () {
    return this.tableAction
  }
  /**
   * sendEvent of the game started
   */
  public onGameStartedEvent (gameStarted: GameStarted) {
    this.multiplePlayers = gameStarted.multiplePlayers
  }

  /**
   * @param gameFinished Finished Event of the game ending
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
   * @param gameError When the game Sends and Error
   */
  public onGameError (gameError: GameError) {
    alert('Error: ' + gameError.error)
    console.log('Error: ' + gameError.error)
  }
  /**
   * send user cards
   * @param userCards
   */
  public onUserCardsEvent (userCards: UserCards) {
    this.multiplePlayers[this.playerLocation].card1 = userCards.card1
    this.multiplePlayers[this.playerLocation].card2 = userCards.card2
  }
  /**
   * SendCards to the user
   * players first card
   *  players second card
   * public sendCards (card1: string, card2: string) {
   * this.multiplePlayers[this.playerLocation].card1 = card1
   * this.multiplePlayers[this.playerLocation].card2 = card2
   * }
   */
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
   * storePremove - Stores the premove or resets
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
   * TableActions will give the player the differnet table actions that they can take
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
  public getUser (): Player {
    return this.multiplePlayers[this.playerLocation]
  }

  public getOpponents (): Player[] {
    const playerArray: Player[] = []
    for (let i = 0;i < this.multiplePlayers.length; i++) {
      if (i !== this.playerLocation) {
        playerArray.push(this.multiplePlayers[i])
      }
    }
    return playerArray
  }

  /**
   *  Sets the Game Transport in the Game Mechanics
   * @param GameState
   */
  public setGameTransport (gameTransport: GameState) {
    if (gameTransport.gameId === this.gameId) {
      this.communityCards = gameTransport.communityCards
      this.hasBet = gameTransport.hasBet
      this.turn = gameTransport.turn
      this.multiplePlayers = gameTransport.multiplePlayers
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
      return true
    }
    return false
  }

  public isGameRunning () {
    return this.gameService.connected()
  }
  // can this game accecpt action from this player
  private send (gameAction: GameAction) {
    this.gameService.sendAction(gameAction)
  }

}
