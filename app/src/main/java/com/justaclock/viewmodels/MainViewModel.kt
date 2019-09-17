package com.justaclock.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var currentFragment: String? = null
    var isTheFirstTimeInTheApp: Boolean? = true
    var lastFragment: String? = null
}
