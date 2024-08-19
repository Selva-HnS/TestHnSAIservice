package com.hns.acumen360.base.remote

data class RemoteViewModelResponse(var type: String, var data: Any) {

    var function: (() -> Unit)? = null
    var subType: String? = null
    var subData: Any? = null
    var msg: String? = null

    constructor(type: String, data: Any, function: () -> (Unit)) : this(type, data) {
        this.type = type
        this.data = data
        this.function = function
    }

    constructor(type: String) : this(type, true) {
        this.type = type
        this.subType = ""
        this.data = true
    }

    constructor(type: String, subType: String, data: Any) : this(type, data) {
        this.type = type
        this.data = data
        this.subType = subType
    }

    companion object {
        const val TOAST = "TOAST"
        const val NETWORK = "NETWORK"
        const val PROGRESSING = "PROGRESSING"
        const val SUCCESS = "SUCCESS"
        const val FAILURE = "FAILURE"
        const val ERROR = "ERROR"
    }
}
