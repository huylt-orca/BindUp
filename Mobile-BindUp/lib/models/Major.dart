class Major {
  String? id;
  String? name;
  String? description;

  Major({this.id,
    this.name,
    this.description
  });

  Major.fromJson(Map<String, dynamic> json) {
    id = json["id"] ?? "";
    name = json["name"] ?? "";
    description = json["description"] ?? "";
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> _data = <String, dynamic>{};
    _data["id"] = id;
    _data["name"] = name;
    _data["description"] = description;
    return _data;
  }
}