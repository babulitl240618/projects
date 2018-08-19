package com.imagine.bd.hayvenapp.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.imagine.bd.hayvenapp.Model.ContactsData;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by User on 8/26/2017.
 */

public class GroupMemberHorizontalListview extends RecyclerView.Adapter<GroupMemberHorizontalListview.ViewHolder> {


    private ArrayList<ContactsData> listitem=new ArrayList<>();
    ContactsData contactsdata;
    Activity context;

    public GroupMemberHorizontalListview(Activity context, ArrayList<ContactsData> listitem) {
        super();
        this.context = context;
        this.listitem = listitem;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        contactsdata=  listitem.get(i);

        viewHolder.tvSpecies.setText(contactsdata.getDisplay_name());
       /* Picasso.with(context)
                .load(API_URL.PHOTO_BASE_URL+contactsdata.getImg())
               // .transform(new CircleTransform())
                .placeholder(R.drawable.chat_icon)
                .into( viewHolder.imgThumbnail);*/


        viewHolder.imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.addmemberlist.remove(i);
                notifyDataSetChanged();
            }
        });

        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(context, "#" + position + " - " , Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "#" + position + " - " , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView imgThumbnail;
        public ImageView imgCancel;
        public TextView tvSpecies;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            imgCancel = (ImageView) itemView.findViewById(R.id.imgMemberCancel);
            tvSpecies = (TextView) itemView.findViewById(R.id.tv_species);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }

}
