package com.hns.acumen360.data.remote.common.request

import com.google.gson.annotations.SerializedName

class RequestTTSModel(
    @field:SerializedName("input") var input: String,
    @field:SerializedName("voice") var voice: String,
    @field:SerializedName("member_id") var memberID: String,
    @field:SerializedName("project_id") var projectID: String,

)