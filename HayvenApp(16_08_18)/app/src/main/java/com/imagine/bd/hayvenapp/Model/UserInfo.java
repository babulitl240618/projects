package com.imagine.bd.hayvenapp.Model;

public class UserInfo {

    private String id="";
    private String dept="";
    private String designation="";
    private String email="";
    private String fullname="";
    private String img="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public UserInfo(String id, String dept, String designation, String email, String fullname, String img) {
        this.id = id;
        this.dept = dept;
        this.designation = designation;
        this.email = email;
        this.fullname = fullname;

        this.img = img;
    }

    public UserInfo() {
    }
}
