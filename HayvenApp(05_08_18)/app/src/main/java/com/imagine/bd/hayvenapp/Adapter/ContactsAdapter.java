package com.imagine.bd.hayvenapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imagine.bd.hayvenapp.utils.CircleTransform;
import com.imagine.bd.hayvenapp.Model.DataModel;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.utils.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 12/12/2017.
 */

public class ContactsAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
      //  TextView txtVersion;
        ImageView imgProfil;
    }

    public ContactsAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.activity_contacts_list, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;

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
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            if (position==0){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.activity_contacts_list1, parent, false);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
                //viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.version_number);
                viewHolder.imgProfil = (ImageView) convertView.findViewById(R.id.imgProfil);

            }else {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.activity_contacts_list, parent, false);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
                //viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.version_number);
                viewHolder.imgProfil = (ImageView) convertView.findViewById(R.id.imgProfil);
            }

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        lastPosition = position;

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtType.setText(dataModel.getType());
       // viewHolder.txtVersion.setText(dataModel.getVersion_number());
        Picasso.with(mContext)
                //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                .load(R.drawable.navigate_group)
                .transform(new RoundedCornersTransform())
                .placeholder(R.drawable.navigate_group)
                .into( viewHolder.imgProfil);
        viewHolder.imgProfil.setOnClickListener(this);
        viewHolder.imgProfil.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
