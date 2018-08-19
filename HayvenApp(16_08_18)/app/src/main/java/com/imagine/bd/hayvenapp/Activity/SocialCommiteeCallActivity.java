package com.imagine.bd.hayvenapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.imagine.bd.hayvenapp.R;

public class SocialCommiteeCallActivity extends AppCompatActivity {
    ImageView imageView,imggroupcall,imggroupvideocall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_commitee_call);
        imageView = (ImageView) findViewById(R.id.notificationChat);
        imggroupcall = (ImageView) findViewById(R.id.audio);
        imggroupvideocall = (ImageView) findViewById(R.id.search);
        imggroupcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SocialCommiteeCallActivity.this, GroupCallActivity.class));
                //Intent i = new Intent(context, Contacts.class);
                //startActivity(i);
            }
        });
        imggroupvideocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SocialCommiteeCallActivity.this, GroupCallActivity.class));
                //Intent i = new Intent(context, Contacts.class);
                //startActivity(i);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }
}
