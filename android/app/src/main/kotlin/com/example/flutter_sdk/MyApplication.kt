package com.example.flutter_sdk

import android.content.Context
import androidx.work.Configuration
import com.asia.sdkcore.entity.ui.theme.NeTheme
import com.asia.sdkcore.sdk.AccountKey
import com.asia.sdkcore.sdk.AppID
import com.asia.sdkcore.sdk.AppKey
import com.asia.sdkcore.sdk.SdkConfig
import com.asia.sdkui.ui.sdk.NetAloSDK
import com.asia.sdkui.ui.sdk.NetAloSdkCore
import dagger.hilt.android.HiltAndroidApp
import io.flutter.app.FlutterApplication
import io.realm.Realm
import kotlinx.coroutines.ObsoleteCoroutinesApi
import javax.inject.Inject

@ObsoleteCoroutinesApi
@HiltAndroidApp
class MyApplication : FlutterApplication(), Configuration.Provider {

    @Inject
    lateinit var netAloSdkCore: NetAloSdkCore

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(netAloSdkCore.workerFactory)
            .build()

    private val sdkConfig = SdkConfig(
        appId = AppID.VNDIRECT,
        appKey = AppKey.VNDIRECT_PRO,
        accountKey = AccountKey.VNDIRECT_PRO,
        isSyncContact = false,
        hidePhone = false,
        hideCreateGroup = false,
        hideAddInfoInChat = false,
        hideInfoInChat = false,
        hideCallInChat = false,
        classMainActivity = MainActivity::class.java.name
    )

    private val sdkTheme = NeTheme(
        mainColor = "#FF9500",
        subColorLight = "#F9D9C9",
        subColorDark = "#ef5222",
        toolbarDrawable = "#FF9500"
    )

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Realm.init(this)
    }

    override fun onCreate() {
        super.onCreate()
        NetAloSDK.initNetAloSDK(
            context = this,
            netAloSdkCore = netAloSdkCore,
            sdkConfig = sdkConfig,
            neTheme = sdkTheme
        )
    }
}