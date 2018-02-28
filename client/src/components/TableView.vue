<template class="row">
  <div class="col-sm-center text-center" >
    <div v-if="this.userId === null">
      <!-- This means that your in the lobby -->
      <seat v-for="player in this.mechanics.multiplePlayers" :key="player.id" :data="player"></seat>
  
    </div>
    <!-- <div class="opps" v-else> -->
      <player  class="player" v-for="player in this.mechanics.multiplePlayers" :key="player.id" :data="player">
      </player>
    <!-- </div> -->
        <div class="inner-tableBorder">

     <p>table{{this.message}}</p>
    
    
     <p class='Communitycards mt-5 '>
      <card class='size' v-for="card in this.mechanics.communityCards" :key="card" :card="card"></card>
    </p>
   <pot :data="this.mechanics.pot"></pot>
  </div>
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
      mechanics: new GameMech(0, this.userId),
      numberofPlayer: 0
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
    this.numberofPlayer = this.mechanics.multiplePlayers.length
    console.log('heres number of players' + this.numberofPlayer)
    // const mechanics = new GameMechanics(0, 0)
    // mechanics.setDefaultTransport(TempGameState)
    // mechanics.setGameTransport(TempGameState)
    // this.gameService = new GameService(1)

    // this.gameService.onGameUpdated((newState) => {
    //   console.log('type: %s , value: %s', typeof newState, JSON.stringify(newState))
    // })
    // this.gameService.sendAction({type: 'BET', bet: 1})
  },
  /* mounted () {
    let players = document.getElementsByClassName('player')
    let numberofPoints = players.length
    let degreeIncrument = 360/numberofPoints
    let radius = 400
    let x = 0
    let y = 0
    let theta = 0
    x = Math.sin()
    for ( let i=0; i<numberofPoints;i++) {
    }
    players[0].style.transform = 'translateX(0px) translateY(0px)'
    players[1].style.transform = 'translateX(-400px) translateY(0px)'
    players[2].style.transform = 'translateX(0px) translateY(400px)'
    players[3].style.transform = 'translateX(-400px) translateY(400px)'
    // players[3].style.transform = 'scale(1.2)'

    /*x=rcos(θ)
    y=rsin(θ)
    On a unit circle, a circle with radius 1,  x=cos(θ)  and  y=sin(θ)
    divide the pooints ie number of players by 360
    for each point that is the theta sin(theata (degrees))
    },
*/
  destroyed () {
    if (this.gameService) {
      this.gameService.finish()
    }
  }
}

</script>

<style src="@/assets/css/Table.css"></style>
