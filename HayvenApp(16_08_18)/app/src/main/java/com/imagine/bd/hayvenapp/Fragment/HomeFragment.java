package com.imagine.bd.hayvenapp.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.imagine.bd.hayvenapp.Activity.CallInfoActivity;
import com.imagine.bd.hayvenapp.Activity.ChattingActivity;
import com.imagine.bd.hayvenapp.Activity.IncomingCallActivity;
import com.imagine.bd.hayvenapp.Activity.JoinGroupCallActivity;
import com.imagine.bd.hayvenapp.Activity.New_ChatActivity;
import com.imagine.bd.hayvenapp.Adapter.AllUserListAdapter;
import com.imagine.bd.hayvenapp.Adapter.CallBaseAdapter;
import com.imagine.bd.hayvenapp.Adapter.CallsAdapter;
import com.imagine.bd.hayvenapp.Adapter.ChatsAdapter;
import com.imagine.bd.hayvenapp.Adapter.ChatsUserAdapter;
import com.imagine.bd.hayvenapp.Adapter.ChatsWorkbaseUserAdapter;
import com.imagine.bd.hayvenapp.AppRTCClient;
import com.imagine.bd.hayvenapp.CallActivity;
import com.imagine.bd.hayvenapp.Database.MyDbHelper;
import com.imagine.bd.hayvenapp.MainActivity2;
import com.imagine.bd.hayvenapp.Model.ChatUserInfo;
import com.imagine.bd.hayvenapp.Model.DataModel;
import com.imagine.bd.hayvenapp.Model.MessageData;
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

import org.joda.time.Instant;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static org.webrtc.ContextUtils.getApplicationContext;


public class HomeFragment extends Fragment {
    private Context con;
    private static final String TAG = "ConnectActivity";
    private static final int CONNECTION_REQUEST = 1;
    private static final int REMOVE_FAVORITE_INDEX = 0;
    private static boolean commandLineRun = false;
    private PeerConnectionFactory factory;
    private PeerConnection peerConnection;
    private AppRTCClient.SignalingParameters signalingParameters;
    private ImageButton connectButton, connectButton1;
    private ImageButton addFavoriteButton;
    private EditText roomEditText;
    private ListView roomListView;
    private SharedPreferences sharedPref;
    private String keyprefVideoCallEnabled;
    private String keyprefScreencapture;
    private String keyprefCamera2;
    private String keyprefResolution;
    private String keyprefFps;
    private String keyprefCaptureQualitySlider;
    private String keyprefVideoBitrateType;
    private String keyprefVideoBitrateValue;
    private String keyprefVideoCodec;
    private String keyprefAudioBitrateType;
    private String keyprefAudioBitrateValue;
    private String keyprefAudioCodec;
    private String keyprefHwCodecAcceleration;
    private String keyprefCaptureToTexture;
    private String keyprefFlexfec;
    private String keyprefNoAudioProcessingPipeline;
    private String keyprefAecDump;
    private String keyprefOpenSLES;
    private String keyprefDisableBuiltInAec;
    private String keyprefDisableBuiltInAgc;
    private String keyprefDisableBuiltInNs;
    private String keyprefEnableLevelControl;
    private String keyprefDisableWebRtcAGCAndHPF;
    private String keyprefDisplayHud;
    private String keyprefTracing;
    private String keyprefRoomServerUrl;
    private String keyprefRoom;
    private String keyprefRoomList;
    private ArrayList<String> roomList;
    private String keyprefEnableDataChannel;
    private String keyprefOrdered;
    private String keyprefMaxRetransmitTimeMs;
    private String keyprefMaxRetransmits;
    private String keyprefDataProtocol;
    private String keyprefNegotiated;
    private String keyprefDataId;
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    // main activity
    //ArrayList<UserInfo> tempArrayList;
    ArrayList<ChatUserInfo> tempArrayList1;
    private ListView listChats, listCall;
    TextView textallchats, textallcalls, textallchats1, textallcalls1;
    private RelativeLayout rel_layChat, rel_layCall;
    private ImageView imgnewchat, imgContinue2, imgContinue3, imgContinue4;
    //private ChatsUserAdapter adapter;
    private ChatsWorkbaseUserAdapter adapter;
    private CallBaseAdapter adapter1;
    private String type_status = "chat";
    private EditText etSearch;
    private int room = 333;
    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    private Socket mSocket;

    private String sender_id;
    private String sender_name;
    private String sender_img;
    private String msg_id;
    private String msg_conv_id;
    private String text = "";
   // private int unreadMessageCounter = 0;
    ChatUserInfo chatuserInfo = new ChatUserInfo();
    ArrayList<ChatUserInfo> conversationList = new ArrayList<>();

    Handler h = new Handler();
    int delay = 10 * 1000; //1 second=1000 milisecond, 15*1000=15seconds
    Runnable runnable;

    String[] permissionsList = new String[]{
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.WAKE_LOCK};

   /* String[] permissionsList = new String[]{
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.CAMERA,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            };*/


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        con = getActivity();

        conversationList.clear();
        conversationList.addAll(getLastConversationfromDB());

        Log.e("ttt ", conversationList.size() + " ");

        initializedConectActivity();

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


