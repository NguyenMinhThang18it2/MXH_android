package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

public class Education {
    @SerializedName("studied_at")
    private String studied_at;
    @SerializedName("studies_at")
    private String studies_at;

    public Education(String studied_at, String studies_at) {
        this.studied_at = studied_at;
        this.studies_at = studies_at;
    }

    @Override
    public String toString() {
        return "Education{" +
                "studied_at='" + studied_at + '\'' +
                ", studies_at='" + studies_at + '\'' +
                '}';
    }

    public String getStudied_at() {
        return studied_at;
    }

    public void setStudied_at(String studied_at) {
        this.studied_at = studied_at;
    }

    public String getStudies_at() {
        return studies_at;
    }

    public void setStudies_at(String studies_at) {
        this.studies_at = studies_at;
    }
}
