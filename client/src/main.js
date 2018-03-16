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
import axios from 'axios'

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

window.axios = axios
window.commit = store.commit
window.dispatch = store.dispatch
window.store = store
window.pokerclient = PokerClient
