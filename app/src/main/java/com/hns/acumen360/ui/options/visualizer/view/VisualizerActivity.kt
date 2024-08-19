package com.hns.acumen360.ui.options.visualizer.view

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.hns.acumen360.R
import com.hns.acumen360.base.BaseActivity
import com.hns.acumen360.base.BaseApplication
import com.hns.acumen360.data.viewmodel.CommonViewModel
import com.hns.acumen360.di.component.ActivityComponent


/**
 * Created by Kamesh Kannan on 24-April-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
class VisualizerActivity : BaseActivity<CommonViewModel>() {
    lateinit var constHeader: ConstraintLayout
    lateinit var webView: WebView
    lateinit var btnClose: AppCompatImageButton
    var inputString: String = "seat"

    override fun setContext() = this@VisualizerActivity

    override fun setOrientation() = BaseApplication.orientationUnSpecified
    override fun setAcumenThemes() = null

    override fun resourceLayout() = R.layout.activity_visualizer

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        inputString = bundle?.getString("VisualizerData").toString()
        webView = findViewById<WebView>(R.id.webView)
        btnClose = findViewById(R.id.btn_close)
        constHeader = findViewById(R.id.const_header)

        if (inputString.equals("removal", true) || inputString.equals("installation", true)) {
            setCarWebView(webView, "file:///android_asset/install_removal/index.html")
        } else
            setCarWebView(webView, "file:///android_asset/seatanimation/index.html")
        btnClose.setOnClickListener { finish() }
    }

    override fun setViewModel() {

    }

    private fun setCarWebView(myWebView: WebView, url: String) {
        var isRedirected: Boolean = true
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.builtInZoomControls = true
        myWebView.settings.displayZoomControls = false
        myWebView.settings.loadWithOverviewMode = true
        myWebView.settings.useWideViewPort = true
        val settings = myWebView.settings
        settings.allowFileAccessFromFileURLs = true
        settings.allowUniversalAccessFromFileURLs = true
        settings.allowFileAccess = true
        settings.allowContentAccess = true
        myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        myWebView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        myWebView.isFocusable = true
        myWebView.isFocusableInTouchMode = true

        myWebView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (isRedirected) {
                    LoadCarVisualizer(inputString)
                }
                isRedirected = false
            }
        }
        myWebView.loadUrl(url)
        WebView.setWebContentsDebuggingEnabled(true)
    }

    private fun LoadCarVisualizer(data: String) {
        webView.loadUrl(
            "javascript:LoadCarVisualizer(" + "'" +
                    data +
                    "'" + ")"
        )
    }
}