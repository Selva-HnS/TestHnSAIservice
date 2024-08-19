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
@Entity(
    tableName = "History",
    indices = [Index(
        value = ["Email", "ProjectID", "UniqueID", "Prompt", "ResponseData"],
        unique = true
    )]
)
class HistorysDB {
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
    var uniqueID: String? = null

    @Expose
    @ColumnInfo(name = "DateTime")
    @SerializedName("DateTime")
    var dateTime: String? = null

    @Expose
    @ColumnInfo(name = "Prompt")
    @SerializedName("Prompt")
    var prompt: String = ""

    @Expose
    @ColumnInfo(name = "ResponseData")
    @SerializedName("ResponseData")
    var responseData: String = ""
}