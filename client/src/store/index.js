import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)
export const store = new Vuex.Store({
  state: {name: '', id: ''},
  mutations: {
    setId (state, userId) {
      state.id = userId
    },
    setPlayer (state, userName) {
      state.name = userName
    }
  },
  getters: {
    user (state) {
      return state.name
    }
  }
})

export default store
