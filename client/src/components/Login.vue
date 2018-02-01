<template>
  <div>
    <form ref="theform" action="'/#/Lobby">
    <label>Username</label>
    <input  v-model="Player.username"ref="myTestField" type="text">
    <input type="button" value = "Login"  @click="storeName(Player.username)">
    </form>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
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
    ...mapActions([
      'storeName'
    ])
  }
}
</script>
<style>
</style>