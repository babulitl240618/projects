package com.imagine.bd.hayvenapp.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eyalbira.loadingdots.LoadingDots;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.imagine.bd.hayvenapp.Adapter.AlertDialogImageAdapter;
import com.imagine.bd.hayvenapp.Adapter.ChatAppMsgAdapter;
import com.imagine.bd.hayvenapp.AppRTCClient;
import com.imagine.bd.hayvenapp.CallActivity;
import com.imagine.bd.hayvenapp.Database.MyDbHelper;
import com.imagine.bd.hayvenapp.MainActivity2;
import com.imagine.bd.hayvenapp.Model.ChatAppMsgDTO;
import com.imagine.bd.hayvenapp.Model.ConversationHistoryResponse;
import com.imagine.bd.hayvenapp.Model.MessageData;
import com.imagine.bd.hayvenapp.Model.ResponseData;
import com.imagine.bd.hayvenapp.Model.SigninResponse;
import com.imagine.bd.hayvenapp.Model.UserInfo;
import com.imagine.bd.hayvenapp.MyApplication;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.Retrofit.IRetrofit;
import com.imagine.bd.hayvenapp.Retrofit.ServiceGenerator;
import com.imagine.bd.hayvenapp.utils.API_URL;
import com.imagine.bd.hayvenapp.utils.AlertMessage;
import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.Config;
import com.imagine.bd.hayvenapp.utils.CustomTextWatcherimplements;
import com.imagine.bd.hayvenapp.utils.NetInfo;
import com.imagine.bd.hayvenapp.utils.NotificationUtils;
import com.imagine.bd.hayvenapp.utils.PersistData;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChattingActivity extends AppCompatActivity {


    /*Ui property for this class*/
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ImageView imggroupcall, imageView1, imgmenu, imggroupvideocall, imageEmoji;
    private TextView tvName, create_conversation;
    private ChatAppMsgAdapter chatAppMsgAdapter;
    private EditText msgInputText;
    private Socket mSocket;
    RecyclerView msgRecyclerView;
    LinearLayout linearLayout, linearLayout1;
    private Context con;
    ArrayList<MessageData> msgList = new ArrayList<>();
    private MessageData messageData;
    private String msghtml = "";
    private Spannable html = null;
    private boolean messagehasEmo = false;
    private boolean typingStarted;
    private Timer timerTyping = new Timer();
    private final long DELAY = 8000; // milliseconds
    LoadingDots typingStatus;
    String status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        con = this;
        initUi();
        MyApplication app = (MyApplication) getApplication();
        mSocket = app.getSocket();
        mSocket.on("newMessage", onMesseageRecived);
        mSocket.on("server_typing_emit", onTypingRecived);
        mSocket.connect();


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
        typingStatus = (LoadingDots) findViewById(R.id.typingStatus);

      /*  linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                //imgnewchat.setImageResource(R.drawable.new_chat_black);

            }
        });*/


        requestChatHistory();

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

       /* // Create the initial data list.
        msgDtoList = new ArrayList<ChatAppMsgDTO>();
        ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, "hello");
        ChatAppMsgDTO msgDto1 = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, "Get as much of that done today as you u can,but we have more then enough bugs to get started");
        ChatAppMsgDTO msgDto2 = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, "ok");
        msgDtoList.add(msgDto);
        msgDtoList.add(msgDto1);
        msgDtoList.add(msgDto2);
        defultValueadd();*/


        msgInputText = (EditText) findViewById(R.id.chat_input_msg);
        imggroupcall = (ImageView) findViewById(R.id.audio);
        imageEmoji = (ImageView) findViewById(R.id.imageEmoji);
        imggroupvideocall = (ImageView) findViewById(R.id.vedio);



       /*
        msgInputText.addTextChangedListener(new TextWatcher() {


            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // Toast.makeText(con, "beforeTextChanged", Toast.LENGTH_LONG).show();
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

               // Toast.makeText(con, "onTextChanged", Toast.LENGTH_LONG).show();

            }

            public void afterTextChanged(Editable s) {

                Toast.makeText(con,"typing",Toast.LENGTH_LONG).show();


              *//*  timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                                   @Override
                                   public void run() {
                                       Toast.makeText(con, "typing", Toast.LENGTH_LONG).show();
                                   }
                               },
                        DELAY
                );
*//*
                //   Toast.makeText(con,"typing",Toast.LENGTH_LONG).show();


              *//*
                if (!TextUtils.isEmpty(s.toString()) && s.toString().trim().length() == 1) {

                    //Log.i(TAG, “typing started event…”);

                    typingStarted = true;

                    Toast.makeText(con,"typing",Toast.LENGTH_LONG).show();

                    //send typing started status

                } else if (s.toString().trim().length() == 0 && typingStarted) {

                    //Log.i(TAG, “typing stopped event…”);

                    typingStarted = false;

                    Toast.makeText(con,"no typing",Toast.LENGTH_LONG).show();

                    //send typing stopped status

                }

                *//*


            }

        });
*/

/*

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(con,"no typing",Toast.LENGTH_LONG).show();
            }
        }, 5000);
*/


        timerTyping.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                JSONObject data = new JSONObject();
                try {
                    data.put("display", false);
                    data.put("room_id", PersistData.getStringData(con, AppConstant.reciverid));
                    data.put("sender_id", PersistData.getStringData(con, AppConstant.userId));
                    data.put("sender_name", PersistData.getStringData(con, AppConstant.userName));
                    data.put("sender_img", PersistData.getStringData(con, AppConstant.userPhoto));
                    data.put("conversation_id", PersistData.getStringData(con, AppConstant.conversation_id));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(data!=null && mSocket!=null){
                    mSocket.emit("client_typing", data);
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      //  Toast.makeText(con, "no typing", Toast.LENGTH_LONG).show();
                        // gfg

                    }
                });

            }
        }, 0, DELAY);//put here time 1000 milliseconds=1 second


        msgInputText.addTextChangedListener(new CustomTextWatcherimplements(msgInputText) { //Notice I'm passing in constructor of CustomTextWatcher myEditText I needed to use
            @Override
            public void textWasChanged() {
                //doSomething(); this is method inside your activity

                JSONObject data = new JSONObject();
                try {
                    data.put("display", true);
                    data.put("room_id", PersistData.getStringData(con, AppConstant.reciverid));
                    data.put("sender_id", PersistData.getStringData(con, AppConstant.userId));
                    data.put("sender_name", PersistData.getStringData(con, AppConstant.userName));
                    data.put("sender_img", PersistData.getStringData(con, AppConstant.userPhoto));
                    data.put("conversation_id", PersistData.getStringData(con, AppConstant.conversation_id));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mSocket.emit("client_typing", data);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                      //  Toast.makeText(con, "typing", Toast.LENGTH_LONG).show();

                    }
                });

            }
        });



        imageEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertDialog(con);
            }
        });

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

                Log.e("conversation id", PersistData.getStringData(con, AppConstant.conversation_id));

                /*if message has emo save data into string , if not emo find data from gettext  */
                if (messagehasEmo == false) {
                    msghtml = msgInputText.getText().toString();
                }

                /* create a json object for request post to server*/
                final JSONObject data = new JSONObject();
                try {
                    data.put("sender_id", PersistData.getStringData(con, AppConstant.userId));
                    data.put("conversation_id", PersistData.getStringData(con, AppConstant.conversation_id));
                    data.put("sender_img", PersistData.getStringData(con, AppConstant.userPhoto));
                    data.put("sender_name", PersistData.getStringData(con, AppConstant.userName));
                    data.put("to", PersistData.getStringData(con, AppConstant.reciverid));
                    data.put("is_room", false);
                    data.put("text", msghtml);
                    data.put("attach_file", null);

                    messageData = new MessageData();

                    messageData.setSender(PersistData.getStringData(con, AppConstant.userId));
                    messageData.setConversation_id(PersistData.getStringData(con, AppConstant.conversation_id));
                    messageData.setSender_img(PersistData.getStringData(con, AppConstant.userPhoto));
                    messageData.setSender_name(PersistData.getStringData(con, AppConstant.userName));
                    messageData.setMsg_body(msghtml);
                    msgList.add(messageData);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                /* check all value empty, if not empty then send data to server*/

                if (TextUtils.isEmpty(PersistData.getStringData(con, AppConstant.userId)) || PersistData.getStringData(con, AppConstant.userId).equalsIgnoreCase("")) {
                    Toast.makeText(con, "user id empty", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(PersistData.getStringData(con, AppConstant.conversation_id)) || PersistData.getStringData(con, AppConstant.conversation_id).equalsIgnoreCase("")) {
                    Toast.makeText(con, "conversation id empty", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(PersistData.getStringData(con, AppConstant.userPhoto)) || PersistData.getStringData(con, AppConstant.userPhoto).equalsIgnoreCase("")) {
                    Toast.makeText(con, "userPhoto id empty ", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(PersistData.getStringData(con, AppConstant.userName)) || PersistData.getStringData(con, AppConstant.userName).equalsIgnoreCase("")) {
                    Toast.makeText(con, "userName id empty", Toast.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(msghtml) || msghtml.equalsIgnoreCase("")) {
                    Toast.makeText(con, "message is empty", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Toast.makeText(con, "userPhoto = " + PersistData.getStringData(con, AppConstant.userPhoto), Toast.LENGTH_LONG).show();

                    /* send message data to server using socket, After checking data is not empty*/
                    mSocket.emit("sendMessageFromMobile", data, new Ack() {
                        @Override
                        public void call(Object... args) {


                            Log.e("msgsend", data.toString());

                            JSONObject data = (JSONObject) args[0];

                            // Toast.makeText(con, "test", Toast.LENGTH_LONG).show();

                            Log.e("jjj", data.toString());
                            String msgContent = msgInputText.getText().toString();


                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    // Stuff that updates the UI
                                    String msgContent = msgInputText.getText().toString();
                                    int newMsgPosition = msgList.size() - 1;

                                    if (!TextUtils.isEmpty(msgContent)) {
                                        // Add a new sent message to the list.
                                        // ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, msgContent);


                                        if (chatAppMsgAdapter == null) {

                                            if (msgList == null) {
                                                msgList.add(messageData);
                                            }

                                            chatAppMsgAdapter = new ChatAppMsgAdapter(msgList, con);
                                            // Set data adapter to RecyclerView.
                                            msgRecyclerView.setAdapter(chatAppMsgAdapter);
                                            // Scroll RecyclerView to the last message.
                                            msgRecyclerView.scrollToPosition(msgList.size() - 1);

                                        } else {
                                            // Notify recycler view insert one new data.
                                            chatAppMsgAdapter.notifyItemInserted(newMsgPosition);

                                            // Scroll RecyclerView to the last message.
                                            msgRecyclerView.scrollToPosition(newMsgPosition);

                                        }

                                        // Empty the input edit text box.
                                        msgInputText.setText("");
                                        msghtml = "";
                                        messagehasEmo = false;

                                    }

                                }
                            });


                        }
                    });

                    /* end socket function*/


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


    public void requestChatHistory() {

        if (!NetInfo.isOnline(con)) {
            AlertMessage.showMessage(con, "No Internet Connection!", "You need to be connected to your network ");
            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", PersistData.getStringData(con, AppConstant.userId));
        jsonObject.addProperty("targetID", PersistData.getStringData(con, AppConstant.reciverid));
        jsonObject.addProperty("ecosystem", "NavCon");

        // Using the Retrofit
        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class, API_URL.BASE_URL);
        Call<JsonObject> call = jsonPostService.getChathistory(jsonObject);
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {

                    if (response.body().toString().equalsIgnoreCase(null)) {
                        Toast.makeText(con, "server respond failed!", Toast.LENGTH_LONG).show();
                    }

                    String responseValue = response.body().toString();

                    Log.e("response-success", responseValue);

                    JsonParser parser = new JsonParser();
                    JsonElement mJson = parser.parse(responseValue);
                    Gson gson = new Gson();
                    ConversationHistoryResponse object = gson.fromJson(mJson, ConversationHistoryResponse.class);

                    if (object.getStatus().equalsIgnoreCase("true")) {

                        // store conversation id
                        PersistData.setStringData(con, AppConstant.conversation_id, object.getConversation_id());

                        Log.e("mmm", object.getResult().size() + "");
                        msgList = object.getResult();

                        if (msgList != null) {
                            // Create the data adapter with above data list.
                            chatAppMsgAdapter = new ChatAppMsgAdapter(msgList, con);
                            // Set data adapter to RecyclerView.
                            msgRecyclerView.setAdapter(chatAppMsgAdapter);
                            // Scroll RecyclerView to the last message.
                            msgRecyclerView.scrollToPosition(msgList.size() - 1);

                        }

                    }


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


    private Emitter.Listener onTypingRecived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.e("test type", data.toString());

            String sender_id = "";


            try {
                sender_id = data.getString("sender_id");
                status = data.getString("display");

                Log.e("sender_id",sender_id);
                Log.e("display",status);

            } catch (Exception e) {
                e.printStackTrace();
            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (status.equalsIgnoreCase("true")) {
                        typingStatus.setVisibility(View.VISIBLE);
                    } else {
                        typingStatus.setVisibility(View.GONE);
                    }
                }
            });



           /*
            if (sender_id.equalsIgnoreCase(PersistData.getStringData(con, AppConstant.userId))) {

                if (status.equalsIgnoreCase("true")) {
                    typingStatus.setVisibility(View.VISIBLE);
                } else {
                    typingStatus.setVisibility(View.GONE);
                }
            }
            */


        }
    };

    private Emitter.Listener onMesseageRecived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];

            // Toast.makeText(con, "test", Toast.LENGTH_LONG).show();

            Log.e("test 738", data.toString());
            // startActivity(new Intent(con,));

            String sender_id;
            String sender_name;
            String sender_img;
            String msg_id;
            String msg_conv_id;
            String text;

            try {
                sender_id = data.getString("msg_from");
                sender_name = data.getString("msg_sender_name");
                sender_img = data.getString("msg_sender_img");
                msg_id = data.getString("msg_id");
                msg_conv_id = data.getString("msg_conv_id");
                text = data.getString("msg_text");

                messageData = new MessageData();

                messageData.setSender(sender_id);
                messageData.setMsg_id(msg_id);
                messageData.setConversation_id(msg_conv_id);
                messageData.setSender_img(sender_img);
                messageData.setSender_name(sender_name);
                messageData.setMsg_body(text);
                msgList.add(messageData);


            } catch (JSONException e) {
                e.printStackTrace();
            }


            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    // Stuff that updates the UI
                    //  String msgContent = msgInputText.getText().toString();
                    int newMsgPosition = msgList.size() - 1;


                    // Add a new sent message to the list.
                    // ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, msgContent);


                    // Notify recycler view insert one new data.
                    chatAppMsgAdapter.notifyItemInserted(newMsgPosition);

                    // Scroll RecyclerView to the last message.
                    msgRecyclerView.scrollToPosition(newMsgPosition);


                }
            });


           /*

            String from;
            String text;
            try {
                from = data.getString("from");
                text = data.getString("text").replace("[", "").replace("]", "").replace("\"", "");
                String[] elements = text.split(",");
                List<String> onlinelist = new ArrayList<>();

                if (elements != null) {
                    for (int i = 0; elements.length > i; i++) {
                        onlinelist.add(elements[i]);
                    }
                    AppConstant.onlinelist = onlinelist;
                }


                Log.e("from", from);
                Log.e("text", text);
            } catch (JSONException e) {

                return;
            }
            // finish();

            */


        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.off("newMessage", onMesseageRecived);
        mSocket.off("server_typing_emit", onTypingRecived);
        timerTyping.cancel();

    }


    private void showAlertDialog(Context context) {

        // This is your source for your icons, fill it with your own
        final String emoNmae[] = {
                "grinning", "disappointed_relieved",
                "heart", "joy",
                "open_mouth", "rage",
                "thumbsdown", "thumbsup",
                "grinning", "disappointed_relieved",
                "heart", "joy",
                "open_mouth", "rage",
                "thumbsdown", "thumbsup",
                "grinning", "disappointed_relieved",
                "heart", "joy",
                "open_mouth", "rage",
                "thumbsdown", "thumbsup"

        };


        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        GridView gridView = new GridView(context);
        gridView.setAdapter(new AlertDialogImageAdapter(context));
        gridView.setNumColumns(4);
        gridView.setGravity(Gravity.CENTER);
        builder.setView(gridView);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setView(image);
        }*/
        builder.setTitle("");
        builder.setIcon(R.drawable.header_image);
        final Dialog dialog = builder.create();
        dialog.show();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: Implement
 /* check message has emo, if emo click guess eamin emo,
                otherwise send message by getedittext data, else has emo send message data as astring*/

                if (messagehasEmo == false) {
                    msghtml = msgInputText.getText().toString();
                }

                messagehasEmo = true;

                // Toast.makeText(view.getContext(), "Clicked position is: " + position, Toast.LENGTH_LONG).show();
                //  msghtml = msgInputText.getText().toString() + "<img src=\"/images/emoji/heart.png\" style=\"width:20px; height:20px;\">";
                msghtml = msghtml + "<img src=\"/images/emoji/" + emoNmae[position] + ".png\" style=\"width:20px; height:20px;\">";


