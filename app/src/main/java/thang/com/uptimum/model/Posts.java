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
    private String comment;

    public Posts(String id, Users iduser, String document, FileMxh file, UserLike[] like, String comment) {
        this.id = id;
        this.iduser = iduser;
        this.document = document;
        this.file = file;
        this.like = like;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
