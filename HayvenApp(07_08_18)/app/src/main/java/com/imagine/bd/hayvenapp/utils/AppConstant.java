package com.imagine.bd.hayvenapp.utils;



import com.imagine.bd.hayvenapp.Model.ContactsData;
import com.imagine.bd.hayvenapp.Model.UserInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Shohel on 2/15/2016.
 */
public class AppConstant {

    public static String isSignin = "false";
    public static String userName = "userName";
    public static String userId = "userId";
    public static String userDesignation = "userDesignation";
    public static String userImg = "userImg";
    public static String userPhoto = "userPhoto";
    public static String firebaseFriendId = "firebaseFriendId";



    public static String senderid = "senderid";
    public static String reciverid = "reciverid";
    public static String request_call_type = "request_call_type";

    public static String call_audio_vedio="call_audio_vedio";
    public static String conversation_id="conversation_id";

    public static String API_TOKEN = "";
  /*  public static String firebase_token="firebase_token";
    public static String friend_name = "friend_name";
    public static String rom_id = "room_id";*/

    public static String firebase_token="firebase_token";
    public static String friend_name = "friend_name";
    public static String rom_id = "rom_id";
    public static ArrayList<ContactsData> addmemberlist = new ArrayList<>();
    public static ArrayList<UserInfo> alluserlist = new ArrayList<>();
    public static  List<String> onlinelist = new ArrayList<>();


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

    public static String GenerateId() {
        int randomId = 0;
        Random rand = new Random();
        for (int j = 0; j < 10; j++) {
            randomId = (int) rand.nextLong();
        }
        String string_id = String.valueOf(randomId);
        return string_id;
    }

}
