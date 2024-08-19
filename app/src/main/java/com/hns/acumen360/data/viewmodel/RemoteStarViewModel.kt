package com.hns.acumen360.data.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hns.acumen360.base.BaseViewModel
import com.hns.acumen360.base.ResponseType
import com.hns.acumen360.base.ViewModelResponse
import com.hns.acumen360.base.ViewModelResponse.Companion.SUCCESS
import com.hns.acumen360.data.local.db.entity.AccumenFeaturesDB
import com.hns.acumen360.data.local.db.entity.CosmicFeaturesDB
import com.hns.acumen360.data.local.db.entity.ProjectDetailsDB
import com.hns.acumen360.data.local.db.entity.SuggestedQuestionsDB
import com.hns.acumen360.data.local.db.entity.UserDetailsDB
import com.hns.acumen360.data.remote.stars.login.LoginRequest
import com.hns.acumen360.data.remote.stars.login.LoginResponse
import com.hns.acumen360.data.remote.stars.login.LoginResponseDBCollections
import com.hns.acumen360.data.remote.stars.login.TokenInfo
import com.hns.acumen360.data.remote.stars.refreshtoken.RefreshTokenRequest
import com.hns.acumen360.data.remote.stars.refreshtoken.RefreshTokenResponse
import com.hns.acumen360.data.remote.stars.resend.ResendOTPRequest
import com.hns.acumen360.data.remote.stars.verifyotp.VerifyOtpRequest
import com.hns.acumen360.data.respository.CommonRepository
import com.hns.acumen360.data.respository.RemoteStarRepository
import com.hns.acumen360.utils.log.Logger
import com.hns.acumen360.utils.network.NetworkHelper
import com.hns.acumen360.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Kamesh Kannan on 07-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
class RemoteStarViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    repository: CommonRepository,
    private val remoteStarRepository: RemoteStarRepository,
    val commonViewModel: CommonViewModel

) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, repository) {
    override fun onCreate() {}

    val networks: NetworkHelper = networkHelper
    val viewResponse: MutableLiveData<ViewModelResponse> = MutableLiveData()


    /**
     *
     * Login Api Call
     */
    fun loginAuthentication(
        request: LoginRequest,
        rememberMe: Boolean,
        callback: (String) -> Unit
    ) {
        if (checkInternetConnectionWithMessage()) {
            showProgress()
            remoteStarRepository
                .getLoginAuthentication(request)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.code() == 200 && response.isSuccessful)
                            response.body()?.let { resp ->
                                resp.let {
                                    if (resp.isUserValid == true) {
                                        if (resp.isOtpSent == true) {
                                            /**
                                             *
                                             * Navigate to Verify OTP Page. If came from Login Activity
                                             */
                                            hideProgress()
                                            callback(it.message ?: "")
                                            commonViewModel.loginUsers.postValue(
                                                ViewModelResponse(
                                                    SUCCESS,
                                                    ResponseType.NAVIGATE_VERIFY_OTP,
                                                    it
                                                )
                                            )
                                        } else if (!resp.projectConfiguration.isNullOrEmpty()) {
                                            callback(it.message ?: "")
                                            loginResponseDataInsertionIntoDB(
                                                it,
                                                rememberMe,
                                                request
                                            )
                                        } else
                                            callback(resp.message ?: "")
                                    } else {
                                        callback(resp.message ?: "")
                                    }
                                }
                            }
                        else {
                            hideProgress()
                            val errorMsg = response.errorBody()?.string()
                            callback(errorMsg ?: "")
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        callback(t.message ?: "")
                        hideProgress()
                    }
                })
        }
    }


    /**
     *
     * Resend OTP
     */
    fun resendOTP(
        request: ResendOTPRequest,
        callback: (String) -> Unit
    ) {
        if (checkInternetConnectionWithMessage()) {
            showProgress()
            remoteStarRepository
                .getResendOTP(request)
                .enqueue(object : Callback<Any> {
                    override fun onResponse(
                        call: Call<Any>,
                        response: Response<Any>
                    ) {

                    }

                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        callback(t.message ?: "")
                        hideProgress()
                    }
                })
        }
    }


    /**
     *
     * Manipulate the Login API Response
     */
    fun loginResponseDataInsertionIntoDB(
        resp: LoginResponse,
        rememberMe: Boolean,
        request: LoginRequest
    ) {
        val loginResponseDBCollections =
            LoginResponseDBCollections()

        setToken(resp.tokenInfo)
        val userDetails = UserDetailsDB()
        userDetails.email = request.email
        userDetails.password = request.password
        userDetails.memberID = resp.userProfile?.memberId ?: ""
        userDetails.firstName = resp.userProfile?.firstName ?: ""
        userDetails.profilePhoto = resp.userProfile?.profilePhoto ?: ""
        userDetails.loginTime = System.currentTimeMillis()
        userDetails.remember = rememberMe
        userDetails.isLogin = true
        loginResponseDBCollections.userDetails = userDetails


        val projectDetailsListDB: MutableList<ProjectDetailsDB> =
            mutableListOf()
        val suggestedQuesDBList: MutableList<SuggestedQuestionsDB> =
            mutableListOf()

        val cosmicFeaturesDBList: MutableList<CosmicFeaturesDB> =
            mutableListOf()

        val accumenFeaturesDBList: MutableList<AccumenFeaturesDB> =
            mutableListOf()

        if (!resp.projectConfiguration.isNullOrEmpty()) {
            for (item in resp.projectConfiguration) {

                /* Project details*/
                val projectDetailsDB = ProjectDetailsDB()
                projectDetailsDB.email = request.email
                projectDetailsDB.memberID =
                    resp.userProfile?.memberId ?: ""
                projectDetailsDB.projectID = item?.projectId ?: ""
                projectDetailsDB.profileID = item?.profileId ?: ""
                projectDetailsDB.productID = item?.productId ?: ""
                projectDetailsDB.projectName = item?.name ?: ""
                projectDetailsDB.projectImage = item?.projectImage ?: ""
                projectDetailsDB.assistantName =
                    item?.assistantName ?: ""
                projectDetailsListDB.add(projectDetailsDB)
                loginResponseDBCollections.projectDetailsListDB =
                    projectDetailsListDB

                if (!item?.suggestedQuestions.isNullOrEmpty()) {
                    for (itemSuggestedQues in item?.suggestedQuestions!!) {
                        /* Suggested Questions*/
                        val suggestedQuestionsDB =
                            SuggestedQuestionsDB()
                        suggestedQuestionsDB.email = request.email
                        suggestedQuestionsDB.memberID =
                            resp.userProfile?.memberId ?: ""
                        suggestedQuestionsDB.projectID =
                            item.projectId ?: ""
                        suggestedQuestionsDB.title =
                            itemSuggestedQues?.title ?: ""
                        suggestedQuesDBList.add(suggestedQuestionsDB)
                        loginResponseDBCollections.suggestedQuesDBList =
                            suggestedQuesDBList
                    }
                }

                if (item?.cosmicFeatures != null) {
                    /* Cosmic Features*/
                    val cosmicFeatures = item.cosmicFeatures
                    val cosmicFeaturesDB =
                        CosmicFeaturesDB()
                    cosmicFeaturesDB.email = request.email
                    cosmicFeaturesDB.memberID =
                        resp.userProfile?.memberId ?: ""
                    cosmicFeaturesDB.projectID =
                        item.projectId ?: ""
                    cosmicFeaturesDB.mobileCopyText =
                        cosmicFeatures.mobileCopyText
                    cosmicFeaturesDB.mobileReadLoud =
                        cosmicFeatures.mobileReadLoud
                    cosmicFeaturesDB.mobileVisualizer =
                        cosmicFeatures.mobileVisualizer
                    cosmicFeaturesDB.mobileVision =
                        cosmicFeatures.mobileVision
                    cosmicFeaturesDB.mobileTranslate =
                        cosmicFeatures.mobileTranslate
                    cosmicFeaturesDB.mobileAugmentReality =
                        cosmicFeatures.mobileAugmentedReality
                    cosmicFeaturesDBList.add(cosmicFeaturesDB)
                    loginResponseDBCollections.cosmicFeaturesDBList =
                        cosmicFeaturesDBList

                }

                if (item?.acumenFeatures != null) {
                    /* Accumen Features*/
                    val acumenFeatures = item.acumenFeatures
                    if (acumenFeatures.isNotEmpty()) {
                        for (acumenFeaturesItem in acumenFeatures) {
                            val accumenFeaturesDB =
                                AccumenFeaturesDB()
                            accumenFeaturesDB.email = request.email
                            accumenFeaturesDB.memberID =
                                resp.userProfile?.memberId ?: ""
                            accumenFeaturesDB.projectID =
                                acumenFeaturesItem?.projectId ?: ""
                            accumenFeaturesDB.title =
                                acumenFeaturesItem?.title ?: ""
                            accumenFeaturesDB.iconPath =
                                acumenFeaturesItem?.iconPath ?: ""
                            accumenFeaturesDB.order =
                                acumenFeaturesItem?.order ?: 0
                            accumenFeaturesDB.status =
                                acumenFeaturesItem?.status ?: false
                            accumenFeaturesDBList.add(accumenFeaturesDB)
                        }
                    }
                    loginResponseDBCollections.accumenFeaturesDBList =
                        accumenFeaturesDBList
                }


            }
        }
        commonViewModel.insertUserDetails(loginResponseDBCollections)
    }


    /**
     *
     * Verify OTP Api calls
     */
    fun verifyOTP(
        verifyOtpRequest: VerifyOtpRequest,
        rememberMe: Boolean,
        loginRequest: LoginRequest,
        callback: (String) -> Unit
    ) {
        if (checkInternetConnectionWithMessage()) {
            showProgress()
            remoteStarRepository
                .getVerifyOTPRequest(verifyOtpRequest)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.code() == 200 && response.isSuccessful)
                            response.body()?.let { resp ->
                                hideProgress()
                                callback(resp.message ?: "")
                                if (resp.isUserValid == true)
                                    loginResponseDataInsertionIntoDB(resp, rememberMe, loginRequest)
                            }
                        else {
                            hideProgress()
                            val errorMsg = response.errorBody()?.string()
                            callback(errorMsg!!)
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        hideProgress()
                        callback(t.message ?: "")
                    }
                })
        }
    }


}