package com.apollo.epos.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apollo.epos.R;

public class OrderListAdapter extends BaseAdapter {
    Activity activity;
    String[] reasonList;
    LayoutInflater inflter;

    public OrderListAdapter(Activity activity, String[] reasonList) {
        this.activity = activity;
        this.reasonList = reasonList;
        inflter = (LayoutInflater.from(activity));
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
        return view;
    }
}
