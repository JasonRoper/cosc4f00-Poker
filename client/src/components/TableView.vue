<template>
  <div class="col">
    <div v-if="this.userId === null">
      <!-- This means that your in the lobby -->
      <seat v-for="player in this.mechanics.multiplePlayers" :key="player.id" :data="player"/>
  
    </div>
    <div v-else>
    
    
    </div>

     <p>table{{this.message}}</p>
    
    
     <p class='Communitycards'>
      <card class='size' v-for="card in this.mechanics.communityCards" :key="card" :card="card"></card>
    </p>
   <pot :data="this.mechanics.pot"></pot>

  <div>Action: {{this.mechanics.userAction}}</div>    

    <input v-model="money" placeholder="How much would you like to bet">

     <button v-on:click="fold(money)" :disabled="this.mechanics.foldAction == 1">FOLD</button>
     <button v-on:click="check(money)" :disabled="this.mechanics.checkAction == 1">CHECK</button>
     <button v-on:click="raise(money)" :disabled="this.mechanics.raiseAction == 1">RAISE</button>
     <button v-on:click="call(money)" :disabled="this.mechanics.callAction == 1">CALL</button>
     <button v-on:click="bet(money)" :disabled="this.mechanics.betAction == 1">BET</button>
   <button v-on:click="sendAction()">Send Action To Server</button>
   
 </div>
</template>

<script>

import Player from '@/components/table/Player.vue'
import Card from '@/components/table/Card'
import TableActions from '@/components/table/TableActions'
import CardSuite from '@/types/cards'
// import Actions from '@/types/actions'
// import { GameService, GameActionType } from '@/api/gameservice'
import GameMech from '@/store/GameMechanics.ts'
// import { GameAction } from '../api/gameservice'
// import Game from '@/store/game.ts'
import Seat from '@/components/table/Seat'
import {GameActionType} from '../api/gameservice'
export default {
  props: ['userId'],
  data () {
    return {
      msg: 'hiiiLow',
      mechanics: new GameMech(0, this.userId)
    }
  },
  watch: {
    mechanics: function (newValue, oldValue) {
      this.$forceUpdate()
    }
  },
  methods: {
    setTableActions: function () {
      this.mechanics.tableActions()
    },
    fold: function () {
      this.premove(GameActionType.FOLD, 0)
    },
    check: function () {
      this.premove(GameActionType.CHECK, 0)
    },
    call: function (money) {
      if (money !== undefined) {
        this.premove(GameActionType.CALL, money)
      } else {
        alert('you are trying to CALL with no money')
      }
    },
    raise: function (money) {
      if (money !== undefined) {
        this.premove(GameActionType.RAISE, money)
      } else {
        alert('you are trying to raise with no money')
      }
    },
    bet: function (money) {
      if (money !== undefined) {
        this.premove(GameActionType.BET, money)
      } else {
        alert('you are trying to Bet with no money')
      }
    },
    sendCards: function () {
      this.mechanics.sendCards('CARD', 'TWOCARD')
    },
    sendAction: function () {
      this.mechanics.sendAction()
    },
    premove: function (action, money) {
      if (this.mechanics.storePremove(action, money)) {
        alert('you have stored a premove' + action + ' with ' + money)
      } else {
        alert('you have not stored a premove' + action + ' not with ' + money)
        this.mechanics.setTableActions()
      }
      this.$forceUpdate()
    },
    communityCards: function () {
      this.mechanics.sendCommunityCards(CardSuite.BLANK_CARD)
    }
  },
  components: {
    player: Player,
    actions: TableActions,
    card: Card,
    seat: Seat
  },
  created () {
    // const mechanics = new GameMechanics(0, 0)
    // mechanics.setDefaultTransport(TempGameState)
    // mechanics.setGameTransport(TempGameState)
    // this.gameService = new GameService(1)

    // this.gameService.onGameUpdated((newState) => {
    //   console.log('type: %s , value: %s', typeof newState, JSON.stringify(newState))
    // })
    // this.gameService.sendAction({type: 'BET', bet: 1})
  },
  destroyed () {
    if (this.gameService) {
      this.gameService.finish()
    }
  }
}

</script>

<style src="@/assets/css/Table.css"></style>
  <style src="@/assets/css/Lobby.css">

</style>