package com.justaclock.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.justaclock.viewmodels.MainViewModel
import android.os.CountDownTimer
import com.justaclock.viewmodels.TimerViewModel
import kotlinx.android.synthetic.main.fragment_timer.*
import java.lang.String.format


class TimerFragment : Fragment() {
    /*
     * ViewModels
     */
    private var mainViewModel: MainViewModel? = null
    private lateinit var timerViewModel: TimerViewModel

    companion object {
        val TAG: String = TimerFragment::class.java.simpleName
        fun newInstance(): TimerFragment = TimerFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.justaclock.R.layout.fragment_timer, container, false)

        /*
         * Instance ViewModels
         */
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        timerViewModel = ViewModelProviders.of(activity!!).get(TimerViewModel::class.java)

        /*
         * Set current fragment
         */
        mainViewModel?.currentFragment = TAG

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCountDown()
    }

    private fun startCountDown() {
        object : CountDownTimer(timerViewModel.millisUntilFinished, 1000) {

            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                timerViewModel.millisUntilFinished = millisUntilFinished
                val minutes: Int = ((millisUntilFinished / 1000) / 60).toInt()
                val seconds: Int = ((millisUntilFinished / 1000) % 60).toInt()

                if (txtv_count_down_timer != null) {
                    txtv_count_down_timer.text = format("%02d:%02d", minutes, seconds)
                }
            }

            override fun onFinish() {
                txtv_count_down_timer.text = "FINISHED!"
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainViewModel?.lastFragment = TAG
    }
}