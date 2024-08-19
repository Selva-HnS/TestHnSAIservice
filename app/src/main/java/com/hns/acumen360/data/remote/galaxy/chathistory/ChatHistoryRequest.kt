package com.hns.acumen360.data.remote.galaxy.chathistory

import com.google.gson.annotations.SerializedName

data class ChatHistoryRequest(

    @field:SerializedName("member_id")
    val memberID: String? = null,

    @field:SerializedName("thread_id")
    val threadId: String? = null,

    @field:SerializedName("project_id")
    val projectId: String? = null
)
