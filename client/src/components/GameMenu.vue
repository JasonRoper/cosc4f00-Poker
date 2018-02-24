<template>
<div>
  <!-- <div class="container"> -->
    <!-- <div  class="row "> -->
          <!-- <div class="col"> -->
            <h2 class="display-1 text-white pt-3 pb-3 mt-4 pb-5">Poker Pals!!</h2> 
            <h2 class="display-4 text-white pb-3 text-center justify-content-left"><u>MAIN MENU</u> </h2> 
            <div class="text-center mx-4 ">
            <div class="chip-holder"><button class="around-words inner-yellow btn btn-lg" @click="instrcutionPokerMode=!instrcutionPokerMode"><hr><button class="inner-button  btn btn-lg">Instructional Poker</button><hr></button></div>
          <div class="chip-holder"><button class="around-words inner-green btn btn-lg" @click="casualPokerMode=!casualPokerMode"><hr><button class=" btn btn-lg">Casual Poker</button><hr></button></div>
            <div class="chip-holder"><button class="around-words inner-blue btn btn-lg"@click="competitivePokerMode=!competitivePokerMode"><hr><button class="btn btn-lg">Competitive Poker</button><hr></button></div>
            <!-- <table-view :userId="1" ></table-view>  -->
          <!-- </div> -->
          <!-- </div> -->
    </div>
  </div>
   <!-- <table-view :userId="1" ></table-view>  -->

  
</template>

<script>

import Player from '@/components/table/Player.vue'
import Card from '@/components/table/Card'
import TableActions from '@/components/table/TableActions'
import CardSuite from '@/types/cards'
import Actions from '@/types/actions'
import TableView from '@/components/TableView'

export default {
  data () {
    return {
      cardPos: 30,
      instrcutionPokerMode: '',
      casualPokerMode: '',
      competitivePokerMode: '',
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
  watch: {
    // Watches the poker mode selected to select which cue should be shownr
    instrcutionPokerMode: function (selection) {
      alert('Going to instruct')
    },
    casualPokerMode: function (selection) {
      alert('Going to Casual')
    },
    competitivePokerMode: function (selection) {
      alert('Going to Competitive')
    }
  },
  components: {
    player: Player,
    actions: TableActions,
    card: Card,
    tableView: TableView
  }
}
</script>

  <style src="@/assets/css/GMenu.css">

</style>

