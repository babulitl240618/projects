package com.imagine.bd.hayvenapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.imagine.bd.hayvenapp.Model.ChatAppMsgDTO;
import com.imagine.bd.hayvenapp.Model.MessageData;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.utils.API_URL;
import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.ImageGetter;
import com.imagine.bd.hayvenapp.utils.PersistData;
import com.imagine.bd.hayvenapp.utils.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerry on 12/19/2017.
 */

public class ChatAppMsgAdapter extends RecyclerView.Adapter<ChatAppMsgAdapter.ChatAppMsgViewHolder> {

    private ArrayList<MessageData> msgDtoList = null;
    private Context context;

    public ChatAppMsgAdapter(ArrayList<MessageData> msgDtoList, Context con) {
        this.msgDtoList = msgDtoList;
        this.context = con;
    }

    @Override
    public void onBindViewHolder(ChatAppMsgViewHolder holder, int position) {
        MessageData msgDto = this.msgDtoList.get(position);

        Spannable html = null;


        ImageGetter imageGetter = new ImageGetter(context);

        // If the message is a received message.
        if (!msgDto.getSender().equalsIgnoreCase(PersistData.getStringData(context, AppConstant.userId))) {
            // Show received message in left linearlayout.
            holder.leftMsgLayout.setVisibility(LinearLayout.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //  holder.leftMsgTextView.setText( Html.fromHtml(msgDto.getMsg_body(), Html.FROM_HTML_MODE_COMPACT));

                html = (Spannable) Html.fromHtml(msgDto.getMsg_body(), Html.FROM_HTML_MODE_LEGACY, new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {

                        int id;
                        if (source.equals("/images/emoji/heart.png")) {
                            id = R.drawable.heart;
                        }
                        else if (source.equals("/images/emoji/grinning.png")) {
                            id = R.drawable.grinning;
                        }
                        else if (source.equals("/images/emoji/disappointed_relieved.png")) {
                            id = R.drawable.disappointed_relieved;
                        }
                        else if (source.equals("/images/emoji/joy.png")) {
                            id = R.drawable.joy;
                        }
                        else if (source.equals("/images/emoji/open_mouth.png")) {
                            id = R.drawable.open_mouth;
                        }
                        else if (source.equals("/images/emoji/rage.png")) {
                            id = R.drawable.rage;
                        }
                        else if (source.equals("/images/emoji/thumbsdown.png")) {
                            id = R.drawable.thumbsdown;
                        }
                        else if (source.equals("/images/emoji/thumbsup.png")) {
                            id = R.drawable.thumbsup;
                        }
                        else {
                            return null;
                        }
                        Drawable d = context.getResources().getDrawable(id);
                        d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
                        return d;

                    }
                }, null);


            } else {
                html = (Spannable) Html.fromHtml(msgDto.getMsg_body(), new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                        int id;

                        if (source.equals("/images/emoji/heart.png")) {
                            id = R.drawable.heart;
                        }
                        else if (source.equals("/images/emoji/grinning.png")) {
                            id = R.drawable.grinning;
                        }
                        else if (source.equals("/images/emoji/disappointed_relieved.png")) {
                            id = R.drawable.disappointed_relieved;
                        }
                        else if (source.equals("/images/emoji/joy.png")) {
                            id = R.drawable.joy;
                        }
                        else if (source.equals("/images/emoji/open_mouth.png")) {
                            id = R.drawable.open_mouth;
                        }
                        else if (source.equals("/images/emoji/rage.png")) {
                            id = R.drawable.rage;
                        }
                        else if (source.equals("/images/emoji/thumbsdown.png")) {
                            id = R.drawable.thumbsdown;
                        }
                        else if (source.equals("/images/emoji/thumbsup.png")) {
                            id = R.drawable.thumbsup;
                        }
                        else {
                            return null;
                        }

                        Drawable d = context.getResources().getDrawable(id);
                        d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
                        return d;
                    }
                }, null);

