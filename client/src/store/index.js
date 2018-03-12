import Vue from 'vue'
import Vuex from 'vuex'
import gameRequest from '@/store/GameRequest'
import users from '@/store/users'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    gameRequest,
    users
  },
  state: {
    number: 1,
    globalErrors: []
  },
  mutations: {
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
})

export default store
