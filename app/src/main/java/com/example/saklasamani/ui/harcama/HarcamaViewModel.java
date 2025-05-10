package com.example.saklasamani.ui.harcama;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HarcamaViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public HarcamaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}