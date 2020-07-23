package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

public class Friend {
    @SerializedName("idfriend")
    private Users idfriend;

    public Friend(Users idfriend) {
        this.idfriend = idfriend;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "idfriend=" + idfriend +
                '}';
    }

    public Users getIdfriend() {
        return idfriend;
    }

    public void setIdfriend(Users idfriend) {
        this.idfriend = idfriend;
    }
}
