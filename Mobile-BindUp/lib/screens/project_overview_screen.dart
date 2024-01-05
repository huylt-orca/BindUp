import 'package:android/constants.dart';
import 'package:android/controller/ProjectController.dart';
import 'package:android/controller/UserController.dart';
import 'package:android/widget/PopupApplyJob.dart';
import 'package:android/widget/ProjectTypeWidget.dart';
import 'package:flutter/material.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:get/get.dart';

import '../models/ProjectImage.dart';

class ProjectOverviewScreen extends StatelessWidget {
  // final List<String> imageUrls = imageUrlsDemo;
  const ProjectOverviewScreen({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    UserController userController = Get.put(UserController());
    final ProjectController projectController = Get.put(ProjectController());
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10),
      height: 450,
      child: SingleChildScrollView(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
           projectController.images.length !=0
            ? SizedBox(
              height: 100,
              child: ListView.builder(
                scrollDirection: Axis.horizontal,
                itemCount: projectController.images.length,
                itemBuilder: (BuildContext context, int index) {
                  return Padding(
                    padding: EdgeInsets.symmetric(horizontal: 10.0),
                    child: SizedBox(
                      width: 100,
                        child: Image.network(projectController.images[index].directory!, fit: BoxFit.cover)),
                  );
                },
              ),
            )
            : SizedBox(height: 0,),
            Container(
              height: 50,
              child: ListView.builder(
                scrollDirection: Axis.horizontal,
                itemCount: projectController.topics.length,
                itemBuilder: (BuildContext context, int index) {
                  Color newColor = Colors.blue;
                  switch (index % 3){
                    case 0:
                      newColor = Colors.blue;
                      break;
                    case 1:
                      newColor = Colors.orange;
                      break;
                    case 2:
                      newColor = Colors.green;
                      break;
                  }
                  return ProjectTypeWidget(text:projectController.topics[index].shortName!,boxColor: newColor, );
                },
              ),
            ),
            Text("Description",
            style: TextStyle(
              fontSize: 20,
              fontWeight: FontWeight.bold
            ),),
            Html(
                data: projectController.description.value
            ),
            // SizedBox(height: 10,),

            projectController.jobs.length != 0 ?
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text("Jobs", style: TextStyle(fontSize: 20,
                        fontWeight: FontWeight.bold,),),
                    Visibility(
                      visible: userController.id.value != projectController.founder!.value.id && userController.id.value != "",
                      child: ElevatedButton(
                          onPressed: (){
                            print( userController.id.value);
                            showDialog(
                              context: context,
                              builder: (BuildContext context) {
                                return  PopupApplyJob();
                              },
                            );
                          },
                          child: Text('Apply' ,
                            style: TextStyle(
                              fontSize: 12,
                                color: Colors.white,
                              fontWeight: FontWeight.bold
                            ),
                          )
                      ),
                    )
                  ],
                ),
                Container(
                  height:  415,
                  child: ListView.builder(
                    itemCount: projectController.jobs.length,
                      itemBuilder: (context,index){
                        // return Text(projectController.jobs[index].name!);
                        return Container(
                          margin: EdgeInsets.all(3),
                          decoration: BoxDecoration(
                            border: Border.all(),
                            borderRadius: BorderRadius.circular(5.0)
                          ),
                          child: ListTile(
                            title: Text(projectController.jobs[index].name!,
                            style: TextStyle(fontSize: 16,fontWeight: FontWeight.bold),
                            ),
                            subtitle: Column(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(projectController.jobs[index].description!),
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.end,
                                  children: [
                                    Text(projectController.jobs[index].duaDate!,
                                    style: TextStyle(color: Colors.red),
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          ),
                        );
                      }),
                ),
              ],
            )
            :  SizedBox(height: 0),
          ],
        ),
      ),
    );
  }
}


