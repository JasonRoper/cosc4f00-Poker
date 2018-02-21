import Vue from 'vue'
import Vuex from 'vuex'
import App from './App'
import router from './router'
import { store } from './store'
import 'bootstrap/dist/css/bootstrap.css'
import VModal from 'vue-js-modal'

Vue.use(VModal)
Vue.config.productionTip = false

Vue.use(Vuex)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store: store,
  render: h => h(App)
})
