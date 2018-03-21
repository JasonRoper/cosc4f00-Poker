<template>

  <div  class="Playingcard" :class="cssMappings[this.card]">
    <div  class='Pcard'>
    {{this.card}}
    </div>

  </div>
</template>

<script>
import { cssMappings } from '@/types/cards'
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