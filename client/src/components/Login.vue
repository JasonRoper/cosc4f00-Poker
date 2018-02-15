<template>
<div>
  <!--======================================================jumbotron=============================================-->
    <div class="jumbotron jumbotron-fluid bg-dark text-white mb-4 ">
  <div class="container text-sm-center pt-5">
    <h1 class="display-1">PokerPals</h1>
    <p class="lead"> We came to Play.</p>
<!--   button group-->
    <div class="btn-group" role="group" aria-label="Basic example">
  <button type="button"   data-toggle="modal" data-target="#register"class="btn btn-info btn-lg">Register Now</button>
  <a href="speakers" type="button" class="btn btn-light btn-lg">See Developers</a>
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
<!--=======================================================================/Jumbotron========================================================================= -->
<!--=======================================================================MainLanding Body========================================================================= -->
<div class="container pt-4">
  <div class="row">
    <!--====================================================================Column 1 ========================================================================= -->
    <div class="col-lg-7">
      <h4 class="display-4 font-weight-bold"><u>Latest News</u></h4>
      <p class="lead text-left"><img class="img-thumbnail d-flex px-4" src="../assets/Webgraphics/Undercon.jpg" >Poker Pals is now curreently Withinin 
      development phase following the generic phases set  by Brock University's COSC 4f00 team poker face. Within our venture we have now moved into the development phase of the Product, While our site is still currently under construction we hope to get things up and running as soon as possible. Until then please observe our structure and lay out of our up comming websitste and all the possible features we look for ward to possible providing in the meant time enjoy.<br> - Team Poker Face. </p>
    </div>
    <!--====================================================================/Column 1 =========================================================================-->
     <!--====================================================================Column 2=========================================================================-->
    <div class="col-lg-5">
      <!--==================Table/Card:System stats==================-->
      <div class="card border-dark mb-3 mx-auto" style="max-width: 40rem;">
        <div class="card-header bg-secondary text-white">System Statistics</div>
        <div class="card-body text-dark">
          <ul class="list-group">
              <li class="list-group-item list-group-item-secondary d-flex justify-content-between align-items-center">
                Memebers:
                <span class="badge badge-light badge-pill">{{Members}}</span>
              </li>
              <li class="list-group-item list-group-item-secondary d-flex justify-content-between align-items-center">
                Ganes Today:
                <span class="badge badge-light badge-pill">{{gamesToday}}</span>
              </li>
              <li class="list-group-item list-group-item-secondary d-flex justify-content-between align-items-center">
                Players Online:
                <span class="badge badge-light badge-pill">{{onlineMembers}}</span>
              </li>
              <li class="list-group-item list-group-item-secondary d-flex justify-content-between align-items-center">
                Ranking1#:
                <span class="badge badge-light badge-pill">{{Level1Members}}</span>
              </li>
               <li class="list-group-item list-group-item-secondary d-flex justify-content-between align-items-center">
                Ranking2#:
                <span class="badge badge-light badge-pill">{{Level2Members}}</span>
              </li>
              <li class="list-group-item list-group-item-secondary d-flex justify-content-between align-items-center">
                Ranking3#:
                <span class="badge badge-light badge-pill">{{Level3Members}}</span>
              </li>
          </ul>
        </div>
      </div><!--=======/ Table:System stats=======-->
<!--=======/Login Card=======-->
<div class="card border-dark mb-3 mx-auto" style="max-width: 40rem;">
  <div class="card-header text-left text-white bg-secondary"><u>Login</u></div>
    <div class="card-body text-dark">
      <form ref="theform" action="/#/Lobby">
  <div class="form-group">
    <input  v-model="Player.username"ref="myTestField" type="text" class="form-control" id="formGroupExampleInput" placeholder="UserName/Email">
  </div>
  <div class="form-group">
    <input type="text" class="form-control" id="formGroupExampleInput2" placeholder="Password">
  </div>
    <div class="form-group">
      <input class="btn btn-darkgrey btn-lg btn-block text-dark" type="button" value = "Login"  @click="storeName(Player.username)">
  </div>
  <div class="form-group">
      <p class="text-right">New to PokerPals? </p>
      <input class="btn btn-warning btn-lg btn-block text-dark" type="button" value = "Register"  @click="storeName(Player.username)">
  </div>
</form>
  </div>
</div><!--=======/Login Card=======-->
<!--===================================================/Login Card=======-->
<div class="card border-dark mb-3 mx-auto" style="max-width: 40rem;">
        <div class="card-header text-center text-white bg-secondary">LeaderBoard</div>
        <div class="card-body text-dark">
        <table class="table table-hover table-dark justify-content-center">
          <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">First</th>
              <th scope="col">Last</th>
              <th scope="col">Handle</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th scope="row">1</th>
              <td>Mark</td>
              <td>Otto</td>
              <td>@mdo</td>
            </tr>
            <tr>
              <th scope="row">2</th>
              <td>Jacob</td>
              <td>Thornton</td>
              <td>@fat</td>
            </tr>
            <tr>
              <th scope="row">3</th>
              <td colspan="2">Larry the Bird</td>
              <td>@twitter</td>
            </tr>
          </tbody>
      </table>
  </div>
</div><!--=======/Login Card=======-->
<!--====================================================================/Column 2 =========================================================================-->
    </div>
  </div>
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
      Level3Members: 1300
    }
  },
  methods: {
    ...mapActions([
      'storeName'
    ])
  }
}
</script>
<style>
</style>