package com.hns.acumen360.data.remote.stars.refreshtoken

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("refresh_token")
	val refreshToken: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("expires_in")
	val expiresIn: Long? = null
)
