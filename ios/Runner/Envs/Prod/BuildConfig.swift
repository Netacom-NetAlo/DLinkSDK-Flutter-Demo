//
//  BuildConfig.swift
//  Runner
//
//  Created by Hieu Bui Van  on 22/12/2021.
//

import NetAloLite
import NetAloFull

struct BuildConfig {
    
    /*
     Init Netalo SDKs and config parameters
     Description enviroment:
      - developer  = 0
      - production = 2
     */
    
    static var config = NetaloConfiguration(
        enviroment: 2,
        appId:17,
        appKey:"B2D89AC8B8ECF",
        accountKey:"adminkey",
        appGroupIdentifier: "group.vn.netacom.vndirect-dev",
        storeUrl:"https://apps.apple.com/vn/app/vndirect/id1594533471",
        forceUpdateProfile:true,
        allowCustomUsername:true,
        allowCustomProfile:false,
        allowCustomAlert:false,
        allowAddContact:true,
        allowBlockContact:true,
        allowSetUserProfileUrl:false,
        allowEnableLocationFeature:false,
        allowTrackingUsingSDK:false,
        isHiddenEditProfile:true,
        allowAddNewContact:false,
        allowEditContact:false,
        isVideoCallEnable:true,
        isVoiceCallEnable:true,
        isHiddenSecretChat:true,
        isSyncDataInApp:true,
        allowReferralCode:false,
        searchByLike:true,
        allowReplaceCountrycode:false,
        isSyncContactInApp:true,
        permissions:[1],
        userProfileUrl: "",
        isLoggingEnable: true
    )
}

