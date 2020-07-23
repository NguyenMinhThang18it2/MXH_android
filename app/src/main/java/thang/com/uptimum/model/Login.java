package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("success")
    private boolean success;
    @SerializedName("msg")
    private String msg;
    @SerializedName("user")
    private Users users;

    public Login(boolean success, String msg, Users users) {
        this.success = success;
        this.msg = msg;
        this.users = users;
    }

    @Override
    public String toString() {
        return "Login{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", users=" + users +
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
}
