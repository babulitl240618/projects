package com.imagine.bd.hayvenapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.imagine.bd.hayvenapp.Adapter.ContactsAdapter;
import com.imagine.bd.hayvenapp.JIRA.JiraLoginActivity;
import com.imagine.bd.hayvenapp.Model.DataModel;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.Trello.activity.MainActivity;

import java.util.ArrayList;


public class ConnectFragment extends Fragment {
    private Context con;
    ArrayList<DataModel> dataModels;
    ListView listContacts;
    Button connect, connectJira;
    private ImageView imgContinue,imgContinue1,imgContinue2,imgContinue3,imgContinue4,imgLauncher;
    private ContactsAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connect,null);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        con=getActivity();
        connect=getView().findViewById(R.id.connect);
        connectJira = getView().findViewById(R.id.connectJira);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(con, MainActivity.class));
            }
        });

        connectJira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(con, JiraLoginActivity.class));
            }
        });


    }
}
