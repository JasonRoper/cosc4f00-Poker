<template>
<div >
       <h2 class="display-1 text-white pb-2 mt-2 pb-2">PokerPals!!  <img src="../assets/Webgraphics/poker.png" width="100" height="100"></h2>
        <div v-show="GameMenu">
        <h2 class="display-4 text-white pb-3 text-left ml-5 pl-5 mr-0 mt-5 pt-5"><u>MAIN MENU</u> </h2>
        <div class="text-center mx-4 ">
        <div class="chip-holder"><button class="around-words inner-yellow btn btn-lg" @click="showInstructPoker()"><hr><button class="inner-button  btn btn-lg">Instructional Poker</button><hr></button></div>
      <div class="chip-holder"><button class="around-words inner-green btn btn-lg" @click="showCasualPoker()"><hr><div class="button-title">Casual Poker</div><hr></button></div>
        <div class="chip-holder"><button class="around-words inner-blue btn btn-lg" @click="chipChoice('CompetitivePoker')"><hr><button class="btn btn-lg">Competitive Poker</button><hr></button></div>
      </div>
      </div>
          <div  v-show="instrcutionPokerMode">
      <div >
      <h2 class="display-4 text-white  text-left ml-5 pl-5 mr-0 mt-5 pt-5 "> <button type="button" class="btn btn-default btn-circle" @click="backButton()">back</button><u>Instructional(CPU) Poker</u> </h2>
       <div class="pt-5 mt-5">
       <div class="chip-holder "><button class="around-words inner-grey btn btn-lg" @click="chipChoice('PokerTutorial')"><hr><div class="button-title">Poker Tutorial</div><hr></button></div>
        <div class="chip-holder "><button data-toggle="modal" data-target="#Register2" class="around-words inner-orange btn btn-lg"><hr> CPU Challenge<hr></button></div>
              </div>
              </div>
      </div>
        <div  v-show="competitivePokerMode">
      <div >
      <h2 class="display-4 text-white text-left ml-5 pl-5 mr-0 "> <button type="button" class="btn btn-default btn-circle"  @click="chipChoice('CompetitivePoker')">back</button><u>Competitive Poker</u> </h2>  
              </div>
      </div>
      <div  v-show="casualPokerMode">
      <div>
      <h2 class="display-4 text-white text-left ml-5 pl-5 mr-0 "> <button type="button" class="btn btn-default btn-circle" @click="backButton()">back</button><u>Casual</u> </h2>
              <div class="pt-0">
         <div class="chip-holder pt-5 mt-5"><button class="around-words2 inner-grey btn btn-lg" data-toggle="modal" data-target="#CreateCasualPoker"><hr><div class="button-title">Create Game</div><hr></button></div>
        <div class="chip-holder pt-5 mt-5"><button class="around-words2 inner-blue btn btn-lg" data-toggle="modal" data-target="#JoinCasualPoker"><hr><button class="btn btn-lg">Join Game</button><hr></button></div>
              </div>
              </div>
      </div>


        <div  v-show="showTable">
      <div>
      <h2 class="display-4 text-white text-left ml-5 pl-5 mr-0 "> <button type="button" class="btn btn-default btn-circle" @click="backButton()">back</button><u>Casual</u> </h2>
              <div class="pt-0">
                <table-view :userId="1" >
              </table-view>
              </div>
              </div>
      </div>
<!--==================*
     CPU CHALLENG MODAL !! 
*==================-->
<div class="modal fade" id="Register2" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header bg-danger text-white">
        <h5 class="modal-title " id="exampleModalLongTitle"> CPU Challenge
        <img src="../assets/Webgraphics/poker.png" width="40" height="40"></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <h5 class="text-left pb-4">Create Game</h5>
   <form>
  <div class="form-group row">
    <label for="inputEmail3" class="col-sm-3 col-form-label">#CPU Players</label>
    <div class="col-sm-9">
     <select class="custom-select">
  <option selected>Number of Players</option>
  <option value="1">1</option>
  <option value="2">2</option>
  <option value="3">3</option>
  <option value="1">4</option>
  <option value="2">5</option>
  <option value="3">6</option>
</select>
      </div>
  </div>
  <div class="form-group row">
    <label for="inputPassword3" class="col-sm-3 col-form-label">$BuyIn</label>
    <div class="col-sm-9">
      <input type="number" class="form-control" id="inputPassword3" placeholder="BigBlind Buy In ">
    </div>
  </div>

  <div class="form-group row">
    <label for="inputPassword3" class="col-sm-3 col-form-label">$Max Balance</label>
    <div class="col-sm-9">
      <input type="pos number" class="form-control" id="inputPassword3" placeholder=" Highest Balance per Player ">
    </div>
  </div>
</form>
      </div>
      <div class="modal-footer">
        <button type="button" @click="chipChoice('CPUChallenge')"  data-dismiss="modal" class="btn btn-success ">Create Game</button>
        <button type="button" class="btn btn-info " data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>

<!--==================*
     /CPU CHALLENG MODAL !! 
*==================-->


<!--==================*
  Casual Poker Create Game MODAL !! 
*==================-->
<div class="modal fade" id="CreateCasualPoker" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header bg-info text-white">
        <h5 class="modal-title " id="exampleModalLongTitle"> Casual Poker
        <img src="../assets/Webgraphics/poker.png" width="40" height="40"></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <h5 class="text-left pb-4">Create Game</h5>
   <form>
  <div class="form-group row">
    <label for="inputEmail3" class="col-sm-3 col-form-label">#Players</label>
    <div class="col-sm-9">
     <select class="custom-select">
  <option selected>Number of Players</option>
  <option value="1">1</option>
  <option value="2">2</option>
  <option value="3">3</option>
  <option value="1">4</option>
  <option value="2">5</option>
  <option value="3">6</option>
