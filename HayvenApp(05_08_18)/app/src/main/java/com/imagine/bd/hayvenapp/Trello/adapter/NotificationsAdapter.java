package com.imagine.bd.hayvenapp.Trello.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.Trello.Utils;
import com.imagine.bd.hayvenapp.Trello.model.TrelloModel;
import com.imagine.bd.hayvenapp.Trello.vo.MemberVO;
import com.imagine.bd.hayvenapp.Trello.vo.NotificationVO;


public class NotificationsAdapter extends ArrayAdapter<NotificationVO> {

    public List<NotificationVO> mNotifications;
    private LayoutInflater mInflater;
    private TrelloModel mModel;
    
    static class ViewHolder {
        protected TextView notificationText;
        protected TextView dateText;

    }
    
    public NotificationsAdapter(Context context, int textViewResourceId, List<NotificationVO> notifications) {
        super(context, textViewResourceId, notifications);

        mModel = TrelloModel.getInstance();
        mInflater = LayoutInflater.from(context);
        mNotifications = notifications;
    }
    
    public void updateNotifications(List<NotificationVO> notifications) {
        mNotifications = notifications;
        notifyDataSetChanged();
    }

    public void addNotifications(List<NotificationVO> notifications) {
        mNotifications.addAll(notifications);
        notifyDataSetChanged();
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.notification_row, null);

            holder = new ViewHolder();
            holder.notificationText = (TextView)     convertView.findViewById(R.id.notification_text);
            holder.dateText         = (TextView)     convertView.findViewById(R.id.date);

            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NotificationVO notification = mNotifications.get(position);
        
        if (notification != null) {
            holder.notificationText.setText(Utils.getHtmlFormattedNotificationText(notification));
            holder.dateText.setText(Utils.getFormattedStringFromDate(notification.date));
            
            //MemberVO member = mModel.getAllBoardsResult().members.get(mModel.getAllBoardsResult().members.indexOf(notification.idMemberCreator));
            
            //holder.gravatar.setInitials(member.initials);

        }
        
        return convertView;
    }
}
