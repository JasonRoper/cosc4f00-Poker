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
         <div  v-if ="this.data.tableAction === 'FOLD'" id="isChipContainer">
      <strong id="chipAction" ><i class="fa fa-remove"></i></strong>
      </div>
          <div v-if ="this.data.tableAction === 'CALL' " id="isChipContainer">
      <strong id="chipAction" ><i class="fa fa-dollar"></i></strong><div id="chipMessage">$Message</div>
      </div>
       <transition name="Action">
      <div v-show = this.BetAction id="isChipContainer">
      <strong id="chipAction" ><i class="fa fa-chevron-up"></i></strong>
      </div></transition>
      <div v-if ="this.data.tableAction === 'RAISE' " id="isChipContainer">
      <strong id="chipAction" ><i class="fa fa-chevron-up"></i></strong><div id="chipMessage">$Message</div>
      </div>
       <div  v-if ="this.data.tableAction === 'CHECK' " id="isChipContainer">
      <strong id="chipAction" ><i class="fa fa-check"></i></strong><div id="chipMessage">$Message</div>
      </div>
       <transition name="Message"><div v-if ="this.data.tableAction === 'BET' " id="isChipContainer"><div id="chipMessage">$Message</div></div></transition>
    </div>
    <div>
      <button @click="test()"> A test</button>
       <button @click="test2()"> A test</button>
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
      playerAction: this.data.tableAction,
      RasieMessage: false,
      Foldction: false,
      CallAction: false,
      CheckAction: false,
      BetAction: false

    }
  },
  props: ['data'],
  components: {
    card: Card
  },
  updated () {
    this.playerAction = this.data.tableAction
    if (this.playerAction === 'BET') {
      this.BetAction = true
    }
  },
  methods: {
    test () {
      // this.playerAction = 'none'
      this.data.tableAction = 'BET'
      // document.getElementById('chipMessage').classList.toggle('active')
    },
    test2 () {
      // this.playerAction = 'none'
      this.data.tableAction = ''
      // this.BetMessage = false
      // document.getElementById('chipMessage').classList.toggle('active')
    },
    showRasieMessage () {
      this.BetMessage = true
      // this.data.tableAction = ''
    }
  },
  computed: {
    playerAction () {
      this.playerAction = this.data.tableAction
      return this.playerAction
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



