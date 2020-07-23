package thang.com.uptimum.model;

import com.google.gson.annotations.SerializedName;

public class Error {
    @SerializedName("success")
    private boolean success;
    @SerializedName("msg")
    private String msg;

    public Error(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Error{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
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
}
