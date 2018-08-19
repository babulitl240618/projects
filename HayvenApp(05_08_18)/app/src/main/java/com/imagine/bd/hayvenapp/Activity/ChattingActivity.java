package com.imagine.bd.hayvenapp.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.imagine.bd.hayvenapp.Adapter.ChatAppMsgAdapter;
import com.imagine.bd.hayvenapp.AppRTCClient;
import com.imagine.bd.hayvenapp.CallActivity;
import com.imagine.bd.hayvenapp.Database.MyDbHelper;
import com.imagine.bd.hayvenapp.Model.ChatAppMsgDTO;
import com.imagine.bd.hayvenapp.Model.SigninResponse;
import com.imagine.bd.hayvenapp.Model.UserInfo;
import com.imagine.bd.hayvenapp.MyApplication;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.Retrofit.IRetrofit;
import com.imagine.bd.hayvenapp.Retrofit.ServiceGenerator;
import com.imagine.bd.hayvenapp.utils.API_URL;
import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.Config;
import com.imagine.bd.hayvenapp.utils.NotificationUtils;
import com.imagine.bd.hayvenapp.utils.PersistData;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChattingActivity extends AppCompatActivity {


    /*Ui property for this class*/
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ImageView imggroupcall, imageView1, imgmenu, imggroupvideocall;
    private TextView tvName, create_conversation;
    ChatAppMsgAdapter chatAppMsgAdapter;
    EditText msgInputText;
    private Socket mSocket;
    RecyclerView msgRecyclerView;
    LinearLayout linearLayout, linearLayout1;
    List<ChatAppMsgDTO> msgDtoList;
    private Context con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        con = this;
        initUi();
        MyApplication app = (MyApplication) getApplication();
        mSocket = app.getSocket();
       // mSocket.on("recivemsg", onLogin);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    // FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    String calltype = intent.getStringExtra("calltype");


                    if (calltype.equalsIgnoreCase(Config.INCOMING_CALL)) {
                        startActivity(new Intent(con, IncomingCallActivity.class));
                    } else {
                        Toast.makeText(con, "cancel call", Toast.LENGTH_LONG).show();
                    }

                    //  startActivity(new Intent(con, IncomingCallActivity.class));

                    // connectToRoom1(message, false, false, false, 0);

                    //   Toast.makeText(con, "Push notification: " + message, Toast.LENGTH_LONG).show();

                    PersistData.setStringData(con, AppConstant.rom_id, message);

                }
            }
        };


    }

    public void defultValueadd() {


        // Add a new sent message to the list.

        ChatAppMsgDTO msgDto1 = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, "Perfect!");
        ChatAppMsgDTO msgDto2 = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, "Amy and I are going for pho.Wanna join?");
        ChatAppMsgDTO msgDto3 = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, "Guess I am working late tonight.");
        msgDtoList.add(msgDto1);
        msgDtoList.add(msgDto2);
        msgDtoList.add(msgDto3);


          /*  int newMsgPosition = msgDtoList.size() - 1;

            // Scroll RecyclerView to the last message.
            msgRecyclerView.scrollToPosition(newMsgPosition);

            // Empty the input edit text box.
            msgInputText.setText("");*/

    }


    public void requesCall(String senderid, String reciver_id, String call_type, String msg) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sender_id", senderid);
        jsonObject.addProperty("call_type", call_type);
        jsonObject.addProperty("reciver_id", reciver_id);
        jsonObject.addProperty("msg", msg);

        Log.e("tstdata", jsonObject.toString());

        // Using the Retrofit
        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class, API_URL.BASE_URL);
        Call<JsonObject> call = jsonPostService.sendCall(jsonObject);
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    Log.e("response-success", response.body().toString());

                  /*
                    JsonObject ob=new JsonObject();
                    JsonParser parser = new JsonParser();
                    JsonElement mJson = parser.parse(response.body().toString());
                    Gson gson = new Gson();
                    SigninResponse object = gson.fromJson(mJson, SigninResponse.class);

                  */


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("response-failure", call.toString());
            }

        });
    }

    public void initUi() {


        imageView1 = (ImageView) findViewById(R.id.notificationChat);
        imgmenu = (ImageView) findViewById(R.id.menu);
        linearLayout1 = (LinearLayout) findViewById(R.id.searchsms);
        linearLayout = (LinearLayout) findViewById(R.id.lin_sms);
        create_conversation = (TextView) findViewById(R.id.create_conversation);
        tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(PersistData.getStringData(con, AppConstant.friend_name));

      /*  linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                //imgnewchat.setImageResource(R.drawable.new_chat_black);

            }
        });*/


        imgmenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog
                final Dialog dialog = new Dialog(con);
                dialog.setContentView(R.layout.chat_menu_dialogue);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.TOP | Gravity.RIGHT;

                wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);

                // set the custom dialog components - text, image and button
               /* TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Android custom dialog example!");
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageResource(R.drawable.ic_launcher);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);*/
                // if button is clicked, close the custom dialog
              /*  dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
*/
                dialog.show();
            }
        });


        create_conversation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog
                final Dialog dialog = new Dialog(con);
                dialog.setContentView(R.layout.dialogue_create_conversation);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.BOTTOM | Gravity.CENTER;

                wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);

                // set the custom dialog components - text, image and button
               /* TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Android custom dialog example!");
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageResource(R.drawable.ic_launcher);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);*/
                // if button is clicked, close the custom dialog
              /*  dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
*/
                dialog.show();
            }
        });
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        setTitle("dev2qa.com - Android Chat App Example");

        // Get RecyclerView object.
        msgRecyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);

        // Set RecyclerView layout manager.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(linearLayoutManager);

        // Create the initial data list.
        msgDtoList = new ArrayList<ChatAppMsgDTO>();
        ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, "hello");
        ChatAppMsgDTO msgDto1 = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, "Get as much of that done today as you u can,but we have more then enough bugs to get started");
        ChatAppMsgDTO msgDto2 = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, "ok");
        msgDtoList.add(msgDto);
        msgDtoList.add(msgDto1);
        msgDtoList.add(msgDto2);
        defultValueadd();

        // Create the data adapter with above data list.
        chatAppMsgAdapter = new ChatAppMsgAdapter(msgDtoList, con);

        // Set data adapter to RecyclerView.
        msgRecyclerView.setAdapter(chatAppMsgAdapter);

        msgInputText = (EditText) findViewById(R.id.chat_input_msg);
        imggroupcall = (ImageView) findViewById(R.id.audio);
        imggroupvideocall = (ImageView) findViewById(R.id.vedio);

        imggroupcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  startActivity(new Intent(ChattingActivity.this, GroupCallActivity.class));
                //Intent i = new Intent(context, Contacts.class);
                //startActivity(i);


                if (isOnline()) {

                    String room = AppConstant.GenerateId();

                    JSONObject jsonObj = new JSONObject();

                    try {
                        jsonObj.put("room", room);
                        jsonObj.put("type", "audio");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("jsontst", jsonObj.toString());

                    String msg = jsonObj.toString();


                    //  PersistData.setStringData(context,AppConstant.reciverid, AppConstant.findIdbyName(PersistData.getStringData(con,AppConstant.friend_name)));

                    String senderid = PersistData.getStringData(con, AppConstant.userId);
                    String reciverid = PersistData.getStringData(con, AppConstant.reciverid);

                    if (senderid != null && reciverid != null) {
                        PersistData.setStringData(con, AppConstant.rom_id, room);
                        requesCall(senderid, reciverid, Config.INCOMING_CALL, msg);
                        //  fragment.connectToRoomVedio(room, false, false, false, 0);

                        Intent intent = new Intent(con, CalingActivity.class);

                        intent.putExtra("name", AppConstant.findNameById(reciverid));
                        intent.putExtra("image", AppConstant.findImgById(reciverid));
                        con.startActivity(intent);


                    } else {
                        if (senderid == null) {
                            Toast.makeText(con, "sender id null", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(con, "reciver id  null", Toast.LENGTH_LONG).show();
                        }

                    }


                } else {

                    new AlertDialog.Builder(con)
                            .setTitle("No Internet Connection!")
                            .setMessage("You need to b connected to your network or wifi to make calls.")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }

            }


        });

        imggroupvideocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  startActivity(new Intent(ChattingActivity.this, GroupCallActivity.class));
                //Intent i = new Intent(context, Contacts.class);
                //startActivity(i);


                if (isOnline()) {

                    String room = AppConstant.GenerateId();

                    JSONObject jsonObj = new JSONObject();

                    try {
                        jsonObj.put("room", room);
                        jsonObj.put("type", "vedio");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("jsontst", jsonObj.toString());

                    String msg = jsonObj.toString();


                    //  PersistData.setStringData(context,AppConstant.reciverid, AppConstant.findIdbyName(PersistData.getStringData(con,AppConstant.friend_name)));

                    String senderid = PersistData.getStringData(con, AppConstant.userId);
                    String reciverid = PersistData.getStringData(con, AppConstant.reciverid);

                    if (senderid != null && reciverid != null) {
                        PersistData.setStringData(con, AppConstant.rom_id, room);
                        requesCall(senderid, reciverid, Config.INCOMING_CALL, msg);
                        //  fragment.connectToRoomVedio(room, false, false, false, 0);

                        Intent intent = new Intent(con, CalingActivity.class);

                        con.startActivity(intent);


                    } else {
                        if (senderid == null) {
                            Toast.makeText(con, "sender id null", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(con, "reciver id  null", Toast.LENGTH_LONG).show();
                        }

                    }


                } else {

                    new AlertDialog.Builder(con)
                            .setTitle("No Internet Connection!")
                            .setMessage("You need to b connected to your network or wifi to make calls.")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }

            }


        });

