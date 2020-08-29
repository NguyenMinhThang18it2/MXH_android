package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class ReplyComment {
    @SerializedName("_id")
    private String id;
    @SerializedName("idcmt")
    private String idcmt;
    @SerializedName("idposts")
    private String idposts;
    @SerializedName("listCmt")
    private ReplyListCmt[] listCmt;

    public ReplyComment(String id, String idcmt, String idposts, ReplyListCmt[] listCmt) {
        this.id = id;
        this.idcmt = idcmt;
        this.idposts = idposts;
        this.listCmt = listCmt;
    }

    @Override
    public String toString() {
        return "ReplyComment{" +
                "id='" + id + '\'' +
                ", idcmt='" + idcmt + '\'' +
                ", idposts='" + idposts + '\'' +
                ", listCmt=" + Arrays.toString(listCmt) +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdcmt() {
        return idcmt;
    }

    public void setIdcmt(String idcmt) {
        this.idcmt = idcmt;
    }

    public String getIdposts() {
        return idposts;
    }

    public void setIdposts(String idposts) {
        this.idposts = idposts;
    }

    public ReplyListCmt[] getListCmt() {
        return listCmt;
    }

    public void setListCmt(ReplyListCmt[] listCmt) {
        this.listCmt = listCmt;
    }
}
