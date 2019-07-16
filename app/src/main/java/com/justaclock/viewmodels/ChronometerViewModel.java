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
        tasks.getValue().add(new Task("dog cero", "00:0:00"));
        tasks.getValue().add(new Task("dog uno", "00:10:00"));
        tasks.getValue().add(new Task("dog dos", "00:20:00"));
        tasks.getValue().add(new Task("dog tres", "00:30:00"));
        tasks.getValue().add(new Task("dog cuatro", "00:40:00"));
        tasks.getValue().add(new Task("dog cinco", "00:50:00"));
        tasks.getValue().add(new Task("dog seis", "01:00:00"));
        tasks.getValue().add(new Task("dog siete", "01:10:00"));
        tasks.getValue().add(new Task("dog ocho", "01:20:00"));
        tasks.getValue().add(new Task("dog nueve", "01:30:00"));
        tasks.getValue().add(new Task("dog diez", "01:40:00"));
    }

    public void insertTask(Task task){
        tasks.getValue().add(new Task(task.getName(), task.getTime()));
        tasks.setValue(tasks.getValue());
    }
}
