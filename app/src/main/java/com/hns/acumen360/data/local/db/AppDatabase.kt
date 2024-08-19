package com.hns.acumen360.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hns.acumen360.data.local.db.dao.AccumenFeaturesDao
import com.hns.acumen360.data.local.db.dao.CosmicFeaturesDao
import com.hns.acumen360.data.local.db.dao.HistoryDao
import com.hns.acumen360.data.local.db.dao.ProjectDetailsDao
import com.hns.acumen360.data.local.db.dao.SuggestedQuestionsDao
import com.hns.acumen360.data.local.db.dao.UserDetailsDao
import com.hns.acumen360.data.local.db.entity.AccumenFeaturesDB
import com.hns.acumen360.data.local.db.entity.CosmicFeaturesDB
import com.hns.acumen360.data.local.db.entity.HistorysDB
import com.hns.acumen360.data.local.db.entity.ProjectDetailsDB
import com.hns.acumen360.data.local.db.entity.SuggestedQuestionsDB
import com.hns.acumen360.data.local.db.entity.UserDetailsDB
import javax.inject.Singleton


/**
 * Created by Kamesh Kannan on 07-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
@Singleton
@Database(
    entities = [
        HistorysDB::class,
        UserDetailsDB::class,
        ProjectDetailsDB::class,
        SuggestedQuestionsDB::class,
        CosmicFeaturesDB::class,
        AccumenFeaturesDB::class,
    ],
    exportSchema = false,
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun userDetailsDao(): UserDetailsDao
    abstract fun projectDetailsDao(): ProjectDetailsDao
    abstract fun suggestedQuestionsDao(): SuggestedQuestionsDao
    abstract fun cosmicFeaturesDao(): CosmicFeaturesDao
    abstract fun accumenFeaturesDao(): AccumenFeaturesDao
}