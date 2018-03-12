<template>
  <div>
    <div  class='oppenentContainer'>
      <div class="showImage " placeholder="asdas"> 
        <div class="text-white oppName"> 
          {{this.data.name}}
        </div>
        <p>{{this.data.id}}</p> 
        <div class="text-white oppBalance">
          $ {{ this.data.money}}
          <!-- Put talbe action made -->
        </div>
      </div>
      <div class="oppentCards">
        <card class="singleCard" :card="this.data.card1"></card>
        <card class="singleCard" :card="this.data.card2"></card>
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
      </div>
    <!--Display status big blind small blind  dealer etc   -->
  </div>
</template>

 
<script>
import Card from '@/components/table/Card'
export default {
  data () {
    return {
      player_num: 0,
      playerAction: this.data.action,
      RasieAction: false,
      FoldAction: false,
      CallAction: false,
      CheckAction: false,
      BetAction: false

    }
  },
  props: ['data'],
  components: {
    card: Card
  },
  updated: function () {
    // alert('hellow')
    this.seeAction()
  },
  methods: {
    test () {
      this.data.action = 'BET'
    },
    test2 () {
      this.data.action = 'CHECK'
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
        window.setInterval(900)
        setTimeout(this.resetActions, interval)
      }
    }
  },
  watch: {
    playerAction (query) {
      alert('changed')
      if (query === 'BET') {
        alert('change')
        console.log('hi')
        this.playerAction = ''
      }
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



