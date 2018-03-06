<template>     
    <div>
        {{ playersNames() }}        
        {{ money }}

    </div>
    

</template>


<script>
import Player from '@/components/table/Player.vue'
export default {
  props: {
      //I did this so that it is ot the whole player Object that is being passed through
      players: Player[],
      money: Number
  }
  /*
  methods: {
      function : playersNames () {
          let potPlayers = "";
          
          for(var i=0;i<player.length;i++)
          {
              potPlayers+=this.players[i].username;
              potPlayer+=" ";
          }
          
          
          return potPlayers;

   
      
        }
 
    */
}
</script>