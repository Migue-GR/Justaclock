package com.justaclock.tools

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.justaclock.R

abstract class BaseActivity: AppCompatActivity() {
    /*
     * Vars
     */
    private var doubleBackToExitPressedOnce: Boolean? = false

    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayout())
        initUI()
    }

    /*
     * Activity structure
     */
    abstract fun getLayout(): Int
    abstract fun initUI()

    /*
     * Activity tools
     */
    fun createFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        transaction.replace(R.id.fyt_content, fragment)
        transaction.commit()
    }

    fun getRippleEffect(pressedColor: Int, backgroundDrawable: Drawable): RippleDrawable {
        return RippleDrawable(getPressedState(pressedColor), backgroundDrawable, null)
    }

    private fun getPressedState(pressedColor: Int): ColorStateList {
        return ColorStateList(arrayOf(intArrayOf()), intArrayOf(pressedColor))
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onBackPressed() {
        if(doubleBackToExitPressedOnce!!) {
            finishAffinity()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Presiona de nuevo para salir", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}