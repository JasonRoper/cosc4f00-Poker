<template>
<div>
<div class='thecontain'>
<div class=' button-nav' role='navigation'>
  <ul id='TableActions' class='nav navbar-nav thecontain'>
    <li v-for="(button, index) in buttons" :key="index" class='nav-item'>
      <button @click='button.event'>{{ button.text }}</button>
    </li>
  </ul>
  <div v-if="canBet">
    <input type='number'/>
  </div>
</div>
</div>
  </div>
</template>

<script>
// import Actions from '@/types/actions'
// import GameActionType from '@/api/gameservice'
export default {
  props: ['data'],
  methods: {
    bet (money) {
      this.$emit('clicked', money)
    }
  }
}

</script>
<style>

.thecontain{
  margin: 0px auto;
  border: 1px soild green;
}
</style>