package com.justaclock.viewmodels

import android.app.Application
import android.text.Editable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.justaclock.adapters.Task
import java.util.ArrayList

class TimerViewModel(application: Application) : AndroidViewModel(application) {
    private var _millisUntilFinished: Long = 30 * 60000
    var millisUntilFinished: Long
        get() = _millisUntilFinished
        set(value) {
            _millisUntilFinished = value
        }
}
