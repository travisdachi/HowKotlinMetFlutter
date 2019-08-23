import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'package:rxdart/rxdart.dart';

abstract class BaseStoreAction {
  String getMethodName() {
    return runtimeType.toString().replaceAll("StoreAction_", "Store.Action."); //TODO: recheck name pattern
  }

  String stringify() {
    return jsonEncode(toJson());
  }

  Map<String, dynamic> toJson() {
    return null;
  }
}

abstract class BaseStore<STATE, EVENT, ACTION extends BaseStoreAction> {
  final BehaviorSubject<STATE> state;
  final BehaviorSubject<EVENT> event;

  BaseStore(STATE initialState)
      : state = BehaviorSubject.seeded(initialState),
        event = BehaviorSubject();

  STATE get currentState => state.value;

  MethodChannel _channel;

  MethodChannel _createChannel() {
    return MethodChannel('com.example.kmpp/store/${_getStoreName()}');
  }

  STATE deSerializeState(String state);
  EVENT deSerializeEvent(String method, String argument);

  String _getStoreName() {
    return runtimeType.toString();
  }

  void init() {
    _channel = _createChannel();
    _channel.setMethodCallHandler((MethodCall call) async {
      if (call.method == "${_getStoreName()}.State") {
        var newState = deSerializeState(call.arguments.toString());
        state.value = newState;
      } else if (call.method.startsWith("${_getStoreName()}")) {
        event.value = deSerializeEvent(call.method, call.arguments.toString());
      }
      return "";
    });
  }

  void dispatch(ACTION action) {
    _channel.invokeMethod(action.getMethodName(), action.stringify());
  }

  @mustCallSuper
  void dispose() {
    state.close();
    event.close();
  }
}
