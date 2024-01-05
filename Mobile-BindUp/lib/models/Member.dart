class Member {
  String? id;
  String? role;
  String? title;
  String? name;

  Member({this.id, this.role, this.title, this.name});

  Member.fromJson(Map<String, dynamic> json) {
    id = json["id"] ?? "";
    role = json["role"] ?? "";
    title = json["title"] ?? "";
    name = json["name"] ?? "";
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> _data = <String, dynamic>{};
    _data["id"] = id;
    _data["role"] = role;
    _data["title"] = title;
    _data["name"] = name;
    return _data;
  }
}