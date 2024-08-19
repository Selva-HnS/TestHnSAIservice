package com.hns.acumen360.data.remote.common.errorresponse

import com.google.gson.annotations.SerializedName

data class ErrorResponse(

	@field:SerializedName("detail")
	val detail: String? = null
)
