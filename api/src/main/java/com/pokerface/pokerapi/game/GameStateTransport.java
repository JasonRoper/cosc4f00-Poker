package com.pokerface.pokerapi.game;

import java.util.List;

///NOT AT ALL DONE
public class GameStateTransport {
    private Card communityCardOne,communityCardTwo,communityCardThree,communityCardFour,communityCardFive;
        int potSum;
        int bigBlind;
        int nextPlayer;
        Event event;
        PlayerTransport[] players;

    public GameStateTransport() {

    }

        public GameStateTransport(Card communityCardOne,Card communityCardTwo,Card communityCardThree,Card communityCardFour,Card communityCardFive, int potSum, int bigBlind, Reason action, String event,List<Player> players,List<GameAction> gameActions,int nextPlayer){
            communityCardOne=communityCardOne;
            communityCardTwo=communityCardTwo;
                    communityCardThree=communityCardThree;
                    communityCardFour=communityCardFour;
                    communityCardFive=communityCardFive;

            this.potSum=potSum;
            this.bigBlind=bigBlind;
            this.event=new Event(action, event);
            this.players = new PlayerTransport[players.size()];
            for (int i=0;i<players.size();i++){
                this.players[i]=new PlayerTransport(i,players.get(i).getCashOnHand(),gameActions.get(i),players.get(i).getIsDealer(),players.get(i).getHasFolded());
            }
            this.nextPlayer = nextPlayer;
        }

    public int nextPlayer() {
        return nextPlayer;
    }

    public GameStateTransport reason(Reason reason, String message) {
        this.event = event;
        return this;
    }


    private class Event {
        Reason action;
        String message;

        public Event() {

        }

        public Event(Reason action, String message) {
            this.action = action;
            this.message = message;
        }
    }

    private class PlayerTransport {
        int id;
        int money;
        GameAction action;
        boolean isPlayer;
        boolean isDealer;

        public PlayerTransport() {

        }

        public PlayerTransport(int id, int money, GameAction action, boolean isPlayer, boolean isDealer) {
            this.id = id;
            this.money = money;
            this.action = action;
            this.isPlayer = isPlayer;
            this.isDealer = isDealer;
        }
    }

    public enum Reason {
        HAND_STARTED,
        PLAYER_ACTION,
        ROUND_FINSHED,
        HAND_FINISHED,
        NEW_PLAYER,
        PLAYER_LEFT,
    }
}
