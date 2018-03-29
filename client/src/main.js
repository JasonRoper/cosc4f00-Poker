/**
 * The main module initializes the Vue application. It sets App.vue as the root vue node, and
 * add the Vuex store and the router to the application.
 */

/**
 * Include vue
 */
import Vue from 'vue'
import App from './App'
import router from './router'
import store from '@/store'
import axios from '@/api/axios'

import VModal from 'vue-js-modal'

Vue.use(VModal)
Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  store,
  router,
  render: h => h(App)
})

import Client, { PokerClient } from '@/api/pokerclient'
import webstomp from 'webstomp-client'

window.axios = axios
window.commit = store.commit
window.dispatch = store.dispatch
window.store = store
window.pokerclient = Client
window.webstomp = webstomp
window.PokerClient = PokerClient

function testAs (auth) {
  axios.get('/api/v1/users/login', {auth, withCredentials: true}).then(() => {
    Client.subscribeOn('/user/messages/game', function (message) {
      console.log('user: ', message)
    })

    Client.subscribeOn('/messages/game', function (message) {
      console.log('general: ', message)
    })

    Client.send('/app/test', 1235)
  })
}

window.testAs = testAs

