package com.hns.acumen360.data.remote.stars.likedislike

import com.google.gson.annotations.SerializedName

data class LikeDisLikeRequest(

    @field:SerializedName("member_id")
    val memberId: String? = null,

    @field:SerializedName("like")
    val like: Int? = null,

    @field:SerializedName("dislike")
    val dislike: Int? = null,

    @field:SerializedName("message_id")
    val messageId: String? = null
)
