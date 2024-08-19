package com.hns.acumen360.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.hns.acumen360.data.support.InstanceData


/**
 * Created by Kamesh Kannan on 07-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
@Entity(tableName = "UserDetails", indices = [Index(value = ["Email"], unique = true)])
class UserDetailsDB {
    @Expose
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "indexId")
    var indexId = 0

    @Expose
    @ColumnInfo(name = "Email")
    var email: String? = null

    @Expose
    @ColumnInfo(name = "password")
    var password: String? = null

    @Expose
    @ColumnInfo(name = "UniqueID")
    @SerializedName("UniqueID")
    var uniqueID: String? = InstanceData.instance.androidUniqueId

    @Expose
    @ColumnInfo(name = "MemberID")
    var memberID: String? = null

    @Expose
    @ColumnInfo(name = "FirstName")
    var firstName: String? = ""

    @Expose
    @ColumnInfo(name = "ProfilePhoto")
    var profilePhoto: String? = null

    @Expose
    @ColumnInfo(name = "loginTime")
    var loginTime: Long? = 0

    @Expose
    @ColumnInfo(name = "remember")
    var remember: Boolean = false

    @Expose
    @ColumnInfo(name = "isLogin")
    var isLogin: Boolean = false
}