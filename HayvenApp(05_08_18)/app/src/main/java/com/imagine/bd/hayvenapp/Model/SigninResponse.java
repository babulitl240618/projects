package com.imagine.bd.hayvenapp.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class SigninResponse implements Serializable {


    ArrayList<UserInfo> alluserlist = new ArrayList<>();
    ArrayList<ConversationData> conversations = new ArrayList<>();
    private String msg = "";
    private UserInfo user=new UserInfo();

    public SigninResponse(ArrayList<UserInfo> alluserlist, ArrayList<ConversationData> conversations, String msg, UserInfo user) {
        this.alluserlist = alluserlist;
        this.conversations = conversations;
        this.msg = msg;
        this.user = user;
    }


    public ArrayList<UserInfo> getAlluserlist() {
        return alluserlist;
    }

    public void setAlluserlist(ArrayList<UserInfo> alluserlist) {
        this.alluserlist = alluserlist;
    }

    public ArrayList<ConversationData> getConversations() {
        return conversations;
    }

    public void setConversations(ArrayList<ConversationData> conversations) {
        this.conversations = conversations;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
