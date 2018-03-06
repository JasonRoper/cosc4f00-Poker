import Vue from 'vue'
import Router from 'vue-router'
import Lobby from '@/components/Lobby'
import NavBar from '@/components/NavBar'
import Game from '@/components/Game'
import TableView from '@/components/TableView'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: NavBar
    },
    {
      path: '/Home',
      name: 'Home',
      component: NavBar
    },
    {
      path: '/Lobby',
      name: 'Lobby',
      component: Lobby
    },
    {
      path: '/Table',
      name: 'TableView',
      component: TableView
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
