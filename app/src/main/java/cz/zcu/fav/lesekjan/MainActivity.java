package cz.zcu.fav.lesekjan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        load_score();
        setContentView(R.layout.activity_main);
    }

    public void new_game_act(View v){
        startActivity(new Intent(this, ChooseDiffActivity.class));
    }

    private void load_score(){
        ArrayList<Score> scores = new ArrayList<>();
        File f = new File("score.solightare");
        if(!f.exists()){
//            findViewById(R.id.tableLayout).setVisibility(View.INVISIBLE);
//            findViewById(R.id.scores_label).setVisibility(View.INVISIBLE);
            return;
        }
        try {
            Scanner sc = new Scanner(f);
            while(sc.hasNext()){
                String[] data = sc.nextLine().split(" ");
                if(!data[1].matches("[0-9]+")){
                    System.err.println("score.solightare damaged, deleting it");
                    f.delete();
                    load_score();
                    return;
                }
                scores.add(new Score(data[0], Integer.parseInt(data[1])));
            }
            Collections.sort(scores, new Comparator<Score>() {
                @Override
                public int compare(Score o1, Score o2) {
                    return o1.score - o2.score;
                }
            });
            if(scores.size() < 1) return;
            ((TextView)findViewById(R.id.p1_name)).setText(scores.get(0).name);
            ((TextView)findViewById(R.id.p1_score)).setText(scores.get(0).score);
            if(scores.size() < 2) return;
            ((TextView)findViewById(R.id.p2_name)).setText(scores.get(1).name);
            ((TextView)findViewById(R.id.p2_score)).setText(scores.get(1).score);
            if(scores.size() < 3) return;
            ((TextView)findViewById(R.id.p3_name)).setText(scores.get(2).name);
            ((TextView)findViewById(R.id.p3_score)).setText(scores.get(2).score);
            if(scores.size() < 4) return;
            ((TextView)findViewById(R.id.p4_name)).setText(scores.get(3).name);
            ((TextView)findViewById(R.id.p4_score)).setText(scores.get(3).score);
            if(scores.size() < 5) return;
            ((TextView)findViewById(R.id.p5_name)).setText(scores.get(4).name);
            ((TextView)findViewById(R.id.p5_score)).setText(scores.get(4).score);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exit_act(View v){
        finish();
        System.exit(0);
    }
     private class Score{
        private String name;
        private int score;
        private Score(String name, int score){
            this.name = name;
            this.score = score;
        }
     }
}
