<template>
<div>
  <!--======================================================jumbotron=============================================-->
    <div id="Home" class="jumbotron jumbotron-fluid bg-dark text-white mb-4 ">
  <div class="container text-sm-center pt-5">
    <h1 class="display-1">PokerPals</h1>
    <p class="lead"> We came to Play.</p>
<!--   button group-->
    <div class="btn-group" role="group" aria-label="Basic example">
  <button type="button" data-toggle="modal" data-target="#Register" class="btn btn-info btn-lg">Register Now</button>
  <a href="#Developers" type="button" class="btn btn-light btn-lg">See Developers</a>
</div>
<!-- =========================================================Carrosal================================================================================================-->
<div id="carouselExampleIndicators" class="carousel slide z-index-0" data-ride="carousel">
  <ol class="carousel-indicators">
    <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
    <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
    <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
  </ol>
  <div class="carousel-inner">
    <div class="carousel-item active">
      <img class="d-block w-100" src="../assets/Webgraphics/Gon13.png" alt="First slide" height="42" width="100">
    </div>
    <div class="carousel-item">
      <img class="d-block w-100" src="../assets/Webgraphics/log.svg" alt="Second slide" height="42" width="100">
    </div>
    <div class="carousel-item">
      <img class="d-block w-100" src="../assets/Webgraphics/Opponent.svg" alt="Third slide" height="42" width="100">
    </div>
  </div>
  <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
    <span class="sr-only">Previous</span>
  </a>
  <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
    <span class="carousel-control-next-icon" aria-hidden="true"></span>
    <span class="sr-only">Next</span>
  </a>

<!-- =========================================================/Carrosal================================================================================================-->


</div>
<!-- button group-->
  </div>
</div>
<!-- Modal -->
<div class="modal fade" id="Register" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header bg-success text-white">
        <h5 class="modal-title " id="exampleModalLongTitle">PokerPals Registration
        <img src="../assets/Webgraphics/poker.png" width="40" height="40"></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <h5 class="text-left pb-4">Create Account</h5>
        <form>
          <!--Username input   -->
          <div class="form-group mb-3">
             <div class="input-group mb-3">
             <label>Please provide a Username/email and Password.</label></div>
          </div>
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="basic-addon1">@</span>
              </div>
              <input type="text" class="form-control" placeholder="Username/Email" aria-label="Username/Email" aria-describedby="basic-addon1">
            </div>
          <!--Username input   -->
          <!-- Password Input -->
             <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" id="basic-addon1"><i class="fa fa-lock"></i></span>
              </div>
              <input type="password" class="form-control" placeholder="Passowrd" aria-label="Username/Email" aria-describedby="basic-addon1">
            </div>
          <!--Password input   -->
      </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-danger ">Register</button>
        <button type="button" class="btn btn-warning " data-dismiss="modal">Close</button>
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
      <h4 class="display-3  text-left"><u>Latest News</u></h4>
      <p class="lead text-left"><img class="img-thumbnail d-flex px-4" src="../assets/Webgraphics/Undercon.jpg" >Poker Pals is now curreently Withinin 
      development phase following the generic phases set  by Brock University's COSC 4f00 team poker face. Within our venture we have now moved into the development phase of the Product, While our site is still currently under construction we hope to get things up and running as soon as possible. Until then please observe our structure and lay out of our up comming websitste and all the possible features we look for ward to possible providing in the meant time enjoy.<br> - Team Poker Face. </p>
    </div>
    <!--====================================================================/Column 1 =========================================================================-->
     <!--====================================================================Column 2=========================================================================-->
    <div class="col-lg-5">
      <!--==================Table/Card:System stats==================-->
      <div class="card border-dark mb-3 mx-auto" style="max-width: 40rem;">
        <div class="card-header bg-secondary text-white text-left"> 
          <span class="lead" ><i class="fa fa-bar-chart-o fa-lg"></i></span>
          <u>System Statistics</u>
          </div>
        <div class="card-body text-dark">
          <ul class="list-group">
              <li class="list-group-item list-group-item-secondary d-flex justify-content-between align-items-center">
               <div class="justify-content-left"> <i class="fa fa-users fa-lg pr-1"> </i> Memebers: </div>
                <div class="ml-5 pl-5"><span class="badge badge-light badge-pill">{{Members}}</span></div>
              </li>
              <li class="list-group-item list-group-item-secondary d-flex justify-content-between align-items-center">
                <div class="justify-content-left"> <img src="../assets/Webgraphics/pTable.png" style="decoration:none" width="35" height="35" > Games Today:</div>
                <span class="badge badge-light badge-pill">{{gamesToday}}</span>
              </li>
              <li class="list-group-item list-group-item-secondary d-flex justify-content-between align-items-center">
                 <div class="justify-content-left"> <img src="../assets/Webgraphics/poker.png" style="decoration:none" width="35" height="35"> Players Online:</div>
                <span class="badge badge-light badge-pill">{{onlineMembers}}</span>
              </li>
              <li class="list-group-item list-group-item-secondary d-flex justify-content-between align-items-center">
                <div class="justify-content-left"> <img src="../assets/Webgraphics/PokerCHIP.png" style="decoration:none" width="35" height="35"> Ranking1#:</div>
                <span class="badge badge-light badge-pill">{{Level1Members}}</span>
              </li>
               <li class="list-group-item list-group-item-secondary d-flex justify-content-between align-items-center">
                  <div class="justify-content-left"> <img src="../assets/Webgraphics/PokerL2.png" style="decoration:none" width="35" height="35"> Ranking2#:</div>
                <span class="badge badge-light badge-pill">{{Level2Members}}</span>
              </li>
              <li class="list-group-item list-group-item-secondary d-flex justify-content-between align-items-center">
                 <div class="justify-content-left"> <img src="../assets/Webgraphics/chipPile.png" style="decoration:none" width="35" height="35">Ranking3#:</div>
                <span class="badge badge-light badge-pill">{{Level3Members}}</span>
              </li>
          </ul>
        </div>
      </div><!--=======/ Table:System stats=======-->
