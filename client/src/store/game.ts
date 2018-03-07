/*import axios from 'axios'
import GamePaths from '@/api/gameservice'
import store from '@/store/index.js'
import PokerClient from '@/api/pokerclient'

export type GameJoinCallback = (matchMakingId: number) => void

export default class gamerequest {
  public userId: number
    public BASE_PATH: string = 'https://localhost:8443'
    public API_V1: string = this.BASE_PATH + '/api/v1'
    public MATCHMAKING: string = this.API_V1 + '/matchmaking/basicGame'
    public matchMakingId: number = 0
    private onGameJoinCallback: GameJoinCallback
  
  constructor () {
    this.userId = parseInt(store.state.id)
    //this.matchMakingId = 0
  }

  public getMatchMakingId () {
    let self = this

      axios.post(this.MATCHMAKING)
      .then(function (response)
       {
         // This creates the path and registers the function that will run
         const subPath: string = self.API_V1 + '/' + response.data.matchmakingId.toString() + '/matchmaking'
        self.matchMakingId = response.data.matchmakingId
        self.onGameJoin(subPath, self.joinGame)
        console.log(response);

      })
      .catch(function (error) {
        console.log(error);
      });
  }

  public joinGame() {
    //This is just subscribing to the 
    //Now I need to change the route and pass the route to 
    response.data.gameId
    //Switch Vue to TableVue
    router.push()//with params
    this.$router.push('/home')
    $router.push()
    router.push()
    //
  }
    /**
   * Register a callback to be called when the game is updated
   * @param callback - will be called when the game updates
   
  public onGameJoin (subPath: string, callback: GameJoinCallback) {
    PokerClient.switchCallback(
      subPath,
      this.onGameJoinCallback,
      callback)
    this.onGameJoinCallback = callback
  }
  public onGameJoin (subPath: string, callback: GameJoinCallback) {
    PokerClient.switchCallback(
      subPath,
      this.onGameJoinCallback,
      callback)
    this.onGameJoinCallback = callback
  


}
*/





