import axios from 'axios'

import GamePaths from '@/api/gameservice'
import PokerClient from '@/api/pokerclient'
import store from '@/store/index.js'

export type GameJoinCallback = (gameId: number) => void

export default class GameRequest {
  public userId: number
  public BASE_PATH: string = 'https://localhost:8443'
  public API_V1: string = this.BASE_PATH + '/api/v1'
  public MATCHMAKING: string = this.API_V1 + '/matchmaking/basicGame'
  public matchMakingId: number = 0
  public gameId: number = -1
  private onGameJoinCallback: GameJoinCallback

  constructor () {
    this.userId = parseInt(store.state.id, 10)
  }

  public getMatchMakingId () {
    axios.post(this.MATCHMAKING).then((response) => {
      const subPath: string = this.API_V1 + '/' + response.data.matchmakingId.toString() + '/matchmaking'
      this.matchMakingId = response.data.matchmakingId
      // this.onGameJoin(subPath, this.setGameId)
      console.log(response)
    }).catch((error) => {
      console.log(error)
    })
  }

  public setGameId (gameId: number) {
    this.gameId = gameId
    // Then you push to the router
  }
  /**
   * Register a callback to be called when the game is updated
   * @param callback - will be called when the game updates
   */
  public onGameJoin (subPath: string, callback: GameJoinCallback) {
    PokerClient.switchCallback(
      subPath,
      this.onGameJoinCallback,
      callback)
    this.onGameJoinCallback = callback
  }
}

  /*
  public onGameJoin (subPath: string, callback: GameJoinCallback) {
    PokerClient.switchCallback(
      subPath,
      this.onGameJoinCallback,
      callback)
    this.onGameJoinCallback = callback
}
*/

export class TestAxios {

  // public axios = require('axios')
  public ret: string = ''
  public abc: string = ''
  // this.axios.ge
  constructor () {
    axios({
      method: 'get',
      url: 'https://api.github.com/users/codeheaven-io'
    }).then((response) => {
      this.ret = response.data
    }).catch((response) => { this.abc = response.data })
  }
}
