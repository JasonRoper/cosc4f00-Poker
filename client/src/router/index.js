import Vue from 'vue'
import Router from 'vue-router'
import Table from '@/components/Table'
import Lobby from '@/components/Lobby'
import WinLoss from '@/components/WinLoss'
import NextGame from '@/components/NextGame'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Table',
      component: Table
    },
    {
      path: '/NextGame',
      name: 'NextGame',
      component: NextGame
    },
    {
      path: '/Lobby',
      name: 'Lobby',
      component: Lobby
    },
    {
      path: '/WinLoss',
      name: 'WinLoss',
      component: WinLoss
    }
  ]
})
