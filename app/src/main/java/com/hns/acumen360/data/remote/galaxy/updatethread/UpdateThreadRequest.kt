package com.hns.acumen360.data.remote.galaxy.updatethread

import com.google.gson.annotations.SerializedName

data class UpdateThreadRequest(

	@field:SerializedName("member_id")
	var memberId: String? = null,

	@field:SerializedName("thread_id")
	var threadId: String? = null,

	@field:SerializedName("heading")
	var heading: String? = null,

	@field:SerializedName("project_id")
	var projectId: String? = null
)
