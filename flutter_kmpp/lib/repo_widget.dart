import 'package:flutter/material.dart';
import 'package:pure_flutter/contributor_widget.dart';
import 'package:pure_flutter/home/home_store_state.dart';

class RepoWidget extends StatelessWidget {
  final String name;
  final String description;
  final int stars;
  final List<User> contributors;

  const RepoWidget({
    Key key,
    @required this.name,
    @required this.description,
    @required this.stars,
    @required this.contributors,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Container(
      color: Colors.white,
      padding: EdgeInsets.all(8),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: <Widget>[
          Text(
            name,
            style: theme.textTheme.title.copyWith(color: theme.primaryColor),
          ),
          SizedBox(height: 2),
          Text(
            description ?? '',
            style: theme.textTheme.subtitle,
          ),
          SizedBox(height: 2),
          Row(
            mainAxisSize: MainAxisSize.min,
            children: <Widget>[
              Icon(Icons.star, color: Colors.yellow),
              Text(stars.toString()),
            ],
          ),
          SizedBox(height: 2),
          SizedBox(
            height: 100,
            child: ListView.builder(
              scrollDirection: Axis.horizontal,
              itemCount: contributors.length,
              itemBuilder: (BuildContext context, int index) => ContributorWidget(
                key: ValueKey(contributors[index].id),
                name: contributors[index].login,
                avatarUrl: contributors[index].avatarUrl,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
