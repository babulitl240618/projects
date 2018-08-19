package com.imagine.bd.hayvenapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.imagine.bd.hayvenapp.Adapter.SocialCommiteeAdapter;
import com.imagine.bd.hayvenapp.Model.DataModel;
import com.imagine.bd.hayvenapp.R;

import java.util.ArrayList;

public class GroupCallActivity extends AppCompatActivity {
    ArrayList<DataModel> dataModels;
    SocialCommiteeAdapter mAdapter;
    private Context con;
    private ImageView imageView;
    TextView textView ,textstartcall;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_call);
        con=this;
        imageView = (ImageView) findViewById(R.id.notificationChat);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        //textView = (TextView) findViewById(R.id.tvNext);
        textstartcall = (TextView) findViewById(R.id.tvstartcall);
        /*textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SocialCommittee.this,IncomingCallActivity.class));
                //Intent i = new Intent(context, Contacts.class);
                //startActivity(i);
            }
        });*/
        textstartcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupCallActivity.this,EvryoneNavigetGroupActivity.class));
                //Intent i = new Intent(context, Contacts.class);
                //startActivity(i);
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(con));

        dataModels= new ArrayList<>();
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Banana Bread", "The ArrayAdapter will ..", "9.58am","February 9, 2009"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        mAdapter= new SocialCommiteeAdapter(dataModels,con);

        mRecyclerView.setAdapter(mAdapter);
    }
}
