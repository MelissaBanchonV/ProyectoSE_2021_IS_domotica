package com.example.proyectose_2021is.ui.exterior;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class exteriorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public exteriorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}