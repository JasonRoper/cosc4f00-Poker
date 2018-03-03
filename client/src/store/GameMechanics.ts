import {
  GameAction,
  GameActionType,
  GameError,
  GameFinished,
  GameService,
  GameStarted,
  GameState,
  Player,
  Pot,
  UserCards
 } from '@/api/gameservice'

import actions from '@/types/actions'
import CardSuite from '@/types/cards'
import { Action } from 'vuex'

export default class GameMech {
  // public numberOfPlayers: number = 5
  // public gameTransport: GameState
  public hasBet: boolean = false
  public turn: number = 0
  public multiplePlayers: Player[] = []
  public temp: string = ''
  public gameId: number = 0
  public pot: Pot [] = []
  public deck: string | null = ''
  public communityCards: string[] = []
  public userId: number | null = 0
  public foldAction: number = 0
  public callAction: number = 0
  public betAction: number = 0
  public raiseAction: number = 0
  public checkAction: number = 0
  public possibleAction: GameActionType [][] = [[GameActionType.BET, GameActionType.FOLD, GameActionType.CHECK],
    [GameActionType.CALL, GameActionType.RAISE, GameActionType.FOLD], [GameActionType.FOLD]]
  public playerLocation: number = -1
  public userAction: GameAction | null = null
  public disable: number = 1
  public enable: number = 0
  public endGame: boolean | null = null
  private lobby: boolean

  private gameService: GameService
   // Generate the websocket paths used for the given gameId
   // @param gameId - the id of the game
  constructor (gameId: number, userId?: number) {
    this.gameService = new GameService(gameId, userId)
    this.setDefaultTransport()
    if (userId) {
      this.gameService.onGameUpdated(this.setGameTransport)
      this.lobby = false
    } else {
      this.lobby = true
    }
  }

  public setDefaultTransport () {
    this.hasBet = false
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
      isPlayer: false,
      currentBet: 0,
      isDealer: false
    }, this.defaultPlayer(),this.defaultPlayer(),this.defaultPlayer(),this.defaultPlayer()]
    this.gameId = 0
    this.pot = []
    this.communityCards = [CardSuite.CLUBS_ACE, CardSuite.CLUBS_EIGHT, 'three', 'four', 'five']
    this.temp = 'temoe'
    this.userId = 0

    for (const player of this.multiplePlayers) {
      if (player.id === this.userId) {
        this.playerLocation = player.id
        break
      }
    }
    this.userAction = null
  }

  public defaultPlayer (): Player {
    const player: Player = {
      id: -1,
      money: 0,
      name: 'defaultPlayer',
      tableAction: {} as GameActionType[],
      premove: null,
      card1: CardSuite.BLANK_CARD,
      card2: CardSuite.BLANK_CARD,
      isPlayer: false,
      currentBet: 0,
      isDealer: false
    }
    return player
  }

  public getTableAction () {
    return this.multiplePlayers[this.playerLocation].tableAction
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
   * Adds cards to the Comminity Cards
   * @param card
   */
  public onGameComminityCardsEvent (card: string) {
    if (this.communityCards.length <= 5) {
      this.communityCards.push(card)
    }
  }

  /**
   * sendEvent of the game started
   */
  public onGameStartedEvent (gameStarted: GameStarted) {
    this.multiplePlayers = gameStarted.multiplePlayers
  }

  /**
   * Game finished event
   * @param gameFinished
   */
  public onGameFinishedEvent (gameFinished: GameFinished) {
    for (const player of this.multiplePlayers) {
      if (player.id === gameFinished.winner) {
        this.endGame = true
      } else {
        this.endGame = false
      }
    }
  }

  public onGameError (gameError: GameError) {
    alert('Error: ' + gameError.error)
    console.log('Error: ' + gameError.error)
  }
  // 1. onGameError
  // 2. onGameStarted
  // 3. onGameFinished
  // 4. onDeal
  // 5. onGameUpdated

  /**
   * storePremove - Stores the premove or resets
   */
  public storePremove (action: GameActionType, money: number): boolean {
    const move: GameAction = { type: action, bet: money }
    // this.userAction = move
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

  /**
   * This will deal cards to the user
   * @param card1
   * @param card2
   */
  public dealCards (card1: string, card2: string) {
    this.multiplePlayers[this.playerLocation].card1 = card1
    this.multiplePlayers[this.playerLocation].card2 = card2
  }

  public sendAction () {
    if (this.userAction !== null) {
      // if (this.storePremove(this.userAction.type, this.userAction.bet)) {
        // if (this.playerLocation === this.turn) {
      // if (this.multiplePlayers[this.playerLocation].playing) {
      this.send(this.userAction)
      this.userAction = null
      // }
        // }
      // }
    }
    // action: GameActionType, money: number) {
    // const move: GameAction = { type: action, bet: money }
  }
  public getAction () {
    const action = this.userAction
    if (action === null) {
      alert('action is null')
      return null
    } else {
      alert('action is valid')
      return this.userAction
    }
  }
  public getUser (): Player {
    return this.multiplePlayers[this.playerLocation]
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
      // this.Pot = gameTransport.Pot
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
  public tempAction () {
    const m: GameAction = { type: GameActionType.BET, bet: 15 }
    this.userAction = m
  }
  public isGameRunning () {
    return this.gameService.connected()
  }

  public finish () {
    this.gameService.finish()
  }

  // can this game accecpt action from this player
  private send (gameAction: GameAction) {
    this.gameService.sendAction(gameAction)
  }
}
