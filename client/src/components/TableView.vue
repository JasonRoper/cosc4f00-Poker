<template>

<div id="page-bg"    class="container-fluid text-sm-center mycontent ">
                 <!-- <h2 class="display-1 completeTop text-white  mt-2 fixed-top ">PokerPals!!  <img src="../assets/Webgraphics/poker.png" width="100" height="100">
                         <form  class=" gameeventBar form-control bg-warning   ">

              <div class="input-group input-group-lg ">

  <input v-model="mechanics.gameStatus" id="Events"  type="number" class="form-control mx-5 text-center bg-secondary lead text-light  " placeholder="Event Bar" aria-label="Event Bar" aria-describedby="basic-addon1" >
            </div>
           </form>
                 </h2> -->
  
<div class="page-Nav">    
    <div class="menu-icon" @click="toggleSidebar()" >
      <span></span>
      <span></span>
      <span></span>
    </div>
    
    
        <div class="tabAction-icon" @click="toggleTableAction()" >
      <span></span>
      <span> <button type="button" class="btn btn-default btn-circle   tabAction-chip  inner-orange">TA</button><br>
      </span>
    </div>

    <form  class=" statsBar form-control bg-light-transparent   ">

         <div class="input-group input-group ">


<label> User: </label>
             </div>

              <div class="input-group input-group">
                

  <input v-model="mechanics.gameStatus" id="Events"  type="number" class="form-control mx-2 text-center bg-secondary lead text-light  " placeholder="Event Bar" aria-label="Event Bar" aria-describedby="basic-addon1" disabled>
            </div>

         <div class="input-group input-group ">


<label> Chips: </label>
             </div>

              <div class="input-group input-group ">
                

  <input v-model="mechanics.gameStatus" id="Events"  type="number" class="form-control mx-2 text-center bg-secondary lead text-light  " placeholder="Event Bar" aria-label="Event Bar" aria-describedby="basic-addon1" disabled >
            </div>
            
           </form>
    <form  class=" eventgameBar form-control bg-warning   ">

              <div class="input-group input-group-lg ">

  <input v-model="mechanics.gameStatus" id="Events"  type="number" class="form-control mx-3 text-center bg-secondary lead text-light  " placeholder="Event Bar" aria-label="Event Bar" aria-describedby="basic-addon1" >
            </div>
           </form>
 <div class="tabAction-icon" >
      <span>
 <button type="button" class="btn btn-default btn-circle    tabAction-chip navigate  inner-blue" @click="backButton()"><i  style="font-size:28px" class="fa fa-arrow-left"></i></button><br>
      </span>
    </div>
                    <!-- <h2 class="display-2  text-white  mt-2  ">PokerPals!!  <img src="../assets/Webgraphics/poker.png" width="0" height="50"> </h2> -->


</div>


  <div class="container text-sm-center mycontent "  >
    <!-- <div  class=" container text-lg-center"> -->
    <div class="row">
            <div id="sidebar"  class="col">
  <div class="SideBarContent" >
    <div class="nameTitle">{{this.UserName}}</div> <br>
    <a href="" ><img src="../assets/Webgraphics/Opponent.svg" class=" border border-white rounded-circle" width="70" height="70"></a>
    <ul class="menu">
      <li><a>Poker Pals</a></li>
       <li><a href="#">Pals</a></li>
      <li><a href="#">Options</a></li>
      <li><a @click="logOut()">Sign Out</a></li>
    </ul>
  </div>
</div>
            <div id="TableActions"  class="col">
  <div class="TA-SideBarContent" >
     <div class="TableActions row">

         <!-- <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
        Chat
        </div>
        <button type="button" class="btn  btn-lg chat "><i class="fa fa-comments  fa-lg"></i></button>
        </div> -->

        
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

         <!-- <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
        Raise
        </div>
        <button type="button" class="btn  btn-lg Bet " v-on:click="raise(money)" :disabled="this.mechanics.raiseAction == 1"><i class="fa fa-chevron-up  fa-lg"></i></button>
        </div> -->

   <form  class="form-control bg-warning mt-5 mx-4 ">

              <div class="input-group input-group-lg ">

  <input v-model="money"  type="number" class="form-control mx-5 text-center bg-secondary lead text-light  " placeholder="Event Bar" aria-label="Event Bar" aria-describedby="basic-addon1" >
            </div>
           </form>

      </div>
  </div>
