package com.imagine.bd.hayvenapp.utils;


import com.imagine.bd.hayvenapp.Model.ChatUserInfo;
import com.imagine.bd.hayvenapp.Model.ContactsData;
import com.imagine.bd.hayvenapp.Model.UserInfo;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by Shohel on 2/15/2016.
 */
public class AppConstant {

    public static String isSignin = "false";
    public static String userName = "userName";
    public static String userId = "userId";
    public static String userDesignation = "userDesignation";
    public static String userImg = "userImg";
    //chat head
    public static String userImgChatHead = "userImgChatHead";
    public static String userTextChatHead = "userTextChatHead";

    public static String userPhoto = "userPhoto";
    public static String firebaseFriendId = "firebaseFriendId";


    public static String senderid = "senderid";
    public static String lastConversation_id = "lastConversation_id";
    public static String badgeCount = "badgeCount";
    public static String reciverid = "reciverid";
    public static String request_call_type = "request_call_type";

    public static String call_audio_vedio = "call_audio_vedio";
    public static String conversation_id = "conversation_id";

    public static String API_TOKEN = "";
  /*  public static String firebase_token="firebase_token";
    public static String friend_name = "friend_name";
    public static String rom_id = "room_id";*/

    public static String firebase_token = "firebase_token";
    public static String friend_name = "friend_name";
    public static String rom_id = "rom_id";
    public static ArrayList<ContactsData> addmemberlist = new ArrayList<>();

    /*
    public static ArrayList<ChatUserInfo> unreadConv = new ArrayList<>();
    public static ArrayList<UserInfo> alluserlist = new ArrayList<>();
    */

    public static List<String> onlinelist = new ArrayList<>();

/*

    // find  name by id
    public static String findNameById(String value) {
        UserInfo pd;
        String data = "";
        for (int i = 0; AppConstant.alluserlist.size() > i; i++) {
            pd = AppConstant.alluserlist.get(i);
            if (pd.getId().equalsIgnoreCase(value)) {
                data = pd.getFullname();
            }
        }
        return data;
    }


    // find  id  by user name
    public static String findIdbyName(String value) {
        UserInfo pd;
        String data = "";
        for (int i = 0; AppConstant.alluserlist.size() > i; i++) {
            pd = AppConstant.alluserlist.get(i);
            if (pd.getFullname().equalsIgnoreCase(value)) {
                data = pd.getId();
            }
        }
        return data;
    }


    // find  indicator_id  by user email
    public static String findImgById(String value) {
        UserInfo pd;
        String data = "";
        for (int i = 0; AppConstant.alluserlist.size() > i; i++) {
            pd = AppConstant.alluserlist.get(i);
            if (pd.getId().equalsIgnoreCase(value)) {
                data = pd.getImg();
            }
        }
        return data;
    }
*/

    public static String GenerateId() {
        int randomId = 0;
        Random rand = new Random();
        for (int j = 0; j < 10; j++) {
            randomId = (int) rand.nextLong();
        }
        String string_id = String.valueOf(randomId);
        return string_id;
    }

   /* public static String getConvertedTime(String datetime) {


        final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf1.setTimeZone(TimeZone.getTimeZone("UTC")); // This line converts the given date into UTC time zone
        java.util.Date dateObj = null;
        try {
            dateObj = sdf1.parse(datetime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String time = new SimpleDateFormat("K:mm a").format(dateObj);


        return "";
    }*/


    // string to converted date formate function
    public static String getConvertedTime(String inputDate) {


        final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf1.setTimeZone(TimeZone.getTimeZone("UTC")); // This line converts the given date into UTC time zone
        java.util.Date dateObj = null;
        try {
            dateObj = sdf1.parse(inputDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String time = new SimpleDateFormat("K:mm a").format(dateObj);



        DateFormat theDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;

        try {
            date = theDateFormat.parse(inputDate);
        } catch (ParseException parseException) {
            // Date is invalid. Do what you want.
        } catch (Exception exception) {
            // Generic catch. Do what you want.
        }

        // theDateFormat = new SimpleDateFormat("EEE, dd MMM, yyyy");

        theDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String timeOnly = "";

       /* try {
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            theDateFormat.setTimeZone(utcZone);// Set UTC time zone
            Date myDate = theDateFormat.parse(inputDate);
            theDateFormat.setTimeZone(TimeZone.getDefault());// Set device time zone
            timeOnly = theDateFormat.format(myDate);
        } catch (Exception e) {
            e.printStackTrace();
        }*/


// +" AT "+ time

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return  time;
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return theDateFormat.format(date);
        } else {
            return theDateFormat.format(date);
        }
    }


}
