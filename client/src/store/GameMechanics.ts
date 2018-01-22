import { GameAction, GameActionType, GameService, GameState, Player, PlayerEntity } from '@/api/gameservice'

class GameError extends Error {
  constructor (m: string) {
    super(m)

    // Set the prototype explicitly.
    Object.setPrototypeOf(this, GameError.prototype)
  }

  public actionError () {
    return 'ActionError: ' + this.message
  }
  public gameError () {
    return 'GameError: ' + this.message
  }
}

class GameMechanics {
  private GAME_TRANSPORT: GameState
  private GAME_SERVICE: GameService
   // Generate the websocket paths used for the given gameID
   // @param gameId - the id of the game
  constructor (gameId: number) {
    this.GAME_SERVICE = new GameService(gameId)
    this.GAME_SERVICE.onGameUpdated(this.setGameTransport)
  }

  /**
   *  Gets the Game Transport in the Game Mechanics
   * @return GameState
   */
  public getGameTransport (userId: number, gameId: number) {
    if (this.removeInfo(userId) && this.validate(userId, gameId)) {
      return this.GAME_TRANSPORT
    }
    return null
  }
  public removeInfo (userId: number) {
    for (const index of this.GAME_TRANSPORT.MultiplePlayers) {
      if (index.id === userId) {
        if (this.GAME_TRANSPORT.HasBet) {
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
    this.GAME_TRANSPORT.Deck = null
  }

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

  public validateMove (userId: number, move: GameAction) {
    for (const index of this.GAME_TRANSPORT.MultiplePlayers) {
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
    // this.GAME_SERVICE.onGameUpdated()
    if (gameTransport.GameId === this.GAME_TRANSPORT.GameId) {
      this.GAME_TRANSPORT = gameTransport
      this.validatePreMove()
    } else {
      const error = new GameError('Attempted to Replace GameTransport with a different GameId')
      if (error instanceof GameError) {
        console.log(error.gameError())
      }
    // throw new GAMETRANSPORTEVENT('Attempted to Replace GameTransport with a different GameId');
    }
  }

  /**
   *  Sets the Game Transport in the Game Mechanics
   * @param GameState
   */

  // is it the players turn?
  public isPlayerTurn (userId: number) {
    const turn = this.GAME_TRANSPORT.Turn
    for (let i = 0;i < this.GAME_TRANSPORT.MultiplePlayers.length;i++) {
      if (userId === this.GAME_TRANSPORT.MultiplePlayers[i].id) {
        if (i === turn) {
          return true
        }
      }
    }
    return false
  }

  public isGameRunning () {
    return this.GAME_SERVICE.connected()
  }
  // is this particular game running?
  public isPlayerInGame (userId: number, gameId: number) {
    // if this is not the game you are supposed to be at it returns false
    if ((this.GAME_TRANSPORT.GameId === gameId)) {
      const players = this.GAME_TRANSPORT.MultiplePlayers
      for (const index of this.GAME_TRANSPORT.MultiplePlayers) {
        if (index.id === userId) {
          return true
        }
      }
    }
    return false
  }
  // can this game accecpt action from this player
  private send (GameAction: GameAction) {
    this.GAME_SERVICE.sendAction(GameAction)
  }

}
