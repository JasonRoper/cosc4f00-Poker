package com.pokerface.pokerapi.game;

import java.util.List;

/**
 * GameStateTransport is the object that is communicated by the GameController to the Front End user, it represents
 * the GameState as they see it, with only public information. It also has an Event object which confers
 * why the GameState is being sent.
 * <p>
 * The GameStateTransport is submitted upon every action that is sent and processed, whenever a hand is started
 * or finished and whenever a player leaves or enters.
 */
public class GameStateTransport {
    private List<Card> communityCards;
    private int potSum;
    private int bigBlind;
    private int nextPlayer;
    private Event event;
    private PlayerTransport[] players;

    public GameStateTransport() {

    }


    public GameStateTransport(GameState gameState){
        this.communityCards=gameState.receiveCommunityCards();

        this.potSum = gameState.getPot().getSum();
        this.bigBlind = gameState.getBigBlind();
        this.players = new PlayerTransport[gameState.getPlayers().size()];
        for (int i = 0; i < this.players.length; i++) {
            this.players[i] = new PlayerTransport(i, gameState.getPlayers().get(i).getCashOnHand(), gameState.getLastGameActions().get(i), gameState.getPlayers().get(i).getIsDealer(), gameState.getPlayers().get(i).getHasFolded());
        }
        this.nextPlayer = nextPlayer;
    }


    /**
     * getNextPlayer is the integer of who is next
     * @return the int of who is next
     */
    public int getNextPlayer() {
        return nextPlayer;
    }

    /**
     * Allows to append a reason after the fact
     * @param reason a Reason object
     * @param message a String message
     * @return the gameStateTransport
     */
    public GameStateTransport reason(Reason reason, String message) {
        this.event = event;
        return this;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public void setCommunityCards(List<Card> communityCards) {
        this.communityCards = communityCards;
    }

    public int getPotSum() {
        return potSum;
    }

    public void setPotSum(int potSum) {
        this.potSum = potSum;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }

    public void setNextPlayer(int nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public PlayerTransport[] getPlayers() {
        return players;
    }

    public void setPlayers(PlayerTransport[] players) {
        this.players = players;
    }

    /**
     * A class that contains an action, why the event is being sent, and an optional message for extra communication if necessary
     */
    private class Event {
        private Reason action;
        private String message;

        public Event() {

        }

        public Event(Reason action, String message) {
            this.action = action;
            this.message = message;
        }

        public Reason getAction() {
            return action;
        }

        public void setAction(Reason action) {
            this.action = action;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    /**
     * A Player representation, containing an id, their seat, the money they have, the action they last performed, if they are a player or if they are a dealer.
     */
    public static class PlayerTransport {
        private int id;
        private int money;
        private GameAction action;
        private boolean isPlayer;
        private boolean isDealer;

        public PlayerTransport() {

        }

        /**
         * Construct a {@link PlayerTransport} with the given data
         * @param id the player id of this player
         * @param money the amount of money this player has
         * @param action the last action that this player took
         * @param isPlayer whether or not the player is a player or an AI
         * @param isDealer whether or not this player is the dealer
         */
        public PlayerTransport(int id, int money, GameAction action, boolean isPlayer, boolean isDealer) {
            this.id = id;
            this.money = money;
            this.action = action;
            this.isPlayer = isPlayer;
            this.isDealer = isDealer;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public GameAction getAction() {
            return action;
        }

        public void setAction(GameAction action) {
            this.action = action;
        }

        public boolean isPlayer() {
            return isPlayer;
        }

        public void setPlayer(boolean player) {
            isPlayer = player;
        }

        public boolean isDealer() {
            return isDealer;
        }

        public void setDealer(boolean dealer) {
            isDealer = dealer;
        }
    }

    /**
     * {@link GameStateTransport.Reason} are all of the reasons that the {@link GameStateTransport} can be
     * sent for.
     */
    public enum Reason {
        HAND_STARTED,
        PLAYER_ACTION,
        ROUND_FINSHED,
        HAND_FINISHED,
        NEW_PLAYER,
        PLAYER_LEFT,
    }
}
