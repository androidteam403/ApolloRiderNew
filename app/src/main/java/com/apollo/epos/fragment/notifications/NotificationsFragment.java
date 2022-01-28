package com.apollo.epos.fragment.notifications;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.adapter.NotificationsListAdapter;
import com.apollo.epos.base.BaseFragment;
import com.apollo.epos.model.NotificationItemModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsFragment extends BaseFragment implements OnItemClickListener{
    private Activity mActivity;
    private NotificationsListAdapter mNotificationListAdapter;
    @BindView(R.id.notificationRecyclerView)
    RecyclerView notificationRecyclerView;
    private ArrayList<NotificationItemModel> notificationsList = new ArrayList<>();

    private NotificationsViewModel notificationsViewModel;

    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
//        notificationsViewModel =
//                ViewModelProviders.of(this).get(NotificationsViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
//        final TextView textView = root.findViewById(R.id.text_share);
//        notificationsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        getNotifications();

        mNotificationListAdapter = new NotificationsListAdapter(mActivity, notificationsList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        notificationRecyclerView.setLayoutManager(mLayoutManager);
        notificationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        notificationRecyclerView.setAdapter(mNotificationListAdapter);
    }

    private void getNotifications() {
        NotificationItemModel notificationItem = new NotificationItemModel();
        notificationItem.setItemHeader("MESSAGE");
        notificationItem.setMessage(true);
        notificationItem.setNewOrder(false);
        notificationItem.setAlert(false);
        notificationItem.setOrder(false);
        notificationItem.setOrderID("");
        notificationItem.setOrderTime("01:00min");
        notificationItem.setMessageBody("Submit your latest photo in the head office by today evening");
        notificationsList.add(notificationItem);

        notificationItem = new NotificationItemModel();
        notificationItem.setItemHeader("NEW ORDER");
        notificationItem.setMessage(false);
        notificationItem.setNewOrder(true);
        notificationItem.setAlert(false);
        notificationItem.setOrder(false);
        notificationItem.setOrderID("#:38728");
        notificationItem.setOrderTime("21:00min");
        notificationItem.setMessageBody("You have new order to delivery with in 45min. Which is in your location");
        notificationsList.add(notificationItem);

        notificationItem = new NotificationItemModel();
        notificationItem.setItemHeader("ALERT");
        notificationItem.setMessage(false);
        notificationItem.setNewOrder(false);
        notificationItem.setAlert(true);
        notificationItem.setOrder(false);
        notificationItem.setOrderID("");
        notificationItem.setOrderTime("01:34min");
        notificationItem.setMessageBody("There is a curfew in the road no.1 Banjara Hills so, wait for the next instructions");
        notificationsList.add(notificationItem);

        notificationItem = new NotificationItemModel();
        notificationItem.setItemHeader("ORDER");
        notificationItem.setMessage(false);
        notificationItem.setNewOrder(false);
        notificationItem.setAlert(false);
        notificationItem.setOrder(true);
        notificationItem.setOrderID("#:38729");
        notificationItem.setOrderTime("26 Aug 19, 10AM");
        notificationItem.setMessageBody("You have successfully completed the delivery in 1:15min");
        notificationsList.add(notificationItem);

        notificationItem = new NotificationItemModel();
        notificationItem.setItemHeader("ORDER");
        notificationItem.setMessage(false);
        notificationItem.setNewOrder(false);
        notificationItem.setAlert(false);
        notificationItem.setOrder(true);
        notificationItem.setOrderID("#:38730");
        notificationItem.setOrderTime("26 Aug 19, 01:05PM");
        notificationItem.setMessageBody("You have successfully completed the delivery in 2:10min");
        notificationsList.add(notificationItem);
    }

    @Override
    public void OnItemClick(NotificationItemModel model) {
//        if (model.getItemHeader().equalsIgnoreCase("NEW ORDER")) {
//            ((NavigationActivity) Objects.requireNonNull(mActivity)).showFragment(new NewOrderFragment(), R.string.menu_take_order);
//            ((NavigationActivity) Objects.requireNonNull(mActivity)).updateSelection(-1);
//        }
    }
}