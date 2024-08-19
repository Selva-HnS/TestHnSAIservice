package com.hns.acumen360.ui.chatbot.model

import android.graphics.Bitmap
import android.webkit.WebView


/**
 * Created by Kamesh Kannan on 20-April-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
class ChatBotData(
    var questions: String,
    var actualResponse: String,
    var data: String,
    var copy: String,
    var bitmap: Bitmap?,
    var stateButton: Boolean,
    var stateLoading: Boolean,
) {

    var htmlStream: String = ""
    var audioState: Boolean = false
    var thumbsUpState: Boolean = false
    var thumbsDownState: Boolean = false
    var audioProgress: Boolean = false
    var messageId: String = ""

}