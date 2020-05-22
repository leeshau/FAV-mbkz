package cz.zcu.fav.lesekjan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    //for the load_score method
    static MainActivity main_activity;
    String user_name = "";
    int total_score;
    //difficulty, means number of columns
    static int diff;
    //number of colors chosen in the difficulty selection
    static int colors;
    private final int ROWS = 4;
    //two dimm.array representing the main cards in the game
    private Card[][] cards;
    //id for the two cards on the sides
    private final int FREECARD_ID = -1;
    //currently clicked (previously clicked) cards
    private Card clickedCard, PclickedCard;
    //how many columns you sorted and then packed
    private int packedCount = 0;
    //for timer
    private int time = 0;
    Timer timer = new Timer();

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

    /**create all the cards, layout etc*/
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
//        findViewById(R.id.pack_it_up_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                packItUp();
//            }
//        });

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
                timer.cancel();
                createDialog();
            }
        });
    }

    /** from createStuff()
     * sets card to appear as it should be*/
    private Card initCard(Card c) {
        if(c.i != ROWS - 1){
            Card.setCard(c);
        }
        setOnClick(c); //sets function
        c.setMaxWidth(Card.width); //sets width
        c.setMinWidth(Card.width);
        c.setMinimumWidth(Card.width);
        c.setMaxHeight(Card.height); //sets height, yes the whole segment...
        c.setMinHeight(Card.height);
        c.setMinimumHeight(Card.height);
        c.setText(c.getSymbol()); //sets the card's graphics acc.to its color/type
        return c;
    }

    /**give functionality to a card, be able to click, move, highlight while checking if you can*/
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
                            card.setText(PclickedCard.getSymbol());
                            PclickedCard.clearMe();
                            PclickedCard.setText(PclickedCard.getSymbol());
                            clickedCard.setShadowLayer(0,0,0,0);
                            PclickedCard.setShadowLayer(0,0,0,0);
                            clickedCard = null;
                            packItUp();
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
        //initial check if it is possible
        for(i = 0; i < diff; i++){
            if(cards[ROWS - 1][i].getCt() != CardType.NONE && cards[ROWS - 1][i].getCc() != CardColor.NONE){
                return;
            }
        }
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
            c = cards[empty_pos[j]][j]; //highest NONE type card
            Card.setCard(c);
            c.setText(c.getSymbol());
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
                    card.setText(card.getSymbol());
                }
                packedCount++;
            }
        }
        TextView packed_tw = findViewById(R.id.packed_count);
        String packed_str = getResources().getText(R.string.packed_count_string).toString() + packedCount;
        packed_tw.setText(packed_str);
    }

    void finishGame(boolean save){
        if(!save){
            Toast.makeText(this, "The score has not been saved.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        int i;
        SharedPreferences sp = getSharedPreferences("score_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        LinkedList<Score> scores = Score.getScores(sp);
        editor.clear();
        if(scores.size() == 0){ //first to score
            editor.putInt("0", total_score);
            editor.putString("0_name", user_name);
        } else { //already some records
            boolean added = false;
            int fixSize = scores.size();
            for (i = 0; i < fixSize && i < 5; i++) {
                if (total_score > scores.get(i).score && !added) {
                    scores.add(i, new Score(user_name, total_score));
                    added = true;
                    fixSize++;
                }
                editor.putInt("" + i, scores.get(i).score);
                editor.putString("" + i + "_name", scores.get(i).name);
            }
        }
        editor.apply();
        main_activity.load_score();
        finish();
    }

    /**dialog created after hitting the finish! button. Input name and submit or cancel.
     * counts the final score*/
    private void createDialog() {
        double temp_score = Math.pow(GameActivity.colors, 2) * packedCount * 600 / (double)ROWS / time;
        this.total_score = (int)temp_score;
        if(this.total_score == 0){
            Toast.makeText(this, "You didn't do anything. Nothing to save here...", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        final EditText input = new EditText(this);
        input.setText(getResources().getString(R.string.default_score_name));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setTitle(R.string.game_finished);
        builder.setView(input);
        builder.setMessage(getResources().getString(R.string.dialog_finish1) + " " + this.total_score + " " + getResources().getString(R.string.dialog_finish2))
                .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        user_name = input.getText().toString();
                        finishGame(true);
                    }
                })
                .setNegativeButton(R.string.dialog_decline, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishGame(false);
                    }
                });
        builder.show();
    }
}




