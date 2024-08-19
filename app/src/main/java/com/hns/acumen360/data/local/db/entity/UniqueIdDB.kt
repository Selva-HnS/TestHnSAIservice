package com.hns.acumen360.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by Kamesh Kannan on 07-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
@Entity(tableName = "UniqueID", indices = [Index(value = ["Email","ProjectID","UniqueID"], unique = true)])
class UniqueIdDB {
    @Expose
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "indexId")
    var indexId = 0

    @Expose
    @ColumnInfo(name = "Email")
    @SerializedName("Email")
    var email: String? = null

    @Expose
    @ColumnInfo(name = "ProjectID")
    @SerializedName("ProjectID")
    var projectID: String? = null

    @Expose
    @ColumnInfo(name = "UniqueID")
    @SerializedName("UniqueID")
    var uniqueID: Boolean = false

    @Expose
    @ColumnInfo(name = "DateTime")
    @SerializedName("DateTime")
    var dateTime: String? = null

    @Expose
    @ColumnInfo(name = "IsActive")
    @SerializedName("IsActive")
    var isActive: Boolean = false
}