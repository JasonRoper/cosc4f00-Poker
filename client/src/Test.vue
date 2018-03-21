<template>
  <div id="app">
    <div>{{messages}}</div>
  </div>
</template>

<script>
import NavBar from '../src/components/NavBar.vue'
export default {
  name: 'app',
  components: {
   navBar: NavBar
  }
}
</script>


<!--<template>
  <div id="app">
    <div>{{messages}}</div>
  </div>
</template>

<script>
// import NavBar from '../src/components/NavBar.vue'
export default {
  name: 'app',
  data: function () { return {messages: ['Hey John', 'Howdy Paco'] }},
}
</script>
-->
