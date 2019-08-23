//import 'package:pure_flutter/api.dart';
//import 'package:pure_flutter/home/home_store_state.dart';
//import 'package:rxdart/rxdart.dart';
//
//class HomeState {
//  final bool isLoading;
//  final User user;
//
//  HomeState(this.isLoading, this.user);
//}
//
//class HomeStore {
//  BehaviorSubject<HomeState> state = BehaviorSubject.seeded(HomeState(true, null));
//  BehaviorSubject<String> event = BehaviorSubject();
//
//  void init() => fetch('kotlin', true);
//
//  void dispose() {
//    state.close();
//    event.close();
//  }
//
//  void fetch(String username, bool isOrg) async {
//    state.value = HomeState(true, null);
////    try {
////      final user = await getUser(username, isOrg: isOrg);
////      state.value = HomeState(false, user);
////    } catch (e) {
////      state.value = HomeState(false, null);
////      event.value = e.toString();
////    }
//  }
//}
