package com.hns.acumen360.data.respository

import com.hns.acumen360.data.remote.EndPoints
import com.hns.acumen360.data.remote.OrbitClient
import com.hns.acumen360.data.remote.common.request.RequestBodyModel
import com.hns.acumen360.data.remote.common.request.RequestImageModel
import com.hns.acumen360.data.remote.common.request.RequestSTTModel
import com.hns.acumen360.data.remote.common.request.RequestTTSModel
import com.hns.acumen360.data.remote.common.response.ResponseBodyModel
import com.hns.acumen360.data.remote.galaxy.OrbitGalaxyService
import com.hns.acumen360.data.remote.galaxy.chathistory.ChatHistoryRequest
import com.hns.acumen360.data.remote.galaxy.chathistory.ChatHistoryResponse
import com.hns.acumen360.data.remote.galaxy.createthread.CreateThreadRequest
import com.hns.acumen360.data.remote.galaxy.createthread.CreateThreadResponse
import com.hns.acumen360.data.remote.galaxy.updatethread.UpdateThreadRequest
import com.hns.acumen360.data.remote.galaxy.updatethread.UpdateThreadResponse
import com.hns.acumen360.data.remote.stars.OrbitStarService
import com.hns.acumen360.data.remote.stars.likedislike.LikeDisLikeRequest
import com.hns.acumen360.data.remote.stars.likedislike.LikeDisLikeResponse
import com.hns.acumen360.data.remote.stars.servicehistory.ServiceHistoryRequest
import com.hns.acumen360.data.remote.stars.threads.ThreadsResponse
import com.hns.acumen360.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Kamesh Kannan on 07-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
@Singleton
class RemoteGalaxyRepository
@Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    private val apiService: OrbitStarService
) {

    private fun getOrbitGalaxy(): OrbitGalaxyService {
        return OrbitClient.clientGalaxy?.create(OrbitGalaxyService::class.java)!!
    }

    fun getTTS(request: RequestTTSModel): Call<ResponseBody> {
        return getOrbitGalaxy().getTTS(request)
    }

    fun getSTT(request: RequestSTTModel): Call<ResponseBodyModel> {
        return getOrbitGalaxy().getSTT(request)
    }

    fun getDataStream(request: RequestBodyModel?, accessToken: String): Call<ResponseBody> {
        return getOrbitGalaxy().getDataStream(
            EndPoints.SUBSCRIPTION_KEY,
            EndPoints.JWT_TOKEN_KEY + accessToken,
            request
        )
    }

    fun getServiceHistory(request: ServiceHistoryRequest?): Call<ResponseBody> {
        return getOrbitGalaxy().serviceHistory(request)
    }

    fun getLikeDisLike(request: LikeDisLikeRequest?): Call<LikeDisLikeResponse> {
        return getOrbitGalaxy().likeDisLike(request)
    }

    fun getVisionChat(request: RequestImageModel?): Call<ResponseBody> {
        return getOrbitGalaxy().getVisionChat(request)
    }

    fun createThread(request: CreateThreadRequest?): Call<CreateThreadResponse> {
        return getOrbitGalaxy().createThread(request)
    }

    fun getThreads(request: CreateThreadRequest?): Call<ThreadsResponse> {
        return getOrbitGalaxy().getThreads(request)
    }

    fun updateThread(request: UpdateThreadRequest?): Call<UpdateThreadResponse> {
        return getOrbitGalaxy().updateThread(request)
    }

    fun getChatHistory(request: ChatHistoryRequest?): Call<ChatHistoryResponse> {
        return getOrbitGalaxy().getChatHistory(request)
    }


}