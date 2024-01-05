import 'Job.dart';
import 'Project.dart';
import 'User.dart';

class Application {
  String? id;
  String? createDate;
  String? description;
  String? status;

  User? user;
  Project? project;
  Job? job;

  Application({
    this.id,
    this.createDate,
    this.description,
    this.status
  });

  Application.fromJson(Map<String, dynamic> json) {
    id = json['id'] ?? "";
    createDate = json['createDate'] ?? "";
    description = json['description'] ?? "";
    status = json['applicationStatus'] ?? "";
    // user = User.fromJson(json['userDTO']);
    // project = Project.fromJson(json['projectDTO']);
    // job = Job.fromJson(json['jobDTO']);
  }

  Application.fromJsonDetail(Map<String, dynamic> json) {
    id = json['id'] ?? "";
    createDate = json['createDate'] ?? "";
    description = json['description'] ?? "";
    status = json['applicationStatus'] ?? "";
    user = User.fromJson(json['userDTO']);
    project = Project.fromJson(json['projectDTO']);
    job = Job.fromJson(json['jobDTO']);
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();

    data['id'] = this.id;
    data['createDate'] = this.createDate;
    data['description'] = this.description;
    data['status'] = this.status;
    return data;
  }
}