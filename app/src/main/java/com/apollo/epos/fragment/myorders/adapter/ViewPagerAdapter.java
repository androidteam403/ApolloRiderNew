package com.apollo.epos.fragment.myorders.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.apollo.epos.R;
import com.apollo.epos.adapter.MyOrdersListAdapter;
import com.apollo.epos.databinding.AdapterViewPagerBinding;
import com.apollo.epos.fragment.myorders.MyOrdersFragmentCallback;
import com.apollo.epos.fragment.myorders.model.MyOrdersListResponse;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter implements MyOrdersListAdapter.MyOrdersListAdapterCallback {

    private Context context;
    private List<List<MyOrdersListResponse.Row>> listofMyOrdersList;
    private MyOrdersFragmentCallback mListener;

    public ViewPagerAdapter(Context context, List<List<MyOrdersListResponse.Row>> listofMyOrdersList, MyOrdersFragmentCallback mListener) {
        this.context = context;
        this.listofMyOrdersList = listofMyOrdersList;
        this.mListener = mListener;
    }

    @Override
    public int getCount() {
        return listofMyOrdersList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
//        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = layoutInflater.inflate(R.layout.adapter_view_pager, null);
        AdapterViewPagerBinding viewPagerBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_view_pager, container, false);
        List<MyOrdersListResponse.Row> myOrdersLists = listofMyOrdersList.get(position);

//        RecyclerView rec = view.findViewById(R.id.ordersRecyclerView);
//        RelativeLayout noOrderFountView = view.findViewById(R.id.no_order_found_layout);
//        TextView noOrdersInstatusText = view.findViewById(R.id.no_orders_instatus_text);

        if (myOrdersLists.size() > 0) {
//            MyOrdersListAdapter myOrdersListAdapter = new MyOrdersListAdapter(context, myOrdersLists, this::onClickOrder);
            MyOrderListAdapter myOrdersListAdapter = new MyOrderListAdapter(context, myOrdersLists, this::onClickOrder);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            viewPagerBinding.ordersRecyclerView.setLayoutManager(mLayoutManager);
            viewPagerBinding.ordersRecyclerView.setItemAnimator(new DefaultItemAnimator());
            viewPagerBinding.ordersRecyclerView.setAdapter(myOrdersListAdapter);
            viewPagerBinding.noOrderFoundLayout.setVisibility(View.GONE);
            viewPagerBinding.ordersRecyclerView.setVisibility(View.VISIBLE);
        } else {
            if (position == 0) {
                viewPagerBinding.noOrdersInstatusText.setText(R.string.label_no_orders_assigned);
            } else if (position == 1) {
                viewPagerBinding.noOrdersInstatusText.setText(R.string.label_no_orders_intransit);
            } else if (position == 2) {
                viewPagerBinding.noOrdersInstatusText.setText(R.string.label_no_orders_delivered);
            } else if (position == 3) {
                viewPagerBinding.noOrdersInstatusText.setText(R.string.label_no_orders_cancelled);
            }
            viewPagerBinding.ordersRecyclerView.setVisibility(View.GONE);
            viewPagerBinding.noOrderFoundLayout.setVisibility(View.VISIBLE);
        }
        container.addView(viewPagerBinding.getRoot());

        return viewPagerBinding.getRoot();

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public void onClickOrder(MyOrdersListResponse.Row order) {
        if (mListener != null)
            mListener.onStatusClick(order);
    }
}