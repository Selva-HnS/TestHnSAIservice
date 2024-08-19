package com.hns.acumen360.data.support

import com.hns.acumen360.ui.login.model.MenuAction
import com.hns.acumen360.utils.common.Constants


/**
 * Created by Kamesh Kannan on 23-April-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
class InstanceData {
    companion object {
        private var INSTANCE: InstanceData? = null


        @get:Synchronized
        val instance: InstanceData
            get() {
                if (INSTANCE == null) {
                    INSTANCE = InstanceData()
                }
                return INSTANCE as InstanceData
            }
    }

    var androidUniqueId: String = "0000"

    var helpMenuData: ArrayList<MenuAction> = arrayListOf(
        MenuAction(
            "TekAlert",
            "Showing top 5 TekAlerts",
            "Showing top 5 TekAlerts",
            Constants.TEK_ALERT
        ),
        MenuAction(
            "R&R Trainer",
            "Suggest top 5 for me!",
            "As an expert in Service of XUV 700 can you explain interactively step by step (one at a time) on a R&R? Suggest top 5 for me!",
            Constants.RR_TRAINER
        )
    )

    var translateData: ArrayList<MenuAction> = arrayListOf(
            MenuAction(
                "English",
                "Translating it into simple English",
                "Translate it into <English>",
                Constants.TRANSLATES
            ),
            MenuAction(
                "Tamil",
                "Translating it into simple Tamil",
                "Translate it into <Tamil>",
                Constants.TRANSLATES
            ),
            MenuAction(
                "Hindi",
                "Translating it into simple Hindi",
                "Translate it into <Hindi>",
                Constants.TRANSLATES
            ),
            MenuAction(
                "Telugu",
                "Translating it into simple Telugu",
                "Translate it into <Telugu>",
                Constants.TRANSLATES
            ),
            MenuAction(
                "Odia",
                "Translating it into simple Odia",
                "Translate it into <Odia>",
                Constants.TRANSLATES
            ),
            MenuAction(
                "Malayalam",
                "Translating it into simple Malayalam",
                "Translate it into <Malayalam>",
                Constants.TRANSLATES
            )
        )

    var moreData: ArrayList<MenuAction> = arrayListOf(
        MenuAction(
            "Similar Content",
            "Similar Content",
            "can you summaries similar related answer available in other manuals with the Manual Name as title and a short summary, other that answered?",
            Constants.SIMILAR_CONTENT
        ),
        MenuAction(
            "Explain Intuitively",
            "Explain Intuitively",
            "can you explain this intuitively in structured way?",
            Constants.EXPLAIN_INTUITIVELY
        ),
        MenuAction(
            "Suggest Question",
            "Suggest Question",
            "Please suggest five short questions based on the provided input documents, unrelated to this conversation.",
            Constants.SUGGEST_QUESTION
        ),
        MenuAction(
            "Related Topics",
            "Related Topics",
            "Suggest few related topics?",
            Constants.RELATED_TOPICS
        ),
        MenuAction(
            "Sales Talk",
            "Sales Talk",
            "Help me with a Sales Talk on this topic!",
            Constants.SALES_TALK
        )
    )

    var selectTypePosition: Int = 0
    var selectType: String = "XUV 700 Clutch"
    var listTypeData: ArrayList<MenuAction> = arrayListOf(
        MenuAction("3731dc641c", "M&M Service", "Acumen360:M&M Service", null),
        MenuAction("b5985d615f", "M&M XUV 700 Clutch", "Acumen360: XUV 700 Clutch", null),
        MenuAction("76dcb00c4d", "BW PS Rotary Die Cutter", "BWPS RDC", null),
    )


    var listSuggestData0: ArrayList<MenuAction> = arrayListOf(
        MenuAction(
            "b5985d615f",
            "What are the Technical Specifications of Engine, with logical grouping?",
            "What are the Technical Specifications of Engine, with logical grouping?", null
        ),
        MenuAction(
            "b5985d615f",
            "What's the procedure for diagnosing clutch issues in an XUV 700?",
            "What's the procedure for diagnosing clutch issues in an XUV 700?", null
        ),
        MenuAction(
            "b5985d615f",
            "Guide me through the XUV 700 routine maintenance SOP.",
            "Guide me through the XUV 700 routine maintenance SOP.", null
        ),
        MenuAction(
            "b5985d615f",
            "Explain how to R&R Clutch Master Cylinder in  XUV 700.",
            "Explain how to R&R Clutch Master Cylinder in  XUV 700.", null
        ),
    )

    var listSuggestData1: ArrayList<MenuAction> = arrayListOf(
        MenuAction(
            "76dcb00c4d",
            "Let me know Machine Specifications of Rotary Die Cutter",
            "Let me know Machine Specifications of Rotary Die Cutter", null
        ),
        MenuAction(
            "76dcb00c4d",
            "what are the SAFETY standards followed?",
            "what are the SAFETY standards followed?", null
        ),
        MenuAction(
            "76dcb00c4d",
            "What are the responsibilities of an operator for the RDC machine?",
            "What are the responsibilities of an operator for the RDC machine?", null
        ),
        MenuAction(
            "76dcb00c4d",
            "How can I avoid hazards while using the sheeter machine?",
            "How can I avoid hazards while using the sheeter machine?", null
        ),
        MenuAction(
            "76dcb00c4d",
            "Anvil Cover Rotation, Trimming, Installation and Removal for Machines Without SHARK",
            "Anvil Cover Rotation, Trimming, Installation and Removal for Machines Without SHARK",
            null
        ),
    )

    var listSuggestData2: ArrayList<MenuAction> = arrayListOf(
        MenuAction(
            "3731dc641c",
            "Explain the Seat Operations in XUV 700",
            "Explain the Seat Operations in XUV 700", null
        ),
        MenuAction(
            "3731dc641c",
            "Engine options available for XUV 700?",
            "Engine options available for XUV 700?", null
        ),
        MenuAction("3731dc641c", "Explain about Hill Assist?", "Explain about Hill Assist?", null),
        MenuAction(
            "3731dc641c",
            "Can you explain the infotainment system in XUV 700?",
            "Can you explain the infotainment system in XUV 700?", null
        ),
        MenuAction(
            "3731dc641c",
            "How to check the Engine Oil?",
            "How to check the Engine Oil?",
            null
        ),
    )


    var listSuggestData3: ArrayList<MenuAction> = arrayListOf(
        MenuAction(
            "3731dc641c",
            "Maintenance Check Sheet for 10K - Diesel MT",
            "Maintenance Check Sheet for 10K - Diesel MT", null
        ),
        MenuAction(
            "3731dc641c",
            "How to Check the Engine Oil Level?",
            "How to Check the Engine Oil Level?", null
        ),
        MenuAction(
            "3731dc641c",
            "How to perform Air Filter Element Removal Procedure?",
            "How to perform Air Filter Element Removal Procedure?", null
        ),
        MenuAction(
            "3731dc641c",
            "Explain DTC P062700",
            "Explain DTC P062700", null
        ),
        MenuAction(
            "3731dc641c",
            "Explain about Wheel Balancing",
            "Explain about Wheel Balancing", null
        ),
        MenuAction(
            "3731dc641c",
            "Explain Clutch Cover and Disc Installation procedure",
            "Explain Clutch Cover and Disc Installation procedure", null
        ),
        MenuAction(
            "3731dc641c",
            "Brief ADAS Brake Assist feature",
            "Brief ADAS Brake Assist feature", null
        ),
        MenuAction(
            "3731dc641c",
            "Give details about Pentalink Suspension with FDD Tech",
            "Give details about Pentalink Suspension with FDD Tech", null
        ),
    )

}