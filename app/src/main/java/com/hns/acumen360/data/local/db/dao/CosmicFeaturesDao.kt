package com.hns.acumen360.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hns.acumen360.data.local.db.entity.CosmicFeaturesDB


/**
 * Created by Kamesh Kannan on 07-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
@Dao
interface CosmicFeaturesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCosmicFeatures(data: List<CosmicFeaturesDB>)

    @Query("DELETE FROM CosmicFeatures")
    fun deleteCosmicFeaturesRecords()

}