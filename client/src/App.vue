<template>
  <div id="app">
    <router-view>
    </router-view>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
export default {
  name: 'app',
  data () {
    return {
      onHomePage: true
    }
  },
  components: {
    navBar: NavBar
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;}

   .moveup-enter-active {
    animation: moveup 0.5s;
  }
  @keyframes moveup {
    0% { transform: translateY(0); }
    50% { transform: translateY(50); }
  } 
</style>
