import 'package:android/api/apiproject.dart';
import 'package:android/controller/UserController.dart';
import 'package:android/models/User.dart';
import 'package:android/models/Project.dart';
import 'package:android/screens/login_screen.dart';
import 'package:android/services/StorageService.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

import '../widget/TarBarMyProject.dart';

class MyProjectScreen extends StatefulWidget {
  const MyProjectScreen({Key? key}) : super(key: key);

  @override
  State<MyProjectScreen> createState() => _MyProjectScreenState();
}

class _MyProjectScreenState extends State<MyProjectScreen> {
  String _message = "";

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
              _message ="hello";
            });
          },

        ),
      );
    }
    return Container(
      // child: Text("My Product"),
      child:  TabBarMyProject(),
    );
  }
}
