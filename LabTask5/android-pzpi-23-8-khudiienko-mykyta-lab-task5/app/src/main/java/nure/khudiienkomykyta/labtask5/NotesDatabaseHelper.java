package nure.khudiienkomykyta.labtask5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes2.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NOTES = "notes2";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRIORITY = "priority";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_IMAGE_URI = "image_uri";

    public NotesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NOTES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PRIORITY + " TEXT, " +
                COLUMN_DATE + " INTEGER, " +
                COLUMN_IMAGE_URI + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public long addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_DESCRIPTION, note.getDescription());
        values.put(COLUMN_PRIORITY, note.getPriority().toString());
        values.put(COLUMN_DATE, note.getDateTime().getTime());
        values.put(COLUMN_IMAGE_URI, note.getImageUri());

        return db.insert(TABLE_NOTES, null, values);
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_DESCRIPTION, note.getDescription());
        values.put(COLUMN_PRIORITY, note.getPriority().toString());
        values.put(COLUMN_DATE, note.getDateTime().getTime());
        values.put(COLUMN_IMAGE_URI, note.getImageUri());

        return db.update(TABLE_NOTES, values, COLUMN_ID + "=?", new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, null, null, null, null, null, COLUMN_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        Priority.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY))),
                        new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DATE))),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URI))
                );
                notes.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return notes;
    }
}
