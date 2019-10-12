package com.apollo.epos.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apollo.epos.R;
import com.apollo.epos.model.NavDrawerModel;

public class NavigationDrawerAdapter extends ArrayAdapter<NavDrawerModel> {
    private Context mContext;
    private int layoutResourceId;
    private NavDrawerModel data[] = null;

    public NavigationDrawerAdapter(Context mContext, int layoutResourceId, NavDrawerModel[] data) {
        super(mContext, layoutResourceId, data);
        this.mContext = mContext;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    public void onSelection(int position) {
        int pos = 0;
        for (NavDrawerModel dataModel : data) {
            if (position == pos) {
                dataModel.setSelected(true);
            } else {
                dataModel.setSelected(false);
            }
            pos++;
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);
        NavDrawerModel folder = data[position];
        TextView textViewName = listItem.findViewById(R.id.textViewName);
        View dividerView = listItem.findViewById(R.id.divider_view);
        textViewName.setText(folder.name);
        listItem.findViewById(R.id.itemview).setVisibility(View.VISIBLE);
        if (folder.isSelected) {
            listItem.findViewById(R.id.itemview).setBackground(mContext.getDrawable(R.drawable.btn_nav_ripple_effect));
            dividerView.setVisibility(View.GONE);
        } else {
            listItem.findViewById(R.id.itemview).setBackgroundColor(Color.TRANSPARENT);
            if (position == 6) {
                dividerView.setVisibility(View.GONE);
            } else {
                dividerView.setVisibility(View.VISIBLE);
            }
        }

        return listItem;
    }
}
