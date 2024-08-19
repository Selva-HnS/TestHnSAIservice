package com.hns.acumen360.data.remote.stars.servicehistory

import com.google.gson.annotations.SerializedName

data class ServiceHistoryRequest(

	@field:SerializedName("registration_number")
	val registrationNumber: String? = null,

	@field:SerializedName("assistant")
	val assistant: String? = null,

	@field:SerializedName("chassis_number")
	val chassisNumber: String? = null,

	@field:SerializedName("prompt")
	val prompt: String? = null
)
