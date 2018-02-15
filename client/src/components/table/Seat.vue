<template>
   <div> seat
<div v-if="this.data.id === -1">
Write out that there is no player for this position
</div>
<div v-else>
   <player :data="this.data"></player>
</div>
</div>
    <!--
    <div v-for= "player in multiplePlayers" :key="player.id">
        <div v-if="player.id===this.user.id">
            
        </div>
        <div v-else>
            <player :data="player" :user="false"></player>
        </div>
    </div>
-->
</template>


<script>

import Player from '@/components/table/Player'
export default {
  props: ['data'],
  components: {
    player: Player
  }
}
</script>


  <style src="@/assets/css/Lobby.css">

</style>

