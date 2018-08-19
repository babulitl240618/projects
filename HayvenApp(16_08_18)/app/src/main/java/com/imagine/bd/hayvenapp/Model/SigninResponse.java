package com.imagine.bd.hayvenapp.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class SigninResponse implements Serializable {


    ArrayList<UserInfo> alluserlist = new ArrayList<>();
    private UserInfo user=new UserInfo();
    ArrayList<ConversationData> conversations = new ArrayList<>();
    ArrayList<ChatUserInfo> myUserList = new ArrayList<>();
    private String msg = "";


    public SigninResponse(ArrayList<UserInfo> alluserlist, UserInfo user, ArrayList<ConversationData> conversations, ArrayList<ChatUserInfo> myUserList, String msg) {
        this.alluserlist = alluserlist;
        this.user = user;
        this.conversations = conversations;
        this.myUserList = myUserList;
        this.msg = msg;
    }

    public ArrayList<UserInfo> getAlluserlist() {
        return alluserlist;
    }

    public void setAlluserlist(ArrayList<UserInfo> alluserlist) {
        this.alluserlist = alluserlist;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public ArrayList<ConversationData> getConversations() {
        return conversations;
    }

    public void setConversations(ArrayList<ConversationData> conversations) {
        this.conversations = conversations;
    }

    public ArrayList<ChatUserInfo> getUnreadConv() {
        return myUserList;
    }

    public void setUnreadConv(ArrayList<ChatUserInfo> myUserList) {
        this.myUserList = myUserList;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
