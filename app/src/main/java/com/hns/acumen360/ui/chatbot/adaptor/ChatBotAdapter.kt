package com.hns.acumen360.ui.chatbot.adaptor

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.HorizontalScrollView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.hns.acumen360.R
import com.hns.acumen360.data.support.ResponseDataFormat
import com.hns.acumen360.ui.chatbot.model.ChatBotData
import com.hns.acumen360.ui.chatbot.support.ImageClickListener
import com.hns.acumen360.utils.common.CommonData
import com.hns.acumen360.utils.common.CommonUtils
import com.hns.acumen360.utils.common.Constants
import com.hns.acumen360.utils.common.FormattingUtils
import de.hdodenhof.circleimageview.CircleImageView


class ChatBotAdapter(recyclerView: RecyclerView?, mActivity: Activity) :
    RecyclerView.Adapter<ChatBotAdapter.ViewHolder>() {
    lateinit var mContext: Context
    private var listItems: MutableList<ChatBotData> = ArrayList<ChatBotData>()
    lateinit var recyclerView: RecyclerView
    lateinit var mActivity: Activity
    lateinit var listener: getOptions
    var streamState: Boolean = false
    var bitMap: Bitmap? = null

    var handler = Handler(Looper.getMainLooper())

    init {
        this.recyclerView = recyclerView!!
        this.mActivity = mActivity
    }

    fun resetAll() {
        listItems = ArrayList<ChatBotData>()
        streamState = false
        notifyDataSetChanged()
    }

    fun resetAllAudio() {
        for ((index, inData) in listItems.withIndex()) {
            listItems[index].audioState = false
        }
    }

    fun resetAllAudioProgress() {
        for ((index, inData) in listItems.withIndex()) {
            listItems[index].audioProgress = false
        }
    }

    fun setProfileImage(bitmap_: Bitmap) {
        this.bitMap = bitmap_
    }

    fun setPostInit(question: String, listener: getOptions) {
        streamState = false
        this.listener = listener
        resetAllAudioProgress()
        notifyItemChanged(listItems.lastIndex)
        listItems.add(ChatBotData(question, "", "", "", null, false, true))
        /* notifyDataSetChanged()*/
    }

    fun setPostImageInit(question: String, bitmap: Bitmap, listener: getOptions) {
        streamState = false
        this.listener = listener
        listItems.add(ChatBotData(question, "", "", "", bitmap, false, true))
        notifyDataSetChanged()
    }

    fun getListSize(): Int {
        if (!listItems.isNullOrEmpty()) return listItems.size
        else return 0
    }

    fun setPost(
        position: Int,
        dataString: ResponseDataFormat,
    ): MutableList<ChatBotData> {
        streamState = false
        var chatBotData: ChatBotData = listItems[position]
        chatBotData.actualResponse = dataString.actualResponse
        chatBotData.data = dataString.htmlData
        chatBotData.copy = dataString.copyData
        chatBotData.stateButton = dataString.moreActionState
        chatBotData.stateLoading = false
        if (listItems.get(listItems.lastIndex).questions.equals(Constants.DISLIKE_QUESTION) ||
            listItems.get(listItems.lastIndex).questions.equals(Constants.DISLIKE_INPUT)
        ) {
            chatBotData.thumbsDownState = true
        }
        listItems.set(listItems.lastIndex, chatBotData)
        return listItems
    }

    fun setPostStream(
        position: Int,
        dataString: ResponseDataFormat,
        messageId: String,
        like: Boolean,
        dislike: Boolean
    ): MutableList<ChatBotData> {/* streamState = true*/
        if (!listItems.isNullOrEmpty()) {
            var chatBotData: ChatBotData = listItems[position]
            chatBotData.actualResponse = dataString.actualResponse
            chatBotData.data = dataString.htmlData
            chatBotData.copy = dataString.copyData
            chatBotData.htmlStream = dataString.htmlStream
            chatBotData.stateButton = dataString.moreActionState
            chatBotData.messageId = messageId
            chatBotData.thumbsUpState = like
            chatBotData.thumbsDownState = dislike
            chatBotData.stateLoading = false
            listItems.set(listItems.lastIndex, chatBotData)
        }
        return listItems
    }

    fun networkInterrupted() {
        if (listItems.isNotEmpty()) {
            val position = listItems.lastIndex
            var chatBotData: ChatBotData = listItems[position]
            chatBotData.stateLoading = false
            listItems.set(position, chatBotData)
            notifyItemChanged(position)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val listItem: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_charbot, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = listItems[position]
        try {

            if (bitMap != null) {
                holder.ivUserIcon.setImageBitmap(bitMap)
            }

            if (streamState) {
                appendContent(holder.webview, dataItem)
            } else {
                if (dataItem.bitmap == null) {
                    holder.tvUserInput.visibility = View.VISIBLE
                    holder.tvUserInput.text = dataItem.questions
                    holder.ivUserImage.visibility = View.GONE
                    holder.ivUserImage.setImageBitmap(dataItem.bitmap)
                } else {
                    holder.tvUserInput.visibility = View.VISIBLE
                    holder.tvUserInput.text = dataItem.questions
                    holder.ivUserImage.visibility = View.VISIBLE
                    holder.ivUserImage.setImageBitmap(dataItem.bitmap)
                }

                if (listItems.size - 1 == position) {
                    holder.viewLine.visibility = View.GONE
                } else {
                    holder.viewLine.visibility = View.VISIBLE
                }

                if (dataItem.stateButton) {
                    holder.llSupportIcon.visibility = View.VISIBLE
                } else {
                    holder.llSupportIcon.visibility = View.GONE
                }

                setListeners(holder, dataItem, position)
                setWebViewData(holder.webview, dataItem, position)
            }

            if (dataItem.actualResponse.isNotEmpty()) {
                holder.ivBrand.visibility = View.VISIBLE
            } else {
                holder.ivBrand.visibility = View.GONE
            }

            if (dataItem.stateLoading) {
                holder.lottieBrand.visibility = View.VISIBLE
            } else {
                holder.lottieBrand.visibility = View.GONE
            }

            if (dataItem.audioState) {
                holder.lottieTTS.visibility = View.VISIBLE
                holder.tbRead.visibility = View.INVISIBLE
            } else {
                holder.lottieTTS.visibility = View.GONE
                holder.tbRead.visibility = View.VISIBLE
            }

            if (dataItem.audioProgress) {
                holder.tbRead.visibility = View.GONE
                holder.audioLoader.visibility = View.VISIBLE
            } else {
                holder.audioLoader.visibility = View.GONE
                holder.tbRead.visibility.apply {
                    if (holder.lottieTTS.visibility == View.VISIBLE)
                        View.INVISIBLE
                    else
                        View.VISIBLE
                }
            }

            if (dataItem.thumbsUpState) {
                holder.thumbsUp.setTextColor(
                    ContextCompat.getColor(
                        mContext, R.color.acumen360_blue
                    )
                )
            } else {
                holder.thumbsUp.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray))
            }

            if (dataItem.thumbsDownState) holder.thumbsDown.setTextColor(
                ContextCompat.getColor(mContext, R.color.acumen360_blue)
            )
            else {
                holder.thumbsDown.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.text_gray
                    )
                )
            }

            if (position == listItems.lastIndex) {
                holder.llSupportIcon1.visibility = View.VISIBLE
            } else {
                holder.llSupportIcon1.visibility = View.GONE
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setListeners(holder: ViewHolder, myData: ChatBotData, position: Int) {
        if (myData.data.trim().contains("clutch", true) || myData.data.trim()
                .contains("disc", true)
        ) {
            holder.tbVisualizer.visibility = View.VISIBLE
        } else if (myData.data.trim().contains("seat", true)
            || myData.data.trim().contains("uphill", true)
            || myData.data.trim().contains("downhill", true)
            || myData.data.trim().contains(CommonData.ADAS_BRAKE_ASSIST, true)
            || myData.data.trim().contains(
                CommonData.PENTALINK_SUSPENSION_WITH_FDD_TECH, true
            )
        ) {
            // Log.i("Visualizer", " others ")
            holder.tbVisualizer.visibility = View.VISIBLE
        } else {
            holder.tbVisualizer.visibility = View.GONE
        }
        holder.audioLoader.setOnClickListener {
            for ((index, inData) in listItems.withIndex()) {
                if (inData.audioProgress && position != index) {
                    inData.audioProgress = false
                    inData.audioState = false
                    notifyItemChanged(index, inData)
                    break
                }
            }
            if (myData.audioProgress) {
                myData.audioProgress = false
                myData.audioState = false
            }
            notifyItemChanged(position)
        }
        holder.tbRead.setOnClickListener {
            for ((index, inData) in listItems.withIndex()) {
                if ((inData.audioState || inData.audioProgress) && position != index) {
                    inData.audioState = false
                    inData.audioProgress = false
                    notifyItemChanged(index, inData)
                    listener.previousPlaying(index, inData)
                    break
                }
            }


            if (myData.audioState && !myData.audioProgress) {

            } else if (!myData.audioProgress && !myData.audioState) {
                myData.audioProgress = true
                notifyItemChanged(position, myData)
            } else {
                resetAllAudio()
                resetAllAudioProgress()
                notifyDataSetChanged()
            }

            notifyItemChanged(position)
            listener.onRead(position, myData.copy, myData)


        }

        holder.lottieTTS.setOnClickListener {
            holder.tbRead.performClick()
        }

        holder.tbCopy.setOnClickListener {
            listener.onCopy(myData.copy)
        }

        holder.thumbsUp.setOnClickListener {

            listItems[position].thumbsUpState = !listItems[position].thumbsUpState
            listItems[position].thumbsDownState = false
            //CommonUtils.rotateView(listItems[position].thumbsUpState, holder.thumbsUp)
            notifyItemChanged(position)
            listener.likeDisLike(
                myData.messageId,
                listItems[position].thumbsUpState,
                listItems[position].thumbsDownState
            )


        }

        holder.thumbsDown.setOnClickListener {
            listItems[position].thumbsDownState = !listItems[position].thumbsDownState
            listItems[position].thumbsUpState = false
            //CommonUtils.rotateView(listItems[position].thumbsDownState, holder.thumbsDown)
            notifyItemChanged(position)
            listener.likeDisLike(
                myData.messageId,
                myData.thumbsUpState,
                myData.thumbsDownState
            )
        }

        holder.tbTransalte.setOnClickListener {
            listener.onTranslate(it)
        }
        holder.tbEvaluate.setOnClickListener {
            listener.onEvaluate("Ask two yes-or-no questions sequentially and evaluate my knowledge based on my answers!")
        }
        holder.tbVisualizer.setOnClickListener {
            if (myData.data.contains("seat")) {
                listener.onVisualizer("seat")
            } else if (myData.data.contains("uphill")) {
                listener.onVisualizer("uphill")
            } else if (myData.data.contains("downhill")) {
                listener.onVisualizer("downhill")
            } else if (myData.data.contains("clutch", true) || myData.data.contains("disc", true)) {
                if (myData.data.contains(CommonData.CLUTCH_COVER_AND_REMOVAL, true)) {
                    listener.onVisualizer("removal")
                } else {
                    listener.onVisualizer("installation")
                }
            } else if (myData.data.contains(CommonData.ADAS_BRAKE_ASSIST)) {
                listener.onVisualizer(CommonData.ADAS_BRAKE_ASSIST_URL)
            } else if (myData.data.contains(CommonData.PENTALINK_SUSPENSION_WITH_FDD_TECH)) {
                listener.onVisualizer(CommonData.PENTALINK_SUSPENSION_WITH_FDD_TECH_URL)
            } else {
                listener.onVisualizer("")
            }
        }

        holder.tbInteractively.setOnClickListener {
            listener.onInteractively("As an expert in Service of XUV 700 can you explain this question interactively step by step (one at a time)?")
        }


        holder.tbMore.setOnClickListener {
            listener.onMore(it)
        }
    }

    fun setWebViewData(webview: WebView, myData: ChatBotData, position: Int) {
        val webData: ResponseDataFormat
        if (!myData.stateButton) {
            if (myData.actualResponse.isNotEmpty()) {
                webData = FormattingUtils.convertMarkDown(myData.actualResponse)
            } else {
                webData = FormattingUtils.convertMarkDown(myData.actualResponse)
            }
        } else {
            webData = FormattingUtils.convertMarkDown(myData.actualResponse)
        }
        webview.settings.javaScriptEnabled = true
        webview.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webview.loadDataWithBaseURL(
            null,
            CommonData.cssStyle + webData.htmlData,
            "text/html",
            "utf-8",
            null
        )
        webview.webViewClient = object : WebViewClient() {
            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }

        webview.apply {
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    setLayerType(View.LAYER_TYPE_NONE, null)
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    /*return super.shouldOverrideUrlLoading(view, request)*/
                    request.let {
                        if (it != null) {
                            val responseUrl: String = it.url.toString()
                            if (CommonUtils.isValidImageURL(responseUrl)) {
                                listener.imageZoom(responseUrl, null)
                            } else {
                                listener.hyperLinks(responseUrl)
                            }
                        }
                    }
                    return true
                }
            }
        }

        webview.isVerticalScrollBarEnabled = false
        webview.isHorizontalScrollBarEnabled = false
        webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webview.setBackgroundColor(Color.TRANSPARENT)

        webview.addJavascriptInterface(JsInterface(object : ImageClickListener {
            override fun clickListener(url: String) {
                listener.imageZoom(url, null)

            }
        }), "Android")

        webviewParent = webview


    }

    fun appendContent(webview: WebView, myData: ChatBotData) {
        try {
            mActivity.runOnUiThread {
                webview.post {
                    webview.evaluateJavascript(
                        "document.body.innerHTML += '${myData.htmlStream}';", null
                    )
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    lateinit var webviewParent: WebView

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var clUserInput: ConstraintLayout
        var ivUserIcon: CircleImageView
        var tvUserInput: AppCompatTextView
        var ivUserImage: AppCompatImageView
        var llBrand: LinearLayoutCompat
        var ivBrand: AppCompatImageView
        var lottieBrand: LottieAnimationView
        var webview: WebView
        var tbRead: AppCompatTextView
        var lottieTTS: LottieAnimationView
        var audioLoader: ProgressBar
        var tbCopy: AppCompatTextView
        var thumbsUp: AppCompatTextView
        var thumbsDown: AppCompatTextView
        var tbTransalte: AppCompatTextView
        var tbEvaluate: AppCompatTextView
        var tbVisualizer: AppCompatTextView
        var tbInteractively: AppCompatTextView
        var tbMore: AppCompatTextView
        var llSupportIcon: HorizontalScrollView
        var llSupportIcon1: LinearLayoutCompat
        var viewLine: View

        init {
            clUserInput = itemView.findViewById(R.id.cl_user_input)
            ivUserIcon = itemView.findViewById(R.id.iv_user_icon)
            tvUserInput = itemView.findViewById(R.id.tv_user_input)
            ivUserImage = itemView.findViewById(R.id.iv_user_image)
            llBrand = itemView.findViewById(R.id.ll_brand)
            ivBrand = itemView.findViewById(R.id.iv_brand)
            lottieBrand = itemView.findViewById(R.id.lottie_brand)
            webview = itemView.findViewById<WebView>(R.id.webview)
            tbRead = itemView.findViewById(R.id.tb_read)
            lottieTTS = itemView.findViewById(R.id.lottie_tts)
            audioLoader = itemView.findViewById(R.id.progress_audio_loader)
            tbCopy = itemView.findViewById(R.id.tb_copy)
            thumbsUp = itemView.findViewById(R.id.tb_thumbs_up)
            thumbsDown = itemView.findViewById(R.id.tb_thumbs_down)
            tbTransalte = itemView.findViewById(R.id.tb_transalte)
            tbEvaluate = itemView.findViewById(R.id.tb_evaluate)
            tbVisualizer = itemView.findViewById(R.id.tb_visualizer)
            tbInteractively = itemView.findViewById(R.id.tb_interactively)
            tbMore = itemView.findViewById(R.id.tb_more)
            llSupportIcon = itemView.findViewById(R.id.ll_support_icon)
            llSupportIcon1 = itemView.findViewById(R.id.ll_support_icon_1)
            viewLine = itemView.findViewById(R.id.view_line)
        }
    }

    interface getOptions {
        fun onStream()
        fun onRead(position: Int, data: String, myData: ChatBotData)
        fun onCopy(data: String)
        fun onTranslate(view: View)
        fun onEvaluate(data: String)
        fun onInteractively(data: String)
        fun onVisualizer(data: String)
        fun onMore(view: View)

        fun previousPlaying(position: Int, myData: ChatBotData)
        fun imageZoom(url: String?, bitmap: Bitmap?)
        fun hyperLinks(url: String)
        fun likeDisLike(messageId: String, like: Boolean, dislike: Boolean)
    }


    class JsInterface(imageClickListener_: ImageClickListener) {
        var imageClickListener: ImageClickListener? = imageClickListener_

        @JavascriptInterface
        fun onImageClicked(message: String) {
            imageClickListener?.clickListener(message)

        }
    }
}
