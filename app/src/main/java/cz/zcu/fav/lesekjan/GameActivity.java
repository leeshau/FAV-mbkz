package cz.zcu.fav.lesekjan;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    public static int diff;
    public static int colors;
    private Random r = new Random();
    private static final int COLUMNS = 4;

    private Card[][] cards;
    private final int FREECARD_ID = -1;
    private Card clickedCard, PclickedCard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        createStuff();
    }

    private void createStuff(){
        cards = new Card[COLUMNS][diff];
        GridLayout grid = findViewById(R.id.cardgrid);
        grid.setColumnCount(diff);
        grid.setRowCount(COLUMNS);
        //creating main board
        for (int i = 0; i < COLUMNS; i++){
            for (int j = 0; j < diff; j++){
                cards[i][j] = initCard(new Card(this, i, j));
                grid.addView(cards[i][j]);
            }
        }
        //creating two free cards
        LinearLayout left = findViewById(R.id.freecard_left);
        left.addView(initCard(new Card(this, FREECARD_ID, FREECARD_ID)));
        LinearLayout right = findViewById(R.id.freecard_right);
        right.addView(initCard(new Card(this, FREECARD_ID, FREECARD_ID)));


    }

    /** from createStuff()
     * sets card to appear as it should be*/
    private Card initCard(Card c) {
        if(c.i != COLUMNS - 1){
            setCard(c);
        }
        setOnClick(c); //sets function
        c.setMaxWidth(Card.width); //sets width
        c.setMinWidth(Card.width);
        c.setMinimumWidth(Card.width);
        c.setMaxHeight(Card.height); //sets height, yes the whole segment...
        c.setMinHeight(Card.height);
        c.setMinimumHeight(Card.height);
        c.setText(getSymbol(c)); //sets the card's graphics acc.to its color/type
        return c;
    }

    private void setOnClick(final Card card) {
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                card.setText("" + card.i + "|"+card.j);
                Card above = null, bellow = null;
                //controlling if we are touching free card
                if(card.i != FREECARD_ID && card.j != FREECARD_ID) {
                    if (card.i != 0) above = cards[card.i - 1][card.j];
                    if (card.i != COLUMNS - 1) bellow = cards[card.i + 1][card.j];
                }
                if(card.getCc()==CardColor.NONE && card.getCt()==CardType.NONE && clickedCard!=null) {
                    if((above==null || PclickedCard.getCc() == above.getCc()) &&(bellow == null || bellow.getCc()==CardColor.NONE) ) {
                        if(above==null || clickedCard.getCt()==above.getCtBellow()) {
                            PclickedCard = clickedCard;
                            clickedCard = card;
                            card.setCc(PclickedCard.getCc());
                            card.setCt(PclickedCard.getCt());
                            card.setText(getSymbol(PclickedCard));
                            PclickedCard.clearMe();
                            PclickedCard.setText(getSymbol(PclickedCard));

//                            clickedCard.setEffect(getShadow(offsetNorm, colorNorm));
//                            PclickedCard.setEffect(getShadow(offsetNorm, colorNorm));
//                            clickedCard.setBackground(PclickedCard.getBackground());
//                            PclickedCard.setBackground(defBg);
                            clickedCard = null;
                        }
                    }
                }

                //oznacovani karet
                else if(card.getCc()!=CardColor.NONE && card.getCt()!=CardType.NONE
                        && (bellow==null || (bellow.getCc()==CardColor.NONE && bellow.getCt()==CardType.NONE) )) {
                    if(card != null) {
//                        if(clickedCard != null)
//                            clickedCard.setEffect(getShadow(offsetNorm, colorNorm));
                        PclickedCard = clickedCard;
                        clickedCard = card;
                        PclickedCard = clickedCard;
//                        clickedCard.setEffect(getShadow(offsetTogl, colorTogl));
                    }
                }
            }
        });
    }

    public void click_freecard(){

    }

    /**randomly sets card its color and type*/
    private void setCard(Card c) {
        int num = r.nextInt(4);
        switch(num % 4){
            case 3: c.setCt(CardType.A); break;
            case 2: c.setCt(CardType.K); break;
            case 1: c.setCt(CardType.Q); break;
            default: c.setCt(CardType.J);
        }
        switch(num % colors) {
            case 3: c.setCc(CardColor.HEART); break;
            case 2: c.setCc(CardColor.CLUB); break;
            case 1: c.setCc(CardColor.DIAMOND); break;
            default: c.setCc(CardColor.SPADE);
        }
    }
    /**
     * serves to write an symbol on the card depending on its card type and color
     * @param card current card
     * @return string that will be written on the cards face
     */
    private String getSymbol(Card card) {
        char type;
        switch(card.getCt()) {
            case J: type = 'J';break;
            case Q: type = 'Q';break;
            case K: type = 'K';break;
            case A: type = 'A';break;
            default: type = ' ';
        }
        String color;
        switch(card.getCc()) {
            case SPADE: color = "\u2660";break;
            case CLUB: color = "\u2663";break;
            case DIAMOND: color = "\u2662";break;
            case HEART: color = "\u2661";break;
            default: color = "°";
        }
        return type+"\n"+color;
    }

}
