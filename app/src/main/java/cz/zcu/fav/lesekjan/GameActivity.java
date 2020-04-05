package cz.zcu.fav.lesekjan;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
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
    private static final String TAG = "Svetlin SurfaceView";
    private static final int COLUMNS = 4;

    private Card[][] cards;

    private Card clickedCard, PclickedCard;


//    @Override
//    public boolean onTouchEvent(MotionEvent event){
//        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            Log.i("touch", "pressed");
//
//            float x_point = event.getX();
//            float y_point = event.getY();
//
//            Rect r;
//            for (int i = 0; i < COLUMNS; i++){
//                for (int j = 0; j < diff; j++) {
//                    if(cards[i][j] == null){
//                        continue;
//                    }
//                    r = new Rect(cards[i][j].x, cards[i][j].y, cards[i][j].x + Card.width, cards[i][j].y + Card.height);
//                    if (r.contains((int) x_point, (int) y_point)) {
//                        Log.i("colision", "touched card:" + cards[i][j].toString());
//                        return true;
//                    }
//                }
//            }
//            Log.i("point", "point: x: "+x_point+", y: "+y_point);
//
//        } else if(event.getAction() == MotionEvent.ACTION_UP) {
//            Log.i("touch", "released");
//        }
//        return true;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        createStuff();
//        SurfaceView view = new SurfaceView(this);
//        setContentView(view);
//        view.getHolder().addCallback(this);
//        System.out.println("creating");
//        diff += 2; //two void helping cards
//        createStuff();
    }

    private void createStuff(){
        cards = new Card[COLUMNS][diff];
//        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
//        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

//        int x_ratio = screenWidth / 1344; //original width
//        int y_ratio = screenHeight / 720; //original height

//        /*setting graphics for every display*/
//        Card.offset *= x_ratio;
//        Card.width *= x_ratio;
//        Card.height *= y_ratio;

//        /*space between left edge of display and the first card*/
//        int offset_x = (screenWidth - (Card.width + Card.offset) * diff) / 2;

        int card_num = 0;
        GridLayout grid = findViewById(R.id.cardgrid);
        grid.setColumnCount(diff);
        grid.setRowCount(COLUMNS);
        for (int i = 0; i < COLUMNS; i++){
//            if(i == 0 || i == diff - 1) {
//                cards[i][0] = new Card(this);
//                cards[i][0].x = offset_x + i * Card.width + Card.offset * i;
//                cards[i][0].y = 0;
//                cards[i][0].setText("number:"+card_num++);
//                setOnClick(cards[i][0], i, 0);
//                grid.addView(cards[i][0]);
//                continue;
//            }
            for (int j = 0; j < diff; j++){
                cards[i][j] = new Card(this, i, j);
//                cards[i][j].x = offset_x + i * Card.width + Card.offset * i;
//                cards[i][j].y = j * Card.height + Card.offset * j;
                if(i != COLUMNS - 1){
                    setCard(cards[i][j]);
                }
//                cards[i][j].setText("number:"+card_num++);
                cards[i][j].setText("");
                setOnClick(cards[i][j]);

                cards[i][j].setMaxWidth(Card.width);
                cards[i][j].setMinWidth(Card.width);
                cards[i][j].setMinimumWidth(Card.width);

                cards[i][j].setMaxHeight(Card.height);
                cards[i][j].setMinHeight(Card.height);
                cards[i][j].setMinimumHeight(Card.height);
                cards[i][j].setText(getSymbol(cards[i][j]));
//                cards[i][j].setLayoutParams(new GridLayout.LayoutParams(new ViewGroup.LayoutParams(Card.width, Card.height)));
                grid.addView(cards[i][j]);
            }
        }

    }

    private void setOnClick(final Card card) {
//        Log.e("click", "clicked");
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                card.setText("" + card.i + "|"+card.j);
                Card above = null, bellow = null;
                if(card.i != 0) above = cards[card.i - 1][card.j];
                if(card.i != COLUMNS-1) bellow = cards[card.i + 1][card.j];
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
        int num = r.nextInt();
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


//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        drawStage(holder);
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int frmt, int w, int h) {
//        drawStage(holder);
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        cards = null;
//    }
//
//    private void drawStage(SurfaceHolder holder) {
//        Log.i(TAG, "Trying to draw...");
//
//        Canvas canvas = holder.lockCanvas();
//        if (canvas == null) {
//            Log.e(TAG, "Cannot draw onto the canvas as it's null");
//            return;
//        }
//        Log.i(TAG, "Drawing...");
//        canvas.drawRGB(255, 128, 0);
//
//        for (int i = 0; i < diff; i++) {
//            for (int j = 0; j < COLUMNS; j++) {
//                if(cards[i][j] == null){
//                    continue;
//                }
//                canvas.drawBitmap(getCardBitmap(cards[i][j]), cards[i][j].x, cards[i][j].y, null);
//            }
//        }
//        holder.unlockCanvasAndPost(canvas);
//    }

//    private Bitmap getCardBitmap(Card c) {
//        int id = R.drawable.gray_back;
//        switch (c.getCc()){
//            case SPADE:
//                switch (c.getCt()){
//                    case A: id = R.drawable.as; break;
//                    case K: id = R.drawable.ks; break;
//                    case Q: id = R.drawable.qs; break;
//                    case J: id = R.drawable.js; break;
//                }
//                break;
//            case DIAMOND:
//                switch (c.getCt()){
//                    case A: id = R.drawable.ad; break;
//                    case K: id = R.drawable.kd; break;
//                    case Q: id = R.drawable.qd; break;
//                    case J: id = R.drawable.jd; break;
//                }
//                break;
//            case CLUB:
//                switch (c.getCt()){
//                    case A: id = R.drawable.ac; break;
//                    case K: id = R.drawable.kc; break;
//                    case Q: id = R.drawable.qc; break;
//                    case J: id = R.drawable.jc; break;
//                }
//                break;
//            case HEART:
//                switch (c.getCt()){
//                    case A: id = R.drawable.ah; break;
//                    case K: id = R.drawable.kh; break;
//                    case Q: id = R.drawable.qh; break;
//                    case J: id = R.drawable.jh; break;
//                }
//        }
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), id);
//        bmp = Bitmap.createScaledBitmap(bmp, Card.width, Card.height, true);
//        return bmp;
//    }

}
