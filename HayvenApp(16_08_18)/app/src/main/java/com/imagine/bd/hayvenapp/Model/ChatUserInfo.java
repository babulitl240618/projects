package com.imagine.bd.hayvenapp.Model;

public class ChatUserInfo {
    private String conversation_id="";
    private String conversation_type="";
    private String conversation_title="";
    private String created_at="";
    private String msg_body="";
    private String msg_id="";
    private String msg_type="";
    private String sender_img="";
    private String sender_name="";
    private String totalUnread="";

    public ChatUserInfo() {
    }

    public ChatUserInfo(String conversation_id, String conversation_type, String conversation_title, String created_at, String msg_body, String msg_id, String msg_type, String sender_img, String sender_name, String totalUnread) {
        this.conversation_id = conversation_id;
        this.conversation_type = conversation_type;
        this.conversation_title = conversation_title;
        this.created_at = created_at;
        this.msg_body = msg_body;
        this.msg_id = msg_id;
        this.msg_type = msg_type;
        this.sender_img = sender_img;
        this.sender_name = sender_name;
        this.totalUnread = totalUnread;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getConversation_type() {
        return conversation_type;
    }

    public void setConversation_type(String conversation_type) {
        this.conversation_type = conversation_type;
    }

    public String getConversation_title() {
        return conversation_title;
    }

    public void setConversation_title(String conversation_title) {
        this.conversation_title = conversation_title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getMsg_body() {
        return msg_body;
    }

    public void setMsg_body(String msg_body) {
        this.msg_body = msg_body;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getSender_img() {
        return sender_img;
    }

    public void setSender_img(String sender_img) {
        this.sender_img = sender_img;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getTotalUnread() {
        return totalUnread;
    }

    public void setTotalUnread(String totalUnread) {
        this.totalUnread = totalUnread;
    }


}
