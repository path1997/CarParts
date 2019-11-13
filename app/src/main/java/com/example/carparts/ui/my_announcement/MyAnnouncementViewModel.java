package com.example.carparts.ui.my_announcement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyAnnouncementViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyAnnouncementViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my announcement fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}