package com.hns.acumen360.data.remote.stars.threads

import com.google.gson.annotations.SerializedName

data class ThreadsResponse(

    @field:SerializedName("message")
    val message: List<MessageItem>? = null
)

data class MessageItem(

    @field:SerializedName("member_id")
    val memberId: String? = null,

    @field:SerializedName("thread_id")
    val threadId: String? = null,

    @field:SerializedName("project_id")
    val projectId: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("modified")
    val modified: String? = null,

    @field:SerializedName("creation")
    val creation: String? = null,

    @field:SerializedName("heading")
    var heading: String? = null
) {
    var isEdit: Boolean = false
}
