import Vue from 'vue'
import Router from 'vue-router'
import Lobby from '@/components/Lobby'
import Login from '@/components/Login'
import Game from '@/components/Game'
import TableView from '@/components/TableView'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'TableView',
      component: TableView
    },
    {
      path: '/Lobby',
      name: 'Lobby',
      component: Lobby
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/game',
      name: 'Game',
      component: Game
    },
    {
      path: '/matchMakeing',
      name: 'MatchMaking'
    }
  ]
})
