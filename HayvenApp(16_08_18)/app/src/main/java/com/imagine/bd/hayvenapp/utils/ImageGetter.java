package com.imagine.bd.hayvenapp.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;

import com.imagine.bd.hayvenapp.R;

public class ImageGetter implements Html.ImageGetter {

    private Context context;

    public ImageGetter(Context current){
        this.context = current;
    }

    public Drawable getDrawable(String source) {
        int id;

        if (source.equals("\\/images\\/emoji\\/heart.png\\")) {
            id = R.drawable.heart;
        }
        else if (source.equals("overflow.jpg")) {
            id = R.drawable.logo;
        }
        else {
            return null;
        }

        Drawable d = context.getResources().getDrawable(id);
        d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
        return d;
    }
};