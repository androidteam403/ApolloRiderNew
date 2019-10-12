package com.apollo.epos.fragment.takeneworder;

public interface OnItemClickListener {
    void onItemClick(int position, boolean isSelected);

    void onDecrementClick(int position);

    void onIncrementClick(int position);
}
