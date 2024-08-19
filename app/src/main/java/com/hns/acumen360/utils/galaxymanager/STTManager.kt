package com.hns.acumen360.utils.galaxymanager

import android.app.Dialog
import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.view.View
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.lottie.LottieAnimationView
import com.google.gson.Gson
import com.hns.acumen360.R
import com.hns.acumen360.data.remote.common.request.RequestSTTModel
import com.hns.acumen360.utils.common.CommonUtils
import com.hns.acumen360.utils.log.Logger
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.RuntimeException


/**
 * Created by Kamesh Kannan on 28-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
class STTManager(private val context: Context) {
    private var mediaRecorder: MediaRecorder? = null
    private var runnableTimer: Runnable? = null
    private var counter = 11L

    private val outputFile: String = context.filesDir.absolutePath + "/recording.mp4"
    lateinit var tvDialogCounter: AppCompatTextView
    var dialog: Dialog? = null
    private var handlerTimer: Handler? = null

    init {
        handlerTimer = Handler(Looper.getMainLooper())
    }


    fun showMicAnimation(): Dialog {
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_mic_layout)
        val lottieAudioRecording: LottieAnimationView =
            dialog.findViewById(R.id.lottie_audio_recording)
        lottieAudioRecording.playAnimation()
        tvDialogCounter = dialog.findViewById(R.id.tv_dialog_counter)
        dialog.show()
        return dialog
    }

    fun startTimer(callback: (String) -> Unit) {
        runnableTimer = Runnable {
            counter--
            tvDialogCounter?.text = CommonUtils.formatTime(counter)
            if (counter == 0L) {
                getBase64()?.let { callback(it) }
                stopTimer()
                dialog?.dismiss()
            } else
                handlerTimer?.postDelayed(runnableTimer!!, 1000)
        }
        handlerTimer?.postDelayed(runnableTimer!!, 100)
    }

    fun stopTimer() {
        if (runnableTimer != null) {
            handlerTimer?.removeCallbacks(runnableTimer!!)
            counter = 11
        }
    }

    fun startRecording(): MediaRecorder? {
        if (mediaRecorder == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                mediaRecorder = MediaRecorder(context).apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                    setAudioEncodingBitRate(16)
                    setAudioChannels(1)
                    setAudioSamplingRate(8000)
                    setOutputFile(outputFile)
                    try {
                        prepare()
                        start()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                mediaRecorder = MediaRecorder().apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                    setAudioEncodingBitRate(16)
                    setAudioChannels(1)
                    setAudioSamplingRate(8000)
                    setOutputFile(outputFile)
                    try {
                        prepare()
                        start()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return mediaRecorder
    }

    fun stopRecording(): MediaRecorder? {
        if (mediaRecorder != null) {
            try {
                mediaRecorder?.apply {
                    stop()
                    release()
                }
            } catch (e: IllegalStateException) {
                Logger.e("Exception",e.message.toString())
            } catch (e: RuntimeException) {
                Logger.e("Exception",e.message.toString())
            }
            mediaRecorder = null
        }
        return mediaRecorder
    }

    fun getBase64(): String? {
        val file = File(outputFile)
        var base64String: String? = null
        try {
            val inputStream = FileInputStream(file)
            val bytes = ByteArray(file.length().toInt())
            inputStream.read(bytes)
            base64String = Base64.encodeToString(bytes, Base64.DEFAULT)
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return base64String
    }

    fun getRecordedFilePath(): String {
        return outputFile
    }
}