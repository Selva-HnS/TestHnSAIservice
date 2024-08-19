package com.hns.acumen360.utils.launcher

import android.app.Activity
import android.content.Intent
import android.os.Bundle


/**
 * Created by Kamesh Kannan on 19-April-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
object NavigationLauncher {
    val CLEAR_NEW_TOP = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    val CLEAR_TOP = Intent.FLAG_ACTIVITY_CLEAR_TOP
    val NEW_TASK = Intent.FLAG_ACTIVITY_NEW_TASK

    /**
     * Launch Explicit Intent to navigate another Class with stack
     *
     * @param activity,         Current BaseActivity of context
     * @param destinationClass, Destination class to navigate.
     */
    fun launchActivityStack(
        activity: Activity,
        destinationClass: Class<out Activity>
    ) {
        activity.startActivity(Intent(activity, destinationClass))
        Animscreen.animateSlideLeft(activity)
    }

    /**
     * Launch Explicit Intent to navigate another Class and finish the current activity
     *
     * @param activity,         Current BaseActivity of context
     * @param destinationClass, Destination class to navigate.
     */
    fun launchFromSplashActivity(
        activity: Activity,
        destinationClass: Class<out Activity>,
        bundle: Bundle
    ) {
        activity.startActivity(
            Intent(activity, destinationClass)
            .putExtras(bundle))
        activity.finish()
        Animscreen.animateFadeOff(activity)
    }

    /**
     * Launch Explicit Intent to navigate another Class and finish the current activity
     *
     * @param activity,         Current BaseActivity of context
     * @param destinationClass, Destination class to navigate.
     */
    fun launchActivity(activity: Activity, destinationClass: Class<out Activity>) {
        activity.startActivity(Intent(activity, destinationClass))
        activity.finish()
        Animscreen.animateSlideLeft(activity)
    }


    /**
     *
     *  * Launch Explicit Intent to navigate another Class with stack
     *  * Navigate the data with bundle
     *
     *
     * @param activity,         Current BaseActivity of context
     * @param destinationClass, Destination class to navigate.
     * @param bundle`           Add a set of BaseActivity extended data to the intent.
     */
    fun launchActivityBundleStack(
        activity: Activity,
        destinationClass: Class<out Activity>,
        bundle: Bundle
    ) {
        activity.startActivity(
            Intent(activity, destinationClass)
            .putExtras(bundle))
        Animscreen.animateSlideLeft(activity)
    }

    /**
     *
     *  * Launch Explicit Intent to navigate another Class and finish the current activity
     *  * Navigate the data with bundle
     *
     *
     * @param activity,         Current BaseActivity of context
     * @param destinationClass, Destination class to navigate.
     * @param bundle            Add a set of BaseActivity extended data to the intent.
     */
    fun launchActivityBundle(
        activity: Activity,
        destinationClass: Class<out Activity>,
        bundle: Bundle
    ) {
        activity.startActivity(
            Intent(activity, destinationClass)
            .putExtras(bundle))
        activity.finish()
        Animscreen.animateSlideLeft(activity)
    }

    /**
     *
     *  * Launch Explicit Intent to navigate another Class and finish the current activity
     *  * Navigate the data with bundle
     *
     *
     * @param activity         Current BaseActivity of context
     * @param destinationClass Destination class to navigate.
     * @param bundle           Add a set of BaseActivity extended data to the intent.
     * @param requestCode      Activity request code.
     */
    fun launchStartActivityForResult(
        activity: Activity,
        destinationClass: Class<out Activity>,
        bundle: Bundle,
        requestCode: Int
    ) {
        activity.startActivityForResult(
            Intent(activity, destinationClass)
                .putExtras(bundle),
            requestCode
        )
        Animscreen.animateSlideLeft(activity)
    }

    /**
     *
     *  * Launch Explicit Intent to navigate another Class and finish the current activity with Flag
     *
     *
     * @param activity         Current BaseActivity of context
     * @param destinationClass Destination class to navigate.
     * @param flags            Set special flags controlling how this intent is handled.
     */
    fun launchActivityFlag(
        activity: Activity,
        destinationClass: Class<out Activity>,
        flags: Int
    ) {
        activity.startActivity(
            Intent(activity, destinationClass)
            .setFlags(flags))
        Animscreen.animateSlideLeft(activity)
    }

    /**
     *
     *  * Launch Explicit Intent to navigate another Class and finish the current activity
     *  * Navigate the data with bundle with Flag
     *
     *
     * @param activity         Current BaseActivity of context
     * @param destinationClass Destination class to navigate.
     * @param bundle           Add a set of BaseActivity extended data to the intent.
     * @param flags            Set special flags controlling how this intent is handled.
     */
    fun launchActivityFlagBundle(
        activity: Activity,
        destinationClass: Class<out Activity>,
        bundle: Bundle,
        flags: Int
    ) {
        activity.startActivity(
            Intent(activity, destinationClass)
                .putExtras(bundle)
                .setFlags(flags)
        )
        Animscreen.animateSlideLeft(activity)
    }
}