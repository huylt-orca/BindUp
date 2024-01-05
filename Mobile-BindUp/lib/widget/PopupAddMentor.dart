import 'package:android/constants.dart';
import 'package:android/controller/ProjectController.dart';
import 'package:android/services/MentorService.dart';
import 'package:android/services/ProjectService.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';

import '../models/Mentor.dart';

class PopupAddMentor extends StatefulWidget {
  const PopupAddMentor({Key? key}) : super(key: key);

  @override
  State<PopupAddMentor> createState() => _PopupAddMentorState();
}

class _PopupAddMentorState extends State<PopupAddMentor> {
  ProjectController projectController = Get.put(ProjectController());
  String keyword ="";

  void _runFilter (String value){

  }
  int _selectedItem = -1 ;
  List<String> items = ["1","2","3"];
  List<Mentor> mentors = List<Mentor>.empty(growable: true);

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    MentorService.fetchMentorList().then((data){
        setState(() {
          mentors = data;
        });
    });
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text('Add Mentor'),
      content:
      Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          // Form(
          //   child: Column(
          //
          //     children: <Widget>[
          //       TextField(
          //         onChanged: (value) {
          //           keyword=value;
          //           _runFilter(value);
          //         },
          //         decoration: InputDecoration(
          //             labelText: 'Search',
          //             suffixIcon: Icon(Icons.search)
          //         ),
          //       ),
          //     ],
          //   ),
          // ),
          // const SizedBox(height: 10,),
          Container(
            height: 250,
            width: 200,

            child: Column(
                children:[
                  Expanded(
                      child: ListView.builder(
                          itemCount: mentors.length,
                          itemBuilder: (context, index) {
                            return GestureDetector(
                              child: Container(
                                margin: EdgeInsets.symmetric(vertical: 2),
                                decoration: BoxDecoration(
                                    borderRadius: BorderRadius.circular(5),
                                    color: index == _selectedItem ? Colors.blueAccent :Colors.white,
                                    border: Border.all(width: 1,
                                      color: index == _selectedItem ? Colors.blueAccent :Colors.black,
                                    )
                                ),
                                child: ListTile(
                                  leading: ClipRRect(
                                    borderRadius: BorderRadius.circular(100),
                                    child: Image.network(imageDemo,width: 30,height: 30,),
                                  ),
                                  title: Text(mentors[index].name!,
                                    style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)
                                    ,),
                                  subtitle: Text(mentors[index].major!,
                                    style: TextStyle(fontSize: 12),
                                  ),

                                ),
                              ),
                              onTap: (){
                                setState(() {
                                  _selectedItem = index;
                                });
                              },
                            );
                          })),
                ]
            ),
          ),
        ],
      ),
      actions: <Widget>[
        TextButton(
          child: Text('Cancel'),
          onPressed: () {
            Navigator.of(context).pop();
          },
        ),
        ElevatedButton(
          child: Text('Add'),
          onPressed: () async{
            if (_selectedItem == -1 ){
              Fluttertoast.showToast(
                  msg: "Please choose mentor"
              );
            } else {
              bool isSuccessful = await ProjectService.postMentorToProject(mentors[_selectedItem].id!);
              if (isSuccessful){
                Fluttertoast.showToast(msg: 'Add Mentor Successful');
                projectController.mentors = RxList(await MentorService.fetchMentorListByProject());
                Navigator.of(context).pop();
              } else {
                Fluttertoast.showToast(msg: 'Add Mentor Failed');
              }
            }

          },
        ),
      ],
    );
  }
}
