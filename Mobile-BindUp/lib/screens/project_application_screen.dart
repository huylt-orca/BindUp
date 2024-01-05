import 'package:android/constants.dart';
import 'package:android/controller/ProjectController.dart';
import 'package:android/services/ApplicationService.dart';
import 'package:android/widget/ProjectMemberCard.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:line_awesome_flutter/line_awesome_flutter.dart';

import '../models/Application.dart';
import '../widget/PopupAddJob.dart';

class ProjectApplicationScreen extends StatefulWidget {
  const ProjectApplicationScreen({Key? key}) : super(key: key);

  @override
  State<ProjectApplicationScreen> createState() => _ProjectApplicationScreenState();
}

class _ProjectApplicationScreenState extends State<ProjectApplicationScreen> {
  ProjectController projectController = Get.put(ProjectController());

  List<Application> applications = List<Application>.empty(growable: true);

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    ApplicationService.fetchApplicationList(pageSize: 10).then((value){
      setState(() {
        applications = value;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 450,
      padding: EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.end,
            children: [
              ElevatedButton(
                  onPressed: (){
                    showDialog(
                      context: context,
                      builder: (BuildContext context) {
                        return  PopupAddJob();
                      },
                    );
                  },
                  child: Text('Create' ,
                    style: TextStyle(
                        color: Colors.white,
                        fontSize: 12,
                        fontWeight: FontWeight.bold
                    ),
                  )
              )
            ],
          ),

          applications.length == 0 ?
              Column(
                children: [
                  Icon(Icons.access_time_outlined,size: 50,),
                  SizedBox(height: 10,),
                  Text('No Applications'),
                ],
              ) :
          Expanded(
            child: ListView.builder(
                itemCount: applications.length,
                itemBuilder: (context,index) {
                return ProjectMemberCard(
                  id: applications[index].id!,
                    name: applications[index].user!.name!,
                    description: applications[index].description!,
                    image: applications[index].user!.avatar!, //== "" ? imageDemo : applications[index].user!.avatar! ,
                    isOwner: true,
                );
              }
            ),
          ),
        ],
      ),
    );
  }
}
