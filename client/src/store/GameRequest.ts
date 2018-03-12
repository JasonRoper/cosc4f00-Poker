import { API_V1 } from '@/config'
import axios from 'axios'

import GamePaths from '@/api/gameservice'
import PokerClient from '@/api/pokerclient'

export type GameJoinCallback = (gameId: number) => void

export default class GameRequest {
  public MATCHMAKING: string = API_V1 + '/matchmaking/start'
  public JOINGAME: string = API_V1 + '/casualGame/basicGame'

  public matchMakingId: number = 0
  public gameId: number = -1
  // private onGameJoinCallback: GameJoinCallback

  public createGame (): Promise<number | void> {
    const prom = axios.post(this.MATCHMAKING).then((response) => {
      alert('it is looking for a game')
      // const subPath: string = this.API_V1 + '/' + response.data.matchmakingId.toString() + '/matchmaking'
      this.gameId = response.data.gameId
      // this.onGameJoin(subPath, this.setGameId)
      console.log(response)

      return Promise.resolve(response.data.gameId)
    }).catch((error) => {
      alert('an error occurred')
      alert(error)
      console.log(error)
      return Promise.reject(error)
    })
    return prom
  }

  public joinGame (): Promise<number | void> {
    const prom = axios.post(this.JOINGAME).then((response) => {
      // const subPath: string = this.API_V1 + '/' + response.data.matchmakingId.toString() + '/matchmaking'
      this.gameId = response.data.gameId
      // this.onGameJoin(subPath, this.setGameId)
      console.log(response)
      return this.gameId
    }).catch((error) => {
      console.log(error)
    })
    return prom
  }
}

  /*
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
/*
const sum = require('./sum');

function sub(a: number, b: number): number {
  return sum(a, -b);
}

export = sub;
*/
/*
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
*/
