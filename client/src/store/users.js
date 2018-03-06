import axios from 'axios'
import { API_V1 } from '@/config'

const paths = {
  LOGIN: API_V1 + '/users/login',
  REGISTER: API_V1 + '/users'
}

const state = {
  username: null,
  password: null,
  userId: null,
  email: null,
  status: true,
  errors: {
    login: [],
    registration: []
  }
}

const getters = {
  loggedIn (state) {
    return state.username != null
  }
}

/**
 * set the user relevant fields in the state
 * @param {UserState} state - the UserState
 * @param {username: string, password: string, userId: number, email: string} fields - the fields to set
 */
function assignUserFields (state, fields) {
  state.username = fields.username
  state.password = fields.password
  state.userId = fields.userId
  state.email = fields.email
}

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
    assignUserFields(state, {username: null, password: null, userId: null, email: null})
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

const actions = {
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

    axios.get(paths.LOGIN,
      {
        auth: {
          username: loginInfo.username,
          password: loginInfo.password}
      }
    ).then(function (response) {
      loginInfo.userId = response.data.id
      context.commit('setUser', loginInfo)
    }).catch(function (reason) {
      if (!reason.response) {
        context.commit('addGlobalError', {error: 'NetworKError', module: 'users', reason: reason})
        return
      }

      let error = reason.response.data.error
      switch (error) {
        case 'Unauthorized':
          context.commit('addLoginError', 'Authentication error')
          break
        default:
          context.commit('addGlobalError', {error: 'UnknownLoginError', module: 'users', reason: reason})
      }
    })
  },
  /**
   * Register a new user
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
        userId: response.data.id,
        email: response.data.email,
        password: registrationFields.password })
    }).catch(function (reason) {
      if (!reason.response) {
        console.log('unhandled error occured: ', reason)
        context.commit('addGlobalError', {error: 'NetworkError', module: 'users', reason: reason})
        return
      }

      const body = reason.response.data

      switch (body.exception) {
        case 'com.pokerface.pokerapi.users.EmailAlreadyExistsException':
          context.commit('addRegistrationError', { field: 'email', message: body.message })
          break
        case 'com.pokerface.pokerapi.users.UsernameAlreadyExistsException':
          context.commit('addRegistrationError', { field: 'username', message: body.message })
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
          context.commit('addGlobalError', {error: 'UnknownRegistrationError',
            module: 'users',
            reason: reason})
      }
    })
  }
}

export default {
  state,
  getters,
  mutations,
  actions
}
