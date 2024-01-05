
import 'package:android/constants.dart';
import 'package:android/models/responsetest.dart';
import 'package:android/services/NotificationService.dart';
import 'package:android/widget/NotificationCard.dart';
import 'package:android/widget/TabBarProject.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

import '../controller/UserController.dart';
import '../models/NotificationCus.dart';
import '../widget/ChangeLogCard.dart';
import 'login_screen.dart';

class NotificationScreen extends StatefulWidget {
  const NotificationScreen({Key? key}) : super(key: key);

  @override
  State<NotificationScreen> createState() => _NotificationScreenState();
}

class _NotificationScreenState extends State<NotificationScreen> {
  List<NotificationCus> notifications = List<NotificationCus>.empty(growable: true);
  // List<RemoteMessage> _messages =[];
String reload = "";
  UserController userController = Get.put(UserController());

  @override
  initState()  {
    // TODO: implement initState
    super.initState();
    if (userController.id.value != "") {
      NotificationService.fetchNotificationList().then((data) {
        setState(() {
          notifications = data;
        });
      });
    }
    FirebaseMessaging.onMessage.listen((RemoteMessage message) {
      print(message.sentTime);
      NotificationService.fetchNotificationList().then((data) {
        setState(() {
          notifications = data;
        });
      });
      // setState(() {
      //   // _messages = [message,..._messages];
      //   notifications = await NotificationService.fetchNotificationList();
      // });
    });
  }

  @override
  Widget build(BuildContext context) {

    UserController userController = Get.put(UserController());

    if (userController.id.isEmpty){
      return Center(
        child: ElevatedButton(
          child: Text("Login"),
          onPressed: ()async {
            await Navigator.push(
                context,
                MaterialPageRoute(builder: (context) =>
                    LoginScreen(),
                )
            );
            setState(() {
              reload ="hello";
            });
          },

        ),
      );
    }
    if (notifications.length == 0){
      return  Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(Icons.notifications_off_outlined,size: 100,),
          SizedBox(height: 10,),
          Text("No Notification",
          style: TextStyle(
            fontSize: 20
          ),),
        ],
      );
    }

    return ListView.builder(
        itemCount: notifications.length,
          itemBuilder: (context,index){
          // RemoteMessage message = _messages[index];
        return NotificationCard(
            title: notifications[index].title! ,
            description: notifications[index].body!,
            image: notifications[index].logo!,
            time: notifications[index].createdDate!
        );
      },

    );
    
  }

  // @override
  // Widget build(BuildContext context) {
  //   List<ResponseTest> list = listTest;
  //   return ListView.builder(
  //     itemCount: list.length,
  //     itemBuilder: (context,index) {
  //       return NotificationCard(
  //           title: list[index].title!,
  //           description: list[index].description!,
  //           image: list[index].link!,
  //           time: list[index].createdDate!
  //       );
  //     },
  //   );
  //
  // }

  Widget listView(){
    return ListView.separated(
        itemBuilder: (context,index){
          return listViewItem(index);
        },
        separatorBuilder: (context,index){
          return Divider(height: 0);
        },
        itemCount: 15
    );
  }

  Widget listViewItem(int index){
    return Container(
      margin: EdgeInsets.symmetric(horizontal: 13,vertical: 10),
      child:Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          prefixIcon(),
          Expanded (
            child: Container(
              margin: EdgeInsets.only(left: 10),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  message(index),
                  timeAndDate()
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget prefixIcon(){
    return Container(
      height: 50,
      width: 50,
      padding: EdgeInsets.all(10),
      decoration: BoxDecoration(
        shape: BoxShape.circle,
        color: Colors.grey.shade300
      ),
      child: Icon(Icons.notifications,size: 25,color: Colors.grey.shade300,),
    );
  }

  Widget message(int index){
    double textSize = 14;
    return Container(
      child: RichText(
        maxLines: 3,
        overflow: TextOverflow.ellipsis,
        text: TextSpan(
          text: "Message",
          style: TextStyle(
            fontSize: textSize,
            color: Colors.black,
            fontWeight: FontWeight.bold
          ),
          children:[
            TextSpan(
              text: "Message Description",
              style: TextStyle(
                fontWeight: FontWeight.w400,
              )
            )
          ]
        ),
      ),
    );
  }

  Widget timeAndDate(){
    return Container(
       margin: EdgeInsets.only(top: 5),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(
              '01-01-2024',
            style: TextStyle(
              fontSize: 10
            ),
          ),
          Text(
            '07: 00 am',
            style: TextStyle(
                fontSize: 10
            ),
          ),
        ],
      ),
    );
  }
}
