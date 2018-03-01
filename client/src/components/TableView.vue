<template class="row ">
  <div class="col-sm-center text-center" >
    <div class="CompleteTable">
      <div v-if="this.userId === null">
        <!-- This means that your in the lobby -->
        <seat v-for="player in this.mechanics.multiplePlayers" :key="player.id" :data="player"></seat>
    
      </div>
    <!-- <div class="opps" v-else> -->
    
      <!-- </div> -->
              <div class="inner-tableBorder">    
          <player  class="player" v-for="player in this.mechanics.multiplePlayers" :key="player.id" :data="player">
        </player>
        <div>
         <h2 class="display-4 pr-4 pb-0 text-white">PokerPals!!<img src="../assets/Webgraphics/poker.png" width="70" height="70"></h2>
      <p class='Communitycards mt-3 ml-4  '>
        <card class='size' v-for="card in this.mechanics.communityCards" :key="card" :card="card"></card>
      </p>
      </div>
    <pot :data="this.mechanics.pot"></pot>
    </div>
  </div>
    <!-- User Interacions -->
    
      <div class="TableActions">
        
        <!-- <div> -->
          <!-- <div class="ActionContainer"> -->
        <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
          Fold
        </div>
        <button type="button" class="btn  btn-lg "><i class="fa fa-remove fa-lg "></i></button>
        </div>

        <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
        Add Player
        </div>
        <button type="button" class="btn  btn-lg "><i class="fa fa-user-plus fa-lg"></i></button>
        </div>

         <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
          Call
        </div>
        <button type="button" class="btn  btn-lg "><i class="fa fa-dollar  fa-lg"></i></button>
        </div>

         <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
        Raise
        </div>
        <button type="button" class="btn  btn-lg "><i class="fa fa-chevron-up  fa-lg"></i></button>
        </div>

         <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
        Chat
        </div>
        <button type="button" class="btn  btn-lg "><i class="fa fa-comments  fa-lg"></i></button>
        </div>
         <!-- </div> -->
      </div>
      <div>Action: {{this.mechanics.userAction}}</div>    
        <input v-model="money" placeholder="How much would you like to bet">
        <button  v-on:click="fold(money)" :disabled="this.mechanics.foldAction == 1">FOLD</button>
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
  mounted () {
    let players = document.getElementsByClassName('player')
    // players[0].style.transform = 'translateX( 0pt) translateY( 280pt)'
    // players[1].style.transform = 'translateX( -280pt) translateY( 0pt)'
    // players[2].style.transform = 'translateX( 0pt) translateY( -280pt)'
    // players[3].style.transform = 'translateX( 280pt) translateY( 0pt)'

    console.log('number of points is ' + players.length)
    let numberofPoints = players.length
    let degreeIncrument = 360 / numberofPoints
    var radius = 238 // 280
    var x = 0
    var y = 0
    let theta = 0
    for (let i = 0; i < numberofPoints; i++) {
      theta = theta + degreeIncrument
      // x = radius * Math.cos(degrees * Math.PI / 180.0).toFixed(3) // convert raidains
      x = radius * Math.cos(theta * Math.PI / 180.0).toFixed(3) // Convert to radians
      y = radius * Math.sin(theta * Math.PI / 180.0).toFixed(3)
      players[i].style.transform = 'translateX(' + x + 'pt) translateY(' + y + 'pt)'
      console.log('heres you x: ' + x + 'here your y:' + y + 'at ' + theta)
    }
  },
  destroyed () {
    if (this.gameService) {
      this.gameService.finish()
    }
  }
}

</script>

<style src="@/assets/css/Table.css"></style>
