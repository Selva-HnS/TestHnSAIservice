package com.hns.acumen360.data.remote.stars.login

import com.hns.acumen360.data.local.db.entity.AccumenFeaturesDB
import com.hns.acumen360.data.local.db.entity.CosmicFeaturesDB
import com.hns.acumen360.data.local.db.entity.ProjectDetailsDB
import com.hns.acumen360.data.local.db.entity.SuggestedQuestionsDB
import com.hns.acumen360.data.local.db.entity.UserDetailsDB

class LoginResponseDBCollections {

    var userDetails: UserDetailsDB? = null

    var projectDetailsListDB: MutableList<ProjectDetailsDB>? = null

    var suggestedQuesDBList: MutableList<SuggestedQuestionsDB>? = null

    var cosmicFeaturesDBList: MutableList<CosmicFeaturesDB>? = null

    var accumenFeaturesDBList: MutableList<AccumenFeaturesDB>? = null

}