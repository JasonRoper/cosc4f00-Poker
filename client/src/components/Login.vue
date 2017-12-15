<template>
  <div>
    <label>Username</label>
    <input  ref="myTestField" type="text" >
    <a href="/#/Lobby"><button @click="storeName">Login</button></a>
  </div>
</template>

<script>
export default {
  data () {
    return {
      Player: {
        ID: ' ',
        username: ''
      }
    }
  },
  methods: {
    toreName () {
      this.Player.username = this.$refs.myTestField.value
      this.$store.commit('setPlayer', this.Player.username)
    }
  }
}
</script>
<style>
</style>