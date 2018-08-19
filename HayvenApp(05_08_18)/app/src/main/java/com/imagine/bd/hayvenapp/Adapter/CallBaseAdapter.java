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
import com.imagine.bd.hayvenapp.Fragment.HomeFragment;
import com.imagine.bd.hayvenapp.Model.DataModel;
import com.imagine.bd.hayvenapp.Model.UserInfo;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.Retrofit.IRetrofit;
import com.imagine.bd.hayvenapp.Retrofit.ServiceGenerator;
import com.imagine.bd.hayvenapp.utils.API_URL;
import com.imagine.bd.hayvenapp.utils.AlertMessage;
import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.CircleTransform;
import com.imagine.bd.hayvenapp.utils.Config;
import com.imagine.bd.hayvenapp.utils.NetInfo;
import com.imagine.bd.hayvenapp.utils.PersistData;
import com.imagine.bd.hayvenapp.utils.RoundedCornersTransform;
import com.imagine.bd.hayvenapp.utils.RoundedTransformation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallBaseAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<UserInfo> list; //data source of the list adapter
    HomeFragment fragment;


    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        ImageView imgVideo;
        ImageView imgAudio;
        ImageView imgProfil;
        ImageView imgOnline;
    }

    //public constructor
    public CallBaseAdapter(Context context, ArrayList<UserInfo> items, HomeFragment fragment) {
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
            convertView = inflater.inflate(R.layout.activity_call_list, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
            viewHolder.imgVideo = (ImageView) convertView.findViewById(R.id.imgVedio);
            viewHolder.imgAudio = (ImageView) convertView.findViewById(R.id.imgAudio);
            viewHolder.imgProfil = (ImageView) convertView.findViewById(R.id.imgProfil);
            viewHolder.imgOnline = (ImageView) convertView.findViewById(R.id.imgOnline);


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


        viewHolder.imgAudio.setVisibility(View.GONE);

/*

        if (list.get(position).getEmail().equalsIgnoreCase("demo@demo.com")) {
            viewHolder.imgAudio.setVisibility(View.VISIBLE);
        }

        if (list.get(position).getEmail().equalsIgnoreCase("msrkhokoncse@gmail.com")) {
            viewHolder.imgAudio.setVisibility(View.VISIBLE);
        }

        if (list.get(position).getEmail().equalsIgnoreCase("jubayer@gmail.com")) {
            viewHolder.imgAudio.setVisibility(View.VISIBLE);
        }
        if (list.get(position).getEmail().equalsIgnoreCase("rabbi@gmail.com")) {
            viewHolder.imgAudio.setVisibility(View.VISIBLE);
        }
*/


        Picasso.with(context)
                //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                .load(API_URL.PHOTO_BASE_URL + list.get(position).getImg())
                .transform(new RoundedTransformation(40,5))
                .placeholder(R.drawable.fajlehrabbi1)
                .into(viewHolder.imgProfil);


        Picasso.with(context)
                //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                .load(R.drawable.vedio_black)
                .transform(new CircleTransform())
                .placeholder(R.drawable.vedio_black)
                .into(viewHolder.imgVideo);


        if(AppConstant.onlinelist.contains(list.get(position).getId())){
           viewHolder.imgOnline.setVisibility(View.VISIBLE);
        }else{
            viewHolder.imgOnline.setVisibility(View.GONE);
        }
/*

        // viewHolder.imgProfil.setOnClickListener( this);
        Picasso.with(context)
                //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                .load(R.drawable.vedio_black)
                .transform(new CircleTransform())
                .placeholder(R.drawable.vedio_black)
                .into(viewHolder.imgVideo);
*/


        // viewHolder.imgProfil.setOnClickListener( this);
        Picasso.with(context)
                //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                .load(R.drawable.voice_black)
                .transform(new CircleTransform())
                .placeholder(R.drawable.voice_black)
                .into(viewHolder.imgAudio);


        viewHolder.imgProfil.setTag(position);
        viewHolder.imgVideo.setTag(position);
        // Return the completed view to render on screen

        viewHolder.imgVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!NetInfo.isOnline(context)) {
                    AlertMessage.showMessage(context, "No Internet Connection!", "You need to be connected to your network ");
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


        viewHolder.imgAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PersistData.setStringData(context, AppConstant.friend_name, list.get(position).getId());

                String room = AppConstant.GenerateId();

                JSONObject jsonObj = new JSONObject();

                try {
                    jsonObj.put("room", room);
                    jsonObj.put("type", "audio");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("jsontst", jsonObj.toString());

                String msg = jsonObj.toString();


                PersistData.setStringData(context, AppConstant.reciverid, list.get(position).getId()); /* set friend id as reciver id in consants bcoz

                                                                                                       always use constant values for friend id */
                /* when send call always use appconstant senderid and reciverid*/
                String senderid = PersistData.getStringData(context, AppConstant.userId);
                String reciverid = PersistData.getStringData(context, AppConstant.reciverid);

                if (senderid != null && reciverid != null) {
                    PersistData.setStringData(context, AppConstant.rom_id, room);
                    requesCall(senderid, reciverid, Config.INCOMING_CALL, msg);
                 //   fragment.connectToRoomAudio(room, false, false, false, 0);


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

        return result;
    }

    public void requesCall(final String senderid, final String reciver_id, String call_type, String msg) {



        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sender_id", senderid);
        jsonObject.addProperty("call_type", call_type);
        jsonObject.addProperty("reciver_id", reciver_id);
        jsonObject.addProperty("msg", msg);

        Log.e("tstdata", jsonObject.toString());

        // Using the Retrofit
        IRetrofit jsonPostService = ServiceGenerator.createService(IRetrofit.class, API_URL.BASE_URL_M);
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