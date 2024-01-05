import 'package:android/screens/home_screen.dart';
import 'package:android/screens/my_project_screen.dart';
import 'package:android/screens/notification_screen.dart';
import 'package:android/screens/profile_screen.dart';
import 'package:fluentui_icons/fluentui_icons.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class BottomBar extends StatefulWidget {
  const BottomBar({Key? key}) : super(key: key);

  @override
  State<BottomBar> createState() => _BottomBarState();
}

class _BottomBarState extends State<BottomBar> {
  int _selectedIndex = 0;
  static final List<Widget> _widgetOptions = <Widget>[
    HomeScreen(),
    MyProjectScreen(),
   NotificationScreen(),
    ProfileScreen(),
  ];

  void _onItemTapped(int index){
    setState((){
      _selectedIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      appBar: AppBar(
        title:
          Text("BindUP"),
        // leading: IconButton(
        //   icon: Icon(Icons.account_circle),
        //   iconSize: 30.0,
        //   onPressed: () {  },
        // ),

      ),
      body: Center(
        child: _widgetOptions[_selectedIndex],
      ),
      bottomNavigationBar: BottomNavigationBar(
        onTap: _onItemTapped,
        currentIndex: _selectedIndex,
        elevation: 10,
        showSelectedLabels: false,
        showUnselectedLabels: false,
        selectedItemColor: Colors.blueGrey,
        type: BottomNavigationBarType.fixed,
        unselectedItemColor: const Color(0xFF526480),
        items: const [
          BottomNavigationBarItem(icon: Icon(FluentSystemIcons.ic_fluent_home_regular),
              activeIcon: Icon(FluentSystemIcons.ic_fluent_home_filled),
              label: "Home"),
          BottomNavigationBarItem(icon: Icon(FluentSystemIcons.ic_fluent_bookmark_regular),
              activeIcon: Icon(FluentSystemIcons.ic_fluent_bookmark_filled),
              label: "Product"),
          BottomNavigationBarItem(icon: Icon(FluentSystemIcons.ic_fluent_alert_regular),
              activeIcon: Icon(FluentSystemIcons.ic_fluent_alert_filled),
              label: "Notification"),
          BottomNavigationBarItem(icon: Icon(FluentSystemIcons.ic_fluent_person_regular),
              activeIcon: Icon(FluentSystemIcons.ic_fluent_person_filled),
              label: "Profile"),
        ],
      ),
    );
  }
}
