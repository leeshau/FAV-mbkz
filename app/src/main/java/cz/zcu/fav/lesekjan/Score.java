package cz.zcu.fav.lesekjan;

import android.content.SharedPreferences;

import java.util.LinkedList;

class Score{
    String name;
    int score;
    Score(String name, int score){
        this.name = name;
        this.score = score;
    }

    static LinkedList<Score> getScores(SharedPreferences sp){
        int i;
        LinkedList<Score> scores = new LinkedList<>();
        for(i = 0; i < 5; i++){
            int sc = sp.getInt("" + i, -1);
            if(sc == -1)
                break;
            scores.add(new Score(sp.getString("" + i + "_name", ""), sc));
        }
        return scores;
    }

    @Override
    public String toString(){
        return this.name + " : " + this.score;
    }
}