import { GameService,GameState,GameAction,GameActionType,Player, PlayerEntity } from "@/api/gameservice";

class GameError extends Error {
  constructor(m: string) {
      super(m);

      // Set the prototype explicitly.
      Object.setPrototypeOf(this, GameError.prototype);
  }

  actionError() {
      return "ActionError: " + this.message;
  }
  gameError() {
    return "GameError: " + this.message;
  }
}

class GameMechanics {
  private GAME_TRANSPORT: GameState
  private GAME_SERVICE: GameService
   // Generate the websocket paths used for the given gameID
   // @param gameId - the id of the game
  
  constructor (gameId: number) {
    this.GAME_SERVICE = new GameService(gameId);
    this.GAME_SERVICE.onGameUpdated(this.setGameTransport);
  }

/**
 * Validates action and sets it as a premove or sends it to a server
 * @param {GameId,Player,GameAction} PlayerEntity
 */
  public action(PlayerEntity: PlayerEntity)
  {
    if(this.isPlayerTurn(PlayerEntity.Player,this.GAME_TRANSPORT.Turn))
    {
      if(this.validate(PlayerEntity))
      {
        this.send(PlayerEntity.GameAction)
        return true;
      }
      return false;
    }
    else{
      if(this.validate(PlayerEntity))
      {
        PlayerEntity.Player.move = PlayerEntity.GameAction
        return true;
      }
      return false;
    }
  }


  private send(GameAction: GameAction)
  {
    this.GAME_SERVICE.sendAction(GameAction);
  }


  /**
   *  Sets the Game Transport in the Game Mechanics
   * @param GameState 
   * 
   */
  public setGameTransport(gameTransport: GameState)
  {
    //this.GAME_SERVICE.onGameUpdated()
    if(gameTransport.GameId===this.GAME_TRANSPORT.GameId)
    {
      this.GAME_TRANSPORT = gameTransport
      this.validatePreMove()
      
    }else{
    
      let error = new GameError("Attempted to Replace GameTransport with a different GameId");
      if(error instanceof GameError){
        console.log(error.gameError());
      }
    //throw new GAMETRANSPORTEVENT('Attempted to Replace GameTransport with a different GameId');
    }
  }

  /**
   *  Gets the Game Transport in the Game Mechanics
   * @return GameState 
   */
  public getGameTransport()
  {
    return this.GAME_TRANSPORT;
  }

  public validatePreMove()
  {
    var players = this.GAME_TRANSPORT.MultiplePlayers

    for(let user of players)
    {
      if(!(this.validateAction(user,user.move,this.GAME_TRANSPORT.HasBet)))
      {
        user.move.type=null;
        user.move.bet=0;
      }
    }
  }



 
  
  public validate(PlayerEntity: PlayerEntity)
  {
    let valid = true;

    valid = this.validateAction(PlayerEntity.Player,PlayerEntity.GameAction,this.GAME_TRANSPORT.HasBet)
    if(!valid){return false}
    valid = this.isPlayerTurn(PlayerEntity.Player,this.GAME_TRANSPORT.Turn)
    if(!valid){return false}
    valid = this.isGameRunning()
    if(!valid){return false}
    valid = this.isPlayerInGame(PlayerEntity);
    if(!valid){return false}
    valid = this.actionByPlayer(PlayerEntity)
    if(!valid){return false}

    return valid
  }





  /**
   *  Sets the Game Transport in the Game Mechanics
   * @param GameState 
   * 
   */
  public validateAction(Player: Player,Action: GameAction,HasBet: boolean)
  {

    switch(Action.type)
    {
      case 'FOLD':
        return true;
      case 'CALL':
      case 'RAISE':
        if(HasBet)
        {
          return true;
        }
        else{
          return false;
        }
      case 'CHECK':
      case 'BET':
        if(HasBet)
        {
          return false
        }
        else{
          return true
        }
      default:
      let error = new GameError("Got a different Action Type than expected");
      if(error instanceof GameError){
        console.log(error.actionError());
      }
      
    }

return false;
  }
 
  //is it the players turn?
  public isPlayerTurn(Player: Player, Turn: number)
  {
    if(Player.id===Turn)
    {
      return true
    }
    return false
  }

  public isGameRunning()
  {
    return this.GAME_SERVICE.connected();
  }





  //is this particular game running?
  public isPlayerInGame(PlayerEntity: PlayerEntity)
  {
    //if this is not the game you are supposed to be at it returns false
    if(!(this.GAME_TRANSPORT.GameId===PlayerEntity.GameId))
    {
      return false
    }    
    var players = this.GAME_TRANSPORT.MultiplePlayers

      for(var i=0;i<players.length;i++)
      {
        if(players[i].id==PlayerEntity.Player.id)
        {
          return true
        }
      }
    return false
  }




  //can this game accecpt action from this player
  public actionByPlayer(PlayerEntity: PlayerEntity)
  {
    var players = this.GAME_TRANSPORT.MultiplePlayers
    
    //if it is your turn and the Game you are apart of is the current one
      if(this.GAME_TRANSPORT.Turn===PlayerEntity.Player.id && PlayerEntity.GameId=== this.GAME_TRANSPORT.GameId)
      {
        return true
      }
    return false
  }

}
