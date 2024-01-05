import 'package:android/models/User.dart' as UserModel;
import 'package:android/screens/signup_screen.dart';
import 'package:android/services/AuthService.dart';
import 'package:android/services/StorageService.dart';
import 'package:android/services/UserService.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

import '../controller/UserController.dart';
import 'bottom_bar.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({Key? key}) : super(key: key);

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  TextEditingController _txtEmail = TextEditingController();
  TextEditingController _txtPassword = TextEditingController();
  final formKey = GlobalKey<FormState>();
  AuthService authService = new AuthService();

  String? _validateUsername(String? value){
    if (value == null || value.isEmpty){
      return "Please enter Username";
    }

    return null;
  }

  String? _validatePassword(String? value){
    if (value == null || value.isEmpty){
      return "Please enter Password";
    }
    return null;
  }

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
                padding: EdgeInsets.symmetric(horizontal: 20),
                child: Column(
                  children: [
                    const SizedBox(height: 30,),
                     Center(
                         child: Text("BindUp",
                         style: TextStyle(
                           fontSize: 52,
                          fontWeight: FontWeight.bold,
                         ),
                         )),
                    const SizedBox(height: 20,),
                    Text("Welcome",
                      style: TextStyle(
                        fontSize: 32,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                    const SizedBox(height: 20,),
                    
                    Form(
                      key: formKey,
                        child: Container(
                          padding: EdgeInsets.symmetric(vertical: 20),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              TextFormField(
                                controller: _txtEmail,
                                validator: _validateUsername,
                                decoration: const InputDecoration(
                                  prefixIcon: Icon(Icons.person_outline_outlined),
                                  labelText: "Username",
                                  hintText: "Username",
                                  border: OutlineInputBorder()
                                ),
                              ),
                              const SizedBox(height: 30,),
                              TextFormField(
                                obscureText: true,
                                controller: _txtPassword,
                                validator: _validatePassword,
                                decoration: const InputDecoration(
                                    prefixIcon: Icon(Icons.fingerprint),
                                    labelText: "Password",
                                    hintText: "Password",
                                    border: OutlineInputBorder(),
                                  suffixIcon: IconButton(
                                      onPressed: null,
                                      icon: Icon(Icons.remove_red_eye_sharp)
                                  ),
                                ),
                              ),
                              const SizedBox(height: 10,),
                              Align(
                                alignment: Alignment.centerRight,
                                child: TextButton(
                                  onPressed: ()async{
                                    await authService.signOut();
                                  },
                                  child: const Text('Forgot Password?'),
                                ),
                              ),
                              SizedBox(
                                width: double.infinity,
                                child: ElevatedButton(
                                    onPressed: () async {
                                      try {
                                        if (formKey.currentState!.validate()){
                                          String token = await authService
                                              .signInWithEmailAndPassword(
                                              _txtEmail.text,
                                              _txtPassword.text
                                          );
                                          StorageService.saveAccessToken(token);

                                          UserModel.User user = await UserService.fetchUserDetail();

                                          final UserController userController = Get.put(UserController());

                                          userController.AddUser(user);
                                          print('Send token');
                                          await AuthService.sendToken();

                                          Navigator.pop(context);

                                          print('Success');
                                        }
                                      }catch (error){
                                        print('Failed');
                                      }
                                    },
                                    child: Text('LOGIN',
                                    style: TextStyle(
                                      color: Colors.white
                                    ),)
                                ),
                              ),
                            ],
                          ),
                        )
                    ),
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        const Text("OR"),
                        const SizedBox(height: 10,),

                        OutlinedButton(
                            onPressed: () async{
                              try{
                                UserCredential userCredential = await authService.signInWithGoogle();

                                UserModel.User user = await UserService.fetchUserDetail();

                                final UserController userController = Get.put(UserController());

                                userController.AddUser(user);

                                print('Send token');
                                await AuthService.sendToken();

                                Navigator.pop(context);

                                print('Successful');

                              } catch(error){
                                print(error);
                              }
                            },
                            child: Row(
                              crossAxisAlignment: CrossAxisAlignment.center,
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                Icon(Icons.g_mobiledata_outlined,color: Colors.black,),
                                Text('Sign-in with Google',
                                  style: TextStyle(color: Colors.black),
                                ),
                              ],
                            ))
                      ],
                    ),
                    const SizedBox(
                      height: 20,
                    ),
                    TextButton(onPressed: () async{
                      Navigator.pushReplacement(
                        context,
                        MaterialPageRoute(builder: (context) => SignUpScreen()),
                      );

                    },
                        child: Text.rich(
                          TextSpan(
                            text: "Don't have an Account?",
                            style: TextStyle(color: Colors.black),
                            children: [
                              TextSpan(
                                text: " Sign up",
                                style: TextStyle(color: Colors.blue)
                              )
                            ]
                          )
                        )
                    )
                  ],
                ),
            ),
          )
      ),
    );
  }
}