                // holder.leftMsgTextView.setText(msgDto.getMsg_body());
            }

            holder.leftMsgTextView.setText(html);

            // Remove left linearlayout.The value should be GONE, can not be INVISIBLE
            // Otherwise each iteview's distance is too big.
            holder.rightMsgLayout.setVisibility(LinearLayout.GONE);
            Picasso.with(context)
                    //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                    .load(API_URL.PHOTO_BASE_URL + msgDto.getSender_img())
                    .transform(new RoundedCornersTransform())
                    .placeholder(R.drawable.fajlehrabbi1)
                    .into(holder.imageView);
        }
        // If the message is a sent message.
        else {
            // Show sent message in right linearlayout.
            holder.rightMsgLayout.setVisibility(LinearLayout.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                html = (Spannable) Html.fromHtml(msgDto.getMsg_body(), Html.FROM_HTML_MODE_LEGACY, new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {

                        int id;


                        if (source.equals("/images/emoji/heart.png")) {
                            id = R.drawable.heart;
                        }
                        else if (source.equals("/images/emoji/grinning.png")) {
                            id = R.drawable.grinning;
                        }
                        else if (source.equals("/images/emoji/disappointed_relieved.png")) {
                            id = R.drawable.disappointed_relieved;
                        }
                        else if (source.equals("/images/emoji/joy.png")) {
                            id = R.drawable.joy;
                        }
                        else if (source.equals("/images/emoji/open_mouth.png")) {
                            id = R.drawable.open_mouth;
                        }
                        else if (source.equals("/images/emoji/rage.png")) {
                            id = R.drawable.rage;
                        }
                        else if (source.equals("/images/emoji/thumbsdown.png")) {
                            id = R.drawable.thumbsdown;
                        }
                        else if (source.equals("/images/emoji/thumbsup.png")) {
                            id = R.drawable.thumbsup;
                        }
                        else {
                            return null;
                        }

                        Drawable d = context.getResources().getDrawable(id);
                        d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
                        return d;

                    }
                }, null);


            } else {

                html = (Spannable) Html.fromHtml(msgDto.getMsg_body(), new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                        int id;

                        if (source.equals("/images/emoji/heart.png")) {
                            id = R.drawable.heart;
                        }
                        else if (source.equals("/images/emoji/grinning.png")) {
                            id = R.drawable.grinning;
                        }
                        else if (source.equals("/images/emoji/disappointed_relieved.png")) {
                            id = R.drawable.disappointed_relieved;
                        }
                        else if (source.equals("/images/emoji/joy.png")) {
                            id = R.drawable.joy;
                        }
                        else if (source.equals("/images/emoji/open_mouth.png")) {
                            id = R.drawable.open_mouth;
                        }
                        else if (source.equals("/images/emoji/rage.png")) {
                            id = R.drawable.rage;
                        }
                        else if (source.equals("/images/emoji/thumbsdown.png")) {
                            id = R.drawable.thumbsdown;
                        }
                        else if (source.equals("/images/emoji/thumbsup.png")) {
                            id = R.drawable.thumbsup;
                        }
                        else {
                            return null;
                        }

                        Drawable d = context.getResources().getDrawable(id);
                        d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
                        return d;
                    }
                }, null);

            }
            holder.rightMsgTextView.setText(html);
            // Remove left linearlayout.The value should be GONE, can not be INVISIBLE
            // Otherwise each iteview's distance is too big.
            holder.leftMsgLayout.setVisibility(LinearLayout.GONE);
        }

        holder.rightMsgTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialogue_create_msg_action);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER | Gravity.CENTER;

                wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);

                dialog.show();
            }
        });
        holder.rightMsgTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.row_dialogue_flag);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER | Gravity.CENTER;

                wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);

                dialog.show();
                return false;
            }
        });
        holder.leftMsgTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.row_dialogue_flag);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER | Gravity.CENTER;

                wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);

                dialog.show();
                return false;
            }
        });

        holder.leftMsgTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialogue_create_msg_action);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER | Gravity.CENTER;

                wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);

                // set the custom dialog components - text, image and button
               /* TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Android custom dialog example!");
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageResource(R.drawable.ic_launcher);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);*/
                // if button is clicked, close the custom dialog
              /*  dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
*/
                dialog.show();
            }
        });

    }

    @Override
    public ChatAppMsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_chat_app_item_view, parent, false);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


            }
        });
        return new ChatAppMsgViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (msgDtoList == null) {
            msgDtoList = new ArrayList<MessageData>();
        }
        return msgDtoList.size();
    }


    public class ChatAppMsgViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftMsgLayout;

        LinearLayout rightMsgLayout;
        ImageView imageView;
        TextView leftMsgTextView;

        TextView rightMsgTextView;

        public ChatAppMsgViewHolder(View itemView) {
            super(itemView);

            if (itemView != null) {
                leftMsgLayout = (LinearLayout) itemView.findViewById(R.id.chat_left_msg_layout);
                rightMsgLayout = (LinearLayout) itemView.findViewById(R.id.chat_right_msg_layout);
                leftMsgTextView = (TextView) itemView.findViewById(R.id.chat_left_msg_text_view);
                imageView = (ImageView) itemView.findViewById(R.id.smspic);
                rightMsgTextView = (TextView) itemView.findViewById(R.id.chat_right_msg_text_view);
               /* Picasso.with(context)
                        //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                        .load(R.drawable.fajlehrabbi1)
                        .transform(new RoundedCornersTransform())
                        .placeholder(R.drawable.fajlehrabbi1)
                        .into(itemView.imageView);
                itemView.imageView.setOnClickListener(this);*/
            }
        }
    }


}