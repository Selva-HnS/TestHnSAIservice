package com.hns.acumen360.data.remote.common.request

import com.google.gson.annotations.SerializedName

class RequestBodyModel(
    @field:SerializedName("prompt") var prompt: String,
    @field:SerializedName("a360_features") var a360Features: String? = null,
    @field:SerializedName("project_id") var projectId: String,
    @field:SerializedName("thread_id") var threadID: String,
    @field:SerializedName("member_id") var memberID: String
)
