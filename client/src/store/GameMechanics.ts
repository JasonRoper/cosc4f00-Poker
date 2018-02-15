import {
  GameAction,
  GameActionType,
  GameEnded,
  GameService,
  GameStarted,
  GameState,
  Player,
  Pot } from '@/api/gameservice'

import actions from '@/types/actions'
import CardSuite from '@/types/cards'
import { Action } from 'vuex'

export default class GameMech {
  public numberOfPlayers: number = 5
  public gameTransport: GameState
  public hasBet: boolean
  public turn: number
  public multiplePlayers: Player[]
  public temp: string
  public gameId: number
  public pot: Pot []
  // public Deck: Card[] | null
  public communityCards: string[]
  public userId: number | null
  public foldAction: number
  public callAction: number
  public betAction: number
  public raiseAction: number
  public checkAction: number
  public possibleAction: GameActionType [][] = [[GameActionType.BET, GameActionType.FOLD, GameActionType.CHECK],
    [GameActionType.CALL, GameActionType.RAISE, GameActionType.FOLD], [GameActionType.FOLD]]
  public playerLocation: number
  private gameService: GameService

  /*
  public get playerLocation () {
    this.playerLocation = -1
    if (this.userId !== null) {
      for (let index = 0; index < this.multiplePlayers.length; index++) {
        if (this.multiplePlayers[index].id === this.userId) {
          this.playerLocation = index
        }
      }
    }
    return this.playerLocation
  }
  public set playerLocation (theBar: number) {
    this.playerLocation = theBar
  }
  */
   // Generate the websocket paths used for the given gameId
   // @param gameId - the id of the game
  constructor (gameId: number, userId: number | null) {
    this.gameService = new GameService(gameId)
    // this.gameService.onGameUpdated(this.setGameTransport)
    this.setDefaultTransport()
  }

  public setDefaultTransport () {
    this.hasBet = true
    this.turn = 0
    const p: Player = this.defaultPlayer()
    this.multiplePlayers = [{
      id: 0,
      money: 500,
      name: 'javon',
      tableAction: {} as GameActionType,
      premove: null,
      card1: CardSuite.BLANK_CARD,
      card2: CardSuite.BLANK_CARD,
      playing: true,
      endGame: null,
      action: null
    }, {
      id: 1,
      money: 888,
      name: 'test',
      tableAction: {} as GameActionType,
      premove: null,
      card1: CardSuite.BLANK_CARD,
      card2: CardSuite.BLANK_CARD,
      playing: true,
      endGame: null,
      action: null
    }, this.defaultPlayer(), this.defaultPlayer() ]
    this.gameId = 0
    this.pot = []
    // this.Deck = []
    this.communityCards = [CardSuite.BLANK_CARD, 'two', 'three', 'four', 'five']
    this.temp = 'temoe'
    this.userId = 0
    this.playerLocation = 0
  }
  public defaultPlayer (): Player {
    const player: Player = {
      id: -1,
      money: 0,
      name: 'defaultPlayer',
      tableAction: {} as GameActionType,
      premove: null,
      card1: CardSuite.BLANK_CARD,
      card2: CardSuite.BLANK_CARD,
      playing: true,
      endGame: null,
      action: null
    }
    return player
  }
  public getTableAction () {
    return this.multiplePlayers[this.playerLocation].tableAction
  }

  /**
   * sendEvent of the game started
   */
  public gameStartedEvent (gameStarted: GameStarted) {
    this.multiplePlayers = gameStarted.multiplePlayers
  }

  // 1. onGameError
  // 2. onGameStarted
  // 3. onGameFinished
  // 4. onDeal
  // 5. onGameUpdated
  /**
   * SendCards to the user
   * @param card1 - players first card
   * @param card2 - players second card
   */
  public sendCards (card1: string, card2: string) {
    this.multiplePlayers[this.playerLocation].card1 = card1
    this.multiplePlayers[this.playerLocation].card2 = card2
  }

  /**
   * sendcommunityCards
   */
  public sendCommunityCards (card1: string, card2: string, card3: string) {
    this.communityCards[0] = card1
    this.communityCards[1] = card2
    this.communityCards[2] = card3
    this.temp = card3
    /*
    const length = this.communityCards.length
    if (length < 3) {

    } else if (length < 5) {
      this.communityCards[length] = card1
    } else {
      alert('ERROR _ YOU ARE TRYING TO ADD A CARD TO COMMUNITY CARDS WHEN THERE IS ALREADY 5')
    }*/
  }

  /**
   * storePremove - Stores the premove or resets
   */
  public storePremove (action: GameActionType, money: number): boolean {
    const move: GameAction = { type: action, bet: money }
    if (this.validatePreMove(move)) {
      this.multiplePlayers[this.playerLocation].action = move
      return true
    } else {
      this.tableActions()
      this.multiplePlayers[this.playerLocation].action = null
      return false
    }
  }

  /**
   * TableActions will give the player the differnet table actions that they can take
   */
  public tableActions () {
    if (this.hasBet) {
      this.foldAction = 1
      this.betAction = 1
      this.checkAction = 1
      this.callAction = 0
      this.raiseAction = 0
    } else {
      this.foldAction = 1
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
    if (this.multiplePlayers[this.playerLocation].money > move.bet) {
      return false
    }
    // Confirm that you made a valid move
    if (this.hasBet) {
      if (this.possibleAction[0].indexOf(move.type) === -1) {
        return false
      }
    } else {
      if (this.possibleAction[1].indexOf(move.type) === -1) {
        return false
      }
    }

    return true
  }

  /**
   * This will deal cards to the user
   * @param card1
   * @param card2
   */
  public dealCards (card1: string, card2: string) {
    this.multiplePlayers[this.playerLocation].card1 = card1
    this.multiplePlayers[this.playerLocation].card2 = card2
  }

  public sendAction (action: GameAction) {
    if (this.multiplePlayers[this.playerLocation].playing) {
      if (this.playerLocation === this.turn) {
        if (this.storePremove(action.type, action.bet)) {
          this.send(action)
          this.multiplePlayers[this.playerLocation].action = null
        }
      }
    }
  }

  public getUser (): Player {
    return this.multiplePlayers[this.playerLocation]
  }

  /**
   *  Sets the Game Transport in the Game Mechanics
   * @param GameState
   */
  /*
  public setGameTransport (gameTransport: GameState) {
    if (gameTransport.gameId === this.gameId) {
      this.communityCards = gameTransport.communityCards
      // this.Deck = gameTransport.Deck
      this.hasBet = gameTransport.hasBet
      this.turn = gameTransport.turn
      this.multiplePlayers = gameTransport.multiplePlayers
      this.Pot = gameTransport.Pot

      // if there is a premove
      if (this.multiplePlayers[this.playerLocation].action === null) {
        this.ValidatePreMove(this.multiplePlayers[this.playerLocation].action as GameAction)
      }
      return true

    }
    return false
  }
  */

  public isGameRunning () {
    return this.gameService.connected()
  }
  private send (gameAction: GameAction) {
    this.gameService.sendAction(gameAction)
  }

}
