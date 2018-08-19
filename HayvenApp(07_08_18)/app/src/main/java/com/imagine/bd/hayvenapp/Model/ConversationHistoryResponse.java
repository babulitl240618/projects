package com.imagine.bd.hayvenapp.Model;

import java.util.ArrayList;

public class ConversationHistoryResponse {

    private String status;
    private String conversation_id;
    ArrayList<MessageData> result=new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<MessageData> getResult() {
        return result;
    }

    public void setResult(ArrayList<MessageData> result) {
        this.result = result;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }
}
