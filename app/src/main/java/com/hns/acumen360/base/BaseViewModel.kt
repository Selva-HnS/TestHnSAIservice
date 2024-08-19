package com.hns.acumen360.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.hns.acumen360.R
import com.hns.acumen360.data.remote.common.errorresponse.ErrorResponse
import com.hns.acumen360.data.remote.stars.login.TokenInfo
import com.hns.acumen360.data.remote.stars.refreshtoken.RefreshTokenRequest
import com.hns.acumen360.data.remote.stars.refreshtoken.RefreshTokenResponse
import com.hns.acumen360.data.respository.CommonRepository
import com.hns.acumen360.utils.common.Resource
import com.hns.acumen360.utils.network.NetworkHelper
import com.hns.acumen360.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection


/**
 * Created by Kamesh Kannan on 07-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
abstract class BaseViewModel(
    protected val schedulerProvider: SchedulerProvider,
    protected val compositeDisposable: CompositeDisposable,
    protected val networkHelper: NetworkHelper,
    protected val repository: CommonRepository
) : ViewModel() {
    var response: MutableLiveData<ViewModelResponse> = MutableLiveData()
    val messageStringId: MutableLiveData<Resource<Int>> = MutableLiveData()
    val messageString: MutableLiveData<Resource<String>> = MutableLiveData()
    val showProgress: MutableLiveData<Boolean> = MutableLiveData()
    abstract fun onCreate()
    fun checkInternetConnectionWithMessage(): Boolean =
        if (networkHelper.isNetworkConnected()) {
            true
        } else {
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
            false
        }

    fun showProgress() {
        showProgress.postValue(true)
    }

    fun hideProgress() {
        showProgress.postValue(false)
    }

    fun parseError(json: String): ErrorResponse {
        return Gson().fromJson(json, ErrorResponse::class.java)
    }

    protected fun checkInternetConnection(): Boolean = networkHelper.isNetworkConnected()

    protected fun handleNetworkError(err: Throwable?) =
        err?.let {
            networkHelper.castToNetworkError(it).run {
                when (status) {
                    -1 -> messageStringId.postValue(Resource.error(R.string.network_default_error))
                    0 -> messageStringId.postValue(Resource.error(R.string.server_connection_error))
                    HttpsURLConnection.HTTP_UNAUTHORIZED -> {
                        messageStringId.postValue(Resource.error(R.string.server_connection_error))
                    }

                    HttpsURLConnection.HTTP_INTERNAL_ERROR ->
                        messageStringId.postValue(Resource.error(R.string.network_internal_error))

                    HttpsURLConnection.HTTP_UNAVAILABLE ->
                        messageStringId.postValue(Resource.error(R.string.network_server_not_available))

                    else -> messageString.postValue(Resource.error(message))
                }
            }
        }


    /**
     *
     * Check token is active or not
     */
    fun isTokenValid(callback: (Boolean) -> Unit) {
        return if ((getTokenInfo().expiresIn ?: 0) > System.currentTimeMillis()) {
            callback(true)
        } else {
            refreshTokenApi(callback)
        }
    }

    /**
     *
     * Fetch token information from shared preference
     */
    fun getTokenInfo(): TokenInfo {
        return TokenInfo(
            repository.getAccessToken(),
            repository.getRefreshToken(),
            repository.getTokenType(),
            repository.getTokenExpireTime()
        )
    }

    /**
     *
     * Refresh token API call
     */
    private fun refreshTokenApi(callback: (Boolean) -> Unit) {
        val refreshTokenRequest = RefreshTokenRequest()
        refreshTokenRequest.accessToken = getTokenInfo().accessToken ?: ""
        refreshTokenRequest.refreshToken = getTokenInfo().refreshToken ?: ""
        if (checkInternetConnectionWithMessage()) {
            showProgress()
            repository
                .getRefreshTokenRequest(refreshTokenRequest)
                .enqueue(object : Callback<RefreshTokenResponse> {
                    override fun onResponse(
                        call: Call<RefreshTokenResponse>,
                        response: Response<RefreshTokenResponse>
                    ) {
                        hideProgress()
                        if (response.code() == 200 && response.isSuccessful) {
                            response.body()?.let { resp ->
                                val tokenInfo = TokenInfo(
                                    resp.accessToken ?: "",
                                    resp.refreshToken ?: "",
                                    "",
                                    resp.expiresIn ?: 0
                                )
                                setToken(tokenInfo)
                                callback(true)
                            } ?: run {
                                callback(false)
                            }
                        } else
                            callback(false)
                    }

                    override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
                        hideProgress()
                        callback(false)
                    }
                })
        } else
            callback(false)
    }


    /**
     *
     * Store token information stored into the shared preference
     */
    fun setToken(tokenInfo: TokenInfo?) {
        repository.setTokenType(tokenInfo?.tokenType ?: "")
        repository.setAccessToken(tokenInfo?.accessToken ?: "")
        repository.setRefreshToken(tokenInfo?.refreshToken ?: "")
        repository.setTokenExpireTime(System.currentTimeMillis() + tokenInfo?.expiresIn!!)

    }
}


