package com.hns.acumen360.data.remote.galaxy.createthread

import com.google.gson.annotations.SerializedName

data class CreateThreadRequest(

	@field:SerializedName("member_id")
	val memberId: String? = null,

	@field:SerializedName("project_id")
	val projectId: String? = null
)
