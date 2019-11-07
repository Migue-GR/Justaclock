package com.justaclock.viewmodels

import android.app.Application
import android.text.Editable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.justaclock.adapters.Task
import java.util.ArrayList

class ChronometerViewModel(application: Application) : AndroidViewModel(application) {
    var tasks: MutableLiveData<ArrayList<Task>> = MutableLiveData()
    var chronometerIsRunning: Boolean = false
    var timeWhenPause: Long = 0
    var lastTimeString: String? = null
    var base: Long? = null
    var focusMode: Boolean = false
    var beforeItWasOnPause: Boolean = false
    var taskName: MutableLiveData<String?> = MutableLiveData()

    init {
        tasks.value = ArrayList()
        tasks.value!!.add(Task("Example task", "00:00:00", true))
    }

    fun insertTask(task: Task) {
        tasks.value!!.add(Task(task.name, task.time, false))
        tasks.value = tasks.value
    }
}
