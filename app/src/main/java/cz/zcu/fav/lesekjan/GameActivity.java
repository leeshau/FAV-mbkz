package cz.zcu.fav.lesekjan;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    //difficulty, means number of columns
    public static int diff;
    //number of colors chosen in the difficulty selection
    public static int colors;
    //used in generating random cards
    private Random r = new Random();
    private static final int ROWS = 4;
    //two dimm.array representing the main cards in the game
    private Card[][] cards;
    //id for the two cards on the sides
    private final int FREECARD_ID = -1;
    //currently clicked (previously clicked) cards
    private Card clickedCard, PclickedCard;
    //how many columns you sorted and then packed
    private int packedCount = 0;
    //for timer
//    private Thread t;
    private int time = 0;
    Timer timer = new Timer();
//    private boolean end_time = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        createStuff();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                time++;
                String timer = getString(R.string.time_string) + (time / 60) + ":" + (time % 60 < 10 ? "0" : "") + (time % 60);
                ((TextView)findViewById(R.id.timer)).setText(timer);
            }
        }, 0, 1000);
    }

    private void createStuff(){
        cards = new Card[ROWS][diff];
        GridLayout grid = findViewById(R.id.cardgrid);
        grid.setColumnCount(diff);
        grid.setRowCount(ROWS);
        //creating main board
        for (int i = 0; i < ROWS; i++){
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

        //creating pack it up card
        findViewById(R.id.pack_it_up_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packItUp();
            }
        });

        //creating give a set card
        findViewById(R.id.give_a_set_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveSet();
            }
        });

        //creating finish button
        findViewById(R.id.finish_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishGame();
            }
        });
    }

    /** from createStuff()
     * sets card to appear as it should be*/
    private Card initCard(Card c) {
        if(c.i != ROWS - 1){
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
                Card above = null, bellow = null;
                //controlling if we are touching free card
                if(card.i != FREECARD_ID && card.j != FREECARD_ID) {
                    if (card.i != 0) above = cards[card.i - 1][card.j];
                    if (card.i != ROWS - 1) bellow = cards[card.i + 1][card.j];
                }
                //moving cards
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
                            clickedCard.setShadowLayer(0,0,0,0);
                            PclickedCard.setShadowLayer(0,0,0,0);
                            clickedCard = null;
                        }
                    }
                }
                //highlighting cards
                else if(card.getCc()!=CardColor.NONE && card.getCt()!=CardType.NONE
                        && (bellow==null || (bellow.getCc()==CardColor.NONE && bellow.getCt()==CardType.NONE) )) {
                        if(PclickedCard != null){
                            PclickedCard.setShadowLayer(0,0,0,0);
                        }
                        PclickedCard = clickedCard;
                        clickedCard = card;
                        PclickedCard = clickedCard;
                        clickedCard.setShadowLayer(24,4,4, Color.GREEN);
                }
            }
        });
    }

    /**checks if in every column is at least one empty card, if is so, gives a random card with the chosen color in each of the columns*/
    private void giveSet() {
        int i, j;
        Card c = null;
        int[] empty_pos = new int[diff]; //each cell represents a column and its value represents exact row where the first empty card is located in the column
        Arrays.fill(empty_pos, -1);
        for(j = 0; j < diff; j++){  //iterate columns from left
            for(i = ROWS - 1; i >= 0; i--){ //iterate rows from bottom, stop when not NONE card
                c = cards[i][j];
                if(c.getCt() != CardType.NONE && c.getCc() != CardColor.NONE){
                    break; //found first card from bottom that is not NONE type
                }
                empty_pos[j] = i;
            }
            if(empty_pos[j] == -1){
                return;
            }
            c = cards[empty_pos[j]][j]; //highest NONE type card
            setCard(c);
            c.setText(getSymbol(c));
        }
        if(clickedCard != null) {
            clickedCard.setShadowLayer(0, 0, 0, 0);
            clickedCard = null;
        }
    }

    /**checks if any column is sorted right and can be packed up, if so, then it packs it up*/
    private void packItUp() {
        boolean goodToGo;
        int i;
        for(int j = 0; j < diff; j++) {
            Card card = cards[0][j];
            goodToGo = true;
            if(card.getCt()!=CardType.A) continue;
            CardColor CC = card.getCc(); //the color of the top card, other need to be the same
            for(i = 1; i < ROWS; i++) { //A is allright at this point, start with K
                card = cards[i][j];
                if(card.getCc()!=CC) { //checking color of the card
                    goodToGo=false;
                    break;
                }
                switch(i) { //checking type of the card
                    case 1: if(card.getCt()!=CardType.K) {goodToGo=false;;}break;
                    case 2: if(card.getCt()!=CardType.Q) {goodToGo=false;;}break;
                    case 3: if(card.getCt()!=CardType.J) {goodToGo=false;;}break;
                }
            }
            if(goodToGo) {
                for(i = 0; i < ROWS; i++) {
                    card = cards[i][j];
                    card.setCc(CardColor.NONE);
                    card.setCt(CardType.NONE);
                    card.setText(getSymbol(card));
                }
                packedCount++;
            }
        }
        TextView packed_tw = findViewById(R.id.packed_count);
        String packed_str = getResources().getText(R.string.packed_count_string).toString();
        packed_tw.setText(packed_str + packedCount);
    }

    private void finishGame(){
//        this.end_time = true;
        this.timer.cancel();
        Log.i("finish", "finishing!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11");
        Toast.makeText(this, "Time is " + this.time, Toast.LENGTH_LONG).show();
//        setContentView(R.layout.activity_main);
        finish();
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
