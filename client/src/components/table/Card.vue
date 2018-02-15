<template>

  <div  class="card" :class="this.card">
    <div  class='Pcard'>
    {{this.card}} 
    </div>

  </div>
</template>

<script>
export default {
  props: {
    card: [String]
  }
}
</script>


<style src="@/assets/css/cards.css">

</style>