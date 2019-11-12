package com.example.carparts.ui.add_announcement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddAnnouncementViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddAnnouncementViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is add announcement fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}