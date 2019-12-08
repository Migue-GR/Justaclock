package com.justaclock.fragments

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.justaclock.R
import com.justaclock.receivers.Alarm_Receiver
import com.justaclock.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_alarm.*
import java.util.*

class AlarmFragment: Fragment() {
    /*
     * Declare ViewModels
     */
    private var mainViewModel: MainViewModel? = null

    /** Declare objects */
    private val calendar = Calendar.getInstance()
    private var my_intent: Intent? = null
    private var pending_intent: PendingIntent? = null
    private var alarm_manager: AlarmManager? = null

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

        my_intent = Intent(context, Alarm_Receiver::class.java)
        alarm_manager = context!!.getSystemService(ALARM_SERVICE) as AlarmManager
        createNotificationChannel()
        return view
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Canalsin"
            val descriptionText = "no c xdd"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Channelsito", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    fun setOnClickListeners(){
        btn_test.setOnClickListener {
            val builder = NotificationCompat.Builder(requireNotNull(context), "Channelsito")
                    .setSmallIcon(R.drawable.alarm_clock)
                    .setContentTitle("Idk what write here")
                    .setContentText("I also don't know what write here")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()

            with(NotificationManagerCompat.from(requireNotNull(context))) {
                // notificationId is a unique int for each notification that you must define
                notify(2, builder)
            }
        }

        btn_set_alarm.setOnClickListener {
            // setting calendar instance with the hour and minute that we picked
            // on the time picker
            calendar.set(Calendar.HOUR_OF_DAY, tp_alarm.hour)
            calendar.set(Calendar.MINUTE, tp_alarm.minute)

            // get the int values of the hour and minute
            val hour = tp_alarm.getHour()
            val minute = tp_alarm.getMinute()

            // convert the int values to strings
            var hour_string = hour.toString()
            var minute_string = minute.toString()

            // convert 24-hour time to 12-hour time
            if (hour > 12) {
                hour_string = (hour - 12).toString()
            }

            if (minute < 10) {
                //10:7 --> 10:07
                minute_string = "0" + minute.toString()
            }

            // method that changes the update text Textbox
//            Toast.makeText(context, "Alarm set to: $hour_string:$minute_string", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Alarm set to: $hour_string:$minute_string")

            // put in extra string into my_intent
            // tells the clock that you pressed the "alarm on" button
            my_intent?.putExtra("extra", "alarm on")

            // put in an extra int into my_intent
            // tells the clock that you want a certain value from the drop-down menu/spinner
            my_intent?.putExtra("whale_choice", 1)
//            Log.e("The whale id is", choose_whale_sound.toString())

            // create a pending intent that delays the intent
            // until the specified calendar time
            pending_intent = PendingIntent.getBroadcast(context, 0,
                    my_intent, PendingIntent.FLAG_UPDATE_CURRENT)

            // set the alarm manager
            alarm_manager?.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pending_intent
            )
        }

        btn_cancel_alarm.setOnClickListener {
            // method that changes the update text Textbox
            Toast.makeText(context, "Cancel alarm", Toast.LENGTH_SHORT).show()

            // cancel the alarm
//            alarm_manager?.cancel(pending_intent)

            // put extra string into my_intent
            // tells the clock that you pressed the "alarm off" button
            my_intent?.putExtra("extra", "alarm off")
            // also put an extra int into the alarm off section
            // to prevent crashes in a Null Pointer Exception
            my_intent?.putExtra("whale_choice", 1)

            // stop the ringtone
            activity?.sendBroadcast(my_intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainViewModel?.lastFragment = TAG
    }
}