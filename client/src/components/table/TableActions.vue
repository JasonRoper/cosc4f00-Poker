<template>
<div class='nav justify-content-center' role='navigation'>
  <ul id='TableActions'class='nav navbar-nav'>
    <li v-for="(button,index) in buttons" :key="index" class='nav-item'>
      <button @click='button.event'>{{ button.text }}</button>
    </li>
  </ul>
  <div v-if="canBet">
    <input type='number'></input>
  </div>
</div>
</template>

<script>

import Actions from '@/types/actions'

export default {
  props: ['maxBet', 'canBet', 'matchBet'],
  data () {
    return {
      bet: 0,
      buttons: {
        call: {
          text: 'Call',
          event: this.call
        },
        raise: {
          text: 'Raise',
          event: this.raise
        }
      }
    }
  },
  methods: {
    bet () {
      this.$emit('action', {
        action: Actions.BET,
        bet: this.bet
      })
    },
    raise () {
      console.log('raise')
      this.$emit('action', {
        actions: Actions.RAISE,
        bet: this.bet
      })
    },
    call () {
      console.log('call')
    },
    check () {
      console.log('check')
    },
    fold () {
      console.log('tell me')
    }
  },
  watch: {
    matchBet () {
      this.buttons.call.text = 'Check'
    }
  }
}
</script>

<style>
.button-nav {
  border: 7px solid red;
  background-color: #abc;
  border-radius: 12px;
  margin-left: 330px;
  margin-right: 550px;
  overflow: hidden;
  height: 85px;
  position: fixed;
}
</style>