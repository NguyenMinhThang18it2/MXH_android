package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

public class Followers {
    @SerializedName("iduserFollowers")
    private Users iduserFollowers;

    public Followers(Users iduserFollowers) {
        this.iduserFollowers = iduserFollowers;
    }

    @Override
    public String toString() {
        return "Followers{" +
                "iduserFollowers=" + iduserFollowers +
                '}';
    }

    public Users getIduserFollowers() {
        return iduserFollowers;
    }

    public void setIduserFollowers(Users iduserFollowers) {
        this.iduserFollowers = iduserFollowers;
    }
}
