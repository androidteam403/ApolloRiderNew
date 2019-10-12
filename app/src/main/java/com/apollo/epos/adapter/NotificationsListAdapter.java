package com.apollo.epos.adapter;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.fragment.notifications.OnItemClickListener;
import com.apollo.epos.model.NotificationItemModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.MyViewHolder> {
    private Activity activity;
    private ArrayList<NotificationItemModel> notificationsList;
    private OnItemClickListener listener;

    public NotificationsListAdapter(Activity activity, ArrayList<NotificationItemModel> notificationsList, OnItemClickListener listener) {
        this.activity = activity;
        this.notificationsList = notificationsList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.parent_layout)
        LinearLayout parentLayout;
        @BindView(R.id.child_header_layout)
        RelativeLayout childHeaderLayout;
        @BindView(R.id.header_text)
        TextView headerText;
        @BindView(R.id.new_message_text)
        TextView newMessageText;
        @BindView(R.id.duration_time)
        TextView durationText;
        @BindView(R.id.message_body)
        TextView messageBody;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_notification_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NotificationItemModel item = notificationsList.get(position);
        if (item.isMessage()) {
            holder.parentLayout.setBackground(activity.getDrawable(R.drawable.notification_message_item_bg));
            holder.childHeaderLayout.setBackground(activity.getDrawable(R.drawable.notification_message_header_bg));
            holder.headerText.setText(item.getItemHeader());
            holder.headerText.setTextColor(activity.getColor(R.color.colorBlack));
            holder.newMessageText.setVisibility(View.VISIBLE);
            holder.durationText.setText(item.getOrderTime());
            holder.durationText.setTextColor(activity.getColor(R.color.colorBlack));
            holder.messageBody.setText(item.getMessageBody());
            holder.messageBody.setTextColor(activity.getColor(R.color.colorBlack));
            holder.messageBody.setBackground(activity.getDrawable(R.drawable.notification_message_body_bg));
        } else if (item.isNewOrder()) {
            holder.parentLayout.setBackground(activity.getDrawable(R.drawable.notification_neworder_item_bg));
            holder.childHeaderLayout.setBackground(activity.getDrawable(R.drawable.notification_neworder_header_bg));
            holder.headerText.setText(item.getItemHeader() + " " + item.getOrderID());
            holder.headerText.setTextColor(activity.getColor(R.color.colorWhite));
            holder.newMessageText.setVisibility(View.GONE);
            holder.durationText.setText(item.getOrderTime());
            holder.durationText.setTextColor(activity.getColor(R.color.colorWhite));
            holder.messageBody.setText(item.getMessageBody());
            holder.messageBody.setTextColor(activity.getColor(R.color.colorWhite));
            holder.messageBody.setBackground(activity.getDrawable(R.drawable.notification_neworder_body_bg));
        } else if (item.isAlert()) {
            holder.parentLayout.setBackground(activity.getDrawable(R.drawable.notification_alert_item_bg));
            holder.childHeaderLayout.setBackground(activity.getDrawable(R.drawable.notification_alert_header_bg));
            holder.headerText.setText(item.getItemHeader());
            holder.headerText.setTextColor(activity.getColor(R.color.colorWhite));
            holder.newMessageText.setVisibility(View.GONE);
            holder.durationText.setText(item.getOrderTime());
            holder.durationText.setTextColor(activity.getColor(R.color.colorWhite));
            holder.messageBody.setText(item.getMessageBody());
            holder.messageBody.setTextColor(activity.getColor(R.color.colorBlack));
            holder.messageBody.setBackground(activity.getDrawable(R.drawable.notification_message_body_bg));
        } else if (item.isOrder()) {
            holder.parentLayout.setBackground(activity.getDrawable(R.drawable.notification_order_item_bg));
            holder.childHeaderLayout.setBackground(activity.getDrawable(R.drawable.notification_order_header_bg));
            holder.headerText.setText(item.getItemHeader() + " " + item.getOrderID());
            holder.headerText.setTextColor(activity.getColor(R.color.colorWhite));
            holder.newMessageText.setVisibility(View.GONE);
            holder.durationText.setText(item.getOrderTime());
            holder.durationText.setTextColor(activity.getColor(R.color.colorWhite));
            holder.messageBody.setText(item.getMessageBody());
            holder.messageBody.setTextColor(activity.getColor(R.color.colorBlack));
            holder.messageBody.setBackground(activity.getDrawable(R.drawable.notification_message_body_bg));
        }

        holder.itemView.setOnClickListener(v -> {
            if(listener != null){
                listener.OnItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }
}
