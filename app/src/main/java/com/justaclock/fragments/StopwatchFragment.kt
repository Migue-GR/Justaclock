package com.justaclock.fragments

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.justaclock.R
import com.justaclock.adapters.Task
import com.justaclock.adapters.TaskAdapter
import com.justaclock.viewmodels.ChronometerViewModel
import com.justaclock.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_stopwatch.*

class StopwatchFragment: Fragment() {
    /*
     * ViewModels
     */
    private var mainViewModel: MainViewModel? = null
    private var chronometerViewModel: ChronometerViewModel? = null

    companion object {
        val TAG: String = StopwatchFragment::class.java.simpleName
        fun newInstance(): StopwatchFragment = StopwatchFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stopwatch, container, false)

        /*
         * Instance ViewModels
         */
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        chronometerViewModel = ViewModelProviders.of(activity!!).get(ChronometerViewModel::class.java)

        /*
         * Set current fragment
         */
        mainViewModel?.currentFragment = TAG

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TransitionManager.beginDelayedTransition(fragment_stopwatch)
        startChronometer()

        if(chronometerViewModel?.chronometerIsRunning == false && chronometerViewModel!!.lastTimeString != "00:00:00"){
            chronometer.text = chronometerViewModel?.lastTimeString ?: "00:00:00"
            fab_play_stopwatch.setImageResource(R.drawable.ic_play_button_24dp)
            fab_stop_stopwatch.show()

            if(mainViewModel?.lastFragment == TAG){
                fab_play_stopwatch.setImageResource(R.drawable.ic_pause_white_24dp)
                fab_stop_stopwatch.show()
                chronometer.base = SystemClock.elapsedRealtime() + chronometerViewModel!!.timeWhenPause
                chronometer.start()
                chronometerViewModel!!.timeWhenPause = 0
                chronometerViewModel?.chronometerIsRunning = true
            }
        }

        fab_play_stopwatch.setOnClickListener{
            if(chronometerViewModel?.chronometerIsRunning == true){
                fab_play_stopwatch.setImageResource(R.drawable.ic_play_button_24dp)
                chronometerViewModel?.timeWhenPause = chronometer.base - SystemClock.elapsedRealtime()
                chronometer.stop()
                chronometerViewModel?.chronometerIsRunning = false

            }else{
                fab_play_stopwatch.setImageResource(R.drawable.ic_pause_white_24dp)
                fab_stop_stopwatch.show()
                chronometer.base = SystemClock.elapsedRealtime() + chronometerViewModel!!.timeWhenPause
                chronometer.start()
                chronometerViewModel!!.timeWhenPause = 0
                chronometerViewModel?.chronometerIsRunning = true
            }
        }

        fab_stop_stopwatch.setOnClickListener {
            fab_stop_stopwatch.hide()
            fab_play_stopwatch.setImageResource(R.drawable.ic_play_button_24dp)
            chronometerViewModel!!.lastTimeString = "00:00:00"
            val taskName  = edtx_task.editText?.text ?: ""

            chronometer.stop()
            chronometerViewModel?.insertTask(Task(taskName.toString(), chronometer.text as String))
            chronometer.text = "00:00:00"
            chronometerViewModel!!.timeWhenPause       = 0
            chronometerViewModel?.chronometerIsRunning = false
        }

        fragment_stopwatch.setOnClickListener{
            edtx_task.clearFocus()
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

    private fun updateItemsOfRecyclerView() {
        val adapter: TaskAdapter = rcv_tasks.adapter as TaskAdapter
//        adapter.notifyDataSetChanged()
        adapter.notifyItemInserted(adapter.getTasks().lastIndex)
    }

    private fun initializeRecyclerView(tasks : ArrayList<Task>) {
        rcv_tasks.layoutManager = LinearLayoutManager(activity!!, LinearLayoutManager.HORIZONTAL, false)
        rcv_tasks.adapter       = TaskAdapter(tasks, activity!!)
    }

    private fun startChronometer() {
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
        chronometerViewModel?.chronometerIsRunning = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainViewModel?.lastFragment = TAG

        if(chronometerViewModel?.chronometerIsRunning == true){
            chronometerViewModel?.timeWhenPause = chronometer.base - SystemClock.elapsedRealtime()
        }

        chronometerViewModel!!.lastTimeString = chronometer.text as String
    }
}