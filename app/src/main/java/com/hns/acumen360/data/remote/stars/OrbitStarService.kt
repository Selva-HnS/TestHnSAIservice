package com.hns.acumen360.data.remote.stars

import com.hns.acumen360.data.remote.EndPoints
import com.hns.acumen360.data.remote.stars.login.LoginRequest
import com.hns.acumen360.data.remote.stars.login.LoginResponse
import com.hns.acumen360.data.remote.stars.refreshtoken.RefreshTokenRequest
import com.hns.acumen360.data.remote.stars.refreshtoken.RefreshTokenResponse
import com.hns.acumen360.data.remote.stars.resend.ResendOTPRequest
import com.hns.acumen360.data.remote.stars.verifyotp.VerifyOtpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface OrbitStarService {

    @POST(EndPoints.LOGIN)
    fun getLogin(
        @Header(EndPoints.SUBSCRIPTION_HEADER_KEY) subscriptionKey: String,
        @Body requestBody: LoginRequest?
    ): Call<LoginResponse>

    @POST(EndPoints.VERIFY_OTP)
    fun verifyOTP(
        @Header(EndPoints.SUBSCRIPTION_HEADER_KEY) subscriptionKey: String,
        @Body requestBody: VerifyOtpRequest?
    ): Call<LoginResponse>

    @POST(EndPoints.RESEND_OTP)
    fun resendOTP(
        @Header(EndPoints.SUBSCRIPTION_HEADER_KEY) subscriptionKey: String,
        @Body requestBody: ResendOTPRequest?
    ): Call<Any>

    @POST(EndPoints.REFRESH_TOKEN)
    fun refreshToken(
        @Header(EndPoints.SUBSCRIPTION_HEADER_KEY) subscriptionKey: String,
        @Body requestBody: RefreshTokenRequest?
    ): Call<RefreshTokenResponse>


}