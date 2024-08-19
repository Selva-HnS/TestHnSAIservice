package com.hns.acumen360.ui.splash.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.hns.acumen360.R
import com.hns.acumen360.base.BaseActivity
import com.hns.acumen360.base.BaseApplication
import com.hns.acumen360.base.ResponseType
import com.hns.acumen360.base.ViewModelResponse
import com.hns.acumen360.data.local.db.entity.UserDetailsDB
import com.hns.acumen360.data.support.InstanceData
import com.hns.acumen360.data.viewmodel.RemoteStarViewModel
import com.hns.acumen360.di.component.ActivityComponent
import com.hns.acumen360.ui.chatbot.view.ChatBotActivity
import com.hns.acumen360.ui.login.view.LoginActivity
import com.hns.acumen360.utils.launcher.NavigationLauncher

class SplashActivity : BaseActivity<RemoteStarViewModel>() {

    override fun setContext() = this@SplashActivity
    override fun setOrientation() = BaseApplication.orientationPortrait
    override fun setAcumenThemes(): Int? = null
    override fun resourceLayout() = R.layout.activity_splash
    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {
        setTimers()
    }

    override fun setViewModel() {
        viewModelCommon.loginUsers.observe(this) {
            when (it.type) {
                ViewModelResponse.SUCCESS -> {
                    when (it.subType) {
                        ResponseType.SELECT_LOGIN_USER -> {
                            val data = it.data as MutableList<UserDetailsDB>
                            if (data.isNotEmpty()) {
                                navigateChatbotActivity(data[0])
                            } else {
                                navigateLoginActivity(false)
                            }
                        }
                    }
                }

                ViewModelResponse.THROWABLE -> {
                    when (it.subType) {
                        ResponseType.SELECT_LOGIN_USER -> {
                            showMessage(it.data.toString())
                        }
                    }
                }
            }
        }
    }

    private fun navigateLoginActivity(isRemember: Boolean) {
        NavigationLauncher.launchActivity(
            mActivity,
            LoginActivity::class.java
        )
    }

    private fun navigateChatbotActivity(userDetailsDB: UserDetailsDB) {
        if (userDetailsDB.isLogin) {
            InstanceData.instance.androidUniqueId = userDetailsDB.uniqueID.toString()
           // InstanceData.instance.selectType = userDetailsDB.projectID.toString()
            if (userDetailsDB.remember) {
                NavigationLauncher.launchActivity(mActivity, ChatBotActivity::class.java)
            } else {
                navigateLoginActivity(true)
            }
        } else {
            navigateLoginActivity(false)
        }
    }

    private fun setTimers() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            viewModelCommon.loginUserSelectActive()
        }, 2000)
    }
}