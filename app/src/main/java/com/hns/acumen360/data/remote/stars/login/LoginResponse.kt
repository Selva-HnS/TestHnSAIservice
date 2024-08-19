package com.hns.acumen360.data.remote.stars.login

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(

    @field:SerializedName("is_user_valid")
    val isUserValid: Boolean? = null,

    @field:SerializedName("otp_sent_message")
    val otpSentMessage: String? = null,

    @field:SerializedName("status_code")
    val statusCode: Int? = null,

    @field:SerializedName("token_info")
    val tokenInfo: TokenInfo? = null,

    @field:SerializedName("otp_input_type")
    val otpInputType: String? = null,

    @field:SerializedName("tts_voices")
    val ttsVoices: List<TtsVoicesItem?>? = null,

    @field:SerializedName("is_otp_sent")
    val isOtpSent: Boolean? = null,

    @field:SerializedName("otp_length")
    val otpLength: Int? = null,

    @field:SerializedName("otp_expires_in")
    val otpExpiresIn: Long? = null,

    @field:SerializedName("user_profile")
    val userProfile: UserProfile? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("project_configuration")
    val projectConfiguration: List<ProjectConfigurationItem?>? = null
) : Parcelable

@Parcelize
data class CosmicFeatures(

    @field:SerializedName("mobile_translate")
    val mobileTranslate: Boolean? = null,

    @field:SerializedName("mobile_visualizer")
    val mobileVisualizer: Boolean? = null,

    @field:SerializedName("mobile_visualizer_icon")
    val mobileVisualizerIcon: String? = null,

    @field:SerializedName("web_read_aloud_icon")
    val webReadAloudIcon: String? = null,

    @field:SerializedName("web_augmented_reality_icon")
    val webAugmentedRealityIcon: String? = null,

    @field:SerializedName("web_copy_text")
    val webCopyText: Boolean? = null,

    @field:SerializedName("mobile_augmented_reality")
    val mobileAugmentedReality: Boolean? = null,

    @field:SerializedName("web_vision")
    val webVision: Boolean? = null,

    @field:SerializedName("web_copy_text_icon")
    val webCopyTextIcon: String? = null,

    @field:SerializedName("web_vision_icon")
    val webVisionIcon: String? = null,

    @field:SerializedName("web_translate")
    val webTranslate: Boolean? = null,

    @field:SerializedName("web_read_aloud")
    val webReadAloud: Boolean? = null,

    @field:SerializedName("web_visualizer_icon")
    val webVisualizerIcon: String? = null,

    @field:SerializedName("mobile_read_loud")
    val mobileReadLoud: Boolean? = null,

    @field:SerializedName("mobile_vision_icon")
    val mobileVisionIcon: String? = null,

    @field:SerializedName("mobile_copy_text")
    val mobileCopyText: Boolean? = null,

    @field:SerializedName("web_augmented_reality")
    val webAugmentedReality: Boolean? = null,

    @field:SerializedName("mobile_translate_icon")
    val mobileTranslateIcon: String? = null,

    @field:SerializedName("web_visualizer")
    val webVisualizer: Boolean? = null,

    @field:SerializedName("mobile_copy_text_icon")
    val mobileCopyTextIcon: String? = null,

    @field:SerializedName("mobile_augmented_reality_icon")
    val mobileAugmentedRealityIcon: String? = null,

    @field:SerializedName("mobile_vision")
    val mobileVision: Boolean? = null,

    @field:SerializedName("web_translate_icon")
    val webTranslateIcon: String? = null,

    @field:SerializedName("mobile_read_loud_icon")
    val mobileReadLoudIcon: String? = null
) : Parcelable

@Parcelize
data class UserProfile(

    @field:SerializedName("member_id")
    val memberId: String? = null,

    @field:SerializedName("profile_photo")
    val profilePhoto: String? = null,

    @field:SerializedName("first_name")
    val firstName: String? = null
) : Parcelable

@Parcelize
data class TtsVoicesItem(

    @field:SerializedName("base_model")
    val baseModel: String? = null,

    @field:SerializedName("name")
    val name: String? = null
) : Parcelable

@Parcelize
data class AcumenFeaturesItem(

    @field:SerializedName("project_id")
    val projectId: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("iconPath")
    val iconPath: String? = null,

    @field:SerializedName("order")
    val order: Int? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
) : Parcelable

@Parcelize
data class ProjectConfigurationItem(

    @field:SerializedName("acumen_features")
    val acumenFeatures: List<AcumenFeaturesItem?>? = null,

    @field:SerializedName("assistant_name")
    val assistantName: String? = null,

    @field:SerializedName("suggested_questions")
    val suggestedQuestions: List<SuggestedQuestionsItem?>? = null,

    @field:SerializedName("project_id")
    val projectId: String? = null,

    @field:SerializedName("cosmic_features")
    val cosmicFeatures: CosmicFeatures? = null,

    @field:SerializedName("profile_id")
    val profileId: String? = null,

    @field:SerializedName("product_id")
    val productId: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("project_image")
    val projectImage: String? = null
) : Parcelable

@Parcelize
data class SuggestedQuestionsItem(

    @field:SerializedName("title")
    val title: String? = null
) : Parcelable

@Parcelize
data class TokenInfo(

    @field:SerializedName("access_token")
    val accessToken: String? = null,

    @field:SerializedName("refresh_token")
    val refreshToken: String? = null,

    @field:SerializedName("token_type")
    val tokenType: String? = null,

    @field:SerializedName("expires_in")
    val expiresIn: Long? = null
) : Parcelable
