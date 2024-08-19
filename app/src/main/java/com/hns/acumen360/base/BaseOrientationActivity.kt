package com.hns.acumen360.base

import android.app.Activity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.hns.acumen360.R
import com.hns.acumen360.data.support.InstanceData
import com.hns.acumen360.ui.login.model.MenuAction
import com.hns.acumen360.listener.GetListener


/**
 * Created by Kamesh Kannan on 19-April-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
abstract class BaseOrientationActivity : AppCompatActivity() {

    lateinit var androidID: String
    lateinit var drawer: DrawerLayout
    var drawerToggle: ActionBarDrawerToggle? = null

    @LayoutRes
    protected abstract fun resourceLayout(): Int

    protected abstract fun setupView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT*/
        /*androidID = CommonUtils.getAndroidID(this)*/
        androidID = InstanceData.instance.androidUniqueId
        setContentView(resourceLayout())
        setupView(savedInstanceState)
    }

    fun setDrawerLayout() {
        drawer = findViewById(R.id.drawer_layout)
        drawerToggle = setupDrawerToggle()
        drawerToggle?.isDrawerIndicatorEnabled = true
        drawerToggle?.syncState()
        drawerToggle?.let { drawer.addDrawerListener(it) }
        drawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
            }

            override fun onDrawerClosed(drawerView: View) {
            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        })
    }

    private fun setupDrawerToggle(): ActionBarDrawerToggle {
        return ActionBarDrawerToggle(
            this,
            drawer,
            null,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
    }


    fun openCloseDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            drawer.openDrawer(GravityCompat.START)
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }

    fun closeNavigationDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    fun onMenuImageClikc() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeNavigationDrawer()
        } else {
            openCloseDrawer()
        }
    }


}