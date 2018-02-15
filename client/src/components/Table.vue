<template>
  <div class="col">
    
     <seat v-for="player in this.mechanics.multiplePlayers" :key="player.id" :data="player"/>
  
     <p class='Communitycards'>
      <card class='size' v-for="card in this.mechanics.communityCards" :key="card" :card="card"></card>

   

    </p>
   <pot :data="this.mechanics.pot"></pot>
     
    <input v-model="money" placeholder="How much would you like to bet">

     <button v-on:click="fold(money)" :disabled="this.mechanics.foldAction == 1">FOLD</button>
     <button v-on:click="check(money)" :disabled="this.mechanics.checkAction == 1">CHECK</button>
     <button v-on:click="raise(money)" :disabled="this.mechanics.raiseAction == 1">RAISE</button>
     <button v-on:click="call(money)" :disabled="this.mechanics.callAction == 1">CALL</button>
     <button v-on:click="bet(money)" :disabled="this.mechanics.betAction == 1">BET</button>
   <button v-on:click="setTableActions()">Send Cards</button>
  <ul>

  </ul>

 <!--

  <seat :data="this.mechanics.multiplePlayers[1]"/>
          <div v-if="index<this.mechanics.multiplePlayers.length">
             
              </div>
              <div v-else>
                <seat :data="this.mechanics.multiplePlayers[index]"/>
              </div>
        


<div class='showCard'>
    {{ this.mechanics.communityCards }}
  </div>
     <card class='size' :card="empty.card"></card>


    <p v-for="card in this.mechanics.CommunityCards" :key="card">
              {{card}}
            </p>
   
        <div class = 'showCard' v-if="state.lobby">
            <card class='size' :key="card" :card="empty.card"/></card>
            <card class='size' :key="card" :card="empty.card"/></card>
        </div>
        <div class='showCard' v-else>
            <card class='size' v-for="card in player.cards" :key="card" :card="card"/></card>
        </div>


    </div>
  <div class='Communitycards'>
      <div v-for="card in this.mechanics.CommunityCards" :key="card">
        {{ card }}
      </div>
      <button v-on:click="sendCommunityCards">Prese ME!</button>
    </div>
     
    <h4> Communnity Cards</h4>
    <div class='Communitycards'>
      <card  class='size' v-for="(card) in state.communityCards" :key="card" :card="card"/></card>
    </div>
 
    <a href="/#/NextGame">NextGame</a>
    Community Cards
  
    <card class='size' v-for="card in user.cards" :key="card" :card="card"/></card>
    Players
    <player v-for="player in mechanics.MultiplePlayers" v-if="mechanics.userId !== player.id" :key="player.id" :data="player"/>
    <player v-for="player in mechanics.MultiplePlayers" v-if="mechanics.userId === player.id" :key="player.id" :data="player"/>
    Button to send players their card
    <div class="opps">
      <player   v-for="player in opponents" :key="player.id" :data="player"/>
    </div>
    <h4> Communnity Cards</h4>
    <div class='Communitycards'>
      <card  class='size' v-for="(card) in state.communityCards" :key="card" :card="card"/></card>
      <card class='size' v-for="card in user.cards" :key="card" :card="card"/></card>
    </div>
    <h4>Users Cards </h4>
    <div class='userCards'>
      <player class='userInfo' :data="user"/>
    </div>
    <actions class='actionsPosition' @action="setAction" :active="state.active"/>
    <a href="/#/Lobby">Lobby</a>-->
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

  data () {
    // const temp = new GameMech(0, 0)
    // const mech = temp.get()// new GameMechanics(0, 0)
    return {
      msg: 'hiiiLow',
      mechanics: new GameMech(0, 0)
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
    getTableActions: function () {
    },
    fold: function (money) {
      this.mechanics.storePremove(GameActionType.FOLD, 0)
      // this.$forceUpdate()
    },
    check: function (money) {
      this.mechanics.storePremove(GameActionType.CHECK, 0)
      // this.$forceUpdate()
    },
    call: function (money) {
      this.mechanics.storePremove(GameActionType.CALL, money)
      // this.$forceUpdate()
    },
    raise: function (money) {
      this.mechanics.storePremove(GameActionType.RAISE, money)
      // this.$forceUpdate()
    },
    bet: function (money) {
      this.mechanics.storePremove(GameActionType.BET, money)
      // this.$forceUpdate()
    },
    sendCards: function () {
      this.mechanics.sendCards('CARD', 'TWOCARD')
      // this.$forceUpdate()
    },
    tableAction: function () {
      // this.mechanics.Multiple
      // GameAction
    },
    premove: function (action) {

    },
    communityCards: function () {
      // this.mechanics.communityCards[0] = 1
      this.mechanics.sendCommunityCards(CardSuite.BLANK_CARD, CardSuite.BLANK_CARD, 'sssss')
      // this.$forceUpdate()
    },

    setAction (event) {
      this.user.nextAction = event
    },
    sortcards () {
      for (let i = 0; i < this.state.communityCards.length; i++) {
        this.state.communityCards[i].css.style.color = 'blue'
      }
    }
  },
  components: {
    player: Player,
    actions: TableActions,
    card: Card,
    seat: Seat
    // gameMechanics: GameMechanics
  },
  empty: {
    card: CardSuite.BLANK_CARD
  },
  created () {
    // const mechanics = new GameMechanics(0, 0)
    // mechanics.setGameTransport(TempGameState)
    // computed property

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