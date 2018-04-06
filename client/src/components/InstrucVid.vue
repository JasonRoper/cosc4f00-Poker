<template>
  <div :id="name" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog  modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h4>
            How to Play Poker
          </h4>
          <button type="button" class="close" @click="$emit('close')">&times;</button>
        </div>
        <div class="modal-body">
          <iframe :id="name+'vid'" width="560" height="315" src="https://www.youtube.com/embed/GAoR9ji8D6A?rel=0&version=3&enablejsapi=1" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    props: ['name', 'showModal'],
    watch: {
      showModal (val) {
        if (val) {
          $('#' + this.name).modal({show: true, backdrop: true})
        } else {
          $('#' + this.name).modal('hide')
          $('#' + this.name + 'vid').stopVideo()
        }
      }
    }
  }
</script>
<style>
</style>
