package com.hns.acumen360.data.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hns.acumen360.base.BaseViewModel
import com.hns.acumen360.base.ViewModelResponse
import com.hns.acumen360.base.ViewModelResponse.Companion.SUCCESS
import com.hns.acumen360.base.ViewModelResponse.Companion.THROWABLE
import com.hns.acumen360.data.local.db.entity.ProjectDetailsDB
import com.hns.acumen360.data.respository.CommonRepository
import com.hns.acumen360.utils.network.NetworkHelper
import com.hns.acumen360.utils.rx.SchedulerProvider
import com.hns.acumen360.base.ResponseType
import com.hns.acumen360.data.remote.stars.login.LoginResponseDBCollections
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by Kamesh Kannan on 07-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
class CommonViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    repository: CommonRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, repository) {

    override fun onCreate() {}

    val networks: NetworkHelper = networkHelper
    val viewResponse: MutableLiveData<ViewModelResponse> = MutableLiveData()
    val loginUsers: MutableLiveData<ViewModelResponse> = MutableLiveData()
    val streamingResponse: MutableLiveData<String> = MutableLiveData()

    /**
     *
     * Fetch user information from the data base
     */

    fun loginUserSelectActive() {
        compositeDisposable.add(
            repository.getUserDetailsDB()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    {
                        loginUsers.postValue(
                            ViewModelResponse(
                                SUCCESS,
                                ResponseType.SELECT_LOGIN_USER,
                                it
                            )
                        )
                    }, {
                        loginUsers.postValue(
                            ViewModelResponse(
                                THROWABLE,
                                ResponseType.SELECT_LOGIN_USER,
                                it.message.toString()
                            )
                        )
                    }
                )
        )
    }


    /**
     *
     * Insert login response data  into data base
     */
    fun insertUserDetails(loginResponseDBCollections: LoginResponseDBCollections?) {
        compositeDisposable.add(
            repository.insertUserDetails(loginResponseDBCollections)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    {
                        loginUsers.postValue(
                            ViewModelResponse(
                                SUCCESS,
                                ResponseType.INSERT_USER_DETAILS,
                                it
                            )
                        )
                    }, {
                        loginUsers.postValue(
                            ViewModelResponse(
                                THROWABLE,
                                ResponseType.INSERT_USER_DETAILS,
                                it.message.toString()
                            )
                        )
                    }
                )
        )
    }


    /**
     *
     * Get project information from the data base
     */

    fun getProjectDetails() {
        compositeDisposable.add(
            repository.getProjectDetails()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    {
                        loginUsers.postValue(
                            ViewModelResponse(
                                SUCCESS,
                                ResponseType.PROJECT_DETAILS,
                                it
                            )
                        )
                    }, {
                        loginUsers.postValue(
                            ViewModelResponse(
                                THROWABLE,
                                ResponseType.PROJECT_DETAILS,
                                it.message.toString()
                            )
                        )
                    }
                )
        )
    }

    /**
     *
     * Delete login user details
     */
    fun deleteUsers(remember: Boolean?, emailId: String) {
        compositeDisposable.add(
            repository.deleteUsers(remember, emailId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    {
                        loginUsers.postValue(
                            ViewModelResponse(
                                SUCCESS,
                                ResponseType.DELETE_USERS,
                                it
                            )
                        )
                    }, {
                        loginUsers.postValue(
                            ViewModelResponse(
                                THROWABLE,
                                ResponseType.DELETE_USERS,
                                it.message.toString()
                            )
                        )
                    }
                )
        )
    }

    fun getSuggestedQuestionsDB(projectID: String?) {
        compositeDisposable.add(
            repository.getSuggestedQuestions(projectID!!)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    {
                        if (!it.isNullOrEmpty())
                            loginUsers.postValue(
                                ViewModelResponse(
                                    SUCCESS,
                                    ResponseType.SUGGESTED_QUESTIONS,
                                    it
                                )
                            )
                    }, {

                    }
                )
        )
    }
}