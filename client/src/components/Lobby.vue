<template>
  <div class="col">
  <table-view v-bind:message="null"></table-view>
  </div>
</template>

<script>

import TableView from '@/components/TableView'

export default {
  methods: {
    sortcards () {
      for (let i = 0; i < this.state.communityCards.length; i++) {
        this.state.communityCards[i].css.style.color = 'blue'
      }
    }
  },
  components: {
    tableView: TableView
  }
}

</script>

  <style src="@/assets/css/Lobby.css">

</style>

