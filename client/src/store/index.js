import Vue from 'vue'
import Vuex from 'vuex'
import router from '@/router'
Vue.use(Vuex)

export const store = new Vuex.Store({
  state: {name: '', id: '', password: ''},
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
  },
  actions: {
    storeName (context, name) {
      console.log('clicked')
      if ((name.length > 0)) {
        context.commit('setPlayer', name)
        router.push('Game')
      }
      console.log(this.state.name)
    }
  }
})

export default store
