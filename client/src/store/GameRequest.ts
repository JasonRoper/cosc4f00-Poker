/**
 * Game Request - Class will request games to be joined or created
 * @example
 * ``` typescript
 * // Request a game to be created and returns the GameId of the game that was requested
 * let game: GameRequest = new GameRequest()
 * let gameId = game.createGame()
 * ```
 */
import GamePaths from '@/api/gameservice'
import PokerClient from '@/api/pokerclient'

/**
 * Axios - Imports axios which is used to send POST and GET requests
 */
import axios from '@/api/axios'

/**
 * API_V1 - Holds webpath data that will interact with client
 */
import { API_V1 } from '@/config'

/**
 * GamePaths - Imports game paths
 */
/**
 * Game Request will request for a new game to be created or join specified game
 * @class - Requests new games to be created or joins games
 */

export default class GameRequest {
  public MATCHMAKING: string = API_V1 + '/matchmaking/basicGame'
  public gameId: number = -1

  public createGame (): Promise<number | void> {
    // , {})
    // , { auth: { username: 'admin', password: 'admin' } })
    const prom = axios.post(this.MATCHMAKING, {}).then((response) => {
      alert('it is looking for a game')
      this.gameId = response.data.gameId
      alert(response)
      console.log(response)
      return Promise.resolve(response.data.gameId)
    }).catch((error) => {
      alert('A game couldn\'t be found')
      console.log(error)
      return Promise.reject(error)
    })
    return prom
  }
}
