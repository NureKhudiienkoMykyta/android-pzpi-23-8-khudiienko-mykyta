package nure.khudiienkomykyta.labtask2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private View colorPanel;
    private SeekBar redSeekBar, blueSeekBar, greenSeekBar;
    private int red, green, blue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorPanel = findViewById(R.id.view);
        redSeekBar = findViewById(R.id.seekBarRed);
        greenSeekBar = findViewById(R.id.seekBarGreen);
        blueSeekBar = findViewById(R.id.seekBarBlue);

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar == redSeekBar) red = progress;
                if (seekBar == greenSeekBar) green = progress;
                if (seekBar == blueSeekBar) blue = progress;
                updateColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        redSeekBar.setOnSeekBarChangeListener(listener);
        greenSeekBar.setOnSeekBarChangeListener(listener);
        blueSeekBar.setOnSeekBarChangeListener(listener);

    }

    private void updateColor() {
        colorPanel.setBackgroundColor(Color.rgb(red, green, blue));
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("red", red);
        outState.putInt("green", green);
        outState.putInt("blue", blue);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        red = savedInstanceState.getInt("red", 0);
        green = savedInstanceState.getInt("green", 0);
        blue = savedInstanceState.getInt("blue", 0);
        updateColor();
    }
}