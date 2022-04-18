package com.apollo.epos.fragment.complaints.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.activity.reports.adapter.OrdersCodStatusAdapter;
import com.apollo.epos.databinding.AdapterComplaintsListBinding;
import com.apollo.epos.databinding.LoadingProgressbarBinding;
import com.apollo.epos.fragment.complaints.ComplaintsFragmentCallback;
import com.apollo.epos.fragment.complaints.model.ComplaintsResponse;
import com.apollo.epos.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ComplaintsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ComplaintsResponse.Row> complaintsList;
    private ComplaintsFragmentCallback mListener;


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public ComplaintsListAdapter(Context mContext, List<ComplaintsResponse.Row> complaintsList, ComplaintsFragmentCallback mListener) {
        this.mContext = mContext;
        this.complaintsList = complaintsList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            AdapterComplaintsListBinding complaintsListBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.adapter_complaints_list, parent, false);
            return new ComplaintViewHolder(complaintsListBinding);
        } else {
            LoadingProgressbarBinding loadingProgressbarBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.loading_progressbar, parent, false);
            return new LoadingViewHolder(loadingProgressbarBinding);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ComplaintsResponse.Row complaint = complaintsList.get(position);
        if (holder instanceof ComplaintsListAdapter.ComplaintViewHolder) {
            complaintsOnBindViewHolder((ComplaintViewHolder) holder, position, complaint);
        } else if (holder instanceof OrdersCodStatusAdapter.LoadingViewHolder) {

        }
    }

    private void complaintsOnBindViewHolder(ComplaintsListAdapter.ComplaintViewHolder holder, int position, ComplaintsResponse.Row complaint) {
        holder.complaintsListBinding.complaintNumber.setText(complaint.getComplaintNo() == null ? "-" : complaint.getComplaintNo());
        holder.complaintsListBinding.reason.setText(complaint.getReason().getName());
        holder.complaintsListBinding.comment.setText(complaint.getComments());
        holder.complaintsListBinding.status.setText(complaint.getStatus().getName());
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date orderDates = formatter.parse(complaint.getCreatedTime());
            long orderDateMills = orderDates.getTime();
            holder.complaintsListBinding.complaintedDate.setText(CommonUtils.getTimeFormatter(orderDateMills));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return complaintsList.size();
    }

    public int getItemViewType(int position) {
        return complaintsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ComplaintViewHolder extends RecyclerView.ViewHolder {
        AdapterComplaintsListBinding complaintsListBinding;

        public ComplaintViewHolder(@NonNull AdapterComplaintsListBinding complaintsListBinding) {
            super(complaintsListBinding.getRoot());
            this.complaintsListBinding = complaintsListBinding;
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        LoadingProgressbarBinding loadingProgressbarBinding;

        public LoadingViewHolder(@NonNull LoadingProgressbarBinding loadingProgressbarBinding) {
            super(loadingProgressbarBinding.getRoot());
            this.loadingProgressbarBinding = loadingProgressbarBinding;
        }
    }
}
