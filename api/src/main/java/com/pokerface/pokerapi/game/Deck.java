package com.pokerface.pokerapi.game;

import javax.persistence.*;
import java.util.Random;
import java.util.Stack;

@Entity
@Table(name = "deck")
/**
 * Deck is an object belonging 1-1 to a gamestate, it is the deck of cards used to play. It begins with a full set of the cards, held from the enum in a stack implementation. It has methods to pop cards.
 */
public class Deck {
    @ElementCollection(targetClass = Card.class)
    @JoinTable(name = "deck", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "cards", nullable = false)
    @Enumerated
    private Stack<Card> cards = new Stack<Card>();
    int position = -1;
    private Integer id;
    private GameState gameState;

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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @OneToOne(mappedBy = "deck")
    public GameState getGameState() {
        return gameState;
    }

    public Card getCard() {
        return cards.pop();
    }

    public void setCard(Card card) {
        this.cards.push(card);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
