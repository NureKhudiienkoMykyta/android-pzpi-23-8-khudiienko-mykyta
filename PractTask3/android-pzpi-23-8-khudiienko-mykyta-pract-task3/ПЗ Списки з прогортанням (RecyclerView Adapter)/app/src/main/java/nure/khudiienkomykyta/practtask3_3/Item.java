package nure.khudiienkomykyta.practtask3_3;

public class Item {
    private String text;
    private int avatarResId;

    public Item(String text, int avatarResId) {
        this.text = text;
        this.avatarResId = avatarResId;
    }

    public String getText() {
        return text;
    }

    public int getAvatarResId() {
        return avatarResId;
    }
}
