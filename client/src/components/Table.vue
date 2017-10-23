<template>
  <div>
    <player v-for="player in players" :key="player.id" :data="player"/>
    <card v-for="card in user.cards" :key="card" :card="card"/>
    <player :data="user"/>
    <actions @action="send_event" :active="state.active"/>
  </div>
</template>

<script>

import Player from '@/components/table/Player'
import TableActions from '@components/table/Button'

export default {
  data () {
    return {
      opponents: [{
        id: 1,
        username: 'Jason',
        account: 100000,
        bet: 110
      }],
      user: {
        id: 2,
        username: 'Javon',
        account: 1000000,
        bet: 110,
        cards: ['S1', 'D1']
      },
      state: {
        active: true
      }
    }
  },
  methods: {
    send_event () {

    }
  },
  components: {
    player: Player,
    actions: TableActions
  }
}
</script>
