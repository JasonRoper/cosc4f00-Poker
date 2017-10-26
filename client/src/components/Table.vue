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
import Card from '@/components/table/Card'
import TableActions from '@/components/table/TableActions'
import Cards from '@/types/cards'

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
        cards: [Cards.SPADES_ACE, Cards.SPADES_TWO]
      },
      state: {
        active: 2,
        community_cards: []
      }
    }
  },
  methods: {
    send_event (event) {

    }
  },
  components: {
    player: Player,
    actions: TableActions,
    card: Card
  }
}
</script>
