class ResponseTest {
  String? id;
  String? createdDate;
  String? description;
  String? title;
  String? link;

  ResponseTest({
    this.id,
    this.createdDate,
    this.description,
    this.title,
    this.link

  });

  ResponseTest.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    createdDate = json['createdDate'];
    description = json['description'];
    title = json['title'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();

    data['id'] = this.id;
    data['createdDate'] = this.createdDate;
    data['description'] = this.description;
    data['title'] = this.title;

    return data;
  }
}