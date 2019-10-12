package com.apollo.epos.fragment.takeneworder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TakeNewOrderViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TakeNewOrderViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}