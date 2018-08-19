package com.imagine.bd.hayvenapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.Retrofit.IRetrofit;
import com.imagine.bd.hayvenapp.Retrofit.ServiceGenerator;
import com.imagine.bd.hayvenapp.utils.API_URL;
import com.imagine.bd.hayvenapp.utils.AlertMessage;
import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.Config;
import com.imagine.bd.hayvenapp.utils.NetInfo;
import com.imagine.bd.hayvenapp.utils.PersistData;
import com.imagine.bd.hayvenapp.utils.RoundedTransformation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallInfoActivity extends AppCompatActivity {
private ImageView imageView,imgCall,imgPic;
TextView tvName;
private Context con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_info_xml);
        con=this;
        imageView = (ImageView) findViewById(R.id.notificationChat);
        imgCall=(ImageView) findViewById(R.id.imgCall);
        imgPic=(ImageView) findViewById(R.id.imgPic);
        tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(PersistData.getStringData(con, AppConstant.friend_name));
        //String name = getIntent().getStringExtra("name");
        //String image = getIntent().getStringExtra("image");


       // tvName.setText(name);
        Picasso.with(con)
                //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                .load(API_URL.PHOTO_BASE_URL + PersistData.getStringData(con, AppConstant.userImg))
                .transform(new RoundedTransformation(40,5))
                .placeholder(R.drawable.fajlehrabbi1)
                .into(imgPic);
        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetInfo.isOnline(con)) {
                    AlertMessage.showMessage(con, "No Internet Connection!", "You need to be connected to your network ");
                    return;
                }

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

                    con.startActivity(intent);


                } else {
                    if (senderid == null) {
                        Toast.makeText(con, "sender id null", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(con, "reciver id  null", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
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


}
