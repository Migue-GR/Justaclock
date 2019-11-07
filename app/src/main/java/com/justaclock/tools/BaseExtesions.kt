package com.justaclock.tools

import android.content.res.Resources
import android.text.Editable

/** Base extensions */
fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()