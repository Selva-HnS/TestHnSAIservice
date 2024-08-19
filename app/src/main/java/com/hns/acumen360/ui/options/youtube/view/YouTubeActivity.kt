package com.hns.acumen360.ui.options.youtube.view

import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageButton
import com.hns.acumen360.R
import com.hns.acumen360.base.BaseActivity
import com.hns.acumen360.base.BaseApplication
import com.hns.acumen360.data.viewmodel.CommonViewModel
import com.hns.acumen360.di.component.ActivityComponent
import com.hns.acumen360.utils.common.CommonUtils.getYouTubeId

class YouTubeActivity : BaseActivity<CommonViewModel>() {

    var inputString: String? = null
    lateinit var webView: WebView
    lateinit var btnClose: AppCompatImageButton

    override fun setContext() = this@YouTubeActivity

    override fun setOrientation() = BaseApplication.screenSize

    override fun setAcumenThemes(): Int?  = null

    override fun resourceLayout() = R.layout.activity_youtube

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        inputString = bundle?.getString("VisualizerData").toString()
        webView = findViewById(R.id.webView)
        btnClose = findViewById(R.id.btn_close)
        btnClose.visibility = View.GONE
        youtubeVideoPlayer()
    }

    override fun setViewModel() {

    }

    fun youtubeVideoPlayer() {
        inputString?.let {
            val regexYoutube =
                Regex("^(http(s)?://)?((w){3}.)?youtu(be|.be)?(\\.com)?/.+")
            var youTubeId = getYouTubeId(it!!)
            var videoUrlBase = "https://www.youtube.com/embed/$youTubeId"
            val frameVideo1 = "<html><body><iframe frameborder=\"0\" marginwidth=\"0\" marginheight=\"0\" width=\"100%\" height=\"300\" src=\"$videoUrlBase\"  frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe></body></html>"

            if (it.matches(regexYoutube)) {
                webView?.webViewClient =
                    object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            return false
                        }

                        override fun onPageFinished(
                            view: WebView?,
                            url: String?
                        ) {
                            super.onPageFinished(view, url)

                        }
                    }

                val webSettings = webView?.settings
                webSettings?.javaScriptEnabled = true
                webView?.webChromeClient =
                    MyChrome1(webView!!, this@YouTubeActivity, videoUrlBase)
                webView?.webViewClient = WebViewClient()
                webSettings?.loadWithOverviewMode = true
                // webSettings?.useWideViewPort = true
                // webSettings?.mediaPlaybackRequiresUserGesture = true
                webView?.loadData(
                    frameVideo1,
                    "text/html",
                    "utf-8"
                )
            }
        }
    }


    internal class MyChrome1(webView: WebView, activity: Activity, url: String) :
        WebChromeClient() {
        val activity: Activity = activity
        var webview: WebView? = webView
        var url: String? = url

        private var mCustomView: View? = null
        private var mCustomViewCallback: CustomViewCallback? = null
        protected var mFullscreenContainer: FrameLayout? = null
        private var mOriginalOrientation = 0
        private var mOriginalSystemUiVisibility = 0
        override fun getDefaultVideoPoster(): Bitmap? {
            return if (mCustomView == null) {
                null
            } else BitmapFactory.decodeResource(activity.resources, 2130837573)
        }

        override fun onHideCustomView() {
            (activity.getWindow().getDecorView() as FrameLayout).removeView(mCustomView)
            mCustomView = null
            //activity.window.decorView.setSystemUiVisibility(mOriginalSystemUiVisibility)
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            activity.requestedOrientation = mOriginalOrientation
            mCustomViewCallback!!.onCustomViewHidden()
            mCustomViewCallback = null
        }

        override fun onShowCustomView(
            paramView: View,
            paramCustomViewCallback: CustomViewCallback
        ) {
            if (mCustomView != null) {
                onHideCustomView()
                return
            }
            mCustomView = paramView
            mOriginalSystemUiVisibility =
                activity.window.decorView.getSystemUiVisibility()
            mOriginalOrientation = activity.requestedOrientation
            mCustomViewCallback = paramCustomViewCallback
            (activity.window.decorView as FrameLayout).addView(
                mCustomView,
                FrameLayout.LayoutParams(-1, -1)
            )
            /*activity.window.decorView
                .setSystemUiVisibility(3846 or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)*/
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            /*val bundle = Bundle()
            bundle.putString("url", url)
            NavigationLauncher.launchActivityBundleStack(
                activity,
                YouTubeFullScreenActivity::class.java, bundle
            )*/
        }
    }
}