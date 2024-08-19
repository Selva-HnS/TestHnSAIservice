package com.hns.acumen360.data.remote.stars.login

import com.google.gson.annotations.SerializedName
import com.hns.acumen360.utils.common.Constants

data class LoginRequest(

    @field:SerializedName("email")
    var email: String? = null,

    @field:SerializedName("password")
    var password: String? = null,

    @field:SerializedName("cosmic_type")
    val cosmicType: String? = null,

    @field:SerializedName("device_id")
    val deviceId: String? = null

   /* @field:SerializedName("project_id")
    val projectId: String? = null,

    ,*/


)
