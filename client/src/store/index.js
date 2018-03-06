import Vuex from 'vuex'
import Vue from 'vue'
import game from '@/store/game'
import users from '@/store/users'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    game,
    users
  },
  state: {
    number: 1
  },
  mutations: {
    add (state) {
      state.number += 1
    }
  }
})

export default store
