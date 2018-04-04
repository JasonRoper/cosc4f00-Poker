/**
 * The vue router handles the different routes in the single page application.
 * when a different path is visted, it will insert the component defined in routes
 * into the page.
 */

 /**
  * Need to set up vue to use the Vue router
  */
import Vue from 'vue'
import Router from 'vue-router'
import Lobby from '@/components/Lobby'
import NavBar from '@/components/NavBar'
import Game from '@/components/Game'
import InstructionalVideo from '@/components/InstructionalVideo'
import TableView from '@/components/TableView'
Vue.use(Router)

export default new Router({
  mode: 'history',
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
      path: '/Table/:gameId',
      name: 'TableView',
      component: TableView
    },
    {
      path: '/game',
      name: 'Game',
      component: Game
    },
    {
      path: '/VideoTutor',
      name: 'VideoTutor',
      component: InstructionalVideo
    },
    {
      path: '/matchMakeing',
      name: 'MatchMaking'
    }
  ]
})
