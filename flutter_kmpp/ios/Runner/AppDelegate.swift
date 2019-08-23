import UIKit
import Flutter
import core

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
    
    let homeStoreFlutterChannel = HomeStoreFlutterChannelKt.HomeStoreFlutterChannel()
    let subscriptions = KCompositeSubscriptions()
    
    deinit {
        subscriptions.clear()
        homeStoreFlutterChannel.dispose()
    }
    
    override func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
        ) -> Bool {
        let controller: FlutterViewController = window?.rootViewController as! FlutterViewController
        let methodChannel = FlutterMethodChannel(
            name: homeStoreFlutterChannel.methodChannelName,
            binaryMessenger: controller)
        
        methodChannel.setMethodCallHandler{
            [weak self] (call: FlutterMethodCall, result: @escaping FlutterResult) -> Void in
            let flutterMethod =  FlutterMethod(method: call.method, argument: call.arguments as? String ?? "")
            
            self?.homeStoreFlutterChannel.invokeMethod(method: flutterMethod)
            result(nil)
        }
        
        homeStoreFlutterChannel.flutterState.observe(subscriptions){(flutterMethod: FlutterMethod) in
            methodChannel.invokeMethod(flutterMethod.method, arguments: flutterMethod.argument)
        }
        homeStoreFlutterChannel.flutterEvent.observe(subscriptions){(flutterMethod: FlutterMethod) in
            methodChannel.invokeMethod(flutterMethod.method, arguments: flutterMethod.argument)
        }
        
        GeneratedPluginRegistrant.register(with: self)
        return super.application(application, didFinishLaunchingWithOptions: launchOptions)
    }
    
    
}

extension KObservable {
    
    func observe<T> (_ by: KCompositeSubscriptions, observer:@escaping (T) -> Void) {
        subscribe { (response)  in
            if let item = response as? T {
                observer(item)
            }
            }.disposed(by: by)
    }
}