/*
        imggroupvideocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(ChattingActivity.this, GroupCallActivity.class));


                String room = AppConstant.GenerateId();

                JSONObject jsonObj = new JSONObject();

                try {
                    jsonObj.put("room", room);
                    jsonObj.put("type", "vedio");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("jsontst", jsonObj.toString());

                String msg = jsonObj.toString();


                //   PersistData.setStringData(context,AppConstant.reciverid, AppConstant.findIdbyName(PersistData.getStringData(con,AppConstant.friend_name)));

                String senderid = PersistData.getStringData(con, AppConstant.userId);
                String reciverid = PersistData.getStringData(con, AppConstant.reciverid);

                if (senderid != null && reciverid != null) {
                    PersistData.setStringData(con, AppConstant.rom_id, room);
                    requesCall(senderid, reciverid, Config.INCOMING_CALL, msg);
                    //  fragment.connectToRoomVedio(room, false, false, false, 0);

                    Intent intent = new Intent(con, CalingActivity.class);

                    intent.putExtra("name", AppConstant.findNameById(reciverid));
                    intent.putExtra("image", AppConstant.findImgById(reciverid));
                    con.startActivity(intent);


                } else {
                    if (senderid == null) {
                        Toast.makeText(con, "sender id null", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(con, "reciver id  null", Toast.LENGTH_LONG).show();
                    }

                }

                //Intent i = new Intent(context, Contacts.class);
                //startActivity(i);
            }
        });*/

        Button msgSendButton = (Button) findViewById(R.id.chat_send_msg);
        msgSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject data = new JSONObject();
                try {
                    data.put("conversation_id", "id");
                    data.put("sender_img", PersistData.getStringData(con,AppConstant.userImg));
                    data.put("sender_name", PersistData.getStringData(con,AppConstant.userName));
                    data.put("to", PersistData.getStringData(con,AppConstant.reciverid));
                    data.put("is_room", "false");
                    data.put("text",  msgInputText.getText().toString());
                    data.put("attach_file", null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                String msgContent = msgInputText.getText().toString();
                if (!TextUtils.isEmpty(msgContent)) {
                    // Add a new sent message to the list.
                    ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, msgContent);
                    msgDtoList.add(msgDto);

                    int newMsgPosition = msgDtoList.size() - 1;

                    // Notify recycler view insert one new data.
                    chatAppMsgAdapter.notifyItemInserted(newMsgPosition);

                    // Scroll RecyclerView to the last message.
                    msgRecyclerView.scrollToPosition(newMsgPosition);

                    // Empty the input edit text box.
                    msgInputText.setText("");
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(con).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(con).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(con);
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(con).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            return false;
        }
        return true;
    }

    // find  indicator_id  by user email
    public String findImgById(String value) {
        UserInfo pd;
        String data = "";
        for (int i = 0; getUserListfromDB().size() > i; i++) {
            pd = getUserListfromDB().get(i);
            if (pd.getId().equalsIgnoreCase(value)) {
                data = pd.getImg();
            }
        }
        return data;
    }
    /*get all userlist from database */

    public ArrayList<UserInfo> getUserListfromDB() {

        ArrayList<UserInfo> userlist = new ArrayList<>();


        MyDbHelper db = MyDbHelper.getInstance(con);
        JsonParser parser = new JsonParser();
        JsonElement mJson = parser.parse(db.getResponse(Config.LOGIN_API));
        Gson gson = new Gson();
        SigninResponse object = gson.fromJson(mJson, SigninResponse.class);

        if (object.getAlluserlist().size() > 0) {

            for (int i = 0; object.getAlluserlist().size() > i; i++) {

                if (object.getAlluserlist().get(i).getId().equalsIgnoreCase(PersistData.getStringData(con, AppConstant.userId))) {
                    object.getAlluserlist().remove(i);
                }
            }

            userlist = object.getAlluserlist();
        }

        return userlist;
    }


}