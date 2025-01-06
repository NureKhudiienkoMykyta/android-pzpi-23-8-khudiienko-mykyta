package nure.khudiienkomykyta.labtask4;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private List<Note> notes;
    private OnItemLongClickListener longClickListener;

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

    public NotesAdapter(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.titleText.setText(note.getTitle());
        holder.dateText.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm")
                .format(note.getDateTime()));

        switch (note.getPriority()) {
            case HIGH:
                holder.priorityIcon.setImageResource(R.drawable.ic_priority_high);
                break;
            case MEDIUM:
                holder.priorityIcon.setImageResource(R.drawable.ic_priority_medium);
                break;
            case LOW:
                holder.priorityIcon.setImageResource(R.drawable.ic_priority_low);
                break;
        }

        if (note.getImageUri() != null) {
            loadImageFromUri(Uri.parse(note.getImageUri()), holder.noteImage);
        }

    }


    private int contextMenuPosition;

    public int getContextMenuPosition() {
        return contextMenuPosition;
    }


    private void loadImageFromUri(Uri uri, ImageView imageView) {
        try {
            InputStream inputStream = imageView.getContext().getContentResolver().openInputStream(uri);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            int targetW = imageView.getWidth();
            int targetH = imageView.getHeight();
            int scaleFactor = Math.min(options.outWidth / targetW, options.outHeight / targetH);

            options.inJustDecodeBounds = false;
            options.inSampleSize = scaleFactor;
            inputStream = imageView.getContext().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            imageView.setImageBitmap(bitmap);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            imageView.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    @Override
    public int getItemCount() {
        return notes != null ? notes.size() : 0;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void updateList(List<Note> newList) {
        notes = newList;
        notifyDataSetChanged();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, dateText;
        ImageView priorityIcon, noteImage;

        NoteViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.note_title);
            dateText = itemView.findViewById(R.id.note_date);
            priorityIcon = itemView.findViewById(R.id.priority_icon);
            noteImage = itemView.findViewById(R.id.note_image);

            itemView.setOnLongClickListener(v -> {
                contextMenuPosition = getAdapterPosition();
                if (longClickListener != null) {
                    return longClickListener.onItemLongClick(v, getAdapterPosition());
                }
                return false;
            });
        }
    }
}