</div>
      <div id="page-content">
               <!-- <h2 class="display-1 text-white  mt-2 ">PokerPals!!  <img src="../assets/Webgraphics/poker.png" width="100" height="100"></h2> -->


         <!-- <form  class="form-control mx-0 bg-warning mt-2 "> -->
              <!-- <div class="input-group input-group-lg "> -->

                    <!-- <card width="40" height="40" class=" singleCard"  :key="BLANK_CARD" :card="BLANK_CARD"></card> -->
         <!-- <button type="button" class="btn btn-default btn-circle backButton  inner-blue BackButton" @click="backButton()"><i class="fa fa-arrow-left  fa-2x"></i></button><br> -->

  <!-- <input id="Events"  v-model="mechanics.gameStatus" type="text" class="form-control mx-5 text-center bg-secondary lead text-light  " placeholder="Event Bar" aria-label="Event Bar" aria-describedby="basic-addon1" disabled> -->
        <!-- <button type="button" class="btn btn-default btn-circle backButton  inner-blue BackButton" @click="backButton()"><i class="fa fa-arrow-left  fa-2x"></i></button><br> -->

            <!-- </div>  -->
            
           <!-- </form> -->
    <div >
  <div class="col-sm-center text-center " >
      <!-- <div v-if="this.userId === null">
        <seat  class="player" v-for="player in this.getOpponents()" :key="player.id" :data="player"></seat>

      </div> -->
              <div class="inner-tableBorder ">
          <player  class="player" v-for="player in this.mechanics.multiplePlayers" :key="player.id" :data="player">
        </player>
            <div class="CompleteTable">
        <div class= 'tableContent'>
         <h2 class="display-4 pr-4 pb-0  text-white">PokerPals!!<img src="../assets/Webgraphics/poker.png" width="70" height="70"></h2>
         <div class= "tableHead"></div>
   
         <h2 class=" mr-5  pt-0 text-info">Pot:<span class="text-warning"> {{this.mechanics.pot}}</span><img src="../assets/Webgraphics/chipPile.png" style="decoration:none" width="55" height="55"></h2>
      <div class="Communitycards-holder">
      <p class='Communitycards  ml-4  '>
        <card class='size' v-for="card in this.mechanics.communityCards" :key="card" :card="BLANK_CARD"></card>
      </p>
      </div>
        <p id="theDeck" class='DECK mt-2 '>
                    <transition-group name="deckCard">

        <card  class= "deckCard DECKsize mt-4" v-for="card in this.mechanics.communityCards" :key="card" :card="card"></card>
                    </transition-group>
      </p>
      </div>
    </div>
  </div>

        <button type="button" class="btn btn-default btn-circle backButton  inner-blue BackButton" @click="doCountDown()">quit</button><br>

      <div class="row">
              <div class="col">
        <button type="button" class="btn btn-default btn-circle backButton  inner-blue BackButton" @click="backButton()"><i class="fa fa-arrow-left  fa-2x"></i></button><br>
        </div>
                </div>
      <!-- <div class="TableActions row">

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
        <button type="button" class="btn  btn-lg check"  v-on:click="check()" :disabled="this.mechanics.checkAction == 1"><i class="fa fa-check  fa-lg"></i></button>
        </div>


     <div class="Action text-center lead text-muted">
      
        <div class="ActionTitle">
          Fold
        </div>
        <button type="button" class="btn btn-lg fold" v-on:click="fold()" :disabled="this.mechanics.foldAction == 1"><strong><i class="fa fa-remove fa-lg "></i></strong></button>
        </div>

              <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
          Call
        </div>
        <button type="button" class="btn  btn-lg CALL "  v-on:click="call()" :disabled="this.mechanics.callAction == 1"><i class="fa fa-dollar  fa-lg"></i>{{BigBlindCurrentBet}}</button>
        </div>


         <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
        Bet/Raise
        </div>
        <button type="button" class="btn  btn-lg Bet "  v-on:click="bet()" :disabled="this.mechanics.betAction == 1"><i class="fa fa-chevron-up  fa-lg"></i></button>
        </div>

         <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
        Raise
        </div>
<<<<<<< Updated upstream
        <button type="button" class="btn  btn-lg Bet " v-on:click="raise()" :disabled="this.mechanics.raiseAction == 1"><i class="fa fa-chevron-up  fa-lg"></i></button>
        </div>

<<<<<<< HEAD
      </div>
=======
        <button type="button" class="btn  btn-lg Bet " v-on:click="raise()" v-bind:disabled="this.mechanics.raiseAction == 1"><i class="fa fa-chevron-up  fa-lg"></i></button>
        </div>

<<<<<<< HEAD
      </div>
      <div>Action: {{this.mechanics.userAction}}</div>
        <input v-model="money" placeholder="How much would you like to bet">
        <button  v-on:click="fold()" :disabled="this.mechanics.foldAction == 1">FOLD</button>
        <button v-on:click="check()" :disabled="this.mechanics.checkAction == 1">CHECK</button>
        <button v-on:click="raise()" :disabled="this.mechanics.raiseAction == 1">RAISE</button>
        <button v-on:click="call()" :disabled="this.mechanics.callAction == 1">CALL</button>
        <button v-on:click="bet()" :disabled="this.mechanics.betAction == 1">BET</button>
        <button v-on:click="sendAction()">Send Action To Server</button>
              
