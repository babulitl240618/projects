package com.imagine.bd.hayvenapp.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imagine.bd.hayvenapp.Fragment.HomeFragment;
import com.imagine.bd.hayvenapp.Model.ChatUserInfo;
import com.imagine.bd.hayvenapp.Model.UserInfo;
import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.utils.API_URL;
import com.imagine.bd.hayvenapp.utils.AppConstant;
import com.imagine.bd.hayvenapp.utils.RoundedCornersTransform;
import com.imagine.bd.hayvenapp.utils.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class ChatsWorkbaseUserAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<ChatUserInfo> list; //data source of the list adapter
    HomeFragment fragment;


    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView txtTime;
        TextView count;
        ImageView imgProfil;
    }

    //public constructor
    public ChatsWorkbaseUserAdapter(Context context, ArrayList<ChatUserInfo> items, HomeFragment fragment) {
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
        Spannable html = null;
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder =new ViewHolder();
           // viewHolder = new ChatsUserAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.activity_chat_list1, parent, false);

         /*   if(position==0||position==1){
                convertView = inflater.inflate(R.layout.activity_chat_list1, parent, false);
            }else if ((position==2)){
                convertView = inflater.inflate(R.layout.activity_chat_list2, parent, false);
            }else {
                convertView = inflater.inflate(R.layout.activity_chat_list, parent, false);
            }*/


            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
            viewHolder.imgProfil = (ImageView) convertView.findViewById(R.id.imgProfil);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
            viewHolder.count = (TextView) convertView.findViewById(R.id.count);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //  holder.leftMsgTextView.setText( Html.fromHtml(msgDto.getMsg_body(), Html.FROM_HTML_MODE_COMPACT));

            html = (Spannable) Html.fromHtml(list.get(position).getMsg_body(), Html.FROM_HTML_MODE_LEGACY, new Html.ImageGetter() {
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
            html = (Spannable) Html.fromHtml(list.get(position).getMsg_body(), new Html.ImageGetter() {
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

        // viewHolder.imgProfil .setBackgroundResource(img_id[position]);
        viewHolder.txtName.setText(list.get(position).getConversation_title());
        viewHolder.txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        viewHolder.txtType.setText(html);
        viewHolder.txtTime.setText(AppConstant.getConvertedTime(list.get(position).getCreated_at()));

         if (list.get(position).getConversation_type().equalsIgnoreCase("group")){
             Picasso.with(context)
                     //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                     .load(R.drawable.navigate_group)
                     .transform(new RoundedTransformation(40,5))
                     .placeholder(R.drawable.fajlehrabbi1)
                     .into(viewHolder.imgProfil);
             viewHolder.imgProfil.setTag(position);
         }else {
             Picasso.with(context)
                     //   .load(PersistData.getStringData(con, AppConstant.person_pic_url))
                     .load(API_URL.PHOTO_BASE_URL + list.get(position).getSender_img())
                     .transform(new RoundedTransformation(40,5))
                     .placeholder(R.drawable.fajlehrabbi1)
                     .into(viewHolder.imgProfil);
                     viewHolder.imgProfil.setTag(position);
         }

        if (Integer.parseInt(list.get(position).getTotalUnread())==0){
            viewHolder.count.setVisibility(View.GONE);
        }else {
            viewHolder.count.setVisibility(View.VISIBLE);
            viewHolder.count.setText(list.get(position).getTotalUnread());

        }








        return result;
    }
   /* public String addTime(int hour, int minute, int minutesToAdd) {
       // Calendar calendar = new GregorianCalendar(1990, 1, 1, hour, minute);
        //calendar.add(Calendar.MINUTE, minutesToAdd);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        String date = sdf.format(calendar.getTime());

        return date;
    }*/
   public static String getCONVERTEDTime(String datetime) {

       final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
       sdf1.setTimeZone(TimeZone.getTimeZone("UTC")); // This line converts the given date into UTC time zone
       java.util.Date dateObj = null;
       try {
           dateObj = sdf1.parse(datetime);
       } catch (ParseException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
       String date = new SimpleDateFormat("K:mm a").format(dateObj);
       return date;
   }

}

