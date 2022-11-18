package com.example.projectbootcamp.activities.ui.menu_1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Menu1ViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public Menu1ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment Review");
    }

    public LiveData<String> getText() {
        return mText;
    }
}