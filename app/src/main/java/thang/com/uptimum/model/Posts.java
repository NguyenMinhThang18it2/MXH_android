package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Posts {
    @SerializedName("_id")
    private String id;
    @SerializedName("iduser")
    private Users iduser;
    @SerializedName("document")
    private String document;
    @SerializedName("file")
    private FileMxh file;
    @SerializedName("numberLike")
    private UserLike[] like;
    @SerializedName("numberCmt")
    private int comment;
    @SerializedName("createdAt")
    private String createdAt;

    public Posts(String id, Users iduser, String document, FileMxh file, UserLike[] like, int comment, String createdAt) {
        this.id = id;
        this.iduser = iduser;
        this.document = document;
        this.file = file;
        this.like = like;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Posts{" +
                "id='" + id + '\'' +
                ", iduser=" + iduser +
                ", document='" + document + '\'' +
                ", file=" + file +
                ", like=" + Arrays.toString(like) +
                ", comment='" + comment + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Users getIduser() {
        return iduser;
    }

    public void setIduser(Users iduser) {
        this.iduser = iduser;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public FileMxh getFile() {
        return file;
    }

    public void setFile(FileMxh file) {
        this.file = file;
    }

    public UserLike[] getLike() {
        return like;
    }

    public void setLike(UserLike[] like) {
        this.like = like;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
