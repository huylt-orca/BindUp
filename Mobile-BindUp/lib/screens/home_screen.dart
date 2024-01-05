import 'package:android/screens/my_project_screen.dart';
import 'package:android/widget/ListViewProjectHome.dart';
import 'package:android/widget/ProjectTypeWidget.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../widget/ProjectCard.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    // return Container(
    //   child: Column(
    //     children: [
    //       Container(
    //         // height: 100,
    //         padding: EdgeInsets.only(top: 15,bottom: 15),
    //         decoration: BoxDecoration(
    //           color: Color(0xFFEDECF2),
    //           borderRadius: BorderRadius.only(
    //             // topLeft: Radius.circular(35),
    //             topRight: Radius.circular(35),
    //           )
    //         ),
    //         child: Column(
    //           children: [
    //             Container(
    //               margin: EdgeInsets.symmetric(horizontal: 15),
    //               padding: EdgeInsets.symmetric(horizontal: 15),
    //               height: 50,
    //               decoration: BoxDecoration(
    //                 color: Colors.white,
    //                 borderRadius: BorderRadius.circular(30)
    //               ),
    //               child: Row(
    //                 children: [
    //                   Container(
    //                     margin: EdgeInsets.only(left: 5),
    //                     height: 50,
    //                     width: 300,
    //                     child: TextFormField(
    //                       decoration: InputDecoration(
    //                         border: InputBorder.none,
    //                         hintText: "Search project...",
    //                       ),
    //                     ),
    //                   ),
    //                   Spacer(),
    //                   Icon(
    //                       Icons.search
    //                   )
    //                 ],
    //
    //               ),
    //             )
    //           ],
    //         ),
    //       ),
    //       ProjectCard(),
    //
    //     ],
    //   ),
    // );
    return Container(
      child:  ListViewProjectHome(),
    );
  }
}
