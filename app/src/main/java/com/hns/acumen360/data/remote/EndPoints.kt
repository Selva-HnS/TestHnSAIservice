package com.hns.acumen360.data.remote

object EndPoints {

    /*Header Keys*/
    const val SUBSCRIPTION_HEADER_KEY = "X-Acumen-Subscription-Key"
    const val AUTHORIZATION_HEADER_KEY = "Authorization"
    const val BEARER = "Bearer "
    const val JWT_TOKEN_KEY = "JWT Token "

    const val SUBSCRIPTION_KEY =
        "mtegSCm8X2JNjNufntRYW2PS1buUPpgafMZ0ddN6ynV58j0qzRBmezJjLwLZ/Y7XXA7Y0jzy8j7Pk5Q9mNumNvxHQQDZiUfyptH0OM1kEj6zrgXmeXX6ls1DyDnvW9AP"

    const val BASE_STARS_URL = "http://hnsaiservices.io/"
    private const val API_VERSION = "1.0"

    const val LOGIN = "identity/api/authentication/$API_VERSION/login"
    const val VERIFY_OTP = "identity/api/authentication/$API_VERSION/verifyotp"
    const val RESEND_OTP = "identity/api/authentication/$API_VERSION/resendotp"
    const val REFRESH_TOKEN = "identity/api/authentication/$API_VERSION/refreshtoken"

    //const val BASE_GALAXY_URL = "https://api.acumen360.ai/v1/"

    const val TEXT_GENERATE = "textgen/galaxy/api/chat/$API_VERSION/generate-text-stream"

    /*Asset base url*/
    const val ASSET_BASE_URL = "https://acumen360.ai"
}