/**
 * @author Migue-GR
 */

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

class MainActivity : BaseActivity() {
    /** Declare ViewModels */
    private var mainViewModel: MainViewModel? = null

    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun initUI() {
        /** Instantiate ViewModels */
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        if (mainViewModel?.isTheFirstTimeInTheApp!!) {
            createFragment(ClockFragment.newInstance())
            mainViewModel?.isTheFirstTimeInTheApp = false
        }

        setOnClickListeners()
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

            cyt_task_timer.setOnClickListener {
                if (mainViewModel?.currentFragment != TaskTimerFragment.TAG) {
                    createFragment(TaskTimerFragment.newInstance())
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }
}