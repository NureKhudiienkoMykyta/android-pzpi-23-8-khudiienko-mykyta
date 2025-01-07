package nure.khudiienkomykyta.labtask5;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {
    private int id; // Нове поле
    private String title;
    private String description;
    private Priority priority;
    private Date dateTime;
    private String imageUri;

    public Note(int id, String title, String description, Priority priority, Date dateTime, String imageUri) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dateTime = dateTime;
        this.imageUri = imageUri;
    }

    public Note(String title, String description, Priority priority, Date dateTime, String imageUri) {
        this(-1, title, description, priority, dateTime, imageUri);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Priority getPriority() {
        return priority;
    }
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    public Date getDateTime() {
        return dateTime;
    }
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
    public String getImageUri() {
        return imageUri;
    }
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}

