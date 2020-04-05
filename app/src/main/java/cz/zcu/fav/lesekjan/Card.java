package cz.zcu.fav.lesekjan;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatButton;

public class Card extends AppCompatButton {
    private CardType ct;
    private CardColor cc;
    //array of cards[][] position, i == y axis, j == x axis
    int i, j;

    static int width = 120;
    static int height = 150;
//    static int offset = 15;


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
}
