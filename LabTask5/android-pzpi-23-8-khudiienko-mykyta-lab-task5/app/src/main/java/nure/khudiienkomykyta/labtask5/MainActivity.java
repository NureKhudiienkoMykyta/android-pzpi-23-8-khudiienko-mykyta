package nure.khudiienkomykyta.labtask5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NotesDatabaseHelper dbHelper;
    private List<Note> notesList;
    private List<Note> filteredList; // Список для відфільтрованих нотаток
    private NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new NotesDatabaseHelper(this);
        notesList = dbHelper.getAllNotes();
        filteredList = new ArrayList<>(notesList);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NotesAdapter(filteredList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemLongClickListener((view, position) -> {
            registerForContextMenu(view);
            view.showContextMenu();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyPreferences(); // Застосовуємо налаштування теми і шрифтів
    }



    private void applyPreferences() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        int fontSize = prefs.getInt("fontSize", 16); // Отримуємо розмір шрифту
        adapter.setFontSize(fontSize); // Застосовуємо до адаптера
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Пошук
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterNotes(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotes(newText);
                return true;
            }
        });

        return true;
    }
    private void applyTheme() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        int themeId = prefs.getInt("theme", R.id.radio_light);

        if (themeId == R.id.radio_dark) {
            setTheme(R.style.Theme_LabTask5_Dark);
        } else {
            setTheme(R.style.Theme_LabTask5_Light);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            startActivityForResult(new Intent(this, AddEditNoteActivity.class), 1);
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.filter_all) {
            filteredList = new ArrayList<>(notesList); // Показати всі нотатки
            adapter.updateList(filteredList);
            return true;
        } else if (id == R.id.filter_high) {
            filterByPriority(Priority.HIGH);
            return true;
        } else if (id == R.id.filter_medium) {
            filterByPriority(Priority.MEDIUM);
            return true;
        } else if (id == R.id.filter_low) {
            filterByPriority(Priority.LOW);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Note note = (Note) data.getSerializableExtra("note");
            if (requestCode == 1) { // Add
                dbHelper.addNote(note);
            } else if (requestCode == 2) { // Edit
                dbHelper.updateNote(note);
            }
            notesList = dbHelper.getAllNotes();
            filteredList = new ArrayList<>(notesList); // Оновлюємо список для пошуку/фільтрації
            adapter.updateList(filteredList);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = adapter.getContextMenuPosition();
        Note note = filteredList.get(position);

        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(this, AddEditNoteActivity.class);
            intent.putExtra("note", note);
            startActivityForResult(intent, 2);
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            dbHelper.deleteNote(note.getId());
            notesList.remove(note); // Видаляємо з основного списку
            filteredList.remove(position); // Видаляємо з відфільтрованого списку
            adapter.notifyItemRemoved(position);
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void filterNotes(String query) {
        filteredList.clear();
        for (Note note : notesList) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    note.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(note);
            }
        }
        adapter.updateList(filteredList);
    }

    private void filterByPriority(Priority priority) {
        filteredList.clear();
        for (Note note : notesList) {
            if (note.getPriority() == priority) {
                filteredList.add(note);
            }
        }
        adapter.updateList(filteredList);
    }
}