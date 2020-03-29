package cz.zcu.fav.lesekjan;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import java.util.Random;

public class GameActivity extends Activity implements SurfaceHolder.Callback {

    public static int diff;
    public static int colors;
    private Random r = new Random();
    private static final String TAG = "Svetlin SurfaceView";
    private static final int COLUMNS = 4;

    private Card[][] cards;


    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            System.out.println("pressed");

            float x_point = event.getX();
            float y_point = event.getY();

            for (int i = 0; i < diff; i++){
                for (int j = 0; j < COLUMNS; j++) {
                    System.out.println("card: " + cards[i][j].toString());
                    if (cards[i][j].cardBT.contains((int) x_point, (int) y_point)) {
                        System.out.println("touched card: " + cards[i][j].toString());
                        System.out.println("point: x: " + x_point + ", y: " + y_point);
                        return true;
                    }
                }
            }
            System.out.println("point: x: "+x_point+", y: "+y_point);

        } else if(event.getAction() == MotionEvent.ACTION_UP) {
            System.out.println("released");
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        SurfaceView view = new SurfaceView(this);
        setContentView(view);
        view.getHolder().addCallback(this);

        createStuff();
    }

    private void createStuff(){
        cards = new Card[diff][COLUMNS];
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        int x_ratio = screenWidth / 1344; //original width
        int y_ratio = screenHeight / 720; //original height

        /*setting graphics for every display*/
        Card.offset *= x_ratio;
        Card.width *= x_ratio;
        Card.height *= y_ratio;

        /*space between left edge of display and the first card*/
        int offset_x = (screenWidth - (Card.width - Card.offset) * diff) / 2;

        for (int i = 0; i < diff; i++){
            for (int j = 0; j < COLUMNS; j++){
                cards[i][j] = new Card();
                setCard(cards[i][j]);
                cards[i][j].x = offset_x + i * Card.width + Card.offset;
                cards[i][j].y = j * Card.height + Card.offset;
            }
        }
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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawStage(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int frmt, int w, int h) {
        drawStage(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        cards = null;
    }

    private void drawStage(SurfaceHolder holder) {
        Log.i(TAG, "Trying to draw...");

        Canvas canvas = holder.lockCanvas();
        if (canvas == null) {
            Log.e(TAG, "Cannot draw onto the canvas as it's null");
            return;
        }
        Log.i(TAG, "Drawing...");
        canvas.drawRGB(255, 128, 0);

        for (int i = 0; i < diff; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                canvas.drawBitmap(getCardBitmap(cards[i][j]), cards[i][j].x, cards[i][j].y, null);
            }
        }
        holder.unlockCanvasAndPost(canvas);

    }

    private Bitmap getCardBitmap(Card c) {
        int id = R.drawable.icon;
        switch (c.getCc()){
            case SPADE:
                switch (c.getCt()){
                    case A: id = R.drawable.as; break;
                    case K: id = R.drawable.ks; break;
                    case Q: id = R.drawable.qs; break;
                    case J: id = R.drawable.js; break;
                }
                break;
            case DIAMOND:
                switch (c.getCt()){
                    case A: id = R.drawable.ad; break;
                    case K: id = R.drawable.kd; break;
                    case Q: id = R.drawable.qd; break;
                    case J: id = R.drawable.jd; break;
                }
                break;
            case CLUB:
                switch (c.getCt()){
                    case A: id = R.drawable.ac; break;
                    case K: id = R.drawable.kc; break;
                    case Q: id = R.drawable.qc; break;
                    case J: id = R.drawable.jc; break;
                }
                break;
            case HEART:
                switch (c.getCt()){
                    case A: id = R.drawable.ah; break;
                    case K: id = R.drawable.kh; break;
                    case Q: id = R.drawable.qh; break;
                    case J: id = R.drawable.jh; break;
                }
        }
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), id);
        bmp = Bitmap.createScaledBitmap(bmp, Card.width, Card.height, true);
        return bmp;
    }

}
