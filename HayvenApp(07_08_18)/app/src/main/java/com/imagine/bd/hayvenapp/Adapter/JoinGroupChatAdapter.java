package com.imagine.bd.hayvenapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imagine.bd.hayvenapp.Activity.CalingActivity;
import com.imagine.bd.hayvenapp.utils.CircleTransform;
import com.imagine.bd.hayvenapp.Model.DataModel;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.utils.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class JoinGroupChatAdapter extends RecyclerView.Adapter<JoinGroupChatAdapter.ViewHolder>{
    private ArrayList<DataModel> list;
    public Context context;
    ViewHolder viewHolder;
    int lastPosition = -1;
    public JoinGroupChatAdapter(ArrayList<DataModel> list, Context context) {

        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void onBindViewHolder(final ViewHolder viewHolder,
                                 final int position) {

        viewHolder.tvDate.setText("ITL Group");
        viewHolder.Tvhizri.setText("26 members");

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
                .load(R.drawable.navigate_group)
                .transform(new RoundedCornersTransform())
                .placeholder(R.drawable.navigate_group)
                .into( viewHolder.tvSuhur);
        viewHolder.tvSuhur.setTag(position);
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

        if (position > lastPosition) {

            /*Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.up_from_bottom);
            viewHolder.itemView.startAnimation(animation);*/
            lastPosition = position;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        //Inflate the layout, initialize the View Holder
        if (lastPosition==0){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemLayoutView=inflater.inflate(R.layout.new_chat_list2, parent,false);


            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,CalingActivity.class));
                }
            });
//
//        View itemLayoutView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.activity_contacts_list, false);
//
//

            viewHolder = new ViewHolder(itemLayoutView);}else {

            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemLayoutView=inflater.inflate(R.layout.new_chat_list2, parent,false);


            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,CalingActivity.class));
                }
            });
//
//        View itemLayoutView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.activity_contacts_list, false);
//
//

            viewHolder = new ViewHolder(itemLayoutView);
        }



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
}
