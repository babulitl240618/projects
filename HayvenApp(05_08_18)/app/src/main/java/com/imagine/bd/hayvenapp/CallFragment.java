/*
 *  Copyright 2015 The WebRTC Project Authors. All rights reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree. An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */

package com.imagine.bd.hayvenapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.imagine.bd.hayvenapp.Database.MyDbHelper;
import com.imagine.bd.hayvenapp.Model.SigninResponse;
import com.imagine.bd.hayvenapp.Model.UserInfo;
import com.imagine.bd.hayvenapp.utils.API_URL;
import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.Config;
import com.imagine.bd.hayvenapp.utils.NotificationUtils;
import com.imagine.bd.hayvenapp.utils.PersistData;
import com.imagine.bd.hayvenapp.utils.RoundedTransformation;
import com.squareup.picasso.Picasso;

import org.webrtc.PeerConnectionFactory;
import org.webrtc.RendererCommon.ScalingType;

import java.util.ArrayList;

import static org.webrtc.ContextUtils.getApplicationContext;

/**
 * Fragment for call control.
 */
public class CallFragment extends Fragment {
    private View controlView;
    private Context con;
    private PeerConnectionFactory factory;
    private TextView contactView, tvContactName;
    private ImageButton disconnectButton;
    private ImageButton cameraSwitchButton;
    private ImageButton videoScalingButton;
    private ImageButton toggleMuteButton;
    private TextView captureFormatText;
    private SeekBar captureFormatSlider;
    private OnCallEvents callEvents;
    private ScalingType scalingType;
    private boolean videoCallEnabled = true;
    FrameLayout fm;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Chronometer focus;
    AudioManager audioManager;
    Boolean isloudSpeaker = true;
    Boolean isvedio = true;
    Boolean ismute = true;

    /**
     * Call control interface for container activity.
     */
    public interface OnCallEvents {
        void onCallHangUp();

        void onCameraSwitch();

        void onVideoScalingSwitch(ScalingType scalingType);

        void onCaptureFormatChange(int width, int height, int framerate);

        boolean onToggleMic();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        controlView = inflater.inflate(R.layout.fragment_call, container, false);

        // Create UI controls.
        focus = (Chronometer) controlView.findViewById(R.id.cronometer);
        tvContactName = (TextView) controlView.findViewById(R.id.tvContactName);
        contactView = (TextView) controlView.findViewById(R.id.contact_name_call);
        disconnectButton = (ImageButton) controlView.findViewById(R.id.button_call_disconnect);
        cameraSwitchButton = (ImageButton) controlView.findViewById(R.id.button_call_switch_camera);
        videoScalingButton = (ImageButton) controlView.findViewById(R.id.button_call_scaling_mode);
        toggleMuteButton = (ImageButton) controlView.findViewById(R.id.button_call_toggle_mic);
        captureFormatText = (TextView) controlView.findViewById(R.id.capture_format_text_call);
        captureFormatSlider = (SeekBar) controlView.findViewById(R.id.capture_format_slider_call);
        fm = (FrameLayout) getActivity().findViewById(R.id.call_fragment_container);

