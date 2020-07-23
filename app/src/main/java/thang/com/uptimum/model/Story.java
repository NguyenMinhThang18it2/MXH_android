package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

public class Story {
    @SerializedName("_id")
    private String id;
    @SerializedName("iduser")
    private Users users;
    @SerializedName("file")
    private String[] file;
    @SerializedName("text")
    private TextStory textStory;
    @SerializedName("numberLike")
    private UserLike[] like;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("updatedAt")
    private String updatedAt;

    public Story(String id, Users users, String[] file, TextStory textStory, UserLike[] like, String createdAt, String updatedAt) {
        this.id = id;
        this.users = users;
        this.file = file;
        this.textStory = textStory;
        this.like = like;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Story{" +
                "id='" + id + '\'' +
                ", users=" + users +
                ", file=" + file +
                ", textStory=" + textStory +
                ", like=" + like +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String[] getFile() {
        return file;
    }

    public void setFile(String[] file) {
        this.file = file;
    }

    public TextStory getTextStory() {
        return textStory;
    }

    public void setTextStory(TextStory textStory) {
        this.textStory = textStory;
    }

    public UserLike[] getLike() {
        return like;
    }

    public void setLike(UserLike[] like) {
        this.like = like;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
