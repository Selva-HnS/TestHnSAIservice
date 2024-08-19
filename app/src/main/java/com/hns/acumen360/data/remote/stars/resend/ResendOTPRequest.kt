package com.hns.acumen360.data.remote.stars.resend

import com.google.gson.annotations.SerializedName

data class ResendOTPRequest(

	@field:SerializedName("member_id")
	var memberId: String? = null,

	@field:SerializedName("email")
    var email: String? = null
)
