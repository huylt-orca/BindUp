import 'package:android/constants.dart';
import 'package:android/controller/ProjectController.dart';
import 'package:android/controller/UserController.dart';
import 'package:android/models/Project.dart';
import 'package:android/models/ProjectImage.dart';
import 'package:android/screens/project_detail_screen.dart';
import 'package:android/services/ProjectService.dart';
import 'package:android/widget/ProjectTypeWidget.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';

class ProjectCard extends StatefulWidget {
  final Project project;
  const ProjectCard(
      { Key? key,
        required this.project
      }) : super(key: key);

  @override
  State<ProjectCard> createState() => _ProjectCardState();
}

class _ProjectCardState extends State<ProjectCard> {
  String? _getImage(){
    return this.widget.project.logo!.isEmpty ? imageDemo : this.widget.project.logo  ;
  }
  bool isVote = true;
  UserController userController = Get.put(UserController());

  String getMilestone(){
    switch (this.widget.project.milestone){
      case 0:
        return "Idea";
        break;
      case 1:
        return "Upcoming";
        break;
      case 2:
        return "Launching";
        break;
      case 3:
        return "Finished";
        break;
        default: return "Idea";
    }
  }

  Color getMilestoneColor(){
    switch (this.widget.project.milestone){
      case 0:
        return Colors.green;
        break;
      case 1:
        return Colors.grey;
        break;
      case 2:
        return Colors.blueAccent;
        break;
      case 3:
        return Colors.deepOrange;
        break;
      default: return Colors.black;
    }
  }

  @override
  Widget build(BuildContext context) {

    return GestureDetector(
      onTap: ()async{
        print(this.widget.project.id);
        await ProjectService.fetchProjectDetail(this.widget.project.id!);

        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) =>
             ProjectDetailScreen()
          )
        );
      },
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
        child: Stack(
          children: [
            Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Container(
                  margin: EdgeInsets.all(5),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(10)
                  ),
                  child: ClipRRect(
                    borderRadius: BorderRadius.circular(10),
                    child: Image.network(
                      _getImage()!,
                      width: 80,
                      height: 80,
                      fit: BoxFit.cover,
                    ),
                  ),
                ),
                Container(
                  width: 200,
                  margin: EdgeInsets.all(5),
                  decoration: BoxDecoration(
                    color: Colors.white
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        this.widget.project.name!,
                        style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis ,
                      ),
                      SizedBox(
                        height: 5,
                      ),
                      ProjectTypeWidget(text: getMilestone(),boxColor: getMilestoneColor()),
                      SizedBox(
                        height: 5,
                      ),
                      Text(
                      this.widget.project.name!,
                     style: TextStyle(fontSize: 12),
                        maxLines: 3,
                        overflow: TextOverflow.ellipsis ,
                      ),

                      // Row(
                      //   children: [
                      //     ProjectTypeWidget(text: "Topic",boxColor: Colors.deepOrange),
                      //     ProjectTypeWidget(text: "Topic",boxColor: Colors.blue, textColor: Colors.red,),
                      //     ProjectTypeWidget(text: "Topic"),
                      //   ],
                      // )
                    ],
                  ),
                ),

              ],
            ),
            Positioned(
                right: 0,
                top: 25  ,
                child: ElevatedButton(
                  style: ButtonStyle(
                    backgroundColor: MaterialStateProperty.all(Colors.lightBlueAccent) ,
                      padding: MaterialStateProperty.all(EdgeInsets.symmetric(vertical: 5))
                  ),
                  onPressed: () {
                    if (userController.id.value == ""){
                      Fluttertoast.showToast(
                          msg: "Please login",
                          toastLength: Toast.LENGTH_SHORT,
                          gravity: ToastGravity.CENTER,
                          timeInSecForIosWeb: 1,
                          backgroundColor: Colors.grey[600],
                          textColor: Colors.white,
                          fontSize: 16.0
                      );
                    } else {
                    ProjectService.postVoteToProject(this.widget.project.id!).then(
                        (data){
                          setState(() {
                            this.widget.project.voteQuantity = data;
                          });
                        }
                    );
                    }
                  },
                  child: Column(
                    children: [
                      Icon(Icons.arrow_drop_up),
                      Text(this.widget.project.voteQuantity.toString())
                    ],
                  )
                )
            ),
          ],
        ),
      ),
    );
  }
}
