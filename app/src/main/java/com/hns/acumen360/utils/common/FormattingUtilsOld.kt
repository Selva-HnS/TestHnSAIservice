package com.hns.acumen360.utils.common

import com.hns.acumen360.data.support.ResponseDataFormat
import com.hns.acumen360.utils.common.CommonUtils.removeLenticularBrackets


/**
 * Created by Kamesh Kannan on 08-May-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
object FormattingUtilsOld {
    val nextLine = "\n"
    val regexOrderList = Regex("\\d+\\.\\s")
    val regexUnOrderList = Regex("\\-\\s")
    val regexTableList = Regex("\\|\\s")
    val regexTableEmpty = Regex("\\|--")
    val regexURL = Regex("(https?://\\S+)")
    val regexValidImage = Regex("\\.(jpg|jpeg|png|gif|bmp|svg)\$", RegexOption.IGNORE_CASE)

    fun convert2Chat(inputData: String): ResponseDataFormat {

        var response: ResponseDataFormat = ResponseDataFormat("", "", null)
        val finalSomeString: StringBuffer = StringBuffer()
        val finalSomeCopyString: StringBuffer = StringBuffer()

        var data = inputData.split("\n")
        var orderListStatus = false
        var unOrderListStatus = false
        var tableRowStatus = false
        var tableHeaderStatus = false
        var tableDataStatus = false

        //Logger.i("RespTest", "Actual Value - " + inputData)
        for ((index, inData) in data.withIndex()) {
            //Logger.i("RespTest", "Actual SubValue - " + inData)

            if (inData.equals("```")) {
                continue
            } else if (inData.isNotEmpty() && inData.trim().length > 4) {

                if (regexURL.containsMatchIn(inData)) {
                    finalSomeString.append("</small>")
                    var responseDummy: ResponseDataFormat = setImages(inData)
                    finalSomeCopyString.append(responseDummy.copyData).append("\n")
                    finalSomeString.append(responseDummy.htmlData)

                } else if (regexUnOrderList.containsMatchIn(inData.trim().substring(0, 3))) {
                    var responseDummy: ResponseDataFormat =
                        setUnOrderList(inData, unOrderListStatus)
                    finalSomeCopyString.append(responseDummy.copyData).append("\n")
                    finalSomeString.append(convertToHTML(responseDummy.htmlData))
                    unOrderListStatus = true
                } else if (regexOrderList.containsMatchIn(
                        inData.trim().substring(0, if (inData.length > 3) 4 else 3)
                    )
                ) {
                    var responseDummy: ResponseDataFormat = setOrderList(inData, orderListStatus)
                    if (unOrderListStatus) {
                        finalSomeCopyString.append(responseDummy.copyData).append("\n")
                        finalSomeString.append("</ul><br>" + convertToHTML(responseDummy.htmlData))
                        unOrderListStatus = false
                    } else {
                        finalSomeCopyString.append(responseDummy.copyData).append("\n")
                        finalSomeString.append(convertToHTML(responseDummy.htmlData))
                    }
                    orderListStatus = true

                } else if (regexTableEmpty.containsMatchIn(inData.substring(0, 4))) {
                    if (tableRowStatus) {
                        finalSomeString.append("</tr>")
                        tableRowStatus = false
                    }
                    continue
                } else if (regexTableList.containsMatchIn(inData.substring(0, 4))) {
                    var responseDummy: ResponseDataFormat
                    if (tableHeaderStatus == true) {
                        tableDataStatus = true
                        responseDummy = setTableFormatData(inData, tableRowStatus)
                    } else {
                        tableHeaderStatus = true
                        responseDummy = setTableFormatHeader(inData, tableRowStatus)
                    }
                    tableRowStatus = true
                    finalSomeCopyString.append(responseDummy.copyData).append("\n")
                    finalSomeString.append(convertToHTML(responseDummy.htmlData))
                } else if (inData.contains("####")) {
                    var responseDummy: ResponseDataFormat = setH4(inData)
                    finalSomeCopyString.append(responseDummy.copyData).append("\n")
                    if (orderListStatus) {
                        responseDummy.htmlData = "</ol>" + responseDummy.htmlData
                    }
                    if (unOrderListStatus) {
                        responseDummy.htmlData = "</ul>" + responseDummy.htmlData
                    }
                    finalSomeString.append(convertToHTML(responseDummy.htmlData))
                    orderListStatus = false
                    unOrderListStatus = false
                } else if (inData.contains("###")) {
                    var responseDummy: ResponseDataFormat = setH3(inData)
                    finalSomeCopyString.append(responseDummy.copyData).append("\n")
                    if (orderListStatus) {
                        responseDummy.htmlData = "</ol>" + responseDummy.htmlData
                    }
                    if (unOrderListStatus) {
                        responseDummy.htmlData = "</ul>" + responseDummy.htmlData
                    }
                    finalSomeString.append(convertToHTML(responseDummy.htmlData))
                    orderListStatus = false
                    unOrderListStatus = false
                } else {
                    if (!inData.equals("\n") && inData.isNotEmpty()) {
                        finalSomeCopyString.append(inData)
                        finalSomeString.append("</ol>")
                        finalSomeString.append(convertToHTML(inData))
                        if (index > 1) {
                            finalSomeString.append("<br>")
                            finalSomeCopyString.append("\n")
                        }
                    } else if (inData.isEmpty()) {
                        finalSomeString.append("<br>")
                        finalSomeCopyString.append("\n")
                    }
                }
            } else {
                if (!inData.equals("\n") && inData.isNotEmpty()) {
                    finalSomeCopyString.append(inData)
                    finalSomeString.append(convertToHTML(inData))
                    if (index > 1) {
                        finalSomeString.append("<br>")
                        finalSomeCopyString.append("\n")
                    }
                } else if (inData.isEmpty()) {
                    //finalSomeString.append("<br>")
                    //finalSomeCopyString.append("\n")
                }
            }

            if (tableRowStatus) {
                finalSomeString.append("</tr>")
                tableRowStatus = false
            } else {
                if (tableHeaderStatus == true && tableDataStatus == true) {
                    tableHeaderStatus = false
                    tableDataStatus = false
                    finalSomeString.append("</table><br>")
                }
            }
        }


        var resultAfterLenticularBracketsRemoved =
            removeLenticularBrackets(finalSomeString.toString())

        var finalSomeCopyStringAfterLenticularBracketsRemoved =
            removeLenticularBrackets(finalSomeCopyString.toString())

        //Log.i("removeContent", "removeContent: " + resultAfterLenticularBracketsRemoved)

        //Log.i("FinalResp", "convert2Chat: $resultAfterLenticularBracketsRemoved")
        response.copyData = finalSomeCopyStringAfterLenticularBracketsRemoved
        response.htmlData = resultAfterLenticularBracketsRemoved.replace("[- **URL**:", "")
        return response
    }

    fun setH4(inputData: String): ResponseDataFormat {
        val response: ResponseDataFormat = ResponseDataFormat("", "", null)
        val data = inputData.replace("####", "")
        response.copyData = data
        response.speechData = data
        response.htmlData = convertToHTML("<H4>".plus(data).plus("</H4>"))
        return response
    }

    fun setH3(inputData: String): ResponseDataFormat {
        val response: ResponseDataFormat = ResponseDataFormat("", "", null)
        val data = inputData.replace("###", "")
        response.copyData = data
        response.speechData = data
        response.htmlData = convertToHTML("<H3>".plus(data).plus("</H3>"))
        return response
    }

    fun setUnOrderList(inputData: String, status: Boolean): ResponseDataFormat {
        val response: ResponseDataFormat = ResponseDataFormat("", "", null)
        val data = inputData.replace("-", "")
        response.copyData = data
        response.speechData = data
        if (!status) {
            response.htmlData = convertToHTML("<ul><li>".plus(data).plus("</li>"))
        } else {
            response.htmlData = convertToHTML("<li>".plus(data).plus("</li>"))
        }
        return response
    }

    fun setOrderList(inputData: String, status: Boolean): ResponseDataFormat {
        val response: ResponseDataFormat = ResponseDataFormat("", "", null)
        val data = inputData.replace(regexOrderList, "")
        response.copyData = data
        response.speechData = data
        if (regexURL.containsMatchIn(inputData)) {
            var responseDummy: ResponseDataFormat = setImages(data)
        }
        if (!status) {
            response.htmlData = convertToHTML("<ol><li>".plus(data).plus("</li>"))
        } else {
            response.htmlData = convertToHTML("<li>".plus(data).plus("</li>"))
        }
        return response
    }

    fun setTableFormatHeader(inputData: String, status: Boolean): ResponseDataFormat {
        val response: ResponseDataFormat = ResponseDataFormat("", "", null)
        val tableString: StringBuffer = StringBuffer()
        val stringWithoutSpaces = inputData.replace("\\s".toRegex(), "")
        val data = stringWithoutSpaces.split("|")
        if (!status) {
            tableString.append(convertToHTML("<br><table border=\"1\"><tr>"))
        }
        for ((index, tableData) in data.withIndex()) {
            if (index == 0 || index == data.lastIndex) {
                continue
            }
            tableString.append(convertToHTML("<th>".plus(tableData).plus("</th>")))
        }
        response.copyData = inputData
        response.htmlData = tableString.toString()
        return response
    }

    fun setTableFormatData(inputData: String, status: Boolean): ResponseDataFormat {
        val response: ResponseDataFormat = ResponseDataFormat("", "", null)
        val tableString: StringBuffer = StringBuffer()
        val stringWithoutSpaces = inputData
        val data = stringWithoutSpaces.split("|")
        if (!status) {
            tableString.append(convertToHTML("<tr>"))
        }
        for ((index, tableData) in data.withIndex()) {
            if (index == 0 || index == data.lastIndex) {
                continue
            }
            tableString.append(convertToHTML("<td>".plus(tableData).plus("</td>")))
        }
        response.copyData = inputData
        response.htmlData = tableString.toString()
        return response
    }

    fun setImages(inputData: String): ResponseDataFormat {
        val response = ResponseDataFormat("", "", null)
        val urls = regexURL.findAll(inputData).map { it.value }.toList()
        for (urlData in urls) {
            var responseUrlHint = inputData.replace(urlData, "")
            responseUrlHint = responseUrlHint.replace("(", "").replace(")", "")
            responseUrlHint = responseUrlHint.replace("![", "").replace("]", "")
            responseUrlHint = responseUrlHint.replace("- [", "")
            responseUrlHint = responseUrlHint.replace("†source", "")
            responseUrlHint = responseUrlHint.replace("[ -", "")
            responseUrlHint = responseUrlHint.replace("[-**URL**:", "")

            val responseUrl = urlData.replace("(", "").replace(")", "")
            if (CommonUtils.isValidImageURL(responseUrl)) {
                var urlValue: StringBuffer = StringBuffer()
                urlValue.append("\n")

                urlValue.append("<a <br> <div style=\" border: 1px solid #8C8C8C; padding:5px; \"> <img  id=\"$responseUrl\"src=\"")
                    .append(responseUrl)
                    .append("\" onclick=imageClicked('$responseUrl') /> </div> </a>")
                    .append(
                        "<script type=\"text/javascript\">\n" +
                                "        function imageClicked(imageId) {\n" +
                                "            // Call the Kotlin function via JavaScript bridge\n" +
                                "            Android.onImageClicked(imageId);\n" +
                                "        }\n" +
                                "    </script>"
                    )
                    .append("<div style=\"display: flex; justify-content: flex-end; margin-top: 5px; \">[${responseUrlHint}]</div> </br>")

                response.copyData = responseUrl
                response.speechData = responseUrl

                response.htmlData = urlValue.toString()
            } else {
                response.htmlData = urlData
            }
        }
        return response
    }

    fun convertToHTML(text: String): String {/* return text.replace(" **", "<strong>").replace("**", "</strong>")*/
        var data = text.replaceFirst("**", "<strong>")
        data = data.replaceFirst("**", "</strong>")
        return data
    }

    fun convertToHTML1(text: String): String {/* return text.replace(" **", "<strong>").replace("**", "</strong>")*/
        var data = text.replaceFirst("- **", "</ul><br>")
        data = data.replaceFirst("- **", "</ul><br>")
        return data
    }

}