<template>
  <div class="col">
    <player v-for="player in opponents" :key="player.id" :data="player"/>
    <h4> Communnity Cards</h4>
    <card class='size' v-for="card in state.communityCards" :key="card" :card="card"/></card>

    <h4>Users Cards </h4>
    <card class='size' v-for="card in user.cards" :key="card" :card="card"/></card>

    <player :data="user"/>
    <actions @action="setAction" :active="state.active"/>
  </div>
</template>

<script>

import Player from '@/components/table/Player.vue'
import Card from '@/components/table/Card'
import TableActions from '@/components/table/TableActions'
import CardSuite from '@/types/cards'
import Actions from '@/types/actions'
import { GameService } from '@/api/gameservice'

export default {
  data () {
    return {
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
    }
  },
  components: {
    player: Player,
    actions: TableActions,
    card: Card
  },
  created () {
    this.gameService = new GameService(1)

    this.gameService.onGameUpdated((newState) => {
      console.log('type: %s , value: %s', typeof newState, JSON.stringify(newState))
    })
    this.gameService.sendAction({type: 'BET', bet: 1})
  },
  destroyed () {
    if (this.gameService) {
      this.gameService.finish()
    }
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
