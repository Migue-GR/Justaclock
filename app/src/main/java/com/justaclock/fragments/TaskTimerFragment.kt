package com.justaclock.fragments

import android.os.Bundle
import android.os.SystemClock
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

        val awa = chronometerViewModel?.chronometerIsRunning
        val ewe = chronometerViewModel!!.lastTimeString
        if(chronometerViewModel?.chronometerIsRunning == true && chronometerViewModel!!.lastTimeString != "00:00:00"){
            chronometer.text = chronometerViewModel?.lastTimeString ?: "00:00:00"

            if(mainViewModel?.lastFragment == TAG) {

                startChronometer()

            } else {
                chronometerViewModel!!.timeWhenPause -= SystemClock.elapsedRealtime()

                clock_of_task_timer?.animateIndeterminate()
                fab_play_stopwatch.setImageResource(R.drawable.ic_pause_white_24dp)
                fab_stop_stopwatch.show()
                chronometer.base = chronometerViewModel!!.timeWhenPause + SystemClock.elapsedRealtime()
                chronometer.start()
                chronometerViewModel!!.timeWhenPause = 0
                chronometerViewModel?.chronometerIsRunning = true
//                startChronometer()
//                pauseChronometer()
            }
        }

        fab_play_stopwatch.setOnClickListener{
            if(chronometerViewModel?.chronometerIsRunning == true){
                clock_of_task_timer.stop()
                fab_play_stopwatch.setImageResource(R.drawable.ic_play_button_24dp)
//                pauseChronometer()
                chronometerViewModel?.timeWhenPause = chronometer.base - SystemClock.elapsedRealtime()
                chronometerViewModel?.chronometerIsRunning = false
                chronometer.stop()

            }else{
                startChronometer()
            }
        }

        fab_stop_stopwatch.setOnClickListener {
            TransitionManager.beginDelayedTransition(fragment_stopwatch, Slide(Gravity.BOTTOM))
            fab_stop_stopwatch.hide()
            fab_play_stopwatch.setImageResource(R.drawable.ic_play_button_24dp)
            chronometerViewModel!!.lastTimeString = "00:00:00"
            val taskName  = edtx_task.editText?.text ?: ""

            chronometer.stop()
            chronometerViewModel?.insertTask(Task(taskName.toString(), chronometer.text as String, false))
            chronometer.text = "00:00:00"
            chronometerViewModel!!.timeWhenPause       = 0
            chronometerViewModel?.chronometerIsRunning = false
        }

        fragment_stopwatch.setOnClickListener{
            val adapter: TaskAdapter = rcv_tasks.adapter as TaskAdapter
            adapter.getTasks()[0].preview = false
            adapter.notifyItemChanged(0)
        }

        setObservers()
    }

    private fun setObservers() {
        chronometerViewModel?.tasks?.observe(this, Observer {
            if(it != null){
                if(rcv_tasks.adapter == null){
                    initializeRecyclerView(it)

                }else{
                    updateItemsOfRecyclerView()
                }
            }
        })
    }

    private fun pauseChronometer(){
        clock_of_task_timer?.stop()
        fab_play_stopwatch.setImageResource(R.drawable.ic_play_button_24dp)
        fab_stop_stopwatch.show()
    }

    private fun startChronometer(){
        clock_of_task_timer?.animateIndeterminate()
        fab_play_stopwatch.setImageResource(R.drawable.ic_pause_white_24dp)
        fab_stop_stopwatch.show()
//        chronometer.base = SystemClock.elapsedRealtime() + chronometerViewModel!!.timeWhenPause
        chronometer.base = chronometerViewModel!!.timeWhenPause + SystemClock.elapsedRealtime()
        chronometer.start()
        chronometerViewModel!!.timeWhenPause = 0
        chronometerViewModel?.chronometerIsRunning = true
    }

    private fun updateItemsOfRecyclerView() {
        val adapter: TaskAdapter = rcv_tasks.adapter as TaskAdapter
        adapter.notifyItemInserted(adapter.getTasks().lastIndex)
    }

    private fun initializeRecyclerView(tasks : ArrayList<Task>) {
        rcv_tasks.layoutManager = LinearLayoutManager(activity!!, LinearLayoutManager.HORIZONTAL, false)
        rcv_tasks.adapter       = TaskAdapter(tasks, activity!!)
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

        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.text = "00:00:00"
        chronometer.stop()

//        chronometerViewModel?.chronometerIsRunning = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /* Set last fragment **/
        mainViewModel?.lastFragment = TAG

        if(chronometerViewModel?.chronometerIsRunning == true){
            chronometerViewModel?.timeWhenPause = chronometer.base - SystemClock.elapsedRealtime()
        }

        chronometerViewModel!!.lastTimeString = chronometer.text as String
    }
}