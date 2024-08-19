package com.hns.acumen360.data.remote.galaxy.chathistory

import com.google.gson.annotations.SerializedName

data class ChatHistoryResponse(

    @field:SerializedName("message")
    val message: List<MessageItem?>? = null
)

data class MessageItem(

    @field:SerializedName("member_id")
    val memberId: String? = null,

    @field:SerializedName("thread_id")
    val threadId: String? = null,

    @field:SerializedName("project_id")
    val projectId: String? = null,

    @field:SerializedName("response")
    val response: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("modified")
    val modified: String? = null,

    @field:SerializedName("response_time")
    val responseTime: String? = null,

    @field:SerializedName("message_id")
    val messageId: String? = null,

    @field:SerializedName("message_type")
    val messageType: Any? = null,

    @field:SerializedName("prompt")
    val prompt: String? = null,

    @field:SerializedName("creation")
    val creation: String? = null,

    @field:SerializedName("like")
    val like: Boolean = false,

    @field:SerializedName("dislike")
    val dislike: Boolean = false
)
