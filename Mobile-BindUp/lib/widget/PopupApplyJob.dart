import 'package:android/controller/ProjectController.dart';
import 'package:android/services/ApplicationService.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';

import '../models/Job.dart';

class PopupApplyJob extends StatefulWidget {
  const PopupApplyJob({Key? key}) : super(key: key);

  @override
  State<PopupApplyJob> createState() => _PopupApplyJobState();
}

class _PopupApplyJobState extends State<PopupApplyJob> {
  ProjectController projectController = Get.put(ProjectController());

  List<Job> jobs = List<Job>.empty(growable: true);

  Job? _seletedJob ;

  final formKey = GlobalKey<FormState>();
  TextEditingController _txtDescription = TextEditingController();

  String? _validateDescription(String? value){
    if (value == null || value.isEmpty){
      return "Please enter description";
    }
    return null;
  }

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    jobs =  projectController.jobs;
    if (jobs.isNotEmpty){
      _seletedJob = jobs[0];
    }
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text('Apply Job'),
      content: Form(
        key: formKey,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            DropdownButtonFormField<Job>(
              value: _seletedJob,
              items: jobs.map((Job job) {
                return DropdownMenuItem<Job>(
                  value:  job,
                  child: Text(job.name!),
                );
              }).toList(),
              onChanged: (Job? value){
                  _seletedJob = value;
              },
              decoration: InputDecoration(
                contentPadding: EdgeInsets.only(left: 0),
                labelText: 'Jobs',
              ),
            ),
            const SizedBox(height: 10,),
            TextFormField(
              validator: _validateDescription,
              controller: _txtDescription,
              decoration: InputDecoration(
                  labelText: 'Description',
                icon: Icon(Icons.description)
              ),
            ),
          ],
        ),
      ),
      actions: <Widget>[
        TextButton(
          child: Text('Cancel'),
          onPressed: () {
            Navigator.of(context).pop();
          },
        ),
        ElevatedButton(
          child: Text('Apply'),
          onPressed: () async{
            if (formKey.currentState!.validate()) {
              bool isSuccess = await ApplicationService.postApplication(
                  _txtDescription.text, _seletedJob!.id!);
              if (isSuccess) {
                Fluttertoast.showToast(
                    msg: "Apply Job",
                    toastLength: Toast.LENGTH_SHORT,
                    gravity: ToastGravity.BOTTOM,
                    timeInSecForIosWeb: 1,
                    backgroundColor: Colors.grey[600],
                    textColor: Colors.white,
                    fontSize: 16.0
                );
              } else {
                Fluttertoast.showToast(
                    msg: "User already apply",
                    toastLength: Toast.LENGTH_SHORT,
                    gravity: ToastGravity.BOTTOM,
                    timeInSecForIosWeb: 1,
                    backgroundColor: Colors.grey[600],
                    textColor: Colors.white,
                    fontSize: 16.0
                );
              }
              Navigator.of(context).pop();
            }
          },
        ),
      ],
    );
  }
}
