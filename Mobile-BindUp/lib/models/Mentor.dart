class Mentor {
  String? id;
  String? email;
  String? major;
  String? name;
  String? phone;

  Mentor({this.id,
    this.email,
    this.major,
    this.name,
    this.phone
  });

  Mentor.fromJson(Map<String, dynamic> json) {
    id = json["id"] ?? "";
    email = json["email"] ?? "";
    major = json["major"] ?? "";
    name = json["name"] ?? "";
    phone = json["phone"] ?? "";
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> _data = <String, dynamic>{};
    _data["id"] = id;
    _data["email"] = email;
    _data["major"] = major;
    _data["name"] = name;
    _data["phone"] = phone;
    return _data;
  }
}