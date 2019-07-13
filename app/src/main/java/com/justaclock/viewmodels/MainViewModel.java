package com.justaclock.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.justaclock.adapters.Task;

import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {
    public MutableLiveData<String> currentFragment;

    public MainViewModel(@NonNull Application application) {
        super(application);
        currentFragment = new MutableLiveData<>();
    }
}
