package com.imagine.bd.hayvenapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;
import com.imagine.bd.hayvenapp.Activity.SigninActivity;
import com.imagine.bd.hayvenapp.Database.MyDbHelper;
import com.imagine.bd.hayvenapp.Fragment.ConnectFragment;
import com.imagine.bd.hayvenapp.Fragment.HomeFragment;
import com.imagine.bd.hayvenapp.Fragment.ContactsFragment;
import com.imagine.bd.hayvenapp.Service.BackgroundService;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;

public class MainActivity2 extends AppCompatActivity {
    LinearLayout ll_contact, ll_connect, ll_more;
    ImageView imgLauncher;
    private Context con;
    String[] stringArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //  startService(new Intent(this, BackgroundService.class));

        con = this;


        Intent intent = getIntent();
        stringArray = intent.getStringArrayExtra("string-array");

        ll_contact = (LinearLayout) findViewById(R.id.ll_contact);
        ll_more = (LinearLayout) findViewById(R.id.ll_more);
        ll_connect = (LinearLayout) findViewById(R.id.ll_connect);
        imgLauncher = (ImageView) findViewById(R.id.imgLauncher);
        HomeFragment newFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.frame, newFragment);
        //transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
        ll_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsFragment newFragment = new ContactsFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.frame, newFragment);
                // transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();

            }
        });
        ll_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectFragment newFragment = new ConnectFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.frame, newFragment);
                // transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();

            }
        });


        ll_more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog
                final Dialog dialog = new Dialog(con);
                dialog.setContentView(R.layout.logout_diaologue);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.BOTTOM | Gravity.RIGHT;

                wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);

                LinearLayout llLogout = (LinearLayout) dialog.findViewById(R.id.llLogout);
                llLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity2.this, SigninActivity.class));
                        dialog.dismiss();
                        finish();

                    }
                });

                // set the custom dialog components - text, image and button
               /* TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Android custom dialog example!");
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageResource(R.drawable.ic_launcher);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);*/
                // if button is clicked, close the custom dialog
              /*  dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
*/
                dialog.show();
            }
        });

       /* ll_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        imgLauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment newFragment = new HomeFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.frame, newFragment);
                //  transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();
                // startActivity(new Intent(MainActivity2.this, MainActivity.class));
                // finish();
                //Intent i = new Intent(context, Contacts.class);
                //startActivity(i);
            }
        });
    }


}
