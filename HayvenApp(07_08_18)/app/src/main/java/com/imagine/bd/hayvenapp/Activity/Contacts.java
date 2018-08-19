package com.imagine.bd.hayvenapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.imagine.bd.hayvenapp.Adapter.ContactsAdapter;
import com.imagine.bd.hayvenapp.Model.DataModel;
import com.imagine.bd.hayvenapp.R;

import java.util.ArrayList;

public class Contacts extends AppCompatActivity {
    ArrayList<DataModel> dataModels;
    ListView listContacts;
    private ImageView imgContinue,imgContinue1,imgContinue2,imgContinue3,imgContinue4,imgLauncher;
    private ContactsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        listContacts=(ListView)findViewById(R.id.listContacts);
        imgLauncher=(ImageView)  findViewById(R.id.imgLauncher);

      /*  imgLauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Contacts.this, MainActivity.class));
                finish();
            }
        });
*/
        dataModels= new ArrayList<>();
        dataModels.add(new DataModel("Banana Bread", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Banana Bread", "The ArrayAdapter will ..", "9.58am","February 9, 2009"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Md. josimass", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Md sojon Udin", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        adapter= new ContactsAdapter(dataModels,getApplicationContext());

        listContacts.setAdapter(adapter);
    }
}
