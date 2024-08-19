package com.hns.acumen360.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hns.acumen360.data.local.db.entity.ProjectDetailsDB
import com.hns.acumen360.data.local.db.entity.SuggestedQuestionsDB


/**
 * Created by Kamesh Kannan on 07-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
@Dao
interface SuggestedQuestionsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSuggestedQues(data: List<SuggestedQuestionsDB>)

    @Query("SELECT * FROM SuggestedQuestions where ProjectID=:projectID")
    fun loadSuggestedQues(projectID: String): MutableList<SuggestedQuestionsDB>

    @Query("DELETE FROM SuggestedQuestions")
    fun deleteAllSuggestedRecords()

}