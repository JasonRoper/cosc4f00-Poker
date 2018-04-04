/**
 * The users vuex module handles everything to do with user accounts.
 * The username and password are stored here, as well as if the
 * user account is in an error state.
 */

/**
 * users uses axios in order to send http requests to the server
 */
import axios from '@/api/axios'

/**
 * the path that is to be requested is defined in the config.
 */
import {
  API_V1
} from '@/config'

/**
 * Need to reconnect with the websocket whenever the authentication state changes
 */
import PokerClient from '@/api/pokerclient'

/**
 * paths defines all of the user paths that are used in this module
 */
const paths = {
  LOGIN: API_V1 + '/users/login',
  REGISTER: API_V1 + '/users',
  LOGOUT: API_V1 + '/users/logout'
}

/**
 * The state is the state of the users Vuex module. It stores everything
 * about the logged in user, as well as the user account error state.
 */
const state = {
  username: null,
  userId: null,
  status: true,
  errors: {
    login: [],
    registration: []
  }
}

/**
 * The users module getters are helper methods for accessing the user
 * state.
 */
const getters = {
  /**
   * loggedIn tests wether or not there currently is a user logged in.
   * @param {UserState} state - the User state
   */
  loggedIn (state) {
    return state.username != null
  }
}

/**
 * set the user relevant fields in the state
 * @param {UserState} state - the UserState
 * @param {username: string, userId: number, email: string} fields - the fields to set
 */
function assignUserFields (state, fields) {
  state.username = fields.username
  state.userId = fields.userId
}

/**
 * The mutations are all of the ways that the user state can be changed.
 */
const mutations = {
  /**
   * Set all of fields related to the user.
   * @param {UserState} state - the user state
   * @param payload - the username, password, userId and email of the user
   */
  setUser (state, payload) {
    assignUserFields(state, payload)
  },
  /**
   * set all of the user fields to null.
   * @param {UserState} state - the user state
   */
  logout (state) {
    assignUserFields(state, {
      username: null,
      userId: null
    })
  },
  /**
   * add a login error to the list
   * @param {UserState} state - the user state
   * @param {string} error - the message about the error that occured.
   */
  addLoginError (state, error) {
    state.errors.login.push(error)
  },
  /**
   * add a registration error to the list
   * @param {UserState} state - the user state
   * @param {*} error - the error that occured. Includes the field that
   *                    the error occured on, and the message
   */
  addRegistrationError (state, error) {
    state.errors.registration.push(error)
  },
  /**
   * reset all errors that have occured during login
   * @param {UserState} state - the user state
   */
  resetLoginErrors (state) {
    state.errors.login = []
  },
  /**
   * reset all errors that have occured during registration
   * @param {UserState} state - the user state
   */
  resetRegistrationErrors (state) {
    state.errors.registration = []
  },
  /**
   * reset all global errors
   * @param {UserState} state - the user state
   */
  resetGlobalErrors (state) {
    state.errors.global = []
  }
}

/**
 * The actions are the user actions that can be taken. These can be asychonous,
 * and can call mutations when they complete.
 */
const actions = {
  /**
   * Tell the api to forget our session cookie, and, if successful, clear
   * the state of the user
   * @param {Vuex} - the vuex context
   */
  logout (context) {
    axios.post(paths.LOGOUT).then((response) => {
      context.commit('logout')
      // need to reconnect to the websocket in order to remove the previous authentication
      PokerClient.reconnect()
    })
  },
  /**
   * test to see if we are logged in (via session cookie), if we are,
   * set the user fields.
   *
   * @param {Vuex} context - the vuex context
   */
  verifyLoginState (context) {
    axios.get(paths.LOGIN).then((response) => {
      const loginInfo = {
        userId: response.data.id,
        username: response.data.username
      }
      context.commit('setUser', loginInfo)

      // don't need to reconnect to the pokerclient here because if we have an
      // active session, the pokerclient will have connected with that session
      // even before this login request is called.
    }).catch(() => {
      context.commit('logout')
    })
  },
  /**
   * log in to the server. This is just a validation function due
   * to the use of HttpBasic authentication
   * @param {Vuex} context - the vuex context
   * @param {LoginFields} loginInfo - the username and password
   */
  login (context, loginInfo) {
    context.commit('resetLoginErrors')
    if (context.getters.loggedIn) {
      context.commit('logout')
    }

    axios.get(paths.LOGIN, {
      auth: {
        username: loginInfo.username,
        password: loginInfo.password
      }
    }).then(function (response) {
      loginInfo.userId = response.data.id
      context.commit('setUser', loginInfo)
      // need to reconnect to the websocket in order to have the new authentication
      // associated with it
      PokerClient.reconnect()
    }).catch(function (reason) {
      if (!reason.response) {
        context.commit('addGlobalError', {
          error: 'NetworKError',
          module: 'users',
          reason: reason
        })
        return
      }

      let error = reason.response
      switch (error.status) {
        case 401:
          context.commit('addLoginError', {
            error: 'AuthenticationError',
            message: 'Error Authenticating'
          })
          break
        default:
          context.commit('addGlobalError', {
            error: 'UnknownLoginError',
            module: 'users',
            reason: reason
          })
      }
    })
  },
  /**
   * Register a new user
   *
   * the server will respond with an error when:
   *
   * 1. the username is < 3 characters or > 20
   * 2. the email does not represent an actual email
   * 3. the password is less than 5 characters long
   * 4. the username already exists
   * 5. the email already exists
   *
   * @param {Vuex} context - the vuex context
   * @param {RegistrationFields} registrationFields - the username, email and password of the user
   */
  register (context, registrationFields) {
    if (context.getters.loggedIn) {
      context.commit('logout')
    }

    context.commit('resetRegistrationErrors')
    axios.post(paths.REGISTER, registrationFields)
      .then(function (response) {
        // success - the response body contains a userId, username and email - need to add password
        context.commit('setUser', {
          username: response.data.username,
          userId: response.data.id
        })

        // need to reconnect to the websocket in order to have the new authentication
        // associated with it
        PokerClient.reconnect()
      }).catch(function (reason) {
        if (!reason.response) {
          console.log('unhandled error occured: ', reason)
          context.commit('addGlobalError', {
            error: 'NetworkError',
            module: 'users',
            reason: reason
          })
          return
        }

        const body = reason.response.data

        switch (body.exception) {
          case 'com.pokerface.pokerapi.users.EmailAlreadyExistsException':
            context.commit('addRegistrationError', {
              field: 'email',
              message: body.message
            })
            break
          case 'com.pokerface.pokerapi.users.UsernameAlreadyExistsException':
            context.commit('addRegistrationError', {
              field: 'username',
              message: body.message
            })
            break
          case 'org.springframework.web.bind.MethodArgumentNotValidException':
            // In the case of a validation error, the server will return a list
            // of errors that occured, iterate through all of them, and set the
            // error state accordingly
            for (var error of body.errors) {
              context.commit('addRegistrationError', {
                field: error.field,
                message: error.field + ' ' + error.defaultMessage
              })
            }
            break
          default:
            context.commit('addGlobalError', {
              error: 'UnknownRegistrationError',
              module: 'users',
              reason: reason
            })
        }
      })
  }
}

const currentlyInGame = false

export default {
  state,
  getters,
  mutations,
  actions,
  currentlyInGame
}
