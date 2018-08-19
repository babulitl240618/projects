package com.imagine.bd.hayvenapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.imagine.bd.hayvenapp.Activity.SocialCommittee;
import com.imagine.bd.hayvenapp.Model.ContactsData;
import com.imagine.bd.hayvenapp.Model.DataModel;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SocialCommiteeAdapter extends RecyclerView.Adapter<SocialCommiteeAdapter.ViewHolder>{
    private ArrayList<DataModel> list;
    public Context context;
    ViewHolder viewHolder;
    int lastPosition = -1;
    public SocialCommiteeAdapter(ArrayList<DataModel> list, Context context) {

        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void onBindViewHolder(final ViewHolder viewHolder,
                                 final int position) {

        viewHolder.tvDate.setText(list.get(position).getName());
        viewHolder.Tvhizri.setText(list.get(position).getFeature());
        //viewHolder.tvDate.setText(list.get(position).getName());
        //viewHolder.Tvhizri.setText(list.get(position).getType());
        // viewHolder.txtVersion.setText(dataModel.getVersion_number());
        Picasso.with(context)
                //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                .load(R.drawable.fajlehrabbi1)
                .transform(new RoundedCornersTransform())
                .placeholder(R.drawable.fajlehrabbi1)
                .into( viewHolder.tvSuhur);
        viewHolder.tvSuhur.setTag(position);

        viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsData data = new ContactsData();

              //  Toast.makeText(context,""+ viewHolder.checkbox.isChecked() ,Toast.LENGTH_LONG).show();

                if(viewHolder.checkbox.isChecked()){
                    data.setDisplay_name(list.get(position).getName());
                    // data.setImg(AppConstant.contactsList.get(position).getImg());
                    data.setEmail(list.get(position).getName());
                    AppConstant.addmemberlist.add(data);
                }else {
                    AppConstant.addmemberlist.remove( AppConstant.addmemberlist.size()-1);
                }

               // Toast.makeText(context,AppConstant.addmemberlist.size()+ AppConstant.addmemberlist.get(AppConstant.addmemberlist.size()-1).getDisplay_name() ,Toast.LENGTH_LONG).show();
                //  holder.imgStatus.setVisibility(View.VISIBLE);

               // Toast.makeText(context, AppConstant.contactsList.get(position).getDisplay_name(), Toast.LENGTH_SHORT).show();

               // SocialCommittee.addAdapter.notifyDataSetChanged();
            }
        });

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
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.social_commitee_list, null);

        viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // initializes textview and imageview to be used by RecyclerView.
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDate, Tvhizri;
        public ImageView tvSuhur;
        public CheckBox checkbox;

        public ViewHolder(View view) {
            super(view);

            tvDate = (TextView) view.findViewById(R.id.name);
            Tvhizri = (TextView) view.findViewById(R.id.type);
            tvSuhur = (ImageView) view.findViewById(R.id.imgProfile);
            checkbox = (CheckBox) view.findViewById(R.id.checkbox);

        }
    }
}
