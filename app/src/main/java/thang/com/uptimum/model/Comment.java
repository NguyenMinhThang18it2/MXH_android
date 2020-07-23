package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Comment {
    @SerializedName("_id")
    private String id;
    @SerializedName("idposts")
    private String idposts;
    @SerializedName("iduser")
    private Users iduser;
    @SerializedName("document")
    private String document;
    @SerializedName("file")
    private FileMxh file;
    @SerializedName("numberLike")
    private UserLike[] numberLike;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("updatedAt")
    private String updatedAt;

    public Comment(String id, String idposts, Users iduser, String document, FileMxh file, UserLike[] numberLike, String createdAt, String updatedAt) {
        this.id = id;
        this.idposts = idposts;
        this.iduser = iduser;
        this.document = document;
        this.file = file;
        this.numberLike = numberLike;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", idposts='" + idposts + '\'' +
                ", iduser=" + iduser +
                ", document='" + document + '\'' +
                ", file=" + file +
                ", numberLike=" + Arrays.toString(numberLike) +
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

    public String getIdposts() {
        return idposts;
    }

    public void setIdposts(String idposts) {
        this.idposts = idposts;
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

    public UserLike[] getNumberLike() {
        return numberLike;
    }

    public void setNumberLike(UserLike[] numberLike) {
        this.numberLike = numberLike;
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
