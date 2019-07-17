package com.justaclock.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.justaclock.adapters.Task;
import java.util.ArrayList;

public class ChronometerViewModel extends AndroidViewModel {
    public MutableLiveData<ArrayList<Task>> tasks;
    public boolean                          chronometerIsRunning;
    public long                             timeWhenPause;
    public String                           lastTimeString;

    public ChronometerViewModel(@NonNull Application application) {
        super(application);
        tasks = new MutableLiveData<>();
        tasks.setValue(new ArrayList<>());
        tasks.getValue().add(new Task("Example task", "00:00:00", true));
        tasks.getValue().add(new Task("Example task", "00:00:00", true));
    }

    public void insertTask(Task task){
        tasks.getValue().add(new Task(task.getName(), task.getTime(), false));
        tasks.setValue(tasks.getValue());
    }
}
