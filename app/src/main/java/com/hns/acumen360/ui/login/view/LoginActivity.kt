package com.hns.acumen360.ui.login.view

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import com.google.android.material.textfield.TextInputEditText
import com.hns.acumen360.R
import com.hns.acumen360.base.BaseActivity
import com.hns.acumen360.base.BaseApplication
import com.hns.acumen360.base.GlobalBundle
import com.hns.acumen360.base.ResponseType
import com.hns.acumen360.base.ViewModelResponse
import com.hns.acumen360.data.local.db.entity.UserDetailsDB
import com.hns.acumen360.data.remote.stars.login.LoginRequest
import com.hns.acumen360.data.remote.stars.login.LoginResponse
import com.hns.acumen360.data.support.InstanceData
import com.hns.acumen360.data.viewmodel.RemoteStarViewModel
import com.hns.acumen360.di.component.ActivityComponent
import com.hns.acumen360.ui.chatbot.view.ChatBotActivity
import com.hns.acumen360.ui.verifyotp.view.VerifyOTPActivity
import com.hns.acumen360.utils.common.CommonUtils
import com.hns.acumen360.utils.common.Constants
import com.hns.acumen360.utils.launcher.NavigationLauncher


/**
 * Created by Kamesh Kannan on 22-April-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */

class LoginActivity : BaseActivity<RemoteStarViewModel>() {

    override fun setContext() = this@LoginActivity
    override fun setOrientation() = BaseApplication.orientationPortrait
    override fun setAcumenThemes(): Int? = null
    override fun resourceLayout() = R.layout.activity_login
    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)


    private lateinit var btnLogin: AppCompatButton
    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var cbRememberMe: AppCompatCheckBox
    val items: ArrayList<String> = ArrayList()

    override fun setupView(savedInstanceState: Bundle?) {
        initView()
        //setSpinner()
        setListener()
        viewModelCommon.loginUserSelectActive()
    }

    /**
     *
     * Initialize the views
     */

    private fun initView() {
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        cbRememberMe = findViewById(R.id.cb_remember_me)
        btnLogin = findViewById(R.id.btn_login)
    }

    private fun setListener() {
        /**
         *
         * Login the Applications (Validation with Api Call)
         */
        btnLogin.setOnClickListener {
            if (etUsername.text.toString().isEmpty()) {
                showMessage(R.string.enter_email_id)
            } else if (etPassword.text.toString().isEmpty()) {
                showMessage(R.string.enter_password)
            } else {
                InstanceData.instance.androidUniqueId = CommonUtils.getAndroidID(this)
                //InstanceData.instance.selectType = spinnerType.selectedItem.toString()
                val loginRequest =
                    LoginRequest(
                        etUsername.text.toString(),
                        etPassword.text.toString(),
                        Constants.COSMIC_TYPE,
                        androidID
                    )
                viewModel.loginAuthentication(
                    loginRequest,
                    cbRememberMe.isChecked
                ) { response ->
                    showMessage(response)
                }
            }
        }
    }

    /**
     *
     * Fetch Response from API or Database
     */
    override fun setViewModel() {
        viewModelCommon.loginUsers.observe(this) {
            when (it.type) {
                ViewModelResponse.SUCCESS -> {
                    when (it.subType) {
                        ResponseType.INSERT_LOGIN_USER -> {
                            navigateChatBotActivity()
                        }

                        ResponseType.INSERT_USER_DETAILS -> {
                            navigateChatBotActivity()
                        }

                        ResponseType.SELECT_LOGIN_USER -> {
                            val data = it.data as MutableList<UserDetailsDB>
                            if (data.isNotEmpty()) {
                                loadUserInfo(data[0])
                            }
                        }

                        ResponseType.NAVIGATE_VERIFY_OTP -> {
                            val response: LoginResponse = it.data as LoginResponse
                            navigateVerifyOTPActivity(response)
                        }
                    }
                }

                ViewModelResponse.THROWABLE -> {
                    when (it.subType) {
                        ResponseType.INSERT_LOGIN_USER -> {
                            showMessage(it.data.toString())
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * Display the user information from Data base. if user already save the information
     */

    private fun loadUserInfo(userDetailsDB: UserDetailsDB?) {
        if (userDetailsDB != null) {
            etUsername.setText(userDetailsDB.email ?: "")
            etPassword.setText(userDetailsDB.password ?: "")
            cbRememberMe.isChecked = userDetailsDB.remember
        }
    }


    /**
     *
     * Navigate to ChatBot Activity
     */
    private fun navigateChatBotActivity() {
        NavigationLauncher.launchActivity(
            mActivity,
            ChatBotActivity::class.java
        )
    }

    /**
     *
     * Navigate to Verify OTP Activity with bundle data
     */
    private fun navigateVerifyOTPActivity(response: LoginResponse) {
        val bundle = Bundle()
        bundle.putParcelable(GlobalBundle.LOGIN_RESP, response)
        bundle.putString(GlobalBundle.EMAIL_ID, etUsername.text.toString())
        bundle.putBoolean(GlobalBundle.REMEMBER_ME, cbRememberMe.isChecked)
        bundle.putString(GlobalBundle.PASSWORD, etPassword.text.toString())
        NavigationLauncher.launchActivityBundleStack(
            mActivity,
            VerifyOTPActivity::class.java, bundle
        )
    }
}