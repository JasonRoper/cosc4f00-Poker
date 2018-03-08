package com.pokerface.pokerapi.game;

import javax.persistence.*;
import java.util.Random;
import java.util.Stack;

@Entity
@Table(name = "deck")
/**
 * Deck is an object belonging 1-1 to a gamestate, it is the deck of cards used to play. It begins with a full set of the cards, held from the enum in a stack implementation. It has methods to pop cards and shuffle itself.
 */
public class Deck {
    @ElementCollection(targetClass = Card.class)
    @JoinTable(name = "deck", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "cards", nullable = false)
    @Enumerated
    /**
     * cards is the deck of cards in a Deck object. It is a stack, of Card type.
     */
    private Stack<Card> cards = new Stack<Card>();
    /**
     * id is the identifier of the deck, used as a primary key to identify which game it belongs to.
     */
    private long id;
    /**
     * this is a reference object to the game it belongs to
     */
    private GameState gameState;

    /**
     * This is the default constructor, when created it generates a new randomly shuffled deck by randomly pulling cards that have not yet been pulled until 52 cards have been chosen in a random order
     */
    public Deck(){
        int cards=0;
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
     * @return
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    /**
     * setId adds the ID parameter to the deck
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * A one to one relationship, each deck belongs to a GameState, and each GameState belongs to a Deck.
     * @return GameState the deck belongs to.
     */
    @OneToOne(mappedBy = "deck")
    public GameState getGameState() {
        return gameState;
    }

    /**
     * getCard is used to return a single card, used in draws
     * @return a Card object from the 'top' of the deck
     */
    public Card getCard() {
        return cards.pop();
    }

    /**
     * setCard return a card to the deck, likely unused.
     * @param card
     */
    public void setCard(Card card) {
        this.cards.push(card);
    }

    /**
     * setGameState sets what game the deck belongs to, likely unused.
     * @param gameState
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
