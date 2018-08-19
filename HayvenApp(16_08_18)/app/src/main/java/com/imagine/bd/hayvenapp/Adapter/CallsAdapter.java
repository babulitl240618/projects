package com.imagine.bd.hayvenapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.imagine.bd.hayvenapp.Fragment.HomeFragment;
import com.imagine.bd.hayvenapp.MainActivity2;
import com.imagine.bd.hayvenapp.Retrofit.ApiClient;
import com.imagine.bd.hayvenapp.Retrofit.ApiInterface;
import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.CircleTransform;
import com.imagine.bd.hayvenapp.Model.DataModel;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.utils.PersistData;
import com.imagine.bd.hayvenapp.utils.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 12/12/2017.
 */

public class CallsAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener {

    private int img_id[]={R.drawable.anwar,R.drawable.profile_pic,R.drawable.jubayer,R.drawable.rabbi,R.drawable.sujon,R.drawable.dipok,R.drawable.joni,R.drawable.mahfuz,R.drawable.manzu};
    private ArrayList<DataModel> dataSet;
    Context mContext;
    HomeFragment fragment;
    int romm = 111;
    ;


    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        ImageView imgVideo;
        ImageView imgProfil;
    }

    public CallsAdapter(ArrayList<DataModel> data, Context context,HomeFragment fragment) {
        super(context, R.layout.activity_call_list, data);
        this.dataSet = data;
        this.fragment=fragment;
        this.mContext = context;
    }


    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        DataModel dataModel = (DataModel) object;

        switch (v.getId()) {
            // case R.id.item_info:
            //  Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
            // .setAction("No action", null).show();
            // break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            if (position ==2){   LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.activity_call_list1, parent, false);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
                viewHolder.imgVideo = (ImageView) convertView.findViewById(R.id.imgVedio);
                viewHolder.imgProfil = (ImageView) convertView.findViewById(R.id.imgProfil);
                Picasso.with(mContext)
                        //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                        .load(R.drawable.voice_black)
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.voice_black)
                        .into(viewHolder.imgVideo);}

            else {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.activity_call_list, parent, false);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
                viewHolder.imgVideo = (ImageView) convertView.findViewById(R.id.imgVedio);
                viewHolder.imgProfil = (ImageView) convertView.findViewById(R.id.imgProfil);
                Picasso.with(mContext)
                        //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                        .load(R.drawable.vedio_black)
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.vedio_black)
                        .into(viewHolder.imgVideo);

            }



            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        lastPosition = position;

       // viewHolder.imgProfil .setBackgroundResource(img_id[position]);
        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        viewHolder.txtType.setText("ID : "+dataModel.getVersion_number());

        Picasso.with(mContext)
                //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                .load(img_id[position])
                .transform(new RoundedCornersTransform())
                .placeholder(R.drawable.fajlehrabbi1)
                .into(viewHolder.imgProfil);


        viewHolder.imgProfil.setOnClickListener(this);


        viewHolder.imgVideo.setOnClickListener(this);
        viewHolder.imgProfil.setTag(position);
        viewHolder.imgVideo.setTag(position);
        // Return the completed view to render on screen

        viewHolder.imgVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Intent i=new Intent(mContext,ConnectActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);*/

                PersistData.setStringData(mContext, AppConstant.friend_name,dataSet.get(position).getName());


                romm++;
                String.valueOf(romm);
                fragment.connectToRoomVedio(dataSet.get(position).getVersion_number(), false, false, false, 0);

            }
        });

        return convertView;
    }
    // request for workspace list
    public void requestRoom(String friend_name,String room_id) {

      /*  if (!NetInfo.isOnline(con)) {
            AlertMessage.showMessage(con, getString(R.string.app_name), "No internet!");
            return;
        }*/



        // retfit code
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiService.requestRoom(friend_name,room_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                ResponseBody sr = response.body();
                Log.e("Response >>>", new GsonBuilder().setPrettyPrinting().create().toJson(response).toString());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                Log.e("SignupActivity ", t.toString());
            }
        });
    }


}
