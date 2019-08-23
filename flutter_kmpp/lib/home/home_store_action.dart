import 'package:pure_flutter/common/store/base_store.dart';
import 'package:json_annotation/json_annotation.dart';

part 'home_store_action.g.dart';

abstract class HomeStoreAction extends BaseStoreAction {}

@JsonSerializable()
class HomeStoreAction_Fetch extends HomeStoreAction {

  final String username;
  final bool isOrg;

  HomeStoreAction_Fetch(this.username, this.isOrg);

  factory HomeStoreAction_Fetch.fromJson(Map<String, dynamic> json) => _$HomeStoreAction_FetchFromJson(json);

  @override
  Map<String, dynamic> toJson() => _$HomeStoreAction_FetchToJson(this);
}