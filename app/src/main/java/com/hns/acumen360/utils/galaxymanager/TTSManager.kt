package com.hns.acumen360.utils.galaxymanager

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.hns.acumen360.data.support.SpeechData
import com.hns.acumen360.ui.chatbot.model.ChatBotData
import com.hns.acumen360.utils.common.CommonUtils


/**
 * Created by Kamesh Kannan on 26-April-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
class TTSManager(private val context: Context, listener: TTSListener) {
    var handler: Handler? = null
    var mediaPlayer: MediaPlayer
    var speechData = mutableListOf<SpeechData>()
    var listener: TTSListener
    private var speechDataState: Boolean = false

    init {
        this.listener = listener
        mediaPlayer = MediaPlayer()
        handler = Handler(Looper.getMainLooper())
    }


    fun setTTSBegin(data: String) {
        speechData.clear()
        var dataIndex = data.split("\n")
        var speakAdd = StringBuffer()
        for ((index, inData) in dataIndex.withIndex()) {
            if (speakAdd.length <= 150) {
                if (inData.isNotEmpty()) {
                    var results = CommonUtils.extractUrls(inData)
                    if (!results.isNullOrEmpty()) {
                    } else {
                        speakAdd.append(inData).append("\n")
                    }
                } else speakAdd.append("\n")
            } else {
                if (inData.isNotEmpty()) {
                    var results = CommonUtils.extractUrls(inData)
                    if (!results.isNullOrEmpty()) {

                    } else {
                        speechData.add(SpeechData(speakAdd.toString()))
                        speakAdd.delete(0, speakAdd.length)
                        speakAdd.append(inData).append("\n")
                    }
                }
            }
        }
        if (speakAdd.toString().isNotEmpty()) {
            speechData.add(SpeechData(speakAdd.toString()))
            speakAdd.delete(0, speakAdd.length)
        }
    }


     fun playNextAudio(
        state: Boolean,
        myData: ChatBotData?,
        position: Int) {
        if (state) {
            if (speechData.isEmpty()) {
                listener.resetAllAudio()
                return
            }

            val speechDataTop = speechData.removeAt(0)
            val audioData = speechDataTop.audioFiles
            audioData?.let {
                mediaPlayer.release()
                mediaPlayer = MediaPlayer().apply {
                    val tempAudioFile = createTempFile()
                    tempAudioFile.writeBytes(it)
                    val dataSource = tempAudioFile.absolutePath
                    setDataSource(dataSource)
                    prepareAsync()
                    setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
                        override fun onCompletion(mp: MediaPlayer?) {
                            if (speechData.isEmpty()) {
                                myData?.audioState = false
                                myData?.audioProgress = false
                                listener.notifyItemChanged(position, myData)
                                speechDataState = true
                            } else {
                                speechDataState = false
                                playNextAudio(true, myData, position)
                            }
                        }
                    })
                    setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                        override fun onPrepared(mp: MediaPlayer?) {
                            if ((myData?.audioProgress == true || myData?.audioState!!) && position >= 0) {
                                myData.audioState = true
                                myData.audioProgress = false
                                listener.notifyItemChanged(position, myData)
                                mp?.start()
                                speechDataState = true
                                startUpdatingProgress(true, myData, position)
                            } else {
                                speechDataState = false
                            }
                        }
                    })
                }
            } ?: run {
                speechDataState = false
                playNextAudio(true, myData, position)
            }
        } else {
            mediaPlayer.stop()
            listener.resetAllAudioProgress()
        }
    }

    fun startUpdatingProgress(
        state: Boolean,
        myData: ChatBotData?,
        position: Int) {
        if (state) {
            handler?.postDelayed(object : Runnable {
                override fun run() {
                    try {
                        if (mediaPlayer != null && mediaPlayer.isPlaying) {
                            val currentPosition = mediaPlayer.currentPosition
                            val totalDuration = mediaPlayer.duration
                            var percentage = 0.0
                            if (totalDuration > 0) {
                                percentage = currentPosition.toDouble() / totalDuration * 100
                            }

                            if (percentage > 50 && mediaPlayer.isPlaying && speechDataState) {
                                listener.recallAPI(myData!!, position)
                            }
                        }
                    } catch (e: Exception) {

                    }

                    handler?.postDelayed(this, 1000)
                }
            }, 1000)
        } else {
            handler?.removeCallbacksAndMessages(null)
        }
    }

    interface TTSListener {
        fun recallAPI(myData: ChatBotData?, position: Int)
        fun resetAllAudio()
        fun resetAllAudioProgress()
        fun notifyItemChanged(position: Int, myData: ChatBotData?)
    }


}