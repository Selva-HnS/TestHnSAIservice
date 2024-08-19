package com.hns.acumen360.data.remote.galaxy

import com.hns.acumen360.data.remote.EndPoints
import com.hns.acumen360.data.remote.common.request.RequestBodyModel
import com.hns.acumen360.data.remote.common.request.RequestImageModel
import com.hns.acumen360.data.remote.common.request.RequestSTTModel
import com.hns.acumen360.data.remote.common.request.RequestTTSModel
import com.hns.acumen360.data.remote.common.response.ResponseBodyModel
import com.hns.acumen360.data.remote.galaxy.chathistory.ChatHistoryRequest
import com.hns.acumen360.data.remote.galaxy.chathistory.ChatHistoryResponse
import com.hns.acumen360.data.remote.galaxy.createthread.CreateThreadRequest
import com.hns.acumen360.data.remote.galaxy.createthread.CreateThreadResponse
import com.hns.acumen360.data.remote.galaxy.updatethread.UpdateThreadRequest
import com.hns.acumen360.data.remote.galaxy.updatethread.UpdateThreadResponse
import com.hns.acumen360.data.remote.stars.likedislike.LikeDisLikeRequest
import com.hns.acumen360.data.remote.stars.likedislike.LikeDisLikeResponse
import com.hns.acumen360.data.remote.stars.servicehistory.ServiceHistoryRequest
import com.hns.acumen360.data.remote.stars.threads.ThreadsResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Streaming
import javax.inject.Singleton

@Singleton
interface OrbitGalaxyService {

    @POST(EndPoints.TEXT_GENERATE)
    @Streaming
    fun getDataStream(
        @Header(EndPoints.SUBSCRIPTION_HEADER_KEY) subscriptionKey: String,
        @Header(EndPoints.AUTHORIZATION_HEADER_KEY) accessToken: String,
        @Body requestBody: RequestBodyModel?
    ): Call<ResponseBody>

    @POST("vision-chat")
    fun getVisionChat(@Body requestBody: RequestImageModel?): Call<ResponseBody>

    @POST("tts")
    @Streaming
    fun getTTS(@Body requestBody: RequestTTSModel?): Call<ResponseBody>

    @POST("stt")
    @Streaming
    fun getSTT(@Body requestBody: RequestSTTModel?): Call<ResponseBodyModel>

    @POST("create-thread")
    fun createThread(@Body requestBody: CreateThreadRequest?): Call<CreateThreadResponse>

    @POST("threads")
    fun getThreads(@Body requestBody: CreateThreadRequest?): Call<ThreadsResponse>

    @PUT("threads")
    fun updateThread(@Body requestBody: UpdateThreadRequest?): Call<UpdateThreadResponse>

    @POST("chat-history")
    fun getChatHistory(@Body requestBody: ChatHistoryRequest?): Call<ChatHistoryResponse>

    @POST("service-history")
    @Streaming
    fun serviceHistory(@Body requestBody: ServiceHistoryRequest?): Call<ResponseBody>

    @POST("message/like-dislike")
    fun likeDisLike(@Body requestBody: LikeDisLikeRequest?): Call<LikeDisLikeResponse>
}