import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:get/get.dart';

import '../constants.dart';
import '../controller/ProjectController.dart';
import '../models/Member.dart';
import '../models/Topic.dart';
import 'package:http/http.dart' as http;

import 'StorageService.dart';

class ProjectMemberService{
  static const String urlMember = server + "members";

  static List<Member> parserMemberList(String responseBody){
    var data = json.decode(responseBody) ;
    var list = data['data']['memberDTOList'] as List<dynamic>;
    List<Member> members = list.map((model) => Member.fromJson(model)).toList();
    return members;
  }

  static List<Member> parserMemberListByProjectId(String responseBody){
    var data = json.decode(responseBody) ;
    var list = data['data'] as List<dynamic>;
    List<Member> members = list.map((model) => Member.fromJson(model)).toList();
    return members;
  }

  static Member parserMemberDetail(String responseBody){
    var data = json.decode(responseBody) ;
    var memberJson = data['data'] ;
    Member member =  Member.fromJson(memberJson);
    return member;
  }


  static Future<List<Member>> fetchMemberList({
    int page =0,
    int pageSize =4,
    bool paging = false,
    required String projectId
  }) async{

    String options =
        "?pageSize=${pageSize}"
        "&pageNo=${page}"
        "&paging=${paging}"
        "&projectId=${projectId}"
    ;
    final response = await http.get(Uri.parse(urlMember + options));
    if (response.statusCode ==200){
      print('Get List Member Successful');
      return compute(parserMemberList,response.body);
    } else if (response.statusCode ==404){
      throw Exception('Not found');
    } else{
      throw Exception('Can\'t get');
    }
  }

  static Future<List<Member>> fetchMemberListByProjectId({
    bool paging = false,
    int pageNo = 0,
    int pageSize =10
  }) async{
    ProjectController projectController = Get.put(ProjectController());
    String options =
        "?&projectId=${projectController.id.value}"
        "&paging=${paging}"
        "&pageNo=${pageNo}"
        "&pageSize=${pageSize}"
    ;
    final response = await http.get(Uri.parse(urlMember + options));
    if (response.statusCode ==200){
      print('Get List Member By Project Successful');
      return compute(parserMemberListByProjectId,response.body);
    } else if (response.statusCode ==404){
      throw Exception('Not found');
    } else{
      throw Exception('Can\'t get');
    }
  }

  static Future<Member> fetchMemberDetail( String memberId  ) async{
    final response = await http.get(Uri.parse(urlMember + "/$memberId"));
    if (response.statusCode ==200){
      print('Get Member Detail Successful');
      return compute(parserMemberDetail,response.body);
    } else if (response.statusCode ==404){
      throw Exception('Not found');
    } else{
      throw Exception('Can\'t get');
    }
  }

  static Future<bool> postMemberToProject(String name, String title, String role) async{

    ProjectController projectController = Get.put(ProjectController());

    var headers = {
      'Content-type': 'application/json',
      'Accept': 'application/json',
      'Authorization': 'Bearer ${await StorageService.getAccessToken()}'
    };

    var uri = Uri.parse(urlMember);
    final body = json.encode({
      'name': name,
      'title': title,
      'role': role,
      'projectId': projectController.id.value
    });
    try {
      final response = await http.post(uri,body: body,headers: headers);
      if (response.statusCode == 200){
        print('Add Mentor to Product Successful');
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