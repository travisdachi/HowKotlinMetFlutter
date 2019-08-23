package com.example.pure_flutter

import android.os.Bundle
import com.example.kmpp.common.flutterchannel.FlutterMethod
import com.example.kmpp.common.kreactive.KCompositeSubscriptions
import com.example.kmpp.common.kreactive.KObservable
import com.example.kmpp.flutterchannel.HomeStoreFlutterChannel

import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity : FlutterActivity() {

    val homeStoreFlutterChannel by lazy {
        HomeStoreFlutterChannel()
    }

    val methodChannel by lazy {
        MethodChannel(
                flutterView,
                homeStoreFlutterChannel.methodChannelName
        ).also {
            it.setMethodCallHandler { methodCall, result ->
                val method = FlutterMethod(
                        methodCall.method, methodCall.arguments?.toString() ?: ""
                )
                homeStoreFlutterChannel.invokeMethod(method)
                result.success(null)
            }
        }
    }

    private val subscriptions = KCompositeSubscriptions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GeneratedPluginRegistrant.registerWith(this)
        methodChannel
        homeStoreFlutterChannel.flutterState.observe {
            methodChannel.invokeMethod(it.method, it.argument)
        }
        homeStoreFlutterChannel.flutterEvent.observe {
            methodChannel.invokeMethod(it.method, it.argument)
        }

    }

    override fun onDestroy() {
        subscriptions.clear()
        homeStoreFlutterChannel.dispose()
        super.onDestroy()
    }

    private inline fun <T> KObservable<T>.observe(
            crossinline onNext: (T) -> Unit
    ) {
        subscriptions += subscribe {
            onNext(it)
        }
    }
}
