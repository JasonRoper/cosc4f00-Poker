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

       <div v-if="this.mechanics.getUser().isDealer" class="input-group input-group ">
                  <button type="button" class="btn btn-default btn-circle bg-warning  tabAction-chip-dealer inner-orange mx-4 ml-5"><strong> D</strong></button>
             </div>
    <form  class=" statsBar form-control bg-transparent ">
<div class="lead text-white text-left"><label> User Status: </label> </div>

         <div class="input-group input-group ">


<label  class="lead text-white text-left"> User: </label>
             </div>

              <div class="input-group input-group roundedName">
                

  <!-- <input v-model="this.UserName()" id="Events"  type="number" class="form-control mx-2 text-center bg-secondary lead text-light  " placeholder="Event Bar" aria-label="Event Bar" aria-describedby="basic-addon1" disabled> -->
           <div v-if="this.mechanics.getUser().isTurn" class="form-control mx-2 text-center bg-info lead text-light">
             {{this.UserName}}
              </div>
          <div v-else class="form-control mx-2 text-center bg-secondary lead text-light">
             {{this.UserName}}
              </div>
              
            </div>

         <div class="input-group input-group ">


<label class="lead text-white " > Chips: </label>
             </div>

              <div class="input-group input-group ">
                

  <!-- <input v-model="mechanics.gameStatus" id="Events"  type="number" class="form-control mx-2 text-center bg-secondary lead text-light  " placeholder="Event Bar" aria-label="Event Bar" aria-describedby="basic-addon1" disabled > -->
            <div class="form-control mx-2 text-center bg-secondary lead text-light ">
            {{this.mechanics.getUser().money}}
            </div>
            </div>
            


           </form>

    <form  class=" eventgameBar form-control bg-warning   ">
     <div class="input-group input-group ">
     <!-- <label class="text-white lead"><strong><em> Event Bar:</em></strong> </label> -->
             </div>

              <div class="input-group input-group-lg ">

  <!-- <input v-model="this.EventBarMessage" id="Events"  type="text" class="form-control mx-3 mt-0 text-center bg-secondary lead text-light  " placeholder="Event Bar" aria-label="Event Bar" aria-describedby="basic-addon1"  disabled> -->
         <div class="form-control mx-2 mt-0 text-center bg-secondary lead text-light  "> 
           <!-- transitions with vue hooks  -->
           {{this.EventBarMessage}} 
           </div>
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
       <!-- <li><a href="#">Pals</a></li>
      <li><a href="#">Options</a></li> -->
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
        <button type="button" class="btn  btn-lg CALL "  v-on:click="call(money)" :disabled="this.mechanics.callAction == 1"><i class="fa fa-dollar  fa-lg"></i>{{this.mechanics.minimumBet}}</button>
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
  <div class="holster">
   <form  class="  inputGroup form-control bg-warning mt-5 mx-4 ">

              <div class="input-group input-group-lg ">

  <input v-model="money"  type="number" class="form-control mx-5 text-center bg-secondary lead text-light  " placeholder="Event Bar" aria-label="Event Bar" aria-describedby="basic-addon1" >
            </div>
           </form>
</div>
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
          <player  class="player" v-for="player in this.mechanics.getOpponent(UserName)" :key="player.id" :data="player">
        </player>
            <div class="CompleteTable">
        <div class= 'tableContent'>
         <h2 class="display-4 pr-4 pb-0  text-white"><img src="../assets/Webgraphics/poker.png" width="70" height="70">PokerPals!!<img src="../assets/Webgraphics/poker.png" width="70" height="70"></h2>
         <div class= "tableHead"></div>
   
         <h2 class=" mr-5  pt-0 text-info">Pot:<span class="text-warning"> {{this.mechanics.potSum}}</span><img src="../assets/Webgraphics/chipPile.png" style="decoration:none" width="55" height="55"></h2>
      <div class="Communitycards-holder">
      <p class='Communitycards  ml-4  '>
        <card class='size' v-for="card in this.comunityCards" :key="card" :card="BLANK_CARD"></card>
      </p>
      </div>
        <p id="theDeck" class='DECK mt-2  '>
                    <transition-group name="deckCard">

        <card  class= "deckCard DECKsize mt-4" v-for="card in this.mechanics.communityCards" :key="card" :card="card"></card>
                 <card  class= " DECKsize mt-4" v-for="card in this.mechanics.communityCards" :key="card" :card="card.BLANK_CARD"></card>

                    </transition-group>

                    
      </p>
                          <!-- <p id="theDeck" class='DECK mt-2'>
    <card  class= "deckCard DECKsize mt-4" v-for="card  in this.comunityCards" :key="card" :card="card"></card>
