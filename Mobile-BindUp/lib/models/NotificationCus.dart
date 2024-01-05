import 'package:android/constants.dart';

class NotificationCus{
  String? id;
  String? title;
  String? body;
  String? createdDate;
  String? logo;

  NotificationCus({
    this.id,
    this.title,
    this.body,
    this.createdDate,
    this.logo
});

  NotificationCus.fromJson(Map<String, dynamic> json) {
    id = json["id"] ?? "";
    title = json["title"] ?? "";
    body = json["body"] ?? "";
    createdDate = json["createdTimestamp"] ?? "";
    logo = json["logo"] ?? imageDemo;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> _data = <String, dynamic>{};
    _data["id"] = id;
    _data["title"] = title;
    _data["body"] = body;
    _data["createdDate"] = createdDate;
    _data["logo"] = logo;
    return _data;
  }

}