import 'dart:convert';

import 'package:android/models/NotificationCus.dart';
import 'package:flutter/foundation.dart';

import '../constants.dart';
import 'StorageService.dart';
import 'package:http/http.dart' as http;
class NotificationService {
  static const String urlNotification = server + "notifications";

  static List<NotificationCus> parserNotificationList(String responseBody){
    var data = json.decode(responseBody) ;
    var list = data['data'] as List<dynamic>;
    List<NotificationCus> notificationCus = list.map((model) => NotificationCus.fromJson(model)).toList();
    return notificationCus;
  }


  static Future<List<NotificationCus>> fetchNotificationList() async{

    var headers = {
      'Content-type': 'application/json',
      'Accept': 'application/json',
      'Authorization': 'Bearer ${await StorageService.getAccessToken()}'
    };
    final response = await http.get(Uri.parse(urlNotification),headers: headers );
    if (response.statusCode ==200){
      print('Get List Notification Successful');
      return compute(parserNotificationList,response.body);
    } else if (response.statusCode ==404){
      throw Exception('Not found');
    } else{
      throw Exception('Can\'t get');
    }
  }

}