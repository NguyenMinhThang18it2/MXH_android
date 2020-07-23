package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

public class Following {
    @SerializedName("iduserFollowing")
    private Users iduserFollowing;

    public Following(Users iduserFollowing) {
        this.iduserFollowing = iduserFollowing;
    }

    @Override
    public String toString() {
        return "Following{" +
                "iduserFollowing=" + iduserFollowing +
                '}';
    }

    public Users getIduserFollowing() {
        return iduserFollowing;
    }

    public void setIduserFollowing(Users iduserFollowing) {
        this.iduserFollowing = iduserFollowing;
    }
}
