package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

public class UserLike {
    @SerializedName("iduserLike")
    private String iduserlike;
    @SerializedName("usernameLike")
    private String nameuserlike;
    @SerializedName("typeLike")
    private int typeLike;

    public UserLike(String iduserlike, String nameuserlike, int typeLike) {
        this.iduserlike = iduserlike;
        this.nameuserlike = nameuserlike;
        this.typeLike = typeLike;
    }

    @Override
    public String toString() {
        return "UserLike{" +
                "iduserlike='" + iduserlike + '\'' +
                ", nameuserlike='" + nameuserlike + '\'' +
                ", typeLike=" + typeLike +
                '}';
    }

    public String getIduserlike() {
        return iduserlike;
    }

    public void setIduserlike(String iduserlike) {
        this.iduserlike = iduserlike;
    }

    public String getNameuserlike() {
        return nameuserlike;
    }

    public void setNameuserlike(String nameuserlike) {
        this.nameuserlike = nameuserlike;
    }

    public int getTypeLike() {
        return typeLike;
    }

    public void setTypeLike(int typeLike) {
        this.typeLike = typeLike;
    }
}
