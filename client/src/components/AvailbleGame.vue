<template>
<div>
   <a href="#" class="list-group-item list-group-item-action">{{this.gameData.name}}</a>
  
    </div>
  
</template>

<script>
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
  props: ['gameData']
}
</script>