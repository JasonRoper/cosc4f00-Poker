import {
  GameAction, GameActionType, GameEnded, GameService, GameStarted, GameState, Player, Pot } from '@/api/gameservice'

import actions from '@/types/actions'
import { Action } from 'vuex'

class GameMechanics {
  public HasBet: boolean
  public Turn: number
  public MultiplePlayers: Player[]
  public GameId: number
  public Valid: boolean
  public Pot: Pot []
  // public Deck: Card[] | null
  public CommunityCards: string[]
  public userId: number
  public playerLocation: number
  public possibleAction: GameActionType [][] = [[GameActionType.BET, GameActionType.FOLD, GameActionType.CHECK],
    [GameActionType.CALL, GameActionType.RAISE, GameActionType.FOLD], [GameActionType.FOLD]]

  private gameService: GameService
   // Generate the websocket paths used for the given gameID
   // @param gameId - the id of the game
  constructor (gameId: number, userId: number | null) {
    this.gameService = new GameService(gameId)

    this.gameService.onGameUpdated(this.setGameTransport)
    if (userId !== null) {
      for (let index = 0; index < this.MultiplePlayers.length; index++) {
        if (this.MultiplePlayers[index].id === this.userId) {
          this.playerLocation = index
        }
      }
    }
  }

// List all events
  /**
   * sendEvent of the game started
   */
  public gameStartedEvent (gameStarted: GameStarted) {
    this.MultiplePlayers = gameStarted.MultiplePlayers

  }
  /*/
  public gameEndedEvent (gameEnded: GameEnded) {
    for (let index = 0; index < this.MultiplePlayers.length; index++) {
      if (index === gameEnded.Winner) {
        this.MultiplePlayers[index].EndGame = true
      } else {

      }
    }

    for (let index = 0; index < this.MultiplePlayers.length; index++) {
    
    }
    player of this.MultiplePlayers) {
      if (player.id === gameEnded.Winner) {
        player.EndGame = true
      } else {
        player.EndGame = false
      }
  }
  /**
   * SendCards
   * @param card1 - players first card
   * @param card2 - players second card
   */
  public SendCards (card1: string, card2: string) {
    this.MultiplePlayers[this.playerLocation].card1 = card1
    this.MultiplePlayers[this.playerLocation].card2 = card2
  }

  /**
   * SendCommunityCards
   */
  public SendCommunityCards (card1: string, card2: string, card3: string) {
    const length = this.CommunityCards.length
    if (length < 3) {
      this.CommunityCards[0] = card1
      this.CommunityCards[1] = card2
      this.CommunityCards[2] = card3
    } else if (length < 5) {
      this.CommunityCards[length] = card1
    } else {
      alert('ERROR _ YOU ARE TRYING TO ADD A CARD TO COMMUNITY CARDS WHEN THERE IS ALREADY 5')
    }
  }

  /**
   * TableActions will give the player the differnet table actions that they can take
   */
  public TableActions () {
    if (this.HasBet) {
      this.MultiplePlayers[this.playerLocation].tableAction = this.possibleAction[0]
    } else {
      this.MultiplePlayers[this.playerLocation].tableAction = this.possibleAction[1]
    }
  }

  /**
   * SelectAction - selects the specific action to be the only selected tableAction
   */
  public SelectAction (action: GameActionType) {
    this.MultiplePlayers[this.playerLocation].tableAction = action
  }

  /**
   * StorePremove - Stores the premove or resets
   */
  public StorePremove (move: GameAction) {
    if (this.ValidatePreMove(move)) {
      this.MultiplePlayers[this.playerLocation].premove = move
      this.SelectAction(move.type)
    } else {
      this.TableActions()
      this.MultiplePlayers[this.playerLocation].premove = null
    }
  }

