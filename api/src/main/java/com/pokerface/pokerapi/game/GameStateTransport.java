package com.pokerface.pokerapi.game;

import java.util.List;

///NOT AT ALL DONE
public class GameStateTransport {
        Card[] communityCards;
        int potSum;
        int bigBlind;
        int nextPlayer;
        Event event;
        PlayerTransport[] players;

    public GameStateTransport() {

    }

        public GameStateTransport(Card[] communityCards, int potSum, int bigBlind, Reason action, String event,Player[] players,List<GameAction> gameActions,int nextPlayer){
            this.communityCards=communityCards;
            this.potSum=potSum;
            this.bigBlind=bigBlind;
            this.event=new Event(action, event);
            this.players = new PlayerTransport[players.length];
            for (int i=0;i<players.length;i++){
                this.players[i]=new PlayerTransport(i,players[i].getCashOnHand(),gameActions.get(i),players[i].getIsDealer(),players[i].getHasFolded());
            }
            this.nextPlayer = nextPlayer;
        }

    public int nextPlayer() {
        return nextPlayer;
    }

    public GameStateTransport reason(Reason reason, String message) {
        this.event = event;
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
