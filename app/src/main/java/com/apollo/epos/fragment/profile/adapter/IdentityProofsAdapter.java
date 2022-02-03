package com.apollo.epos.fragment.profile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apollo.epos.R;
import com.apollo.epos.databinding.AdapterIdentityProofsBinding;
import com.apollo.epos.fragment.profile.ProfileFragmentCallback;
import com.apollo.epos.model.GetRiderProfileResponse;
import com.bumptech.glide.Glide;

import java.util.List;

public class IdentityProofsAdapter extends RecyclerView.Adapter<IdentityProofsAdapter.ViewHolder> {
    private Context context;
    private List<GetRiderProfileResponse.IdentificationProof> identificationProofList;
    private ProfileFragmentCallback mListener;

    public IdentityProofsAdapter(Context context, List<GetRiderProfileResponse.IdentificationProof> identificationProofList, ProfileFragmentCallback mListener) {
        this.context = context;
        this.identificationProofList = identificationProofList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterIdentityProofsBinding identityProofsBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_identity_proofs, parent, false);
        return new ViewHolder(identityProofsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetRiderProfileResponse.IdentificationProof identificationProof = identificationProofList.get(position);
        holder.identityProofsBinding.proofName.setText(identificationProof.getDocType().getName());
        Glide.with(context)
                .load(identificationProof.getDoc().get(0).getDimenesions().get300300FullPath())
                .error(R.drawable.placeholder_image)
                .placeholder(R.drawable.placeholder_image)
                .into(holder.identityProofsBinding.proofIcon);
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClickIdentityProof(identificationProof);
            }
        });
    }


    @Override
    public int getItemCount() {
        return identificationProofList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterIdentityProofsBinding identityProofsBinding;

        public ViewHolder(@NonNull AdapterIdentityProofsBinding identityProofsBinding) {
            super(identityProofsBinding.getRoot());
            this.identityProofsBinding = identityProofsBinding;
        }
    }
}
