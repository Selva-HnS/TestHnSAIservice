package com.hns.acumen360.ui.chatbot.view

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.gson.Gson
import com.hns.acumen360.R
import com.hns.acumen360.base.BaseActivity
import com.hns.acumen360.base.BaseApplication
import com.hns.acumen360.base.GlobalBundle
import com.hns.acumen360.base.GlobalBundle.GET_DATA
import com.hns.acumen360.base.GlobalBundle.MESSAGE_ID
import com.hns.acumen360.base.GlobalBundle.VISUALIZER_DATA
import com.hns.acumen360.base.ResponseType
import com.hns.acumen360.base.ViewModelResponse
import com.hns.acumen360.base.remote.RemoteResponseType
import com.hns.acumen360.base.remote.RemoteViewModelResponse
import com.hns.acumen360.data.local.db.entity.ProjectDetailsDB
import com.hns.acumen360.data.local.db.entity.SuggestedQuestionsDB
import com.hns.acumen360.data.local.db.entity.UserDetailsDB
import com.hns.acumen360.data.remote.EndPoints
import com.hns.acumen360.data.remote.OrbitClient
import com.hns.acumen360.data.remote.common.request.RequestBodyModel
import com.hns.acumen360.data.remote.common.request.RequestImageModel
import com.hns.acumen360.data.remote.common.request.RequestSTTModel
import com.hns.acumen360.data.remote.common.request.RequestTTSModel
import com.hns.acumen360.data.remote.common.response.ResponseBodyModel
import com.hns.acumen360.data.remote.galaxy.chathistory.ChatHistoryRequest
import com.hns.acumen360.data.remote.galaxy.chathistory.ChatHistoryResponse
import com.hns.acumen360.data.remote.galaxy.createthread.CreateThreadRequest
import com.hns.acumen360.data.remote.galaxy.createthread.CreateThreadResponse
import com.hns.acumen360.data.remote.galaxy.updatethread.UpdateThreadRequest
import com.hns.acumen360.data.remote.galaxy.updatethread.UpdateThreadResponse
import com.hns.acumen360.data.remote.stars.OrbitStarService
import com.hns.acumen360.data.remote.stars.likedislike.LikeDisLikeRequest
import com.hns.acumen360.data.remote.stars.likedislike.LikeDisLikeResponse
import com.hns.acumen360.data.remote.stars.servicehistory.ServiceHistoryRequest
import com.hns.acumen360.data.remote.stars.threads.MessageItem
import com.hns.acumen360.data.remote.stars.threads.ThreadsResponse
import com.hns.acumen360.data.support.InstanceData
import com.hns.acumen360.data.support.ResponseDataFormat
import com.hns.acumen360.data.viewmodel.RemoteStarViewModel
import com.hns.acumen360.di.component.ActivityComponent
import com.hns.acumen360.listener.GetListener
import com.hns.acumen360.ui.chatbot.adaptor.ChatBotAdapter
import com.hns.acumen360.ui.chatbot.adaptor.ParentCallBackListener
import com.hns.acumen360.ui.chatbot.adaptor.SuggestedAdapter
import com.hns.acumen360.ui.chatbot.adaptor.ThreadAdapter
import com.hns.acumen360.ui.chatbot.adaptor.ThreadChildAdapter
import com.hns.acumen360.ui.chatbot.model.ChatBotData
import com.hns.acumen360.ui.login.model.MenuAction
import com.hns.acumen360.ui.login.view.LoginActivity
import com.hns.acumen360.ui.options.imagezoom.ImageZoomActivity
import com.hns.acumen360.ui.options.visualizer.view.VisualizerActivity
import com.hns.acumen360.ui.options.youtube.view.YouTubeActivity
import com.hns.acumen360.utils.common.CommonData
import com.hns.acumen360.utils.common.CommonUtils
import com.hns.acumen360.utils.common.CommonUtils.smoothScroll
import com.hns.acumen360.utils.common.Constants
import com.hns.acumen360.utils.common.Constants.QUESTION_EVALUATE
import com.hns.acumen360.utils.common.Constants.QUESTION_EXPLAIN_INTERACTIVELY
import com.hns.acumen360.utils.common.Constants.VISION
import com.hns.acumen360.utils.common.Constants.YOUTUBE
import com.hns.acumen360.utils.common.FormattingUtils
import com.hns.acumen360.utils.common.TimeFormats
import com.hns.acumen360.utils.galaxymanager.STTManager
import com.hns.acumen360.utils.galaxymanager.TTSManager
import com.hns.acumen360.utils.launcher.NavigationLauncher
import com.hns.acumen360.utils.log.Logger
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale


/**
 * Created by Kamesh Kannan on 19-April-2024.
 * Copyright (c) by Hard n Soft Technologies Pvt. Ltd. All rights reserved..
 */
class ChatBotActivity : BaseActivity<RemoteStarViewModel>(), GetListener {

    override fun resourceLayout() = R.layout.activity_main
    override fun setContext() = this@ChatBotActivity
    override fun setOrientation() = BaseApplication.orientationPortrait
    override fun setAcumenThemes(): Int? = null
    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    lateinit var smoothScroller: SmoothScroller
    lateinit var layoutManager: LinearLayoutManager
    lateinit var responseBodyCall: Call<ResponseBody>
    private lateinit var responseLikeDisLike: Call<LikeDisLikeResponse>

    private var speechDataState: Boolean = false
    private var handler: Handler? = null
    private lateinit var cvNewChat: CardView
    private lateinit var ibSuggestMenu: AppCompatTextView
    private lateinit var ibHelpMenu: AppCompatTextView
    private lateinit var btnLogout: AppCompatButton
    private lateinit var tvNameProduct: AppCompatTextView
    private lateinit var clDummyLogo: ConstraintLayout
    lateinit var linearProgress: LinearProgressIndicator
    lateinit var rvChatBot: RecyclerView
    lateinit var chatbotAdapter: ChatBotAdapter
    private lateinit var imageMenu: AppCompatImageButton
    lateinit var ivAttachment: AppCompatImageButton
    private lateinit var clSenderButton: ConstraintLayout
    lateinit var ibSendData: AppCompatImageButton
    lateinit var etContent: AppCompatEditText
    lateinit var spVoiceList: AppCompatSpinner
    lateinit var ivProfileImage: CircleImageView
    private lateinit var tvProfileID: AppCompatTextView
    private lateinit var tvUserName: AppCompatTextView
    private lateinit var tvLoginDate: AppCompatTextView
    private lateinit var rvChatThread: RecyclerView
    lateinit var btnDownArrow: AppCompatButton
    lateinit var ivProjectImage: AppCompatImageView
    private lateinit var spProduct: AppCompatSpinner

