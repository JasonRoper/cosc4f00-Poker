<template>
<div>
    <card :card = "CommunityCards[0]" ></card>
</div>
</template>


<script>
// import Player from '@/components/table/Player.vue'
import Card from '@/components/table/Card.vue'
// import CardSuite from '@/types/cards'
export default {
  props: {
    CommunityCards: ['One Card', 'Two Card']// CardSuite.SPADES_FOUR, CardSuite.HEARTS_ACE]
  },
  components: {
    card: Card
  }
}
</script>