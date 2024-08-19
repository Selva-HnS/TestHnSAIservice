package com.hns.acumen360.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hns.acumen360.data.local.db.entity.HistorysDB
import com.hns.acumen360.data.local.db.entity.UserDetailsDB


/**
 * Created by Kamesh Kannan on 07-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
@Dao
interface UserDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserDetails(data: UserDetailsDB): Long?

    @Query("SELECT * FROM UserDetails")
    fun loadUserDetails(): MutableList<UserDetailsDB>


    @Query("DELETE FROM UserDetails")
    fun deleteAllUser()

    @Query("UPDATE UserDetails SET remember=:isRemember ,  isLogin=:isLogIn where Email=:emailID")
    fun updateUserDetails(isRemember: Boolean, isLogIn: Boolean, emailID: String): Int?
}