import 'package:android/constants.dart';
import 'package:android/controller/ProjectController.dart';
import 'package:android/controller/UserController.dart';
import 'package:android/models/Project.dart';
import 'package:android/models/ProjectImage.dart';
import 'package:android/screens/project_application_screen.dart';
import 'package:android/screens/project_changelog_screen.dart';
import 'package:android/screens/project_member_screen.dart';
import 'package:android/screens/project_milestones_screen.dart';
import 'package:android/screens/project_overview_screen.dart';
import 'package:android/services/ProjectService.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class TabBarProject extends StatefulWidget {
  const TabBarProject({Key? key}) : super(key: key);

  @override
  State<TabBarProject> createState() => _TabBarProjectState();
}

class _TabBarProjectState extends State<TabBarProject> {
  ProjectController projectController = Get.put(ProjectController());
  UserController userController = Get.put(UserController());
  List<String> tabProject = ["Overview", "Milestones","Member", "Changelog"];
  int selectedIndex = 0;
  static final List<Widget> _widgetOptions = <Widget>[
    ProjectOverviewScreen(),
    ProjectMilestonesScreen(),
    ProjectMemberScreen(),
    ProjectChangelogScreen(),
    ProjectApplicationScreen(),
  ];

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    if (projectController.founder.value.id == userController.id.value){
      tabProject = ["Overview", "Milestones","Member", "Changelog", "Application"];
    } else{
      tabProject = ["Overview", "Milestones","Member", "Changelog"];
    }
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 10.0),
      child: Column(
        children: [
          SizedBox(
            height: 25,
            child: ListView.builder(
              scrollDirection: Axis.horizontal,
                itemCount: tabProject.length,
                itemBuilder: (context,index) =>
                    buildTab(index),
            ),
          ),
          SizedBox(height: 10,),
          Container(
            child: _widgetOptions[selectedIndex],
          )
        ],
      ),
    );
  }

  Widget buildTab (int index){
    return GestureDetector(
      onTap: () {
        setState(() {
          selectedIndex = index;
        });
      },
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 17),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text(
                tabProject[index],
              style: TextStyle(
                fontWeight: FontWeight.bold,
                color: selectedIndex == index ? kTextColor: kTextLightColor
              ),
            ),
            Container(
              margin: EdgeInsets.only(top:5),
              height: 2,
              width: 30,
              color: selectedIndex == index ? Colors.black : Colors.transparent,
            )
          ],
        ),
      ),
    );
  }

}

