package com.example.carparts.ui.all_announcement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AllAnnouncementViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AllAnnouncementViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is All announcement fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}