package com.intern.myapplication.Model;

public class CheckUserResponse {
    boolean exists;
    String error_msg;

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public CheckUserResponse(boolean exists, String error_msg) {
        this.exists = exists;
        this.error_msg = error_msg;
    }

    public CheckUserResponse()
    {

    }
}
