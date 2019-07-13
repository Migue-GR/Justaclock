package com.justaclock.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.view.View
import kotlinx.android.synthetic.main.activity_chronometer.*
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.justaclock.adapters.Task
import com.justaclock.adapters.TaskAdapter
import com.justaclock.viewmodels.ChronometerViewModel
import android.widget.Chronometer
import android.widget.Chronometer.OnChronometerTickListener



class ChronometerActivity : AppCompatActivity() {
    private var chronometerViewModel: ChronometerViewModel? = null
    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.justaclock.R.layout.activity_chronometer)

        /*
         * Instance View Models
         */
        chronometerViewModel = ViewModelProviders.of(this).get(ChronometerViewModel::class.java)

        hideSystemUI()
        startChronometer()

        chronometer.setOnClickListener {
            if(chronometerViewModel?.chronometerIsRunning == true){
                chronometerViewModel?.timeWhenPause = chronometer.base - SystemClock.elapsedRealtime()
                chronometer.stop()
                chronometerViewModel?.chronometerIsRunning = false

            }else{
                chronometer.base = SystemClock.elapsedRealtime() + chronometerViewModel!!.timeWhenPause
                chronometer.start()
                chronometerViewModel!!.timeWhenPause = 0
                chronometerViewModel?.chronometerIsRunning = true
            }
        }

        chronometer.setOnLongClickListener {
            val taskName  = edtx_task.text ?: ""

            chronometer.stop()
            chronometerViewModel?.insertTask(Task(taskName.toString(), chronometer.text as String))
            chronometer.text = "00:00:00"
            chronometerViewModel!!.timeWhenPause       = 0
            chronometerViewModel?.chronometerIsRunning = false

            return@setOnLongClickListener true
        }

        activity_chronometer.setOnClickListener{
            edtx_task.clearFocus()
            hideSystemUI()
        }

        setObservers()
    }

    private fun setObservers() {
        chronometerViewModel?.tasks?.observe(this, Observer {
            if(it != null){
                initializeRecyclerView(it)
            }
        })
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
        chronometer.start()
        chronometerViewModel?.chronometerIsRunning = true
    }

    private fun initializeRecyclerView(tasks : ArrayList<Task>) {
        rcv_tasks.layoutManager = LinearLayoutManager(this)
        rcv_tasks.adapter       = TaskAdapter(tasks, this)
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        val decorView = window.decorView
        decorView.systemUiVisibility = (SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or SYSTEM_UI_FLAG_LAYOUT_STABLE
                or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or SYSTEM_UI_FLAG_FULLSCREEN)
    }
}
