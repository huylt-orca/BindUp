import 'package:android/models/Project.dart';
import 'package:android/screens/header_project.dart';
import 'package:android/widget/TabBarProject.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

import '../constants.dart';
import '../controller/ProjectController.dart';

class ProjectDetailScreen extends StatelessWidget {
  const ProjectDetailScreen(
      { Key? key
      }) : super(key: key);

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      resizeToAvoidBottomInset: false,
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0,
        leading: BackButton(color: Colors.black),
      ),
      body: SingleChildScrollView(
        child: Column(
          children: [
            HeaderProject(),
            TabBarProject(),
          ],
        ),
      ),
    );
  }
}
