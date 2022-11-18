package com.example.projectbootcamp.activities.ui.menu_2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Menu2ViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public Menu2ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment Gallery");
    }

    public LiveData<String> getText() {
        return mText;
    }
}