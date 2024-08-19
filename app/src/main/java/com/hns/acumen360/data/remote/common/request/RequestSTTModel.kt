package com.hns.acumen360.data.remote.common.request

import com.google.gson.annotations.SerializedName

class RequestSTTModel(
    @field:SerializedName("project_id") var projectID: String,
    @field:SerializedName("member_id") var memberID: String,
    @field:SerializedName("audio_file_base64") var audioFileBase64: String,

)