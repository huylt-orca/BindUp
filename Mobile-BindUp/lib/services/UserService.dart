import 'dart:convert';

import 'package:android/controller/UserController.dart';
import 'package:android/models/ProjectImage.dart';
import 'package:android/services/ProjectService.dart';
import 'package:android/services/StorageService.dart';
import 'package:flutter/foundation.dart';
import 'package:get/get.dart';

import '../constants.dart';
import '../models/Project.dart';
import 'package:http/http.dart' as http;

import '../models/User.dart';

class UserService{
  static const String urlAuth = server + "auth";
  static const String urlUser = server + "users";


  static List<User> parserUserList(String responseBody){
    var data = json.decode(responseBody) ;
    var list = data['data']['userDTOList'] as List<dynamic>;
    List<User> users = list.map((model) => User.fromJson(model)).toList();
    return users;
  }

  static User parserUserDetail(String responseBody){
    var data = json.decode(responseBody) ;
    var userJson = data['data'] ;
    User user =  User.fromJson(userJson);
    return user;
  }

  static User parserUserId(String responseBody){
    var data = json.decode(responseBody) ;
    var userJson = data ;
    User user =  User.fromJson(userJson);
    return user;
  }

  static Future<List<User>> fetchUserList({
    int page =0,
    int pageSize =4,
    String sortBy = "id",
    int statusType = 0

  }) async{

    String options =
        "?pageSize=${pageSize}"
        "&pageNo=${page}"
        "&sortBy=${sortBy}"
        "&statusType=${statusType}"
    ;

    final response = await http.get(Uri.parse(urlUser + options));
    if (response.statusCode ==200){
      print('Get List User Successful');
      return compute(parserUserList,response.body);
    } else if (response.statusCode ==404){
      throw Exception('Not found');
    } else{
      throw Exception('Can\'t get');
    }
  }

  static Future<User> fetchUserDetail() async{
    String? accessToken = await StorageService.getAccessToken();

    final mainresponse = await http.get(
        Uri.parse(urlAuth +'?accessToken=$accessToken'),
        headers: {
          'Authorization': 'Bearer $accessToken',
        }
    );

    if (mainresponse.statusCode ==200){
      // get User detail
      User mainUser = await compute(parserUserId,mainresponse.body);

      final response = await http.get(
          Uri.parse(urlUser + "/${mainUser.id}"),
          headers: {
            'Authorization': 'Bearer $accessToken',
          }
      );
      print(response.statusCode);
      if (response.statusCode ==200){
        print ('Get User Detail Successful');
        return compute(parserUserDetail,response.body);
      } else if (response.statusCode ==404){
        throw Exception('Not found');
      } else{
        throw Exception('Can\'t get');
      }
    } else if (mainresponse.statusCode ==404){
      throw Exception('Not found');
    } else{
      throw Exception('Can\'t get');
    }
  }



  static Future<void> postUser(User user) async {
    var uri = Uri.parse(urlUser);

    var body = jsonEncode({
      'name': user.name,
      'description': user.description,
      'voteQuantity': 0,
      'milestone': 0,
      'founderId': '19d19cf5-68b7-4688-9a53-97f9ac2d1676',

    });
    var headers = {
      'Content-type': 'application/json',
      'Accept': 'application/json',
    };

    try {
      var response = await http.post(uri, body: body, headers: headers);

      if (response.statusCode == 200) {
        print('Create User Successful');
      } else {
        print('Error: ${response.reasonPhrase}');
      }
    } catch (error) {
      print('Error: $error');
    }
  }

  static Future<bool> putUser(User user) async {
    UserController userController = Get.put(UserController());

    var uri = Uri.parse(urlUser + "/" +userController.id.value);

    var body = jsonEncode({
      'name': user.name,
      'gender': user.gender,
      'headline': user.headline,
      'description': user.description,
      'address': user.address,
      'phone': user.phone
    });
    var headers = {
      'Content-type': 'application/json',
      'Accept': 'application/json',
      'Authorization': 'Bearer ${await StorageService.getAccessToken()}'
    };

    try {
      var response = await http.put(uri, body: body, headers: headers);

      if (response.statusCode == 200) {
        print('Edit Profile Successful');
        return true;
      } else {
        print('Error: ${response.reasonPhrase}');
      }
    } catch (error) {
      print('Error: $error');
    }
    return false;
  }

  static Future<List<Project>> fetchProjectListByUser() async{
    UserController userController = Get.put(UserController());
    final response = await http.get(Uri.parse(urlUser + "/${userController.id.value}/members"));
    if (response.statusCode ==200){
      print('Get Líst Project By User Join Successful');
      return compute(ProjectService.parserProjectListByUserJoin,response.body);
    } else if (response.statusCode ==404){
      throw Exception('Not found');
    } else{
      throw Exception('Can\'t get');
    }
  }
}