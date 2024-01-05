import 'dart:convert';

import 'package:android/constants.dart';
import 'package:android/controller/UserController.dart';
import 'package:android/services/StorageService.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:get/get.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:http/http.dart' as http;

class AuthService{

  final FirebaseAuth _auth = FirebaseAuth.instance;
  final GoogleSignIn googleSignIn = GoogleSignIn();
  final UserController userController = Get.put(UserController());
  static final String urlAuth = server + "auth";
  static final String urlAccount = server + "accounts";


   Future<String> signInWithEmailAndPassword(String email, String password) async {
    var uri = Uri.parse(urlAuth);
    var body = jsonEncode({
      'username': email,
      'password': password,

    });
    var headers = {
      'Content-type': 'application/json',
      'Accept': 'application/json',
    };

    try {
      var response = await http.post(uri, body: body, headers: headers);

      if (response.statusCode == 200) {
        var responseData = jsonDecode(response.body);
        // await AuthService.sendToken();
        print("Access Token: " + responseData['token']);
        return responseData['token'];
      } else {

        print('Error: ${response.reasonPhrase}');
      }
    } catch (error) {
      // Xử lý lỗi khi không kết nối được đến API
      print('Error: $error');
    }
    return'';
  }


  Future<UserCredential> signInWithGoogle() async {

    await googleSignIn.signOut();
    final GoogleSignInAccount? googleSignInAccount = await googleSignIn.signIn();

    final GoogleSignInAuthentication googleSignInAuthentication =
    await googleSignInAccount!.authentication;


    final OAuthCredential credential = GoogleAuthProvider.credential(
      accessToken: googleSignInAuthentication.accessToken,
      idToken: googleSignInAuthentication.idToken,
    );

    final UserCredential authResult = await _auth.signInWithCredential(credential);
    final User? user = authResult.user;

    String token = await user!.getIdToken() ;
    print(token);
    final uri = Uri.parse(urlAuth+"/google");

    final body = jsonEncode(token);


    final headers = {
    'Content-type': 'application/json',
    'Accept': 'application/json',
    };

    try {
    final response = await http.post(uri, body: body, headers: headers);
    if (response.statusCode == 200) {
      print('Login Successful');
      final responseData = jsonDecode(response.body);
      StorageService.saveAccessToken(responseData['token']);

    } else {
    print('Error: ${response.reasonPhrase}');
    }
    } catch (error) {
    print('Error: $error');
    }
    return authResult;
  }

  Future<String> registerAccount(
  {required String username, required String password, required String fullname,
    required int gender,required String email,required String phone}
      ) async {
    var uri = Uri.parse(urlAuth + "/new");
    var body = jsonEncode({
      'username': username,
      'password': password,
      'gender': gender,
      'email': email,
      'name': fullname,
      'phone': phone
    });
    var headers = {
      'Content-type': 'application/json',
      'Accept': 'application/json',
    };

    try {
      var response = await http.post(uri, body: body, headers: headers);

      if (response.statusCode == 200) {
        print('Register Successful');
        var responseData = jsonDecode(response.body);
        print("Access Token: " + responseData['token']);
        return responseData['token'];
      } else {

        print('Error: ${response.reasonPhrase}');
      }
    } catch (error) {
      // Xử lý lỗi khi không kết nối được đến API
      print('Error: $error');
    }
    return'';
  }


  Future<void> signOut() async {
    await googleSignIn.signOut();
    await _auth.signOut();
    StorageService.removeAccessToken();
    userController.RemoveUser();
  }


  static Future<void> sendToken() async {
     UserController userController = Get.put(UserController());
    final uri = Uri.parse(urlAccount + "/${await StorageService.getDeviceToken()}");

    print(userController.id.value);
    var headers = {
      'Content-type': 'application/json',
      'Accept': 'application/json',
      'Authorization': 'Bearer ${await StorageService.getAccessToken()}',
    };


    try {
      var response = await http.post(uri, headers: headers);

      if (response.statusCode == 200) {
        print ('Send Device Token Successful');
      } else {
        print('Error: ${response.reasonPhrase}');
      }
    } catch (error) {
      // Xử lý lỗi khi không kết nối được đến API
      print('Error: $error');
    }
  }
}