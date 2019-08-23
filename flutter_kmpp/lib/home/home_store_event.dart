import 'package:json_annotation/json_annotation.dart';

part 'home_store_event.g.dart';

abstract class HomeStoreEvent {}

@JsonSerializable()
class HomeStoreEvent_FetchError extends HomeStoreEvent{

  final String message;

  HomeStoreEvent_FetchError(this.message);
  factory HomeStoreEvent_FetchError.fromJson(Map<String, dynamic> json) => _$HomeStoreEvent_FetchErrorFromJson(json);
  @override
  Map<String, dynamic> toJson() => _$HomeStoreEvent_FetchErrorToJson(this);
}