    private lateinit var sttManager: STTManager
    private lateinit var ttsManager: TTSManager
    var profileImage: Bitmap? = null
    private var finalSomeString: String = ""
    private var inputCount = -1
    var voiceType: String? = ""
    private var userContentListItems: MutableList<ChatBotData> = ArrayList()
    private lateinit var responseDataFormat: ResponseDataFormat
    var projectDetails: ProjectDetailsDB? = null
    private var createThreadResponse: CreateThreadResponse? = null
    private var backPressedTime: Long = 0
    private var apiService: OrbitStarService? = null
    var threadID: String? = null
    var projectID: String? = null
    private var suggestedQuestionsDBList: MutableList<SuggestedQuestionsDB>? = null
    var likeDisLikeRequest: LikeDisLikeRequest? = null
    private var userDetailsDB: UserDetailsDB? = null

    companion object {
        private const val STORAGE_PERMISSION_CODE = 101
    }

    override fun setupView(savedInstanceState: Bundle?) {
        setDrawerLayout()
        setInit()
        setListener()
        setListChat()
        backPressHandleCallBack()
        viewModelCommon.loginUserSelectActive()
    }

    /**
     *
     * Initialize the view
     */
    private fun setInit() {
        cvNewChat = findViewById(R.id.cv_new_chat)
        ibSuggestMenu = findViewById(R.id.ib_suggest_menu)
        ibHelpMenu = findViewById(R.id.ib_help_menu)
        btnLogout = findViewById(R.id.btn_logout)
        tvNameProduct = findViewById(R.id.tv_name_product)
        clDummyLogo = findViewById(R.id.cl_dummy_logo)
        linearProgress = findViewById(R.id.linear_progress)
        rvChatBot = findViewById(R.id.rv_chat_bot)
        etContent = findViewById(R.id.et_content)
        ivAttachment = findViewById(R.id.iv_attachment)
        ibSendData = findViewById(R.id.ib_send_data)
        clSenderButton = findViewById(R.id.cl_sender_button)
        imageMenu = findViewById(R.id.ib_image_menu)
        spVoiceList = findViewById(R.id.sp_voice_list)
        ivProfileImage = findViewById(R.id.iv_user)
        tvUserName = findViewById(R.id.tv_user_name)
        tvLoginDate = findViewById(R.id.tv_login_date)
        rvChatThread = findViewById(R.id.rv_chat_thread)
        btnDownArrow = findViewById(R.id.btn_down_arrow)
        tvProfileID = findViewById(R.id.tv_profile_id)
        ivProjectImage = findViewById(R.id.iv_logo_product)
        spProduct = findViewById(R.id.sp_product)

        sttManager = STTManager(mActivity)
        ttsManager = TTSManager(mActivity, ttsListener)
        handler = Handler(Looper.getMainLooper())
        apiService = OrbitClient.clientStars?.create(OrbitStarService::class.java)!!
    }

    /**
     *
     * Fetch Response from API or Database
     */
    override fun setViewModel() {
        viewModelCommon.loginUsers.observe(this) {
            when (it.type) {
                ViewModelResponse.SUCCESS -> {
                    when (it.subType) {
                        ResponseType.SELECT_LOGIN_USER -> {
                            val data = it.data as MutableList<UserDetailsDB>
                            if (data.isNotEmpty()) {
                                loadUserInfo(data[0])
                            }
                        }

                        ResponseType.PROJECT_DETAILS -> {
                            val data = it.data as MutableList<ProjectDetailsDB>
                            if (data.isNotEmpty()) {
                                loadProjectDetails(data)
                            }
                        }

                        ResponseType.DELETE_USERS -> {
                            NavigationLauncher.launchActivity(mActivity, LoginActivity::class.java)
                        }

                        ResponseType.SUGGESTED_QUESTIONS -> {
                            suggestedQuestionsDBList?.clear()
                            suggestedQuestionsDBList = it.data as MutableList<SuggestedQuestionsDB>
                        }
                    }
                }
            }
        }

        viewModelGalaxy.RemoteviewResponse.observe(this) {
            when (it.type) {
                RemoteViewModelResponse.SUCCESS -> {
                    when (it.subType) {
                        RemoteResponseType.RR_DATA_RESPONSE -> {
                            val response = it.data as Response<ResponseBody>
                            response.body()?.let {
                                val messageIdFromHeader = response.headers()[MESSAGE_ID]
                                inputStreamData(it.byteStream(), true, messageIdFromHeader ?: "")
                            } ?: run {
                                linearProgress.visibility = View.GONE
                            }
                        }

                        RemoteResponseType.RR_LIKE_DISLIKE -> {

                        }

                        RemoteResponseType.RR_VISION -> {
                            val response = it.data as Response<ResponseBody>
                            val responseValue = response.body()!!.byteStream()
                            val messageIdFromHeader = response.headers()[MESSAGE_ID]
                            inputStreamData(responseValue, false, messageIdFromHeader ?: "")
                        }

                        RemoteResponseType.RR_CREATE_THREAD -> {
                            val response = it.data as Response<CreateThreadResponse>
                            response.body()?.let {
                                if (it != null) {
                                    threadID = it.threadId ?: ""
                                    createThreadResponse = it
                                }
                            } ?: run {
                            }
                        }

                        RemoteResponseType.RR_GET_THREAD -> {
                            val response = it.data as Response<ThreadsResponse>
                            fetchThreadResponse(response)
                        }

                        RemoteResponseType.RR_UPDATE_THREAD -> {
                            val response = it.data as Response<UpdateThreadResponse>
                            response.body()?.let {
                                closeNavigationDrawer()
                            } ?: run {
                            }
                        }

                        RemoteResponseType.RR_HISTORY_THREAD -> {
                            val response = it.data as Response<ChatHistoryResponse>
                            response.body()?.let {
                                streamChatHistory(it)
                            } ?: run {
                            }
                        }
                    }
                }

                RemoteViewModelResponse.FAILURE -> {
                    when (it.subType) {
                        RemoteResponseType.RR_DATA_RESPONSE -> {
                            val response = it.data as Response<ResponseBody>
                            val errorBody = response.errorBody()?.string()
                            errorBody?.let {
                                val errorResponse = viewModelCommon.parseError(it)
                                showMessage(errorResponse.detail!!)
                            }
                            networkInterruptedAction()
                        }

                        RemoteResponseType.RR_LIKE_DISLIKE -> {
                            val response = it.data as Response<ResponseBody>
                            response.body()?.let {
                                linearProgress.visibility = View.GONE
                            } ?: run {
                                linearProgress.visibility = View.GONE
                            }
                        }
                    }
                }

                RemoteViewModelResponse.ERROR -> {
                    when (it.subType) {
                        RemoteResponseType.RR_DATA_RESPONSE -> {
                            val throws = it.data as Throwable
                            networkInterruptedAction()
                        }
                    }
                }
            }
        }

        viewModel.viewResponse.observe(this) {

        }
    }

