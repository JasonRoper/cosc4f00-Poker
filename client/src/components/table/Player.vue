

<template>
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
    </div>
</template>

 
<script>
import Card from '@/components/table/Card'
import TableActions from '@/components/table/TableActions'
// actions="this.data.useractions"
export default {
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



