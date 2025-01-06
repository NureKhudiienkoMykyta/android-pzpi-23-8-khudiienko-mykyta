package nure.khudiienkomykyta.labtask4;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private List<Note> notesList;
    private NotesAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NotesAdapter(notesList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemLongClickListener((view, position) -> {
            registerForContextMenu(view);
            view.showContextMenu();
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

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

    private void filterNotes(String query) {
        List<Note> filteredList = new ArrayList<>();
        for (Note note : notesList) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    note.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(note);
            }
        }
        adapter.updateList(filteredList);
    }

    public void filterByPriority(Priority priority) {
        if (priority == null) {
            adapter.updateList(notesList);
            return;
        }

        List<Note> filteredList = new ArrayList<>();
        for (Note note : notesList) {
            if (note.getPriority() == priority) {
                filteredList.add(note);
            }
        }
        adapter.updateList(filteredList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.filter_all) {
            adapter.updateList(notesList);
            return true;
        }
        else if (itemId == R.id.filter_high) {
            filterByPriority(Priority.HIGH);
            return true;
        }
        else if (itemId == R.id.filter_medium) {
            filterByPriority(Priority.MEDIUM);
            return true;
        }
        else if (itemId == R.id.filter_low) {
            filterByPriority(Priority.LOW);
            return true;
        }
        else if (itemId == R.id.action_add) {
            startActivityForResult(new Intent(this, AddEditNoteActivity.class), 1);
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
                notesList.add(note);
            } else if (requestCode == 2) { // Edit
                int position = data.getIntExtra("position", -1);
                if (position != -1) {
                    notesList.set(position, note);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(adapter.getContextMenuPosition());
        if (viewHolder == null) {
            return super.onContextItemSelected(item);
        }
        int position = viewHolder.getAdapterPosition();

        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(this, AddEditNoteActivity.class);
            intent.putExtra("note", notesList.get(position));
            intent.putExtra("position", position);
            startActivityForResult(intent, 2);
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            notesList.remove(position);
            adapter.notifyItemRemoved(position);
            return true;
        }
        return super.onContextItemSelected(item);
    }

}
