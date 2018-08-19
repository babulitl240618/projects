package com.imagine.bd.hayvenapp.utils;

/**
 * Created by User on 4/10/2017.
 */


public class API_URL {
    //  public static final String PHOTO_BASE_URL = "http://172.16.0.64/nc27/asset/img/avatars/";
    public static final String PHOTO_BASE_URL = "http://27.147.195.222:2265/images/users/";
    public static final String NEWS_BASE_URL = "https://newsapi.org/v1/articles?source=";

    //  public static final  String BASE_URL="http://172.16.0.64/nc27/API/"; // local server
    // public static final String BASE_URL = "http://27.147.195.222:2241/nc27/API/";// online server

   // public static final  String BASE_URL="http://172.16.3.72:5000/hvandroid/"; // local server mahfug
    public static final String BASE_URL = "http://27.147.195.222:2265/hvandroid/"; // global server
    public static final String BASE_URL_M = "http://27.147.195.222:2265/hvandroid/"; // global server

    // public static final  String BASE_URL="http://208.82.90.170:4480/API/"; // canada server
    public static final String RETROFIT_BASE_URL = "http://27.147.195.222:2241/nc27/";
    public static final String SIGNIN_URL = BASE_URL + "Login/signin";
    public static final String LOGOUT_URL = BASE_URL + "Login/logout";
    public static final String MYFEED_URL = BASE_URL + "Feed/getNotificationStatusAll";
    public static final String FEED_QUICK_REPLY = BASE_URL + "Feed/insertCmnt";
    public static final String CHAT_LIST = BASE_URL + "chat/contactlists";
    public static final String CHAT_HISTORY_LIST = BASE_URL + "chat/chatting_history";
    public static final String CHAT_SEND_MSG = BASE_URL + "chat/send_chat_msg";
    public static final String CHAT_UPLOAD_ATTACHMENT = BASE_URL + "chat/send_attach_msg";
    public static final String GET_API_TOKEN = BASE_URL + "Login/GetApiToken";

    public static final String CHAT_DELETE_MSG = BASE_URL + "chat/delete_chat";
    public static final String CHAT_FAVOURITE_MSG = BASE_URL + "chat/favourite";
    public static final String CHAT_Block_Unblock = BASE_URL + "chat/block_contact";
    public static final String CHAT_MUTE = BASE_URL + "chat/mute_conversation";
    public static final String CHAT_UNMUTE = BASE_URL + "chat/unmute_conversation";
    public static final String CHAT_Conversation_List_URL = BASE_URL + "chat/chat_notification_latest";
    public static final String CHAT_CONVERSATION_DELETE = BASE_URL + "chat/clear_chat";
    public static final String ALL_TASK_LIST = BASE_URL + "chat/all_issues";
}
