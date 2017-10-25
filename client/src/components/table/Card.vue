<template>
  <div class="card" :class="card">
    {{ card }}
  </div>
</template>

<script>
export default {
  props: ['card']
}
</script>

<style src="@/assets/css/cards.css">
</style>