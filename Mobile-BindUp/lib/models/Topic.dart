class Topic {
  String? id;
  String? description;
  String? name;
  String? shortName;

  Topic({
    this.id,
    this.description,
    this.name,
    this.shortName
  });

  Topic.fromJson(Map<String, dynamic> json) {
    id = json['id'] ?? "";
    description = json['description'] ?? "";
    name = json['name'] ?? "";
    shortName = json['shortName'] ?? "";
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();

    data['id'] = this.id;
    data['description'] = this.description;
    data['name'] = this.name;
    data['shortName'] = this.shortName;
    return data;
  }
}