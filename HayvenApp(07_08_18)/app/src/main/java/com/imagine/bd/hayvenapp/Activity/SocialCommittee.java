package com.imagine.bd.hayvenapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imagine.bd.hayvenapp.Adapter.ContactsAdapter;
import com.imagine.bd.hayvenapp.Adapter.GroupMemberHorizontalListview;
import com.imagine.bd.hayvenapp.Adapter.SocialCommiteeAdapter;
import com.imagine.bd.hayvenapp.Model.DataModel;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.utils.AppConstant;

import java.util.ArrayList;

public class SocialCommittee extends AppCompatActivity {
    ArrayList<DataModel> dataModels;
    SocialCommiteeAdapter mAdapter;
   // GroupMemberHorizontalListview addAdapter;
    private Context con;
    TextView textView,tvGroupName;
    ImageView imageView;
    private RecyclerView mRecyclerView;
    private EditText etSearch;
    LinearLayout llCrossbar;
    ImageView imgsearch;
    RecyclerView recycleSelectedView;
    RecyclerView.LayoutManager mLayoutManager;
    public static RecyclerView.Adapter addAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_committee);
        con=this;
        initUI();

    }

    public void initUI(){

        String group_name=getIntent().getStringExtra("groupName");

        tvGroupName = (TextView) findViewById(R.id.tvGroupName);
        textView = (TextView) findViewById(R.id.tvNext);
        imageView = (ImageView) findViewById(R.id.notificationChat);
        etSearch=(EditText)findViewById(R.id.etSearch) ;
        llCrossbar=(LinearLayout)findViewById(R.id.llCrossbar);
        imgsearch=(ImageView) findViewById(R.id.imgsearch);
        tvGroupName.setText(group_name);

        recycleSelectedView = (RecyclerView) findViewById(R.id.recycler_view_member);
        recycleSelectedView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        RecyclerView.LayoutManager  mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        recycleSelectedView.setLayoutManager(mLayoutManager2);

        addAdapter = new GroupMemberHorizontalListview(SocialCommittee.this, AppConstant.addmemberlist);
        recycleSelectedView.setAdapter(addAdapter);



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

                ArrayList<DataModel> tempArrayList=new ArrayList<DataModel>();
                int textlength = cs.length();


                for(DataModel c: dataModels){

                    String name = etSearch.getText().toString();
                   /* if (name.matches("")) {
                        Toast.makeText(con, "You did not enter a username", Toast.LENGTH_SHORT).show();
                        listViewcontacts.setVisibility(View.VISIBLE);
                        return;
                    }else {
                        listViewcontacts.setVisibility(View.GONE);
                    }*/

                    // chack data by character sequence and get searching list data
                    if (textlength <= c.getName().length()) {
                        if (c.getName().toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }

                }
                mAdapter= new SocialCommiteeAdapter(tempArrayList,con);

                mRecyclerView.setAdapter(mAdapter);

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
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SocialCommittee.this,SocialCommiteeCallActivity.class));
                //Intent i = new Intent(context, Contacts.class);
                //startActivity(i);
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(con));

        dataModels= new ArrayList<>();
        dataModels.add(new DataModel("Michel john", "Office Manager", "9.58am","Office Manager"));
        dataModels.add(new DataModel("Araft joy", "Human Division Director", "9.58am","Human Division Director"));
        dataModels.add(new DataModel("Fajleh Rabbi", "Marketing Manager", "9.58am","Marketing Manager"));
        dataModels.add(new DataModel("SM Jubayer", "CEO,Co-Founder", "9.58am","CEO,Co-Founder"));
        dataModels.add(new DataModel("Nexon lie", "Marketing Manager", "9.58am","Marketing Manage"));
        dataModels.add(new DataModel("Alexjon hesle", "Office Manager", "9.58am","Office Manager"));
        dataModels.add(new DataModel("Fajleh Rabbi", "Office Manager", "9.58am","Office Manager"));
        dataModels.add(new DataModel("Fajleh Rabbi", "Office Manager", "9.58am","Office Manager"));
        dataModels.add(new DataModel("Michel john", "Office Manager", "9.58am","Office Manager"));
        dataModels.add(new DataModel("Michel john", "Office Manager", "9.58am","Office Manager"));
        mAdapter= new SocialCommiteeAdapter(dataModels,con);

        mRecyclerView.setAdapter(mAdapter);
    }
}
