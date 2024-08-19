package com.hns.acumen360.data.remote.common.request

import com.google.gson.annotations.SerializedName

class RequestImageModel(
    @field:SerializedName("image_base64") var imageBase64: String,
    @field:SerializedName("member_id") var memberID: String,
    @field:SerializedName("project_id") var projectID: String,
    @field:SerializedName("prompt") var prompt: String,
    @field:SerializedName("thread_id") var threadID: String,
)
