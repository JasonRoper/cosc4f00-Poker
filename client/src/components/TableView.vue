<template>

<div id="page-bg"    class="container-fluid text-sm-center mycontent ">

  <!-- MenuIcon -->
  <!-- <div id="sidebar"> -->
    <div class="menu-icon" @click="toggleSidebar()" >
      <span></span>
      <span></span>
      <span></span>
    </div>
  <!-- </div>   -->

  <!--/Menu Icon-->
  <!-- Side Bar Content -->
  <!--  -->


  <!--/Side BAr content  -->
  <div class="container text-sm-center mycontent "  >
    <!-- <div  class=" container text-lg-center"> -->
    <div class="row">
            <div id="sidebar"  class="col">
  <div >
    <a href="#" ><img src="../assets/Webgraphics/Opponent.svg" class=" border border-white rounded-circle" width="70" height="70"></a>
    <ul class="menu">
      <li><a href="#">Poker Pals</a></li>
       <li><a href="#">Pals</a></li>
      <li><a href="#">Games</a></li>
      <li><a href="#">Groups</a></li>
      <li><a href="#">Trophics</a></li>
      <li><a href="#">Options</a></li>
      <li><a href="#">Sign Outs</a></li>
    </ul>
    <ul class="social-icon ">
      <li><a href="#"><i class=" fa fa-twitter fa-lg fa-2x " aria-hidden="true"></i></a></li>
      <li><a href="#"><i class=" fa fa-google-plus fa-lg fa-2x" aria-hidden="true"></i></a></li>
      <li><a href="#"><i class=" fa fa-linkedin fa-lg fa-2x" aria-hidden="true"></i></a></li>
      <li><a href="#"><i class=" fa fa-instagram fa-lg fa-2x" aria-hidden="true"></i></a></li>

    </ul>
  </div>
</div>
      <div id="page-content">
        <nav class="text-center">
  <div class="nav nav-tabs" id="nav-tab" role="tablist">
    <a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true">Game</a>
    <a class="nav-item nav-link" id="nav-profile-tab" data-toggle="tab" href="#nav-profile" role="tab" aria-controls="nav-profile" aria-selected="false">Profile</a>
    <a class="nav-item nav-link" id="nav-contact-tab" data-toggle="tab" href="#nav-contact" role="tab" aria-controls="nav-contact" aria-selected="false">Community</a>
  </div>
</nav>
<div class="tab-content" id="nav-tabContent">
  <div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">
    <div class="pt-5 mt-3" >
  <div class="col-sm-center text-center" >
    <div class="CompleteTable">
      <div v-if="this.userId === null">
        <seat v-for="player in this.mechanics.multiplePlayers" :key="player.id" :data="player"></seat>

      </div>
              <div class="inner-tableBorder">
          <player  class="player" v-for="player in this.mechanics.multiplePlayers" :key="player.id" :data="player">
        </player>
        <div>
         <h2 class="display-4 pr-4 pb-0  text-white">PokerPals!!<img src="../assets/Webgraphics/poker.png" width="70" height="70"></h2>
         <h5 class=" mr-5  pt-0 text-info">Pot:{{this.mechanics.pot}}</h5>
      <p class='Communitycards  ml-4  '>
        <card class='size' v-for="card in this.mechanics.communityCards" :key="card" :card="card"></card>
      </p>
      DECK
        <p class='DECK  mt-4 pt-4 '>
        <card  class= "DECKsize mt-4" v-for="card in this.mechanics.communityCards" :key="card" :card="card"></card>
      </p>
      </div>
    <pot :data="this.mechanics.pot"></pot>
    </div>
  </div>
      <div class="TableActions row">

         <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
        Chat
        </div>
        <button type="button" class="btn  btn-lg chat "><i class="fa fa-comments  fa-lg"></i></button>
        </div>


         <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
        Check
        </div>
        <button type="button" class="btn  btn-lg check"  v-on:click="check(money)" :disabled="this.mechanics.checkAction == 1"><i class="fa fa-check  fa-lg"></i></button>
        </div>


     <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
          Fold
        </div>
        <button type="button" class="btn btn-lg fold" v-on:click="fold(money)" :disabled="this.mechanics.foldAction == 1"><strong><i class="fa fa-remove fa-lg "></i></strong></button>
        </div>

              <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
          Call
        </div>
        <button type="button" class="btn  btn-lg CALL "  v-on:click="call(money)" :disabled="this.mechanics.callAction == 1"><i class="fa fa-dollar  fa-lg"></i>{{BigBlindCurrentBet}}</button>
        </div>


         <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
        Bet/Raise
        </div>
        <button type="button" class="btn  btn-lg Bet "  v-on:click="bet(money)" :disabled="this.mechanics.betAction == 1"><i class="fa fa-chevron-up  fa-lg"></i></button>
        </div>

         <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
        Raise
        </div>
        <button type="button" class="btn  btn-lg Bet " v-on:click="raise(money)" :disabled="this.mechanics.raiseAction == 1"><i class="fa fa-chevron-up  fa-lg"></i></button>
        </div>

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
</div>

  </div>
  <div class="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab">...</div>
  <div class="tab-pane fade" id="nav-contact" role="tabpanel" aria-labelledby="nav-contact-tab">...</div>
</div>
        </div>
      </div>
    <!-- </div> -->
    </div>




</div>
</template>



<script>
/**
 * A simple checkbox component.
 * @module components/basic/checkbox
 * @param {number} [disabled=false] - Disabled component
 * @param {string[]} model - Required, need two way
 */
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
  name: 'checkbox',
  introduction: 'an amazing checkbox',
  props: ['userId'],
  data () {
    return {
      mechanics: new GameMech(0, this.userId),
      numberofPlayer: 0,
      BigBlindCurrentBet: 0,
      user: 0,
      opponents: 0
    }
  },
  watch: {
    mechanics: function (newValue, oldValue) {
      this.user = this.getUser()
      this.opponents = this.getOpponents()
      this.$forceUpdate()
    }
  },
  methods: {
    getUser: function () {
      return this.mechanics.getUser()
    },
    getOpponents: function () {
      return this.mechanics.getOpponents()
    },
    setTableActions: function () {
      this.mechanics.tableActions()
    },
    fold: function () {
      alert('FOLD')
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
    },
    toggleSidebar: function () {
      document.getElementById('sidebar').classList.toggle('active')
      document.getElementById('page-content').classList.toggle('active')
    },
    adjustsideBar: function () {
      document.getElementById('page-content').classList.remove('active')
      document.getElementById('sidebar').classList.remove('active')
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
    this.$nextTick(function () {
      window.addEventListener('resize', this.adjustsideBar)
    })
    // Circle equation for displaying player corectly  in a circle
    let players = document.getElementsByClassName('player')
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
<style src="@/assets/css/GameNav.css"></style>