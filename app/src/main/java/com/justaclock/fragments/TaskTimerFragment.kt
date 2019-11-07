/**
 * @author Migue-GR
 */

package com.justaclock.fragments

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.justaclock.R
import com.justaclock.adapters.Task
import com.justaclock.adapters.TaskAdapter
import com.justaclock.tools.toEditable
import com.justaclock.viewmodels.ChronometerViewModel
import com.justaclock.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_task_timer.*
import kotlinx.android.synthetic.main.fragment_task_timer_content.*
import java.lang.Exception

class TaskTimerFragment : Fragment() {
    /** Declare ViewModels */
    private var mainViewModel: MainViewModel? = null
    private lateinit var chronometerViewModel: ChronometerViewModel

    /** Declare objects */
    private val handler = Handler()

    companion object {
        val TAG: String = TaskTimerFragment::class.java.simpleName
        fun newInstance(): TaskTimerFragment = TaskTimerFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_task_timer, container, false)

        /** Instantiate ViewModels */
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        chronometerViewModel = ViewModelProviders.of(activity!!).get(ChronometerViewModel::class.java)

        /** Set current fragment */
        mainViewModel?.currentFragment = TAG

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initChronometer()

        edtx_task.editText!!.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                val text = editable?.toString() ?: ""

                if (text.isNotEmpty()) {
                    chronometerViewModel.taskName.value = text
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                /** Implementation not necessary */
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                /** Implementation not necessary */
            }
        })

        if (chronometerViewModel.taskName.value != null) {
            edtx_task.editText?.text = chronometerViewModel.taskName.value?.toEditable()
            txtv_task_label.text = chronometerViewModel.taskName.value
        }

        chronometer.text = chronometerViewModel.lastTimeString ?: "00:00:00"

        if (chronometerViewModel.chronometerIsRunning) {
            chronometerViewModel.timeWhenPause = chronometerViewModel.base!! - SystemClock.elapsedRealtime()
            startChronometer()
        } else if (chronometer.text != "00:00:00") {
            pauseChronometer()
        }

        setObservers()
        setOnclickListeners()
    }

    private fun setObservers() {
        chronometerViewModel.tasks.observe(this, Observer {
            if (it != null) {
                if (rcv_tasks.adapter == null) {
                    initializeRecyclerView(it)
                } else {
                    addItemToRecyclerView()
                }
            }

            if (it.size == 0) {
                txtv_tasks_empty.visibility = View.VISIBLE
            } else {
                txtv_tasks_empty.visibility = View.GONE
            }
        })

        chronometerViewModel.taskName.observe(this, Observer { taskName ->
            if (taskName != null && taskName.isNotEmpty()) {
                txtv_task_label.text = taskName
            }
        })
    }

    private fun initChronometer() {
        chronometer.setOnChronometerTickListener { chronometer ->
            val time = SystemClock.elapsedRealtime() - chronometer.base
            val h = (time / 3600000).toInt()
            val m = (time - h * 3600000).toInt() / 60000
            val s = (time - (h * 3600000).toLong() - (m * 60000).toLong()).toInt() / 1000
            val t = (if (h < 10) "0$h" else h.toString()).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
            chronometer.text = t
        }
    }

    private fun setOnclickListeners() {
        fab_play_task_timer.setOnClickListener {
            TransitionManager.beginDelayedTransition(fragment_task_timer)
            if (chronometerViewModel.chronometerIsRunning) {
                pauseChronometer2()
            } else {
                startChronometer()
            }
        }

        fab_stop_task_timer.setOnClickListener {
            stopChronometer()
        }

        chronometer.setOnClickListener {
            validateFocusMode()
        }

        clock_of_task_timer.setOnClickListener {
            validateFocusMode()
        }
    }

    private fun validateFocusMode() {
        if (chronometerViewModel.focusMode) {
            chronometerViewModel.focusMode = false
            hideFocusMode()
        } else {
            chronometerViewModel.focusMode = true
            showFocusMode()
        }
    }

    private fun showFocusMode() {
        val keyframeTwo = ConstraintSet()
        keyframeTwo.clone(context, R.layout.fragment_task_timer_content_keyframe_2)
        TransitionManager.beginDelayedTransition(fragment_task_timer)
        keyframeTwo.applyTo(fragment_task_timer)
    }

    private fun hideFocusMode() {
        val keyframeTwo = ConstraintSet()
        keyframeTwo.clone(context, R.layout.fragment_task_timer_content)
        TransitionManager.beginDelayedTransition(fragment_task_timer)
        keyframeTwo.applyTo(fragment_task_timer)
        fab_stop_task_timer.show()
    }

    private fun startChronometer() {
        if (chronometerViewModel.chronometerIsRunning == false) {
            chronometerViewModel.beforeItWasOnPause = true
        }

        clock_of_task_timer?.animateIndeterminate()
        fab_play_task_timer.setImageResource(R.drawable.ic_pause_white_24dp)
        fab_stop_task_timer.show()
        chronometer.base = SystemClock.elapsedRealtime() + chronometerViewModel!!.timeWhenPause
        chronometer.start()
        chronometerViewModel!!.timeWhenPause = 0
        chronometerViewModel.chronometerIsRunning = true

        if (chronometerViewModel.base == null) {
            Log.d(TAG, "chronometer.base: ${chronometer.base}")
            chronometerViewModel.base = chronometer.base
        }
    }

    private fun pauseChronometer() {
        clock_of_task_timer?.stop()
        fab_play_task_timer.setImageResource(R.drawable.ic_play_button_24dp)
        fab_stop_task_timer.show()
        chronometerViewModel.beforeItWasOnPause = false
    }

    private fun pauseChronometer2() {
        clock_of_task_timer.stop()
        fab_play_task_timer.setImageResource(R.drawable.ic_play_button_24dp)
        chronometerViewModel.timeWhenPause = chronometer.base - SystemClock.elapsedRealtime()
        chronometerViewModel.chronometerIsRunning = false
        chronometer.stop()
        chronometerViewModel.beforeItWasOnPause = false
    }

    private fun stopChronometer() {
        clock_of_task_timer?.animateToTime(12, 0)
        fab_stop_task_timer.hide()
        fab_play_task_timer.setImageResource(R.drawable.ic_play_button_24dp)
        chronometerViewModel!!.lastTimeString = "00:00:00"
        val taskName = edtx_task.editText?.text ?: ""

        chronometer.stop()
        chronometerViewModel.insertTask(Task(taskName.toString(), chronometer.text as String, false))
        chronometer.text = "00:00:00"
        chronometerViewModel!!.timeWhenPause = 0
        chronometerViewModel.chronometerIsRunning = false
        chronometerViewModel.base = null
        edtx_task.editText?.text = Editable.Factory.getInstance().newEditable("")
        txtv_task_label.text = ""
        chronometerViewModel.beforeItWasOnPause = false
    }

    private fun initializeRecyclerView(tasks : ArrayList<Task>) {
        rcv_tasks.layoutManager = LinearLayoutManager(activity!!, LinearLayoutManager.HORIZONTAL, false)
        rcv_tasks.adapter = TaskAdapter(tasks, activity!!, txtv_tasks_empty)

        val adapter: TaskAdapter = rcv_tasks.adapter as TaskAdapter

        handler.postDelayed({
            try {
                if (adapter.getTasks().size >= 1) {
                    rcv_tasks.smoothScrollToPosition(adapter.getTasks().lastIndex)
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }
        }, 400)
    }

    private fun addItemToRecyclerView() {
        val adapter: TaskAdapter = rcv_tasks.adapter as TaskAdapter

        if (adapter.getTasks().size == 2) {
            if (adapter.getTasks()[0].preview) {
                adapter.getTasks()[0].name = adapter.getTasks()[1].name
                adapter.getTasks()[0].time = adapter.getTasks()[1].time
                adapter.getTasks()[0].preview = false
                adapter.getTasks().removeAt(1)
                adapter.notifyItemChanged(0)
            } else {
                adapter.notifyItemInserted(adapter.getTasks().lastIndex)
            }
        } else {
            adapter.notifyItemInserted(adapter.getTasks().lastIndex)
        }

        /** Scroll to last item with animation */
        rcv_tasks.smoothScrollToPosition(adapter.getTasks().lastIndex)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /** Set last fragment */
        mainViewModel?.lastFragment = TAG

        if (chronometerViewModel.chronometerIsRunning == false && chronometer.text != "00:00:00") {
            chronometerViewModel.base = chronometer.base
        }

        if (chronometerViewModel.chronometerIsRunning == true && chronometerViewModel.beforeItWasOnPause == true) {
            chronometerViewModel.base = chronometer.base
        } else if (chronometerViewModel.chronometerIsRunning == true) {
            chronometerViewModel.timeWhenPause = chronometerViewModel.base ?: 0 - SystemClock.elapsedRealtime()
        }

        chronometerViewModel.lastTimeString = chronometer.text as String
        handler.removeCallbacksAndMessages(null)
    }
}