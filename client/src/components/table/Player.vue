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
        <div v-show ="this.data.isDealer" id="isDealer">
         D 
      </div>
    </div>
    <!--Display status big blind small blind  dealer etc   -->
  </div>
</template>

 
<script>
import Card from '@/components/table/Card'
export default {
  data () {
    return {
      player_num: 0
    }
  },
  props: ['data'],
  components: {
    card: Card
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



