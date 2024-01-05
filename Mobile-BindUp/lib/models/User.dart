import 'package:android/constants.dart';

class User {
  String? id;
  String? name;
  int? gender;
  String? headline;
  String? description;
  String? address;
  String? phone;
  String? avatar;
  String? email;
  String? role;

  User({
    this.id,
    this.name,
    this.gender,
    this.headline,
    this.description,
    this.address,
    this.phone,
    this.avatar,
    this.email,
    this.role
  });

  User.fromJson(Map<String, dynamic> json) {
    id = json['id']??"";
    name = json['name'] ?? "";
    gender = json['gender'] ?? 0;
    headline = json['headline'] ?? "";
    description = json['description'] ?? "";
    address = json['address'] ?? "";
    phone = json['phone'] ?? "";
    avatar = json['avatar'] ?? imageDemo;
    email = json['email'] ?? "";
    role = json['role'] ?? "";

  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();

    data['id'] = this.id;
    data['name'] = this.name;
    data['gender'] = this.gender;
    data['headline'] = this.headline;
    data['description'] = this.description;
    data['address'] = this.address;
    data['phone'] = this.phone;
    data['avatar'] = this.avatar;
    data['email'] = this.email;
    data['role'] = this.role;
    return data;
  }
}