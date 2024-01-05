

import 'package:android/controller/ProjectController.dart';
import 'package:android/screens/project_detail_screen.dart';
import 'package:android/services/ApplicationService.dart';
import 'package:android/statusType/ApplicationStatus.dart';
import 'package:android/widget/ProjectTypeWidget.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class ProjectMemberCard extends StatelessWidget {
  final String id;
  final String name;
  final String description;
  final String image;
  final bool isOwner ;
  const ProjectMemberCard(
      { Key? key,
        this.id = "",
        required this.name,
        required this.description,
        required this.image,
        this.isOwner =false
      }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    ProjectController projectController = Get.put(ProjectController());

    return GestureDetector(
      onTap: (){},
      child: Container(
        margin: EdgeInsets.all(10),
        padding: EdgeInsets.all(5),
        decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(5),
            border: Border.all(
                color: Colors.black,
                width: 1,
                style: BorderStyle.solid
            )
        ),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              margin: EdgeInsets.all(5),
              decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(10)
              ),
              child: ClipRRect(
                borderRadius: BorderRadius.circular(100),
                child: Image.network(
                  this.image,
                  width: 50,
                  height: 50,
                  fit: BoxFit.cover,
                ),
              ),
            ),
            Container(
              width: 200,
              margin: EdgeInsets.all(10),
              decoration: BoxDecoration(
                  color: Colors.white
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    this.name,
                    style: TextStyle(fontSize: 14, fontWeight: FontWeight.bold),
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis ,
                  ),
                  SizedBox(
                    height: 10,
                  ),
                  Text(
                    this.description,
                    style: TextStyle(fontSize: 12),
                    maxLines: 3,
                    overflow: TextOverflow.ellipsis ,
                  ),
                ],
              ),
            ),
            Visibility(
              visible: isOwner,
              child: Container(
                child: Column(
                  children: [
                    SizedBox(height: 5,),
                    SizedBox(
                      height:20,
                      child: ElevatedButton(
                          onPressed: () async{
                            await ApplicationService.putProject(id, ApplicationStatus.ACCEPTED);
                          },
                          style: ButtonStyle(
                            minimumSize: MaterialStateProperty.all(Size(50, 20)),
                            backgroundColor: MaterialStateProperty.all(Colors.green),
                            padding: MaterialStateProperty.all<EdgeInsets>(
                              EdgeInsets.symmetric(horizontal: 0, vertical: 0),
                          ),),
                          child: Text('Apply',
                          style: TextStyle(
                            color: Colors.white,
                            fontWeight: FontWeight.bold,
                            fontSize: 12
                          ),
                          )
                      ),
                    ),
                    SizedBox(height: 10,),
                    SizedBox(
                      height: 20,
                      child: ElevatedButton(
                          onPressed: () async{
                            await ApplicationService.putProject(id, ApplicationStatus.REJECTED);
                          },
                          style: ButtonStyle(
                            minimumSize: MaterialStateProperty.all(Size(50, 20)),
                            backgroundColor: MaterialStateProperty.all(Colors.red),
                            padding: MaterialStateProperty.all<EdgeInsets>(
                              EdgeInsets.zero,
                            ),),
                          child: Text('Reject',
                            style: TextStyle(
                                color: Colors.white,
                                fontWeight: FontWeight.bold,
                                fontSize: 12
                            ),
                          )
                      ),
                    ),
                  ],
                ),
              ),
            )
          ],
        ),
      ),
    );
  }
}
