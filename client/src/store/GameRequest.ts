/**
 * Game Request - Class will request games to be joined or created
 * @example
 * ``` typescript
 * // Request a game to be created and returns the GameId of the game that was requested
 * let game: GameRequest = new GameRequest()
 * let gameId = game.createGame()
 * ```
 */

/**
 * Axios - Imports axios which is used to send POST and GET requests
 */
import axios from '@/api/axios'
import GamePaths from '@/api/gameservice'
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
  public MATCHMAKING: string = API_V1 + '/matchmaking/competitiveGame'
  public CASUAL: string = API_V1 + '/matchmaking/casualGame'
  public gameId: number = -1

  public createCompetitiveGame (): Promise<number | void> {
    // , {})
    // , { auth: { username: 'admin', password: 'admin' } })
    const prom = axios.post(this.MATCHMAKING, {}).then((response) => {
      this.gameId = response.data.gameId
      return Promise.resolve(response.data.gameId)
    }).catch((error) => {
      console.log(error)
      return Promise.reject(error)
    })
    return prom
  }
  public createCasualGame (): Promise<number | void> {
    // , {})
    // , { auth: { username: 'admin', password: 'admin' } })
    const prom = axios.post(this.CASUAL, {}).then((response) => {
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

  public removeCompetitiveGame (gameId: number, userId: number): Promise<any> {
    const removeMATCHMAKING = API_V1 + '/games/' + gameId + '/' + userId
    const remove = axios.delete(removeMATCHMAKING, {
    }).then((responce) => {
      console.log(responce)
      console.log('successfully deleted the player ' + userId + ' in game ' + gameId)
      return Promise.resolve(responce)
    }).catch((error) => {
      console.log(error)
      console.log('failed to delete the player ' + userId + ' in game ' + gameId)
      return Promise.reject(error)
    })
    return remove
  }
}
