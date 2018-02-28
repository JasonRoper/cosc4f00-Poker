

<template>
<<<<<<< HEAD
    <div class='oppenentContainer'>
        <div class="showImage " placeholder="asdas"> 
            <p>{{this.data.id}}</p> 
            <div class="opponentInfo">
               <p>Account Balance: {{this.data.name}}</p>
               <p>Current Bet: {{this.data.money}}</p>
               
            
               <card :card="this.data.card1"></card>
                <card :card="this.data.card2"></card>
          
          <table-actions :data="this.data.tableAction" @clicked="bet"></table-actions>
               
            </div>
        </div>       
=======
  <div>
    <div  class='oppenentContainer'>
      <div class="showImage " placeholder="asdas"> 
        <div class="text-white oppName"> 
          {{this.data.name}}
        </div>
        <p>{{this.data.id}}</p> 
        <div class="text-white oppBalance">
          $ {{ this.data.money}}
        </div>
      </div>
      <div class="oppentCards">
        <card class="singleCard":card="this.data.card1"></card>
        <card class="singleCard" :card="this.data.card2"></card>
      </div>
>>>>>>> 368b9f83d873b3d3d25cf70a34e40f837eb0dace
    </div>
  </div>
</template>

 
<script>
import Card from '@/components/table/Card'
import TableActions from '@/components/table/TableActions'
// actions="this.data.useractions"
export default {
  data () {
    return {
      player_num: 0
    }
  },
  props: ['data'],
  methods: {
    bet (value) {
      alert('Bet was clicked') // someValue
      alert(value)
    }
  },
  components: {
    card: Card,
    tableActions: TableActions
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



