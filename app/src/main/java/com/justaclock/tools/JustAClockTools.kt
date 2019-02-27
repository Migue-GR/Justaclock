package com.justaclock.tools

import android.content.Context
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

object JustAClockTools {
    fun startAnimation(context: Context, frame1: ConstraintLayout, frame2: Int?, duration: Long?, tension: Float?) {
        val constraintSet = ConstraintSet()
        val transition    = ChangeBounds()
        constraintSet.clone(context, frame2!!)
        transition.interpolator = AnticipateOvershootInterpolator(tension!!)
        transition.duration = duration!!
        TransitionManager.beginDelayedTransition(frame1, transition)
        constraintSet.applyTo(frame1)
    }
}
