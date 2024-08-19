package com.hns.acumen360.listener

import androidx.appcompat.widget.AppCompatEditText
import com.hns.acumen360.data.remote.stars.threads.MessageItem


/**
 * Created by Kamesh Kannan on 21-April-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
interface GetListener {
    fun getMenuData(name: String, data: String, feature:String?)
    fun getServiceHistory(chasisNo: String, regNumber: String)
    fun getThreadMore(
        threadId: String,
        isEdit: Boolean,
        isDelete: Boolean,
        position: Int,
        childPosition: Int,
        dataItem: MessageItem,
        tvContent: AppCompatEditText
    )
}