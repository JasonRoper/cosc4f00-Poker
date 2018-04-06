<template>
  <div>
    <!--======================================================jumbotron=============================================-->
    <div class="jumboTron-image">
      <div id="Home" class="jumbotron jumboTron-image jumbotron-fluid  text-white mb-4 ">
        <div class="container text-sm-center pt-5">
          <h1 class="display-1">PokerPals</h1>
          <h1 class=" tagLine text-dark lead ">
            <strong>We came to Play.</strong>
          </h1>
          <!--   button group-->
          <img src="../assets/Webgraphics/poker.png" width="40" height="40">
          <div class="btn-group px-3 py-4" role="group" aria-label="Basic example">
            <button v-if="!loggedIn" type="button" class=" lead theregister btn-outline-primary btn btn-info btn-lg" @click="registModal = true">Register Now</button>
             <button v-else type="button" class=" lead theregister btn-outline-primary btn btn-info btn-lg">  Play Poker  </button>
            <button href="#Developers" type="button" class="theregister btn btn-outline-light btn-lg lead ">See Developers</button>
          </div>
          <img src="../assets/Webgraphics/poker.png" width="40" height="40">
          <hr style="height:2px;color:#e2e2e2; background-color:#e2e2e2;">
          <!-- =========================================================Carrosal================================================================================================-->

        </div>
      </div>
    </div>
    <!-- Modal -->
    <div>
      <div class="modal fade" id="Register" data-backdrop="false" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle"
        aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
          <div class="modal-content">
            <div class="modal-header bg-success text-white">
              <h5 class="modal-title " id="exampleModalLongTitle">PokerPals Registration
                <img src="../assets/Webgraphics/poker.png" width="40" height="40">
              </h5>
              <button type="button" class="close" aria-label="Close" @click="closeRegisterModal()">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <h5 class="text-left pb-4">Create Account</h5>
              <form>
                <div v-show="registerErrorMessage !== ''">
                  <error-messages class=".error-messages" :Error="registerErrorMessage"></error-messages>
                </div>
                <!--Username input   -->
                <div class="form-group mb-3">
                  <div class="input-group mb-3">
                    <label>Please enter the following.</label>
                  </div>
                </div>
                <div class="input-group mb-3">
                  <div class="input-group-prepend">
                    <span class="input-group-text" id="basic-addon1">
                      <i class="fa fa-user"></i>
                    </span>
                  </div>
                  <input v-model="RegisterPlayer.username" type="text" class="form-control" placeholder="Username" aria-label="Username/Email"
                    aria-describedby="basic-addon1">
                </div>
                <!--Username input   -->
                <!-- Password Input -->
                <div class="input-group mb-3">
                  <div class="input-group-prepend">
                    <span class="input-group-text" id="basic-addon1">
                      <i class="fa fa-lock"></i>
                    </span>
                  </div>
                  <input v-model="RegisterPlayer.password" type="password" class="form-control" placeholder="Password" aria-label="Username/Email"
                    aria-describedby="basic-addon1">
                </div>
                <!--Password input   -->
                <!--Email input   -->
                <div class="input-group mb-3">
                  <div class="input-group-prepend">
                    <span class="input-group-text" id="basic-addon1">@</span>
                  </div>
                  <input v-model="RegisterPlayer.email" type="Email" class="form-control" placeholder="Email" aria-label="Email" aria-describedby="basic-addon1">
                </div>
                <!--Email input   -->
              </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-danger registerButton " @click="AttemptRegister()">Register</button>
              <button type="button" class="btn btn-warning " @click="closeRegisterModal()">Close</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!--/Modal -->

    <!--=======================================================================/Jumbotron========================================================================= -->
    <!--=======================================================================MainLanding Body========================================================================= -->
    <div class="container pt-4">
      <div class="row">
        <!--====================================================================Column 1 ========================================================================= -->
        <div class="col-lg-7">
          <h4 class="display-3  text-left">
            <u>Latest News</u>
          </h4>
          <p class="lead text-left">
            <!-- <img class="img-thumbnail d-flex px-4" src="../assets/Webgraphics/Undercon.jpg" > -->
            <div class="landingChip-wprapper">
              <div class="  d-flex px-2 chip-holder ">
                <button class="landingChip  bg-success btn btn-lg">
                  <hr>
                  <div class="button-title">POKER PALS</div>
                  <hr>
                </button>
              </div>
            </div>
            <p class="lead">

              Poker Pals is now curreently Withinin development phase following the generic phases set by Brock University's COSC 4f00
              team poker face. Within our venture we have now moved into the development phase of the Product, While our
              site is still currently under construction we hope to get things up and running as soon as possible. Until
              then please observe our structure and lay out of our up comming websitste and all the possible features we
              look for ward to possible providing in the meant time enjoy.
              <br> - Team Poker Face.
            </p>

          </p>
        </div>
        <!--====================================================================/Column 1 =========================================================================-->
        <!--====================================================================Column 2=========================================================================-->
        <div class="col-lg-5">
          <!--==================Table/Card:System stats==================-->
          <div class="card border-dark mb-3 mx-auto" style="max-width: 40rem;">
            <div class="card-header bg-secondary text-white text-left">
              <span class="lead">
                <i class="fa fa-bar-chart-o fa-lg"></i>
              </span>
              <u>System Statistics</u>
            </div>
            <div class="card-body text-dark">
              <ul class="list-group">
                <li class="list-group-item list-group-item-secondary d-flex justify-content-between align-items-center">
                  <div class="justify-content-left">
                    <i class="fa fa-users fa-lg pr-1"> </i> Memebers: </div>
                  <div class="ml-5 pl-5">
                    <span class="badge badge-light badge-pill">{{Members}}</span>
                  </div>
                </li>
                <li class="list-group-item list-group-item-secondary d-flex justify-content-between align-items-center">
                  <div class="justify-content-left">
                    <img src="../assets/Webgraphics/PTable.png" style="decoration:none" width="35" height="35"> Games Today:</div>
                  <span class="badge badge-light badge-pill">{{gamesToday}}</span>
                </li>
                <li class="list-group-item list-group-item-secondary d-flex justify-content-between align-items-center">
                  <div class="justify-content-left">
                    <img src="../assets/Webgraphics/poker.png" style="decoration:none" width="35" height="35"> Players Online:</div>
                  <span class="badge badge-light badge-pill">{{onlineMembers}}</span>
                </li>
                <li class="list-group-item list-group-item-secondary d-flex justify-content-between align-items-center">
                  <div class="justify-content-left">
                    <img src="../assets/Webgraphics/Pokerchip.png" style="decoration:none" width="35" height="35"> Ranking1#:</div>
                  <span v-if="Level1Members" class="badge badge-light badge-pill">{{Level1Members.username}}</span>
                  <span v-else class="badge badge-light badge-pill">...</span>
                </li>
                <li class="list-group-item list-group-item-secondary d-flex justify-content-between align-items-center">
                  <div class="justify-content-left">
                    <img src="../assets/Webgraphics/PokerL2.png" style="decoration:none" width="35" height="35"> Ranking2#:</div>
                  <span v-if="Level1Members" class="badge badge-light badge-pill">{{Level2Members.username}}</span>
                  <span v-else class="badge badge-light badge-pill">...</span>
                </li>
                <li class="list-group-item list-group-item-secondary d-flex justify-content-between align-items-center">
                  <div class="justify-content-left">
                    <img src="../assets/Webgraphics/chipPile.png" style="decoration:none" width="35" height="35">Ranking3#:</div>
                  <span v-if="Level1Members" class="badge badge-light badge-pill">{{Level3Members.username}}</span>
                  <span v-else class="badge badge-light badge-pill">...</span>
                </li>
              </ul>
            </div>
          </div>
          <!--=======/ Table:System stats=======-->
          <!--=======/Login Card=======-->
          <div class="card border-dark mb-3 mx-auto" style="max-width: 40rem;">
            <div class="card-header text-left text-white bg-secondary lead" v-if="!loggedIn">
              <i class="fa fa-user-circle pr-2 fa-lg"></i>
              <u>Login</u>
            </div>
            <div class="card-header text-left text-white bg-secondary lead" v-else>
              Play Poker!
            </div>
          </div>
          <div class="card-body text-dark" v-if="!loggedIn">
            <form class="needs-validation" ref="theform" action="/#/Lobby">
              <div v-show="loginErrorMessage !== ''">
                <error-messages class="error-messages" :Error="loginErrorMessage"></error-messages>
              </div>
              <div class="form-group">
                <input v-model="Player.username" ref="myTestField" type="text" class="form-control nameInput" id="formGroupExampleInput"
                  placeholder="UserName/Email">
              </div>
              <div class="form-group">
                <input v-model="Player.password" ref="myTestField2" type="password" class="form-control passwordInput" id="formGroupExampleInput2"
                  placeholder="Password">
              </div>
              <div class="form-group">
                <button class="btn btn-darkgrey btn-lg btn-block text-dark text-center loginButton" type="button" value="Login" @click="AttemptLogin()"
                  :disabled="(this.clickedLogin === true)||(this.clickedRegister === true)">
                  <i class="fa fa-sign-in"></i> Login</button>
              </div>
              <div class="form-group">
                <button class="btn btn-warning btn-lg btn-block text-dark  text-center " type="button" value="Register"
                  @click="registModal = true" :disabled="(this.clickedLogin === true )||(this.clickedRegister === true )" data-target="#Register">
                  <i class="fa fa-sign-in"></i> Register</button>
              </div>
              <div class="form-group">
                <p class="text-right">Forgot password?
                  <br>
                  <a style="text-decoration:none" href="">
                    <i class="fa fa-sign-in"></i> Password reset </a>
                </p>
              </div>
            </form>
          </div>
          <div class="card-body text-dark" v-else>
            <GameMenu/>
          </div>
        </div>

        <!--=======/Login Card=======-->
        <!--===================================================/Login Card=======-->
        <!--====================================================================/Column 2 =========================================================================-->
      </div>

      <!--===================================================Row 3================-->
      <div class="row">
        <div class="col-lg">
          <!--====================================================Developers=================-->
          <div>
            <hr style="height:2px;color:#333; background-color:#333;">
          </div>
          <h1 id="Developers" class="display-4 text-center  text-muted ">LeaderBoard</h1>
          <!--====================================================Listdevelopers=================-->
          <div class="pb-4">
            <hr style="height:2px;color:#333; background-color:#333;">
          </div>
          <!--====================================================/Developers=================-->
          <table class="table table-hover table-dark justify-content-center">
            <thead>
              <tr>
                <th scope="col">Username</th>
                <th scope="col">Poker Chips</th>
                <th scope="col">#HandsWon</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="player in leaderboard" :key="player.id">
                <th scope="row">{{player.username}}</th>
                <td>{{player.money}}</td>
                <td>0</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>



      <!--====================================================================Features row=========================================================================-->



      <!--===================================================Row 2================-->


      <!--===================================================/Row 2================-->




      <!--===================================================Row 3================-->
      <div class="row">
        <div class="col-lg">
          <!--====================================================Developers=================-->
          <div>
            <hr style="height:2px;color:#333; background-color:#333;">
          </div>
          <h1 id="Developers" class="display-4 text-center  text-muted ">Developers</h1>
          <!--====================================================Listdevelopers=================-->
          <div class="pb-4">
            <hr style="height:2px;color:#333; background-color:#333;">
          </div>
          <!--====================================================/Developers=================-->
        </div>
      </div>
      <div class="row">
        <!--Developer 1 -->
        <div class="col-md-6 col-lg-3">
          <div class="card mb-3 ">
            <img class="card-img-top   border-0" src="../assets/Webgraphics/crown.png" alt="Javon Luke">
            <div class="card-body">
              <h5 class="card-title">Javon Luke</h5>
              <p class="card-text">Team Lead and Front-End porgrammer lead of team Pokerface with great interest in Web and Mobile(IOS) Development.</p>
            </div>
          </div>
        </div>
        <!--/Developer 1 -->
        <!--Developer 2 -->
        <div class="col-md-6 col-lg-3">
          <div class="card mb-3 ">
            <img class="card-img-top" src="../assets/Webgraphics/pointy-hat.png" alt="Adam Caverhill">
            <div class="card-body">
              <h5 class="card-title">Adam Caverhill</h5>
              <p class="card-text">Back-End Programmer and Documentation manager of PokerFace, Adam's interest Lies mainly within Back-end Development
                for the web. </p>
            </div>
          </div>
        </div>
        <!--/Developer 2 -->
        <!--Developer 3 -->
        <div class="col-md-6 col-lg-3">
          <div class="card mb-3 ">
            <img class="card-img-top" src="../assets/Webgraphics/developers.png" alt="Jason Roper">
            <div class="card-body">
              <h5 class="card-title">Jason Roper</h5>
              <p class="card-text"> Application Architect and DevOps of team Poker Face, Jason's delight are more found within Networking Programming.</p>
            </div>
          </div>
        </div>
        <!--/Developer 3 -->
        <!--Developer 4 -->
        <div class="col-md-6 col-lg-3">
          <div class="card mb-3 ">
            <img class="card-img-top" src="../assets/Webgraphics/TopHat.png" alt="">
            <div class="card-body">
              <h5 class="card-title">Ashley Hamilton-Morley</h5>
              <p class="card-text">Front-end Programmer, Test Lead and UserInterface, Ashley's Interest are heavily invested in Game and Web development.
                </p>
            </div>
          </div>
        </div>
        <!--/Developer 4-->
        <!--====================================================Listdevelopers=================-->
      </div>
    </div>

    <!--===================================================/Row 3================-->
    <!-- </div> -->

    <!--=======================================================================/Mai nLanding Body========================================================================= -->
  </div>