</p> -->


      </div>
      
    </div>

  </div>
                 <div class="userCards-wrapper">            
  <p class='TheUsercards'>
        <card class='size ' :card= this.mechanics.getUser().card1></card>
        <card class='size' :card= this.mechanics.getUser().card2></card>
     
      </p>
      
</div>


        <button type="button" class="btn btn-default btn-circle backButton  inner-blue BackButton" @click="doCountDown()">quit</button><br>

      <div class="row">
              <div class="col">
        <button type="button" class="btn btn-default btn-circle backButton  inner-blue BackButton" @click="backButton()"><i class="fa fa-arrow-left  fa-2x"></i></button><br>
        </div>
                </div>


                
            <!-- <div id="TableActions "  class="col">
  <div class="TA-SideBarContent TableActions2" >
     <div class="TableActions row">



        
         <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
        Check
        </div>
        <button type="button" class="btn  btn-lg check"  v-on:click="check(money)" :disabled="this.mechanics.checkAction == 1"><i class="fa fa-check  fa-lg"></i></button>
        </div>


     <div class="Action text-center lead text-white">
        <div class="ActionTitle">
          Fold
        </div>
        <button type="button" class="btn btn-lg fold" v-on:click="fold(money)" :disabled="this.mechanics.foldAction == 1"><strong><i class="fa fa-remove fa-lg "></i></strong></button>
        </div>

              <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
          Call
        </div>
        <button type="button" class="btn  btn-lg CALL "  v-on:click="call(money)" :disabled="this.mechanics.callAction == 1"><i class="fa fa-dollar  fa-lg"></i>{{this.mechanics.minimumBet}}</button>
        </div>


         <div class="Action text-center lead text-muted">
        <div class="ActionTitle">
        Bet/Raise
        </div>
        <button type="button" class="btn  btn-lg Bet "  v-on:click="bet(money)" :disabled="this.mechanics.betAction == 1"><i class="fa fa-chevron-up  fa-lg"></i></button>
        </div>



   <form  class="form-control bg-warning mt-5 mx-4 ">

              <div class="input-group input-group-lg ">

  <input v-model="money"  type="number" class="form-control mx-5 text-center bg-secondary lead text-light  " placeholder="Event Bar" aria-label="Event Bar" aria-describedby="basic-addon1" >
            </div>
           </form>

      </div>
  </div>
</div> -->



 </div>

</div>


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
import GameMech from '@/store/GameMechanics.ts'
import GameRequest from '@/store/GameRequest.ts'
import Seat from '@/components/table/Seat'
import {GameActionType} from '@/api/gameservice.ts'
import state from '../store/users'

