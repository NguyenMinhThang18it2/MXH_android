package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

public class ListNotification {
    @SerializedName("_id")
    private String id;
    @SerializedName("idPosts")
    private Posts idPosts;
    @SerializedName("idStory")
    private String idStory;
    @SerializedName("iduserNotify")
    private Users iduserNotify;
    @SerializedName("status")
    private boolean status;
    @SerializedName("typeLike")
    private int typeLike;
    @SerializedName("title")
    private String title;
    @SerializedName("createdAt")
    private String createAt;
    @SerializedName("updatedAt")
    private String updateAt;

    public ListNotification(String id, Posts idPosts, String idStory, Users iduserNotify, boolean status, int typeLike, String title, String createAt, String updateAt) {
        this.id = id;
        this.idPosts = idPosts;
        this.idStory = idStory;
        this.iduserNotify = iduserNotify;
        this.status = status;
        this.typeLike = typeLike;
        this.title = title;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    @Override
    public String toString() {
        return "ListNotification{" +
                "id='" + id + '\'' +
                ", idPosts=" + idPosts +
                ", idStory='" + idStory + '\'' +
                ", iduserNotify=" + iduserNotify +
                ", status=" + status +
                ", typeLike=" + typeLike +
                ", title='" + title + '\'' +
                ", createAt='" + createAt + '\'' +
                ", updateAt='" + updateAt + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Posts getIdPosts() {
        return idPosts;
    }

    public void setIdPosts(Posts idPosts) {
        this.idPosts = idPosts;
    }

    public String getIdStory() {
        return idStory;
    }

    public void setIdStory(String idStory) {
        this.idStory = idStory;
    }

    public Users getIduserNotify() {
        return iduserNotify;
    }

    public void setIduserNotify(Users iduserNotify) {
        this.iduserNotify = iduserNotify;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getTypeLike() {
        return typeLike;
    }

    public void setTypeLike(int typeLike) {
        this.typeLike = typeLike;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
