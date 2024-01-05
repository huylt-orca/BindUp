import 'package:android/services/ChangeLogService.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';

class PopupCreateChangelog extends StatefulWidget {
  const PopupCreateChangelog({Key? key}) : super(key: key);

  @override
  State<PopupCreateChangelog> createState() => _PopupCreateChangelogState();
}

class _PopupCreateChangelogState extends State<PopupCreateChangelog> {
  TextEditingController _txtTitle = TextEditingController();
  TextEditingController _txtDescription = TextEditingController();
 final formKey = GlobalKey<FormState>();

  String? _validateTitle(String? value){
    if ( value!.isEmpty){
      return "Please enter Title";
    }
    if (value.length < 10 || value.length > 100){
      return "Title must be between 10-100 characters";
    }
    return null;
  }

  String? _validateDescription(String? value){
    if ( value!.isEmpty){
      return "Please enter Description";
    }
    return null;
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text('Create Changelog'),
      content: Form(
        key: formKey,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            TextFormField(
              validator: _validateTitle,
              controller: _txtTitle,
              decoration: InputDecoration(
                  labelText: 'Title',
                  icon: Icon(Icons.history)
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
          child: Text('Create'),
          onPressed: () async{
            if (formKey.currentState!.validate()){
              bool isSuccessful =  await ChangelogService.postChangelog(_txtTitle.text, _txtDescription.text);
              if (isSuccessful) {
                Fluttertoast.showToast(
                    msg: "Create Changelog Successful",
                    toastLength: Toast.LENGTH_SHORT,
                    gravity: ToastGravity.BOTTOM,
                    timeInSecForIosWeb: 1,
                    backgroundColor: Colors.grey[600],
                    textColor: Colors.white,
                    fontSize: 16.0
                );
                Navigator.of(context).pop();
              } else {
                Fluttertoast.showToast(msg: "Create Changelog Failed");
              }
            }
          },
        ),
      ],
    );
  }
}
