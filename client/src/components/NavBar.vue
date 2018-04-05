<template>
<div>
  
<!--====================================================================Navbar=========================================-->
<nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top mb-3" >
  <div class="container ">
    <a class="nav-item text-secondary" href="#">PokerPals</a>
    <img src="../assets/Webgraphics/poker.png" width="50" height="50">
    <!--Add image logo -->
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse justify-content-end " id="navbarNavAltMarkup">
      <div class="navbar-nav nav nav-pills mb-3 bg-lights" id="pills-tab" role="tablist">
        <!-- <ul class="nav nav-pills mb-3 bg-lights" id="pills-tab" role="tablist"> -->
    <a class=" nav-item  nav-link active bg-transparent active" id="pills-home-tab" data-toggle="pill" href="#pills-home" role="tab" aria-controls="pills-home" aria-selected="true">Home</a>
    <a class="nav-item nav-link bg-transparent" id="pills-features-tab" data-toggle="pill" href="#pills-features" role="tab" aria-controls="pills-features" aria-selected="false">Features</a>
    <a class=" nav-item nav-link bg-transparent" id="pills-about-tab" data-toggle="pill" href="#pills-about" role="tab" aria-controls="pills-about" aria-selected="false">About</a>
    <a class="nav-item nav-link" href="#Support">Support</a>
    <a class="nav-item nav-link"  data-toggle="modal" data-target="#Register2"> Login</a>
      </div>
    </div>
  </div>
</nav>

<div class="tab-content" id="pills-tabContent">
 <div class="tab-pane fade show active" id="pills-home" role="tabpanel" aria-labelledby="pills-home-tab"><login></login></div>
 <div class="tab-pane fade" id="pills-features" role="tabpanel" aria-labelledby="pills-features-tab"><features></features></div> 
  <div class="tab-pane fade" id="pills-about" role="tabpanel" aria-labelledby="pills-about-tab"><about></about></div>
</div>
<!--====================================================================/Navbar=========================================-->
<!-- <button @click="Showhome = !Showhome">Change Now</button> -->

<!-- <login v-show="Showhome"></login> -->
<!--==================*
    MODAL Login!!!
*==================-->
<div class="modal fade" id="Register2" tabindex="-1" role="dialog"   data-backdrop="false" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header bg-danger text-white">
        <h5 class="modal-title " id="exampleModalLongTitle">PokerPals Login
        <img src="../assets/Webgraphics/poker.png" width="40" height="40"></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <h5 class="text-left pb-4">Login</h5>
        <form>
          <!--Username input   -->
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="basic-addon1">@</span>
              </div>
              <input v-model="Player.username" type="text" class="form-control" placeholder="Username/Email" aria-label="Username/Email" aria-describedby="basic-addon1">
            </div>
          <!--Username input   -->
          <!-- Password Input -->
             <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="basic-addon1"><i class="fa fa-lock"></i></span>
              </div>
              <input v-model="Player.password" type="password" class="form-control" placeholder="Passowrd" aria-label="Username/Email" aria-describedby="basic-addon1">
            </div>
          <!--Password input   -->
           <error-messages  :Error="this.ErrorMessage "  v-show="this.isLoginError"></error-messages>
      </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-success" @click="AttemptLogin()">Login</button>
        <button type="button" class="btn btn-info " data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
</div>
</template>

<script>
import Login from '@/components/Login'
import Features from '@/components/Features'
import About from '@/components/About'
import ErrorMessages from '@/components/WebComponents/ErrorMessages' // ErrorMessages Components
import { mapActions } from 'vuex' // used for maping actions of the vue store files
export default{
  data: function () {
    return {
      Showhome: true,
      ErrorMessage: '',
      isLoginError: false,
      // Representation of a Player Logging in
      Player: {
        username: '',
        password: ''
      }
    }
  },
  components: {
    login: Login,
    features: Features,
    about: About,
    errorMessages: ErrorMessages
  },
  methods: {
    ...mapActions([
      'register',
      'login'
    ]),
         // Attempts login  givent the requested fields If the fields are empty then return an aerror
    AttemptLogin () {
      this.ResetVariables()
      if ((this.Player.username.length === 0) || (this.Player.password.length === 0)) {
        this.isLoginError = true
        this.ErrorMessage = 'Both Fields Must Be Filled'
        return // Must add CSS for this action
      } else {
        this.login(this.Player) // Servery queries for login
        this.loginErrorMessage = this.$store.state.users.errors.login // Sets login errorMessage to the store array to the store array of error login message
        setTimeout(this.checkLoginErrors, 3000) // Chec Login Errors after 900 ms
      }
    },
    checkLoginErrors () {
      if (this.loginErrorMessage.length > 0) {
        this.ErrorMessage = this.loginErrorMessage[0].message
        this.isLoginError = true
        return
        // this.$store.state.users.errors.login = []
      }
      if ((this.loginErrorMessage.length === 0)) { // if ater check
        this.$router.push('Game')
        alert('start game')
      }
    },
    showLoginModal () {
      this.$modal.show('hello-world')
    },
    ResetVariables () {
      this.isRegistrationError = false
      this.isLoginError = false
      this.ErrorMessage = ''
    }
  }
}
</script>
<style src="@/assets/css/NavBar.css">
</style>
