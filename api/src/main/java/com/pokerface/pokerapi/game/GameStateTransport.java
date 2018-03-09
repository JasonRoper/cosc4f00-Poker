package com.pokerface.pokerapi.game;
///NOT AT ALL DONE
public class GameStateTransport {
        Card[] communityCards;
        int potSum;
        int bigBlind;
        Event event;
        PlayerTransport[] players;

        public GameStateTransport(){

        }

        public GameStateTransport(Card[] communityCards, int potSum, int bigBlind, String action, String event,Player[] players){
            this.communityCards=communityCards;
            this.potSum=potSum;
            this.bigBlind=bigBlind;
            this.event=new Event(action,event);
            this.players = new PlayerTransport[players.length];
            for (int i=0;i<players.length;i++){
                this.players[i]=new PlayerTransport(i,players[i].getCashOnHand(),null,players[i].getIsDealer(),players[i].getHasFolded());
            }
        }



    private class Event {
        String action;
        String message;

        public Event(){

        }

        public Event(String action, String message){
            this.action=action;
            this.message=message;
        }
    }

    private class PlayerTransport{
        int id;
        int money;
        GameAction action;
        boolean isPlayer;
        boolean isDealer;

        public PlayerTransport(){

        }
        public PlayerTransport(int id, int money, GameAction action,boolean isPlayer, boolean isDealer){
            this.id=id;
            this.money=money;
            this.action=action;
            this.isPlayer=isPlayer;
            this.isDealer=isDealer;
        }
    }
}
