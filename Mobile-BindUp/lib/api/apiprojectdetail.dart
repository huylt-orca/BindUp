import 'dart:convert';

import 'package:android/constants.dart';
import 'package:android/models/Project.dart';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;

class ProjectDetailRequest{
  static const String url = server + "project/";

  static Project parserTest(String responseBody){
    var list1 = json.decode(responseBody) ;
    var projectJson = list1['data'];
    Project project =  Project.fromJson(projectJson);
    return project;
  }

  static Future<Project> fetchPosts({required String id }) async{
    final response = await http.get(Uri.parse(url + id));
    if (response.statusCode ==200){
      return compute(parserTest,response.body);
    } else if (response.statusCode ==404){
      throw Exception('Not found');
    } else{
      throw Exception('Can\'t get');
    }
  }


}