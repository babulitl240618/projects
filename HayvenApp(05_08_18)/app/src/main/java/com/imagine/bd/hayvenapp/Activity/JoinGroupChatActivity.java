package com.imagine.bd.hayvenapp.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.imagine.bd.hayvenapp.Adapter.JoinGroupAdapter;
import com.imagine.bd.hayvenapp.Adapter.JoinGroupChatAdapter;
import com.imagine.bd.hayvenapp.Model.DataModel;
import com.imagine.bd.hayvenapp.R;

import java.util.ArrayList;

public class JoinGroupChatActivity extends AppCompatActivity {
    ArrayList<DataModel> dataModels;
    JoinGroupChatAdapter mAdapter;
    private Context con;
    ImageView imageView;
    private RecyclerView listGroupChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group_chat);
        con=this;
        imageView = (ImageView) findViewById(R.id.notificationChat);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        listGroupChats = (RecyclerView) findViewById(R.id.listGroupChats);
        listGroupChats.setHasFixedSize(true);
        listGroupChats.setLayoutManager(new LinearLayoutManager(con));

        dataModels= new ArrayList<>();
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Banana Bread", "The ArrayAdapter will ..", "9.58am","February 9, 2009"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Manzu Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Rejaon Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008")); dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Abdul Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        mAdapter= new JoinGroupChatAdapter(dataModels,con);

        listGroupChats.setAdapter(mAdapter);


    }
}
