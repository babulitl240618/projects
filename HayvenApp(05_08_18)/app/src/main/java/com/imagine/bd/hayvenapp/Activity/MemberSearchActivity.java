package com.imagine.bd.hayvenapp.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.imagine.bd.hayvenapp.Adapter.JoinGroupAdapter;
import com.imagine.bd.hayvenapp.Database.MyDbHelper;
import com.imagine.bd.hayvenapp.Model.DataModel;
import com.imagine.bd.hayvenapp.Model.SigninResponse;
import com.imagine.bd.hayvenapp.Model.UserInfo;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.Config;
import com.imagine.bd.hayvenapp.utils.PersistData;

import java.util.ArrayList;

public class MemberSearchActivity extends AppCompatActivity {
    ArrayList<DataModel> dataModels;
    JoinGroupAdapter mAdapter;
    private Context con;
    private RecyclerView listGroupChats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_search);
        con=this;
        listGroupChats = (RecyclerView) findViewById(R.id.listGroupChats);
        listGroupChats.setHasFixedSize(true);
        listGroupChats.setLayoutManager(new LinearLayoutManager(con));

        mAdapter= new JoinGroupAdapter(getUserListfromDB(),con);

        listGroupChats.setAdapter(mAdapter);

    }
    /*get all userlist from database */

    public  ArrayList<UserInfo> getUserListfromDB(){

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

}
