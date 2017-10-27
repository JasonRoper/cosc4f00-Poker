<template>
  <div>
    <player v-for="player in players" :key="player.id" :data="player"/>
   
    <card class='size' v-for="card in user.cards" :key="card" :card="card"/></card>

    <player :data="user"/>
    <div class='container'>
    <div class='row'>
    <div class='col-lg-12'>
    <actions @action="send_event" :active="state.active"/>
  </div>
  </div>
  </div>
  </div>
</template>

<script>

import Player from '@/components/table/Player.vue'
import Card from '@/components/table/Card'
import TableActions from '@/components/table/TableActions'
import CardSuite from '@/types/cards'

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
        cards: [CardSuite.HEARTS_ACE, CardSuite.SPADES_TWO]
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
<style>
  .size{
    height: 138px;
    width:  103px;
    background-repeat:no-repeat;
    position:center;
  }
</style>
