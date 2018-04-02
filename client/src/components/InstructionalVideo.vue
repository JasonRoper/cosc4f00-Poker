<template>
  <div>
    <div id="page-bg"    class="container-fluid text-sm-center mycontent ">
       <div class="menu-icon" @click="toggleSidebar()" >
    <span></span>
    <span></span>
    <span></span>
  </div>
      <div class="container text-sm-center mycontent ">
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
                <h2 class="display-4 text-white  text-left ml-5 pl-5 mr-0 mt-3 pt-3 pb-4 "> 
        <button type="button" class="btn btn-default btn-circle backButton  inner-blue" @click="backButton()"><i class="fa fa-arrow-left  fa-2x"></i></button><br>
        <u>Instructional Poker</u> </h2>
        <iframe width="560" height="315" src="https://www.youtube.com/embed/GAoR9ji8D6A?rel=0" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe>
              </div>

       </div>
      </div>
    </div>
  </div>
</template>
<script>
</script>
<style src="@/assets/css/GameNav.css"></style>
<style src="@/assets/css/Table.css"></style>
