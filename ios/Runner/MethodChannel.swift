//
//  MethodChannel.swift
//  Runner
//
//  Created by Thai Nguyen on 16/11/2022.
//

import Foundation

class MethodChannel{
    static let instance = MethodChannel()
    private var sendChannel:FlutterMethodChannel?
    private let sendChannelName = "callbacks"
    public var receiveChannel:FlutterMethodChannel?
    private let receiveChannelName = "vn.netacom.demo/flutter_channel"
    
    init(){}
    
    func initChannel(messenger:FlutterBinaryMessenger){
        sendChannel = FlutterMethodChannel(name: sendChannelName,
                                           binaryMessenger: messenger)
        
        receiveChannel = FlutterMethodChannel(name: receiveChannelName,
                                              binaryMessenger:messenger)
    }
    
}
