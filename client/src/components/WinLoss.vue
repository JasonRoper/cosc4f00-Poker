<template>
  <div class="col">
     <div class="opps">
       <player   v-for="player in opponents" :key="player.id" :data="player">


       </player>
     </div>

    <div class='card' v-for= "player in opponents" :key="player.id">
        <div class = 'showCard' v-if="player.result">
           <player :data="player"></player>
        </div>
        <div class='showCard' v-else>
            <card class='size' v-for="card in player.cards" :key="card" :card="card"/></card>
        </div>
    </div>
    <a href="/NextGame">NextGame</a>
    <a href="/Lobby">Lobby</a>

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
        username: 'Jasddon',
        account: 100000,
        cards: [CardSuite.HEARTS_ACE, CardSuite.SPADES_TWO],
        bet: 110

      },
      {
        id: 2,
        username: 'Lucy',
        account: 100000,
        cards: [CardSuite.HEARTS_ACE, CardSuite.SPADES_TWO],
        bet: 210
      },
      {
        id: 2,
        username: 'Javon',
        account: 1000000,
        bet: 110,
        cards: [CardSuite.HEARTS_ACE, CardSuite.SPADES_TWO],
        nextAction: Actions.NONE
      }],
      state: {
        active: 2,
        communityCards: [CardSuite.HEARTS_EIGHT, CardSuite.SPADES_THREE, CardSuite.SPADES_ACE],
        lobby: true,
        pot: 0

      },
      empty: {
        card: CardSuite.HEARTS_EIGHT
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

<style src="@/assets/css/Lobby.css">

</style>

