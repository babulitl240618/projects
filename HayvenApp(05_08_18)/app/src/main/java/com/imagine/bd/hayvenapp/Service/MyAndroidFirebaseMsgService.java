package com.imagine.bd.hayvenapp.Service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.google.gson.JsonObject;
import com.imagine.bd.hayvenapp.Activity.IncomingCallActivity;
import com.imagine.bd.hayvenapp.MainActivity2;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.Config;
import com.imagine.bd.hayvenapp.utils.NotificationUtils;
import com.imagine.bd.hayvenapp.utils.PersistData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;


public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";
    private NotificationUtils notificationUtils;
    public static int OVERLAY_PERMISSION_REQ_CODE_CHATHEAD = 1234;
    public static int OVERLAY_PERMISSION_REQ_CODE_CHATHEAD_MSG = 5678;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

       /* sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));

        //Log data to Log Cat
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e("work", "test recive: " + remoteMessage.getFrom());
        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().toString());

            //   String data="{ \"name\":\"jubayer\", \"room_id\":\"123\", \"firebase-token\":\"wewew\" }";
            String data = remoteMessage.getNotification().getBody();

          *//*  String room_id="";

            try {
                JSONObject json = new JSONObject(data);
                room_id=json.getString("room_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }*//*

            Log.e("bodytest", "df");
            // send notification to all user (global) from here.
            //    handleNotification(data);
        }*/

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            String data = remoteMessage.getData().toString();

           /* HashMap<String, String> holder = new HashMap();
            String payload = "cossn=0, abc=hello/=world";
            String[] keyVals = payload.split(", ");
            for(String keyVal:keyVals)
            {
                String[] parts = keyVal.split("=",2);
                holder.put(parts[0],parts[1]);
            }*/

            Log.e("bodytest", "abc");
            String roomId = "";
            String sender = "";
            String reciver = "";

            String calling_answer_type = ""; /* recive/incoming or reject*/

            for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (key.equalsIgnoreCase("msg")) {
                    roomId = value;
                }
                if (key.equalsIgnoreCase("sender_id")) {
                    sender = value;
                    PersistData.setStringData(getApplicationContext(), AppConstant.firebaseFriendId, sender);
                }

                if (key.equalsIgnoreCase("reciver_id")) {
                    reciver = value;
                }

                if (key.equalsIgnoreCase("call_type")) {
                    calling_answer_type = value;
                }

                Log.e("hello", "key, " + key + " value " + value);


               /* if (key == "msg") {
                    roomId=value;
                }*/
            }

            JSONObject ob = null;
            String room="";
            String calling_type=""; /* audio or vedio*/
            try {
                ob = new JSONObject(roomId);
                 room = ob.getString("room"); // getting value CAD with key from
                calling_type = ob.getString("type"); // getting value CAD with key from

            } catch (JSONException e) {
                e.printStackTrace();
            }

            /* store senderid and reciver id
            * after get notification, senderid is set as a friend id or reciver id*/
           // PersistData.setStringData(getApplicationContext(),AppConstant.senderid,reciver);
            PersistData.setStringData(getApplicationContext(),AppConstant.reciverid,sender);
            /* call type store audio or vedio */
            PersistData.setStringData(getApplicationContext(),AppConstant.request_call_type,calling_type);

            PersistData.setStringData(getApplicationContext(),AppConstant.call_audio_vedio,calling_type);
            PersistData.setStringData(getApplicationContext(),AppConstant.rom_id,room);

            Log.e("777",PersistData.getStringData(getApplicationContext(),AppConstant.request_call_type));


            //  data.replace("{data={\"message\":","");
            // String final_data=  data.substring(17);
            Log.e(TAG, "Data : " + data);
            Log.e(TAG, "room : " + room);
            Log.e(TAG, "type_audio_vedio : " + calling_type);
            Log.e(TAG, "type_audio_vedio sf: " + PersistData.getStringData(getApplicationContext(),AppConstant.call_audio_vedio));
            Log.e(TAG, "sender : " + sender);
            Log.e(TAG, "sender sf: " + PersistData.getStringData(getApplicationContext(), AppConstant.firebaseFriendId));
            Log.e(TAG, "reciver : " + reciver);
            Log.e(TAG, "call_type : " + calling_answer_type);

           /* String room_id="";

            try {
                JSONObject json = new JSONObject(data);
                room_id=json.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

/*

            Intent dialogIntent = new Intent(this, IncomingCallActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            dialogIntent.putExtra("calling_type",calling_type);
            startActivity(dialogIntent);



           /* Intent intent = new Intent("com.journaldev.broadcastreceiver.SOME_ACTION");
            sendBroadcast(intent);*/
            // send notification to all user (global) from here.

            if(calling_answer_type.equalsIgnoreCase(Config.INCOMING_CALL)){


                Intent pushNotification = new Intent(this, IncomingCallActivity.class);
                pushNotification.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                pushNotification.putExtra("message", room);
                pushNotification.putExtra("sender", sender);
                pushNotification.putExtra("reciver", reciver);
                pushNotification.putExtra("calltype", calling_answer_type);
                pushNotification.putExtra("type", calling_type);

                startActivity(pushNotification);

            }else{
                handleNotification(room,sender,reciver,calling_answer_type,calling_type);
            }


          //  handleNotification(room,sender,reciver,calling_answer_type,calling_type);

        }
    }


    private void sendNotification(String messageTitle, String messageBody) {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long[] pattern = {500, 500, 500, 500, 500};

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.profile_pic)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.BLUE, 1, 1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void handleNotification(String message,String sender,String reciver,String calling_answer_type,String calling_type) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            pushNotification.putExtra("sender", sender);
            pushNotification.putExtra("reciver", reciver);
            pushNotification.putExtra("calltype", calling_answer_type);
            pushNotification.putExtra("type", calling_type);

            Log.e("pusher2", "sdji");
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);


        } else {
            // If the app is in background, firebase itself handles the notification

         /*   Intent notificationIntent = new Intent(getApplicationContext(), IncomingCallActivity.class);
            getApplicationContext().startActivity(notificationIntent);*/

        }
    }

    private void handleMessage(String json) {

        // check app id background !

       /* if(getCurrentActivity().equalsIgnoreCase("com.imaginebd.navigateconnect.Activity.ChatingActivity")){

        }*/






       /*

        Gson g = new Gson();
        NotificationResponse nr = g.fromJson(new String(json), NotificationResponse.class);
        String sender_email = nr.getMessage().getData().getGroupId();

        // store sender email for knowing friend imail id in chating activity.
        PersistData.setStringData(getApplicationContext(), AppConstant.freind_email_id, sender_email);
        PersistData.setStringData(getApplicationContext(), AppConstant.person_pic_url, nr.getMessage().getData().getFimg());

        // play notification sound
        notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext()) && getCurrentActivity().equalsIgnoreCase("com.imaginebd.navigateconnect.Activity.ChatingActivity")) {

            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", json);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);


        } else {

            // add notification into  arraylist and count recent notification
            AppConstant.notificationList.add(nr.getMessage().getData());

            // This is for bladge on home icon
            ShortcutBadger.applyCount(MyAndroidFirebaseMsgService.this, AppConstant.notificationList.size());

            if (getCurrentActivity().equalsIgnoreCase("com.imaginebd.navigateconnect.Activity.NavigationDrawerActivity")) {

                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", json);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            }

            // create custom notification design for tray
            int icon = R.drawable.profile_pic;
            long when = System.currentTimeMillis();
            Notification notification = new Notification(icon, "Navigate Connect Notification", when);
            // Cancel the notification after its selected
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_push_design);
            contentView.setImageViewResource(R.id.image, R.drawable.nav_icon);

            switch (nr.getMessage().getData().getMessageType()) {
                case "Direct":
                    PersistData.setStringData(getApplicationContext(), AppConstant.fromActivity, "contact_chat");
                    PersistData.setStringData(getApplicationContext(), AppConstant.person_pic_url, nr.getMessage().getData().getFimg());
                    PersistData.setStringData(getApplicationContext(), AppConstant.chating_person, nr.getMessage().getData().getFname());
                    contentView.setTextViewText(R.id.title, nr.getMessage().getData().getFname() + " send you a message.");
                    break;
                case "Project":
                    // find project id.
                    int pid = Integer.parseInt(nr.getMessage().getData().getGroupId()) - 99999999;
                    String projectId = String.valueOf(pid);
                    String projectName = AppConstant.finderProjectName(projectId);

                    PersistData.setStringData(getApplicationContext(), AppConstant.fromActivity, "project_chat");
                    PersistData.setStringData(getApplicationContext(), AppConstant.person_pic_url, "http://27.147.195.222:2241/nc27/uploads/chat_attachment/14971734860.png");
                    PersistData.setStringData(getApplicationContext(), AppConstant.chating_person, projectName);
                    contentView.setTextViewText(R.id.title, nr.getMessage().getData().getFname() + " send a message in project " + projectName);
                    break;
                case "Group":
                    String groupName = AppConstant.finderGroupName(nr.getMessage().getData().getGroupId());
                    PersistData.setStringData(getApplicationContext(), AppConstant.fromActivity, "group_chat");
                    PersistData.setStringData(getApplicationContext(), AppConstant.chating_person, groupName);
                    PersistData.setStringData(getApplicationContext(), AppConstant.person_pic_url, "http://27.147.195.222:2241/nc27/uploads/chat_attachment/14971734980.png");
                    contentView.setTextViewText(R.id.title, nr.getMessage().getData().getFname() + " send a message in group " + groupName);
                    break;
            }

            contentView.setTextViewText(R.id.text, nr.getMessage().getData().getMsg());
            notification.contentView = contentView;


            if (NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                PersistData.setStringData(getApplicationContext(), AppConstant.notificationcClick, "PUSH");
                switch (PersistData.getStringData(getApplicationContext(), AppConstant.isSignin)) {
                    case "false":
                        Intent notificationIntent = new Intent(this, SigninActivity.class);
                        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
                        notification.contentIntent = contentIntent;
                        break;
                    case "true":
                        Intent notificationIntent2 = new Intent(this, ChatingActivity.class);
                        PendingIntent contentIntent2 = PendingIntent.getActivity(this, 0, notificationIntent2, 0);
                        notification.contentIntent = contentIntent2;
                        break;
                }


            } else {

                if (getCurrentActivity().equalsIgnoreCase("com.imaginebd.navigateconnect.Activity.SigninActivity")) {

                 } else {
                    Intent notificationIntent = new Intent(this, ChatingActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
                    notification.contentIntent = contentIntent;
                }

            }



            //  notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
            notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
            notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
            notification.defaults |= Notification.DEFAULT_SOUND; // Sound
            mNotificationManager.notify(1, notification);
            // end custom notification design

            startService(new Intent(getApplicationContext(), NavigetChatHead.class));
}

        */

    }


    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }


    private String decodeString(String encoded) {
        byte[] dataDec = Base64.decode(encoded, Base64.DEFAULT);
        String decodedString = "";
        try {

            decodedString = new String(dataDec, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } finally {

            return decodedString;
        }
    }

    public String getCurrentActivity() {

        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        String activity = taskInfo.get(0).topActivity.getClassName();
        Log.e(" Check activity ", "CURRENT Activity ::" + activity + "   Package Name :  " + componentInfo.getPackageName());

        return activity;
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.profile_pic : R.drawable.profile_pic;
    }
}