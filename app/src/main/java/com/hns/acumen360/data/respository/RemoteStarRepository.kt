package com.hns.acumen360.data.respository

import com.hns.acumen360.data.remote.EndPoints
import com.hns.acumen360.data.remote.OrbitClient
import com.hns.acumen360.data.remote.stars.OrbitStarService
import com.hns.acumen360.data.remote.stars.login.LoginRequest
import com.hns.acumen360.data.remote.stars.login.LoginResponse
import com.hns.acumen360.data.remote.stars.refreshtoken.RefreshTokenRequest
import com.hns.acumen360.data.remote.stars.refreshtoken.RefreshTokenResponse
import com.hns.acumen360.data.remote.stars.resend.ResendOTPRequest
import com.hns.acumen360.data.remote.stars.verifyotp.VerifyOtpRequest
import com.hns.acumen360.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Kamesh Kannan on 07-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
@Singleton
class RemoteStarRepository
@Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    private val apiService: OrbitStarService
) {

    private fun getOrbitStars(): OrbitStarService {
        return OrbitClient.clientStars?.create(OrbitStarService::class.java)!!
    }

    fun getLoginAuthentication(request: LoginRequest): Call<LoginResponse> {
        return getOrbitStars().getLogin(EndPoints.SUBSCRIPTION_KEY, request)
    }

    fun getResendOTP(request: ResendOTPRequest): Call<Any> {
        return getOrbitStars().resendOTP(EndPoints.SUBSCRIPTION_KEY, request)
    }

    fun getVerifyOTPRequest(verifyOtpRequest: VerifyOtpRequest): Call<LoginResponse> {
        return getOrbitStars().verifyOTP(EndPoints.SUBSCRIPTION_KEY, verifyOtpRequest)
    }

    fun getRefreshTokenRequest(refreshTokenRequest: RefreshTokenRequest): Call<RefreshTokenResponse> {
        return getOrbitStars().refreshToken(EndPoints.SUBSCRIPTION_KEY, refreshTokenRequest)
    }


}