/*
                if(html!=null){
                    msghtml = Html.toHtml(html);
                }*/


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //  holder.leftMsgTextView.setText( Html.fromHtml(msgDto.getMsg_body(), Html.FROM_HTML_MODE_COMPACT));

                    html = (Spannable) Html.fromHtml(msghtml, Html.FROM_HTML_MODE_LEGACY, new Html.ImageGetter() {
                        @Override
                        public Drawable getDrawable(String source) {

                            int id;
                            if (source.equals("/images/emoji/heart.png")) {
                                id = R.drawable.heart;
                            } else if (source.equals("/images/emoji/grinning.png")) {
                                id = R.drawable.grinning;
                            } else if (source.equals("/images/emoji/disappointed_relieved.png")) {
                                id = R.drawable.disappointed_relieved;
                            } else if (source.equals("/images/emoji/joy.png")) {
                                id = R.drawable.joy;
                            } else if (source.equals("/images/emoji/open_mouth.png")) {
                                id = R.drawable.open_mouth;
                            } else if (source.equals("/images/emoji/rage.png")) {
                                id = R.drawable.rage;
                            } else if (source.equals("/images/emoji/thumbsdown.png")) {
                                id = R.drawable.thumbsdown;
                            } else if (source.equals("/images/emoji/thumbsup.png")) {
                                id = R.drawable.thumbsup;
                            } else {
                                return null;
                            }
                            Drawable d = con.getResources().getDrawable(id);
                            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                            return d;

                        }
                    }, null);


                } else {
                    html = (Spannable) Html.fromHtml(msghtml, new Html.ImageGetter() {
                        @Override
                        public Drawable getDrawable(String source) {
                            int id;

                            if (source.equals("/images/emoji/heart.png")) {
                                id = R.drawable.heart;
                            } else if (source.equals("/images/emoji/grinning.png")) {
                                id = R.drawable.grinning;
                            } else if (source.equals("/images/emoji/disappointed_relieved.png")) {
                                id = R.drawable.disappointed_relieved;
                            } else if (source.equals("/images/emoji/joy.png")) {
                                id = R.drawable.joy;
                            } else if (source.equals("/images/emoji/open_mouth.png")) {
                                id = R.drawable.open_mouth;
                            } else if (source.equals("/images/emoji/rage.png")) {
                                id = R.drawable.rage;
                            } else if (source.equals("/images/emoji/thumbsdown.png")) {
                                id = R.drawable.thumbsdown;
                            } else if (source.equals("/images/emoji/thumbsup.png")) {
                                id = R.drawable.thumbsup;
                            } else {
                                return null;
                            }

                            Drawable d = con.getResources().getDrawable(id);
                            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                            return d;
                        }
                    }, null);

                    // holder.leftMsgTextView.setText(msgDto.getMsg_body());
                }


                msgInputText.setText(html);
                msgInputText.setSelection(msgInputText.getText().length());
                dialog.dismiss();
            }
        });


    }

}