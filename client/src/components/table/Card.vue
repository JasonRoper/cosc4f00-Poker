<template>
  <div class=" Playingcard pokerCard">
<div class=" Playingcard  blank card topCard">   </div>

  <div  class="Playingcard bottomCard" :class="cssMappings[this.card]">
    <div  class='Pcard'>
    <!-- {{this.card}} -->
    </div>
  </div>

  </div>
</template>

<script>
import { cssMappings } from '@/types/cards.ts'
export default {
  props: {
    card: [String]
  },
  data () {
    return {
      cssMappings
    }
  }
}
</script>


<style src="@/assets/css/cards.css">

</style>