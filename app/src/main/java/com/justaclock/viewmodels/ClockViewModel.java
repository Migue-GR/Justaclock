package com.justaclock.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ClockViewModel extends AndroidViewModel {
    public MutableLiveData<Boolean> clockActivityIsOpen;

    public ClockViewModel(@NonNull Application application) {
        super(application);
        clockActivityIsOpen = new MutableLiveData<>();
        clockActivityIsOpen.setValue(false);
    }
}
