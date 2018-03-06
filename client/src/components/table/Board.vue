<template>

<div> hello</div>
    
<!--
<div v-for="card in game.CommunityCards" :key="card">
      <card :card="card"></card>
    </div>
        <div class = "deck"></div>
    <div class = "burnCard"></div>

    <pot v-for="pot in game.pot" :key="pot" :data="pot"></pot>

    <player v-for="player in game.MultiplePlayers" :key="player" :data="player"></seat>
-->

</template>


<script>
import Player from '@/components/table/Player.vue'
import Seat from '@/components/table/Seat.vue'
import Card from '@/components/table/Card'
import TableActions from '@/components/table/TableActions'
import CardSuite from '@/types/cards'
import Actions from '@/types/actions'




export default {
  props: {
      game:{
          CommunityCards:[{
              card1: CardSuite.SPADES_FOUR
          },{
              card2: CardSuite.HEARTS_ACE
          },{
              card3: CardSuite.SPADES_TWO
          }
              
          ],
          User:{
            id: 1,
            username: 'One',
            turn: false,
            hasBet: true,
            money: 1000000,
            card1: CardSuite.HEARTS_ACE,
            card2: CardSuite.SPADES_TWO
            
          },
          MultiplePlayers:[{
                id: 1,
                username: 'One',
                turn: false,
                hasBet: true,
                money: 1000000,
                card1: CardSuite.HEARTS_ACE,
                card2: CardSuite.SPADES_TWO
            }, {
                id: 2,
                username: 'Two',
                turn: false,
                hasBet: true,
                money: 999,
                card1: CardSuite.SPADES_ACE,
                card2: CardSuite.HEARTS_THREE
            }, {
                id: 3,
                username: 'Three',
                turn: true,
                hasBet: true,
                money: 5555,
                card1: CardSuite.HEARTS_FOUR,
                card2: CardSuite.SPADES_FOUR
            }
          ],
        GameId: 1,
        Valid: true,
        Deck: null,
        Pots:[
            {
            names:[MultiplePlayers[0],MultiplePlayers[1]],
            pot: 500
            },{
                names:[MultiplePlayers[0],MultiplePlayers[2]],
                pot: 99999
            }
        ],
        Turn: 0 
      }        

      }
}
</script>