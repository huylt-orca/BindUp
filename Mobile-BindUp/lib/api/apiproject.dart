import 'dart:convert';

import 'package:android/constants.dart';
import 'package:android/models/Project.dart';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;

class ProjectRequest{
  static const String url = server + "project/?pageSize=4&sortBy=id";

  static List<Project> parserTest(String responseBody){
    var list1 = json.decode(responseBody) ;
    var list = list1['data']['projectDTOList'] as List<dynamic>;
    List<Project> projects = list.map((model) => Project.fromJson(model)).toList();
    print(projects[0].logo);
    return projects;
  }

  static Future<List<Project>> fetchPosts({int page =0 }) async{
    final response = await http.get(Uri.parse(url + "&pageNo=$page"));
    if (response.statusCode ==200){
      return compute(parserTest,response.body);
    } else if (response.statusCode ==404){
      throw Exception('Not found');
    } else{
      throw Exception('Can\'t get');
    }
  }


}