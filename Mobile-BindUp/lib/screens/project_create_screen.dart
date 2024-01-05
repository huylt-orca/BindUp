import 'dart:io';

import 'package:android/constants.dart';
import 'package:android/models/Project.dart';
import 'package:android/models/Topic.dart';
import 'package:android/services/ProjectService.dart';
import 'package:android/services/TopicService.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:image_picker/image_picker.dart';
import 'package:line_awesome_flutter/line_awesome_flutter.dart';

import '../widget/MultiSelectWidget.dart';

class CreateProjectScreen extends StatefulWidget {
  const CreateProjectScreen({Key? key}) : super(key: key);

  @override
  State<CreateProjectScreen> createState() => _CreateProjectScreenState();
}

class _CreateProjectScreenState extends State<CreateProjectScreen> {
  File? image;

  ProjectService projectService = new ProjectService();

  TextEditingController _txtName = TextEditingController();
  TextEditingController _txtDescription = TextEditingController();
  TextEditingController _txtSummary = TextEditingController();
  TextEditingController _txtSource = TextEditingController();

  final formKey = GlobalKey<FormState>();

  String? _validateName(String? value){
    if (value == null || value.isEmpty){
      return "Please enter Project Name";
    }
    return null;
  }

  String? _validateDescription(String? value){
    if (value == null || value.isEmpty){
      return "Please enter Description";
    }
    return null;
  }

  String? _validateSummary(String? value){
    if (value == null || value.isEmpty){
      return "Please enter Summary";
    }
    return null;
  }

  String? _validateSource(String? value){
    if (value == null || value.isEmpty){
      return null;
    } else {
      // check source if have value
      return null;
    }

    return null;
  }

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    // selectedItem = items[0];
  }

  List<Topic> _selectedItems = [];

  void  _showMultiSelect() async {

    final List<Topic> items = await TopicService.fetchTopicList();

    final List<Topic>? results = await showDialog(
      context: context,
      builder: (BuildContext context) {
        return MultiSelect(items: items);
      },
    );

    // Update UI
    if (results != null) {
      setState(() {
        _selectedItems = results;
      });
    }
  }

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      appBar: AppBar(
        leading: BackButton(),
        title: Text("Create Project",
          style: TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 20
          ),
        ),
      ),
      body: SingleChildScrollView(
        child: Container(
          padding: const EdgeInsets.all(20),
          child: Column(
            children: [
              Stack(
                children: [
                  SizedBox(
                    width: 120,
                    height: 120,
                    child: this.image == null ? Image(
                        image:  AssetImage(imageUnknowPerson
                    ))
                    : Image.file(this.image!)
                    ,
                  ),
                  Positioned(
                      bottom: 0,
                      right: 0,
                      child: GestureDetector(
                        onTap: ()async{
                          try{
                            final image = await ImagePicker().pickImage(source: ImageSource.gallery);
                            if (image == null) return;
                            final imageTemporary = File(image.path);
                            setState(() => this.image = imageTemporary);
                          } on PlatformException catch (e){
                            print('Failed to pick image $e');
                          }
                        },
                        child: Container(
                          width: 35,
                          height: 35,
                          decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(100),
                              color: mCustomMainColor
                          ),
                          child:  Icon(
                            LineAwesomeIcons.camera,
                            color: Colors.black,
                            size: 20,
                          ),
                        ),
                      )
                  )
                ],
              ),
              const SizedBox(height: 10,),
              Form(
                key: formKey,
                  child:Column(
                    children: [
                      TextFormField(
                        validator: _validateName,
                        controller: _txtName,
                        decoration: const InputDecoration(
                            label: Text("Project Name"),
                            prefixIcon: Icon(LineAwesomeIcons.alternate_pencil)
                        ),
                      ),
                      const SizedBox(height: 10),


                      const SizedBox(height: 10),
                      TextFormField(
                        validator: _validateSummary,
                        controller: _txtSummary,
                        decoration: const InputDecoration(
                            label: Text("Summary"),
                            prefixIcon: Icon(LineAwesomeIcons.envelope)
                        ),
                      ),
                      const SizedBox(height: 10),
                      TextFormField(
                        validator: _validateSource,
                        controller: _txtSource,
                        decoration: const InputDecoration(
                            label: Text("Source"),
                            prefixIcon: Icon(LineAwesomeIcons.map)
                        ),
                      ),
                      const SizedBox(height: 10),

                      TextFormField(
                        validator: _validateDescription,
                        controller: _txtDescription,
                        decoration: const InputDecoration(
                            label: Text("Description"),
                            prefixIcon: Icon(LineAwesomeIcons.book)
                        ),
                      ),
                      const SizedBox(height: 20,),

                      SizedBox(
                        width: MediaQuery.of(context).size.width,
                        height: 40,
                        child: ElevatedButton(
                          onPressed: _showMultiSelect,
                          style: ButtonStyle(
                            backgroundColor: MaterialStateProperty.all<Color>(Colors.white),
                          ),
                          child:  Row(
                            mainAxisAlignment: MainAxisAlignment.start,
                            children: [
                              Icon(LineAwesomeIcons.accusoft,color: Colors.grey),
                              const SizedBox(width: 10,),
                              Text('Select Topic',
                                style: TextStyle(
                                  fontSize: 16,
                                  color: Colors.grey,
                                  fontWeight: FontWeight.w400
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                      // display selected items
                      Wrap(
                        children: _selectedItems
                            .map((e) => Chip(
                          label: Text(e.name!),
                        ))
                            .toList(),
                      ),


                      // const SizedBox(height: 10,),
                      // ElevatedButton(
                      //   onPressed: ()async{
                      //     try{
                      //       final image = await ImagePicker().pickImage(source: ImageSource.gallery);
                      //       if (image == null) return;
                      //       final imageTemporary = File(image.path);
                      //       setState(() => this.imageProject.add(imageTemporary));
                      //     } on PlatformException catch (e){
                      //       print('Failed to pick image $e');
                      //     }
                      //   },
                      //   child: Text('Add Image'),
                      // ),
                      // const SizedBox(height: 10,),
                      // Expanded(child: ListView.builder(
                      //     itemCount: imageProject.length,
                      //     scrollDirection: Axis.horizontal,
                      //     itemBuilder: (context,index){
                      //       return Container(
                      //         padding: EdgeInsets.all(8),
                      //         child: Image.file(imageProject[index]),
                      //       );
                      //     },
                      //
                      // )),
                      const SizedBox(height: 10,),
                      SizedBox(
                        width: 200,
                        child: ElevatedButton(
                          onPressed: ()async{
                            if (formKey.currentState!.validate()){
                              Project project = new Project(
                                  name: _txtName.text,
                                  summary: _txtSummary.text,
                                  description: _txtDescription.text,
                                  source: _txtSource.text
                              );
                              ProjectService.postProject(project, this.image,this._selectedItems);
                              Fluttertoast.showToast(msg: 'Create Project Successful');
                              Navigator.pop(context);
                            }
                          },
                          style: ElevatedButton.styleFrom(
                              side: BorderSide.none, shape: const StadiumBorder()
                          ),
                          child: const Text ("Create",style: TextStyle(
                              color: Colors.white,
                              fontSize: 20,
                              fontWeight: FontWeight.bold
                          ),),
                        ),
                      )
                    ],
                  )
              )
            ],
          ),
        ),
      ),
    );
  }
}
