import 'package:flutter/material.dart';

import '../services/AuthService.dart';

class ChangePasswordScreen extends StatefulWidget {
  const ChangePasswordScreen({Key? key}) : super(key: key);

  @override
  State<ChangePasswordScreen> createState() => _ChangePasswordScreenState();
}

class _ChangePasswordScreenState extends State<ChangePasswordScreen> {
  TextEditingController _txtOldPassword = TextEditingController();
  TextEditingController _txtNewPassword = TextEditingController();
  TextEditingController _txtConfirmPassword = TextEditingController();
  AuthService authService = new AuthService();
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          leading: BackButton(),
          title: Text("Change Password"),
        ),
        body: Container(
          padding: EdgeInsets.symmetric(horizontal: 20),
          child: Column(children: [
            const SizedBox(
              height: 30,
            ),
            Form(
                child: Container(
              padding: EdgeInsets.symmetric(vertical: 20),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  TextFormField(
                    obscureText: true,
                    controller: _txtOldPassword,
                    decoration: const InputDecoration(
                      prefixIcon: Icon(Icons.fingerprint),
                      labelText: "Old Password",
                      hintText: "Old Password",
                      border: OutlineInputBorder(),
                      suffixIcon: IconButton(
                          onPressed: null,
                          icon: Icon(Icons.remove_red_eye_sharp)),
                    ),
                  ),
                  const SizedBox(
                    height: 10,
                  ),
                  TextFormField(
                    obscureText: true,
                    controller: _txtNewPassword,
                    decoration: const InputDecoration(
                      prefixIcon: Icon(Icons.fingerprint),
                      labelText: "New Password",
                      hintText: "New Password",
                      border: OutlineInputBorder(),
                      suffixIcon: IconButton(
                          onPressed: null,
                          icon: Icon(Icons.remove_red_eye_sharp)),
                    ),
                  ),
                  const SizedBox(
                    height: 10,
                  ),
                  TextFormField(
                    obscureText: true,
                    controller: _txtConfirmPassword,
                    decoration: const InputDecoration(
                      prefixIcon: Icon(Icons.fingerprint),
                      labelText: "Confirm Password",
                      hintText: "Confirm Password",
                      border: OutlineInputBorder(),
                      suffixIcon: IconButton(
                          onPressed: null,
                          icon: Icon(Icons.remove_red_eye_sharp)),
                    ),
                  ),
                  const SizedBox(
                    height: 10,
                  ),
                  SizedBox(
                    width: double.infinity,
                    child: ElevatedButton(
                        onPressed: () async {},
                        child: Text(
                          'Change Password',
                          style: TextStyle(color: Colors.white),
                        )),
                  ),
                ],
              ),
            )),
          ]),
        ));
  }
}
