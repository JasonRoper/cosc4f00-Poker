<template>
<div class='nav justify-content-center' role='navigation'>
   <div class='container'>
    <div class='row'>
    <div class='col-sm-12'>
  <div class=' button-nav'>
  <ul id='TableActions'class='nav navbar-nav'>
    <button @click='Bet()'><li class='nav-item'>Bet</li></button>
    <button><li class='nav-item' >Call</li></button>
    <button><li class='nav-item'>Raise</li></button>
    <button><li class='nav-item'>Check</li></button>
    <button><li class='nav-item'>Fold</li></button>

 

</ul>

<div v-if='shown==true'>
  <input type='number'></input>
</div>

  </div>
</div>
</div>
</div>
 </div>
</template>

<script>
 export default {
   data () {
     return {
       bet: 'Bet',
       shown2: false,
       shown: false}
   },
   methods: {
     Bet: function () {
       this.shown = true
       if (this.shown2 === true) {
         this.shown = true
         let inputElement = document.createElement('INPUT')
         let buttonNav = document.getElementById('TableActions')
         inputElement.className = 'text'
         inputElement.size = '10'
         buttonNav.appendChild(inputElement)
         console.log('tell me')
       }
     },
     Raise: function () {
       this.shown = true
       console.log('tell me')
     },
     Call: function () {
       this.shown = true
       console.log('tell me')
     },
     Check: function () {
       this.shown = true
       console.log('tell me')
     },
     Fold: function () {
       this.shown = true
       console.log('tell me')
     }
   }
 }
 </script>

<style>

  .button-nav{

    border: 7px solid red;
    background-color: #ABC;
    border-radius: 12px;
    margin-left:330px;
    margin-right:550px;
    overflow:hidden;
    height:85px;
    position:fixed;


  
  }

</style>