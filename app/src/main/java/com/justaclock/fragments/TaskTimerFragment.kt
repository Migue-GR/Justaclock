package com.justaclock.fragments

import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.justaclock.R
import com.justaclock.adapters.Task
import com.justaclock.adapters.TaskAdapter
import com.justaclock.viewmodels.ChronometerViewModel
import com.justaclock.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_stopwatch.*

class TaskTimerFragment: Fragment() {
    /* ViewModels**/
    private var mainViewModel: MainViewModel? = null
    private var chronometerViewModel: ChronometerViewModel? = null

    companion object {
        val TAG: String = TaskTimerFragment::class.java.simpleName
        fun newInstance(): TaskTimerFragment = TaskTimerFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stopwatch, container, false)

        /* Instance ViewModels **/
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        chronometerViewModel = ViewModelProviders.of(activity!!).get(ChronometerViewModel::class.java)

        /* Set current fragment **/
        mainViewModel?.currentFragment = TAG

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChronometer()

        if(chronometerViewModel?.taskName != null){
            edtx_task.editText?.text = chronometerViewModel?.taskName
        }

        chronometer.text = chronometerViewModel?.lastTimeString ?: "00:00:00"

        if(chronometerViewModel?.chronometerIsRunning == true){
            chronometerViewModel?.timeWhenPause = chronometerViewModel?.base!! - SystemClock.elapsedRealtime()
            startChronometer()

        }else if(chronometer.text != "00:00:00"){
            pauseChronometer()
        }

        setObservers()
        setOnclickListeners()
    }

    private fun setObservers() {
        chronometerViewModel?.tasks?.observe(this, Observer {
            if(it != null){
                if(rcv_tasks.adapter == null){
                    initializeRecyclerView(it)

                }else{
                    addItemToRecyclerView()
                }
            }
        })
    }

    private fun startChronometer(){
        clock_of_task_timer?.animateIndeterminate()
        fab_play_stopwatch.setImageResource(R.drawable.ic_pause_white_24dp)
        fab_stop_stopwatch.show()
        chronometer.base = SystemClock.elapsedRealtime() + chronometerViewModel!!.timeWhenPause
        chronometer.start()
        chronometerViewModel!!.timeWhenPause = 0
        chronometerViewModel?.chronometerIsRunning = true

        if(chronometerViewModel?.base == null){
            Log.d(TAG, "chronometer.base: ${chronometer.base}")
            chronometerViewModel?.base = chronometer.base
        }
    }

    private fun pauseChronometer(){
        clock_of_task_timer?.stop()
        fab_play_stopwatch.setImageResource(R.drawable.ic_play_button_24dp)
        fab_stop_stopwatch.show()
    }

    private fun pauseChronometer2(){
        clock_of_task_timer.stop()
        fab_play_stopwatch.setImageResource(R.drawable.ic_play_button_24dp)
        chronometerViewModel?.timeWhenPause = chronometer.base - SystemClock.elapsedRealtime()
        chronometerViewModel?.chronometerIsRunning = false
        chronometer.stop()
    }

    private fun stopChronometer(){
        TransitionManager.beginDelayedTransition(fragment_stopwatch, Slide(Gravity.BOTTOM))
        clock_of_task_timer?.stop()
        fab_stop_stopwatch.hide()
        fab_play_stopwatch.setImageResource(R.drawable.ic_play_button_24dp)
        chronometerViewModel!!.lastTimeString = "00:00:00"
        val taskName  = edtx_task.editText?.text ?: ""

        chronometer.stop()
        chronometerViewModel?.insertTask(Task(taskName.toString(), chronometer.text as String, false))
        chronometer.text = "00:00:00"
        chronometerViewModel!!.timeWhenPause       = 0
        chronometerViewModel?.chronometerIsRunning = false
        chronometerViewModel?.base = null
        edtx_task.editText?.text = Editable.Factory.getInstance().newEditable("")
    }

    private fun initializeRecyclerView(tasks : ArrayList<Task>) {
        rcv_tasks.layoutManager = LinearLayoutManager(activity!!, LinearLayoutManager.HORIZONTAL, false)
        rcv_tasks.adapter       = TaskAdapter(tasks, activity!!)
    }

    private fun addItemToRecyclerView() {
        val adapter: TaskAdapter = rcv_tasks.adapter as TaskAdapter
//        adapter.notifyItemInserted(adapter.getTasks().lastIndex)
        if(adapter.getTasks().size == 2){
            if(adapter.getTasks()[0].preview){
                adapter.getTasks()[0].name = adapter.getTasks()[1].name
                adapter.getTasks()[0].time = adapter.getTasks()[1].time
                adapter.getTasks()[0].preview = false
                adapter.getTasks().removeAt(1)
                adapter.notifyItemChanged(0)

            }else{
                adapter.notifyItemInserted(adapter.getTasks().lastIndex)
            }

        }else{
            adapter.notifyItemInserted(adapter.getTasks().lastIndex)
        }
    }

    private fun initChronometer() {
        chronometer.setOnChronometerTickListener {chronometer ->
            val time = SystemClock.elapsedRealtime() - chronometer.base
            val h    = (time / 3600000).toInt()
            val m    = (time - h * 3600000).toInt() / 60000
            val s    = (time - (h * 3600000).toLong() - (m * 60000).toLong()).toInt() / 1000
            val t    = (if (h < 10) "0$h" else h.toString()).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
            chronometer.text = t
        }

//        chronometer.base = SystemClock.elapsedRealtime()
//        chronometer.text = "00:00:00"
//        chronometer.stop()
    }

    private fun setOnclickListeners() {
        fab_play_stopwatch.setOnClickListener{
            if(chronometerViewModel?.chronometerIsRunning == true){
                pauseChronometer2()

            }else{
                startChronometer()
            }
        }

        fab_stop_stopwatch.setOnClickListener {
            stopChronometer()
        }

        fragment_stopwatch.setOnClickListener{
//remove this
//            val adapter: TaskAdapter = rcv_tasks.adapter as TaskAdapter
//            adapter.getTasks()[0].preview = false
//            adapter.notifyItemChanged(0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /* Set last fragment **/
        mainViewModel?.lastFragment = TAG

        if(chronometerViewModel?.chronometerIsRunning == false && chronometer.text != "00:00:00"){
            chronometerViewModel?.base = chronometer.base
        }

        if(chronometerViewModel?.chronometerIsRunning == true) {
            chronometerViewModel?.timeWhenPause = chronometerViewModel?.base ?: 0 - SystemClock.elapsedRealtime()
        }

        chronometerViewModel?.lastTimeString = chronometer.text as String
        chronometerViewModel?.taskName = edtx_task.editText?.text
    }
}