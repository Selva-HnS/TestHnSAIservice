package com.hns.acumen360.data.remote.stars.verifyotp

import com.google.gson.annotations.SerializedName

data class VerifyOtpRequest(

    @field:SerializedName("otp")
    var otp: String? = null,

    @field:SerializedName("email")
    var email: String? = null,

    @field:SerializedName("member_id")
    var memberID: String? = null
)
