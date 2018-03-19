/**
 * The config defines values that are repeated throughout the code.
 */

 /**
  * BASE_PATH is the base path of the api.
  */
export const BASE_PATH = 'localhost:8443'

/**
 * API_V1 defines the root path that all api v1 queries will be based
 * off of.
 */
export const API_V1 = 'https://' + BASE_PATH + '/api/v1' // 'https://' + BASE_PATH + '/api/v1'

/**
 * The WEBSOCKET_PATH is the path to connect to the websocket.
 */
export const WEBSOCKET_PATH = 'wss://' + BASE_PATH + '/live'
