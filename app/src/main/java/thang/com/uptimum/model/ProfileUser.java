package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

public class ProfileUser {
    @SerializedName("_id")
    private String id;
    @SerializedName("iduser")
    private Users users;
    @SerializedName("profile")
    private Profile profile;
    @SerializedName("education")
    private Education education;
    @SerializedName("placeslive")
    private String placeslive;
    @SerializedName("from")
    private String from;
    @SerializedName("job")
    private String job;

    public ProfileUser(String id, Users users, Profile profile, Education education, String placeslive, String from, String job) {
        this.id = id;
        this.users = users;
        this.profile = profile;
        this.education = education;
        this.placeslive = placeslive;
        this.from = from;
        this.job = job;
    }

    @Override
    public String toString() {
        return "ProfileUser{" +
                "id='" + id + '\'' +
                ", users=" + users +
                ", profile=" + profile +
                ", education=" + education +
                ", placeslive='" + placeslive + '\'' +
                ", from='" + from + '\'' +
                ", job='" + job + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public String getPlaceslive() {
        return placeslive;
    }

    public void setPlaceslive(String placeslive) {
        this.placeslive = placeslive;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
