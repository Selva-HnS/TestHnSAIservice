package com.hns.acumen360.base

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.airbnb.lottie.LottieAnimationView
import com.hns.acumen360.R
import com.hns.acumen360.data.remote.stars.threads.MessageItem
import com.hns.acumen360.data.viewmodel.CommonViewModel
import com.hns.acumen360.data.viewmodel.RemoteGalaxyViewModel
import com.hns.acumen360.di.component.ActivityComponent
import com.hns.acumen360.di.component.DaggerActivityComponent
import com.hns.acumen360.di.module.ActivityModule
import com.hns.acumen360.listener.GetListener
import com.hns.acumen360.ui.chatbot.adaptor.ThreadChildAdapter
import com.hns.acumen360.ui.login.model.MenuAction
import com.hns.acumen360.utils.common.CommonUtils
import java.util.Date
import javax.inject.Inject


/**
 * Created by Kamesh Kannan on 19-April-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    @Inject
    lateinit var viewModelCommon: CommonViewModel

    @Inject
    lateinit var viewModelGalaxy: RemoteGalaxyViewModel

    @Inject
    lateinit var viewModel: VM
    lateinit var androidID: String
    lateinit var drawer: DrawerLayout
    var drawerToggle: ActionBarDrawerToggle? = null
    lateinit var mActivity: Activity

    protected abstract fun setContext(): Activity
    protected abstract fun setOrientation(): Int
    protected abstract fun setAcumenThemes(): Int?

    @LayoutRes
    protected abstract fun resourceLayout(): Int
    protected abstract fun injectDependencies(activityComponent: ActivityComponent)
    protected abstract fun setupView(savedInstanceState: Bundle?)
    protected abstract fun setViewModel()

    lateinit var pLoader: ConstraintLayout
    var lottieAnimation: LottieAnimationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        setAcumenThemes()?.let { setTheme(it) }
        super.onCreate(savedInstanceState)
        if (setOrientation() != null)
            requestedOrientation = setOrientation()
        injectDependencies(buildActivityComponent())
        androidID = CommonUtils.getAndroidID(this)
        mActivity = setContext()
        setContentView(resourceLayout())
        setupObservers()
        setupView(savedInstanceState)
        setViewModel()
        pLoader = layoutInflater.inflate(R.layout.progress_loader, null) as ConstraintLayout
        addContentView(
            pLoader, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
        pLoader.visibility = View.GONE
        lottieAnimation = pLoader.findViewById(R.id.lottie_speed)
    }

    protected open fun setupObservers() {
        viewModelCommon.onCreate()
        viewModelGalaxy.onCreate()
        val vmList = arrayListOf(viewModelCommon, viewModel, viewModelGalaxy)
        for (vm in vmList) {
            vm.messageString.observe(this, Observer {
                it.data?.run { showMessage(this) }
            })

            vm.messageStringId.observe(this, Observer {
                it.data?.run { showMessage(this) }
            })

            vm.showProgress.observe(this, Observer {
                if (it) {
                    showProgress()
                } else {
                    hideProgress()
                }
            })
        }
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

    fun showProgress() {
        pLoader.visibility = View.VISIBLE
    }

    fun hideProgress() {
        pLoader.visibility = View.GONE
    }

    open fun btShortMenu(
        list: ArrayList<MenuAction>,
        mActivity: Activity,
        view: View,
        listener: GetListener,
        isServiceHistory: Boolean
    ) {
        val contextCompat = ContextThemeWrapper(mActivity, R.style.popupMenus)
        val popupMenu = PopupMenu(contextCompat, view)
        for (item in list.indices) {
            popupMenu.menu.add(item, item, item, list[item].name)
        }
        popupMenu.menu.setGroupDividerEnabled(true)
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                if (isServiceHistory) {
                    if (item.itemId == 2) {
                        showCustomDialog(listener)
                        return false
                    }
                }

                listener.getMenuData(
                    list[item.itemId].displayName,
                    list[item.itemId].action,
                    list[item.itemId].feature
                )
                return true
            }
        })
        popupMenu.show()
    }


    open fun btnThreadMore(
        list: ArrayList<MenuAction>,
        mActivity: Activity,
        view: View,
        listener: GetListener,
        threadID: String,
        position: Int,
        childPosition: Int,
        dataItem: MessageItem,
        tvContent: AppCompatEditText,
        threadChildAdapter: ThreadChildAdapter?
    ) {
        val contextCompat = ContextThemeWrapper(mActivity, R.style.popupMenus)
        val popupMenu = PopupMenu(contextCompat, view)
        for (item in list.indices) {
            popupMenu.menu.add(item, item, item, list.get(item).name)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            popupMenu.menu.setGroupDividerEnabled(true)
        }
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                if (item.itemId == 0) {
                    // edit
                    listener.getThreadMore(
                        threadID,
                        true,
                        false,
                        position,
                        childPosition,
                        dataItem,
                        tvContent
                    )
                    Log.i("onMenuItemClick", "start: " + Date())
                } else {
                    //delete
                    listener.getThreadMore(
                        threadID,
                        false,
                        true,
                        position,
                        childPosition,
                        dataItem,
                        tvContent
                    )
                }
                return true
            }
        })
        popupMenu.show()
    }

    fun showMessage(input: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, input, Toast.LENGTH_SHORT).show()
        }
    }

    fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))


    private fun buildActivityComponent() =
        DaggerActivityComponent
            .builder()
            .applicationComponent((application as BaseApplication).applicationComponent)
            .activityModule(ActivityModule(this))
            .build()


    private fun showCustomDialog(listener: GetListener) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.layout_custom_dialog, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val editChasisNo: AppCompatEditText = dialogView.findViewById(R.id.edit_chasis_no)
        val editChasisRegNo: AppCompatEditText = dialogView.findViewById(R.id.edit_reg_no)
        val confirmButton: AppCompatButton = dialogView.findViewById(R.id.btn_confirm)
        confirmButton.setOnClickListener {
            val chasisNo = editChasisNo.text.toString()
            val regNo = editChasisRegNo.text.toString()

            if (!editChasisNo.text.toString().isNullOrEmpty() || !editChasisRegNo.text.toString()
                    .isNullOrEmpty()
            ) {
                listener.getServiceHistory(chasisNo, regNo)
                dialogBuilder.dismiss()
            } else {
                showMessage("Enter chasis number or registration number")
            }
        }
        dialogBuilder.show()
    }

    fun hideKeypad(windowToken: IBinder) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}