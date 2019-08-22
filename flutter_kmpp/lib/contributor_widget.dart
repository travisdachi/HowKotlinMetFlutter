import 'package:flutter/material.dart';

class ContributorWidget extends StatelessWidget {
  final String name;
  final String avatarUrl;

  const ContributorWidget({
    Key key,
    @required this.name,
    @required this.avatarUrl,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 80,
      height: 84,
      margin: EdgeInsets.symmetric(horizontal: 4, vertical: 2),
      padding: EdgeInsets.all(4),
      alignment: Alignment.center,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(4),
        boxShadow: [
          BoxShadow(
            color: Colors.grey[300],
            blurRadius: 2,
            offset: Offset(0, 2),
          ),
        ],
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          CircleAvatar(
            backgroundImage: NetworkImage(avatarUrl),
            radius: 30,
          ),
          SizedBox(height: 4),
          Text(
            name,
            maxLines: 1,
            overflow: TextOverflow.ellipsis,
          ),
        ],
      ),
    );
  }
}
