package com.apollo.epos.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apollo.epos.R;
import com.apollo.epos.activity.orderdelivery.OrderDeliveryActivityCallback;

public class CustomReasonAdapter extends BaseAdapter {
    Activity activity;
    String[] reasonList;
    LayoutInflater inflter;
    private OrderDeliveryActivityCallback mListener;

    public CustomReasonAdapter(Activity activity, String[] reasonList, OrderDeliveryActivityCallback mListener) {
        this.activity = activity;
        this.reasonList = reasonList;
        inflter = (LayoutInflater.from(activity));
        this.mListener = mListener;
    }


    @Override
    public int getCount() {
        return reasonList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflter.inflate(R.layout.view_custom_reason, null);
        TextView names = view.findViewById(R.id.reason_id);
        names.setText(reasonList[position]);
        if (mListener != null) {
//            mListener.onClickHandoverTheParcelItem(reasonList[position]);
        }
        return view;
    }
}
