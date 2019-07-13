package com.justaclock.activities

import android.content.Intent
import android.os.Handler
import android.util.Log
import com.justaclock.R
import com.justaclock.tools.BaseActivity

class SplashActivity: BaseActivity() {
    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    override fun getLayout(): Int {
        return R.layout.activity_splash
    }

    override fun initUI() {
        goToMainActivity()
    }

    private fun goToMainActivity() {
        Handler().postDelayed({
            try{
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)

            }catch (e: Exception) {
                Log.e(TAG, e.message)
            }
        }, 1000)
    }
}