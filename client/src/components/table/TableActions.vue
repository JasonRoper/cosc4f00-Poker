<template>
<div class='thecontain'>
<div class=' button-nav ' role='navigation'>
  <ul id='TableActions' class='nav navbar-nav thecontain"'>

      <div v-if="this.data.length != 0">
          <input v-model="money" placeholder="How much would you like to bet">
          
          <button v-on:click="fold(money)" :disabled="this.mechanics.foldAction == 1">FOLD</button>
          <button v-on:click="check(money)" :disabled="this.mechanics.checkAction == 1">CHECK</button>
          <button v-on:click="raise(money)" :disabled="this.mechanics.raiseAction == 1">RAISE</button>
          <button v-on:click="call(money)" :disabled="this.mechanics.callAction == 1">CALL</button>-->
          <button v-on:click="bet(10)">BET</button>
      </div>
      
      
      

  </ul>

</div>
</div>
</template>

<script>

// import Actions from '@/types/actions'
import GameActionType from '@/api/gameservices'
export default {
  props: ['data'],
  methods: {
    bet (money) {
      this.$emit('clicked', money)
    }
  },
}

</script>

<style>
.thecontain{
 margin: 0px auto;
 border: 1px soild green;
  
}
</style>