    override fun getMenuData(name: String, data: String, feature: String?) {
        userQuery(name, data, feature, null, true, "", "", null)
    }

    override fun getServiceHistory(chasisNo: String, regNumber: String) {
        userQuery("", "", Constants.SERVICE_HISTORY, null, true, chasisNo, regNumber, null)
    }

    override fun getThreadMore(
        threadId: String,
        isEdit: Boolean,
        isDelete: Boolean,
        position: Int,
        childPosition: Int,
        dataItem: MessageItem,
        tvContent: AppCompatEditText
    ) {
        dataItem.isEdit = isEdit
        threadAdapter?.notifyItemChanged(position)
    }


    /**
     * Button onClick Listener
     */
    private fun setListener() {
        drawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
                hideKeypad(drawer.windowToken)
                fetchThread()
            }

            override fun onDrawerClosed(drawerView: View) {
                hideKeypad(drawer.windowToken)
            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        })

        ibSendData.run {
            setOnTouchListener(object : View.OnTouchListener {
                var state: Boolean = false

                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if (ibSendData.tag == resources.getResourceEntryName(R.drawable.ic_mic)) {
                                if (ActivityCompat.checkSelfPermission(
                                        mActivity,
                                        Manifest.permission.RECORD_AUDIO
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    ActivityCompat.requestPermissions(
                                        mActivity,
                                        arrayOf(Manifest.permission.RECORD_AUDIO),
                                        0
                                    )
                                } else {
                                    sttManager.dialog = sttManager.showMicAnimation()
                                    if (sttManager.dialog?.isShowing == true) {
                                        sttManager.startRecording()
                                        sttManager.startTimer { data ->
                                            apiSTT(data)
                                        }
                                    }
                                    state = true
                                }
                            }
                        }

                        MotionEvent.ACTION_UP -> {
                            when (ibSendData.tag) {
                                resources.getResourceEntryName(R.drawable.ic_send) -> {
                                    setSubmitRequest(etContent)
                                }

                                resources.getResourceEntryName(R.drawable.ic_stop) -> {
                                    if (::responseBodyCall.isInitialized) {
                                        if (responseBodyCall.isExecuted) {
                                            responseBodyCall.cancel()
                                            chatbotAdapter.networkInterrupted()
                                        } else if (responseBodyCall.isCanceled) {
                                            runOnUiThread {
                                                sttManager.stopTimer()
                                                setSendButtonAction(R.drawable.ic_send)
                                                checkSendButtonAction(etContent.text.toString())
                                                linearProgress.visibility = View.GONE
                                                chatbotAdapter.networkInterrupted()
                                            }
                                        }
                                    }
                                }

                                resources.getResourceEntryName(R.drawable.ic_mic) -> {
                                    if (state) {
                                        state = false
                                        if (ActivityCompat.checkSelfPermission(
                                                mActivity,
                                                Manifest.permission.RECORD_AUDIO
                                            ) != PackageManager.PERMISSION_GRANTED
                                        ) {
                                            ActivityCompat.requestPermissions(
                                                mActivity,
                                                arrayOf(Manifest.permission.RECORD_AUDIO),
                                                0
                                            )
                                        } else {
                                            if (sttManager.stopRecording() == null) {
                                                sttManager.getBase64()?.let { apiSTT(it) }
                                            }
                                        }
                                    }
                                    hideKeypad(ibSendData.windowToken)
                                }
                            }

                            if (sttManager.dialog != null) {
                                sttManager.dialog.let { it?.dismiss() }
                                sttManager.dialog = null
                                sttManager.stopTimer()
                            }
                        }
                    }
                    return false
                }
            })
        }

        btnDownArrow.setOnClickListener {
            val lastPosition = rvChatBot.adapter?.itemCount?.minus(1)
            lastPosition?.let {
                smoothScroller.targetPosition = it
                layoutManager.startSmoothScroll(smoothScroller)
            }
        }

        imageMenu.setOnClickListener {
            onMenuImageClikc()
        }

        cvNewChat.setOnClickListener {
            isDataStream()
            { isStream ->
                if (!isStream) {
                    InstanceData.instance.androidUniqueId = CommonUtils.getAndroidID(this)
                    setListChat()
                    chatbotAdapter.resetAll()
                    if (ttsManager.mediaPlayer.isPlaying) ttsManager.mediaPlayer.stop()
                    closeNavigationDrawer()
                    linearProgress.visibility = View.GONE
                    btnDownArrow.visibility = View.GONE
                    etContent.text?.clear()
                    createThread()
                    sttManager.stopTimer()
                }
            }
        }

        ibSuggestMenu.setOnClickListener {
            isDataStream()
            { isStream ->
                if (!isStream)
                    showSuggestedDialog()
            }
        }

        ibHelpMenu.setOnClickListener {
            isDataStream()
            { isStream ->
                if (!isStream)
                    helpMenu()
            }
        }

        btnLogout.setOnClickListener {
            viewModelCommon.deleteUsers(
                userDetailsDB?.remember ?: false,
                userDetailsDB?.email ?: ""
            )
        }

        ivAttachment.setOnClickListener {
            isDataStream()
            { isStream ->
                if (!isStream) {
                    showAttachmentDialog()
                }
            }
        }

        window.decorView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            window.decorView.getWindowVisibleDisplayFrame(r)
            val height = window.decorView.height
            if (height - r.bottom > height * 0.1399) {
                if (linearProgress.visibility == View.GONE) {
                    layoutManager.stackFromEnd = true
                }
            } else {
                if (linearProgress.visibility == View.GONE) {
                    layoutManager.stackFromEnd = true
                }
            }
        }

        spVoiceList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                closeNavigationDrawer()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                voiceType = spVoiceList.selectedItem.toString()
                closeNavigationDrawer()
            }
        }

        etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                checkSendButtonAction(s.toString())
                if (s?.isNotEmpty() == true) {
                    ivAttachment.visibility = View.GONE
                } else {
                    ivAttachment.visibility = View.VISIBLE
                }
            }
        })

        checkSendButtonAction(etContent.text.toString())
    }

    private fun helpMenu() {
        btShortMenu(
            InstanceData.instance.helpMenuData,
            mActivity,
            ibHelpMenu,
            this@ChatBotActivity,
            true
        )
    }

    private fun onSampleContent() {
        layoutManager.stackFromEnd = false
        clDummyLogo.visibility = View.GONE
        etContent.text?.clear()
        val inputStream: InputStream = CommonData.yourData1.byteInputStream()
        responseDataFormat = ResponseDataFormat("", "", null)
        chatbotAdapter.setPostInit("Sample", listenerAPI)

        var position = chatbotAdapter.getListSize() - 1
        chatbotAdapter.notifyItemInserted(position)
        rvChatBot.post { rvChatBot.scrollToPosition(chatbotAdapter.getListSize() - 1) }
        (rvChatBot.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        inputStreamData(inputStream, true, "")
        inputCount = chatbotAdapter.itemCount.minus(1)

        val lastPosition = rvChatBot.adapter?.itemCount?.minus(1)
        lastPosition?.let { rvChatBot.scrollToPosition(it) }
    }

    fun setSubmitRequest(editText: AppCompatEditText) {
        if (editText.text.toString().isNotEmpty()) {
            if (!viewModelCommon.networks.isNetworkConnected()) {
                showMessage(resources.getString(R.string.network_connection_error))
                return
            }
            val inputData: String = editText.text.toString()
            if (inputData.uppercase(Locale.ROOT).equals("SAMPLE")) {
                onSampleContent()
            } else {
                userQuery(editText.text.toString(), inputData, null, null, true, "", "", null)
            }
            hideKeypad(ibSendData.windowToken)
            ttsManager.playNextAudio(false, null, -1)
        } else {
            CommonUtils.makeMeShake(findViewById(R.id.cl_sender_content), 20, 5)
            CommonUtils.setVibrate(mActivity, 300)
        }
    }

    fun setSendButtonAction(action: Int) {
        ibSendData.setImageResource(action)
        ibSendData.tag = resources.getResourceEntryName(action)
    }

    fun checkSendButtonAction(check: String) {
        if (check.isNullOrEmpty()) {
            when (ibSendData.tag) {
                resources.getResourceEntryName(R.drawable.ic_send) -> {
                    setSendButtonAction(R.drawable.ic_mic)
                }

                resources.getResourceEntryName(R.drawable.ic_stop) -> {
                    setSendButtonAction(R.drawable.ic_stop)
                }

                resources.getResourceEntryName(R.drawable.ic_mic) -> {
                    setSendButtonAction(R.drawable.ic_mic)
                }
            }
        } else {
            when (ibSendData.tag) {
                resources.getResourceEntryName(R.drawable.ic_send) -> {
                    setSendButtonAction(R.drawable.ic_send)
                }

                resources.getResourceEntryName(R.drawable.ic_stop) -> {
                    setSendButtonAction(R.drawable.ic_stop)
                }

                resources.getResourceEntryName(R.drawable.ic_mic) -> {
                    sttManager.stopTimer()
                    setSendButtonAction(R.drawable.ic_send)
                }
            }
        }
    }

    private fun setListChat() {
        layoutManager = LinearLayoutManager(this)
        rvChatBot.layoutManager = layoutManager
        rvChatBot.isNestedScrollingEnabled = true
        chatbotAdapter = ChatBotAdapter(rvChatBot, mActivity)
        rvChatBot.adapter = chatbotAdapter
        chatbotAdapter.resetAll()
        clDummyLogo.visibility = View.VISIBLE

        smoothScroller = object : LinearSmoothScroller(this@ChatBotActivity) {
            private val SPEED = 100f
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_END
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return SPEED / displayMetrics.densityDpi
            }

        }

        rvChatBot.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                } else if (dy < 0) {
                    // Scrolling up
                    btnDownArrow.visibility = View.VISIBLE
                } else if (dy > 0) {
                    // Need to check
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    btnDownArrow.visibility = View.GONE
                } else if (!recyclerView.canScrollVertically(-1)) {
                    btnDownArrow.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle!!.syncState()
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (drawerToggle != null) {
            drawerToggle!!.onConfigurationChanged(newConfig)
        }
    }

    private val listenerAPI = object : ChatBotAdapter.getOptions {
        override fun onStream() {
            val lastPosition = rvChatBot.adapter?.itemCount?.minus(1)
            lastPosition?.let {
                smoothScroller.targetPosition = it
                layoutManager.startSmoothScroll(smoothScroller)
            }
        }

        override fun onRead(position: Int, data: String, myData: ChatBotData) {
            if (!ttsManager.mediaPlayer.isPlaying) {
                ttsManager.setTTSBegin(data)
                apiTTS(myData, position)
                smoothScroll(rvChatBot, position, 500)
            } else {
                ttsManager.playNextAudio(false, myData, position)
            }
        }

        override fun onCopy(data: String) {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", data)
            clipboardManager.setPrimaryClip(clipData)
        }

        override fun onTranslate(view: View) {
            btShortMenu(
                InstanceData.instance.translateData,
                mActivity,
                view,
                this@ChatBotActivity,
                false
            )
        }

        override fun onEvaluate(data: String) {
            userQuery(
                QUESTION_EVALUATE,
                data,
                Constants.EVALUATE,
                null,
                true,
                "",
                "",
                null
            )
        }

        override fun onInteractively(data: String) {
            userQuery(
                QUESTION_EXPLAIN_INTERACTIVELY,
                data,
                Constants.EXPLAIN_INTERACTIVELY,
                null,
                true,
                "",
                "",
                null
            )
        }

        override fun onVisualizer(data: String) {
            val bundle = Bundle()
            bundle.putString(VISUALIZER_DATA, data)
            if (data.contains(YOUTUBE)) {
                NavigationLauncher.launchActivityBundleStack(
                    mActivity, YouTubeActivity::class.java, bundle
                )
            } else {
                NavigationLauncher.launchActivityBundleStack(
                    mActivity, VisualizerActivity::class.java, bundle
                )
            }
        }

        override fun onMore(view: View) {
            btShortMenu(
                InstanceData.instance.moreData,
                mActivity,
                view,
                this@ChatBotActivity, false
            )
        }

        override fun previousPlaying(position: Int, myData: ChatBotData) {
            try {
                ttsManager.mediaPlayer.stop()
            } catch (e: Exception) {
                Logger.e("Exception", e.message.toString())
            }
        }

        override fun imageZoom(url: String?, bitmap: Bitmap?) {
            if (!viewModelCommon.networks.isNetworkConnected()) {
                showMessage(resources.getString(R.string.network_connection_error))
                return
            }

            if (!url.isNullOrEmpty()) {
                val intent = Intent(this@ChatBotActivity, ImageZoomActivity::class.java)
                intent.putExtra(GlobalBundle.IMAGE_URL, url)
                getResultLauncher.launch(intent)
            }
        }

        override fun hyperLinks(url: String) {

        }

        override fun likeDisLike(messageId: String, like: Boolean, dislike: Boolean) {
            val likeVal = if (like) 1 else 0
            val disLikeVal = if (dislike) 1 else 0
            likeDisLikeRequest =
                LikeDisLikeRequest(projectDetails?.memberID ?: "", likeVal, disLikeVal, messageId)
            userQuery(
                Constants.DISLIKE_QUESTION,
                Constants.DISLIKE_INPUT,
                null,
                null,
                true,
                "",
                "",
                likeDisLikeRequest
            )
        }
    }

    private var getResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {

            }
        }


    /**
     *
     * Display the suggested questions on the dialog
     */
    private fun showSuggestedDialog() {
        if (!suggestedQuestionsDBList.isNullOrEmpty()) {
            val dialog = Dialog(mActivity)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_suggested_layout)
            val recycler: RecyclerView = dialog.findViewById(R.id.recycler_suggest)
            val layoutManager = LinearLayoutManager(dialog.context)
            recycler.layoutManager = layoutManager
            val suggestedAdapter =
                SuggestedAdapter(suggestedQuestionsDBList!!, object : SuggestedAdapter.getOptions {
                    override fun onRead(data: String, displayName: String) {
                        etContent.setText(data)
                        etContent.setSelection(etContent.text?.length ?: 0)
                        dialog.dismiss()
                    }

                })
            recycler.adapter = suggestedAdapter
            dialog.show()
        } else
            showMessage(resources.getString(R.string.no_record_found))


    }


    private fun showAttachmentDialog() {
        val dialog = Dialog(mActivity)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_custom_layout)

        val yesBtn: Button = dialog.findViewById(R.id.btn_camera)
        yesBtn.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CAMERA), 0
                )
            } else {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraActivityResultLauncher.launch(takePictureIntent)
            }
            dialog.dismiss()
        }

        val noBtn: Button = dialog.findViewById(R.id.btn_Gallery)
        noBtn.setOnClickListener {
            storagePermission()
            dialog.dismiss()
        }
        dialog.show()
    }

    private var cameraActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get(GET_DATA) as Bitmap
                ResponseDataFormat("", "", imageBitmap)
                userQuery(VISION, "", null, imageBitmap, true, "", "", null)
                val stringUrl = encodeImage(imageBitmap)
            }
        }

    private var imageActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.data
                val imageStream = data?.let { contentResolver.openInputStream(it) }
                val imageBitmap = BitmapFactory.decodeStream(imageStream)
                userQuery(VISION, "", null, imageBitmap, true, "", "", null)
                val stringUrl = encodeImage(imageBitmap)
            }
        }

    private fun encodeImage(bm: Bitmap): String? {
        val stream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val b = stream.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun userQuery(
        questions: String,
        input: String,
        inputFeature: String?,
        bitmap: Bitmap?,
        state: Boolean,
        chassisNo: String,
        registrationNo: String,
        likeDisLikeRequest: LikeDisLikeRequest?
    ) {
        if (isLikeDisLikeClick(likeDisLikeRequest))
            layoutManager.stackFromEnd = false
        if (!viewModelCommon.networks.isNetworkConnected()) {
            showMessage(resources.getString(R.string.network_connection_error))
            return
        }
        if (bitmap == null) {
            if (!chassisNo.isNullOrEmpty() || !registrationNo.isNullOrEmpty()) {
                var serviceHistory: String = ""
                if (!chassisNo.isNullOrEmpty()) {
                    if (!registrationNo.isNullOrEmpty())
                        serviceHistory = "Chassis No: $chassisNo,"
                    else
                        serviceHistory = "Chassis No: $chassisNo"
                }
                if (!registrationNo.isNullOrEmpty()) {
                    serviceHistory += "Registration No: $registrationNo"
                }
                chatbotAdapter.setPostInit(serviceHistory, listenerAPI)
            } else if (likeDisLikeRequest != null) {
                if (likeDisLikeRequest.dislike == 1)
                    chatbotAdapter.setPostInit(questions, listenerAPI)
            } else {
                chatbotAdapter.setPostInit(questions, listenerAPI)
            }
            if (isLikeDisLikeClick(likeDisLikeRequest))
                moveLastItem()
            responseDataFormat = ResponseDataFormat("", "", null)
            apiHnSChatStream(input, inputFeature, chassisNo, registrationNo, likeDisLikeRequest)

        } else {
            chatbotAdapter.setPostImageInit(questions, bitmap, listenerAPI)
            moveLastItem()
            responseDataFormat = ResponseDataFormat("", "", bitmap)
            apiVisionChat("", bitmap)
        }
        if (isLikeDisLikeClick(likeDisLikeRequest)) {
            inputCount = chatbotAdapter.itemCount.minus(1)
            val lastPosition = rvChatBot.adapter?.itemCount?.minus(1)
            lastPosition?.let {
                rvChatBot.scrollToPosition(it)
            }
        }
    }

    private fun moveLastItem() {
        var position = chatbotAdapter.getListSize() - 1
        chatbotAdapter.notifyItemInserted(position)
        rvChatBot.post {
            val lastPosition = rvChatBot.adapter?.itemCount?.minus(1)
            lastPosition?.let { rvChatBot.scrollToPosition(it) }
            (rvChatBot.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    private fun apiHnSChatStream(
        text: String,
        inputFeature: String?,
        chasisNo: String,
        registrationNo: String,
        likeDisLikeRequest: LikeDisLikeRequest?
    ) {
        clDummyLogo.visibility = View.GONE
        etContent.setText("")
        linearProgress.visibility = View.VISIBLE
        if (isLikeDisLikeClick(likeDisLikeRequest))
            setSendButtonAction(R.drawable.ic_stop)
        val requestBody = RequestBodyModel(
            text,
            inputFeature,
            projectDetails?.projectID ?: "",
            threadID ?: "thread_TmjQqRNmgs8LTJoqsCieYSlL",
            projectDetails?.memberID ?: "",
        )


        if (!chasisNo.isNullOrEmpty() || !registrationNo.isNullOrEmpty()) {
            val requestBodyServiceHistory = ServiceHistoryRequest(
                registrationNo,
                InstanceData.instance.listTypeData[InstanceData.instance.selectTypePosition].action,
                chasisNo,
                "",

                )
            isStreaning = true
            viewModelGalaxy.getServiceHistory(requestBodyServiceHistory) { responseCall ->
                responseBodyCall = responseCall
            }
        } else if (likeDisLikeRequest != null) {
            viewModelGalaxy.getLikeDisLike(likeDisLikeRequest) { responseCall ->
                responseLikeDisLike = responseCall
            }
            if (likeDisLikeRequest.dislike == 1) {
                isStreaning = true

                viewModelGalaxy.getDataStream(requestBody) { responseCall ->
                    responseBodyCall = responseCall
                }
            }
        } else {
            isStreaning = true
            viewModelGalaxy.getDataStream(requestBody) { responseCall ->
                responseBodyCall = responseCall
            }
        }
    }

    private fun createThread() {
        val createThreadRequest = CreateThreadRequest(
            projectDetails?.memberID ?: "", projectDetails?.projectID ?: ""
        )
        viewModelGalaxy.createThread(createThreadRequest)
    }

    private fun fetchThread() {
        val createThreadRequest = CreateThreadRequest(
            projectDetails?.memberID ?: "", projectDetails?.projectID ?: ""
        )
        viewModelGalaxy.getThreads(createThreadRequest)
    }

    private fun fetchThreadResponse(response: Response<ThreadsResponse>) {
        response.body()?.let {
            if (!it.message.isNullOrEmpty()) {
                runOnUiThread {
                    var orderList = CommonUtils.orderChatHistoryThreads(it.message)
                    val groupedItems = groupItemsByDate(orderList)
                    if (groupedItems.isNotEmpty()) {
                        rvChatThread.layoutManager =
                            LinearLayoutManager(this@ChatBotActivity)
                        threadAdapter = ThreadAdapter(groupedItems, this@ChatBotActivity,
                            object : ParentCallBackListener {
                                override fun parentClickListener(threadID_: String) {
                                    isDataStream()
                                    { isStream ->
                                        if (!isStream) {
                                            threadID = threadID_
                                            chatHistory(threadID!!)
                                        }
                                    }
                                }

                                override fun more(
                                    threadID: String,
                                    view: View,
                                    position: Int,
                                    childPosition: Int,
                                    dataItem: MessageItem,
                                    tvContent: AppCompatEditText,
                                    threadChildAdapter: ThreadChildAdapter?,
                                ) {
                                    isDataStream()
                                    { isStream ->
                                        if (!isStream) {
                                            btnThreadMore(
                                                arrayListOf(
                                                    MenuAction(
                                                        resources.getString(R.string.chat_history_rename),
                                                        "",
                                                        "",
                                                        null
                                                    )
                                                ),
                                                mActivity,
                                                view,
                                                this@ChatBotActivity,
                                                threadID,
                                                position,
                                                childPosition,
                                                dataItem,
                                                tvContent,
                                                threadChildAdapter
                                            )
                                        }
                                    }
                                }

                                override fun updateThread(
                                    threadID: String,
                                    heading: String
                                ) {

                                    isDataStream()
                                    { isStream ->
                                        if (!isStream) {
                                            var updateThreadRequest = UpdateThreadRequest()
                                            updateThreadRequest.threadId = threadID
                                            updateThreadRequest.heading = heading
                                            updateThreadRequest.memberId =
                                                projectDetails?.memberID ?: ""
                                            updateThreadRequest.projectId =
                                                projectDetails?.projectID ?: ""

                                            viewModelGalaxy.updateThread(updateThreadRequest)
                                        }
                                    }
                                }
                            })
                        rvChatThread.adapter = threadAdapter
                        threadAdapter?.notifyDataSetChanged()
                    }
                    viewModelCommon.hideProgress()
                }
            }
        } ?: run {
            viewModelCommon.hideProgress()
        }
    }

    private fun apiVisionChat(text: String, bitmap: Bitmap) {
        clDummyLogo.visibility = View.GONE
        etContent.setText("")
        linearProgress.visibility = View.VISIBLE
        val requestBody = encodeImage(bitmap)?.let {
            RequestImageModel(
                it,
                projectDetails?.memberID ?: "",
                projectDetails?.projectID ?: "",
                Constants.VISION_STREAM,
                threadID ?: ""
            )
        }
        isStreaning = true
        viewModelGalaxy.getVisionChat(requestBody)
    }

    var isStreaning: Boolean = false
    fun inputStreamData(
        responsevalue: InputStream,
        streamState: Boolean,
        messageId: String,
    ) {
        val thread = Thread {
            try {
                try {
                    var someString: String?
                    val buf = ByteArray(4096)
                    var size = 0
                    while (responsevalue.read(buf).also { size = it } != -1) {
                        someString = String(buf, 0, size)

                        if (streamState) {
                            responseDataFormat.moreActionState = false
                            responseDataFormat.actualResponse += someString
                            val responseDataAppend =
                                FormattingUtils.convertMarkDown(responseDataFormat.actualResponse)
                            responseDataFormat.copyData = responseDataFormat.actualResponse
                            responseDataFormat.htmlData = responseDataAppend.htmlData
                            responseDataFormat.htmlStream = responseDataAppend.htmlData
                            finalSomeString = responseDataFormat.htmlData
                            finalSomeString = someString

                            runOnUiThread {
                                try {
                                    userContentListItems =
                                        chatbotAdapter.setPostStream(
                                            inputCount,
                                            responseDataFormat,
                                            messageId,
                                            false,
                                            false,
                                        )
                                    chatbotAdapter.notifyItemChanged(inputCount)
                                    rvChatBot.post {
                                        val lastPosition =
                                            rvChatBot.adapter?.itemCount?.minus(1)
                                        lastPosition?.let {
                                            if (it > 0) {
                                                smoothScroller.targetPosition = it
                                                layoutManager.startSmoothScroll(smoothScroller)
                                            } else {
                                                if (chatbotAdapter.itemCount == 1) {
                                                    smoothScroller.targetPosition = 0
                                                    layoutManager.startSmoothScroll(
                                                        smoothScroller
                                                    )
                                                }
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                }
                            }
                        } else {
                            val responseBodyModel: ResponseBodyModel = Gson().fromJson(
                                someString,
                                ResponseBodyModel::class.java
                            )
                            responseDataFormat =
                                FormattingUtils.convertMarkDown(responseBodyModel.response!!)
                            responseDataFormat.actualResponse = responseBodyModel.response
                            responseDataFormat.moreActionState = false
                            finalSomeString = responseDataFormat.htmlData

                            runOnUiThread {
                                userContentListItems =
                                    chatbotAdapter.setPost(inputCount, responseDataFormat)
                                chatbotAdapter.notifyItemChanged(inputCount)
                                val lastPosition = rvChatBot.adapter?.itemCount?.minus(1)
                                lastPosition?.let {
                                    if (it > 0) {
                                        smoothScroller.targetPosition = it
                                        layoutManager.startSmoothScroll(smoothScroller)
                                    }

                                }
                            }
                        }
                    }
                    runOnUiThread {
                        isStreaning = false

                        setSendButtonAction(R.drawable.ic_send)
                        checkSendButtonAction(etContent.text.toString())
                        linearProgress.visibility = View.GONE
                        responseDataFormat.moreActionState = true
                        userContentListItems =
                            chatbotAdapter.setPostStream(
                                inputCount,
                                responseDataFormat,
                                messageId,
                                likeDisLikeRequest?.like == 1,
                                likeDisLikeRequest?.dislike == 1,
                            )
                        chatbotAdapter.notifyItemChanged(inputCount)
                        val lastPosition = rvChatBot.adapter?.itemCount?.minus(1)
                        lastPosition?.let {
                            if (it > 0) {
                                smoothScroller.targetPosition = it
                                layoutManager.startSmoothScroll(smoothScroller)
                            }

                        }
                        likeDisLikeRequest = null
                    }
                } catch (e: IOException) {
                    e.message?.let { refreshItemsError(it) }
                    if (::responseBodyCall.isInitialized) {
                        responseBodyCall = throw IllegalStateException("API call returned null")
                    }
                    isStreaning = false
                }
            } catch (e: Exception) {
                e.message?.let { refreshItemsError(it) }
                isStreaning = false
            }
        }
        thread.start()
    }


    fun refreshItemsError(errorMsg: String) {
        runOnUiThread {
            setSendButtonAction(R.drawable.ic_send)
            checkSendButtonAction(etContent.text.toString())
            linearProgress.visibility = View.GONE
            responseDataFormat.moreActionState = true
            userContentListItems = chatbotAdapter.setPostStream(
                inputCount,
                responseDataFormat,
                "",
                false,
                false
            )
            chatbotAdapter.notifyItemChanged(inputCount)
            val lastPosition = rvChatBot.adapter?.itemCount?.minus(1)
            lastPosition?.let {
                smoothScroller.targetPosition = it
                layoutManager.startSmoothScroll(smoothScroller)
            }
        }
    }

    /**
     *
     * Check storage permission
     */
    private fun storagePermission() {
        val readImagePermission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_IMAGES
            else
                Manifest.permission.READ_EXTERNAL_STORAGE
        checkPermission(
            readImagePermission,
            STORAGE_PERMISSION_CODE
        )
    }


    /**
     *
     * Permission Status
     */
    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this@ChatBotActivity, permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            // Requesting the permission
            ActivityCompat.requestPermissions(
                this@ChatBotActivity, arrayOf(permission), requestCode
            )
        } else {
            launchGalleryIntent()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchGalleryIntent()
            } else {
                showMessage(resources.getString(R.string.storage_permission_denied))
            }
        }
    }

    /**
     *
     * Pick image from device gallery
     */
    private fun launchGalleryIntent() {
        val galleryIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        imageActivityResultLauncher.launch(galleryIntent)
    }

    fun stopOperationMessage(view: View) {
        if (linearProgress.visibility == View.VISIBLE) {
            showMessage("Please stop operation")

        }
    }

    /**
     *
     * Fetched User information from the data base
     */
    private fun loadUserInfo(userDetailsDB: UserDetailsDB?) {
        this.userDetailsDB = userDetailsDB
        if (!userDetailsDB?.profilePhoto.isNullOrEmpty()) {
            Glide.with(this)
                .asBitmap()
                .load(EndPoints.ASSET_BASE_URL + userDetailsDB?.profilePhoto)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        profileImage = resource
                        ivProfileImage.setImageBitmap(resource)
                        chatbotAdapter.setProfileImage(profileImage!!)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Handle the placeholder if needed
                    }
                })
        }




        if (!userDetailsDB?.firstName.isNullOrEmpty())
            tvUserName.text = resources.getString(R.string.welcome) + " " + userDetailsDB?.firstName

        tvLoginDate.text = CommonUtils.convertMillisecondsToDate(
            userDetailsDB?.loginTime!!, TimeFormats.FORMAT_DD_MMM_YYYY
        )
        viewModelCommon.getProjectDetails()
    }

    /**
     *
     * Fetched project information from the data base
     */
    private fun loadProjectDetails(data: MutableList<ProjectDetailsDB>) {
        val projectDetailsDB: ProjectDetailsDB = data[0]
        this.projectDetails = projectDetailsDB
        tvNameProduct.text = projectDetailsDB.assistantName ?: "-"
        tvProfileID.text = projectDetailsDB.profileID ?: "-"
        loadProductSpinnerData(data)
        loadProductImage(projectDetailsDB)
        createThread()
    }

    /**
     *
     * Load product list on the spinner
     */
    private fun loadProductSpinnerData(data: MutableList<ProjectDetailsDB>) {
        if (data.isNotEmpty()) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
            spProduct.adapter = adapter
            spProduct.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    suggestedQuestionsDBList?.clear()
                    projectID = data[position].projectID
                    viewModelCommon.getSuggestedQuestionsDB(projectID ?: "")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }

    }

    /**
     *
     * Load product Image on glide
     */
    private fun loadProductImage(projectDetailsDB: ProjectDetailsDB) {
        if (!projectDetailsDB?.projectImage.isNullOrEmpty()) {
            Glide.with(this)
                .asBitmap()
                .load(EndPoints.ASSET_BASE_URL + projectDetailsDB?.projectImage)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        ivProjectImage.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Handle the placeholder if needed
                    }
                })
        }
    }

    private var threadAdapter: ThreadAdapter? = null
    private fun groupItemsByDate(items: List<MessageItem>): Map<String, List<MessageItem>> {
        return items.groupBy {
            val inputFormat = SimpleDateFormat(
                TimeFormats.FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSSSSS,
                Locale.getDefault()
            )
            val date = inputFormat.parse(it.modified)
            val outputFormat =
                SimpleDateFormat(TimeFormats.FORMAT_DD_MM_YYYY, Locale.getDefault())
            when {
                CommonUtils.isSameDay(
                    date!!,
                    CommonUtils.chatGroupDate(0)
                ) -> resources.getString(R.string.today)

                CommonUtils.isSameDay(
                    date,
                    CommonUtils.chatGroupDate(-1)
                ) -> resources.getString(R.string.yesterday)

                else -> resources.getString(R.string.previous_30_days)
            }
        }
    }

    fun chatHistory(threadID: String) {
        closeNavigationDrawer()

        responseDataFormat = ResponseDataFormat("", "", null)
        val chatHistoryRequest = ChatHistoryRequest(
            projectDetails?.memberID ?: "",
            threadID,
            projectDetails?.projectID ?: ""
        )

        viewModelGalaxy.getChatHistory(chatHistoryRequest)
    }

    private fun streamChatHistory(chatHistoryResponse: ChatHistoryResponse) {
        runOnUiThread {
            try {
                chatbotAdapter.resetAll()
                if (!chatHistoryResponse.message.isNullOrEmpty()) {
                    clDummyLogo.visibility = View.GONE
                    for ((position, item) in CommonUtils.orderChatHistory(chatHistoryResponse.message)!!
                        .withIndex()) {
                        chatbotAdapter.setPostInit(item?.prompt!!, listenerAPI)

                        responseDataFormat.moreActionState = false
                        responseDataFormat.actualResponse = item.response ?: ""

                        val responseDataAppend =
                            FormattingUtils.convertMarkDown(responseDataFormat.actualResponse)

                        responseDataFormat.copyData = responseDataFormat.actualResponse
                        responseDataFormat.htmlData = responseDataAppend.htmlData
                        responseDataFormat.htmlStream = responseDataAppend.htmlData
                        finalSomeString = responseDataFormat.htmlData
                        responseDataFormat.moreActionState = true

                        userContentListItems =
                            chatbotAdapter.setPostStream(
                                position,
                                responseDataFormat,
                                item.messageId ?: "",
                                item.like,
                                item.dislike
                            )

                    }
                    val position = chatbotAdapter.getListSize() - 1
                    chatbotAdapter.notifyItemInserted(position)
                    rvChatBot.post { rvChatBot.scrollToPosition(chatbotAdapter.getListSize() - 1) }
                    (rvChatBot.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
                        false
                } else {
                    clDummyLogo.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun isDataStream(callback: (Boolean) -> Unit) {
        if (isStreaning) {
            callback(true)
            showMessage("Please stop operations")
        } else {
            callback(false)
        }
    }

    private fun isLikeDisLikeClick(likeDisLikeRequest: LikeDisLikeRequest?): Boolean {
        if (likeDisLikeRequest == null) {
            return true
        } else if (likeDisLikeRequest.dislike == 1) {
            return true
        } else if (likeDisLikeRequest.like == 0) {
            return false
        } else if (likeDisLikeRequest.like == 1) {
            return false
        } else if (likeDisLikeRequest.dislike == 0) {
            return false
        } else {
            return true
        }
    }

    private fun networkInterruptedAction() {
        isStreaning = false
        runOnUiThread {
            setSendButtonAction(R.drawable.ic_send)
            checkSendButtonAction(etContent.text.toString())
            linearProgress.visibility = View.GONE
            chatbotAdapter.networkInterrupted()
        }
    }


    private val ttsListener = object : TTSManager.TTSListener {
        override fun recallAPI(myData: ChatBotData?, position: Int) {
            apiTTS(myData!!, position)
        }

        override fun resetAllAudio() {
            chatbotAdapter.resetAllAudio()
            chatbotAdapter?.notifyDataSetChanged()
        }

        override fun resetAllAudioProgress() {
            chatbotAdapter.resetAllAudio()
            chatbotAdapter.resetAllAudioProgress()
        }

        override fun notifyItemChanged(position: Int, myData: ChatBotData?) {
            chatbotAdapter.notifyItemChanged(position, myData)
        }

    }

    private fun apiTTS(myData: ChatBotData, position: Int) {
        speechDataState = false
        if (ttsManager.speechData.isEmpty() && !ttsManager.mediaPlayer.isPlaying) {
            return
        }
        if (ttsManager.speechData.isNotEmpty()) {
            val speechTTS = ttsManager.speechData[0]

            val requestBody = RequestTTSModel(
                speechTTS.speechContent,
                voiceType!!.lowercase(),
                projectDetails?.memberID ?: "",
                projectDetails?.projectID ?: ""
            )

            viewModelGalaxy.getTTS(requestBody) { byteArray ->
                speechTTS.audioFiles = byteArray
                ttsManager.speechData[0] = speechTTS
                if (!ttsManager.mediaPlayer.isPlaying) {
                    ttsManager.playNextAudio(true, myData, position)
                }
            }
        }
    }

    private fun apiSTT(myData: String) {
        if (myData.isNotEmpty()) {
            val requestBody = RequestSTTModel(
                projectDetails?.projectID ?: "",
                projectDetails?.memberID ?: "",
                myData,
            )
            viewModelGalaxy.getSTT(requestBody) { inputData ->
                if (inputData.equals("false", true))
                    etContent.text?.clear()
                else
                    etContent.setText(inputData)
            }
        }
    }

    private fun backPressHandleCallBack() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    finish()
                } else {
                    showMessage(resources.getString(R.string.press_back))
                    backPressedTime = System.currentTimeMillis()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        sttManager.stopTimer()
        ttsManager.mediaPlayer.release()
        ttsManager.startUpdatingProgress(false, null, -1)
    }
}

