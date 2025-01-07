package nure.khudiienkomykyta.labtask5;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        titleEdit = findViewById(R.id.edit_title);
        descriptionEdit = findViewById(R.id.edit_description);
        prioritySpinner = findViewById(R.id.priority_spinner);
        selectedImage = findViewById(R.id.selected_image);
        selectImageButton = findViewById(R.id.btn_select_image);
        saveButton = findViewById(R.id.btn_save);

        setupPrioritySpinner();
        checkForEditingNote();
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
            fillFieldsWithNote();
        }
    }

    private void fillFieldsWithNote() {
        titleEdit.setText(editingNote.getTitle());
        descriptionEdit.setText(editingNote.getDescription());
        prioritySpinner.setSelection(editingNote.getPriority().ordinal());
        if (editingNote.getImageUri() != null) {
            selectedImageUri = Uri.parse(editingNote.getImageUri());
            loadImage(selectedImageUri);
        }
    }

    private void setupClickListeners() {
        selectImageButton.setOnClickListener(v -> selectImage());
        saveButton.setOnClickListener(v -> saveNote());
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            loadImage(selectedImageUri);
        }
    }

    private void loadImage(Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
            selectedImage.setImageURI(uri);
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
                editingNote != null ? editingNote.getId() : -1,
                title,
                description,
                priority,
                new Date(),
                selectedImageUri != null ? selectedImageUri.toString() : null
        );

        Intent resultIntent = new Intent();
        resultIntent.putExtra("note", note);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}