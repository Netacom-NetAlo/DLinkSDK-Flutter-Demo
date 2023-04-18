package com.example.flutter_sdk

import android.os.Bundle
import androidx.annotation.NonNull
import com.asia.sdkbase.logger.Logger
import com.asia.sdkcore.config.EndPoint
import com.asia.sdkcore.define.ErrorCodeDefine
import com.asia.sdkcore.define.GalleryType
import com.asia.sdkcore.define.NavigationDef
import com.asia.sdkcore.define.SdkCodeDefine
import com.asia.sdkcore.entity.ui.local.LocalFileModel
import com.asia.sdkcore.entity.ui.user.NeUser
import com.asia.sdkcore.network.model.response.SettingResponse
import com.asia.sdkcore.sdk.SdkClickNotification
import com.asia.sdkcore.sdk.SdkStringSend
import com.asia.sdkcore.util.CallbackResult
import com.asia.sdkui.ui.sdk.NetAloSDK
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity : FlutterActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var currentResult: MethodChannel.Result? = null
    var netAloSDKEnvironment = "" // dev,pro
    private val receiveChannelName = "vn.netacom.demo/flutter_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Click notification open chat
        intent.extras?.getParcelable<NeUser>(NavigationDef.ARGUMENT_NEUSER)?.apply {
            val neUser = this
            Logger.e("MainActivity:neUser===$this")

            CoroutineScope(Dispatchers.IO).launch {
                delay(TimeUnit.SECONDS.toMillis(1))
                NetAloSDK.openNetAloSDK(
                    this@MainActivity,
                    false,
                    null,
                    NeUser(
                        id = neUser.id,
                        token = neUser.token ?: "",
                        username = neUser.username ?: ""
                    )
                )
            }
        }

        NetAloSDK.checkGroupExist(0) { isExist ->
            Logger.e("isExist==$isExist")
        }
        launch {
            try {
                NetAloSDK.netAloEvent?.receive<ArrayList<LocalFileModel>>()?.collect { listPhoto ->
                    Logger.e("SELECT_PHOTO_VIDEO==$listPhoto")
                    val photoPaths = listPhoto.map { it.filePath }
                    currentResult?.success(photoPaths)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        launch {
            try {
                NetAloSDK.netAloEvent?.receive<SdkStringSend>()?.collect { data ->
                    Logger.e("String:data==${data.data}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        launch {
            try {
                NetAloSDK.netAloEvent?.receive<LocalFileModel>()?.collect { document ->
                    Logger.e("SELECT_FILE==$document")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        launch {
            try {
                NetAloSDK.netAloEvent?.receive<SdkClickNotification>()?.collect { sdkClickNoti ->
                    Logger.e("SdkClickNotification==$sdkClickNoti")
                    withContext(Dispatchers.Main) {

                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //Event SDK
        launch {
            try {
                NetAloSDK.netAloEvent?.receive<Int>()?.collect { errorEvent ->
                    Logger.e("Event:==$errorEvent")
                    when (errorEvent) {
                        ErrorCodeDefine.ERRORCODE_FAILED_VALUE -> {
                            Logger.e("Event:Socket error")
                        }
                        ErrorCodeDefine.ERRORCODE_EXPIRED_VALUE -> {
                            Logger.e("Event:Session expired")
                        }
                        SdkCodeDefine.SDK_LOGOUT -> {
                            Logger.e("SdkCodeDefine:SDK_LOGOUT")
                        }
                        SdkCodeDefine.SDK_EXIT -> {
                            Logger.e("SdkCodeDefine:Login SDK_EXIT")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        GeneratedPluginRegistrant.registerWith(flutterEngine);
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, receiveChannelName).setMethodCallHandler { call, result ->
            currentResult = result
            when (call.method) {
                "openListConversation" -> openChatConversation(call, result)
                "openChatWithUser" -> openChatWithUser(call, result)
                "openCallWithUser" -> openChatWithUser(call, result)
                "logoutSDK" -> NetAloSDK.logOut()
                "setSdkUser" -> setNetAloUser(call, result)
                "pickImages" -> openImagePicker(call, result)
                "blockUser" -> blockUser(call, result, isBlock = true)
                "unBlockUser" -> blockUser(call, result, isBlock = false)
                "checkPermissionCall" -> {}
                "setDomainLoadAvatarNetAloSdk" -> setDomainLoadAvatarNetAloSdk(call, result)
                "sendMessage" -> sendMessage(call, result)
                "onFirebaseReceivedChat" -> onFirebaseReceivedChat(call)
                "onFirebaseReceivedCall" -> onFirebaseReceivedCall(call)
                "closeNetAloChat" -> closeNetAloChat()
            }
        }
    }

    private fun sendMessage(call: MethodCall, result: MethodChannel.Result) {
        try {
            val receiver: HashMap<String, Any> = call.argument("receiver")!!
            val message: String = call.argument("message") as? String ?: ""
            NetAloSDK.sendMessage(
                text = message,
                partnerUid = receiver["id"] as? Long ?: 0,
                callbackSuccess = { neSubMessages, tempMessageId, neMessage ->
                    try {
                        Logger.e("onSendMessage = $neMessage neSubMessages=$neSubMessages tempMessageId$tempMessageId")
                        runBlocking(Dispatchers.Main) {
                            currentResult?.success(true)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                callbackError = { error, tempMessageId ->
                    try {
                        Logger.e("onSendMessage:callbackError = $error")
                        runBlocking(Dispatchers.Main) {
                            currentResult?.success(false)
                        }
                    } catch (e: Exception) {
                    }
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openImagePicker(call: MethodCall, result: MethodChannel.Result) {
        val type = when (call.argument("type") as? Int ?: 1) {
            0 -> GalleryType.GALLERY_ALL
            1 -> GalleryType.GALLERY_PHOTO
            2 -> GalleryType.GALLERY_VIDEO
            else -> GalleryType.GALLERY_ALL
        }
        NetAloSDK.openGallery(
            context = this,
            maxSelections = call.argument("maxImages") as? Int ?: 1,
            autoDismissOnMaxSelections = call.argument("autoDismissOnMaxSelections") ?: true,
            galleryType = type
        )
    }

    private fun blockUser(call: MethodCall, result: MethodChannel.Result, isBlock: Boolean) {
        NetAloSDK.blockUser(
            userId = call.arguments as? Long ?: 0,
            isBlock = isBlock,
            callbackResult = object : CallbackResult<Boolean> {
                override fun callBackError(error: String?) {
                    Logger.e("blockUserError: $error")
                    result.success(false)
                }

                override fun callBackSuccess(isSuccess: Boolean) {
                    Logger.e("blockUserSuccess: $isSuccess")
                    result.success(isSuccess)
                }
            }
        )
    }

    private fun setDomainLoadAvatarNetAloSdk(call: MethodCall, result: MethodChannel.Result) {
        NetAloSDK.initSetting(
            settingResponse = SettingResponse(
                apiEndpoint = EndPoint.URL_API,//string
                cdnEndpoint = EndPoint.URL_CDN,
                cdnEndpointSdk = call.arguments as? String ?: "",
                chatEndpoint = EndPoint.URL_SOCKET
            )
        )
        result.success(true)
    }

    private fun setNetAloUser(call: MethodCall, result: MethodChannel.Result) {
        NetAloSDK.setNetAloUser(
            context = this,
            NeUser(
                id = call.argument("id") as? Long ?: 0,
                token = call.argument("token") as? String ?: "",
                username = call.argument("username") ?: "",
                avatar = call.argument("avatar") as? String ?: ""
            )
        )
        result.success(true)
    }

    private fun openChatConversation(call: MethodCall, result: MethodChannel.Result) {
        NetAloSDK.openNetAloSDK(this)
        result.success(true)
    }

    private fun openChatWithUser(call: MethodCall, result: MethodChannel.Result) {
        NetAloSDK.openNetAloSDK(
            this,
            false,
            null,
            NeUser(
                id = call.argument("id") as? Long ?: 0,
                token = call.argument("token") as? String ?: "",
                username = call.argument("username") ?: "",
                avatar = call.argument("avatar") as? String ?: ""
            )
        )
        result.success(true)
    }
    private fun onFirebaseReceivedChat(call: MethodCall) {
        val fcmPayLoad = call.argument("fcmPayLoad") as? String ?: ""
        NetAloSDK.onFirebaseReceivedChat(context = this, fcmPayload = fcmPayLoad)
    }

    private fun onFirebaseReceivedCall(call: MethodCall) {
        val fcmPayLoad = call.argument("fcmPayLoad") as? String ?: ""
        NetAloSDK.onFirebaseReceivedCall(context = this, fcmPayload = fcmPayLoad)
    }

    private fun closeNetAloChat() {
        NetAloSDK.exit()
    }

}
