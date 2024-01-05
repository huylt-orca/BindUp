import 'package:android/constants.dart';

class ProjectImage {
  String? id;
  String? directory;

  ProjectImage({
    this.id,
    this.directory,
  });

  ProjectImage.fromJson(Map<String, dynamic> json) {
    id = json['id']?? "";
    directory = json['directory'] ?? imageDemo;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();

    data['id'] = this.id;
    data['directory'] = this.directory;
    return data;
  }
}