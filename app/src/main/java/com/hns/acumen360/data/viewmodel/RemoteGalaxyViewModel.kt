package com.hns.acumen360.data.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.hns.acumen360.base.BaseViewModel
import com.hns.acumen360.base.remote.RemoteViewModelResponse
import com.hns.acumen360.base.ResponseType
import com.hns.acumen360.base.ViewModelResponse
import com.hns.acumen360.base.remote.RemoteResponseType
import com.hns.acumen360.base.remote.RemoteViewModelResponse.Companion.ERROR
import com.hns.acumen360.base.remote.RemoteViewModelResponse.Companion.FAILURE
import com.hns.acumen360.base.remote.RemoteViewModelResponse.Companion.SUCCESS
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
import com.hns.acumen360.data.respository.CommonRepository
import com.hns.acumen360.data.respository.RemoteGalaxyRepository
import com.hns.acumen360.utils.log.Logger
import com.hns.acumen360.utils.network.NetworkHelper
import com.hns.acumen360.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Kamesh Kannan on 07-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
class RemoteGalaxyViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    repository: CommonRepository,
    val remoteGalaxyRepository: RemoteGalaxyRepository,
    val commonViewModel: CommonViewModel

) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, repository) {
    override fun onCreate() {}

    val networks: NetworkHelper = networkHelper
    val viewResponse: MutableLiveData<ViewModelResponse> = MutableLiveData()
    val RemoteviewResponse: MutableLiveData<RemoteViewModelResponse> = MutableLiveData()

    var enqueueCall = object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful) {
                RemoteviewResponse.postValue(
                    RemoteViewModelResponse(
                        SUCCESS,
                        RemoteResponseType.RR_DATA_RESPONSE,
                        response
                    )
                )
            } else {
                RemoteviewResponse.postValue(
                    RemoteViewModelResponse(
                        FAILURE,
                        RemoteResponseType.RR_DATA_RESPONSE,
                        response
                    )
                )
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            RemoteviewResponse.postValue(
                RemoteViewModelResponse(
                    ERROR,
                    RemoteResponseType.RR_DATA_RESPONSE,
                    t
                )
            )
        }
    }

    fun getDataStream(
        request: RequestBodyModel,
        callback: (Call<ResponseBody>) -> Unit
    ) {
        if (checkInternetConnectionWithMessage()) {
            isTokenValid {
                Log.i("getDataStream", " status: $it")
                val responseBodyCall: Call<ResponseBody> =
                    remoteGalaxyRepository.getDataStream(request, getTokenInfo().accessToken ?: "")
                responseBodyCall.enqueue(enqueueCall)
                callback(responseBodyCall)
            }
        }
    }


    fun getServiceHistory(request: ServiceHistoryRequest, callback: (Call<ResponseBody>) -> Unit) {
        if (checkInternetConnectionWithMessage()) {
            val responseBodyCall: Call<ResponseBody> =
                remoteGalaxyRepository.getServiceHistory(request)
            responseBodyCall.enqueue(enqueueCall)
            callback(responseBodyCall)
        }
    }

    fun getLikeDisLike(request: LikeDisLikeRequest, callback: (Call<LikeDisLikeResponse>) -> Unit) {
        if (checkInternetConnectionWithMessage()) {
            val responseBodyCall: Call<LikeDisLikeResponse> =
                remoteGalaxyRepository.getLikeDisLike(request)
            responseBodyCall.enqueue(object : Callback<LikeDisLikeResponse> {
                override fun onResponse(
                    call: Call<LikeDisLikeResponse>,
                    response: Response<LikeDisLikeResponse>
                ) {
                    if (response.isSuccessful) {
                        RemoteviewResponse.postValue(
                            RemoteViewModelResponse(
                                SUCCESS,
                                RemoteResponseType.RR_LIKE_DISLIKE,
                                response
                            )
                        )
                    } else {
                        RemoteviewResponse.postValue(
                            RemoteViewModelResponse(
                                FAILURE,
                                RemoteResponseType.RR_LIKE_DISLIKE,
                                response
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<LikeDisLikeResponse>, t: Throwable) {
                    RemoteviewResponse.postValue(
                        RemoteViewModelResponse(
                            ERROR,
                            RemoteResponseType.RR_LIKE_DISLIKE,
                            t
                        )
                    )
                }
            })
            callback(responseBodyCall)
        }
    }

    fun getVisionChat(request: RequestImageModel?) {
        if (checkInternetConnectionWithMessage()) {
            val responseBodyCall: Call<ResponseBody> = remoteGalaxyRepository.getVisionChat(request)
            responseBodyCall.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>, response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        RemoteviewResponse.postValue(
                            RemoteViewModelResponse(
                                SUCCESS,
                                RemoteResponseType.RR_VISION,
                                response
                            )
                        )
                    } else {
                        RemoteviewResponse.postValue(
                            RemoteViewModelResponse(
                                SUCCESS,
                                RemoteResponseType.RR_VISION,
                                response
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    RemoteviewResponse.postValue(
                        RemoteViewModelResponse(
                            ERROR,
                            RemoteResponseType.RR_VISION,
                            t
                        )
                    )
                }
            })
        }
    }


    fun createThread(request: CreateThreadRequest) {
        if (checkInternetConnectionWithMessage()) {
            val responseBodyCall: Call<CreateThreadResponse> =
                remoteGalaxyRepository.createThread(request)
            responseBodyCall.enqueue(object : Callback<CreateThreadResponse> {
                override fun onResponse(
                    call: Call<CreateThreadResponse>,
                    response: Response<CreateThreadResponse>
                ) {
                    if (response.isSuccessful) {
                        RemoteviewResponse.postValue(
                            RemoteViewModelResponse(
                                SUCCESS,
                                RemoteResponseType.RR_CREATE_THREAD,
                                response
                            )
                        )
                    } else {
                        RemoteviewResponse.postValue(
                            RemoteViewModelResponse(
                                FAILURE,
                                RemoteResponseType.RR_CREATE_THREAD,
                                response
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<CreateThreadResponse>, t: Throwable) {
                    RemoteviewResponse.postValue(
                        RemoteViewModelResponse(
                            ERROR,
                            RemoteResponseType.RR_CREATE_THREAD,
                            t
                        )
                    )
                }
            })
        }
    }


    fun getThreads(request: CreateThreadRequest) {
        if (checkInternetConnectionWithMessage()) {
            val responseBodyCall: Call<ThreadsResponse> = remoteGalaxyRepository.getThreads(request)
            responseBodyCall.enqueue(object : Callback<ThreadsResponse> {
                override fun onResponse(
                    call: Call<ThreadsResponse>,
                    response: Response<ThreadsResponse>
                ) {
                    if (response.isSuccessful) {
                        RemoteviewResponse.postValue(
                            RemoteViewModelResponse(
                                SUCCESS,
                                RemoteResponseType.RR_GET_THREAD,
                                response
                            )
                        )
                    } else {
                        RemoteviewResponse.postValue(
                            RemoteViewModelResponse(
                                FAILURE,
                                RemoteResponseType.RR_GET_THREAD,
                                response
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<ThreadsResponse>, t: Throwable) {
                    RemoteviewResponse.postValue(
                        RemoteViewModelResponse(
                            ERROR,
                            RemoteResponseType.RR_GET_THREAD,
                            t
                        )
                    )
                }
            })
        }
    }


    fun updateThread(request: UpdateThreadRequest) {
        if (checkInternetConnectionWithMessage()) {
            val responseBodyCall: Call<UpdateThreadResponse> =
                remoteGalaxyRepository.updateThread(request)
            responseBodyCall.enqueue(object : Callback<UpdateThreadResponse> {
                override fun onResponse(
                    call: Call<UpdateThreadResponse>,
                    response: Response<UpdateThreadResponse>
                ) {
                    if (response.isSuccessful) {
                        RemoteviewResponse.postValue(
                            RemoteViewModelResponse(
                                SUCCESS,
                                RemoteResponseType.RR_UPDATE_THREAD,
                                response
                            )
                        )
                    } else {
                        RemoteviewResponse.postValue(
                            RemoteViewModelResponse(
                                FAILURE,
                                RemoteResponseType.RR_UPDATE_THREAD,
                                response
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<UpdateThreadResponse>, t: Throwable) {
                    RemoteviewResponse.postValue(
                        RemoteViewModelResponse(
                            ERROR,
                            RemoteResponseType.RR_UPDATE_THREAD,
                            t
                        )
                    )
                }
            })
        }
    }


    fun getChatHistory(request: ChatHistoryRequest) {
        if (checkInternetConnectionWithMessage()) {
            val responseBodyCall: Call<ChatHistoryResponse> =
                remoteGalaxyRepository.getChatHistory(request)
            responseBodyCall.enqueue(object : Callback<ChatHistoryResponse> {
                override fun onResponse(
                    call: Call<ChatHistoryResponse>,
                    response: Response<ChatHistoryResponse>
                ) {
                    if (response.isSuccessful) {
                        RemoteviewResponse.postValue(
                            RemoteViewModelResponse(
                                SUCCESS,
                                RemoteResponseType.RR_HISTORY_THREAD,
                                response
                            )
                        )
                    } else {
                        RemoteviewResponse.postValue(
                            RemoteViewModelResponse(
                                FAILURE,
                                RemoteResponseType.RR_HISTORY_THREAD,
                                response
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<ChatHistoryResponse>, t: Throwable) {
                    RemoteviewResponse.postValue(
                        RemoteViewModelResponse(
                            ERROR,
                            RemoteResponseType.RR_HISTORY_THREAD,
                            t
                        )
                    )
                }
            })
        }
    }

    fun getTTS(request: RequestTTSModel, callback: (ByteArray) -> Unit) {
        if (checkInternetConnectionWithMessage()) {
            remoteGalaxyRepository
                .getTTS(request)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>, response: Response<ResponseBody>
                    ) {
                        val thread = Thread {
                            try {
                                if (response.isSuccessful) {
                                    response.body()?.let { responseBody ->
                                        val byteArray = responseBody.bytes()
                                        callback(byteArray)
                                    }
                                }
                            } catch (e: Exception) {
                                Logger.i("TTS", e.message.toString())
                            }
                        }
                        thread.start()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Logger.i("TTS", t.message.toString())
                    }
                })
        }
    }

    fun getSTT(request: RequestSTTModel, callback: (String) -> Unit) {
        if (checkInternetConnectionWithMessage()) {
            showProgress()
            remoteGalaxyRepository
                .getSTT(request)
                .enqueue(object : Callback<ResponseBodyModel> {
                    override fun onResponse(
                        call: Call<ResponseBodyModel>,
                        response: Response<ResponseBodyModel>
                    ) {
                        response.body()?.let { resp ->
                            resp.let {
                                it.response?.let { it1 -> callback(it1) }
                            }
                        } ?: run {
                            Logger.i("STT", " nothing: ")
                        }
                        hideProgress()
                    }

                    override fun onFailure(call: Call<ResponseBodyModel>, t: Throwable) {
                        hideProgress()
                    }
                })
        }
    }
}