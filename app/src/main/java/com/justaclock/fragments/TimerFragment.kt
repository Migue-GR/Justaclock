package com.justaclock.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.justaclock.R
import com.justaclock.viewmodels.MainViewModel

class TimerFragment : Fragment() {
    /*
     * ViewModels
     */
    private var mainViewModel: MainViewModel? = null

    companion object {
        val TAG: String = TimerFragment::class.java.simpleName
        fun newInstance(): TimerFragment = TimerFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_timer, container, false)

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
}