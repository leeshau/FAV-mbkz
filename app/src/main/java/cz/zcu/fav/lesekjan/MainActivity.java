package cz.zcu.fav.lesekjan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void new_game_act(View v){
        Toast.makeText(this, "tady bude new game", Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(this, GameActivity.class));
        startActivity(new Intent(this, ChooseDiffActivity.class));
    }

    public void score_act(View v){
        Toast.makeText(this, "tady bude score69", Toast.LENGTH_SHORT).show();
    }

    public void exit_act(View v){
        finish();
        System.exit(0);
    }
}