>>>>>>> Stashed changes
=======
=======
>>>>>>> 7aae5580c7bd9b1f0aacc5ce160158be4aaa4cab
      </div> -->
      <!-- <div>Action: {{this.mechanics.userAction}}</div> -->
        <!--<input v-model="money" placeholder="How much would you like to bet">-->
        <!-- <button  v-on:click="fold(money)" :disabled="this.mechanics.foldAction == 1">FOLD</button>
        <button v-on:click="check(money)" :disabled="this.mechanics.checkAction == 1">CHECK</button>
        <button v-on:click="raise(money)" :disabled="this.mechanics.raiseAction == 1">RAISE</button>
        <button v-on:click="call(money)" :disabled="this.mechanics.callAction == 1">CALL</button>
        <button v-on:click="bet(money)" :disabled="this.mechanics.betAction == 1">BET</button>
        <button v-on:click="sendAction()">Send Action To Server</button> -->

 </div>
</div>

  <!-- </div> -->
  <div class="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab">...</div>
  <div class="tab-pane fade" id="nav-contact" role="tabpanel" aria-labelledby="nav-contact-tab">...</div>
<!-- </div> -->

                      <button v-on:click="layCommunity()">Second Round of Betting </button>

        </div>
      </div>
    <!-- </div> -->
    </div>


</div>
</template>



<script>
import Player from '@/components/table/Player.vue'
import CardView from '@/components/table/Card'
import TableActions from '@/components/table/TableActions'
import { Card } from '@/types/cards.ts'
import { mapActions } from 'vuex' // used for maping actions of the vue store files
// import Actions from '@/types/actions'
// import { GameService, GameActionType } from '@/api/gameservice'
import GameMech from '@/store/GameMechanics.ts'
import GameRequest from '@/store/GameRequest.ts'
// import { GameAction } from '../api/gameservice'
// import Game from '@/store/game.ts'
import router from '@/router'
import Seat from '@/components/table/Seat'
import {GameActionType} from '@/api/gameservice.ts'
import state from '../store/users'

