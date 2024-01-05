import 'package:android/screens/login_screen.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:line_awesome_flutter/line_awesome_flutter.dart';

import '../controller/UserController.dart';
import '../models/User.dart';
import '../services/AuthService.dart';
import '../services/StorageService.dart';
import '../services/UserService.dart';

class SignUpScreen extends StatefulWidget {
  const SignUpScreen({Key? key}) : super(key: key);

  @override
  State<SignUpScreen> createState() => _SignUpScreenState();
}

class _SignUpScreenState extends State<SignUpScreen> {
  TextEditingController _txtEmail = TextEditingController();
  TextEditingController _txtPassword = TextEditingController();
  TextEditingController _txtUsername = TextEditingController();
  TextEditingController _txtName = TextEditingController();
  TextEditingController _txtPhone = TextEditingController();
  final formKey = GlobalKey<FormState>();

  String? _validateUsername(String? value){
    if (value == null || value.isEmpty){
      return "Please enter Username";
    }
    if (value.length < 8 || value.length > 40){
      return "Username must be between 8-40 characters";
    }
    return null;
  }
  String? _validatePassword(String? value){
    if (value == null || value.isEmpty){
      return "Please enter Password";
    }
    if (value.length < 8 || value.length > 40){
      return "Password must be between 8-40 characters";
    }
    return null;
  }
  String? _validateEmail(String? value){
    if (value == null || value.isEmpty){
      return "Please enter Email";
    }
    return null;
  }
  String? _validateName(String? value){
    if (value == null || value.isEmpty){
      return "Please enter Name";
    }
    if (value.length < 6 || value.length > 50){
      return "Fullname must be between 6-50 characters";
    }
    return null;
  }
  String? _validatePhone(String? value){
    if (value == null || value.isEmpty){
      return "Please enter Phone";
    }
    final pattern = r'^\d{10,11}$';
    final regExp = RegExp(pattern);

    if (!regExp.hasMatch(value)) {
      return "Phone have 10-11 digits, not have special character";
    }

    return null;
  }

  int _gender = 0;
  AuthService authService = new AuthService();
  List<String> genders = ["Male", "Female"];
  @override
  Widget build(BuildContext context) {

    return SafeArea(
      child: Scaffold(
          appBar: AppBar(
            backgroundColor: Colors.white,
            elevation: 0,
            leading: BackButton(color: Colors.black),
          ),
          body: SingleChildScrollView(
            child: Container(
              padding: const EdgeInsets.all(20),
              child: Column(
                children: [
                  Center(
                      child: Text("BindUp",
                        style: TextStyle(
                          fontSize: 52,
                          fontWeight: FontWeight.bold,
                        ),
                      )),
                  const SizedBox(height: 20,),
                  Text("Sign Up",
                    style: TextStyle(
                      fontSize: 32,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                  const SizedBox(height: 20,),

                  Form(
                    key: formKey,
                      child:Column(
                        children: [
                          TextFormField(
                            validator: _validateUsername,
                            controller: _txtUsername,
                            decoration: const InputDecoration(
                                prefixIcon: Icon(Icons.person_outline_outlined),
                                labelText: "Username",
                                hintText: "Username",
                            ),
                          ),
                          const SizedBox(height: 10,),
                          TextFormField(
                            validator: _validatePassword,
                            obscureText: true,
                            controller: _txtPassword,
                            decoration: const InputDecoration(
                              prefixIcon: Icon(Icons.fingerprint),
                              labelText: "Password",
                              hintText: "Password",
                              suffixIcon: IconButton(
                                  onPressed: null,
                                  icon: Icon(Icons.remove_red_eye_sharp)
                              ),
                            ),
                          ),
                          const SizedBox(height: 10,),
                          TextFormField(
                            validator: _validateName,
                            controller: _txtName,
                            decoration: const InputDecoration(
                                label: Text("Fullname"),
                                hintText: "Fullname",
                                prefixIcon: Icon(Icons.badge)
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
                            validator: _validateEmail,
                            controller: _txtEmail,
                            decoration: const InputDecoration(
                                label: Text("Email"),
                                hintText: "Email",
                                prefixIcon: Icon(LineAwesomeIcons.envelope)
                            ),
                          ),
                          const SizedBox(height: 10),
                          TextFormField(
                            validator: _validatePhone,
                            controller: _txtPhone,
                            decoration: const InputDecoration(
                                label: Text("Phone"),
                                hintText: "Phone",
                                prefixIcon: Icon(LineAwesomeIcons.phone)
                            ),
                          ),
                          const SizedBox(height: 10,),
                          SizedBox(
                            width: 200,
                            child: ElevatedButton(
                              onPressed: () async{
                                if (formKey.currentState!.validate()){
                                  try {
                                    String token = await authService
                                        .registerAccount(
                                        username:  _txtUsername.text,
                                        password: _txtPassword.text,
                                        email: _txtEmail.text,
                                        fullname: _txtName.text,
                                        gender: this._gender,
                                        phone: _txtPhone.text
                                    );
                                    StorageService.saveAccessToken(token);

                                    User user = await UserService.fetchUserDetail();

                                    final UserController userController = Get.put(UserController());

                                    userController.AddUser(user);
                                    print('Success');

                                    Fluttertoast.showToast(msg: "Register Successful");
                                    Navigator.pop(context);


                                  }catch (error){
                                    Fluttertoast.showToast(msg: "Register Failed");
                                    print('Failed');
                                  }
                                }
                              },
                              style: ElevatedButton.styleFrom(
                                  side: BorderSide.none, shape: const StadiumBorder()
                              ),
                              child: const Text ("Sign Up",style: TextStyle(
                                  color: Colors.white,
                                  fontSize: 20,
                                  fontWeight: FontWeight.bold
                              ),),
                            ),
                          ),

                          const SizedBox(
                            height: 20,
                          ),
                          TextButton(onPressed: () async{
                            Navigator.pushReplacement(
                              context,
                              MaterialPageRoute(builder: (context) => LoginScreen()),
                            );
                          },
                              child: Text.rich(
                                  TextSpan(
                                      text: "Already have an Account?",
                                      style: TextStyle(color: Colors.black),
                                      children: [
                                        TextSpan(
                                            text: " Login",
                                            style: TextStyle(color: Colors.blue)
                                        )
                                      ]
                                  )
                              )
                          )
                        ],
                      )
                  )
                ],
              ),
            ),
          ),
      ),
    );
  }
}
