package com.hns.acumen360.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hns.acumen360.data.local.db.entity.HistorysDB
import com.hns.acumen360.data.local.db.entity.ProjectDetailsDB
import com.hns.acumen360.data.local.db.entity.UserDetailsDB


/**
 * Created by Kamesh Kannan on 07-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
@Dao
interface ProjectDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProjectDetails(data: List<ProjectDetailsDB>)

    @Query("SELECT * FROM ProjectDetails")
    fun loadUserDetails(): MutableList<ProjectDetailsDB>

    @Query("DELETE FROM ProjectDetails")
    fun deleteAllProjectDetails()


}