        // Add buttons click events.
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callEvents.onCallHangUp();
            }
        });


        cameraSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                callEvents.onCameraSwitch();
                //   fm.setBackgroundColor(0x00000000);


                if(isvedio){
                    isvedio=false;
                    cameraSwitchButton.setBackgroundResource(R.drawable.video_off_56);

                }else{
                    isvedio=true;
                    cameraSwitchButton.setBackgroundResource(R.drawable.video_on_56);
                }



            }
        });


        videoScalingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isloudSpeaker) {
                    isloudSpeaker = false;
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                    audioManager.setMode(AudioManager.MODE_NORMAL);
                    videoScalingButton.setBackgroundResource(R.drawable.speaker_off_56);
                   // Toast.makeText(con, "speaker off", Toast.LENGTH_LONG).show();
                } else {
                    isloudSpeaker = true;
                    audioManager.setMode(AudioManager.MODE_NORMAL);
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                    videoScalingButton.setBackgroundResource(R.drawable.speaker_on_56);
                   // Toast.makeText(con, "speaker on", Toast.LENGTH_LONG).show();
                }

                audioManager.setSpeakerphoneOn(isloudSpeaker);


             /*   if (scalingType == ScalingType.SCALE_ASPECT_FILL) {
                    videoScalingButton.setBackgroundResource(R.drawable.ic_action_full_screen);
                    scalingType = ScalingType.SCALE_ASPECT_FIT;
                } else {
                    videoScalingButton.setBackgroundResource(R.drawable.ic_action_return_from_full_screen);
                    scalingType = ScalingType.SCALE_ASPECT_FILL;
                }
                callEvents.onVideoScalingSwitch(scalingType);
*/
            }

        });
        scalingType = ScalingType.SCALE_ASPECT_FILL;

        toggleMuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ismute){
                    ismute=false;
                    toggleMuteButton.setBackgroundResource(R.drawable.mute_off_56);
                }else{
                    ismute=true;
                    toggleMuteButton.setBackgroundResource(R.drawable.mute_on_56);
                }

                boolean enabled = callEvents.onToggleMic();
                toggleMuteButton.setAlpha(enabled ? 1.0f : 0.3f);
            }
        });

        return controlView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        con = getActivity();
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        focus.setBase(SystemClock.elapsedRealtime());
        focus.start();

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

                    /*cancel call by push*/
                    callEvents.onCallHangUp();
                    // connectToRoom1(message, false, false, false, 0);

                    //  startActivity(new Intent(con,IncomingCallActivity.class));
                    Log.e("2222", " recive");

                    Toast.makeText(getActivity(), "reject call", Toast.LENGTH_LONG).show();

                    //   PersistData.setStringData(con, AppConstant.rom_id,message);


                }
            }
        };


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStart() {
        super.onStart();

        boolean captureSliderEnabled = false;
        Bundle args = getArguments();
        if (args != null) {
            String contactName = args.getString(CallActivity.EXTRA_ROOMID);
            contactView.setText(contactName);
            tvContactName.setText(PersistData.getStringData(getActivity(), AppConstant.friend_name));
            videoCallEnabled = args.getBoolean(CallActivity.EXTRA_VIDEO_CALL, true);
            captureSliderEnabled = videoCallEnabled
                    && args.getBoolean(CallActivity.EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED, false);
        }

        ImageView imgPic=(ImageView) getView().findViewById(R.id.imgPic);


        if (!videoCallEnabled) {
            imgPic.setVisibility(View.VISIBLE);
            Picasso.with(con)
                    //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                    .load(API_URL.PHOTO_BASE_URL +findImgById(PersistData.getStringData(con,AppConstant.reciverid)))
                    .transform(new RoundedTransformation(40,5))
                    .placeholder(R.drawable.fajlehrabbi1)
                    .into(imgPic);

            // defalt loudspeker off mode

            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.setMode(AudioManager.MODE_NORMAL);
            videoScalingButton.setBackgroundResource(R.drawable.speaker_off_56);


           /*
            LineBarVisualizer lineBarVisualizer = (LineBarVisualizer)getView().findViewById(R.id.visualizer);
          //  mediaPlayer = MediaPlayer.create(this, R.raw.you_music);

// set custom color to the line.
            lineBarVisualizer.setColor(ContextCompat.getColor(getActivity(), R.color.white));

// define custom number of bars you want in the visualizer between (10 - 256).
            lineBarVisualizer.setDensity(70);

// Set you media player to the visualizer.
            lineBarVisualizer.setPlayer(audioManager.generateAudioSessionId());


            */
        }else{
            imgPic.setVisibility(View.GONE);

//
//            audioManager.setMode(AudioManager.MODE_NORMAL);
//            audioManager.setMode(AudioManager.MODE_IN_CALL);
//            videoScalingButton.setBackgroundResource(R.drawable.speaker_on_56);

        }


        if (captureSliderEnabled) {
            captureFormatSlider.setOnSeekBarChangeListener(
                    new CaptureQualityController(captureFormatText, callEvents));
        } else {
            captureFormatText.setVisibility(View.GONE);
            captureFormatSlider.setVisibility(View.GONE);
        }
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


    // TODO(sakal): Replace with onAttach(Context) once we only support API level 23+.
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callEvents = (OnCallEvents) activity;
    }
    public ArrayList<UserInfo> getUserListfromDB(){

        ArrayList<UserInfo> userlist=new ArrayList<>();



        MyDbHelper db=MyDbHelper.getInstance(con);
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

    public  String findImgById(String value) {
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
}
