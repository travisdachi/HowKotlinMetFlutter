import 'package:json_annotation/json_annotation.dart';

part 'home_store_state.g.dart';

@JsonSerializable()
class User {
  final String id;
  final String login;
  final String description;
  final String avatarUrl;
  final List<Repo> repositories;

  User(this.id, this.login, this.description, this.avatarUrl, this.repositories);

  factory User.fromJson(Map<String, dynamic> json) => _$UserFromJson(json);

  @override
  Map<String, dynamic> toJson() => _$UserToJson(this);
}

@JsonSerializable()
class Repo {
  final String id;
  final String name;
  final String description;
  final User owner;
  final int stargazersCount;
  final List<User> contributors;

  Repo(this.id, this.name, this.description, this.owner, this.stargazersCount, this.contributors);

  factory Repo.fromJson(Map<String, dynamic> json) => _$RepoFromJson(json);

  @override
  Map<String, dynamic> toJson() => _$RepoToJson(this);
}

@JsonSerializable()
class HomeStoreState {
  final bool loading;
  final User user;

  HomeStoreState(this.loading, this.user);

  factory HomeStoreState.fromJson(Map<String, dynamic> json) => _$HomeStoreStateFromJson(json);

  @override
  Map<String, dynamic> toJson() => _$HomeStoreStateToJson(this);
}
