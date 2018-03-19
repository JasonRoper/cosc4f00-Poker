/**
 * The main module initializes the Vue application. It sets App.vue as the root vue node, and
 * add the Vuex store and the router to the application.
 */


/**
 * Need to import axios first in order to set defaults
 */
import axios from 'axios'

// we need cookies to be attached
axios.defaults.withCredentials = true

/**
 * Include vue
 */
import Vue from 'vue'
import App from './App'
import router from './router'
import store from '@/store'

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

import PokerClient from '@/api/pokerclient'
import webstomp from 'webstomp-client'

window.axios = axios
window.commit = store.commit
window.dispatch = store.dispatch
window.store = store
window.pokerclient = PokerClient
window.webstomp = webstomp

function testAs (auth) {
  axios.get('/api/v1/users/login', {auth, withCredentials: true}).then(() => {
    PokerClient.subscribeOn('/user/messages/game', function (message) {
      console.log('user: ', message)
    })

    PokerClient.subscribeOn('/messages/game', function (message) {
      console.log('general: ', message)
    })

    PokerClient.send('/app/test', 1235)
  })
}

window.testAs = testAs

