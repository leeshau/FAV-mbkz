package cz.zcu.fav.lesekjan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load_score();
        GameActivity.main_activity = this;
        mp = MediaPlayer.create(MainActivity.this, R.raw.dreamy_synth);
        mp.setLooping(true);
        mp.start();
        init_mute_play_btn();
    }

    private void init_mute_play_btn() {
        findViewById(R.id.mute_play_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_music_play();
            }
        });
    }

    private void change_music_play(){
        if(mp.isPlaying()){
            mp.pause();
        } else {
            mp.start();
        }
    }

    public void new_game_act(View v){
        startActivity(new Intent(this, ChooseDiffActivity.class));
    }

    void load_score(){
        LinkedList<Score> scores = Score.getScores(getSharedPreferences("score_prefs", MODE_PRIVATE));
        if (scores.size() < 1) return;
        ((TextView) findViewById(R.id.p1_name)).setText(scores.get(0).name);
        ((TextView) findViewById(R.id.p1_score)).setText("" + scores.get(0).score);
        if (scores.size() < 2) return;
        ((TextView) findViewById(R.id.p2_name)).setText(scores.get(1).name);
        ((TextView) findViewById(R.id.p2_score)).setText("" + scores.get(1).score);
        if (scores.size() < 3) return;
        ((TextView) findViewById(R.id.p3_name)).setText(scores.get(2).name);
        ((TextView) findViewById(R.id.p3_score)).setText("" + scores.get(2).score);
        if (scores.size() < 4) return;
        ((TextView) findViewById(R.id.p4_name)).setText(scores.get(3).name);
        ((TextView) findViewById(R.id.p4_score)).setText("" + scores.get(3).score);
        if (scores.size() < 5) return;
        ((TextView) findViewById(R.id.p5_name)).setText(scores.get(4).name);
        ((TextView) findViewById(R.id.p5_score)).setText("" + scores.get(4).score);
    }

    public void delete_scores_button_press(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.dialog_delete))
                .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        delete_scores();
                    }
                })
                .setNegativeButton(R.string.dialog_decline, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //empty
                    }
                });
        builder.show();
    }

    public void delete_scores(){
        getSharedPreferences("score_prefs", MODE_PRIVATE).edit().clear().apply();
        ((TextView) findViewById(R.id.p1_name)).setText(getResources().getText(R.string.void_score_name));
        ((TextView) findViewById(R.id.p1_score)).setText(getResources().getText(R.string.void_score_score));
        ((TextView) findViewById(R.id.p2_name)).setText(getResources().getText(R.string.void_score_name));
        ((TextView) findViewById(R.id.p2_score)).setText(getResources().getText(R.string.void_score_score));
        ((TextView) findViewById(R.id.p3_name)).setText(getResources().getText(R.string.void_score_name));
        ((TextView) findViewById(R.id.p3_score)).setText(getResources().getText(R.string.void_score_score));
        ((TextView) findViewById(R.id.p4_name)).setText(getResources().getText(R.string.void_score_name));
        ((TextView) findViewById(R.id.p4_score)).setText(getResources().getText(R.string.void_score_score));
        ((TextView) findViewById(R.id.p5_name)).setText(getResources().getText(R.string.void_score_name));
        ((TextView) findViewById(R.id.p5_score)).setText(getResources().getText(R.string.void_score_score));
    }

    public void exit_act(View v) {
        finish();
        System.exit(0);
    }
}
