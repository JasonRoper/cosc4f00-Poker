<template>
<div >

  <!-- MenuIcon -->
  <!-- <div id="sidebar"> -->
    <div class="menu-icon" @click="toggleSidebar()" >
      <span></span>
      <span></span>
      <span></span>
    </div>
  <!-- </div>   -->

  <!--/Menu Icon-->
  <!-- Side Bar Content -->
  <!--  -->


  <!--/Side BAr content  -->
  <div  id="page-bg" >
    <!-- <div  class=" container text-lg-center"> -->
    <div class="row">
            <div id="sidebar"  class="col">
  <div >
    <a href="#" ><img src="../assets/Webgraphics/Opponent.svg" class=" border border-white rounded-circle" width="70" height="70"></a>
    <ul class="menu">
      <li><a href="#">Poker Pals</a></li>
       <li><a href="#">Pals</a></li>
      <li><a href="#">Games</a></li>
      <li><a href="#">Groups</a></li>
      <li><a href="#">Trophics</a></li>
      <li><a href="#">Options</a></li>
      <li><a href="#">Sign Outs</a></li>
    </ul>
    <ul class="social-icon ">
      <li><a href="#"><i class=" fa fa-twitter fa-lg fa-2x " aria-hidden="true"></i></a></li>
      <li><a href="#"><i class=" fa fa-google-plus fa-lg fa-2x" aria-hidden="true"></i></a></li>
      <li><a href="#"><i class=" fa fa-linkedin fa-lg fa-2x" aria-hidden="true"></i></a></li>
      <li><a href="#"><i class=" fa fa-instagram fa-lg fa-2x" aria-hidden="true"></i></a></li>

    </ul>
  </div>
</div>
      <div id="page-content" class="col">
        <nav>
  <div class="nav nav-tabs" id="nav-tab" role="tablist">
    <a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true">Game</a>
    <a class="nav-item nav-link" id="nav-profile-tab" data-toggle="tab" href="#nav-profile" role="tab" aria-controls="nav-profile" aria-selected="false">Profile</a>
    <a class="nav-item nav-link" id="nav-contact-tab" data-toggle="tab" href="#nav-contact" role="tab" aria-controls="nav-contact" aria-selected="false">Community</a>
  </div>
</nav>
<div class="tab-content" id="nav-tabContent">
  <div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab"><game-menu></game-menu></div>
  <div class="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab">...</div>
  <div class="tab-pane fade" id="nav-contact" role="tabpanel" aria-labelledby="nav-contact-tab">...</div>
</div>
        </div>
      </div>
    <!-- </div> -->
    </div>
   </div>

</template>

<script>
import TableView from '@/components/TableView'
import GameMenu from '@/components/GameMenu'
export default{
  data () {
    return {
      windowWidth: 'document.documentElement.clientWidth',
      toggleon: false
    }
  },
  components: {
    tableView: TableView,
    gameMenu: GameMenu
  },
  watch: {
    windowWidth: function (query) {
      console.log(query)
    }
  },
  mounted () {
    this.$nextTick(function () {
      window.addEventListener('resize', this.adjustsideBar)
    })
  },
  beforeDestroy () {
    window.removeEventListener('resize', this.adjustsideBar)
  },
  methods: {
    toggleSidebar: function () {
      document.getElementById('sidebar').classList.toggle('active')
      document.getElementById('page-content').classList.toggle('active')
     // if (document.documentElement.clientWidth <= 1600) {
       // document.getElementById('page-content').classList.toggle('active')
        // document.getElementById('sidebar').classList.toggle('active')
      // }
      // if (document.documentElement.clientWidth > 1600) {
       // document.getElementById('page-content').classList.remove('active')
      // }
      // document.getElementById('page-content').style.transform = 'translatex(10%)'
    },
    adjustsideBar: function () {
      document.getElementById('page-content').classList.remove('active')
      document.getElementById('sidebar').classList.remove('active')
     // this.toggleSidebar()
    }
  }
}

</script>

<style src="@/assets/css/GameNav.css">

</style>