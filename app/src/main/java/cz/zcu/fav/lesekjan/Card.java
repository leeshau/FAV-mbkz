package cz.zcu.fav.lesekjan;

import android.content.Context;
import androidx.appcompat.widget.AppCompatButton;

import java.util.Random;

public class Card extends AppCompatButton {
    private CardType ct;
    private CardColor cc;
    int i, j;

    static int width = 120;
    static int height = 150;

    /**
     * main constructor
     * @param ct card type
     * @param cc card color
     */
    public Card(Context context, CardType ct, CardColor cc, int i, int j) {
        super(context);
        this.ct = ct;
        this.cc = cc;
        this.i = i;
        this.j = j;
    }
    /**
     * simpler constructor to make an empty card
     */
    public Card(Context context, int i, int j) {
        this(context, CardType.NONE, CardColor.NONE, i, j);
    }
    /**
     * method that makes the card empty, just for convenience this method exists
     */
    public void clearMe() {
        ct = CardType.NONE;
        cc = CardColor.NONE;
    }
    /**
     * method to determine the type of card bellow, to check if moving the card is available
     * @return cz.zcu.fav.lesekjan.CardType one rank lower
     */
    public CardType getCtBellow() {
        switch(this.getCt()) {
            case A: return CardType.K;
            case K: return CardType.Q;
            case Q: return CardType.J;
            default: return CardType.NONE;
        }
    }

    /**
     * Card c should be above this card*/
    public boolean isBellow(Card c){
        return (c.i == (this.i - 1) && c.j == this.j);
    }


    public CardType getCt() {
        return ct;
    }

    public CardColor getCc() {
        return cc;
    }
    public void setCt(CardType ct) {
        this.ct = ct;
    }

    public void setCc(CardColor cc) {
        this.cc = cc;
    }
    /**
     * serves as a testing method
     */
    @Override
    public String toString() {
        return ""+ this.getCt() +", "+this.getCc()+", x: "+this.i+", y: "+this.j;
    }

    /**
     * serves to write an symbol on the card depending on its card type and color
     * @return string that will be written on the cards face
     */
    String getSymbol() {
        char type;
        switch(this.getCt()) {
            case J: type = 'J';break;
            case Q: type = 'Q';break;
            case K: type = 'K';break;
            case A: type = 'A';break;
            default: type = ' ';
        }
        String color;
        switch(this.getCc()) {
            case SPADE: color = "\u2660";break;
            case CLUB: color = "\u2663";break;
            case DIAMOND: color = "\u2662";break;
            case HEART: color = "\u2661";break;
            default: color = "°";
        }
        return type+"\n"+color;
    }

    /**randomly sets card its color and type*/
    static void setCard(Card c) {
        Random r = new Random();
        int num = r.nextInt(4);
        switch(num % 4){
            case 3: c.setCt(CardType.A); break;
            case 2: c.setCt(CardType.K); break;
            case 1: c.setCt(CardType.Q); break;
            default: c.setCt(CardType.J);
        }
        num = r.nextInt(4);
        switch(num % GameActivity.colors) {
            case 3: c.setCc(CardColor.HEART); break;
            case 2: c.setCc(CardColor.CLUB); break;
            case 1: c.setCc(CardColor.DIAMOND); break;
            default: c.setCc(CardColor.SPADE);
        }
    }
}
