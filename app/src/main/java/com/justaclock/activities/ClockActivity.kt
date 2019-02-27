package com.justaclock.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.justaclock.R
import com.justaclock.tools.JustAClockTools
import com.justaclock.viewmodels.ClockViewModel
import kotlinx.android.synthetic.main.activity_clock.*

class ClockActivity : AppCompatActivity() {
    private var clockViewModel: ClockViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock)

        /*
         * Instance View Models
         */
        clockViewModel = ViewModelProviders.of(this).get(ClockViewModel::class.java)

        setObservers()
    }

    private fun setObservers() {
        clockViewModel!!.clockActivityIsOpen!!.observe(this, Observer { clockActivityIsOpen ->
            if(!clockActivityIsOpen){
                Handler().postDelayed({
                    JustAClockTools.startAnimation(this, activity_clock, R.layout.activity_clock_keyframe_2, 1200.toLong(), 1.0f)
                    clockViewModel!!.clockActivityIsOpen!!.postValue(true)
                }, 100)

            }else{
                JustAClockTools.startAnimation(this, activity_clock, R.layout.activity_clock_keyframe_2, 0.toLong(), 0f)
            }
        })
    }
}
