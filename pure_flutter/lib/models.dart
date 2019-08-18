class User {
  String id;
  String login;
  String description;
  String avatarUrl;
  List<Repo> repositories;
}

class Repo {
  String id;
  String name;
  String description;
  User owner;
  int stargazersCount;
  List<User> contributors;
}
