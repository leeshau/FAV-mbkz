package cz.zcu.fav.lesekjan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load_score();
        GameActivity.main_activity = this;
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