        if (checkPermissions()) {
            initUI();
        } else {
            initUI();
        }
    }


    public void setDefaultadapter() {

        // adapter = new ChatsWorkbaseUserAdapter(con, AppConstant.myUserList, HomeFragment.this);
        adapter = new ChatsWorkbaseUserAdapter(con,conversationList, HomeFragment.this);
        //adapter = new ChatsUserAdapter(con, getUserListfromDB(), HomeFragment.this);
        // listView=null;
        listChats.setAdapter(adapter);
        listChats.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dialog = new Dialog(con);
                dialog.setContentView(R.layout.dailog_more);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER | Gravity.CENTER;

                wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);

                dialog.show();
                return false;
            }
        });

        listChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, "chat list", Toast.LENGTH_LONG).show();

                if ((tempArrayList1 != null)) {
                    PersistData.setStringData(con, AppConstant.friend_name, tempArrayList1.get(position).getSender_name());
                    PersistData.setStringData(con, AppConstant.reciverid, findIdByName(tempArrayList1.get(position).getSender_name()));
                    startActivity(new Intent(con, ChattingActivity.class));

                    Log.e("name", "");
                } else {
                    Log.e("name", conversationList.get(position).getSender_name());
                    Log.e("msg", conversationList.get(position).getMsg_body());

                    PersistData.setStringData(con, AppConstant.friend_name, conversationList.get(position).getConversation_title());
                    PersistData.setStringData(con, AppConstant.reciverid, findIdByName(conversationList.get(position).getConversation_title()));
                    startActivity(new Intent(con, ChattingActivity.class));
                }

                String conversationid = getConversationIdfromdb(PersistData.getStringData(con, AppConstant.reciverid));
                if (conversationid != "") {
                    Log.e("final test c", conversationid);
                    // Toast.makeText(con, conversationid, Toast.LENGTH_LONG).show();
                } else {
                    //  Toast.makeText(con, "no conversation id", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissionsList) {
            result = ContextCompat.checkSelfPermission(con, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) con, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                } else {
                    String permission = "";
                    for (String per : permissionsList) {
                        permission += "\n" + per;
                    }
                    // permissions list of don't granted permission
                }
                return;
            }
        }
    }


    public void initUI() {

        MyApplication app = (MyApplication) getActivity().getApplication();

        mSocket = app.getSocket();
        //  mSocket.on("logout", onLogout);
        mSocket.on("online_user_list", onUserList);
        mSocket.on("newMessage", onMesseageRecived);
        mSocket.connect();

        //ll_contact = (LinearLayout) getView().findViewById(R.id.ll_contact);
        //ll_notifacaton = (LinearLayout) getView().findViewById(R.id.ll_notification);
        //ll_more = (LinearLayout) getView().findViewById(R.id.ll_more);
        listChats = (ListView) getView().findViewById(R.id.listChats);
        listCall = (ListView) getView().findViewById(R.id.listCall);
        textallchats = (TextView) getView().findViewById(R.id.textallchats);
        textallchats1 = (TextView) getView().findViewById(R.id.textallchats1);
        textallcalls1 = (TextView) getView().findViewById(R.id.textallcalls1);
        rel_layChat = (RelativeLayout) getView().findViewById(R.id.rel_layChat);
        rel_layCall = (RelativeLayout) getView().findViewById(R.id.rel_layCall);
        // imgcontacts = (ImageView) findViewById(R.id.imgContacts);
        imgnewchat = (ImageView) getView().findViewById(R.id.imgNewchat);
        etSearch = (EditText) getView().findViewById(R.id.etSearch);

        adapter1 = new CallBaseAdapter(con, getUserListfromDB(), HomeFragment.this);
        setDefaultadapter();
        imgnewchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PersistData.setStringData(con, AppConstant.isSignin, "false");

                if (type_status == "chat") {
                    startActivity(new Intent(con, New_ChatActivity.class));
                } else {
                    startActivity(new Intent(con, JoinGroupCallActivity.class));
                }


                //Intent i = new Intent(context, Contacts.class);
                //startActivity(i);
            }
        });


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int start, int count, int after) {
                // contactadapter.getFilter().filter(s);

                tempArrayList1 = new ArrayList<ChatUserInfo>();
                int textlength = cs.length();
                listChats.setVisibility(View.VISIBLE);

                // listChats.setVisibility(View.VISIBLE);
                listCall.setVisibility(View.GONE);

                for (ChatUserInfo c : conversationList) {

                    String name = etSearch.getText().toString();
                   /* if (name.matches("")) {
                        Toast.makeText(con, "You did not enter a username", Toast.LENGTH_SHORT).show();
                        listViewcontacts.setVisibility(View.VISIBLE);
                        return;
                    }else {
                        listViewcontacts.setVisibility(View.GONE);
                    }*/

                    // chack data by character sequence and get searching list data
                    if (textlength <= c.getSender_name().length()) {
                        if (c.getSender_name().toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList1.add(c);
                        }
                    }

                }
                //adapter = new ChatsUserAdapter(con, tempArrayList, HomeFragment.this);
                adapter = new ChatsWorkbaseUserAdapter(con, tempArrayList1, HomeFragment.this);
                listChats.setAdapter(adapter);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });   // end of search view


        textallchats.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            public void onClick(View v) {

                listChats.setVisibility(View.VISIBLE);
                listCall.setVisibility(View.GONE);

                type_status = "chat";
                imgnewchat.setImageResource(R.drawable.new_chat_black);
                rel_layChat.setVisibility(View.VISIBLE);
                rel_layCall.setVisibility(View.GONE);

                /*textallchats.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
                // textallchats.setTextColor(R.color.black);
                textallchats.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                textallchats.setTextColor(getResources().getColor(R.color.black));

                textallcalls.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                textallcalls.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                textallcalls.setTextColor(getResources().getColor(R.color.txtGray));*/

              /*  dataModels = new ArrayList<>();
                dataModels = new ArrayList<>();
                dataModels.add(new DataModel("Anwar Ali", "The ArrayAdapter will ..", "rm_111", "September 23, 2008"));
                dataModels.add(new DataModel("Md.Sadequr rahman", "The ArrayAdapter will ..", "rm_112", "February 9, 2009"));
                dataModels.add(new DataModel("SM Jubayer", "The ArrayAdapter will ..", "rm_113", "September 23, 2008"));
                dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "rm_119", "September 23, 2008"));
                dataModels.add(new DataModel("Sujon Ahmed", "The ArrayAdapter will ..", "rm_114", "September 23, 2008"));
                dataModels.add(new DataModel("Dipok Chakroborty", "The ArrayAdapter will ..", "rm_115", "September 23, 2008"));
                dataModels.add(new DataModel("Joni Chowdury", "The ArrayAdapter will ..", "rm_116", "September 23, 2008"));
                dataModels.add(new DataModel(" Mahfuzur Rahman", "The ArrayAdapter will ..", "rm_117", "September 23, 2008"));
                dataModels.add(new DataModel("Manzurul Alam", "The ArrayAdapter will ..", "rm_118", "September 23, 2008"));*/
                //adapter = new ChatsUserAdapter(con, getUserListfromDB(), HomeFragment.this);
                adapter = new ChatsWorkbaseUserAdapter(con, conversationList, HomeFragment.this);

                listChats.setAdapter(adapter);
                listChats.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        final Dialog dialog = new Dialog(con);
                        dialog.setContentView(R.layout.dailog_more);
                        Window window = dialog.getWindow();
                        WindowManager.LayoutParams wlp = window.getAttributes();
                        wlp.gravity = Gravity.CENTER | Gravity.CENTER;

                        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                        window.setAttributes(wlp);

                        dialog.show();
                        return false;
                    }
                });


                listChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(MainActivity.this, "chat list", Toast.LENGTH_LONG).show();
                        if ((tempArrayList1 != null)) {
                            PersistData.setStringData(con, AppConstant.friend_name, tempArrayList1.get(position).getSender_name());
                            PersistData.setStringData(con, AppConstant.reciverid, findIdByName(tempArrayList1.get(position).getSender_name()));
                            startActivity(new Intent(con, ChattingActivity.class));

                            Log.e("name", "");
                        } else {
                            Log.e("name", conversationList.get(position).getSender_name());
                            Log.e("msg", conversationList.get(position).getMsg_body());

                            PersistData.setStringData(con, AppConstant.friend_name,conversationList.get(position).getSender_name());
                            PersistData.setStringData(con, AppConstant.reciverid, findIdByName(conversationList.get(position).getSender_name()));
                            startActivity(new Intent(con, ChattingActivity.class));
                        }

                        Toast.makeText(con, conversationList.get(position).getSender_name(), Toast.LENGTH_LONG);

                        String conversationid = getConversationIdfromdb(PersistData.getStringData(con, AppConstant.reciverid));
                        if (conversationid != "") {
                            Log.e("final test c", conversationid);
                            // Toast.makeText(con, conversationid, Toast.LENGTH_LONG).show();
                        } else {
                            //  Toast.makeText(con, "no conversation id", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });
        textallcalls = (TextView) getView().findViewById(R.id.textallcalls);
        textallcalls.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                listChats.setVisibility(View.GONE);
                listCall.setVisibility(View.VISIBLE);


                type_status = "call";
                imgnewchat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PersistData.setStringData(con, AppConstant.isSignin, "false");

                        startActivity(new Intent(con, JoinGroupCallActivity.class));


                        //Intent i = new Intent(context, Contacts.class);
                        //startActivity(i);
                    }
                });
                imgnewchat.setImageResource(R.drawable.new_call_black);
                rel_layChat.setVisibility(View.GONE);
                rel_layCall.setVisibility(View.VISIBLE);
                textallchats1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rel_layChat.setVisibility(View.VISIBLE);
                        rel_layCall.setVisibility(View.GONE);
                        listChats.setVisibility(View.VISIBLE);
                        listCall.setVisibility(View.GONE);
                        imgnewchat.setImageResource(R.drawable.new_chat_black);
                        imgnewchat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                PersistData.setStringData(con, AppConstant.isSignin, "false");

                                startActivity(new Intent(con, New_ChatActivity.class));


                                //Intent i = new Intent(context, Contacts.class);
                                //startActivity(i);
                            }
                        });
                    }
                });
            /*    textallcalls.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
                // textallchats.setTextColor(R.color.black);
                textallcalls.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                textallcalls.setTextColor(getResources().getColor(R.color.black));

                textallchats.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                textallchats.setTypeface(Typeface.DEFAULT);
                textallchats.setTextColor(getResources().getColor(R.color.txtGray));*/
