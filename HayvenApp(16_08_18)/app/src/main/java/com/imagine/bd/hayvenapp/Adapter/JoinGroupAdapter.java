package com.imagine.bd.hayvenapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.imagine.bd.hayvenapp.Activity.CalingActivity;
import com.imagine.bd.hayvenapp.Model.UserInfo;
import com.imagine.bd.hayvenapp.Retrofit.IRetrofit;
import com.imagine.bd.hayvenapp.Retrofit.ServiceGenerator;
import com.imagine.bd.hayvenapp.utils.API_URL;
import com.imagine.bd.hayvenapp.utils.AlertMessage;
import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.CircleTransform;
import com.imagine.bd.hayvenapp.Model.DataModel;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.utils.Config;
import com.imagine.bd.hayvenapp.utils.NetInfo;
import com.imagine.bd.hayvenapp.utils.PersistData;
import com.imagine.bd.hayvenapp.utils.RoundedCornersTransform;
import com.imagine.bd.hayvenapp.utils.RoundedTransformation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinGroupAdapter extends RecyclerView.Adapter<JoinGroupAdapter.ViewHolder> {
    private ArrayList<UserInfo> list;
    public Context context;
    ViewHolder viewHolder;

    public JoinGroupAdapter(ArrayList<UserInfo> list, Context context) {

        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void onBindViewHolder(final ViewHolder viewHolder,
                                 final int position) {

        viewHolder.tvDate.setText(list.get(position).getFullname());
        viewHolder.tvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        viewHolder.Tvhizri.setText(list.get(position).getDesignation());

     /*   viewHolder.Tvhizri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,ChattingActivity.class));
            }
        });*/


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetInfo.isOnline(context)) {
                    AlertMessage.showMessage(context, "No Internet Connection!", "You need to be connected to your network. ");
                    return;
                }
                PersistData.setStringData(context, AppConstant.friend_name, list.get(position).getId());
                String room = AppConstant.GenerateId();

                JSONObject jsonObj = new JSONObject();

                try {
                    jsonObj.put("room", room);
                    jsonObj.put("type", "vedio");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("jsontst", jsonObj.toString());

                String msg = jsonObj.toString();


                PersistData.setStringData(context, AppConstant.reciverid, list.get(position).getId());
                String senderid = PersistData.getStringData(context, AppConstant.userId);
                String reciverid = PersistData.getStringData(context, AppConstant.reciverid);

                if (senderid != null && reciverid != null) {
                    PersistData.setStringData(context, AppConstant.rom_id, room);
                    requesCall(senderid, reciverid, Config.INCOMING_CALL, msg);
                    //  fragment.connectToRoomVedio(room, false, false, false, 0);

                    Intent intent = new Intent(context, CalingActivity.class);

                    intent.putExtra("name", list.get(position).getFullname());
                    intent.putExtra("image", list.get(position).getImg());
                    context.startActivity(intent);


                } else {
                    if (senderid == null) {
                        Toast.makeText(context, "sender id null", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "reciver id  null", Toast.LENGTH_LONG).show();
                    }

                }


            }
        });
        //viewHolder.tvDate.setText(list.get(position).getName());
        //viewHolder.Tvhizri.setText(list.get(position).getType());
        // viewHolder.txtVersion.setText(dataModel.getVersion_number());
        Picasso.with(context)
                //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                .load(API_URL.PHOTO_BASE_URL + list.get(position).getImg())
                .transform(new RoundedTransformation(40, 5))
                .placeholder(R.drawable.fajlehrabbi1)
                .into(viewHolder.tvSuhur);
        viewHolder.tvSuhur.setTag(position);

        if (AppConstant.onlinelist.contains(list.get(position).getId())) {
            viewHolder.imgOnline.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgOnline.setVisibility(View.GONE);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         final int pos) {
        //Inflate the layout, initialize the View Holder


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemLayoutView = inflater.inflate(R.layout.new_chat_list2, parent, false);
        final ViewHolder holder = new ViewHolder(itemLayoutView);


      /*  itemLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            final int position = holder.getAdapterPosition();
                Toast.makeText(context, list.get(position).getFullname(), Toast.LENGTH_LONG).show();
            }
        });
*/

        viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // initializes textview and imageview to be used by RecyclerView.
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDate, Tvhizri;
        public ImageView tvSuhur;
        ImageView imgOnline;
        LinearLayout llItemView;

        public ViewHolder(View view) {
            super(view);

            tvDate = (TextView) view.findViewById(R.id.name);
            Tvhizri = (TextView) view.findViewById(R.id.type);
            tvSuhur = (ImageView) view.findViewById(R.id.imgProfile);
            imgOnline = (ImageView) view.findViewById(R.id.imgOnline);
            llItemView = (LinearLayout) view.findViewById(R.id.llItemView);

        }
    }


    public void requesCall(final String senderid, final String reciver_id, String call_type, String msg) {

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

                    PersistData.setStringData(context, AppConstant.senderid, senderid);
                    PersistData.setStringData(context, AppConstant.reciverid, reciver_id);

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

}
