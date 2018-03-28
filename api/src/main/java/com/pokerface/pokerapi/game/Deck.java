package com.pokerface.pokerapi.game;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.*;

/**
 * Deck is an object belonging 1-1 to a gamestate, it is the deck of cards used to play. It begins with a full set of
 * the cards, held from the enum in a stack implementation. It has methods to pop cards and shuffle itself.
 */
@Entity
@Table(name = "deck")
public class Deck {
    int ten=0;


    /**
     * cards is the deck of cards in a Deck object. It is a stack, of Card type.
     */
    @Enumerated
    private List<Card> cards;
    /**
     * id is the identifier of the deck, used as a primary key to identify which game it belongs to.
     */

    @ElementCollection

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    private long id;
    /**
     * this is a reference object to the game it belongs to
     */
    private GameState gameState;

    /**
     * This is the default constructor, when created it generates a new randomly shuffled deck by randomly pulling
     * cards that have not yet been pulled until 52 cards have been chosen in a random order
     */
    public Deck(){
    }

    public Deck(GameState gameState){
        int cards=0;
        this.cards=new ArrayList<>();
        this.gameState=gameState;
        boolean[] taken = new boolean[52];
        Random rand = new Random(System.currentTimeMillis());
        while (cards!=52){
            int cardNumber = rand.nextInt(52);
            if (taken[cardNumber]==false){
                taken[cardNumber]=true;
                Card card = Card.values()[cardNumber];
                this.cards.add(card);
                cards++;
            }
        }
    }

    /**
     * Shuffle deck is used to restore the deck to its created state, 52 cards in the stack randomly chosen
     */
    public void shuffleDeck(){
        int cards=0;
        boolean[] taken = new boolean[52];
        this.cards=new Stack<Card>();
        Random rand = new Random(System.currentTimeMillis());
        while (cards!=52){
            int cardNumber = rand.nextInt(52);
            if (taken[cardNumber]==false){
                taken[cardNumber]=true;
                Card card = Card.values()[cardNumber];
                this.cards.add(card);
                cards++;
            }
        }
    }

    /**
     * getID returns to the ID of the deck
     * @return a long representing the ID of the deck
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    /**
     * setId adds the ID parameter to the deck
     * @param id of the deck
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * A one to one relationship, each deck belongs to a GameState, and each GameState belongs to a Deck.
     * @return GameState the deck belongs to.
     */
    @OneToOne
    @JoinColumn(name="gameState")
    public GameState getGameState() {
        return gameState;
    }

    /**
     * getCard is used to return a single card, used in draws
     * @return a Card object from the 'top' of the deck
     */
    public Card dealCard() {
        return cards.remove(cards.size()-1);
    }

    /**
     * setGameState sets what game the deck belongs to, likely unused.
     * @param gameState the gamestate being set
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getTen() {
        return ten;
    }

    public void setTen(int ten) {
        this.ten = ten;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        return getTen() == deck.getTen() &&
                getId() == deck.getId() &&
                Objects.equals(getCards(), deck.getCards());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getTen(), getCards(), getId());
    }
}
