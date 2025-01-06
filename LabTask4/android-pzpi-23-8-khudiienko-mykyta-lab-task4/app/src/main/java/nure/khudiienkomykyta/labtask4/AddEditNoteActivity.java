package nure.khudiienkomykyta.labtask4;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.InputStream;
import java.util.Date;

public class AddEditNoteActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText titleEdit;
    private EditText descriptionEdit;
    private Spinner prioritySpinner;
    private ImageView selectedImage;
    private Button selectImageButton;
    private Button saveButton;

    private Uri selectedImageUri;
    private Note editingNote;
    private int editingPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        // Ініціалізація views
        titleEdit = findViewById(R.id.edit_title);
        descriptionEdit = findViewById(R.id.edit_description);
        prioritySpinner = findViewById(R.id.priority_spinner);
        selectedImage = findViewById(R.id.selected_image);
        selectImageButton = findViewById(R.id.btn_select_image);
        saveButton = findViewById(R.id.btn_save);

        // Налаштування spinner'а для вибору пріоритету
        setupPrioritySpinner();

        // Перевірка чи редагуємо існуючу нотатку
        checkForEditingNote();

        // Налаштування обробників кнопок listeners
        setupClickListeners();
    }



    private void setupPrioritySpinner() {
        ArrayAdapter<Priority> priorityAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Priority.values()
        );
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityAdapter);
    }

    private void checkForEditingNote() {
        Intent intent = getIntent();
        if (intent.hasExtra("note")) {
            editingNote = (Note) intent.getSerializableExtra("note");
            editingPosition = intent.getIntExtra("position", -1);
            fillFieldsWithNote(editingNote);
        }
    }

    private void setupClickListeners() {
        selectImageButton.setOnClickListener(v -> selectImage());
        saveButton.setOnClickListener(v -> saveNote());
    }

    private void fillFieldsWithNote(Note note) {
        titleEdit.setText(note.getTitle());
        descriptionEdit.setText(note.getDescription());
        prioritySpinner.setSelection(note.getPriority().ordinal());

        if (note.getImageUri() != null) {
            selectedImageUri = Uri.parse(note.getImageUri());
            loadImage(selectedImageUri);
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*"); // Обираємо всі типи зображень
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            loadImage(selectedImageUri);
        }
    }

    private void loadImage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();


                int maxDimension = 1024;
                int scaleFactor = Math.min(options.outWidth / maxDimension, options.outHeight / maxDimension);


                options.inJustDecodeBounds = false;
                options.inSampleSize = scaleFactor;

                inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                selectedImage.setImageBitmap(bitmap);
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveNote() {
        String title = titleEdit.getText().toString().trim();
        String description = descriptionEdit.getText().toString().trim();
        Priority priority = (Priority) prioritySpinner.getSelectedItem();

        if (title.isEmpty()) {
            titleEdit.setError("Title is required");
            return;
        }

        Note note = new Note(
                title,
                description,
                priority,
                new Date(),
                selectedImageUri != null ? selectedImageUri.toString() : null
        );

        Intent resultIntent = new Intent();
        resultIntent.putExtra("note", note);
        if (editingPosition != -1) {
            resultIntent.putExtra("position", editingPosition);
        }

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}