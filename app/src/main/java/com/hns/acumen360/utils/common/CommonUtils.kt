package com.hns.acumen360.utils.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.hns.acumen360.R
import com.hns.acumen360.data.remote.galaxy.chathistory.MessageItem
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.abs


/**
 * Created by Kamesh Kannan on 20-April-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
object CommonUtils {

    fun getSimpleDateFormat(): SimpleDateFormat {
        val simpleDateFormat = SimpleDateFormat("dd MMM yyyy")
        return simpleDateFormat
    }

    fun getSimpleTimeFormat(): SimpleDateFormat {
        return SimpleDateFormat("hh:mm:ss.SSS")
    }

    fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        return currentDate
    }

    fun getCurrentDate(): String? {
        val calendar = Calendar.getInstance()
        return getSimpleDateFormat().format(calendar.time)
    }

    fun isValidImageURL(url: String): Boolean {
        return FormattingUtils.regexValidImage.find(url) != null
    }

    fun makeMeShake(view: View, duration: Int, offset: Int): View {
        val anim: Animation = TranslateAnimation(-offset.toFloat(), offset.toFloat(), 0f, 0f)
        anim.duration = duration.toLong()
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = 5
        view.startAnimation(anim)
        return view
    }

    fun setVibrate(mContext: Activity, duration: Long) {
        val v = mContext.getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v.vibrate(duration)
        }
    }

    @SuppressLint("HardwareIds")
    fun getAndroidID(context: Activity): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun playMp3(context: Context, mp3SoundByteArray: ByteArray): FileInputStream {
        var fis = FileInputStream("")
        try {
            val tempMp3 = File.createTempFile("Sample", "mp3", context.cacheDir)
            tempMp3.deleteOnExit()
            val fos = FileOutputStream(tempMp3)
            fos.write(mp3SoundByteArray)
            fos.close()
            fis = FileInputStream(tempMp3)
        } catch (ex: IOException) {
            val s = ex.toString()
            ex.printStackTrace()
        }

        return fis
    }

    fun playAudioFromByteArray(byteArray: ByteArray) {
        val bufferSize = AudioTrack.getMinBufferSize(
            44100,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        val audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            44100,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize,
            AudioTrack.MODE_STREAM
        )
        audioTrack.play()
        audioTrack.write(byteArray, 0, byteArray.size)
        audioTrack.stop()
        audioTrack.release()
    }

    fun extractUrls(text: String): List<String> {
        if (!text.isNullOrEmpty()) {
            val urlPattern = "(https?|ftp)://[^\\s/$.?#].[^\\s]*".toRegex()
            return urlPattern.findAll(text).map {
                it.value
            }.toList()
        } else {
            return mutableListOf()
        }
    }


    fun getYouTubeId(youTubeUrl: String): String? {
        val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern: Pattern = Pattern.compile(pattern)
        val matcher: Matcher = compiledPattern.matcher(youTubeUrl)
        return if (matcher.find()) {
            matcher.group()
        } else {
            "error"
        }
    }


    @Throws(IllegalArgumentException::class)
    fun smoothScroll(rv: RecyclerView, toPos: Int, duration: Int) {
        val TARGET_SEEK_SCROLL_DISTANCE_PX = 10000
        var itemHeight = rv.getChildAt(0).height
        itemHeight = itemHeight + 33
        val firstPos =
            (rv.layoutManager as LinearLayoutManager?)!!.findFirstCompletelyVisibleItemPosition()
        var i = abs((firstPos - toPos) * itemHeight)
        if (i == 0) {
            i = abs(rv.getChildAt(0).y).toInt()
        }
        val totalPix = i
        val smoothScroller: RecyclerView.SmoothScroller =
            object : LinearSmoothScroller(rv.context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }

                override fun calculateTimeForScrolling(dx: Int): Int {
                    var ms = (duration * dx / totalPix.toFloat()).toInt()
                    if (dx < TARGET_SEEK_SCROLL_DISTANCE_PX) {
                        ms = ms * 2
                    }
                    return ms
                }
            }
        smoothScroller.targetPosition = toPos
        rv.layoutManager!!.startSmoothScroll(smoothScroller)
    }

    fun removeLenticularBrackets(text: String): String {
        // Define the pattern to be removed
        val pattern = "【\\d+:\\d+†source】".toRegex()
        // Replace all instances of the pattern with an empty string
        return text.replace(pattern, "")
    }

    fun getScreenWidth(context: Activity): Int {
        val windowManager = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    fun getScreenHeight(context: Activity): Int {
        val windowManager = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

    fun convertMillisecondsToDate(milliseconds: Long, dateFormat: String): String {
        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        val date = Date(milliseconds)
        return sdf.format(date)
    }

    fun orderChatHistory(message: List<MessageItem?>?): List<MessageItem?>? {
        val dateFormatter =
            DateTimeFormatter.ofPattern(TimeFormats.FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSSSSS)
        val sortedPastEvents = message!!.sortedBy {
            LocalDate.parse(
                it!!.modified,
                dateFormatter
            )
        }.reversed()
        return sortedPastEvents
    }


    fun orderChatHistoryThreads(message: List<com.hns.acumen360.data.remote.stars.threads.MessageItem>?): List<com.hns.acumen360.data.remote.stars.threads.MessageItem> {
        val dateFormatter =
            DateTimeFormatter.ofPattern(TimeFormats.FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSSSSS)
        val sortedPastEvents = message!!.sortedBy {
            LocalDate.parse(
                it!!.modified,
                dateFormatter
            )
        }.reversed()

        return sortedPastEvents
    }

    fun isSameDay(date1: Date, date2: Date): Boolean {
        val sdf = SimpleDateFormat(TimeFormats.FORMAT_yyyyMMdd, Locale.getDefault())
        return sdf.format(date1) == sdf.format(date2)
    }


    fun chatGroupDate(agoDate: Int): Date {
        val date = Calendar.getInstance().apply {
            if (agoDate == 0) {
                set(Calendar.HOUR_OF_DAY, agoDate)
            } else {
                add(Calendar.DAY_OF_YEAR, -1)
                set(Calendar.HOUR_OF_DAY, 0)
            }
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        return date
    }

    fun formatTime(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        //return String.format("%02d:%02d:%02d", hours, minutes, secs)
        return String.format("%02d:%02d", minutes, secs)
    }

    fun rotateView(isLike: Boolean, view: AppCompatTextView) {
        var rotateDegree: Float?
        if (isLike) {
            rotateDegree = 180f
        } else {
            rotateDegree = -180f
        }
        val rotate = RotateAnimation(
            0f,
            rotateDegree,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 300
        rotate.interpolator = LinearInterpolator()
        view.startAnimation(rotate)

        rotate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                //textColor()
            }
        })
    }

    /**
     *
     * OTP Validations
     */

    fun otpValidation(data: String, otpLength: Int, context: Context): String {
        return if (data.isEmpty()) {
            context.resources.getString(R.string.please_enter_otp)
        } else if (data.length < otpLength) {
            context.resources.getString(R.string.please_enter_valid_otp)
        } else {
            "true"
        }
    }
}