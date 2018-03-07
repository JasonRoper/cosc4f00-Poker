<template  >
<div id="page-bg"    class="container-fluid text-sm-center mycontent ">

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


  <div class="container text-sm-center mycontent "  >
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

<ul class="nav nav-tabs" id="myTab" role="tablist">
  <li class="nav-item">
    <a class="nav-link active" id="game-tab" data-toggle="tab" href="#game" role="tab" aria-controls="game" aria-selected="true">Game</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false">Profile</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" id="contact-tab" data-toggle="tab" href="#contact" role="tab" aria-controls="contact" aria-selected="false">Contact</a>
  </li>
</ul>
<div class="tab-content" id="myTabContent">
  <div class="tab-pane fade show active" id="game" role="tabpanel" aria-labelledby="home-tab"><game-menu></game-menu></div>
  <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">somthine 2</div>
  <div class="tab-pane fade" id="contact" role="tabpanel" aria-labelledby="contact-tab">...</div>
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
// import GameRequest from '@/store/GameRequest'
export default{
  data () {
    return {
      // gameRequest = new GameRequest(),
      windowWidth: 'document.documentElement.clientWidth',
      toggleon: false
    }
  },
  components: {
    tableView: TableView,
    gameMenu: GameMenu
  },
  watch: {
    // gameRequest: function (newValue, oldValue) { },
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
    createGame: function () {
      // gameRequest.createGame()
    },
    toggleSidebar: function () {
      document.getElementById('sidebar').classList.toggle('active')
      document.getElementById('page-content').classList.toggle('active')
    },
    adjustsideBar: function () {
      document.getElementById('page-content').classList.remove('active')
      document.getElementById('sidebar').classList.remove('active')
    }
  }
}

</script>

<style src="@/assets/css/GameNav.css">

</style>