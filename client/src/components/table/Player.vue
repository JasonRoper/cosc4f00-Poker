<template>
  <div>



    <div  class='oppenentContainer'>
<div  v-if ="this.isTurn === true">
<div  class="progress bg-secondary  theTimeBar">
<transition name="Action">
  <progress  id='theBar'  :value= this.progressBar class="progress-bar " role="progressbar" aria-valuenow="15" aria-valuemin="0" max="45"></progress>
</transition>
</div>
</div>

  <!-- <div class="progress-bar  " role="progressbar" style="width: 15%" aria-valuenow="15" aria-valuemin="0" aria-valuemax="100"></div> -->


      <div class="showImage " placeholder="asdas">
              <!-- <div class="showImage2 " placeholder="asdas">  
                          </div> -->

        <div v-if="this.data.isTurn === true" class="text-dark oppName lead bg-danger"> 
         <strong> <em>!</em>{{this.data.name}}<em>!</em> </strong>
        </div>
          <div v-else class="text-dark oppName lead"> 
         <strong> {{this.data.name}} </strong>
         <div v-if="this.data.isUser">This is the user</div>
        </div>

        <p>{{this.data.id}}</p> 
        <div class="text-white oppBalance">
          $ {{ this.data.money}}
          <!-- Put talbe action made -->
        </div>
      </div>
      <div class="oppentCards">
        <card v-ref='card1' ref='card1' id="hand" class="singleCard" :card="this.data.card1"></card>
        <card v-ref="card2" ref='card2' id="hand2" class="singleCard" :card="this.data.card2"></card>
      </div>
        <div v-show ="this.data.isDealer" id="isDealerContainer">
          
      <strong id= "isDealer" >D </strong>
      </div>



       <div  v-if ="this.data.action === 'FOLD'" id="isChipContainer">
      <strong id="chipAction" ><i class="fa fa-remove"></i></strong>
      </div>
               <transition name="Action">
      <div v-show = this.FoldAction id="isChipContainer">
      <strong id="chipAction" ><i  style = "padding-left:2px;padding-right:2px;" class="fa fa-remove"></i></strong>
      </div>
      </transition>

             <transition name="Action">
      <div v-show = this.CallAction id="isChipContainer">
      <strong id="chipAction" ><i  style = "padding-left:2px;padding-right:2px;" class="fa fa-dollar"></i></strong>
      <div id="chipMessage">${{this.data.currentBet}}</div>
      </div></transition>

       <transition name="Action">
      <div v-show = this.BetAction id="isChipContainer">
      <strong id="chipAction" ><i class="fa fa-chevron-up"></i></strong>
      <div id="chipMessage">${{this.data.currentBet}}</div>
      </div></transition>
      <transition name="Action">
      <div v-show = this.RasieAction id="isChipContainer">
      <strong id="chipAction" ><i class="fa fa-chevron-up"></i></strong>
      <div id="chipMessage">${{this.data.currentBet}}</div>
      </div></transition>

       <!-- <strong id="chipAction" ><i class="fa fa-chevron-up"></i></strong>
      <div id="chipMessage">${{this.data.currentBet}}</div> -->
      <!-- <div v-show="this.RasieAction" id="isChipContainer">
      <strong id="chipAction" ><i class="fa fa-chevron-up"></i></strong><div id="chipMessage">${{this.data.currentBet}}</div>
      </div> -->
          <transition name="Action">
      <div v-show = this.CheckAction id="isChipContainer">
      <strong id="chipAction" ><i class="fa fa-check"></i></strong>
      <div id="chipMessage">${{this.data.currentBet}}</div>
      </div>
      </transition>

    </div>
    <div>
      <button @click="test()"> A test</button>
       <button @click="test2()"> A test2</button>
              <button @click="test3()"> ShowHand</button>

      </div>
    <!--Display status big blind small blind  dealer etc   -->
  </div>
</template>

 
<script>
import Card from '@/components/table/Card'
import { GameActionType } from '@/api/gameservice'
export default {
  data () {
    return {
      player_num: 0,
      playerAction: this.data.action,
      RasieAction: false,
      FoldAction: false,
      CallAction: false,
      CheckAction: false,
      BetAction: false,
      progressBar: 0,
      timerDone: false,
      showHand: false,
      isUser: this.data.isUser
    }
  },
  props: ['data'],
  components: {
    card: Card
  },
  updated: function () {
   // this.seeAction()
  },
  methods: {
    addBar: function () {
      // this.progressBar = this.progressBar + 0.35
      // console.log(this.progressBar)
      if (this.progressBar >= 45) {
        this.timerDone = true
        // this.data.isTurn = false
        this.progressBar = 0
      }
    },
    // checkTimmer () {
    //   if (this.progressBar >= 45) {
    //     this.data.isTurn = false
    //     this.progressBar = 0
    //   }
    // },
    backButton () {
      this.$router.push('Game')
    },
    test () {
      // const brain = require('brain.js')
      // const network = new brain.NeuralNetwork()
      // network.train([
      // { input: { height: 60, weight: 150 }, output: { adult:1 } }
      // ])
      // const result = network.run({ height: 78, weight: 200 })
      // console.log(result)
      this.data.isTurn = true
      var countDown = new Date()
      countDown.setSeconds(countDown.getSeconds() + 45) // countDown.getMinutes() + numberofMins
    },
    test2 () {
      this.data.action = GameActionType.CALL
    },
    test3 () {
      this.showHand = !this.showHand
    },
    showRasieMessage () {
      this.BetMessage = true
    },
    resetActions () {
      this.BetAction = false
      this.RasieAction = false
      this.FoldAction = false
      this.CallAction = false
      this.CheckAction = false
    },
    seeAction () {
      if (this.data.action === 'BET') {
        alert('YO GON BETS EVERYONE')
        this.BetAction = true
      }
      if (this.data.action === 'RAISE') {
        alert('YO GON RAISE EVERYONE')
        this.RasieAction = true
      }
      if (this.data.action === 'CALL') {
        alert('Yo GON CALL EVERYONE')
        this.CallAction = true
      }
      if (this.data.action === 'CHECK') {
        alert('Yo GON CHECK EVERYONE')
        this.CheckAction = true
      }
      if (this.data.action === 'FOLD') {
        alert('Yo GON FOLD EVERYONE')
        this.FoldAction = true
      }
      if (this.data.action !== '') {
        this.data.action = ''
        let interval = 2200
        // window.setInterval(900)
        setTimeout(this.resetActions, interval)
      }
    }
  },
  mounted () {
    var playerCards = document.getElementsByClassName('singleCard')
    playerCards[playerCards.length - 1].classList.toggle('active')
    playerCards[playerCards.length - 2].classList.toggle('active')
  },
  watch: {
    isTurn () {
     /*  window.setInterval(() => {
        this.addBar()
      }, 100) */
    },
    showHand () {
      if (this.showHand === true) {
        // document.getElementById('hand').classList.toggle('active')
        // document.getElementById('hand2').classList.toggle('active')
        var playerCards = document.getElementsByClassName('singleCard')
        playerCards[playerCards.length - 1].classList.toggle('active')
        playerCards[playerCards.length - 2].classList.toggle('active')
      }
    }
  },
  computed: {
    isTurn () {
      return this.data.isTurn
    }
  }
}
  /*
  money: number
  id: number | null
  name: string
  tableAction: GameActionType[] | GameActionType
  premove: GameAction | null
  card1: string | null
  card2: string | null
  playing: boolean
  endGame: boolean | null
*/
</script>


<style src="@/assets/css/Opponents.css">

</style>