<!--=======/Login Card=======-->
<div class="card border-dark mb-3 mx-auto" style="max-width: 40rem;">
  <div class="card-header text-left text-white bg-secondary lead"><i class="fa fa-user-circle pr-2 fa-lg"></i> <u>Login</u></div>
    <div class="card-body text-dark">
      <form class="needs-validation" ref="theform" action="/#/Lobby">
  <div class="form-group">
    <input  v-model="Player.username" ref="myTestField" type="text" class="form-control" id="formGroupExampleInput" placeholder="UserName/Email">
  </div>
  <div class="form-group">
    <input v-model="Player.password" ref="myTestField2" type="password" class="form-control" id="formGroupExampleInput2" placeholder="Password">
  </div>
    <div class="form-group">
      <button class="btn btn-darkgrey btn-lg btn-block text-dark text-center" type="button" value = "Login"  @click="checkInputs()"><i class="fa fa-sign-in"></i> Login</button>
  </div>
  <div class="form-group">
      <button class="btn btn-warning btn-lg btn-block text-dark  text-center" type="button" value ="Register"  data-toggle="modal" data-target="#Register">  <i class="fa fa-sign-in"></i> Register</button>
  </div>
  <div class="form-group">
     <p class="text-right">Forgot password?<br><a  style="text-decoration:none" href=""><i class="fa fa-sign-in"></i> Password reset </a></p>
  </div>
</form>
  </div>
</div><!--=======/Login Card=======-->
<!--===================================================/Login Card=======-->
<!--====================================================================/Column 2 =========================================================================-->
    </div>
</div>
<div class="row">
  <div class="col-lg">
    <!--====================================================LeaderBoard=================-->
    <div class="card border-dark mb-3 mx-auto">
            <div class="card-header text-center text-white bg-secondary"><i class="fa fa-clipboard"></i>LeaderBoard</div>
            <div class="card-body text-dark">
            <table class="table table-hover table-dark justify-content-center">
              <thead>
                <tr>
                  <th scope="col">Username</th>
                  <th scope="col">Poker Chips</th>
                  <th scope="col">#HandsWon</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <th scope="row">Mark</th>
                  <td>6000</td>
                  <td>6</td>
                </tr>
                <tr>
                  <th scope="row">Jacob</th>
                  <td>5000</td>
                  <td>5</td>
                </tr>
                <tr>
                  <th scope="row">Tomas</th>
                  <td>5</td>
                  <td>5</td>
                </tr>
              </tbody>
          </table>
      </div>
    </div>
  </div>
  <!--====================================================LeaderBoard=================-->
</div>
<!--====================================================================Features row=========================================================================-->



<!--===================================================Row 2================-->


<!--===================================================/Row 2================-->




<!--===================================================Row 3================-->
  <div class="row">
    <div class="col-lg">
      <!--====================================================Developers=================-->
        <div >
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
        <img class="card-img-top" src="../assets/Webgraphics/pointy-hat.png" alt="Adam Caverhill"  >
        <div class="card-body">
          <h5 class="card-title">Adam Caverhill</h5>
          <p class="card-text">Back-End Programmer and Documentation manager of PokerFace, Adam's interest Lies mainly within Back-end Development for the web.  </p>
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
          <p class="card-text">Front-end Programmer, Test Lead and UserInterface, Ashley's Interest are heavily invested in Game and Web development. </p>
        </div>
      </div>  
    </div>
<!--/Developer 4-->
<!--====================================================Listdevelopers=================-->
</div>

<!--===================================================/Row 3================-->
</div>

<!--=======================================================================/Mai nLanding Body========================================================================= -->
</div>
</template>

<script>
import { mapActions } from 'vuex'
export default {
  data () {
    return {
      Player: {
        ID: ' ',
        username: '',
        password: ''
      },
      Members: 23000,
      gamesToday: 21000,
      onlineMembers: 2300,
      Level1Members: 3000,
      Level2Members: 6000,
      Level3Members: 1300,
      LoginSucessful: false,
      onPage: true
    }
  },
  methods: {
    ...mapActions([
      'storeName'
    ]),
    checkInputs () {
      console.log('Password length is' + this.Player.password.length)
      if ((this.Player.username.length === 0) || (this.Player.password.length === 0)) {
        return
      } else {
        this.storeName(this.Player.username)
      }
    }
  }
}
</script>
<style>
</style>