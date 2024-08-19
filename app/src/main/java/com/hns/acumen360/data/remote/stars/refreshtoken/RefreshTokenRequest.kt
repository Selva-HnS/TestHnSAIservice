package com.hns.acumen360.data.remote.stars.refreshtoken

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(

	@field:SerializedName("access_token")
	var accessToken: String? = null,

	@field:SerializedName("refresh_token")
    var refreshToken: String? = null
)
