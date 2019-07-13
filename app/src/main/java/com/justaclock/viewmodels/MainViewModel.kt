package com.justaclock.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var currentFragment: String? = null
    var isTheFirstTimeInTheApp: Boolean? = true
}
