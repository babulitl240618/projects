package com.imagine.bd.hayvenapp.Model;

import java.util.ArrayList;

public class ConversationData {

    private String conversation_id;
    private String archive;
    private String created_at;
    private String created_by;
    private String group;
    private String group_keyspace;
    private String guests;
    private ArrayList<String> participants=new ArrayList<>();
    private ArrayList<String> participants_admin=new ArrayList<>();
    private ArrayList<String> participants_guest=new ArrayList<>();
    private String privacy;
    private String single;
    private String title;

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup_keyspace() {
        return group_keyspace;
    }

    public void setGroup_keyspace(String group_keyspace) {
        this.group_keyspace = group_keyspace;
    }

    public String getGuests() {
        return guests;
    }

    public void setGuests(String guests) {
        this.guests = guests;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }

    public ArrayList<String> getParticipants_admin() {
        return participants_admin;
    }

    public void setParticipants_admin(ArrayList<String> participants_admin) {
        this.participants_admin = participants_admin;
    }

    public ArrayList<String> getParticipants_guest() {
        return participants_guest;
    }

    public void setParticipants_guest(ArrayList<String> participants_guest) {
        this.participants_guest = participants_guest;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getSingle() {
        return single;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
