import 'package:android/controller/ProjectController.dart';
import 'package:android/services/JobService.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
class PopupAddJob extends StatefulWidget {
  const PopupAddJob({Key? key}) : super(key: key);

  @override
  State<PopupAddJob> createState() => _PopupAddJobState();
}

class _PopupAddJobState extends State<PopupAddJob> {

  TextEditingController _txtTitle = TextEditingController();
  TextEditingController _txtDescription = TextEditingController();
  TextEditingController _txtDueDate = TextEditingController();

  ProjectController projectController = Get.put(ProjectController());
  final formKey = GlobalKey<FormState>();

  String? _validateTitle(String? value){
    if (value == null || value.isEmpty){
      return "Please enter Title";
    }
    if (value.length < 1 || value.length > 100 ){
      return "Title must be 1-100 characters";
    }
    return null;
  }

  String? _validateDescription(String? value){
    if (value == null || value.isEmpty){
      return "Please enter Description";
    }
    return null;
  }
  String? _validateDueDate(String? value){
    if (value == null || value.isEmpty){
      return "Please enter Due Date";
    }
    return null;
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text('Create Job'),
      content: Form(
        key: formKey,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            TextFormField(
              validator: _validateTitle,
              controller: _txtTitle,
              decoration: InputDecoration(
                  icon: Icon(Icons.work),
                  labelText: 'Title'),
            ),
            TextFormField(
              validator: _validateDescription,
              controller: _txtDescription,
              decoration: InputDecoration(
                  labelText: 'Description',
                icon: Icon(Icons.info)
              ),
            ),
            TextFormField(
              validator: _validateDueDate,
              controller: _txtDueDate,
              decoration: InputDecoration(
                  labelText: 'Due Date',
                icon: Icon(Icons.calendar_month),
              ),
              onTap: () async{
                DateTime? pickDate = await showDatePicker(
                    context: context,
                    initialDate: DateTime.now(),
                    firstDate: DateTime.now(),
                    lastDate: DateTime(2024));

                if (pickDate!=null){
                  setState(() {
                    _txtDueDate.text = DateFormat('yyyy-MM-dd').format(pickDate);
                  });
                }
              },
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
          child: Text('Create'),
          onPressed: () async {
            if (formKey.currentState!.validate()){
              bool isSuccessful = await JobService.postJob(_txtTitle.text, _txtDescription.text,_txtDueDate.text);
              if (isSuccessful){
                Fluttertoast.showToast(
                    msg: "Create Job Successful",
                    toastLength: Toast.LENGTH_SHORT,
                    gravity: ToastGravity.BOTTOM,
                    timeInSecForIosWeb: 1,
                    backgroundColor: Colors.grey[600],
                    textColor: Colors.white,
                    fontSize: 16.0
                );
                projectController.jobs = RxList(await JobService.fetchJobList());
                Navigator.of(context).pop();
              } else {
                Fluttertoast.showToast(
                    msg: "Create Job Failed",
                    toastLength: Toast.LENGTH_SHORT,
                    gravity: ToastGravity.BOTTOM,
                    timeInSecForIosWeb: 1,
                    backgroundColor: Colors.grey[600],
                    textColor: Colors.white,
                    fontSize: 16.0
                );
              }
            }
          },
        ),
      ],
    );
  }
}
