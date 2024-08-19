package com.hns.acumen360.data.support

import android.graphics.Bitmap


/**
 * Created by Kamesh Kannan on 21-April-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
class ResponseDataFormat(
    var htmlData: String,
    var copyData:String,
    var bitmap: Bitmap?) {

    var speechData:String =""
    var actualResponse:String =""
    var htmlStream:String =""
    var moreActionState:Boolean =false

    fun addItems(data: ResponseDataFormat): ResponseDataFormat {
        htmlData+=data.htmlData
        copyData+=data.copyData
        return this
    }

}