package com.imagine.bd.hayvenapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.imagine.bd.hayvenapp.Activity.CalingActivity;
import com.imagine.bd.hayvenapp.Fragment.ChatFragment;
import com.imagine.bd.hayvenapp.Fragment.HomeFragment;
import com.imagine.bd.hayvenapp.Model.UserInfo;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.Retrofit.IRetrofit;
import com.imagine.bd.hayvenapp.Retrofit.ServiceGenerator;
import com.imagine.bd.hayvenapp.utils.API_URL;
import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.CircleTransform;
import com.imagine.bd.hayvenapp.utils.Config;
import com.imagine.bd.hayvenapp.utils.PersistData;
import com.imagine.bd.hayvenapp.utils.RoundedTransformation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsUserAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<UserInfo> list; //data source of the list adapter
    HomeFragment fragment;


    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        ImageView imgProfil;
    }

    //public constructor
    public ChatsUserAdapter(Context context, ArrayList<UserInfo> items, HomeFragment fragment) {
        this.context = context;
        this.list = items;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return list.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return list.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.activity_chat_list, parent, false);

         /*   if(position==0||position==1){
                convertView = inflater.inflate(R.layout.activity_chat_list1, parent, false);
            }else if ((position==2)){
                convertView = inflater.inflate(R.layout.activity_chat_list2, parent, false);
            }else {
                convertView = inflater.inflate(R.layout.activity_chat_list, parent, false);
            }*/


            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
            viewHolder.imgProfil = (ImageView) convertView.findViewById(R.id.imgProfil);


            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        // viewHolder.imgProfil .setBackgroundResource(img_id[position]);
        viewHolder.txtName.setText(list.get(position).getFullname());
        viewHolder.txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        viewHolder.txtType.setText(list.get(position).getDesignation());




        Picasso.with(context)
                //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                .load(API_URL.PHOTO_BASE_URL + list.get(position).getImg())
                 .transform(new RoundedTransformation(40,5))
                .placeholder(R.drawable.fajlehrabbi1)
                .into(viewHolder.imgProfil);



        viewHolder.imgProfil.setTag(position);


        return result;
    }

}
