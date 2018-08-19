package com.imagine.bd.hayvenapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.imagine.bd.hayvenapp.R;

public class AlertDialogImageAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;

    public AlertDialogImageAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return iconList.length;
    }

    @Override
    public Object getItem(int position) {
        return iconList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @SuppressLint("InflateParams")
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        AlertDialogViewHolder alertDialogViewHolder;

        if (convertView == null) {
            // This is an alertDialog, therefore it has no root
            convertView = layoutInflater.inflate(R.layout.emoji_row_list, null);

            DisplayMetrics metrics = convertView.getResources().getDisplayMetrics();
            int screenWidth = metrics.widthPixels;

            convertView.setLayoutParams(new GridView.LayoutParams(screenWidth / 6, screenWidth / 6));
            alertDialogViewHolder = new AlertDialogViewHolder();
            alertDialogViewHolder.icon = convertView.findViewById(R.id.image_choose_icon_entry);
            convertView.setTag(alertDialogViewHolder);
        } else {
            alertDialogViewHolder = (AlertDialogViewHolder) convertView.getTag();
        }

        alertDialogViewHolder.icon.setAdjustViewBounds(true);
        alertDialogViewHolder.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        alertDialogViewHolder.icon.setPadding(8, 8, 8, 8);
        alertDialogViewHolder.icon.setImageResource(iconList[position]);
        return convertView;
    }

    // This is your source for your icons, fill it with your own
    private Integer[] iconList = {
            R.drawable.grinning, R.drawable.disappointed_relieved,
            R.drawable.heart, R.drawable.joy,
            R.drawable.open_mouth, R.drawable.rage,
            R.drawable.thumbsdown, R.drawable.thumbsup,
            R.drawable.grinning, R.drawable.disappointed_relieved,
            R.drawable.heart, R.drawable.joy,
            R.drawable.open_mouth, R.drawable.rage,
            R.drawable.thumbsdown, R.drawable.thumbsup,
            R.drawable.grinning, R.drawable.disappointed_relieved,
            R.drawable.heart, R.drawable.joy,
            R.drawable.open_mouth, R.drawable.rage,
            R.drawable.thumbsdown, R.drawable.thumbsup

    };

    // This is your source for your icons, fill it with your own
    final  String emoNmae[] = {
            "grinning", "disappointed_relieved",
            "heart", "joy",
            "open_mouth", "rage",
            "thumbsdown", "thumbsup",
            "grinning", "disappointed_relieved",
            "heart", "joy",
            "open_mouth", "rage",
            "thumbsdown", "thumbsup",
            "grinning", "disappointed_relieved",
            "heart", "joy",
            "open_mouth", "rage",
            "thumbsdown", "thumbsup"

    };
   // String htmlImgTag2="\"<img src=\\\"/images/emoji/"+emoNmae[position]+".png\\\" style=\\\"width:20px; height:20px;\\\">\"";

    private class AlertDialogViewHolder {
        ImageView icon;

    }
}