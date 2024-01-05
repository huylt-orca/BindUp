import 'dart:convert';

import 'package:android/controller/ProjectController.dart';
import 'package:android/controller/UserController.dart';
import 'package:android/models/Application.dart';
import 'package:android/statusType/ApplicationStatus.dart';
import 'package:flutter/foundation.dart';
import 'package:get/get.dart';
import 'package:http/http.dart' as http;
import '../constants.dart';
import 'StorageService.dart';

class ApplicationService{
  static const String urlApplication = server + "applications";

  static List<Application> parserApplicationList(String responseBody){
    var data = json.decode(responseBody) ;
    var list = data['data']['applicationDTOList'] as List<dynamic>;
    List<Application> applications = list.map((model) => Application.fromJsonDetail(model)).toList();
    return applications;
  }


  static Future<bool> postApplication(String description, String jobId) async{
    UserController userController = Get.put(UserController());
    ProjectController projectController = Get.put(ProjectController());

    var headers = {
      'Content-type': 'application/json',
      'Accept': 'application/json',
      'Authorization': 'Bearer ${await StorageService.getAccessToken()}'
    };

    var uri = Uri.parse(urlApplication);

    final body = json.encode({
      'userId': userController.id.value,
      'projectId': projectController.id.value,
      'jobId': jobId,
      'description': description
    });
    try {
      final response = await http.post(uri,body: body,headers: headers);
      if (response.statusCode == 200){
        print('Apply to Project Successful');
        final dataResponse = json.decode(response.body)['data'];
        // if (dataResponse !=""){
        //   return false;
        // }
        return true;
      } else{
        print('Error: ${response.reasonPhrase}');
      }
    } catch (error){
      print('print $error');
    }
    return false;
  }

  static Future<List<Application>> fetchApplicationList({
    int page =0,
    int pageSize =5,
    String sortBy = "created_timestamp",
    int statusType = 0,
    String ascending ="DESC"
  }) async{
    ProjectController projectController = Get.put(ProjectController());

    String options =
        "?pageSize=${pageSize}"
        "&pageNo=${page}"
        "&sortBy=${sortBy}"
        "&ascending=${ascending}"
        "&status=${statusType}"
        "&projectId=${projectController.id.value}"
    ;

    var headers = {
      'Content-type': 'application/json',
      'Accept': 'application/json',
      'Authorization': 'Bearer ${await StorageService.getAccessToken()}'
    };

    final response = await http.get(Uri.parse(urlApplication + options),headers: headers );
    if (response.statusCode ==200){
      print('Get Application Successful');
      return compute(parserApplicationList,response.body);
    } else if (response.statusCode ==404){
      throw Exception('Not found');
    } else{
      throw Exception('Can\'t get');
    }
  }

  static Future<bool> putProject(String applicationId, ApplicationStatus status) async {

    String options =
        "/status?applicationId=${applicationId}"
        "&applicationStatus=${status.name}"
    ;
    print (status.name);

    var uri = Uri.parse(urlApplication + options);

    var headers = {
      'Content-type': 'application/json',
      'Accept': 'application/json',
      'Authorization': 'Bearer ${await StorageService.getAccessToken()}'
    };
    try {
      var response = await http.put(uri, headers: headers);

      if (response.statusCode == 200) {
        print("Update Project Success");
        return true;
      } else {
        print('Error: ${response.reasonPhrase}');
      }
    } catch (error) {
      print('Error: $error');
    }
    return false;
  }

  static Future<List<Application>> fetchApplicationListByUser({
    int page =0,
    int pageSize =5,
    String sortBy = "created_timestamp",
    int statusType = 0,
    String ascending ="DESC"
  }) async{
    UserController userController = Get.put(UserController());

    String options =
        "/user?pageSize=${pageSize}"
        "&pageNo=${page}"
        "&sortBy=${sortBy}"
        "&ascending=${ascending}"
        "&status=${statusType}"
        "&userId=${userController.id.value}"
    ;

    var headers = {
      'Content-type': 'application/json',
      'Accept': 'application/json',
      'Authorization': 'Bearer ${await StorageService.getAccessToken()}'
    };

    final response = await http.get(Uri.parse(urlApplication + options),headers: headers );
    if (response.statusCode ==200){
      print('Get Application Successful');
      return compute(parserApplicationList,response.body);
    } else if (response.statusCode ==404){
      throw Exception('Not found');
    } else{
      throw Exception('Can\'t get');
    }
  }

}