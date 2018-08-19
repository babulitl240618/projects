package com.imagine.bd.hayvenapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.imagine.bd.hayvenapp.Database.MyDbHelper;
import com.imagine.bd.hayvenapp.MainActivity2;
import com.imagine.bd.hayvenapp.Model.ResponseData;
import com.imagine.bd.hayvenapp.Model.SigninResponse;
import com.imagine.bd.hayvenapp.MyApplication;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.Retrofit.IRetrofit;
import com.imagine.bd.hayvenapp.Retrofit.ServiceGenerator;
import com.imagine.bd.hayvenapp.utils.API_URL;
import com.imagine.bd.hayvenapp.utils.AlertMessage;
import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.Config;
import com.imagine.bd.hayvenapp.utils.NetInfo;
import com.imagine.bd.hayvenapp.utils.PersistData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity {

    private TextView tvLogin;
    private EditText etName, etpass;
    private Context con;
    String notificationToken = "";
    private CheckBox signCheckBox1;
    private MyDbHelper db;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin2);
        con = this;
        MyApplication app = (MyApplication) getApplication();
        db = MyDbHelper.getInstance(con);
        mSocket = app.getSocket();
        mSocket.on("login", onLogin);
        mSocket.on("online_user_list", onUserList);
        mSocket.connect();
        inittUI();
    }


    public void inittUI() {
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        etName = (EditText) findViewById(R.id.etName);
        etpass = (EditText) findViewById(R.id.etPass);
        signCheckBox1 = (CheckBox) findViewById(R.id.signCheckBox1);

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        notificationToken = FirebaseInstanceId.getInstance().getToken();

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(SigninActivity.this, MainActivity2.class));
                String name = etName.getText().toString();
                String pass = etpass.getText().toString();

                FirebaseMessaging.getInstance().subscribeToTopic("news");
                notificationToken = FirebaseInstanceId.getInstance().getToken();

                MyDbHelper db = MyDbHelper.getInstance(con);

                Log.e("data sign", db.getSigninStatus(Config.LOGIN_API));
                Log.e("data res", db.getResponse(Config.LOGIN_API));
                Log.e("data size", AppConstant.alluserlist.size() + "");
                Log.e("data user", PersistData.getStringData(con, AppConstant.userName));

                Log.e("email", name);
                Log.e("pass", pass);
                //  Log.e("token", notificationToken);

                //requestSignin("rabbi@gmail.com","123456","fhdhfjfjd");
                // requestRoom("rabbi@gmail.com","123456","fhdhfjfjd");

                if (!TextUtils.isEmpty(notificationToken)) {
                    // Log.e("token", notificationToken);
                    //  Toast.makeText(con, notificationToken, Toast.LENGTH_LONG).show();
                    requestSignin(name, pass, notificationToken);
                    //  requestSignin("jubayer@gmail.com", "123456", notificationToken);
                } else {
                    Toast.makeText(con, "not found firebase id", Toast.LENGTH_LONG).show();
                    // requestSignin(name, pass, notificationToken);
                }

            }
        });
    }


    public void requestSignin(String email, String pass, String gcm_id) {

        if (!NetInfo.isOnline(con)) {
            AlertMessage.showMessage(con, "No Internet Connection!", "You need to be connected to your network ");
            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("password", pass);
        jsonObject.addProperty("gcm_id", gcm_id);

        // Using the Retrofit
        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class, API_URL.BASE_URL);
        Call<JsonObject> call = jsonPostService.postRawJSON(jsonObject);
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
                    SigninResponse object = gson.fromJson(mJson, SigninResponse.class);



                    if (object.getMsg().equalsIgnoreCase("true")) {

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("from", object.getUser().getId());
                        jsonObject.put("text", object.getUser().getFullname());


                        mSocket.emit("login", jsonObject);

                        Log.e("id", object.getUser().getId());
                        Log.e("name", object.getUser().getFullname());
                        Log.e("size", object.getAlluserlist().size() + " tst");
                        Log.e("size c", object.getConversations().size() + " tst");

/*
                        for(int i=0;object.getConversations().size()>i;i++){
                            Log.e("conversation id name ", object.getConversations().get(i).getConversation_id());
                            for(int j=0;object.getConversations().get(i).getParticipants().size()>j;j++){
                                Log.e("ppp", object.getConversations().get(i).getParticipants().get(j) + " tst");

                                if(object.getConversations().get(i).getParticipants().get(j).equalsIgnoreCase("fadb049c-6714-4b0b-8419-d2ef2c051345")){
                                    Log.e("ffff ", object.getConversations().get(i).getConversation_id());
                                    return;
                                }
                            }
                        }*/


                        String isRemember = "";
                        if (signCheckBox1.isChecked()) {
                            isRemember = "true";
                            PersistData.setStringData(con, AppConstant.isSignin, "true");
                        } else {
                            isRemember = "false";
                            PersistData.setStringData(con, AppConstant.isSignin, "flase");
                        }


                        if (db.getResponseCountData() == 0) {

                            // insert db
                            ResponseData data = new ResponseData(Config.LOGIN_API, isRemember, responseValue);
                            db.insertResponseTable(data);
                            //  Toast.makeText(con, db.getResponseCountData() +" a", Toast.LENGTH_SHORT).show();
                        } else {

                            // update db
                            ResponseData data = new ResponseData(Config.LOGIN_API, isRemember, responseValue);
                            db.updateResponsetable(data);
                            //  Toast.makeText(con, db.getResponseCountData() +" a", Toast.LENGTH_SHORT).show();

                        }


                        PersistData.setStringData(con, AppConstant.userId, object.getUser().getId());
                        PersistData.setStringData(con, AppConstant.userName, object.getUser().getFullname());


                        Toast.makeText(con, "login success!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SigninActivity.this, MainActivity2.class));


                        if (object.getAlluserlist().size() > 0) {

                            for (int i = 0; object.getAlluserlist().size() > i; i++) {

                                if (object.getAlluserlist().get(i).getId().equalsIgnoreCase(PersistData.getStringData(con, AppConstant.userId))) {
                                    object.getAlluserlist().remove(i);
                                }
                            }

                            AppConstant.alluserlist = object.getAlluserlist();
                        }


                        finish();

                    } else {
                        Toast.makeText(con, object.getMsg(), Toast.LENGTH_LONG).show();
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

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            return false;
        }
        return true;
    }

    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Toast.makeText(con, "test", Toast.LENGTH_LONG).show();
            Log.e("test", "login");
            // startActivity(new Intent(con,));


            // finish();
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


                Log.e("from", from);
                Log.e("text", text);
            } catch (JSONException e) {

                return;
            }
            // finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.off("login", onLogin);
        mSocket.off("online_user_list", onUserList);
        mSocket.disconnect();

    }

}
