import Vuex from 'vuex'
import game from '@/store/game'
import users from '@/store/users'

const store = new Vuex.Store({
  modules: {
    game,
    users
  }
})

export default store
