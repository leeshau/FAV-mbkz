package cz.zcu.fav.lesekjan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Spinner;
import android.widget.Toast;

public class ChooseDiffActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_diff);
    }
    public void begin_act(View v){
        String[] diff_arr = getResources().getStringArray(R.array.diff_array_values);
        int diff_position = ((Spinner)(findViewById(R.id.spinner_diff)) ).getSelectedItemPosition();
        /*int diff*/ GameActivity.diff = Integer.valueOf(diff_arr[diff_position]);
        System.out.println("Diff, number of columns: " + GameActivity.diff);

        String[] colors_arr = getResources().getStringArray(R.array.colors_array_values);
        int colors_position = ((Spinner) (findViewById(R.id.spinner_colors)) ).getSelectedItemPosition();
        /*int colors*/ GameActivity.colors = Integer.valueOf(colors_arr[colors_position]);
        System.out.println("Colors: " + GameActivity.colors);

        startActivity(new Intent(this, GameActivity.class));
        finish();
    }
}
