package com.hns.acumen360.ui.verifyotp.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.Log
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.hns.acumen360.R
import com.hns.acumen360.base.BaseActivity
import com.hns.acumen360.base.BaseApplication
import com.hns.acumen360.base.GlobalBundle
import com.hns.acumen360.base.ResponseType
import com.hns.acumen360.base.ViewModelResponse
import com.hns.acumen360.data.remote.stars.login.LoginRequest
import com.hns.acumen360.data.remote.stars.login.LoginResponse
import com.hns.acumen360.data.remote.stars.resend.ResendOTPRequest
import com.hns.acumen360.data.remote.stars.verifyotp.VerifyOtpRequest
import com.hns.acumen360.data.viewmodel.RemoteStarViewModel
import com.hns.acumen360.di.component.ActivityComponent
import com.hns.acumen360.ui.chatbot.view.ChatBotActivity
import com.hns.acumen360.utils.common.CommonUtils
import com.hns.acumen360.utils.launcher.NavigationLauncher
import com.hns.acumen360.utils.log.Logger


/**
 * Created by Ranjith G on 11-July-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */

class VerifyOTPActivity : BaseActivity<RemoteStarViewModel>() {
    override fun setContext() = this@VerifyOTPActivity
    override fun setOrientation() = BaseApplication.orientationPortrait
    override fun setAcumenThemes(): Int? = null
    override fun resourceLayout() = R.layout.activity_verify_otp

    private lateinit var btnVerify: AppCompatButton
    lateinit var editOTP: AppCompatEditText
    private lateinit var tvTimer: AppCompatTextView
    private lateinit var tvResendOTP: AppCompatTextView
    private lateinit var tvOTPSentMessage: AppCompatTextView

    private var loginResponse: LoginResponse? = null
    private var emailID: String? = null
    private var rememberMe: Boolean = false
    private var password: String? = ""
    private var validTime: Long = 0
    private var otpMaxLength: Int = 0
    private var runnableTimer: Runnable? = null
    private var handlerTimer: Handler? = null


    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {
        getBundle()
        initView()
        otpExpireTimeLimitationAndSetData()
        setListener()
        startTimer()

    }

    /**
     *
     * Retrieve response from View Model
     */
    override fun setViewModel() {
        viewModelCommon.loginUsers.observe(this) {
            when (it.type) {
                ViewModelResponse.SUCCESS -> {
                    when (it.subType) {
                        ResponseType.INSERT_USER_DETAILS -> {
                            navigateChatBotActivity()
                        }

                        ResponseType.NAVIGATE_VERIFY_OTP -> {
                            loginResponse = it.data as LoginResponse
                            otpExpireTimeLimitationAndSetData()
                            startTimer()
                        }
                    }
                }

                ViewModelResponse.THROWABLE -> {
                    when (it.subType) {

                    }
                }
            }
        }
    }

    /**
     *
     * Get bundle data
     */

    private fun getBundle() {
        val bundle = intent.extras
        if (bundle != null) {
            loginResponse = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(GlobalBundle.LOGIN_RESP, LoginResponse::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(GlobalBundle.LOGIN_RESP)
            }
            emailID = bundle.getString(GlobalBundle.EMAIL_ID)
            password = bundle.getString(GlobalBundle.PASSWORD)
            rememberMe = bundle.getBoolean(GlobalBundle.REMEMBER_ME)
        }
    }

    /**
     *
     * Set OTP valid time and OTP Length etc from Login API Response
     */
    private fun otpExpireTimeLimitationAndSetData() {
        validTime = loginResponse?.otpExpiresIn ?: 0
        otpMaxLength = loginResponse?.otpLength ?: 0
        tvOTPSentMessage.text = loginResponse?.otpSentMessage ?: ""
        editOTP.filters = arrayOf(InputFilter.LengthFilter(otpMaxLength))

        /*
        val inputType = resources.getString(R.string.digits)
        editOTP.keyListener = DigitsKeyListener.getInstance(inputType!!)
        editOTP.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)*/

    }

    /**
     *
     * Initialize the views
     */
    private fun initView() {
        btnVerify = findViewById(R.id.btn_verify)
        editOTP = findViewById(R.id.edit_otp)
        tvTimer = findViewById(R.id.tv_otp_timer)
        tvResendOTP = findViewById(R.id.tv_resend_otp)
        tvOTPSentMessage = findViewById(R.id.tv_otp_hint)
        handlerTimer = Handler(Looper.getMainLooper())
    }

    /**
     *
     * Button Onclick Listener
     */
    private fun setListener() {
        editOTP.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    editOTP.letterSpacing = 0f
                } else {
                    editOTP.letterSpacing = 1f
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }
        })

        tvResendOTP.setOnClickListener {
            resendOTPRequest()
        }

        btnVerify.setOnClickListener {
            otpSendRequest()
        }
    }

    /**
     *
     * Verify OTP Api call with OTP validation
     */
    private fun otpSendRequest() {
        if (CommonUtils.otpValidation(
                editOTP.text.toString(),
                loginResponse?.otpLength ?: 0,
                this
            ) == "true"
        ) {
            val loginRequest = LoginRequest()
            loginRequest.email = emailID
            loginRequest.password = password

            val verifyOtpRequest = VerifyOtpRequest()
            verifyOtpRequest.email = emailID ?: ""
            verifyOtpRequest.otp = editOTP.text.toString()
            verifyOtpRequest.memberID = loginResponse?.userProfile?.memberId ?: ""
            viewModel.verifyOTP(verifyOtpRequest, rememberMe, loginRequest) { message ->
                showMessage(message)
            }
        } else {
            showMessage(
                CommonUtils.otpValidation(
                    editOTP.text.toString(),
                    loginResponse?.otpLength ?: 0,
                    this
                )
            )
        }
    }


    /**
     *
     * Resend OTP Api call
     */
    private fun resendOTPRequest() {
        editOTP.text?.clear()
        val resendOTPRequest = ResendOTPRequest()
        resendOTPRequest.email = emailID
        resendOTPRequest.memberId = loginResponse?.userProfile?.memberId ?: ""
        viewModel.resendOTP(
            resendOTPRequest,
        ) { message ->
            showMessage(message)
        }
    }

    /**
     *
     * Start OTP Timer
     */
    private fun startTimer() {
        tvResendOTP.alpha = 0.5f
        tvResendOTP.isClickable = false
        tvResendOTP.isEnabled = false
        runnableTimer = Runnable {
            validTime--
            tvTimer.text = CommonUtils.formatTime(validTime)
            if (validTime == 0L) {
                stopTimer()
            } else
                handlerTimer?.postDelayed(runnableTimer!!, 1000)
        }
        handlerTimer?.postDelayed(runnableTimer!!, 100)
    }

    /**
     *
     * Stop OTP Timer
     */
    private fun stopTimer() {
        tvResendOTP.alpha = 1f
        tvResendOTP.isClickable = true
        tvResendOTP.isEnabled = true
        if (runnableTimer != null) {
            handlerTimer?.removeCallbacks(runnableTimer!!)
            validTime = loginResponse?.otpExpiresIn ?: 0
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }

    /**
     *
     * Navigate to ChatBot Activity
     */
    private fun navigateChatBotActivity() {
        val flag = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        NavigationLauncher.launchActivityFlag(
            mActivity,
            ChatBotActivity::class.java, flag
        )
    }
}