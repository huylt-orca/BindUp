import 'package:android/constants.dart';
import 'package:android/screens/project_milestones_screen.dart';
import 'package:android/widget/MyProjectCard.dart';
import 'package:android/widget/ProjectMilestonesCard.dart';
import 'package:flutter/material.dart';

class TestScreen extends StatefulWidget {
  const TestScreen({Key? key}) : super(key: key);

  @override
  State<TestScreen> createState() => _TestScreenState();
}

class _TestScreenState extends State<TestScreen> {
  List<String> list  = ["Luu","Thanh","Huy","Luu","Thanh","Huy","Luu","Thanh","Huy",
      "Luu","Thanh","Huy","Luu","Thanh","Huy","Luu","Thanh","Huy","Luu","Thanh","Huy",
    "Luu","Thanh","Huy","Luu","Thanh","Huy","Luu","Thanh","Huy","Luu","Thanh","Huy",
    "Luu","Thanh","Huy","Luu","Thanh","Huy","Luu","Thanh","Huy","Luu","Thanh","Huy",
    "Luu","Thanh","Huy","Luu","Thanh","Huy","Luu","Thanh","Huy","Luu","Thanh","Huy",

  ];
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: [
          Expanded(
            child: Padding(
              padding: const EdgeInsets.all(8.0),
              child: Column(
                children: [
                  Container(
                    child: Text("kdsjafl"),
                  ),
                  const SizedBox(height: 20,),
                  Expanded(
                    child: ListView.builder(
                      itemCount: list.length,
                        itemBuilder: (context,index){
                        return Text("Not user");
                          // return MyProjectCard(name: list[index], description: "1dk",image: imageDemo,);
                        }),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
