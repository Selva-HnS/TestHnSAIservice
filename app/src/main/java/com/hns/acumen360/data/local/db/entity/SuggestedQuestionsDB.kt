package com.hns.acumen360.data.local.db.entity

import androidx.annotation.NonNull
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
@Entity(
    tableName = "SuggestedQuestions",
    indices = [Index(value = ["Email", "ProjectID", "MemberID","title"], unique = true)]
)
class SuggestedQuestionsDB {
    @Expose
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "indexId")
    var indexId = 0


    @Expose
    @ColumnInfo(name = "Email")
    var email: String? = null

    @Expose
    @ColumnInfo(name = "MemberID")
    var memberID: String? = null

    @Expose
    @ColumnInfo(name = "UniqueID")
    @SerializedName("UniqueID")
    var uniqueID: String? = InstanceData.instance.androidUniqueId

    @Expose
    @ColumnInfo(name = "ProjectID")
    var projectID: String? = null

    @Expose
    @ColumnInfo(name = "title")
    var title: String? = null


}