</select> 
      </div>
  </div>  
  <div class="form-group row">
    <label for="inputEmail3" class="col-sm-3 col-form-label">#CPU Players</label>
    <div class="col-sm-9">
     <select class="custom-select">
  <option selected>Number of CPU Players</option>
  <option value="1">1</option>
  <option value="2">2</option>
  <option value="3">3</option>
  <option value="1">4</option>
  <option value="2">5</option>
  <option value="3">6</option>
</select> 
      </div>
  </div>

  <div class="form-group row">
    <label for="inputPassword3" class="col-sm-3 col-form-label">$BigBlind</label>
    <div class="col-sm-9">
      <input type="number" class="form-control" id="inputPassword3" placeholder=" ">
    </div>
  </div>

  <div class="form-group row">
    <label for="inputPassword3" class="col-sm-3 col-form-label">$Max Balance</label>
    <div class="col-sm-9">
      <input type="pos number" class="form-control" id="inputPassword3" placeholder=" Highest Balance per Player">
    </div>
  </div>
</form>
      </div>
      <div class="modal-footer">
        <button type="button" @click="chipChoice('CPUChallenge')"  data-dismiss="modal" class="btn btn-success ">Create Game</button>
        <button type="button" class="btn btn-info " data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>

<!--==================*
 / Casual Poker Create Game MODAL !! 
*==================-->

<!--==================*
  Casual Poker  Join Game MODAL !! 
*==================-->

<div class="modal fade" id="JoinCasualPoker" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header bg-info text-white">
        <h5 class="modal-title " id="exampleModalLongTitle"> Casual Poker
        <img src="../assets/Webgraphics/poker.png" width="40" height="40"></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <h5 class="text-left pb-4">Join Game</h5>
   <form>
     
<div class="list-group list-group-flush ">
  <h4 href="#" class="">
    Availble Games 
  </h4>
         <availbleGame  v-for="player in this.mechanics.multiplePlayers" :key="player.id" :gameData="player">
        </availbleGame>
</div>
</form>
      </div>
    </div>
  </div>
</div>
<!--==================*
  /Casual Poker  Join Game MODAL !! 
*==================-->

</div>
   <!-- <table-view :userId="1" ></table-view>  -->


</template>

<script>
import AvailbleGame from '@/components/AvailbleGame.vue'
import Player from '@/components/table/Player.vue'
import Card from '@/components/table/Card'
import TableActions from '@/components/table/TableActions'
import CardSuite from '@/types/cards'
import Actions from '@/types/actions'
import TableView from '@/components/TableView'
import GameMech from '@/store/GameMechanics.ts'
import router from '@/router'
import GameRequest from '@/store/GameRequest.ts'
export default {
  data () {
    return {
      gameReq: new GameRequest(),
      cardPos: 30,
      GameMenu: true,
      instrcutionPokerMode: false,
      casualPokerMode: false,
      competitivePokerMode: false,
      showTable: false,
      mechanics: new GameMech(0, this.userId),
      opponents: [{
        id: 1,
        username: 'Jasddon',
        account: 100000,
        cards: [CardSuite.HEARTS_ACE, CardSuite.SPADES_TWO],
        bet: 110

      },
      {
        id: 2,
        username: 'Lucy',
        account: 100000,
        cards: [CardSuite.HEARTS_ACE, CardSuite.SPADES_TWO],
        bet: 210
      },
      {
        id: 2,
        username: 'Javon',
        account: 1000000,
        bet: 110,
        cards: [CardSuite.HEARTS_ACE, CardSuite.SPADES_TWO],
        nextAction: Actions.NONE
      }],
      state: {
        active: 2,
        communityCards: [CardSuite.HEARTS_EIGHT, CardSuite.SPADES_THREE, CardSuite.SPADES_ACE],
        lobby: true,
        pot: 0

      },
      empty: {
        card: CardSuite.HEARTS_EIGHT
      }
    }
  },
  methods: {
    setAction (event) {
      this.user.nextAction = event
    },
    sortcards () {
      for (let i = 0; i < this.state.communityCards.length; i++) {
        this.state.communityCards[i].css.style.color = 'blue'
      }
    },
    showInstructPoker () {
      this.GameMenu = false
      this.instrcutionPokerMode = true
    },
    showCasualPoker () {
      this.GameMenu = false
      this.casualPokerMode = true
    },
    showCompetitivePoker () {
      this.GameMenu = false
      this.showTable = true
    },
    backButton () {
      this.GameMenu = true
      this.instrcutionPokerMode = false
      this.casualPokerMode = false
      this.competitivePokerMode = false
      this.showTable = false
    },
    chipChoice: function (choice) {
      if (choice === 'PokerTutorial') {
        this.$router.push('Table')
      }
      if (choice === 'CompetitivePoker') {
        alert('Lets Compete Poker')
        alert('Lets Compete Poker')
        // this.instrcutionPokerMode = false
        // this.showTable = true
        this.gameReq.createGame().then(() => {
          alert('I am pushing you to the table view')
          alert(this.gameReq.gameId)
          router.push({name: 'TableView', params: {gameId: this.gameReq.gameId}})
        }).catch(() => {
          alert('an error occured - there is no GameId returned')
        })
      }
      if (choice === 'CPUChallenge') {
        alert('Bot Plays Poker')
        this.$router.push('Table')
      }
      if (choice === 'CasualPoker') {
        alert('Casual Plays Poker')
        router.push('Table')
      }
    }
  },
  watch: {
  },
  components: {
    player: Player,
    actions: TableActions,
    card: Card,
    tableView: TableView,
    availbleGame: AvailbleGame
  }
}
</script>

  <style src="@/assets/css/GMenu.css">

</style>

