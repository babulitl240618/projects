package com.imagine.bd.hayvenapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.imagine.bd.hayvenapp.Adapter.ContactsAdapter;
import com.imagine.bd.hayvenapp.Adapter.NewChatAdapter;
import com.imagine.bd.hayvenapp.Database.MyDbHelper;
import com.imagine.bd.hayvenapp.Model.DataModel;
import com.imagine.bd.hayvenapp.Model.SigninResponse;
import com.imagine.bd.hayvenapp.Model.UserInfo;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.Config;
import com.imagine.bd.hayvenapp.utils.PersistData;

import java.util.ArrayList;

public class New_ChatActivity extends AppCompatActivity {
    ArrayList<UserInfo> tempArrayList;
    ListView listView;
    ImageView imageView,imgsearch;
    LinearLayout linearLayout,linearLayout1,llCrossbar;
    //private RecyclerView mRecyclerView;
    private Context con;
  //  private NewChatAdapter mAdapter;
    private NewChatAdapter adapter;
   private EditText etSearch;

    //@SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_chat);
        con=this;
        listView=(ListView)findViewById(R.id.list);
        linearLayout=(LinearLayout) findViewById(R.id.addgroupchat);
        linearLayout1=(LinearLayout) findViewById(R.id.joingroupchat);
        imageView = (ImageView) findViewById(R.id.notificationChat);
        etSearch=(EditText)findViewById(R.id.etSearch) ;
        llCrossbar=(LinearLayout)findViewById(R.id.llCrossbar);
        imgsearch=(ImageView) findViewById(R.id.imgsearch);

        llCrossbar.setVisibility(View.VISIBLE);
        etSearch.setVisibility(View.GONE);

        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llCrossbar.setVisibility(View.GONE);
                etSearch.setVisibility(View.VISIBLE);
            }
        });



        // search contactlist by edittext textwatcher
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence cs, int start, int count, int after) {
                // contactadapter.getFilter().filter(s);

                tempArrayList=new ArrayList<UserInfo>();
                int textlength = cs.length();


                for(UserInfo c: getUserListfromDB()){

                    String name = etSearch.getText().toString();
                   /* if (name.matches("")) {
                        Toast.makeText(con, "You did not enter a username", Toast.LENGTH_SHORT).show();
                        listViewcontacts.setVisibility(View.VISIBLE);
                        return;
                    }else {
                        listViewcontacts.setVisibility(View.GONE);
                    }*/

                    // chack data by character sequence and get searching list data
                    if (textlength <= c.getFullname().length()) {
                        if (c.getFullname().toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }

                }
                adapter= new NewChatAdapter(tempArrayList,con);
                listView.setAdapter(adapter);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });   // end of search view




        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(New_ChatActivity.this,NewGroupChatActivity.class));
                //Intent i = new Intent(context, Contacts.class);
                //startActivity(i);
            }
        });
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(New_ChatActivity.this,JoinGroupChatActivity.class));
                //Intent i = new Intent(context, Contacts.class);
                //startActivity(i);
            }
        });
        //mRecyclerView =(RecyclerView)findViewById(R.id.list);
      /*  dataModels= new ArrayList<>();
        dataModels.add(new DataModel("Michel john", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Jraft joy", "The ArrayAdapter will ..", "9.58am","February 9, 2009"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Alexjon hesle", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Nexon lie", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("SM Jubayer", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Soni Jubayer", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Josim Jon", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));*/
        adapter= new NewChatAdapter(getUserListfromDB(),con);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, "chat list", Toast.LENGTH_LONG).show();
             if ((tempArrayList!=null)){
                 PersistData.setStringData(con,AppConstant.friend_name,tempArrayList.get(position).getFullname());
                 startActivity(new Intent(con, ChattingActivity.class));}
                 else {
                 PersistData.setStringData(con,AppConstant.friend_name,getUserListfromDB().get(position).getFullname());
                 PersistData.setStringData(con, AppConstant.reciverid, findIdByName(getUserListfromDB().get(position).getFullname()));

                 startActivity(new Intent(con, ChattingActivity.class));
             }

            }
        });
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

}
