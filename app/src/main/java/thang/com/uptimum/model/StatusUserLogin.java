package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

public class StatusUserLogin {
    @SerializedName("_id")
    private String id;
    @SerializedName("iduser")
    private Users iduser;
    @SerializedName("status")
    private boolean status;
    @SerializedName("createdAt")
    private String createdAt;

    public StatusUserLogin(String id, Users iduser, boolean status, String createdAt) {
        this.id = id;
        this.iduser = iduser;
        this.status = status;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "StatusUserLogin{" +
                "id='" + id + '\'' +
                ", iduser=" + iduser +
                ", status=" + status +
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
