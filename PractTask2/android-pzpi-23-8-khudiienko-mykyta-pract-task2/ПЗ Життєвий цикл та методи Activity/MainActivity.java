package nure.khudiienkomykyta.practtask2_2;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    String TAG = "Життєвий цикл MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button button = findViewById(R.id.btnSecAct);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        Button finishBtn = findViewById(R.id.btn_finish);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Log.d(TAG, "Створення Activity");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Activity стає видимою");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Стан Resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Стан Paused");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Стан Stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Знищення Activity");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        EditText editText = findViewById(R.id.editText);
        outState.putString("KEY", editText.getText().toString());
        Log.d(TAG, "Збереження");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        EditText editText = findViewById(R.id.editText);
        String savedText = savedInstanceState.getString("KEY");
        editText.setText(savedText);
        Log.d(TAG, "Відновлення");
    }
}