  /**
   * ValidatePreMove
   */
  public ValidatePreMove (move: GameAction) {
    // Confirm that you have enough money
    if (this.MultiplePlayers[this.playerLocation].money > move.bet) {
      return false
    }
    // Confirm that you made a valid move
    if (this.HasBet) {
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
   *  Returns valid output of GameState for the identified user in a game
   * @param userId - holds user identifier
   * @param gameId - contains game indentifier
   * @return valid GameState object or null
   */
  /*/
  public getGameTransport (userId: number, gameId: number): GameState | null {
    const gameState: GameState = new GameState()
    gameState.CommunityCards = this.CommunityCards
    gameState.Deck = this.Deck 
    gameState.GameId = this.GameId
    gameState.HasBet = this.HasBet
    this.Turn = gameState.Turn
    this.MultiplePlayers = gameState.MultiplePlayers
    this.Valid = gameState.Valid
    this.Pot = gameState.Pot
    
    
      return gameState
    
    return null
  }

/**
 * Validate move that a specific player is playing.
 * @param userId
 * @param gameId
 * @returns valid
 */
/*/
  public validate (userId: number, gameId: number) {
    let valid = true
    valid = this.isPlayerTurn(userId)
    if (!valid) { return false }
    valid = this.isGameRunning()
    if (!valid) { return false }
    valid = this.isPlayerInGame(userId, gameId)
    if (!valid) { return false }
    return valid
  }
  /**
   * This will deal cards to the user
   * @param card1
   * @param card2
   */
  public dealCards (card1: string, card2: string) {
    this.MultiplePlayers[this.playerLocation].card1 = card1
    this.MultiplePlayers[this.playerLocation].card2 = card2
  }

  /**
   * Modifies the possible moves of a user based on whether there has been a bet at the table or not
   * @param userId
   */
/*
  public setAction (action: GameAction) {
    if (this.playerLocation === this.Turn) {
      for (const index of this.possibleAction) {
        if (index === action.type) {
          this.send(action)
        }
      }
    }
  }
*/
/*/
  public validateAction () {
    // if true then there is no premove
    if (this.gameTransport.MultiplePlayers[this.playerLocation].premove === null) {
      if (this.gameTransport.HasBet) {
        this.possibleAction = [GameActionType.CHECK, GameActionType.BET, GameActionType.FOLD]
      } else {
        this.possibleAction = [GameActionType.RAISE, GameActionType.CALL, GameActionType.FOLD]
      }
    } else {
      if (this.gameTransport.HasBet) {
        this.possibleAction = [GameActionType.CHECK, GameActionType.BET, GameActionType.FOLD]
      } else {
        this.possibleAction = [GameActionType.RAISE, GameActionType.CALL, GameActionType.FOLD]
      }

      let valid: boolean = false
      for (const index of this.possibleAction) {
        if (index === (this.gameTransport.MultiplePlayers[this.playerLocation].premove as GameAction).type) {
          valid = true
        }
      }
      if (!valid) {
        this.gameTransport.MultiplePlayers[this.playerLocation].premove = null
      }
    }
  }

/*
  public setAction (action: GameAction) {
    if (this.gameTransport.MultiplePlayers[this.playerLocation].playing) {
      if (this.playerLocation === this.gameTransport.Turn) {
        if (validateMove(action)) {
          this.send(action)
        }
        //Validate Move
      } else {
        //validateMove
        //set move 
      }
      
      //is it your turn?
        //yes
          //validate move
        //no
          //validate move
          //set move as a pre-move

      if (this.gameTransport.HasBet) {
        index.action[0].valid = false
        if (this.gameTransport.MultiplePlayers[this.playerLocation].action[1].valid) {
          
        } else if ()
        index.action[2].valid = true
      } else {
        index.action[0].valid = true
        index.action[1].valid = false
        index.action[2].valid = true
      }
    }
    
    for (const index of this.gameTransport.MultiplePlayers) {
      if (index.id === userId) {
     
      }
    }
    
    if (this.gameTransport.Turn === userId) {
      if(validAction())
    } else if (userId === ) {

    }
    //if its your turn - send to server
    //if not valid ignore
    //if valid premovepremvoe
    if ()

  }
*/
// ignore bad actions
// premove
// provide event to vue before game has started 
// update GS
/*/
  public removeInfo (userId: number) {
    for (const index of this.gameTransport.MultiplePlayers) {
      if (index.id === userId) {
        if (this.gameTransport.HasBet) {
          index.action[0].valid = false
        } else {
          index.action[1].valid = false
        }
      } else {
        index.card1 = null
        index.card2 = null
        index.move = null
        index.id = null
      }
    }
    this.gameTransport.Deck = null
  }

  public validateMove (userId: number, move: GameAction) {
    for (const index of this.gameTransport.MultiplePlayers) {
      if (index.id === userId) {
        if (move.type === GameActionType.FOLD) {
          return true
        }
        if (move.valid) {
          return true
        }
      }
    }
    return false
  }
/**
 * Validates action and sets it as a premove or sends it to a server
 * @param {GameId,Player,GameAction} PlayerEntity
 */
/*/
  public action (userId: number, gameId: number, move: GameAction) {
    if (this.validate(userId,gameId)) {
      if (this.validateMove(userId,move)) {
        this.send(move)
        return true
      }
    }
    return false
  }
  /**
   *  Sets the Game Transport in the Game Mechanics
   * @param GameState
   */
  public setGameTransport (gameTransport: GameState) {
    if (gameTransport.GameId === this.GameId) {
      this.CommunityCards = gameTransport.CommunityCards
     // this.Deck = gameTransport.Deck
      this.HasBet = gameTransport.HasBet
      this.Turn = gameTransport.Turn
      this.MultiplePlayers = gameTransport.MultiplePlayers
      this.Valid = gameTransport.Valid
      this.Pot = gameTransport.Pot
    }
    /*
    else {
      const error = new GameError('Attempted to Replace GameTransport with a different GameId')
      if (error instanceof GameError) {
        console.log(error.gameError())
      }
    // throw new GAMETRANSPORTEVENT('Attempted to Replace GameTransport with a different GameId');
    }
    */
  }

  /**
   *  Sets the Game Transport in the Game Mechanics
   * @param GameState
   */

  // is it the players turn?
  /*/
  public isPlayerTurn (userId: number) {
    const turn = this.gameTransport.Turn
    for (let i = 0;i < this.gameTransport.MultiplePlayers.length;i++) {
      if (userId === this.gameTransport.MultiplePlayers[i].id) {
        if (i === turn) {
          return true
        }
      }
    }
    return false
  }
/*/
/*/
  public isGameRunning () {
    return this.gameService.connected()
  }
  /*/
  // is this particular game running?
  /*/
  public isPlayerInGame (userId: number, gameId: number) {
    // if this is not the game you are supposed to be at it returns false
    if ((this.gameTransport.GameId === gameId)) {
      const players = this.gameTransport.MultiplePlayers
      for (const index of this.gameTransport.MultiplePlayers) {
        if (index.id === userId) {
          return true
        }
      }
    }
    return false
  }
  // can this game accecpt action from this player
  /*/
  private send (gameAction: GameAction) {
    this.gameService.sendAction(gameAction)
  }

}
