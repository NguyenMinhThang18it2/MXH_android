package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("success")
    private boolean success;
    @SerializedName("msg")
    private String msg;
    @SerializedName("user")
    private Users users;
    @SerializedName("token")
    private String token;

    public Login(boolean success, String msg, Users users, String token) {
        this.success = success;
        this.msg = msg;
        this.users = users;
        this.token = token;
    }

    @Override
    public String toString() {
        return "Login{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", users=" + users +
                ", token='" + token + '\'' +
                '}';
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
