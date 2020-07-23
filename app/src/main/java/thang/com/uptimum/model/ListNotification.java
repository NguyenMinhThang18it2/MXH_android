package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

public class ListNotification {
    @SerializedName("idPosts")
    private Posts idPosts;
    @SerializedName("idStory")
    private String idStory;
    @SerializedName("iduserNotify")
    private Users iduserNotify;
    @SerializedName("status")
    private boolean status;
    @SerializedName("title")
    private String title;
    @SerializedName("createAt")
    private String createAt;
    @SerializedName("updateAt")
    private String updateAt;

    public ListNotification(Posts idPosts, String idStory, Users iduserNotify, boolean status, String title, String createAt, String updateAt) {
        this.idPosts = idPosts;
        this.idStory = idStory;
        this.iduserNotify = iduserNotify;
        this.status = status;
        this.title = title;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    @Override
    public String toString() {
        return "ListNotification{" +
                "idPosts='" + idPosts + '\'' +
                ", idStory='" + idStory + '\'' +
                ", iduserNotify=" + iduserNotify +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", createAt='" + createAt + '\'' +
                ", updateAt='" + updateAt + '\'' +
                '}';
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
