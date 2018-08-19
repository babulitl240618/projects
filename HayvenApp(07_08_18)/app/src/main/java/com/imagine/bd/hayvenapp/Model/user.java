package com.imagine.bd.hayvenapp.Model;

public class user {
    private String email="";
    private String password="";
    private String gcm_id="";

    public user(String email, String password, String gcm_id) {
        this.email = email;
        this.password = password;
        this.gcm_id = gcm_id;
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

    public String getGcm_id() {
        return gcm_id;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }
}
