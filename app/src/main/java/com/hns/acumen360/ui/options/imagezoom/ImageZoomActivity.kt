package com.hns.acumen360.ui.options.imagezoom

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Picture
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SwitchCompat
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.caverock.androidsvg.SVG
import com.hns.acumen360.R
import com.hns.acumen360.base.BaseActivity
import com.hns.acumen360.base.BaseApplication
import com.hns.acumen360.base.GlobalBundle
import com.hns.acumen360.data.viewmodel.CommonViewModel
import com.hns.acumen360.di.component.ActivityComponent
import com.hns.acumen360.utils.common.ZoomableImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class ImageZoomActivity : BaseActivity<CommonViewModel>() {

    var zoomableImageView: ZoomableImageView? = null
    var ivClose: AppCompatImageView? = null
    var progressLoader: ProgressBar? = null
    var switchThemeChange: SwitchCompat? = null
    var clParent: CoordinatorLayout? = null
    override fun setContext() = this@ImageZoomActivity

    override fun setOrientation() = BaseApplication.orientationUnSpecified

    override fun setAcumenThemes() = null

    override fun resourceLayout() = R.layout.image_zoom_layout

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {
        /*window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val wm = getSystemService(
            WINDOW_SERVICE
        ) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val width: Int = CommonUtils.getScreenWidth(this) - 200
        val height: Int = CommonUtils.getScreenHeight(this) - 200
        window.setLayout(width, height)*/

        zoomableImageView = findViewById(R.id.iv_image)
        ivClose = findViewById(R.id.cv_close)
        progressLoader = findViewById(R.id.p_loader)
        switchThemeChange = findViewById(R.id.switch_theme)
        clParent = findViewById(R.id.l_parent)


        var backgroundColor = ContextCompat.getColor(this, R.color.white)
        clParent?.setBackgroundColor(backgroundColor)

        switchThemeChange?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                backgroundColor = ContextCompat.getColor(this, R.color.grey)
                clParent?.setBackgroundColor(backgroundColor)
            } else {
                backgroundColor = ContextCompat.getColor(this, R.color.white)
                clParent?.setBackgroundColor(backgroundColor)
            }

        }

        ivClose?.setOnClickListener {
            finish()
        }

        val url = intent.getStringExtra(GlobalBundle.IMAGE_URL)
        url?.apply {
            if (this.contains(".svg")) {
                Thread {
                    val svgStream = fetchSvg(this)
                    val bitmap = renderSvgToBitmap(svgStream)
                    runOnUiThread {
                        if (bitmap != null) {
                            zoomableImageView?.setImageBitmap(bitmap)
                            progressLoader?.visibility = View.GONE
                        } else
                            progressLoader?.visibility = View.GONE
                    }
                }.start()
            } else
                loadImageFromUrl(this)
        }
    }

    override fun setViewModel() {
    }

    suspend fun urlToBitmap(url: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    fun loadImageFromUrl(url: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val bitmap = urlToBitmap(url)
            if (bitmap != null) {
                progressLoader?.visibility = View.GONE
                zoomableImageView?.setImageBitmap(bitmap)
            } else
                progressLoader?.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    private val httpClient = OkHttpClient()
    private fun fetchSvg(url: String): InputStream? {
        val request = Request.Builder().url(url).build()
        val response = httpClient.newCall(request).execute()
        return response.body?.byteStream()
    }

    private fun renderSvgToBitmap(svgStream: InputStream?): Bitmap? {
        return try {
            val svg = SVG.getFromInputStream(svgStream)
            val picture: Picture = svg.renderToPicture()
            val width = picture.width
            val height = picture.height
            Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
                val canvas = Canvas(this)
                canvas.drawPicture(picture)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            svgStream?.close()
        }
    }
}