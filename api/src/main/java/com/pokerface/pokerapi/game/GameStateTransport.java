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
    private Card communityCardOne, communityCardTwo, communityCardThree, communityCardFour, communityCardFive;
    int potSum;
    int bigBlind;
    int nextPlayer;
    Event event;
    PlayerTransport[] players;

    public GameStateTransport() {

    }

    /**
     * This creates a GameStateTransport for full use.
     *
     * @param communityCardOne   Each of community cards are identical, representing a card in the community
     * @param communityCardTwo card two
     * @param communityCardThree card three
     * @param communityCardFour card four, in round 2
     * @param communityCardFive card five, in round 3
     * @param potSum the sum of the pot
     * @param bigBlind the amount the bigBlind currently is
     * @param action the reason the action is being sent
     * @param event the occurence that made the object be sent
     * @param players the players in the game, a class for transport
     * @param gameActions the actions performed
     * @param nextPlayer whose turn it is now
     */
    public GameStateTransport(Card communityCardOne, Card communityCardTwo, Card communityCardThree, Card communityCardFour, Card communityCardFive, int potSum, int bigBlind, Reason action, String event, List<Player> players, List<GameAction> gameActions, int nextPlayer) {
        this.communityCardOne = communityCardOne;
        this.communityCardTwo = communityCardTwo;
        this.communityCardThree = communityCardThree;
        this.communityCardFour = communityCardFour;
        this.communityCardFive = communityCardFive;

        this.potSum = potSum;
        this.bigBlind = bigBlind;
        this.event = new Event(action, event);
        this.players = new PlayerTransport[players.size()];
        for (int i = 0; i < players.size(); i++) {
            this.players[i] = new PlayerTransport(i, players.get(i).getCashOnHand(), gameActions.get(i), players.get(i).getIsDealer(), players.get(i).getHasFolded());
        }
        this.nextPlayer = nextPlayer;
    }

    /**
     * nextPlayer is the integer of who is next
     * @return the int of who is next
     */
    public int nextPlayer() {
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

    /**
     * A class that contains an action, why the event is being sent, and an optional message for extra communication if necessary
     */
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

    /**
     * A Player representation, containing an id, their seat, the money they have, the action they last performed, if they are a player or if they are a dealer.
     */
    private class PlayerTransport {
        int id;
        int money;
        GameAction action;
        boolean isPlayer;
        boolean isDealer;

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