</template>

<script>
import { mapActions } from 'vuex' // used for maping actions of the vue store files
import ErrorMessages from '@/components/WebComponents/ErrorMessages' // ErrorMessages Components
import GameMenu from '@/components/GameMenu'
import { API_V1 } from '@/config'
import axios from '@/api/axios'
// import router from '@/router'
export default {
  name: 'Login',
  data () {
    return {
      /* The Name of the Component.
      */
      // Representation of a Player Logging in
      Player: {
        username: '',
        password: ''
      },
      // Representation of a  player that wants to register
      RegisterPlayer: {
        username: '',
        password: '',
        email: ''
      },
      Members: 23000,
      gamesToday: 21000,
      onlineMembers: 2300,
      Level1Members: null,
      Level2Members: null,
      Level3Members: null,
      registModal: false,
      after: false,
      clickedLogin: false,
      clickedRegister: false,
      checked: false,
      checkedRegisterErrors: false,
      iszero: false,
      errorMessage: '',
      leaderboard: []
    }
  },
  watch: {
    registModal (val) {
      if (val) {
        $('#Register').modal({show: true, backdrop: true})
        this.errorMessage = ''
      } else {
        $('#Register').modal('hide')
      }
    }
  },
  computed: {
    loggedIn () {
      return this.$store.getters.loggedIn
    },
    username () {
      return this.$store.state.users.username
    },
    loginBlank () {
      return (this.Player.username.length === 0) || (this.Player.password.length === 0)
    },
    registerBlank () {
      return ((this.RegisterPlayer.username.length === 0) || (this.RegisterPlayer.password.length === 0) || (this.RegisterPlayer.email.length === 0))
    },
    loginErrorMessage () {
      // if(this.after === true)
      let loginErrors = this.$store.state.users.errors.login
      if (this.errorMessage !== '') {
        return this.errorMessage
      }
      if (loginErrors.length !== 0) {
        return loginErrors[0].message
      }
      return ''
    },
    registerErrorMessage () {
      let registrationErrors = this.$store.state.users.errors.registration
      if (this.errorMessage !== '') {
        return this.errorMessage
      }
      if (registrationErrors.length !== 0) {
        return registrationErrors[0].message
      }
      return ''
    }
  },
  methods: {
    ...mapActions([
      'register',
      'login',
      'logout'
    ]),
     /*
      Attempts login  givent the requested fields If the fields
       are empty then return an aerror
    */
    AttemptLogin () {
      if (this.loginBlank) {
        this.errorMessage = 'Both Fields Must Be Filled'
        return // Must add CSS for this action
      } else {
        this.errorMessage = ''
        this.clickedLogin = true
        this.login(this.Player).then((response) => {
          this.clickedLogin = false
          if (this.loggedIn) {
            this.errorMessage = ''
          }
        })
        // Servery queries for login
        // add animation
        // setTimeout(this.checkLoginErrors, 3000) // Chec Login Errors after 900 ms
      }
    },
    AttemptRegister () {
      if (this.registerBlank) {
        this.errorMessage = 'All fields must filled'
        return // Must add CSS for this action
      } else {
        this.clickedRegister = true
        this.register(this.RegisterPlayer).then(() => {
          this.errorMessage = ''
          this.clickedRegister = false
          if (this.loggedIn) {
            this.errorMessage = ''
            this.closeRegisterModal()
          }
        })
        // setTimeout(this.checkRegisterErrors, 4000) // Check Registratio Errors after 900 ms
      }
    },
    closeRegisterModal () {
      this.registModal = false
      this.RegisterPlayer.username = ''
      this.RegisterPlayer.email = ''
      this.RegisterPlayer.password = ''
    },
    updateGeneralInfo () {
      axios.get(API_V1 + '/info').then((response) => {
        let data = response.data
        this.Members = data.registeredUsers
        this.onlineMembers = data.usersOnline + 1
        this.gamesToday = data.activeGames
        this.Level1Members = data.highestRated[0]
        this.Level2Members = data.highestRated[1]
        this.Level3Members = data.highestRated[2]
      })
    },
    updateLeaderBoard() {
      axios.get(API_V1 + '/users?sort=money').then((response) => {
        this.leaderboard = response.data.data.slice(0,5)
      })
    }
  },
  created () {
    this.updateGeneralInfo()
    this.updateLeaderBoard()
  },
  components: {
    errorMessages: ErrorMessages,
    GameMenu
  }
}
</script>
<style src="@/assets/css/Login.css"></style>
