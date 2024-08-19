package com.hns.acumen360.data.local.shared

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Kamesh Kannan on 07-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
@Singleton
class AppPreferences @Inject constructor(private val prefs: SharedPreferences) {

    companion object {
        const val KEY_TOKEN_TYPE = "PREF_TOKEN_TYPE"
        const val KEY_ACCESS_TOKEN = "PREF_ACCESS_TOKEN"
        const val KEY_REFRESH_TOKEN = "PREF_REFRESH_TOKEN"
        const val KEY_EXPIRE_TIME = "PREF_TOKEN_EXPIRE_TIME"
    }

    fun getToken(): String = prefs.getString(KEY_ACCESS_TOKEN, "").toString()

    fun setAccessToken(data: String) {
        prefs.edit().putString(KEY_ACCESS_TOKEN, data).apply()
    }

    fun clearToken(): Boolean {
        return prefs.edit().remove(KEY_ACCESS_TOKEN).commit()
    }

    fun setRefreshToken(data: String) {
        prefs.edit().putString(KEY_REFRESH_TOKEN, data).apply()
    }

    fun getRefreshToken(): String = prefs.getString(KEY_REFRESH_TOKEN, "").toString()

    fun setTokenExpireTime(data: Long) {
        prefs.edit().putLong(KEY_EXPIRE_TIME, data).apply()
    }

    fun getTokenExpireTime(): Long = prefs.getLong(KEY_EXPIRE_TIME, 0)

    fun setTokenTokenType(data: String) {
        prefs.edit().putString(KEY_TOKEN_TYPE, data).apply()
    }

    fun getTokenType(): String =prefs.getString(KEY_TOKEN_TYPE, "").toString()

}