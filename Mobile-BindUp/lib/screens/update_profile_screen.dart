import 'dart:io';

import 'package:android/constants.dart';
import 'package:android/controller/UserController.dart';
import 'package:android/models/User.dart';
import 'package:android/services/UserService.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:line_awesome_flutter/line_awesome_flutter.dart';

class UpdateProfileScreen extends StatefulWidget {
  const UpdateProfileScreen({Key? key}) : super(key: key);

  @override
  State<UpdateProfileScreen> createState() => _UpdateProfileScreenState();
}

class _UpdateProfileScreenState extends State<UpdateProfileScreen> {
  UserController userController = Get.put(UserController());
  TextEditingController _txtFullName = TextEditingController();
  TextEditingController _txtEmail = TextEditingController();
  TextEditingController _txtAddress = TextEditingController();
  TextEditingController _txtHeadline = TextEditingController();
  TextEditingController _txtPhone = TextEditingController();
  int _gender = 0;
  File? image;

  final formKey = GlobalKey<FormState>();

  String? _validateFullName(String? value){
    if (value == null || value.isEmpty){
      return "Please enter Username";
    }
    return null;
  }

  String? _validateEmail(String? value){
    if (value == null || value.isEmpty){
      return "Please enter Username";
    }
    return null;
  }

  String? _validateHeadline(String? value){
    if (value == null || value.isEmpty){
      return "Please enter Username";
    }
    return null;
  }

  String? _validatePhone(String? value){
    if (value == null || value.isEmpty){
      return "Please enter Username";
    }
    return null;
  }

  String? _validateAddress(String? value){
    if (value == null || value.isEmpty){
      return "Please enter Username";
    }
    return null;
  }

  List<String> genders = ["Male", "FeMale"];

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    _txtFullName = TextEditingController(text: userController.name.value);
    _txtEmail = TextEditingController(text: userController.email.value);
    _txtAddress = TextEditingController(text: userController.address.value);
    _txtHeadline = TextEditingController(text: userController.headline.value);
    _txtPhone = TextEditingController(text: userController.phone.value);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: BackButton(),
        title: Text("Edit Profile",
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
                    child: ClipRRect(
                      borderRadius: BorderRadius.circular(100),
                      child:
                      userController.avatar.value != "" ?
                        Image.network(userController.avatar.value) :
                        Image.network(imageDemo),

                    ),
                  ),
                  Positioned(
                    bottom: 0,
                      right: 0,
                      child: Container(
                        width: 35,
                        height: 35,
                        decoration: BoxDecoration(
                          borderRadius: BorderRadius.circular(100),
                          color: mCustomMainColor
                        ),
                        child: const Icon(
                          LineAwesomeIcons.camera,
                          color: Colors.black,
                          size: 20,
                        ),
                      )
                  )
                ],
              ),
              const SizedBox(height: 50,),
              Form(
                key:formKey,
                  child:Column(
                    children: [
                      TextFormField(
                        validator: _validateFullName,
                        controller: _txtFullName,
                        decoration: const InputDecoration(
                          label: Text("Fullname"),
                          prefixIcon: Icon(LineAwesomeIcons.male)
                        ),
                      ),
                      const SizedBox(height: 10),
                      TextFormField(
                        validator: _validateEmail,
                        controller: _txtEmail,
                        decoration: const InputDecoration(
                            label: Text("Email"),
                            prefixIcon: Icon(LineAwesomeIcons.envelope)
                        ),
                      ),
                      const SizedBox(height: 10),
                      TextFormField(
                        validator: _validateAddress,
                        controller: _txtAddress,
                        decoration: const InputDecoration(
                            label: Text("Address"),
                            prefixIcon: Icon(LineAwesomeIcons.map)
                        ),
                      ),
                      const SizedBox(height: 20),
                      DropdownButtonFormField<String>(
                        value: genders[_gender],
                        items: genders.map((String gender) {
                          return DropdownMenuItem<String>(
                            value: gender,
                            child: Text(gender),
                          );
                        }).toList(),
                        onChanged: (String? value){
                          if (value =="Female"){
                            this._gender = 1;
                          }
                          if (value == "Male"){
                            this._gender = 0;
                          }
                        },
                        decoration: InputDecoration(
                          contentPadding: EdgeInsets.only(left: 20),
                          labelText: 'Gender',
                        ),
                      ),

                      const SizedBox(height: 10),
                      TextFormField(
                        validator: _validateHeadline,
                        controller: _txtHeadline,
                        decoration: const InputDecoration(
                            label: Text("Headline"),
                            prefixIcon: Icon(LineAwesomeIcons.book)
                        ),
                      ),
                      const SizedBox(height: 10),
                      TextFormField(
                        validator: _validatePhone,
                        controller: _txtPhone,
                        decoration: const InputDecoration(
                            label: Text("Phone"),
                            prefixIcon: Icon(LineAwesomeIcons.phone)
                        ),
                      ),
                      const SizedBox(height: 10,),
                      SizedBox(
                        width: 200,
                        child: ElevatedButton(
                          onPressed: () async{
                            if (formKey.currentState!.validate()){
                              User user = User(
                                  name:_txtFullName.text,
                                  email: _txtEmail.text,
                                  address: _txtAddress.text,
                                  gender: _gender,
                                  headline: _txtHeadline.text,
                                  phone: _txtPhone.text
                              );
                              bool isSuccessful =  await UserService.putUser(user);
                              if (isSuccessful){
                                Fluttertoast.showToast(msg: "Update Profile Successful");
                                User updateUser = await UserService.fetchUserDetail();
                                userController.AddUser(updateUser);
                              } else {
                                Fluttertoast.showToast(msg: "Update Profile Failed");
                              }
                            }
                          },
                          style: ElevatedButton.styleFrom(
                            side: BorderSide.none, shape: const StadiumBorder()
                          ),
                          child: const Text ("Edit",style: TextStyle(
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
