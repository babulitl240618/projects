package com.imagine.bd.hayvenapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.imagine.bd.hayvenapp.Activity.CalingActivity;
import com.imagine.bd.hayvenapp.Activity.SigninActivity;
import com.imagine.bd.hayvenapp.MainActivity2;
import com.imagine.bd.hayvenapp.Model.SigninResponse;
import com.imagine.bd.hayvenapp.Model.UserInfo;
import com.imagine.bd.hayvenapp.Retrofit.IRetrofit;
import com.imagine.bd.hayvenapp.Retrofit.ServiceGenerator;
import com.imagine.bd.hayvenapp.utils.API_URL;

import com.imagine.bd.hayvenapp.R;

import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.PersistData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllUserListAdapter extends RecyclerView.Adapter<AllUserListAdapter.ViewHolder> {
    private ArrayList<UserInfo> list;
    public Context context;
    ViewHolder viewHolder;
    private AdapterCallback mAdapterCallback;
    int romm = 111;

    public AllUserListAdapter(Context context) {
        try {
            this.mAdapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }


    public AllUserListAdapter(ArrayList<UserInfo> list, Context context) {

        this.list = list;
        this.context = context;

        try {
            this.mAdapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void onBindViewHolder(final ViewHolder viewHolder,
                                 final int position) {

        viewHolder.tvDate.setText(list.get(position).getFullname());
        viewHolder.Tvhizri.setText(list.get(position).getDesignation());

     /*   viewHolder.Tvhizri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,ChattingActivity.class));
            }
        });*/


        //viewHolder.tvDate.setText(list.get(position).getName());
        //viewHolder.Tvhizri.setText(list.get(position).getType());
        // viewHolder.txtVersion.setText(dataModel.getVersion_number());
        Picasso.with(context)
                //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                .load(API_URL.PHOTO_BASE_URL + list.get(position).getImg())
                // .transform(new CircleTransform())
                .placeholder(R.drawable.fajlehrabbi1)
                .into(viewHolder.tvSuhur);
        //  viewHolder.tvSuhur.setTag(position);
        // viewHolder.tvSuhur.setText("rabbi");
        // viewHolder.tvDuhur.setText(list.get(position).getTimings().getMaghrib());

      /*
        Picasso.with(context).load(R.drawable.ic_launcher)
                .into(viewHolder.imageView);
        viewHolder.textView.setOnClickListener(new
                                                       View.OnClickListener() {

                                                           @Override
                                                           public void onClick(View v) {
                                                               Toast.makeText(v.getContext(),
                                                                       "OnClick :" + list.get(position),
                                                                       Toast.LENGTH_SHORT).show();

                                                           }
                                                       });*/

        /*   if (position > lastPosition) {

         *//*Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.up_from_bottom);
            viewHolder.itemView.startAnimation(animation);*//*
            lastPosition = position;
        }*/
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         final int viewType) {
        //Inflate the layout, initialize the View Holder

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemLayoutView = inflater.inflate(R.layout.activity_contacts_list, parent, false);
        final ViewHolder holder = new ViewHolder(itemLayoutView);


        itemLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                romm++;
                String room = String.valueOf(romm);

                PersistData.setStringData(context, AppConstant.rom_id, room);
                requesCall(list.get(3).getId(), list.get(3).getId(), room, "msg here");
                Toast.makeText(context, list.get(position).getFullname(), Toast.LENGTH_LONG).show();
                try {
                    mAdapterCallback.onMethodCallback();
                } catch (ClassCastException exception) {
                    // do something
                }

                // ((MainActivity2)context).connectToRoom1(String.valueOf(romm), false, false, false, 0);
                // fragment.connectToRoom1(String.valueOf(romm), false, false, false, 0);
            }
        });
//
//        View itemLayoutView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.activity_contacts_list, false);
//
//

        viewHolder = new ViewHolder(itemLayoutView);


        return viewHolder;
    }

    // initializes textview and imageview to be used by RecyclerView.
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDate, Tvhizri;
        public ImageView tvSuhur;

        public ViewHolder(View view) {
            super(view);

            tvDate = (TextView) view.findViewById(R.id.name);
            Tvhizri = (TextView) view.findViewById(R.id.type);
            tvSuhur = (ImageView) view.findViewById(R.id.imgProfile);


        }
    }


    public void requesCall(String senderid, String reciver_id, String call_type, String msg) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sender_id", senderid);
        jsonObject.addProperty("call_type", call_type);
        jsonObject.addProperty("reciver_id", reciver_id);
        jsonObject.addProperty("msg", msg);

        Log.e("tstdata", jsonObject.toString());

        // Using the Retrofit
        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class, API_URL.BASE_URL);
        Call<JsonObject> call = jsonPostService.sendCall(jsonObject);
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    Log.e("response-success", response.body().toString());

                  /*
                    JsonObject ob=new JsonObject();
                    JsonParser parser = new JsonParser();
                    JsonElement mJson = parser.parse(response.body().toString());
                    Gson gson = new Gson();
                    SigninResponse object = gson.fromJson(mJson, SigninResponse.class);

                  */


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("response-failure", call.toString());
            }

        });
    }

    public static interface AdapterCallback {
        void onMethodCallback();
    }
}
