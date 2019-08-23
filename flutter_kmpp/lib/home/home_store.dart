import 'dart:convert';

import 'package:pure_flutter/common/store/base_store.dart';
import 'package:pure_flutter/home/home_store_action.dart';
import 'package:pure_flutter/home/home_store_event.dart';
import 'package:pure_flutter/home/home_store_state.dart';

class HomeStore extends BaseStore<HomeStoreState, HomeStoreEvent, HomeStoreAction> {
  HomeStore() : super(HomeStoreState(false, null));

  @override
  HomeStoreEvent deSerializeEvent(String method, String argument) {
    switch (method) {
      case "HomeStore.Event.FetchError":
        return HomeStoreEvent_FetchError.fromJson(jsonDecode(argument));
      default:
        return null;
    }
  }

  @override
  HomeStoreState deSerializeState(String state) {
    return HomeStoreState.fromJson(jsonDecode(state));
  }
}
