import Vue from 'vue'
import App from './App'
import router from './router'
import store from '@/store'
import axios from 'axios'

import 'bootstrap/dist/css/bootstrap.css'
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

window.axios = axios
window.commit = store.commit
window.dispatch = store.dispatch
window.store = store
