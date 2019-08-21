import 'package:dio/dio.dart';
import 'package:pure_flutter/models.dart';

final Dio dio = Dio()..interceptors.add(LogInterceptor(requestBody: true, responseBody: true));

final kotlinUser = getUser('kotlin', isOrg: true);

Future<User> getUser(String owner, {bool isOrg = false}) {
  final type = isOrg ? 'organization' : 'user';
  final desc = isOrg ? 'description' : 'bio';
  return dio
      .post(
    'https://api.github.com/graphql',
    data: {
      'query': """{
  $type(login: "$owner") {
    id
    login
    $desc
    avatarUrl
    repositories(first: 5, orderBy: {field: STARGAZERS, direction: DESC}) {
      nodes {
        id
        name
        description
        stargazers {
          totalCount
        }
        assignableUsers(first: 10) {
          nodes {
            id
            login
            avatarUrl
          }
        }
      }
    }
  }
}
"""
    },
    options: Options(
      headers: {
        'Authorization': 'Bearer 33745460051377c5152150127a75aaf40beb49c8',
      },
    ),
  )
      .then(
    (response) {
      final data = response.data['data'];
      final userJson = data[type];
      final user = User()
        ..id = userJson['id']
        ..login = userJson['login']
        ..description = userJson[desc]
        ..avatarUrl = userJson['avatarUrl'];
      final repositoriesJson = userJson['repositories']['nodes'] as List;
      user.repositories = repositoriesJson
          .map((r) => Repo()
            ..id = r['id']
            ..name = r['name']
            ..description = r['description']
            ..stargazersCount = r['stargazers']['totalCount']
            ..contributors = (r['assignableUsers']['nodes'] as List)
                .map((c) => User()
                  ..id = c['id']
                  ..login = c['login']
                  ..description = c['description']
                  ..avatarUrl = c['avatarUrl'])
                .toList())
          .toList();
      return user;
    },
  );
}