/*
                dataModels = new ArrayList<>();
                dataModels.add(new DataModel("Anwar Ali", "The ArrayAdapter will ..", "rm_111", "September 23, 2008"));
                dataModels.add(new DataModel("Md.Sadequr rahman", "The ArrayAdapter will ..", "rm_112", "February 9, 2009"));
                dataModels.add(new DataModel("SM Jubayer", "The ArrayAdapter will ..", "rm_113", "September 23, 2008"));
                dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "rm_119", "September 23, 2008"));
                dataModels.add(new DataModel("Sujon Ahmed", "The ArrayAdapter will ..", "rm_114", "September 23, 2008"));
                dataModels.add(new DataModel("Dipok Chakroborty", "The ArrayAdapter will ..", "rm_115", "September 23, 2008"));
                dataModels.add(new DataModel("Joni Chowdury", "The ArrayAdapter will ..", "rm_116", "September 23, 2008"));
                dataModels.add(new DataModel(" Mahfuzur Rahman", "The ArrayAdapter will ..", "rm_117", "September 23, 2008"));
                dataModels.add(new DataModel("Manzurul Alam", "The ArrayAdapter will ..", "rm_118", "September 23, 2008"));*/

                listCall.setAdapter(adapter1);
                listCall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if ((tempArrayList1 != null)) {
                            PersistData.setStringData(con, AppConstant.friend_name, tempArrayList1.get(position).getSender_name());
                            PersistData.setStringData(con, AppConstant.reciverid, findIdByName(tempArrayList1.get(position).getSender_name()));
                            startActivity(new Intent(con, ChattingActivity.class));

                            Log.e("name", "");
                        } else {
                            Log.e("name", conversationList.get(position).getSender_name());
                            Log.e("msg", conversationList.get(position).getMsg_body());

                            PersistData.setStringData(con, AppConstant.friend_name,conversationList.get(position).getSender_name());
                            PersistData.setStringData(con, AppConstant.reciverid, findIdByName(conversationList.get(position).getSender_name()));
                            startActivity(new Intent(con, ChattingActivity.class));
                        }

                    }
                });

            }
        });

    }

    public void initializedConectActivity() {

        // Get setting keys.
        PreferenceManager.setDefaultValues(con, R.xml.preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(con);
        keyprefVideoCallEnabled = getString(R.string.pref_videocall_key);
        keyprefScreencapture = getString(R.string.pref_screencapture_key);
        keyprefCamera2 = getString(R.string.pref_camera2_key);
        keyprefResolution = getString(R.string.pref_resolution_key);
        keyprefFps = getString(R.string.pref_fps_key);
        keyprefCaptureQualitySlider = getString(R.string.pref_capturequalityslider_key);
        keyprefVideoBitrateType = getString(R.string.pref_maxvideobitrate_key);
        keyprefVideoBitrateValue = getString(R.string.pref_maxvideobitratevalue_key);
        keyprefVideoCodec = getString(R.string.pref_videocodec_key);
        keyprefHwCodecAcceleration = getString(R.string.pref_hwcodec_key);
        keyprefCaptureToTexture = getString(R.string.pref_capturetotexture_key);
        keyprefFlexfec = getString(R.string.pref_flexfec_key);
        keyprefAudioBitrateType = getString(R.string.pref_startaudiobitrate_key);
        keyprefAudioBitrateValue = getString(R.string.pref_startaudiobitratevalue_key);
        keyprefAudioCodec = getString(R.string.pref_audiocodec_key);
        keyprefNoAudioProcessingPipeline = getString(R.string.pref_noaudioprocessing_key);
        keyprefAecDump = getString(R.string.pref_aecdump_key);
        keyprefOpenSLES = getString(R.string.pref_opensles_key);
        keyprefDisableBuiltInAec = getString(R.string.pref_disable_built_in_aec_key);
        keyprefDisableBuiltInAgc = getString(R.string.pref_disable_built_in_agc_key);
        keyprefDisableBuiltInNs = getString(R.string.pref_disable_built_in_ns_key);
        keyprefEnableLevelControl = getString(R.string.pref_enable_level_control_key);
        keyprefDisableWebRtcAGCAndHPF = getString(R.string.pref_disable_webrtc_agc_and_hpf_key);
        keyprefDisplayHud = getString(R.string.pref_displayhud_key);
        keyprefTracing = getString(R.string.pref_tracing_key);
        keyprefRoomServerUrl = getString(R.string.pref_room_server_url_key);
        keyprefRoom = getString(R.string.pref_room_key);
        keyprefRoomList = getString(R.string.pref_room_list_key);
        keyprefEnableDataChannel = getString(R.string.pref_enable_datachannel_key);
        keyprefOrdered = getString(R.string.pref_ordered_key);
        keyprefMaxRetransmitTimeMs = getString(R.string.pref_max_retransmit_time_ms_key);
        keyprefMaxRetransmits = getString(R.string.pref_max_retransmits_key);
        keyprefDataProtocol = getString(R.string.pref_data_protocol_key);
        keyprefNegotiated = getString(R.string.pref_negotiated_key);
        keyprefDataId = getString(R.string.pref_data_id_key);

        // If an implicit VIEW intent is launching the app, go directly to that URL.
        final Intent intent = getActivity().getIntent();
        if ("android.intent.action.VIEW".equals(intent.getAction()) && !commandLineRun) {
            boolean loopback = intent.getBooleanExtra(CallActivity.EXTRA_LOOPBACK, false);
            int runTimeMs = intent.getIntExtra(CallActivity.EXTRA_RUNTIME, 0);
            boolean useValuesFromIntent =
                    intent.getBooleanExtra(CallActivity.EXTRA_USE_VALUES_FROM_INTENT, false);
            String room = sharedPref.getString(keyprefRoom, "");
            connectToRoomAudio(room, true, loopback, useValuesFromIntent, runTimeMs);
        }

    }


    public void connectToRoomAudio(String roomId, boolean commandLineRun, boolean loopback,
                                   boolean useValuesFromIntent, int runTimeMs) {
        this.commandLineRun = commandLineRun;

        // roomId is random for loopback.
        if (loopback) {
            roomId = Integer.toString((new Random()).nextInt(100000000));
        }

        String roomUrl = sharedPref.getString(
                keyprefRoomServerUrl, getString(R.string.pref_room_server_url_default));

        boolean videoCallEnabled = false;
      /*  // Video call enabled flag.
        boolean videoCallEnabled = sharedPrefGetBoolean(R.string.pref_videocall_key,
                CallActivity.EXTRA_VIDEO_CALL, R.string.pref_videocall_default, useValuesFromIntent);
*/
        // Use screencapture option.
        boolean useScreencapture = sharedPrefGetBoolean(R.string.pref_screencapture_key,
                CallActivity.EXTRA_SCREENCAPTURE, R.string.pref_screencapture_default, useValuesFromIntent);

        // Use Camera2 option.
        boolean useCamera2 = sharedPrefGetBoolean(R.string.pref_camera2_key, CallActivity.EXTRA_CAMERA2,
                R.string.pref_camera2_default, useValuesFromIntent);

        // Get default codecs.
        String videoCodec = sharedPrefGetString(R.string.pref_videocodec_key,
                CallActivity.EXTRA_VIDEOCODEC, R.string.pref_videocodec_default, useValuesFromIntent);
        String audioCodec = sharedPrefGetString(R.string.pref_audiocodec_key,
                CallActivity.EXTRA_AUDIOCODEC, R.string.pref_audiocodec_default, useValuesFromIntent);

        // Check HW codec flag.
        boolean hwCodec = sharedPrefGetBoolean(R.string.pref_hwcodec_key,
                CallActivity.EXTRA_HWCODEC_ENABLED, R.string.pref_hwcodec_default, useValuesFromIntent);

        // Check Capture to texture.
        boolean captureToTexture = sharedPrefGetBoolean(R.string.pref_capturetotexture_key,
                CallActivity.EXTRA_CAPTURETOTEXTURE_ENABLED, R.string.pref_capturetotexture_default,
                useValuesFromIntent);

        // Check FlexFEC.
        boolean flexfecEnabled = sharedPrefGetBoolean(R.string.pref_flexfec_key,
                CallActivity.EXTRA_FLEXFEC_ENABLED, R.string.pref_flexfec_default, useValuesFromIntent);

        // Check Disable Audio Processing flag.
        boolean noAudioProcessing = sharedPrefGetBoolean(R.string.pref_noaudioprocessing_key,
                CallActivity.EXTRA_NOAUDIOPROCESSING_ENABLED, R.string.pref_noaudioprocessing_default,
                useValuesFromIntent);

        // Check Disable Audio Processing flag.
        boolean aecDump = sharedPrefGetBoolean(R.string.pref_aecdump_key,
                CallActivity.EXTRA_AECDUMP_ENABLED, R.string.pref_aecdump_default, useValuesFromIntent);

        // Check OpenSL ES enabled flag.
        boolean useOpenSLES = sharedPrefGetBoolean(R.string.pref_opensles_key,
                CallActivity.EXTRA_OPENSLES_ENABLED, R.string.pref_opensles_default, useValuesFromIntent);

        // Check Disable built-in AEC flag.
        boolean disableBuiltInAEC = sharedPrefGetBoolean(R.string.pref_disable_built_in_aec_key,
                CallActivity.EXTRA_DISABLE_BUILT_IN_AEC, R.string.pref_disable_built_in_aec_default,
                useValuesFromIntent);

        // Check Disable built-in AGC flag.
        boolean disableBuiltInAGC = sharedPrefGetBoolean(R.string.pref_disable_built_in_agc_key,
                CallActivity.EXTRA_DISABLE_BUILT_IN_AGC, R.string.pref_disable_built_in_agc_default,
                useValuesFromIntent);

        // Check Disable built-in NS flag.
        boolean disableBuiltInNS = sharedPrefGetBoolean(R.string.pref_disable_built_in_ns_key,
                CallActivity.EXTRA_DISABLE_BUILT_IN_NS, R.string.pref_disable_built_in_ns_default,
                useValuesFromIntent);

        // Check Enable level control.
        boolean enableLevelControl = sharedPrefGetBoolean(R.string.pref_enable_level_control_key,
                CallActivity.EXTRA_ENABLE_LEVEL_CONTROL, R.string.pref_enable_level_control_key,
                useValuesFromIntent);

        // Check Disable gain control
        boolean disableWebRtcAGCAndHPF = sharedPrefGetBoolean(
                R.string.pref_disable_webrtc_agc_and_hpf_key, CallActivity.EXTRA_DISABLE_WEBRTC_AGC_AND_HPF,
                R.string.pref_disable_webrtc_agc_and_hpf_key, useValuesFromIntent);

        // Get video resolution from settings.
        int videoWidth = 0;
        int videoHeight = 0;
        if (useValuesFromIntent) {
            videoWidth = getActivity().getIntent().getIntExtra(CallActivity.EXTRA_VIDEO_WIDTH, 0);
            videoHeight = getActivity().getIntent().getIntExtra(CallActivity.EXTRA_VIDEO_HEIGHT, 0);
        }
        if (videoWidth == 0 && videoHeight == 0) {
            String resolution =
                    sharedPref.getString(keyprefResolution, getString(R.string.pref_resolution_default));
            String[] dimensions = resolution.split("[ x]+");
            if (dimensions.length == 2) {
                try {
                    videoWidth = Integer.parseInt(dimensions[0]);
                    videoHeight = Integer.parseInt(dimensions[1]);
                } catch (NumberFormatException e) {
                    videoWidth = 0;
                    videoHeight = 0;
                    Log.e(TAG, "Wrong video resolution setting: " + resolution);
                }
            }
        }

        // Get camera fps from settings.
        int cameraFps = 0;
        if (useValuesFromIntent) {
            cameraFps = getActivity().getIntent().getIntExtra(CallActivity.EXTRA_VIDEO_FPS, 0);
        }
        if (cameraFps == 0) {
            String fps = sharedPref.getString(keyprefFps, getString(R.string.pref_fps_default));
            String[] fpsValues = fps.split("[ x]+");
            if (fpsValues.length == 2) {
                try {
                    cameraFps = Integer.parseInt(fpsValues[0]);
                } catch (NumberFormatException e) {
                    cameraFps = 0;
                    Log.e(TAG, "Wrong camera fps setting: " + fps);
                }
            }
        }

        // Check capture quality slider flag.
        boolean captureQualitySlider = sharedPrefGetBoolean(R.string.pref_capturequalityslider_key,
                CallActivity.EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED,
                R.string.pref_capturequalityslider_default, useValuesFromIntent);

        // Get video and audio start bitrate.
        int videoStartBitrate = 0;
        if (useValuesFromIntent) {
            videoStartBitrate = getActivity().getIntent().getIntExtra(CallActivity.EXTRA_VIDEO_BITRATE, 0);
        }
        if (videoStartBitrate == 0) {
            String bitrateTypeDefault = getString(R.string.pref_maxvideobitrate_default);
            String bitrateType = sharedPref.getString(keyprefVideoBitrateType, bitrateTypeDefault);
            if (!bitrateType.equals(bitrateTypeDefault)) {
                String bitrateValue = sharedPref.getString(
                        keyprefVideoBitrateValue, getString(R.string.pref_maxvideobitratevalue_default));
                videoStartBitrate = Integer.parseInt(bitrateValue);
            }
        }

        int audioStartBitrate = 0;
        if (useValuesFromIntent) {
            audioStartBitrate = getActivity().getIntent().getIntExtra(CallActivity.EXTRA_AUDIO_BITRATE, 0);
        }
        if (audioStartBitrate == 0) {
            String bitrateTypeDefault = getString(R.string.pref_startaudiobitrate_default);
            String bitrateType = sharedPref.getString(keyprefAudioBitrateType, bitrateTypeDefault);
            if (!bitrateType.equals(bitrateTypeDefault)) {
                String bitrateValue = sharedPref.getString(
                        keyprefAudioBitrateValue, getString(R.string.pref_startaudiobitratevalue_default));
                audioStartBitrate = Integer.parseInt(bitrateValue);
            }
        }

        // Check statistics display option.
        boolean displayHud = sharedPrefGetBoolean(R.string.pref_displayhud_key,
                CallActivity.EXTRA_DISPLAY_HUD, R.string.pref_displayhud_default, useValuesFromIntent);

        boolean tracing = sharedPrefGetBoolean(R.string.pref_tracing_key, CallActivity.EXTRA_TRACING,
                R.string.pref_tracing_default, useValuesFromIntent);

        // Get datachannel options
        boolean dataChannelEnabled = sharedPrefGetBoolean(R.string.pref_enable_datachannel_key,
                CallActivity.EXTRA_DATA_CHANNEL_ENABLED, R.string.pref_enable_datachannel_default,
                useValuesFromIntent);
        boolean ordered = sharedPrefGetBoolean(R.string.pref_ordered_key, CallActivity.EXTRA_ORDERED,
                R.string.pref_ordered_default, useValuesFromIntent);
        boolean negotiated = sharedPrefGetBoolean(R.string.pref_negotiated_key,
                CallActivity.EXTRA_NEGOTIATED, R.string.pref_negotiated_default, useValuesFromIntent);
        int maxRetrMs = sharedPrefGetInteger(R.string.pref_max_retransmit_time_ms_key,
                CallActivity.EXTRA_MAX_RETRANSMITS_MS, R.string.pref_max_retransmit_time_ms_default,
                useValuesFromIntent);
        int maxRetr =
                sharedPrefGetInteger(R.string.pref_max_retransmits_key, CallActivity.EXTRA_MAX_RETRANSMITS,
                        R.string.pref_max_retransmits_default, useValuesFromIntent);
        int id = sharedPrefGetInteger(R.string.pref_data_id_key, CallActivity.EXTRA_ID,
                R.string.pref_data_id_default, useValuesFromIntent);
        String protocol = sharedPrefGetString(R.string.pref_data_protocol_key,
                CallActivity.EXTRA_PROTOCOL, R.string.pref_data_protocol_default, useValuesFromIntent);


        // Start AppRTCMobile activity.
        Log.d(TAG, "Connecting to room " + roomId + " at URL " + roomUrl);
        if (validateUrl(roomUrl)) {
            Uri uri = Uri.parse(roomUrl);
            Intent intent = new Intent(con, CallActivity.class);
            intent.setData(uri);
            intent.putExtra(CallActivity.EXTRA_ROOMID, roomId);
            intent.putExtra(CallActivity.EXTRA_LOOPBACK, loopback);
            intent.putExtra(CallActivity.EXTRA_VIDEO_CALL, videoCallEnabled);
            intent.putExtra(CallActivity.EXTRA_SCREENCAPTURE, useScreencapture);
            intent.putExtra(CallActivity.EXTRA_CAMERA2, useCamera2);
            intent.putExtra(CallActivity.EXTRA_VIDEO_WIDTH, videoWidth);
            intent.putExtra(CallActivity.EXTRA_VIDEO_HEIGHT, videoHeight);
            intent.putExtra(CallActivity.EXTRA_VIDEO_FPS, cameraFps);
            intent.putExtra(CallActivity.EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED, captureQualitySlider);
            intent.putExtra(CallActivity.EXTRA_VIDEO_BITRATE, videoStartBitrate);
            intent.putExtra(CallActivity.EXTRA_VIDEOCODEC, videoCodec);
            intent.putExtra(CallActivity.EXTRA_HWCODEC_ENABLED, hwCodec);
            intent.putExtra(CallActivity.EXTRA_CAPTURETOTEXTURE_ENABLED, captureToTexture);
            intent.putExtra(CallActivity.EXTRA_FLEXFEC_ENABLED, flexfecEnabled);
            intent.putExtra(CallActivity.EXTRA_NOAUDIOPROCESSING_ENABLED, noAudioProcessing);
            intent.putExtra(CallActivity.EXTRA_AECDUMP_ENABLED, aecDump);
            intent.putExtra(CallActivity.EXTRA_OPENSLES_ENABLED, useOpenSLES);
            intent.putExtra(CallActivity.EXTRA_DISABLE_BUILT_IN_AEC, disableBuiltInAEC);
            intent.putExtra(CallActivity.EXTRA_DISABLE_BUILT_IN_AGC, disableBuiltInAGC);
            intent.putExtra(CallActivity.EXTRA_DISABLE_BUILT_IN_NS, disableBuiltInNS);
            intent.putExtra(CallActivity.EXTRA_ENABLE_LEVEL_CONTROL, enableLevelControl);
            intent.putExtra(CallActivity.EXTRA_DISABLE_WEBRTC_AGC_AND_HPF, disableWebRtcAGCAndHPF);
            intent.putExtra(CallActivity.EXTRA_AUDIO_BITRATE, audioStartBitrate);
            intent.putExtra(CallActivity.EXTRA_AUDIOCODEC, audioCodec);
            intent.putExtra(CallActivity.EXTRA_DISPLAY_HUD, displayHud);
            intent.putExtra(CallActivity.EXTRA_TRACING, tracing);
            intent.putExtra(CallActivity.EXTRA_CMDLINE, commandLineRun);
            intent.putExtra(CallActivity.EXTRA_RUNTIME, runTimeMs);

            intent.putExtra(CallActivity.EXTRA_DATA_CHANNEL_ENABLED, dataChannelEnabled);

            if (dataChannelEnabled) {
                intent.putExtra(CallActivity.EXTRA_ORDERED, ordered);
                intent.putExtra(CallActivity.EXTRA_MAX_RETRANSMITS_MS, maxRetrMs);
                intent.putExtra(CallActivity.EXTRA_MAX_RETRANSMITS, maxRetr);
                intent.putExtra(CallActivity.EXTRA_PROTOCOL, protocol);
                intent.putExtra(CallActivity.EXTRA_NEGOTIATED, negotiated);
                intent.putExtra(CallActivity.EXTRA_ID, id);
            }

            if (useValuesFromIntent) {
                if (getActivity().getIntent().hasExtra(CallActivity.EXTRA_VIDEO_FILE_AS_CAMERA)) {
                    String videoFileAsCamera =
                            getActivity().getIntent().getStringExtra(CallActivity.EXTRA_VIDEO_FILE_AS_CAMERA);
                    intent.putExtra(CallActivity.EXTRA_VIDEO_FILE_AS_CAMERA, videoFileAsCamera);
                }

                if (getActivity().getIntent().hasExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE)) {
                    String saveRemoteVideoToFile =
                            getActivity().getIntent().getStringExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE);
                    intent.putExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE, saveRemoteVideoToFile);
                }

                if (getActivity().getIntent().hasExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_WIDTH)) {
                    int videoOutWidth =
                            getActivity().getIntent().getIntExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_WIDTH, 0);
                    intent.putExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_WIDTH, videoOutWidth);
                }

                if (getActivity().getIntent().hasExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_HEIGHT)) {
                    int videoOutHeight =
                            getActivity().getIntent().getIntExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_HEIGHT, 0);
                    intent.putExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_HEIGHT, videoOutHeight);
                }
            }

            startActivityForResult(intent, CONNECTION_REQUEST);
        }
    }

    private boolean sharedPrefGetBoolean(
            int attributeId, String intentName, int defaultId, boolean useFromIntent) {
        boolean defaultValue = Boolean.valueOf(getString(defaultId));
        if (useFromIntent) {
            return getActivity().getIntent().getBooleanExtra(intentName, defaultValue);
        } else {
            String attributeName = getString(attributeId);
            return sharedPref.getBoolean(attributeName, defaultValue);
        }
    }

    private String sharedPrefGetString(
            int attributeId, String intentName, int defaultId, boolean useFromIntent) {
        String defaultValue = getString(defaultId);
        if (useFromIntent) {
            String value = getActivity().getIntent().getStringExtra(intentName);
            if (value != null) {
                return value;
            }
            return defaultValue;
        } else {
            String attributeName = getString(attributeId);
            return sharedPref.getString(attributeName, defaultValue);
        }
    }

    /**
     * Get a value from the shared preference or from the intent, if it does not
     * exist the default is used.
     */
    private int sharedPrefGetInteger(
            int attributeId, String intentName, int defaultId, boolean useFromIntent) {
        String defaultString = getString(defaultId);
        int defaultValue = Integer.parseInt(defaultString);
        if (useFromIntent) {
            return getActivity().getIntent().getIntExtra(intentName, defaultValue);
        } else {
            String attributeName = getString(attributeId);
            String value = sharedPref.getString(attributeName, defaultString);
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                Log.e(TAG, "Wrong setting for: " + attributeName + ":" + value);
                return defaultValue;
            }
        }

    }

    private boolean validateUrl(String url) {
        if (URLUtil.isHttpsUrl(url) || URLUtil.isHttpUrl(url)) {
            return true;
        }

        new AlertDialog.Builder(con)
                .setTitle(getText(R.string.invalid_url_title))
                .setMessage(getString(R.string.invalid_url_text, url))
                .setCancelable(false)
                .setNeutralButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .create()
                .show();
        return false;
    }


    public void connectToRoomVedio(String roomId, boolean commandLineRun, boolean loopback,
                                   boolean useValuesFromIntent, int runTimeMs) {
        this.commandLineRun = commandLineRun;

        // roomId is random for loopback.
        if (loopback) {
            roomId = Integer.toString((new Random()).nextInt(100000000));
        }


        String roomUrl = sharedPref.getString(
                keyprefRoomServerUrl, getString(R.string.pref_room_server_url_default));

/*

    // Video call enabled flag.
    boolean videoCallEnabled = sharedPrefGetBoolean(R.string.pref_videocall_key,
            CallActivity.EXTRA_VIDEO_CALL_UNABLE, R.string.pref_videocall_default1, useValuesFromIntent);
*/


        boolean videoCallEnabled = true;

        // Use screencapture option.
        boolean useScreencapture = sharedPrefGetBoolean(R.string.pref_screencapture_key,
                CallActivity.EXTRA_SCREENCAPTURE, R.string.pref_screencapture_default, useValuesFromIntent);

        // Use Camera2 option.
        boolean useCamera2 = sharedPrefGetBoolean(R.string.pref_camera2_key, CallActivity.EXTRA_CAMERA2,
                R.string.pref_camera2_default, useValuesFromIntent);

        // Get default codecs.
        String videoCodec = sharedPrefGetString(R.string.pref_videocodec_key,
                CallActivity.EXTRA_VIDEOCODEC, R.string.pref_videocodec_default, useValuesFromIntent);
        String audioCodec = sharedPrefGetString(R.string.pref_audiocodec_key,
                CallActivity.EXTRA_AUDIOCODEC, R.string.pref_audiocodec_default, useValuesFromIntent);

        // Check HW codec flag.
        boolean hwCodec = sharedPrefGetBoolean(R.string.pref_hwcodec_key,
                CallActivity.EXTRA_HWCODEC_ENABLED, R.string.pref_hwcodec_default, useValuesFromIntent);

        // Check Capture to texture.
        boolean captureToTexture = sharedPrefGetBoolean(R.string.pref_capturetotexture_key,
                CallActivity.EXTRA_CAPTURETOTEXTURE_ENABLED, R.string.pref_capturetotexture_default,
                useValuesFromIntent);

        // Check FlexFEC.
        boolean flexfecEnabled = sharedPrefGetBoolean(R.string.pref_flexfec_key,
                CallActivity.EXTRA_FLEXFEC_ENABLED, R.string.pref_flexfec_default, useValuesFromIntent);

        // Check Disable Audio Processing flag.
        boolean noAudioProcessing = sharedPrefGetBoolean(R.string.pref_noaudioprocessing_key,
                CallActivity.EXTRA_NOAUDIOPROCESSING_ENABLED, R.string.pref_noaudioprocessing_default,
                useValuesFromIntent);

        // Check Disable Audio Processing flag.
        boolean aecDump = sharedPrefGetBoolean(R.string.pref_aecdump_key,
                CallActivity.EXTRA_AECDUMP_ENABLED, R.string.pref_aecdump_default, useValuesFromIntent);

        // Check OpenSL ES enabled flag.
        boolean useOpenSLES = sharedPrefGetBoolean(R.string.pref_opensles_key,
                CallActivity.EXTRA_OPENSLES_ENABLED, R.string.pref_opensles_default, useValuesFromIntent);

        // Check Disable built-in AEC flag.
        boolean disableBuiltInAEC = sharedPrefGetBoolean(R.string.pref_disable_built_in_aec_key,
                CallActivity.EXTRA_DISABLE_BUILT_IN_AEC, R.string.pref_disable_built_in_aec_default,
                useValuesFromIntent);

        // Check Disable built-in AGC flag.
        boolean disableBuiltInAGC = sharedPrefGetBoolean(R.string.pref_disable_built_in_agc_key,
                CallActivity.EXTRA_DISABLE_BUILT_IN_AGC, R.string.pref_disable_built_in_agc_default,
                useValuesFromIntent);

        // Check Disable built-in NS flag.
        boolean disableBuiltInNS = sharedPrefGetBoolean(R.string.pref_disable_built_in_ns_key,
                CallActivity.EXTRA_DISABLE_BUILT_IN_NS, R.string.pref_disable_built_in_ns_default,
                useValuesFromIntent);

        // Check Enable level control.
        boolean enableLevelControl = sharedPrefGetBoolean(R.string.pref_enable_level_control_key,
                CallActivity.EXTRA_ENABLE_LEVEL_CONTROL, R.string.pref_enable_level_control_key,
                useValuesFromIntent);

        // Check Disable gain control
        boolean disableWebRtcAGCAndHPF = sharedPrefGetBoolean(
                R.string.pref_disable_webrtc_agc_and_hpf_key, CallActivity.EXTRA_DISABLE_WEBRTC_AGC_AND_HPF,
                R.string.pref_disable_webrtc_agc_and_hpf_key, useValuesFromIntent);

        // Get video resolution from settings.
        int videoWidth = 0;
        int videoHeight = 0;
        if (useValuesFromIntent) {
            videoWidth = getActivity().getIntent().getIntExtra(CallActivity.EXTRA_VIDEO_WIDTH, 0);
            videoHeight = getActivity().getIntent().getIntExtra(CallActivity.EXTRA_VIDEO_HEIGHT, 0);
        }
        if (videoWidth == 0 && videoHeight == 0) {
            String resolution =
                    sharedPref.getString(keyprefResolution, getString(R.string.pref_resolution_default));
            String[] dimensions = resolution.split("[ x]+");
            if (dimensions.length == 2) {
                try {
                    videoWidth = Integer.parseInt(dimensions[0]);
                    videoHeight = Integer.parseInt(dimensions[1]);
                } catch (NumberFormatException e) {
                    videoWidth = 0;
                    videoHeight = 0;
                    Log.e(TAG, "Wrong video resolution setting: " + resolution);
                }
            }
        }

        // Get camera fps from settings.
        int cameraFps = 0;
        if (useValuesFromIntent) {
            cameraFps = getActivity().getIntent().getIntExtra(CallActivity.EXTRA_VIDEO_FPS, 0);
        }
        if (cameraFps == 0) {
            String fps = sharedPref.getString(keyprefFps, getString(R.string.pref_fps_default));
            String[] fpsValues = fps.split("[ x]+");
            if (fpsValues.length == 2) {
                try {
                    cameraFps = Integer.parseInt(fpsValues[0]);
                } catch (NumberFormatException e) {
                    cameraFps = 0;
                    Log.e(TAG, "Wrong camera fps setting: " + fps);
                }
            }
        }

        // Check capture quality slider flag.
        boolean captureQualitySlider = sharedPrefGetBoolean(R.string.pref_capturequalityslider_key,
                CallActivity.EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED,
                R.string.pref_capturequalityslider_default, useValuesFromIntent);

        // Get video and audio start bitrate.
        int videoStartBitrate = 0;
        if (useValuesFromIntent) {
            videoStartBitrate = getActivity().getIntent().getIntExtra(CallActivity.EXTRA_VIDEO_BITRATE, 0);
        }
        if (videoStartBitrate == 0) {
            String bitrateTypeDefault = getString(R.string.pref_maxvideobitrate_default);
            String bitrateType = sharedPref.getString(keyprefVideoBitrateType, bitrateTypeDefault);
            if (!bitrateType.equals(bitrateTypeDefault)) {
                String bitrateValue = sharedPref.getString(
                        keyprefVideoBitrateValue, getString(R.string.pref_maxvideobitratevalue_default));
                videoStartBitrate = Integer.parseInt(bitrateValue);
            }
        }

        int audioStartBitrate = 0;
        if (useValuesFromIntent) {
            audioStartBitrate = getActivity().getIntent().getIntExtra(CallActivity.EXTRA_AUDIO_BITRATE, 0);
        }
        if (audioStartBitrate == 0) {
            String bitrateTypeDefault = getString(R.string.pref_startaudiobitrate_default);
            String bitrateType = sharedPref.getString(keyprefAudioBitrateType, bitrateTypeDefault);
            if (!bitrateType.equals(bitrateTypeDefault)) {
                String bitrateValue = sharedPref.getString(
                        keyprefAudioBitrateValue, getString(R.string.pref_startaudiobitratevalue_default));
                audioStartBitrate = Integer.parseInt(bitrateValue);
            }
        }

        // Check statistics display option.
        boolean displayHud = sharedPrefGetBoolean(R.string.pref_displayhud_key,
                CallActivity.EXTRA_DISPLAY_HUD, R.string.pref_displayhud_default, useValuesFromIntent);

        boolean tracing = sharedPrefGetBoolean(R.string.pref_tracing_key, CallActivity.EXTRA_TRACING,
                R.string.pref_tracing_default, useValuesFromIntent);

        // Get datachannel options
        boolean dataChannelEnabled = sharedPrefGetBoolean(R.string.pref_enable_datachannel_key,
                CallActivity.EXTRA_DATA_CHANNEL_ENABLED, R.string.pref_enable_datachannel_default,
                useValuesFromIntent);
        boolean ordered = sharedPrefGetBoolean(R.string.pref_ordered_key, CallActivity.EXTRA_ORDERED,
                R.string.pref_ordered_default, useValuesFromIntent);
        boolean negotiated = sharedPrefGetBoolean(R.string.pref_negotiated_key,
                CallActivity.EXTRA_NEGOTIATED, R.string.pref_negotiated_default, useValuesFromIntent);
        int maxRetrMs = sharedPrefGetInteger(R.string.pref_max_retransmit_time_ms_key,
                CallActivity.EXTRA_MAX_RETRANSMITS_MS, R.string.pref_max_retransmit_time_ms_default,
                useValuesFromIntent);
        int maxRetr =
                sharedPrefGetInteger(R.string.pref_max_retransmits_key, CallActivity.EXTRA_MAX_RETRANSMITS,
                        R.string.pref_max_retransmits_default, useValuesFromIntent);
        int id = sharedPrefGetInteger(R.string.pref_data_id_key, CallActivity.EXTRA_ID,
                R.string.pref_data_id_default, useValuesFromIntent);
        String protocol = sharedPrefGetString(R.string.pref_data_protocol_key,
                CallActivity.EXTRA_PROTOCOL, R.string.pref_data_protocol_default, useValuesFromIntent);

        // Start AppRTCMobile activity.
        Log.d(TAG, "Connecting to room " + roomId + " at URL " + roomUrl);
        if (validateUrl(roomUrl)) {
            Uri uri = Uri.parse(roomUrl);
            Intent intent = new Intent(con, CallActivity.class);
            intent.setData(uri);
            intent.putExtra(CallActivity.EXTRA_ROOMID, roomId);
            intent.putExtra(CallActivity.EXTRA_LOOPBACK, loopback);
            intent.putExtra(CallActivity.EXTRA_VIDEO_CALL_UNABLE, videoCallEnabled);
            intent.putExtra(CallActivity.EXTRA_SCREENCAPTURE, useScreencapture);
            intent.putExtra(CallActivity.EXTRA_CAMERA2, useCamera2);
            intent.putExtra(CallActivity.EXTRA_VIDEO_WIDTH, videoWidth);
            intent.putExtra(CallActivity.EXTRA_VIDEO_HEIGHT, videoHeight);
            intent.putExtra(CallActivity.EXTRA_VIDEO_FPS, cameraFps);
            intent.putExtra(CallActivity.EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED, captureQualitySlider);
            intent.putExtra(CallActivity.EXTRA_VIDEO_BITRATE, videoStartBitrate);
            intent.putExtra(CallActivity.EXTRA_VIDEOCODEC, videoCodec);
            intent.putExtra(CallActivity.EXTRA_HWCODEC_ENABLED, hwCodec);
            intent.putExtra(CallActivity.EXTRA_CAPTURETOTEXTURE_ENABLED, captureToTexture);
            intent.putExtra(CallActivity.EXTRA_FLEXFEC_ENABLED, flexfecEnabled);
            intent.putExtra(CallActivity.EXTRA_NOAUDIOPROCESSING_ENABLED, noAudioProcessing);
            intent.putExtra(CallActivity.EXTRA_AECDUMP_ENABLED, aecDump);
            intent.putExtra(CallActivity.EXTRA_OPENSLES_ENABLED, useOpenSLES);
            intent.putExtra(CallActivity.EXTRA_DISABLE_BUILT_IN_AEC, disableBuiltInAEC);
            intent.putExtra(CallActivity.EXTRA_DISABLE_BUILT_IN_AGC, disableBuiltInAGC);
            intent.putExtra(CallActivity.EXTRA_DISABLE_BUILT_IN_NS, disableBuiltInNS);
            intent.putExtra(CallActivity.EXTRA_ENABLE_LEVEL_CONTROL, enableLevelControl);
            intent.putExtra(CallActivity.EXTRA_DISABLE_WEBRTC_AGC_AND_HPF, disableWebRtcAGCAndHPF);
            intent.putExtra(CallActivity.EXTRA_AUDIO_BITRATE, audioStartBitrate);
            intent.putExtra(CallActivity.EXTRA_AUDIOCODEC, audioCodec);
            intent.putExtra(CallActivity.EXTRA_DISPLAY_HUD, displayHud);
            intent.putExtra(CallActivity.EXTRA_TRACING, tracing);
            intent.putExtra(CallActivity.EXTRA_CMDLINE, commandLineRun);
            intent.putExtra(CallActivity.EXTRA_RUNTIME, runTimeMs);

            intent.putExtra(CallActivity.EXTRA_DATA_CHANNEL_ENABLED, dataChannelEnabled);

            if (dataChannelEnabled) {
                intent.putExtra(CallActivity.EXTRA_ORDERED, ordered);
                intent.putExtra(CallActivity.EXTRA_MAX_RETRANSMITS_MS, maxRetrMs);
                intent.putExtra(CallActivity.EXTRA_MAX_RETRANSMITS, maxRetr);
                intent.putExtra(CallActivity.EXTRA_PROTOCOL, protocol);
                intent.putExtra(CallActivity.EXTRA_NEGOTIATED, negotiated);
                intent.putExtra(CallActivity.EXTRA_ID, id);
            }

            if (useValuesFromIntent) {
                if (getActivity().getIntent().hasExtra(CallActivity.EXTRA_VIDEO_FILE_AS_CAMERA)) {
                    String videoFileAsCamera =
                            getActivity().getIntent().getStringExtra(CallActivity.EXTRA_VIDEO_FILE_AS_CAMERA);
                    intent.putExtra(CallActivity.EXTRA_VIDEO_FILE_AS_CAMERA, videoFileAsCamera);
                }

                if (getActivity().getIntent().hasExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE)) {
                    String saveRemoteVideoToFile =
                            getActivity().getIntent().getStringExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE);
                    intent.putExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE, saveRemoteVideoToFile);
                }

                if (getActivity().getIntent().hasExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_WIDTH)) {
                    int videoOutWidth =
                            getActivity().getIntent().getIntExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_WIDTH, 0);
                    intent.putExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_WIDTH, videoOutWidth);
                }

                if (getActivity().getIntent().hasExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_HEIGHT)) {
                    int videoOutHeight =
                            getActivity().getIntent().getIntExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_HEIGHT, 0);
                    intent.putExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_HEIGHT, videoOutHeight);
                }
            }

            startActivityForResult(intent, CONNECTION_REQUEST);
        }
    }

    @Override
    public void onResume() {

        h.postDelayed(runnable = new Runnable() {
            public void run() {
                //do something

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("from", PersistData.getStringData(con, AppConstant.userId));
                    jsonObject.put("text", PersistData.getStringData(con, AppConstant.userName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                mSocket.emit("login", jsonObject);

                h.postDelayed(runnable, delay);
            }
        }, delay);


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
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
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


    /* get last conversation list*/
    public ArrayList<ChatUserInfo> getLastConversationfromDB() {

        ArrayList<ChatUserInfo> conversationlist = new ArrayList<>();


        MyDbHelper db = MyDbHelper.getInstance(con);
        JsonParser parser = new JsonParser();
        JsonElement mJson = parser.parse(db.getResponse(Config.LOGIN_API));
        Gson gson = new Gson();
        SigninResponse object = gson.fromJson(mJson, SigninResponse.class);

        conversationlist = object.getUnreadConv();
        return conversationlist;


    }





    /*get conversation id by specepic user*/

    public String getConversationIdfromdb(String userid) {

        Log.e("reciver id", userid);
        String conversation_id = "";
        MyDbHelper db = MyDbHelper.getInstance(con);
        JsonParser parser = new JsonParser();
        JsonElement mJson = parser.parse(db.getResponse(Config.LOGIN_API));
        Gson gson = new Gson();
        SigninResponse object = gson.fromJson(mJson, SigninResponse.class);


        for (int i = 0; object.getConversations().size() > i; i++) {
            // Log.e("conversation id name ", object.getConversations().get(i).getConversation_id());

            for (int j = 0; object.getConversations().get(i).getParticipants().size() > j; j++) {
                // Log.e("ppp", object.getConversations().get(i).getParticipants().get(j) + " tst");

                if (object.getConversations().get(i).getSingle().equalsIgnoreCase("yes")) {

                    if (object.getConversations().get(i).getParticipants().get(j).equalsIgnoreCase(userid)) {
                        conversation_id = object.getConversations().get(i).getConversation_id();
                        Log.e("find cid ", conversation_id);
                        //  Toast.makeText(con, object.getConversations().get(i).getConversation_id(), Toast.LENGTH_LONG).show();
                        // return conversation_id;
                    } else {
                        //  Toast.makeText(con, "no conversation id", Toast.LENGTH_LONG).show();
                    }

                }


            }
        }

        return conversation_id;
    }

    private Emitter.Listener onLogout = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];

            // Toast.makeText(con, "test", Toast.LENGTH_LONG).show();

            Log.e("1345 logout data", data.toString());
//            Toast.makeText(getApplicationContext(), data.toString(), Toast.LENGTH_LONG).show();
            String userdata = "";
            try {
                userdata = data.getString("userdata");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject user = new JSONObject(userdata);
                String id = user.getString("from");
                String name = user.getString("text");
                Log.e("1358 logout person", name);
                // Log.e("online size o",AppConstant.onlinelist.size()+" r");

                if (AppConstant.onlinelist.contains(id)) {
                    AppConstant.onlinelist.remove(id);
                    // Log.e("online size n",AppConstant.onlinelist.size()+" r");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // startActivity(new Intent(con,));


        }
    };


    private Emitter.Listener onUserList = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];

            // Toast.makeText(con, "test", Toast.LENGTH_LONG).show();

            Log.e("test", data.toString());
            // startActivity(new Intent(con,));

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

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
/*

                            // Stuff that updates the UI
                            adapter1 = new CallBaseAdapter(con, getUserListfromDB(), HomeFragment.this);
                            listCall.setAdapter(adapter1);
                            adapter1.notifyDataSetChanged();
*/

                            listCall.invalidate();
                            adapter1.notifyDataSetChanged();

                        }
                    });
                }

                Log.e("from", from);
                Log.e("text", text);
            } catch (JSONException e) {

                return;
            }
            // finish();
        }
    };


    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            JSONObject data = (JSONObject) args[0];
            Log.e("login", data.toString());
            // startActivity(new Intent(con,));


            // finish();
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        // mSocket.disconnect();
    }

    // find  name by id
    public String findIdByName(String value) {
        UserInfo pd;
        String data = "";
        for (int i = 0; getUserListfromDB().size() > i; i++) {
            pd = getUserListfromDB().get(i);
            if (pd.getFullname().equalsIgnoreCase(value)) {
                data = pd.getId();
            }
        }
        return data;
    }


    private Emitter.Listener onMesseageRecived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];

            // Toast.makeText(con, "test", Toast.LENGTH_LONG).show();

            Log.e("test 738", data.toString());
            // startActivity(new Intent(con,));


            try {
                sender_id = data.getString("msg_from");
                sender_name = data.getString("msg_sender_name");
                sender_img = data.getString("msg_sender_img");
                msg_id = data.getString("msg_id");
                msg_conv_id = data.getString("msg_conv_id");
                text = data.getString("msg_text");




            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        for (int i = 0; conversationList.size() > i; i++) {

                            if (msg_conv_id.equalsIgnoreCase(conversationList.get(i).getConversation_id())) {

                                /* update this list data*/
                                Log.e("ccc", "user p  " + i);
                                Log.e("ccc", "unread a " + conversationList.get(i).getTotalUnread());
                                Log.e("ccc", "cid " + msg_conv_id);

                                int totalUnread = Integer.parseInt(conversationList.get(i).getTotalUnread()) + 1;

                                ChatUserInfo chatuserInfo1 = new ChatUserInfo();

                                chatuserInfo1.setConversation_id(msg_conv_id);
                                chatuserInfo1.setMsg_body(text);
                                chatuserInfo1.setCreated_at(Instant.now().toString());
                                chatuserInfo1.setMsg_id(msg_id);
                                chatuserInfo1.setMsg_type("direct");
                                chatuserInfo1.setSender_img(sender_img);
                                chatuserInfo1.setConversation_title(sender_name);
                                // chatuserInfo.setTotalUnread("1");
                                chatuserInfo1.setTotalUnread(String.valueOf(totalUnread));
                                conversationList.set(i, chatuserInfo1);

                                Log.e("ccc", "list size a " + conversationList.size());
                                Log.e("ccc", "unread l " + conversationList.get(i).getTotalUnread());


                                PersistData.setStringData(con, AppConstant.lastConversation_id, msg_conv_id);

                                listChats.invalidate();
                                adapter.notifyDataSetChanged();

                                /*adapter = new ChatsWorkbaseUserAdapter(con, conversationList, HomeFragment.this);
                                listChats.setAdapter(adapter);*/

                                return;

                            } else {

                            }

                        }


                    }
                });

            }


        }
    };

}
