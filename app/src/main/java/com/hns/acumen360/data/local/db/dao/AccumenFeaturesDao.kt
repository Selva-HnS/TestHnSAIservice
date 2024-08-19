package com.hns.acumen360.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hns.acumen360.data.local.db.entity.AccumenFeaturesDB


/**
 * Created by Kamesh Kannan on 07-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
@Dao
interface AccumenFeaturesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAccumenFeatures(data: List<AccumenFeaturesDB>)

    @Query("DELETE FROM AccumenFeatures")
    fun deleteAllAccumenFeaturesRecords()

}