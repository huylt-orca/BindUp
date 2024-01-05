import 'dart:convert';

import 'package:android/controller/ProjectController.dart';
import 'package:flutter/foundation.dart';
import 'package:get/get.dart';

import '../constants.dart';
import '../models/Changelog.dart';
import 'package:http/http.dart' as http;

import 'StorageService.dart';

class ChangelogService{
  static const String urlChangelog = server + "changelogs";

  static List<Changelog> parserChangelogList(String responseBody){
    var data = json.decode(responseBody) ;
    var list = data['data'] as List<dynamic>;
    List<Changelog> changelogs = list.map((model) => Changelog.fromJson(model)).toList();
    return changelogs;
  }

  static Changelog parserChangelogDetail(String responseBody){
    var data = json.decode(responseBody) ;
    var changelogJson = data['data'] ;
    Changelog changelog =  Changelog.fromJson(changelogJson);
    return changelog;
  }


  static Future<List<Changelog>> fetchChangelogList({
        int page =0,
        int pageSize =4,
        bool paging = false,
        String sortBy = 'created_timestamp',
        String ascending = 'DESC',
        required String projectId
      }) async{

    String options =
        "?pageSize=${pageSize}"
        "&pageNo=${page}"
        "&paging=${paging}"
        "&projectId=${projectId}"
        "&sortBy=${sortBy}"
        "&ascending=${ascending}"
    ;
    final response = await http.get(Uri.parse(urlChangelog + options));
    print(response.statusCode);
    if (response.statusCode ==200){
      print('Get Changelog Successful');
      return compute(parserChangelogList,response.body);
    } else if (response.statusCode ==404){
      throw Exception('Not found');
    } else{
      throw Exception('Can\'t get');
    }
  }

  static Future<Changelog> fetchChangelogDetail( String changelogId  ) async{
    final response = await http.get(Uri.parse(urlChangelog + "/${changelogId}"));
    if (response.statusCode ==200){
      print('Get Changelog Detail Successful');
      return compute(parserChangelogDetail,response.body);
    } else if (response.statusCode ==404){
      throw Exception('Not found');
    } else{
      throw Exception('Can\'t get');
    }
  }

  static Future<bool> postChangelog(String title,String description)async {
    ProjectController projectController = Get.put(ProjectController());
    final uri = Uri.parse(urlChangelog);
    final body = jsonEncode({
      'projectId': projectController.id.value,
      'title': title,
      'description': description
    });

    var headers = {
      'Content-type': 'application/json',
      'Accept': 'application/json',
      'Authorization': 'Bearer ${await StorageService.getAccessToken()}'
    };

    try {
      final response = await http.post(uri,body: body,headers: headers);
      if (response.statusCode == 200){
        print('Create Changelog Successful');
        return true;
      } else{
        print('Error: ${response.reasonPhrase}');
      }
    } catch (error){
      print('print $error');
    }
    return false;
  }
}