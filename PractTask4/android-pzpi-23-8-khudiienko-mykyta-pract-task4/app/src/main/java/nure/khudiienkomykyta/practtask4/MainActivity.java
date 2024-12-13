package nure.khudiienkomykyta.practtask4;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    EditText editTextName;
    EditText editTextAge;
    EditText editTextText;
    Button buttonAddUser;
    Button buttonSaveSharedPreferences;
    Button btnwriteToFile;
    Button btnreadFromFile;
    TextView textViewDisplay;

    public class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, "MyDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY, name TEXT, age INTEGER)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = new DBHelper(this).getWritableDatabase();


        btnwriteToFile = findViewById(R.id.writeToFile);
        btnreadFromFile = findViewById(R.id.readFromFile);

        editTextText = findViewById(R.id.eText);
        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        buttonAddUser = findViewById(R.id.buttonAddUser);
        buttonSaveSharedPreferences = findViewById(R.id.buttonSaveSharedPreferences);
        textViewDisplay = findViewById(R.id.textViewDisplay);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Зчитування даних при запуску
        String name = sharedPreferences.getString("name", "");
        String age = sharedPreferences.getString("age", "");

        textViewDisplay.setText(name + " "+ age);

        // Збереження даних при натисканні кнопки
        buttonSaveSharedPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String age = editTextAge.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", name);
                editor.putString("age", age);
                editor.apply();
            }
        });

        // Додаємо користувача SQLite
        buttonAddUser.setOnClickListener(v -> {
            ContentValues values = new ContentValues();
            values.put("name", editTextName.getText().toString());
            values.put("age", Integer.parseInt(editTextAge.getText().toString()));
            db.insert("users", null, values);

            loadUsers();
        });


        //Додаваня до файлу
        btnwriteToFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile();
            }
        });

        //Зчитування з файлу
        btnreadFromFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFromFile();
            }
        });
    }

    private void loadUsers() {
        StringBuilder usersText = new StringBuilder();
        Cursor cursor = db.query("users", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") int age = cursor.getInt(cursor.getColumnIndex("age"));
            usersText.append("Name: ").append(name).append(", Age: ").append(age).append("\n");
        }
        cursor.close();

        // Виведення даних у TextView
        textViewDisplay.setText(usersText.toString());
    }


    private void saveToFile() {
        FileOutputStream fos;
        String text = editTextText.getText().toString();
        try {
            fos = openFileOutput("myfile.txt", MODE_PRIVATE);
            fos.write(text.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        FileInputStream fis;
        try {
            fis = openFileInput("myfile.txt");
            int c;
            String temp = "";
            while( (c = fis.read()) != -1) {
                temp = temp + Character.toString((char)c);
            }
            fis.close();
            textViewDisplay.setText(temp.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}