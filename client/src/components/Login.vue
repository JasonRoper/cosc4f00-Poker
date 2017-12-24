<template>
  <div>
    <form ref="theform" action="'/#/Lobby">
    <label>Username</label>
    <input  ref="myTestField" type="text">
    <input type="button" value = "Login"  @click="storeName">
    </form>
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
    storeName () {
      let namefield = this.$refs.myTestField.value
      if (namefield.length === 0) {
        return
      } else
      if (namefield.length > 0) {
        this.Player.username = this.$refs.myTestField.value
        this.$store.commit('setPlayer', this.Player.username)
        window.location.href = '/#/Lobby'
      }
    }
  }
}
</script>
<style>
</style>