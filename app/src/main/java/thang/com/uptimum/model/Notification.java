package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Notification {
    @SerializedName("_id")
    private String id;
    @SerializedName("iduser")
    private String iduser;
    @SerializedName("listnotification")
    private ListNotification[] listNotification;

    public Notification(String id, String iduser, ListNotification[] listNotification) {
        this.id = id;
        this.iduser = iduser;
        this.listNotification = listNotification;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", iduser=" + iduser +
                ", listNotification=" + Arrays.toString(listNotification) +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public ListNotification[] getListNotification() {
        return listNotification;
    }

    public void setListNotification(ListNotification[] listNotification) {
        this.listNotification = listNotification;
    }
}
