package com.hns.acumen360.base

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.ActivityInfo
import com.hns.acumen360.di.component.ApplicationComponent
import com.hns.acumen360.di.component.DaggerApplicationComponent
import com.hns.acumen360.di.module.ApplicationModule


/**
 * Created by Kamesh Kannan on 07-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
class BaseApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        val orientationPortrait = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val orientationLandscape = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val orientationUnSpecified= ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        val screenSize= ActivityInfo.CONFIG_SCREEN_SIZE

        /**
         * Return the Project Application Context
         * @return Returns the Context refers the application context.
         */
        @Synchronized
        @JvmStatic
        fun getAppContext(): Context {
            return context
        }
    }

    /**
     * Create a Project Application with injecting the DaggerComponent
     */
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        injectDependencies()
    }

    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }

    fun setComponent(applicationComponent: ApplicationComponent) {
        this.applicationComponent = applicationComponent
    }

}