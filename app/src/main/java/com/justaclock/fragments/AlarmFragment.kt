package com.justaclock.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.justaclock.R
import com.justaclock.viewmodels.MainViewModel

class AlarmFragment: Fragment() {
    /*
     * ViewModels
     */
    private var mainViewModel: MainViewModel? = null

    companion object {
        private val TAG: String = this::class.java.simpleName
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
        mainViewModel?.currentFragment?.value = TAG

        return view
    }
}