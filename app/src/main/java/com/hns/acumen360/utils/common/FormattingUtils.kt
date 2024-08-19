package com.hns.acumen360.utils.common

import com.hns.acumen360.data.support.ResponseDataFormat
import com.hns.acumen360.utils.commonmark.ext.gfm.tables.TablesExtension
import com.hns.acumen360.utils.commonmark.node.Node
import com.hns.acumen360.utils.commonmark.parser.Parser
import com.hns.acumen360.utils.commonmark.renderer.html.HtmlRenderer
import com.hns.acumen360.utils.log.Logger


/**
 * Created by Kamesh Kannan on 08-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
object FormattingUtils {
    val regexValidImage = Regex("\\.(jpg|jpeg|png|gif|bmp|svg)\$", RegexOption.IGNORE_CASE)

    fun convertMarkDown(inputData: String): ResponseDataFormat {
        var response = ResponseDataFormat("", "", null)
        Logger.e("RespTest", inputData)
        try {
            var inputDatas = """ $inputData """
            val extensions = listOf(TablesExtension.create())
            val parser: Parser = Parser.builder().extensions(extensions).build()
            val document: Node = parser.parse(inputDatas)
            val renderer: HtmlRenderer = HtmlRenderer.builder().extensions(extensions).build()
            val htmlContent: String = renderer.render(document)
            response.copyData = htmlContent
            response.htmlData = htmlContent
        } catch (e: StringIndexOutOfBoundsException) {
            Logger.e("RespTest", "String index out of bounds", e)
        } catch (e: Exception) {
            Logger.e("RespTest", "An error occurred", e)
        }
        return response
    }

    fun String.isValidMarkdownLink(): Boolean {
        val regex = Regex("""\[[A-Za-z0-9 ]+\]\((.*?)\)""")
        return regex.containsMatchIn(this)
    }

}