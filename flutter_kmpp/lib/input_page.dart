import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class UsernameInput {
  final String username;
  final bool isOrg;

  UsernameInput(this.username, this.isOrg);
}

class InputPage extends StatefulWidget {
  @override
  _InputPageState createState() => _InputPageState();

  static MaterialPageRoute<UsernameInput> route() {
    return MaterialPageRoute(builder: (BuildContext context) => InputPage());
  }
}

class _InputPageState extends State<InputPage> {
  TextEditingController _controller = TextEditingController();
  bool _isOrg = false;
  String _username = '';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Enter a username'),
      ),
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16),
          child: Column(
            children: <Widget>[
              TextField(
                controller: _controller,
                onChanged: (s) {
                  setState(() {
                    _username = s;
                  });
                },
              ),
              Row(
                children: <Widget>[
                  Checkbox(
                    value: _isOrg,
                    onChanged: (isCheck) {
                      setState(() {
                        _isOrg = isCheck;
                      });
                    },
                  ),
                  Text('isOrganization'),
                ],
              ),
              RaisedButton(
                child: Text('OKAY'),
                onPressed: _username.isEmpty
                    ? null
                    : () {
                        Navigator.pop(context, UsernameInput(_username, _isOrg));
                      },
              ),
            ],
          ),
        ),
      ),
    );
  }
}
