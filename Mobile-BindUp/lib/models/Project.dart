import 'package:android/constants.dart';

class Project {
  String? id;
  String? name;
  String? summary;
  String? logo;
  String? description;
  String? source;
  int? voteQuantity;
  int? milestone;
  String? status;


  Project({
    this.id,
    this.name,
    this.summary,
    this.logo,
    this.description,
    this.source,
    this.voteQuantity,
    this.milestone,
    this.status,
  });

  Project.fromJson(Map<String, dynamic> json) {
    id = json['id'] ?? "";
    name = json['name'] ?? "";
    summary = json['summary'] ?? "";
    logo = json['logo']  ?? imageDefault;
    description = json['description'] ?? "";
    source = json['source'] ?? "";
    voteQuantity = json['voteQuantity'] ?? 0;
    milestone = json['milestone'] ?? 0;
    status = json['status'] ?? "";
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['name'] = this.name;
    data['summary'] = this.summary;
    data['logo'] = this.logo ;
    data['description'] = this.description;
    data['source'] = this.source;
    data['voteQuantity'] = this.voteQuantity;
    data['milestone'] = this.milestone;
    data['status'] = this.status;
    return data;
  }
}