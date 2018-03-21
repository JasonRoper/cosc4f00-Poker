<template>
  <div id="app">
    <router-view>
    </router-view>
    <div>{{messages}}</div>
  </div>
</template>

<script>
// import NavBar from '../src/components/NavBar.vue'
export default {
  name: 'app',
  data () {
    return {
      messages: ['doc']
      // onHomePage: true
    }
  }
  // components: {
  //   navBar: NavBar
  // }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;}
</style>
