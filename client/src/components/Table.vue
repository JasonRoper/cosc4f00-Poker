<template>
  <div class="col">
     <div class="opps">
       <player   v-for="player in opponents" :key="player.id" :data="player"/>
     </div>
    <h4> Communnity Cards</h4>
    <div class='Communitycards'>
      <card  class='size' v-for="(card,index) in state.communityCards" :key="card" :card="card"/></card>
    </div>
    <h4>Users Cards </h4>
    <div class='userCards'>
     
      <card class='size' v-for="card in user.cards" :key="card" :card="card"/></card>
  
       <player class='userInfo' :data="user"/>
    </div>
    <actions class='actionsPosition' @action="setAction" :active="state.active"/>
  </div>
</template>

<script>

import Player from '@/components/table/Player.vue'
import Card from '@/components/table/Card'
import TableActions from '@/components/table/TableActions'
import CardSuite from '@/types/cards'
import Actions from '@/types/actions'

export default {
  data () {
    return {
      cardPos: 30,
      opponents: [{
        id: 1,
        username: 'Jason',
        account: 100000,
        bet: 110
      },
      {
        id: 2,
        username: 'Lucy',
        account: 100000,
        bet: 210
      }],
      user: {
        id: 2,
        username: 'Javon',
        account: 1000000,
        bet: 110,
        cards: [CardSuite.HEARTS_ACE, CardSuite.SPADES_TWO],
        nextAction: Actions.NONE
      },
      state: {
        active: 2,
        communityCards: [CardSuite.HEARTS_EIGHT, CardSuite.SPADES_THREE, CardSuite.SPADES_ACE],
        pot: 0
      }
    }
  },
  methods: {
    setAction (event) {
      this.user.nextAction = event
    },
    sortcards () {
      for (let i = 0; i < this.state.communityCards.length; i++) {
        this.state.communityCards[i].css.style.color = 'blue'
      }
    }
  },
  components: {
    player: Player,
    actions: TableActions,
    card: Card
  }
}
</script>

  <style src="@/assets/css/Table.css">

</style>

