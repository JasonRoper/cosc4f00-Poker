package com.pokerface.pokerapi.game;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
    private PlayerTransport[] multiplePlayers;

    public GameStateTransport() {

    }


    public GameStateTransport(GameState gameState){
        this.communityCards=gameState.receiveCommunityCards();

        this.potSum = gameState.getPot().getSum();
        this.bigBlind = gameState.getBigBlind();
        this.multiplePlayers = new PlayerTransport[gameState.getPlayerCount()];

        for (int i = 0; i < this.multiplePlayers.length; i++) {
            Player player = gameState.getPlayers().get(i);
            this.multiplePlayers[i] = new PlayerTransport(i, player.getCashOnHand(), player.getLastGameAction(), player.isAI(), player.getIsDealer(), player.getHasFolded(),player.getBet(),player.getName());
        }
        this.nextPlayer = gameState.getPresentTurn();
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
        this.event = new Event(reason, message);
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
        return multiplePlayers;
    }

    public void setPlayers(PlayerTransport[] players) {
        this.multiplePlayers = players;
    }

    /**
     * A class that contains an action, why the event is being sent, and an optional message for extra communication if necessary
     */
    public static class Event {
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
     * {@link GameStateTransport.Reason} are all of the reasons that the {@link GameStateTransport} can be
     * sent for.
     */
    public enum Reason {
        GAME_STARTED,
        HAND_STARTED,
        PLAYER_ACTION,
        ROUND_FINSHED,
        HAND_FINISHED,
        PLAYER_JOINED,
        PLAYER_LEFT,
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameStateTransport that = (GameStateTransport) o;
        return getPotSum() == that.getPotSum() &&
                getBigBlind() == that.getBigBlind() &&
                getNextPlayer() == that.getNextPlayer() &&
                Objects.equals(getCommunityCards(), that.getCommunityCards()) &&
                Objects.equals(getEvent(), that.getEvent()) &&
                Arrays.equals(multiplePlayers, that.multiplePlayers);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(getCommunityCards(), getPotSum(), getBigBlind(), getNextPlayer(), getEvent());
        result = 31 * result + Arrays.hashCode(multiplePlayers);
        return result;
    }
}
