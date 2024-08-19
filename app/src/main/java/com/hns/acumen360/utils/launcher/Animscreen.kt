package com.hns.acumen360.utils.launcher

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import com.hns.acumen360.R


/**
 * Created by Kamesh Kannan on 19-April-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
object Animscreen {
    fun animateFadeOff(context: Context) {
        (context as Activity).overridePendingTransition(
            0,
            R.anim.animate_fade_off
        )
    }
    fun animateSlideLeft(context: Context) {
        (context as Activity).overridePendingTransition(
            R.anim.animate_slide_left_enter,
            R.anim.animate_slide_left_exit
        )
    }

    fun animateSlideRightOut(context: Context) {
        (context as Activity).overridePendingTransition(
            0,
            R.anim.slide_right_out
        )
    }
    fun animateSlideRightIn(context: Context) {
        (context as Activity).overridePendingTransition(
            0,
            R.anim.slide_right_out
        )
    }
    fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 500
        view.startAnimation(anim)
    }
}