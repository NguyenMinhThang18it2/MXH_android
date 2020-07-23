package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

public class ThemeStatus {
    @SerializedName("_id")
    private String id;
    @SerializedName("themestatus")
    private String themestatus;

    public ThemeStatus(String id, String themestatus) {
        this.id = id;
        this.themestatus = themestatus;
    }

    @Override
    public String toString() {
        return "ThemeStatus{" +
                "id='" + id + '\'' +
                ", themestatus='" + themestatus + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThemestatus() {
        return themestatus;
    }

    public void setThemestatus(String themestatus) {
        this.themestatus = themestatus;
    }
}
