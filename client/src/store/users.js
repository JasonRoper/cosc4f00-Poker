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
}

const mutations = {
  /**
   * Set all of fields related to the user.
   * @param {UserState} state - the user state
   * @param payload - the username, password, userId and email of the user
   */
  setUser (state, payload) {
    state.username = payload.username
    state.password = payload.password
    state.userId = payload.userId
    state.email = payload.email
  },
  /**
   * set all of the user fields to null.
   * @param {UserState} state - the user state
   */
  logout (state) {
    this.setUser(state, {username: null, password: null, userId: null, email: null})
  },
  /**
   * add a login error to the list
   * @param {UserState} state - the user state
   * @param {*} error - the error that occured. Includes the field that
   *                    the error occured on, and the message
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
    axios.get(paths.LOGIN,
      {
        auth: {
          username: loginInfo.username,
          password: loginInfo.password}
      }
    ).then(function (response) {
      loginInfo.userId = response.data.id
      context.commit('setUser', loginInfo)
      context.commit('resetLoginErrors')
    }).catch(function (reason) {
      context.commit('addLoginError', {field: 'username', message: 'invalid username'})
    })
  },
  /**
   * Register a new user
   *
   * @param {Vuex} context - the vuex context
   * @param {RegistrationFields} registrationFields - the username, email and password of the user
   */
  register (context, registrationFields) {
    context.commit('resetRegistrationErrors')
    axios.post(paths.REGISTER, registrationFields)
    .then(function (response) {
      // success - the response body contains a userId, username and email - need to add password
      context.commit('setUser', { ...response.data, password: registrationFields.password })
    }).catch(function (reason) {
      console.log('reason: ', reason)
      const body = reason.response.body

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
