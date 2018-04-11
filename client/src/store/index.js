/**
 * The store index defines the Vuex store and initializes each of the modules.
 * It also defines a global state and the mutation that will act on the global state.
 */

/**
 * Vue must be set to use Vuex before a new Vue is created.
 */
import Vue from 'vue'
import Vuex from 'vuex'
import gameRequest from '@/store/GameRequest'
import users from '@/store/users'

Vue.use(Vuex)

/**
 * The modules object defines all of the vuex modules that are being used.
 */
const modules = {
  gameRequest,
  users
}

/**
 * The state is the global vuex state. This is used for properties that do not fall
 * under the jurisdiction of a module.
 */
const state = {
  number: 1,
  globalErrors: []
}

/**
 * The mutations are the ways that it is possible to change the global state.
 */
const mutations = {
  /**
   * global errors that have no better place, go here. In general, that means errors
   * that cannot be handled more specifically - ie Unkown Errors
   * @param {GlobalState} state - the global state
   * @param {error: string, module: string, reason: {*}} error - the error that occured
   */
  addGlobalError (state, error) {
    state.globalErrors.push(error)
  }
}

/**
 * The store is the global Vuex store. it is initialized with all of the vuex modules, as well as the
 * global state and mutations.
 */
const store = new Vuex.Store({
  modules,
  state,
  mutations
})

function doInitialSetup () {
  // check to see if the user already has an active session, and if they do, initialize it.
  return Promise.all([store.dispatch('verifyLoginState').then(() => console.log('finished checking if logged in'))])
}

export const setupComplete = doInitialSetup()
export default store
