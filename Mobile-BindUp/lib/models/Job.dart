class Job {
  String? id;
  String? name;
  String? description;
  String? duaDate;
  String? jobStatus;

  Job({this.id,
    this.name,
    this.description,
    this.duaDate,
    this.jobStatus
  });

  Job.fromJson(Map<String, dynamic> json) {
    id = json["id"] ?? "";
    name = json["name"] ?? "";
    duaDate = json["dueDate"] ?? "";
    jobStatus = json["jobStatus"] ?? "";
    description = json["description"]??"";
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> _data = <String, dynamic>{};
    _data["id"] = id;
    _data["name"] = name;
    return _data;
  }
}