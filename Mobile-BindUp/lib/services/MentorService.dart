import 'dart:convert';

import 'package:android/controller/ProjectController.dart';
import 'package:flutter/foundation.dart';
import 'package:get/get.dart';

import '../constants.dart';
import '../models/Mentor.dart';
import 'package:http/http.dart' as http;

import 'StorageService.dart';

class MentorService{
  static const String urlMentor = server + "mentors";

  static List<Mentor> parserMentorList(String responseBody){
    var data = json.decode(responseBody) ;
    var list = data['data']['mentorDTOList'] as List<dynamic>;
    List<Mentor> mentors = list.map((model) => Mentor.fromJson(model)).toList();
    return mentors;
  }

  static List<Mentor> parserMentorListByProject(String responseBody){
    var data = json.decode(responseBody) ;
    var list = data['data'] as List<dynamic>;
    List<Mentor> mentors = list.map((model) => Mentor.fromJson(model)).toList();
    return mentors;
  }

  static Mentor parserMentorDetail(String responseBody){
    var data = json.decode(responseBody) ;
    var mentorJson = data['data'] ;
    Mentor mentor =  Mentor.fromJson(mentorJson);
    return mentor;
  }


  static Future<List<Mentor>> fetchMentorList({
    int page =0,
    int pageSize =20,
    String sortBy = "id"
  }) async{

    String options =
        "?pageSize=${pageSize}"
        "&pageNo=${page}"
        "&sortBy=${sortBy}"
    ;
    var headers = {
      'Content-type': 'application/json',
      'Accept': 'application/json',
      'Authorization': 'Bearer ${await StorageService.getAccessToken()}'
    };
    final response = await http.get(Uri.parse(urlMentor + options),headers: headers );
    if (response.statusCode ==200){
      print('Get Mentor List Successful');
      return compute(parserMentorList,response.body);
    } else if (response.statusCode ==404){
      throw Exception('Not found');
    } else{
      throw Exception('Can\'t get');
    }
  }

  static Future<Mentor> fetchMentorDetail(String mentorId  ) async{
    final response = await http.get(Uri.parse(urlMentor + "/$mentorId"));
    if (response.statusCode ==200){
      print ('Get Mentor Detail Successful');
      return compute(parserMentorDetail,response.body);
    } else if (response.statusCode ==404){
      throw Exception('Not found');
    } else{
      throw Exception('Can\'t get');
    }
  }

  static Future<List<Mentor>> fetchMentorListByProject() async{
    ProjectController projectController = Get.put(ProjectController());
    var headers = {
      'Content-type': 'application/json',
      'Accept': 'application/json',
      'Authorization': 'Bearer ${await StorageService.getAccessToken()}'
    };
    
    
    final response = await http.get(Uri.parse(urlMentor + "/project/${projectController.id.value}"),headers: headers );
    if (response.statusCode ==200){
      print('Get Mentor List By Project Successful');
      return compute(parserMentorListByProject,response.body);
    } else if (response.statusCode ==404){
      throw Exception('Not found');
    } else{
      throw Exception('Can\'t get');
    }
  }


}