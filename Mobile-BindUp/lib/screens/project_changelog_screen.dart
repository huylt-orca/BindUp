import 'package:android/controller/ProjectController.dart';
import 'package:android/controller/UserController.dart';
import 'package:android/models/User.dart';
import 'package:android/services/ChangeLogService.dart';
import 'package:android/widget/ChangeLogCard.dart';
import 'package:android/widget/PopupCreateChangelog.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

import '../models/Changelog.dart';

class ProjectChangelogScreen extends StatefulWidget {
  const ProjectChangelogScreen({Key? key}) : super(key: key);

  @override
  State<ProjectChangelogScreen> createState() => _ProjectChangelogScreenState();
}

class _ProjectChangelogScreenState extends State<ProjectChangelogScreen> {
  ProjectController projectController = Get.put(ProjectController());
  UserController userController = Get.put(UserController());
  final scrollController = ScrollController();
  List<Changelog> changelogs = List<Changelog>.empty(growable: true);
  int page =0;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    scrollController.addListener(_scrollListener);
    ChangelogService.fetchChangelogList(projectId: projectController.id.value).then(
            (data)  {
              setState(() {
                changelogs = data;
              });
            }
    );
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 450,
      padding: EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: [
          Visibility(
            visible: projectController.founder.value.id == userController.id.value ,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                ElevatedButton(
                    onPressed: () async{
                      await showDialog(
                        context: context,
                        builder: (BuildContext context) {
                          return  PopupCreateChangelog();
                        },
                      );
                      await ChangelogService.fetchChangelogList(projectId: projectController.id.value).then(
                              (data)  {
                            setState(() {
                              changelogs = data;
                            });
                          }
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
          ),

          changelogs.length == 0 ?
              Center(child: Column(
                children: [
                  SizedBox(height: 50,),
                  Icon(Icons.history,size: 100,),
                  SizedBox(height: 10,),
                  Text('No Changelog'),
                ],
              ),) :
          Expanded(
              child: ListView.builder(
                itemCount: changelogs.length,
                  itemBuilder: (context,index){
                  return ChangeLogCard(
                    title: changelogs[index].title!,
                    description: changelogs[index].description!,
                    time: changelogs[index].createdDate!,
                  );
                  }
              )
          )
        ],
      ),
    );
  }

  void _scrollListener(){
    if (scrollController.position.pixels == scrollController.position.maxScrollExtent){
      this.page++;
      ChangelogService.fetchChangelogList(projectId: projectController.id.value,page: this.page).then((data){
        setState(() {
          changelogs.addAll(data);
        });
      });
    }
  }

}
