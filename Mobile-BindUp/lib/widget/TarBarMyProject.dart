import 'package:android/constants.dart';
import 'package:android/screens/project_application_screen.dart';
import 'package:android/screens/project_changelog_screen.dart';
import 'package:android/screens/project_member_screen.dart';
import 'package:android/screens/project_milestones_screen.dart';
import 'package:android/screens/project_overview_screen.dart';
import 'package:android/widget/MyProjectWidget.dart';
import 'package:flutter/material.dart';

class TabBarMyProject extends StatefulWidget {
  const TabBarMyProject({Key? key}) : super(key: key);

  @override
  State<TabBarMyProject> createState() => _TabBarMyProjectState();
}

class _TabBarMyProjectState extends State<TabBarMyProject> {
  List<String> tabProject = ["Owner Project", "Team Project"];
  int selectedIndex = 0;
  static final List<Widget> _widgetOptions = <Widget>[
    MyProjectWidget(isOwner: true,),
    MyProjectWidget(),
  ];


  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(top: 30.0),
      child: Column(
        children: [
          SizedBox(
            height: 30,
            child: ListView.builder(
              scrollDirection: Axis.horizontal,
              itemCount: tabProject.length,
              itemBuilder: (context,index) =>
                  buildTab(index),
            ),
          ),
          SizedBox(height: 10,),
          Expanded(
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
        padding: const EdgeInsets.symmetric(horizontal: 50),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text(
              tabProject[index],
              style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: 16,
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

