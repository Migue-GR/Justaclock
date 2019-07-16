package com.justaclock.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.justaclock.R
import com.justaclock.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_clock.*
import java.text.DateFormat
import java.util.*

class ClockFragment: Fragment() {
    /*
     * ViewModels
     */
    private var mainViewModel: MainViewModel? = null

    /*
     * Vars
     */
    val handler = Handler()

    companion object {
        val TAG: String = ClockFragment::class.java.simpleName
        fun newInstance(): ClockFragment = ClockFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_clock, container, false)

        /*
         * Instance ViewModels
         */
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        /*
         * Set current fragment
         */
        mainViewModel?.currentFragment = TAG
        startClock()

        return view
    }

    private fun startClock() {
        handler.postDelayed(object: Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                try {
                    val hour    = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    val minutes = Calendar.getInstance().get(Calendar.MINUTE)
                    val seconds = Calendar.getInstance().get(Calendar.SECOND)
                    val millis  = Calendar.getInstance().get(Calendar.MILLISECOND)

                    val calendar: Calendar = Calendar.getInstance()
                    val currentDate: String = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.time)

                    txtv_current_date.text = currentDate
                    clock.setTime(hour, minutes, seconds, millis)

                    if(minutes < 10){
                        txtv_current_time.text = "$hour:0$minutes"

                    }else{
                        txtv_current_time.text    = "$hour:$minutes"
                    }

                    if(seconds < 10){
                        txtv_current_seconds.text = "0$seconds"

                    }else{
                        txtv_current_seconds.text = "$seconds"
                    }

                    handler.postDelayed(this, 1000)

                } catch (e: Exception) {
                    Log.e(TAG, e.message)
                }
            }
        }, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainViewModel?.lastFragment = TAG
        handler.removeCallbacksAndMessages(null)
    }
}