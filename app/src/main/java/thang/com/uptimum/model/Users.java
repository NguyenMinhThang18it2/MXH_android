package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

public class Users {
    @SerializedName("_id")
    private String id;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("avata")
    private String avata;
    @SerializedName("coverimage")
    private String coverimage;
    @SerializedName("token")
    private String token;

    public Users(String id, String username, String email, String password, String avata, String coverimage, String token) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.avata = avata;
        this.coverimage = coverimage;
        this.token = token;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", avata='" + avata + '\'' +
                ", coverimage='" + coverimage + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvata() {
        return avata;
    }

    public void setAvata(String avata) {
        this.avata = avata;
    }

    public String getCoverimage() {
        return coverimage;
    }

    public void setCoverimage(String coverimage) {
        this.coverimage = coverimage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
