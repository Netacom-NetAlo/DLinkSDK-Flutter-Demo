//
//  BuildConfig.swift
//  Runner
//
//  Created by Hieu Bui Van  on 22/12/2021.
//

import NetAloLite
import NetAloFull

struct BuildConfig {
    static var config = NetaloConfiguration(
        enviroment: .production,
        appId: 17,
        appKey: "B2D89AC8B8ECF",
        accountKey: "adminkey",
        appGroupIdentifier: "group.vn.com.vndirect.stockchat",
        storeUrl: URL(string: "https://apps.apple.com/vn/app/vndirect/id1594533471")!,
        analytics: [],
        featureConfig: FeatureConfig(
            user: FeatureConfig.UserConfig(
                forceUpdateProfile: false,
                allowCustomUsername: false,
                allowCustomProfile: false,
                allowCustomAlert: false,
                allowAddContact: false,
                allowBlockContact: false,
                allowSetUserProfileUrl: false,
                allowEnableLocationFeature: false,
                allowTrackingUsingSDK: true,
                isHiddenEditProfile: true,
                allowAddNewContact: false,
                allowEditContact: false
            ),
            chat: FeatureConfig.ChatConfig(
                isVideoCallEnable: true,
                isVoiceCallEnable: true,
                isHiddenSecretChat: true
            ),
            isSyncDataInApp: true,
            allowReferralCode: false,
            searchByLike: true,
            allowReplaceCountrycode: false,
            isSyncContactInApp: true
        ),
        permissions: [SDKPermissionSet.microPhone]
    )
}

