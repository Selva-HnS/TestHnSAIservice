package com.hns.acumen360.data.respository

import com.hns.acumen360.data.local.db.AppDatabase
import com.hns.acumen360.data.local.db.entity.ProjectDetailsDB
import com.hns.acumen360.data.local.db.entity.SuggestedQuestionsDB
import com.hns.acumen360.data.local.db.entity.UserDetailsDB
import com.hns.acumen360.data.local.shared.AppPreferences
import com.hns.acumen360.data.remote.EndPoints
import com.hns.acumen360.data.remote.OrbitClient
import com.hns.acumen360.data.remote.stars.OrbitStarService
import com.hns.acumen360.data.remote.stars.login.LoginResponseDBCollections
import com.hns.acumen360.data.remote.stars.refreshtoken.RefreshTokenRequest
import com.hns.acumen360.data.remote.stars.refreshtoken.RefreshTokenResponse
import com.hns.acumen360.utils.rx.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Kamesh Kannan on 07-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
@Singleton
class CommonRepository
@Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    private val appDatabase: AppDatabase,
    private val appPreferences: AppPreferences
) {

    fun setTokenType(s: String) {
        appPreferences.setTokenTokenType(s)
    }

    fun getTokenType(): String {
        return appPreferences.getTokenType()
    }

    fun setAccessToken(token: String) {
        return appPreferences.setAccessToken(token)
    }

    fun getAccessToken(): String {
        return appPreferences.getToken()
    }

    fun clearToken(): Boolean {
        return appPreferences.clearToken()
    }


    fun setRefreshToken(token: String) {
        appPreferences.setRefreshToken(token)
    }

    fun getRefreshToken(): String {
        return appPreferences.getRefreshToken()
    }

    fun setTokenExpireTime(token: Long) {
        return appPreferences.setTokenExpireTime(token)
    }

    fun getTokenExpireTime(): Long {
        return appPreferences.getTokenExpireTime()
    }


    /**
     *
     * Insert project config data into database
     */
    fun insertUserDetails(loginResponseDBCollections: LoginResponseDBCollections?): Observable<Boolean> {
        return Observable.fromCallable {
            appDatabase.userDetailsDao()
                .insertUserDetails(loginResponseDBCollections?.userDetails!!)
            loginResponseDBCollections.projectDetailsListDB?.let {
                appDatabase.projectDetailsDao().insertProjectDetails(it)
            }
            loginResponseDBCollections.suggestedQuesDBList?.let {
                appDatabase.suggestedQuestionsDao().insertSuggestedQues(it)
            }
            loginResponseDBCollections.cosmicFeaturesDBList?.let {
                appDatabase.cosmicFeaturesDao().insertCosmicFeatures(it)
            }
            loginResponseDBCollections.accumenFeaturesDBList?.let {
                appDatabase.accumenFeaturesDao().insertAccumenFeatures(it)
            }
            true
        }
    }

    fun getUserDetailsDB(): Observable<MutableList<UserDetailsDB>> {
        return Observable.fromCallable {
            appDatabase.userDetailsDao().loadUserDetails()
        }
    }


    fun getProjectDetails(): Observable<MutableList<ProjectDetailsDB>> {
        return Observable.fromCallable {
            appDatabase.projectDetailsDao().loadUserDetails()
        }
    }

    fun getSuggestedQuestions(projectID: String): Observable<MutableList<SuggestedQuestionsDB>> {
        return Observable.fromCallable {
            appDatabase.suggestedQuestionsDao().loadSuggestedQues(projectID)
        }
    }

    fun deleteUsers(remember: Boolean?, emailId: String): Observable<Boolean> {
        return Observable.fromCallable {
            appDatabase.projectDetailsDao().deleteAllProjectDetails()
            appDatabase.suggestedQuestionsDao().deleteAllSuggestedRecords()
            appDatabase.accumenFeaturesDao().deleteAllAccumenFeaturesRecords()
            appDatabase.cosmicFeaturesDao().deleteCosmicFeaturesRecords()
            if (remember == true) {
                // don't delete
                appDatabase.userDetailsDao().updateUserDetails(remember, false, emailId)
            } else
                appDatabase.userDetailsDao().deleteAllUser()

            true
        }
    }

    private fun getOrbitStars(): OrbitStarService {
        return OrbitClient.clientStars?.create(OrbitStarService::class.java)!!
    }

    fun getRefreshTokenRequest(refreshTokenRequest: RefreshTokenRequest): Call<RefreshTokenResponse> {
        return getOrbitStars().refreshToken(EndPoints.SUBSCRIPTION_KEY, refreshTokenRequest)
    }
}