export default {
  data () {
    alert('just logged route' + this.$route.params.gameId)
    return {
      // UserName: state.state.username,
      mechanics: new GameMech(this.$route.params.gameId, state.state.username),
      numberofPlayer: 0,
      preivousnumberofPlayers: 0,
      money: '0',
      BigBlindCurrentBet: 0,
      user: 0,
      opponents: 0,
      incrument:0,
      posX:-191,
      posY:-141,
      posZ:200,
    }
  },
  /*
  Below Should really be updated to avoid visual issues
  */
  watch: {
    preivousnumberofPlayers  () {
      // this.mechanics = new GameMech(1, this.userId)
    },
    mechanics: function (newValue, oldValue) {
      this.user = this.getUser()
      this.opponents = this.getOpponents()
    },
    numberofPlayers () {
      console.log(this.numberofPlayers)
      this.mechanics.setGame()
    },
    EventBarMessage () {
      alert('Change change change')
    }
  },
  computed: {
    numberofPlayers () {
      return this.mechanics.getMultiplayers()
    },
    UserName () {
      return this.$store.state.users.username
    },
    EventBarMessage () {
      return this.mechanics.gameStatus

    },
    deckLength () {
    }
  },
  methods: {
    layCommunity () {
      var card = document.getElementsByClassName('deckCard')
      for (let i = 0; i < card.length; i++) {
        card[0].style.transform = 'translateX(' + 60 + 'pt) translateY(' + -60 + 'pt) rotateY(-180deg)'
        //  alert('card', i)
      }
   
      // for (let i = 0; i < card.length; i++) {
              var card = document.getElementsByClassName('deckCard')
var max =card.length
        card[max-1].style.transform = 'translateX(' + this.posX + 'pt) translateY(' + this.posY + 'pt) rotateX(-180deg) translatez(' + this.posZ + 'pt)'
   card[max-1].classList.remove('deckCard')
console.log(max)
this.posX = this.posX + 90
      // this.deckLength = this.deckLength - 1 
      // //  alert('card', i)
      // }

    },
    doCountDown () {
      var countDown = new Date()
      countDown.setMinutes(countDown.getMinutes() + 1) // countDown.getMinutes() + numberofMins
      var x = setInterval(function () {
        var now = new Date()
        var distance = countDown - now
        var days = Math.floor(distance / (1000 * 60 * 60 * 24))
        var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))
        var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60))
        var seconds = Math.floor((distance % (1000 * 60)) / 1000)
        this.EventBarMessage = days + 'd' + hours + 'h' + minutes + 'm' + seconds + 's '
        console.log('checking')
        document.getElementById('Events').value = this.EventBarMessage
        if (distance < 0) {
          clearInterval(x)
          alert('Done')
          document.getElementById('Events').value = ''
        }
      }, 1000)
      document.getElementById('Events').value = ''
    },
    backButton () {
      this.$router.push('/Game')
    },
    force () {
      // console.log(this.mechanics.getMultiplayers())
    },
    ...mapActions([
      'logout'
    ]),
    logOut: function () {

      const logoutRequest = new GameRequest()
      logoutRequest.removeCompetitiveGame(this.mechanics.gameId, state.state.userId).then((responce) => {
        router.push({ name: 'Home' })
        console.log(responce + 'success in delete request')
      }).catch((error) => { console.log('Failed delete request' + error) })

      this.logout()
      this.$router.push('/Home')

    },
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
      this.premove(GameActionType.FOLD, 0)
    },
    check: function () {
      this.premove(GameActionType.CHECK, 0)
    },
    call: function () {
      if (this.money !== undefined) {
        this.premove(GameActionType.CALL, this.money)
      } else {
        console.log('Action: CALL - you are trying to CALL with no money')
      }
    },
    raise: function () {
      if (this.money !== undefined) {
        this.premove(GameActionType.RAISE, this.money)
      } else {
        console.log('Action: RAISE - you are trying to raise with no money')
      }
    },
    bet: function () {
      if (this.money !== undefined) {
        this.premove(GameActionType.BET, this.money)
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
      alert('Tried to make tableAction ' + money)
      money = parseInt(money)
      alert('money is still ' + money)
      if (this.mechanics.turn === this.mechanics.playerId && typeof money === 'number' && money >= 0) {
        console.log('You are attempting to send a move to the server')
        if (this.mechanics.storePremove(action, money)) {
          console.log('You sent a move to there server ' + action + ' with ' + money)
        } else {
          console.log('The move you attempted to send failed')
          this.mechanics.setTableActions()
        }
      } else {
        console.log('you have stored a premove' + action + ' with ' + money)
      }
      // this.$forceUpdate()
    },
    communityCards: function () {
      this.mechanics.sendCommunityCards(Card.BLANK_CARD)
    },
    toggleSidebar: function () {
      document.getElementById('sidebar').classList.toggle('active')
      document.getElementById('page-content').classList.toggle('active')
    },
    adjustsideBar: function () {
      document.getElementById('page-content').classList.remove('active')
      document.getElementById('sidebar').classList.remove('active')
    },
    toggleTableAction: function () {
      document.getElementById('TableActions').classList.toggle('active')
      document.getElementById('page-content').classList.toggle('active')
    },
    adjustsideActionBar: function () {
      document.getElementById('page-content').classList.remove('active')
      document.getElementById('TableActions').classList.remove('active')
    }
  },
  components: {
    player: Player,
    actions: TableActions,
    card: CardView,
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
      // window.addEventListener('resize2', this.adjustsideActionBar)
    })
    // Circle equation for displaying player corectly  in a circle
    // Put Get Opponents in the players
    let players = document.getElementsByClassName('player')
    let numberofPoints = players.length
    let degreeIncrument = 360 / numberofPoints
    var radius = 240 // 280
    var x = 0
    var y = 0
    let theta = 0
    for (let i = 0; i < numberofPoints; i++) {
      theta = theta + degreeIncrument
      x = radius * Math.cos(theta * Math.PI / 180.0).toFixed(3) // Convert to radians
      y = radius * Math.sin(theta * Math.PI / 180.0).toFixed(3)
      players[i].style.transform = 'translateX(' + x + 'pt) translateY(' + y + 'pt)'
      // console.log('heres you x: ' + x + 'here your y:' + y + 'at ' + theta)
    }
    // setInterval(this.force, 3000) // checking if player ammount has changed to update board
  },
  updated: function () {
    let players = document.getElementsByClassName('player')
    let numberofPoints = players.length
    let degreeIncrument = 360 / numberofPoints
    var radius = 238 // 280
    var x = 0
    var y = 0
    let theta = 0
    for (let i = 0; i < numberofPoints; i++) {
      theta = theta + degreeIncrument
      x = radius * Math.cos(theta * Math.PI / 180.0).toFixed(3) // Convert to radians
      y = radius * Math.sin(theta * Math.PI / 180.0).toFixed(3)
      players[i].style.transform = 'translateX(' + x + 'pt) translateY(' + y + 'pt)'
      // console.log('heres you x: ' + x + 'here your y:' + y + 'at ' + theta)
    }
  },
  destroyed () {
    if (this.gameService) {
      this.gameService.finish()
    }
  }
}

</script>
<style src="@/assets/css/GameNav.css"></style>
<style src="@/assets/css/Table.css"></style>
