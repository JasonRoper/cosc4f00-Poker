<template>
<div class='nav justify-content-center button-nav' role='navigation'>
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
        },
        fold: {
          text: 'Fold',
          event: this.fold
        },
        check: {
          text: 'Check',
          event: this.check
        },
        bet: {
          text: 'Bet',
          event: this.Makebet
        }
      }
    }
  },
  methods: {
    Makebet () {
      console.log('bet')
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
      this.$emit('action', {
        actions: Actions.CALL,
        bet: this.bet
      })
    },
    check () {
      console.log('check')
      this.$emit('action', {
        actions: Actions.CHECK,
        bet: this.bet
      })
    },
    fold () {
      console.log('fold')
      this.$emit('action', {
        actions: Actions.CHECK,
        bet: this.bet
      })
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
  margin: 0px auto;
  
}
</style>