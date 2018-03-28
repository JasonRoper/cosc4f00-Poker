<template  >
<div id="page-bg"    class="container-fluid text-sm-center mycontent ">
  <!-- MenuIcon -->
  <!-- <div id="sidebar"> -->
  <div class="menu-icon" @click="toggleSidebar()" >
    <span></span>
    <span></span>
    <span></span>
  </div>
  <div class="container text-sm-center mycontent "  >
    <!-- <div  class=" container text-lg-center"> -->
    <div class="row">
      <div id="sidebar"  class="col">
        <div class="SideBarContent" >
          <div class="nameTitle">{{this.UserName}}</div> <br>
          <a href="" ><img src="../assets/Webgraphics/Opponent.svg" class=" border border-white rounded-circle" width="70" height="70"></a>
          <ul class="menu">
            <li><a>Poker Pals</a></li>
            <li><a href="#">Pals</a></li>
            <li><a href="#">Options</a></li>
            <li><a @click="logOut()">Sign Out</a></li>
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
          <div class="tab-pane fade show active" id="game" role="tabpanel" aria-labelledby="home-tab"><gameMenu></gameMenu></div>
          <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">somthine 2</div>
          <div class="tab-pane fade" id="contact" role="tabpanel" aria-labelledby="contact-tab">...</div>
        </div>
      </div>
    </div>
  </div>
</div>
</template>
<script>
// import TableView from '@/components/TableView'
import { mapActions } from 'vuex' // used for maping actions of the vue store files
import GameMenu from '@/components/GameMenu' // ErrorMessages Components
export default {
  name: 'Game',
  data () {
    return {
      windowWidth: 'document.documentElement.clientWidth',
      toggleon: false
    }
  },
  components: {
    // tableView: TableView,
    gameMenu: GameMenu
  },
  watch: {
    windowWidth: function (query) {
      console.log(query)
    }
  },
  computed: {
    UserName () {
      return this.$store.state.users.username
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
    ...mapActions([
      'logout'
    ]),
    toggleSidebar: function () {
      document.getElementById('sidebar').classList.toggle('active')
      document.getElementById('page-content').classList.toggle('active')
    },
    adjustsideBar: function () {
      document.getElementById('page-content').classList.remove('active')
      document.getElementById('sidebar').classList.remove('active')
    },
    logOut: function () {
      this.logout()
      this.$router.push('Home')
    }
  }
}
</script>
<style src="@/assets/css/GameNav.css">
</style>