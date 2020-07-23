package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

public class Profile {
    @SerializedName("phone")
    private String phone;
    @SerializedName("dateofbirth")
    private String dateofbirth;
    @SerializedName("nickname")
    private String nickname;

    public Profile(String phone, String dateofbirth, String nickname) {
        this.phone = phone;
        this.dateofbirth = dateofbirth;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "phone='" + phone + '\'' +
                ", dateofbirth='" + dateofbirth + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
