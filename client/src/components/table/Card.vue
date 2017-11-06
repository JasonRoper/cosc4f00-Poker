<template>

  <div  class="card" :class="card">
    <div  class='Pcard'>
    {{this.card}} 
    </div>
  </div>
</template>

<script>
export default {
  props: {
    card: [String],
    cardPosition: {
      type: Number
    }
  },
  data () {
    return {
      blank: 'a'
    }
  }
}
</script>


<style src="@/assets/css/cards.css">

</style>