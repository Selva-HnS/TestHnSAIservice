package com.hns.acumen360.di.component


import com.hns.acumen360.di.module.ActivityModule
import com.hns.acumen360.ui.chatbot.view.ChatBotActivity
import com.hns.acumen360.ui.login.view.LoginActivity
import com.hns.acumen360.ui.options.imagezoom.ImageZoomActivity
import com.hns.acumen360.ui.splash.view.SplashActivity
import com.hns.acumen360.ui.options.visualizer.view.VisualizerActivity
import com.hns.acumen360.ui.options.youtube.view.YouTubeActivity
import com.hns.acumen360.ui.verifyotp.view.VerifyOTPActivity
import com.hns.minda.di.ActivityScope
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent {
    fun inject(activity: SplashActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: ChatBotActivity)
    fun inject(activity: VisualizerActivity)
    fun inject(activity: YouTubeActivity)
    fun inject(activity: ImageZoomActivity)
    fun inject(activity: VerifyOTPActivity)
}