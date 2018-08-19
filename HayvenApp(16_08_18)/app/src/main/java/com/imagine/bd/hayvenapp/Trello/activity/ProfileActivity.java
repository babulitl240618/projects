package com.imagine.bd.hayvenapp.Trello.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.Trello.model.TrelloModel;
import com.imagine.bd.hayvenapp.Trello.service.TrelloService;
import com.imagine.bd.hayvenapp.Trello.vo.MemberVO;


public class ProfileActivity extends Activity {
    private TrelloModel mModel;
    private TextView fullname, initialname, username, url;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        fullname = findViewById(R.id.full_name);
        initialname = findViewById(R.id.initial_name);
        username = findViewById(R.id.user_name);
        url = findViewById(R.id.url);

        mModel = TrelloModel.getInstance();

        MemberVO user = mModel.getUser();

        fullname.setText(user.fullName);
        initialname.setText(user.initials);
        username.setText(user.username);
        url.setText(user.url);

    }
}
