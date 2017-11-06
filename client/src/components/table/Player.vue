<template>
    <div>
        <div class="showImage" placeholder="asdas"> 
            <p>{{this.data.username}}</p> 
            <div class="opponentInfo">
               <p>Account Balance: {{this.data.account}}</p>
               <p>Current Bet: {{this.data.bet}}</p>
            </div>
        </div>
       
    </div>
</template>

<script>
  export default {
    props: ['data']
  }
</script>


<style src="@/assets/css/Opponents.css">

</style>



