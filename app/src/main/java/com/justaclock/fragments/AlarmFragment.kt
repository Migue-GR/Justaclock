package com.justaclock.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.justaclock.R
import com.justaclock.receivers.AlarmNotificationReceiver
import com.justaclock.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_alarm.*
import java.util.*


class AlarmFragment: Fragment() {
    /*
     * ViewModels
     */
    private var mainViewModel: MainViewModel? = null

    companion object {
        val TAG: String = AlarmFragment::class.java.simpleName
        fun newInstance(): AlarmFragment = AlarmFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_alarm, container, false)

        /*
         * Instance ViewModels
         */
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        /*
         * Set current fragment
         */
        mainViewModel?.currentFragment = TAG

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtv_title_alarm.setOnClickListener{
            Toast.makeText(context, "awa", Toast.LENGTH_SHORT).show()
            startAlarm()
        }
    }

    private fun startAlarm() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 18)
        calendar.set(Calendar.MINUTE, 16)
        calendar.set(Calendar.SECOND, 10)

        val manager       = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val myIntent      = Intent(activity, AlarmNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0)

        manager.set(
                AlarmManager.RTC_WAKEUP,
                SystemClock.elapsedRealtime() + calendar.timeInMillis,
                pendingIntent
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainViewModel?.lastFragment = TAG
    }
}