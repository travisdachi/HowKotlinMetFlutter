import 'package:pure_flutter/api.dart';
import 'package:pure_flutter/models.dart';
import 'package:rxdart/rxdart.dart';

class HomeState {
  final bool isLoading;
  final User user;

  HomeState(this.isLoading, this.user);
}

class HomeStore {
  BehaviorSubject<HomeState> state = BehaviorSubject.seeded(HomeState(true, null));

  void init() => fetch('kotlin', true);

  void dispose() {
    state.close();
  }

  void fetch(String username, bool isOrg) async {
    state.value = HomeState(true, null);
    final user = await getUser(username, isOrg: isOrg);
    state.value = HomeState(false, user);
  }
}
