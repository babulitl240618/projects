package com.imagine.bd.hayvenapp.Adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imagine.bd.hayvenapp.Fragment.HomeFragment;
import com.imagine.bd.hayvenapp.Model.UserInfo;
import com.imagine.bd.hayvenapp.utils.API_URL;
import com.imagine.bd.hayvenapp.utils.CircleTransform;
import com.imagine.bd.hayvenapp.Model.DataModel;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.utils.RoundedCornersTransform;
import com.imagine.bd.hayvenapp.utils.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 12/12/2017.
 */

public class NewChatAdapter extends ArrayAdapter<UserInfo> implements View.OnClickListener{

    private ArrayList<UserInfo> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        //  TextView txtVersion;
        ImageView imgProfil;
    }

   public NewChatAdapter(ArrayList<UserInfo> data, Context context) {
        super(context, R.layout.new_chat_list, data);
        this.dataSet = data;
        this.mContext=context;

    }



    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        UserInfo dataModel=(UserInfo)object;

        switch (v.getId())
        {
            // case R.id.item_info:
            //  Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
            // .setAction("No action", null).show();
            // break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        UserInfo dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            if (position==0){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.new_chat_list1, parent, false);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
                //viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.version_number);
                viewHolder.imgProfil = (ImageView) convertView.findViewById(R.id.imgProfile);
            }else if (position==3){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.new_chat_list3, parent, false);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
                //viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.version_number);
                viewHolder.imgProfil = (ImageView) convertView.findViewById(R.id.imgProfile);

            }else {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.new_chat_list, parent, false);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
                //viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.version_number);
                viewHolder.imgProfil = (ImageView) convertView.findViewById(R.id.imgProfile);
            }


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        lastPosition = position;
        viewHolder.txtName.setText(dataSet.get(position).getFullname());
        viewHolder.txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        viewHolder.txtType.setText(dataSet.get(position).getDesignation());
        /*viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtType.setText(dataModel.getType());*/
        // viewHolder.txtVersion.setText(dataModel.getVersion_number());
        Picasso.with(mContext)
                //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                .load(API_URL.PHOTO_BASE_URL + dataSet.get(position).getImg())
                .transform(new RoundedTransformation(40,5))
                .placeholder(R.drawable.fajlehrabbi1)
                .into(viewHolder.imgProfil);

       /* Picasso.with(mContext)
                //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                .load(R.drawable.fajlehrabbi1)
                .transform(new RoundedCornersTransform())
                .placeholder(R.drawable.fajlehrabbi1)
                .into( viewHolder.imgProfil);*/
        viewHolder.imgProfil.setOnClickListener(this);
        viewHolder.imgProfil.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
