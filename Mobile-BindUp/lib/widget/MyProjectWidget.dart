import 'package:android/constants.dart';
import 'package:android/models/responsetest.dart';
import 'package:android/screens/project_create_screen.dart';
import 'package:android/services/ProjectService.dart';
import 'package:android/services/UserService.dart';
import 'package:android/widget/MyProjectCard.dart';
import 'package:flutter/material.dart';

import '../models/Project.dart';

class MyProjectWidget extends StatefulWidget {
  final bool isOwner ;
  const MyProjectWidget({Key? key, this.isOwner =false }) : super(key: key);

  @override
  State<MyProjectWidget> createState() => _MyProjectWidgetState();
}

class _MyProjectWidgetState extends State<MyProjectWidget> {
  List<Project> list = List<Project>.empty(growable: true);
  bool _isWhat = true;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();

  }

  @override
  Widget build(BuildContext context) {
    if (this.widget.isOwner && _isWhat){
      ProjectService.fetchOwnerProjectList().then(
              (data) {
            setState(() {
              list = data;
            });});
      _isWhat = !_isWhat;
    }
    if (!this.widget.isOwner && !_isWhat){
      UserService.fetchProjectListByUser().then((data) {
        setState(() {
          list = data;
        });
      });
      _isWhat = !_isWhat;
    }
    return Padding(
      padding: const EdgeInsets.all(10),
      child: Column(
        children: [
           Row(
            mainAxisAlignment: MainAxisAlignment.end,
            children: [
              Visibility(
                visible: this.widget.isOwner,
                child: Container(
                  margin: const EdgeInsets.only(right: 10),
                  child: ElevatedButton(
                      onPressed: (){
                          Navigator.push(
                            context,
                            MaterialPageRoute(builder: (context) =>
                                CreateProjectScreen(),
                            ),
                          );
                      },
                      child: Text("Create"),
                  ),
                ),
              )
            ],
          ),
          list.isEmpty ? Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              SizedBox(height: 50,),
              Icon(Icons.add_circle,size: 50,),
              SizedBox(height: 10,),
              Text("No Project"),
            ],
          ) :
          Expanded(
            child: ListView.builder(
                itemCount: list.length,
                itemBuilder: (context, index){
                  return MyProjectCard(project: list[index],);
                  // return Text(list[index].title!);
                },

            ),
          ),
        ],
      ),
    );
  }
}
