package com.imagine.bd.hayvenapp.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.imagine.bd.hayvenapp.Adapter.ContactsAdapter;
import com.imagine.bd.hayvenapp.Model.DataModel;
import com.imagine.bd.hayvenapp.R;

import java.util.ArrayList;

public class CallFragment extends Fragment {
    private Context con;
    ArrayList<DataModel> dataModels;
    ListView listContacts;
    private ImageView imgContinue,imgContinue1,imgContinue2,imgContinue3,imgContinue4,imgLauncher;
    private ContactsAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_call2,null);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        con=getActivity();
        listContacts=(ListView)getView().findViewById(R.id.listContacts);


        dataModels= new ArrayList<>();
        dataModels.add(new DataModel("Banana Bread", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Banana Bread", "The ArrayAdapter will ..", "9.58am","February 9, 2009"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Md. josimass", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Md sojon Udin", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        dataModels.add(new DataModel("Fajleh Rabbi", "The ArrayAdapter will ..", "9.58am","September 23, 2008"));
        adapter= new ContactsAdapter(dataModels,con);

        listContacts.setAdapter(adapter);


    }
}
