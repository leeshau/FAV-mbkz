package cz.zcu.fav.lesekjan;

import android.graphics.Rect;

public class Card {

    Rect cardBT;
    private CardType ct;
    private CardColor cc;
    //upper-left corner
    int x, y;

    static int width = 100;
    static int height = 120;
    static int offset = 5;


    /**
     * simple constructor
     * @param ct card type
     * @param cc card color
     */
    public Card(CardType ct, CardColor cc, int x, int y) {
        cardBT = new Rect(x, y, x + width, y + height);
        this.ct = ct;
        this.cc = cc;
    }
    /**
     * simpler constructor to make an empty card
     */
    public Card() {
        this(CardType.NONE, CardColor.NONE, 0, 0);
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
        return ""+ this.getCt() +", "+this.getCc()+", x: "+this.x+", y: "+this.y;
    }
}
