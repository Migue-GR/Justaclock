package com.justaclock.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({
            /*
             * Go to ClockActivity
             */
            startActivity(Intent(this, ClockActivity::class.java))

        }, 1000)
    }
}