export default {
  data () {
    return {
      // UserName: state.state.username,
      mechanics: new GameMech(this.$route.params.gameId, state.state.username),
      numberofPlayer: 0,
      preivousnumberofPlayers: 0,
      money: '0',
      BigBlindCurrentBet: 0,
      incrument: 0,
      posX: -191,
      posY: -141,
      posZ: 200,
      cardDown: false,
      comunityCards: {
        card1: '',
        card2: '',
        card3: '',
        card4: '',
        card5: ''
      }
      // gameStarted: false
    }
  },
  /*
  Below Should really be updated to avoid visual issues
  */
  watch: {
    roundNumber () {
      if (this.roundNumber === 1) {
        setTimeout(this.layCommunity, 1000)
      }
      // if (this.roundNumber >= 1) {
      //   this.layCommunity()
      // }
    },
    preivousnumberofPlayers  () {
      // this.mechanics = new GameMech(1, this.userId)
    },
    numberofPlayers () {
      // console.log(this.numberofPlayers)
      this.mechanics.setGame() // I left this for asnley
    },
    EventBarMessage () {
    },
    roundTwo () {
      // Show your hand Animation

      // if (this.roundTwo === true) {
      //   this.layCommunity()
      // }
    }
  },
  computed: {
    numberofPlayers () {
      return this.mechanics.getMultiplayers() // Kept this for ashley
    },
    UserName () {
      return this.$store.state.users.username
    },
    EventBarMessage () {
      return this.mechanics.gameStatus
    },
    roundTwo () {
      return this.mechanics.hasGameStarted
    },
    roundNumber () {
      return this.mechanics.roundNumber
    },
    opponents () {
      return this.mechanics.getOpponent()
    },
    GameOver () {
      return this.mechanics.isHandFinished
    }
  },
  methods: {
    giveRound () {
      console.log('heres your round' + this.roundNumber)
    },
    testGameStarted () {
      console.log(this.gameStarted)
      // this.gameStarted = !this.gameStarted
      // this.mechanics.gameStatus = 'hellow world'
      // this.EventBarMessage = 'Hello World'
      console.log(this.mechanics.getUser().card1)
    },
    findPlayer () {
      var player = this.mechanics.getOpponent()
      console.log(player)
      var user = this.mechanics.getUser()
      console.log(user)
    },
    showDown () {
      // var playerCards = document.get
      var playerCards = document.getElementsByClassName('oppHand')
      for (let i = 0; i < playerCards.length; i++) {
        playerCards[i].classList.toggle('active')
      }
    },
    layCommunity () {
      var card = document.getElementsByClassName('deckCard')
      var max = card.length
      card[0].style = 'z-index:3'
      card[0].style.transform = 'translateX(' + this.posX + 'pt) translateY(' + this.posY + 'pt) rotateX(-180deg) translatez(' + this.posZ + 'pt) rotatez(-180deg)'
      card[0].classList.remove('deckCard')
      console.log(max)
      this.posX = this.posX + 90
      window.setTimeout(this.pumpCard2, 750)
    },
    pumpCard2 () {
      var card = document.getElementsByClassName('deckCard')
      var max = card.length
      card[0].style = 'z-index:3'
      card[0].style.transform = 'translateX(' + this.posX + 'pt) translateY(' + this.posY + 'pt) rotateX(-180deg) translatez(' + this.posZ + 'pt)  rotatez(-180deg)'
      card[0].classList.remove('deckCard')
      console.log(max)
      this.posX = this.posX + 90
      window.setTimeout(this.pumpCard3, 750)
    },
    pumpCard3 () {
      var card = document.getElementsByClassName('deckCard')
      var max = card.length
      card[0].style = 'z-index:3'
      card[0].style.transform = 'translateX(' + this.posX + 'pt) translateY(' + this.posY + 'pt) rotateX(-180deg) translatez(' + this.posZ + 'pt) rotatez(-180deg)'
      card[0].classList.remove('deckCard')
      console.log(max)
      this.posX = this.posX + 90
      // this.deckLength = this.deckLength - 1
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
          console.log('Done')
          document.getElementById('Events').value = ''
        }
      }, 1000)
      document.getElementById('Events').value = ''
    },
    backButton () {
      const backButtonRequest = new GameRequest()
      backButtonRequest.removeCompetitiveGame(this.mechanics.gameId, state.state.userId).then(() => {
        this.$router.push('/Game')
      }).catch((error) => { console.log('Failed delete request' + error) })
    },
    force () {
      // console.log(this.mechanics.getMultiplayers())
    },
    ...mapActions([
      'logout'
    ]),
    logOut: function () {
      this.logout()
      const logoutRequest = new GameRequest()
      logoutRequest.removeCompetitiveGame(this.mechanics.gameId, state.state.userId).then(() => {
      }).catch((error) => { console.log('Failed delete request' + error) })
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
      this.premove(GameActionType.CALL, this.mechanics.minimumBet)
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
        console.log('you are trying to Bet with no money')
      }
    },
    sendCards: function () {
      this.mechanics.sendCards('CARD', 'TWOCARD')
    },
    sendAction: function () {
      this.mechanics.sendAction()
    },
    premove: function (action, money) {
      money = parseInt(money)
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
    let degreeIncrument = 180 / numberofPoints
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
    let degreeIncrument = (260 / numberofPoints)
    var radius = 238 // 280
    var x = 0
    var y = 0
    let theta = 140 + degreeIncrument / 2
    for (let i = 0; i < numberofPoints; i++) {
      x = radius * Math.cos(theta * Math.PI / 180.0).toFixed(3) // Convert to radians
      y = radius * Math.sin(theta * Math.PI / 180.0).toFixed(3)
      players[i].style.transform = 'translateX(' + x + 'pt) translateY(' + y + 'pt)'
      theta = theta + degreeIncrument
      // console.log('heres you x: ' + x + 'here your y:' + y + 'at ' + theta)
    }
    if (this.GameOver === true) {
      this.posX = -191
      this.showDown()
      //  Make Sure all the comunity cards have shown after this point
    }

    // if (this.roundNumber === 1) {
    //   setTimeout(this.layCommunity, 2000)
    // }
    if (this.roundNumber === 2) {
      setTimeout(this.pumpCard3, 2000)
    }
    if (this.roundNumber === 3) {
      setTimeout(this.pumpCard3, 2000)
    }
    // if (this.mechanics.isHandFinished === true) {
    //   setTimeout(this.showDown, 2000)
    // }
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
