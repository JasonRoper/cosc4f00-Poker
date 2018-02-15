

<template>
    <div class='oppenentContainer'>
        <div class="showImage " placeholder="asdas"> 
            <p>{{this.data.id}}</p> 
            <div class="opponentInfo">
               <p>Account Balance: {{this.data.name}}</p>
               <p>Current Bet: {{this.data.money}}</p>
               
            
               <card :card="this.data.card1"></card>
                <card :card="this.data.card2"></card>
               <!--<p v-if='this.data.playing'>You are playing the Game</p>
               
              
               -->
            </div>
        </div>       
    </div>
</template>

 
<script>
import Card from '@/components/table/Card'
export default {
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



