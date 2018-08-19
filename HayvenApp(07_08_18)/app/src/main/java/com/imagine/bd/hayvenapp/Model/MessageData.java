package com.imagine.bd.hayvenapp.Model;

import java.util.ArrayList;

public class MessageData {
    private String msg_id;
    private ArrayList<String> attch_audiofile;
    private ArrayList<String> attch_imgfile;
    private ArrayList<String> attch_otherfile;
    private ArrayList<String> attch_videofile;
    private String call_duration;
    private String conversation_id;
    private String created_at;
    private ArrayList<String> has_delete;
    private ArrayList<String> has_flagged;
    private String has_reply;
    private String has_tag_text;
    private String last_reply_time;
    private String msg_body;
    private ArrayList<String> msg_status;
    private String msg_type;
    private String sender;
    private String sender_img;
    private String sender_name;

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public ArrayList<String> getAttch_audiofile() {
        return attch_audiofile;
    }

    public void setAttch_audiofile(ArrayList<String> attch_audiofile) {
        this.attch_audiofile = attch_audiofile;
    }

    public ArrayList<String> getAttch_imgfile() {
        return attch_imgfile;
    }

    public void setAttch_imgfile(ArrayList<String> attch_imgfile) {
        this.attch_imgfile = attch_imgfile;
    }

    public ArrayList<String> getAttch_otherfile() {
        return attch_otherfile;
    }

    public void setAttch_otherfile(ArrayList<String> attch_otherfile) {
        this.attch_otherfile = attch_otherfile;
    }

    public ArrayList<String> getAttch_videofile() {
        return attch_videofile;
    }

    public void setAttch_videofile(ArrayList<String> attch_videofile) {
        this.attch_videofile = attch_videofile;
    }

    public String getCall_duration() {
        return call_duration;
    }

    public void setCall_duration(String call_duration) {
        this.call_duration = call_duration;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ArrayList<String> getHas_delete() {
        return has_delete;
    }

    public void setHas_delete(ArrayList<String> has_delete) {
        this.has_delete = has_delete;
    }

    public ArrayList<String> getHas_flagged() {
        return has_flagged;
    }

    public void setHas_flagged(ArrayList<String> has_flagged) {
        this.has_flagged = has_flagged;
    }

    public String getHas_reply() {
        return has_reply;
    }

    public void setHas_reply(String has_reply) {
        this.has_reply = has_reply;
    }

    public String getHas_tag_text() {
        return has_tag_text;
    }

    public void setHas_tag_text(String has_tag_text) {
        this.has_tag_text = has_tag_text;
    }

    public String getLast_reply_time() {
        return last_reply_time;
    }

    public void setLast_reply_time(String last_reply_time) {
        this.last_reply_time = last_reply_time;
    }

    public String getMsg_body() {
        return msg_body;
    }

    public void setMsg_body(String msg_body) {
        this.msg_body = msg_body;
    }

    public ArrayList<String> getMsg_status() {
        return msg_status;
    }

    public void setMsg_status(ArrayList<String> msg_status) {
        this.msg_status = msg_status;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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
}
