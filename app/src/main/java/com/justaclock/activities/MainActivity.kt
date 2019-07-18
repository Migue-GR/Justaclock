package com.justaclock.activities

import android.util.Log
import com.justaclock.R
import com.justaclock.fragments.ClockFragment
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.ViewModelProviders
import com.justaclock.fragments.AlarmFragment
import com.justaclock.fragments.TaskTimerFragment
import com.justaclock.fragments.TimerFragment
import com.justaclock.tools.BaseActivity
import com.justaclock.viewmodels.MainViewModel
import kotlin.Exception

class MainActivity: BaseActivity() {
    /*
     * ViewModels
     */
    private var mainViewModel: MainViewModel? = null

    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun initUI() {
        /*
         * Instance ViewModels
         */
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        setRippleEffects()
        setOnClickListeners()

        if (mainViewModel?.isTheFirstTimeInTheApp!!) {
            createFragment(ClockFragment.newInstance())
            mainViewModel?.isTheFirstTimeInTheApp = false
        }
    }

    private fun setRippleEffects() {
        try {
            cyt_clock.background = getRippleEffect(
                    resources.getColor(R.color.colorAccent, null),
                    resources.getDrawable(R.drawable.bg_corner_top_right_20dp_primary_dark, null)
            )

            cyt_alarm.background = getRippleEffect(
                    resources.getColor(R.color.colorAccent, null),
                    resources.getDrawable(R.drawable.bg_solid_primary_dark, null)
            )

            cyt_timer.background = getRippleEffect(
                    resources.getColor(R.color.colorAccent, null),
                    resources.getDrawable(R.drawable.bg_solid_primary_dark, null)
            )

            cyt_stopwatch.background = getRippleEffect(
                    resources.getColor(R.color.colorAccent, null),
                    resources.getDrawable(R.drawable.bg_corner_bottom_right_20dp_primary_dark, null)
            )

        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    private fun setOnClickListeners() {
        try {
            cyt_clock.setOnClickListener {
                if (mainViewModel?.currentFragment != ClockFragment.TAG) {
                    createFragment(ClockFragment.newInstance())
                }
            }

            cyt_alarm.setOnClickListener {
                if (mainViewModel?.currentFragment != AlarmFragment.TAG) {
                    createFragment(AlarmFragment.newInstance())
                }
            }

            cyt_timer.setOnClickListener {
                if (mainViewModel?.currentFragment != TimerFragment.TAG) {
                    createFragment(TimerFragment.newInstance())
                }
            }

            cyt_stopwatch.setOnClickListener {
                if (mainViewModel?.currentFragment != TaskTimerFragment.TAG) {
                    createFragment(TaskTimerFragment.newInstance())
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }
}