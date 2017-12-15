import Vue from 'vue'
import Vuex from 'vuex'
import App from './App'
import router from './router'
import { store } from './store'
import 'bootstrap/dist/css/bootstrap.css'

Vue.config.productionTip = false

Vue.use(Vuex)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store: store,
  render: h => h(App)
})
