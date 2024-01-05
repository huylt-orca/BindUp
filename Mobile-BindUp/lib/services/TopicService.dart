import 'dart:convert';

import 'package:flutter/foundation.dart';

import '../constants.dart';
import '../models/Topic.dart';
import 'package:http/http.dart' as http;

class TopicService{
  static const String urlTopic = server + "topics";

  static List<Topic> parserTopicList(String responseBody){
    var data = json.decode(responseBody) ;
    var list = data['data']['topicDTOList'] as List<dynamic>;
    List<Topic> topics = list.map((model) => Topic.fromJson(model)).toList();
    return topics;
  }

  static Topic parserTopicDetail(String responseBody){
    var data = json.decode(responseBody) ;
    var topicJson = data['data'] ;
    Topic topic =  Topic.fromJson(topicJson);
    return topic;
  }


  static Future<List<Topic>> fetchTopicList({
    int page =0,
    int pageSize =10,
    String sortBy = "id",
  }) async{

    String options =
        "?pageSize=${pageSize}"
        "&pageNo=${page}"
        "&sortBy=${sortBy}"
    ;
    final response = await http.get(Uri.parse(urlTopic + options));
    if (response.statusCode ==200){
      print('Get List Topic Successful');
      return compute(parserTopicList,response.body);
    } else if (response.statusCode ==404){
      throw Exception('Not found');
    } else{
      throw Exception('Can\'t get');
    }
  }

  static Future<Topic> fetchTopicDetail( String topicId  ) async{
    final response = await http.get(Uri.parse(urlTopic + "/$topicId"));
    if (response.statusCode ==200){
      print('Get Topic Detail Successful');
      return compute(parserTopicDetail,response.body);
    } else if (response.statusCode ==404){
      throw Exception('Not found');
    } else{
      throw Exception('Can\'t get');
    }
  }
}