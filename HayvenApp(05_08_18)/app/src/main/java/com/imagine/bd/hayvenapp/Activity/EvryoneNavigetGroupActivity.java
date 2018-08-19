package com.imagine.bd.hayvenapp.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.imagine.bd.hayvenapp.Adapter.EvryoneNavigetGroupAdapter;
import com.imagine.bd.hayvenapp.Model.DataModel;
import com.imagine.bd.hayvenapp.R;

import java.util.ArrayList;

public class EvryoneNavigetGroupActivity extends AppCompatActivity {
    ArrayList<DataModel> dataModels;
    EvryoneNavigetGroupAdapter mAdapter;
    private Context con;
    TextView textView;
    ImageView imgProfil;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evryone_naviget_group);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        imgProfil = (ImageView) findViewById(R.id.imgProfil);
        imgProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
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
        mAdapter= new EvryoneNavigetGroupAdapter(dataModels,con);

        mRecyclerView.setAdapter(mAdapter);
    }
}
