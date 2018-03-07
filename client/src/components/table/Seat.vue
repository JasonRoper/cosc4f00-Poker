<template>
   <div> seat
<div v-if="this.data.id === -1">
Write out that there is no player for this position
        <button type="button" class="btn  btn-lg "><i class="fa fa-user-plus fa-lg"></i></button>

</div>
<div v-else-if="this.data.isPlayer === true">
  This is classified as the user
  <player :data="this.data"></player>
</div>
<div v-else>
   <player :data="this.data"></player>
</div>
</div>
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

