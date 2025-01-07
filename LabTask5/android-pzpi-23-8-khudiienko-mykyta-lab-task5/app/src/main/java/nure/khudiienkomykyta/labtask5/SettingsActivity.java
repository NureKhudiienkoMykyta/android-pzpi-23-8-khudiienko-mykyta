package nure.khudiienkomykyta.labtask5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {
    private RadioGroup themeGroup;
    private SeekBar fontSizeSeekBar;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        themeGroup = findViewById(R.id.theme_group);
        fontSizeSeekBar = findViewById(R.id.font_size_seekbar);
        saveButton = findViewById(R.id.btn_save_settings);

        loadPreferences();

        saveButton.setOnClickListener(v -> {
            savePreferences();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Закриваємо SettingsActivity
        });
    }

    private void loadPreferences() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        int selectedTheme = prefs.getInt("theme", R.id.radio_light);
        int fontSize = prefs.getInt("fontSize", 16);

        themeGroup.check(selectedTheme);
        fontSizeSeekBar.setProgress(fontSize - 12); // Мінімальний розмір 12
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putInt("theme", themeGroup.getCheckedRadioButtonId());
        editor.putInt("fontSize", fontSizeSeekBar.getProgress() + 12); // Мінімальний розмір 12
        editor.apply();
    }
}