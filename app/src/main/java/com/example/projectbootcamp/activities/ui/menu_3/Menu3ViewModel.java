package com.example.projectbootcamp.activities.ui.menu_3;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Menu3ViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public Menu3ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment Review");
    }

    public LiveData<String> getText() {
        return mText;
    }
}