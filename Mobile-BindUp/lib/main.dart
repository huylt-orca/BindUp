
import 'package:android/ForGroundLocalNotification.dart';
import 'package:android/constants.dart';
import 'package:android/controller/UserController.dart';
import 'package:android/screens/bottom_bar.dart';
import 'package:android/screens/login_screen.dart';
import 'package:android/screens/project_detail_screen.dart';
import 'package:android/screens/signup_screen.dart';
import 'package:android/screens/test_screen.dart';
import 'package:android/services/AuthService.dart';
import 'package:android/services/StorageService.dart';
import 'package:android/services/UserService.dart';
import 'package:flutter/material.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:flutter_native_splash/flutter_native_splash.dart';
import 'package:get/get.dart';
import 'firebase_options.dart';
import 'package:firebase_messaging/firebase_messaging.dart';

import 'models/User.dart';

Future<void> main() async {
  WidgetsBinding widgetsBinding = WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );

  FlutterNativeSplash.preserve(widgetsBinding: widgetsBinding);
  FlutterNativeSplash.remove();
  String token = await getToken();
  print('Token Device: $token}');

  await StorageService.saveDeviceToken(token);

  String? accessToken = await StorageService.getAccessToken();
  if (accessToken != null && !accessToken.isEmpty){
    try {
      User user = await UserService.fetchUserDetail();
      final UserController userController = Get.put(UserController());
      userController.AddUser(user);
      await AuthService.sendToken();
    } catch(error){
      print('Access Token: $accessToken}');
      print(error);
    }

  }

  print('start');
  runApp(const MyApp());
}



Future getToken() async{
  FirebaseMessaging firebaseMessaging = FirebaseMessaging.instance;
  String? token = await firebaseMessaging.getToken();
  return token;
}



class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    LocalNotification.initialize();
    // For Forground state;
    FirebaseMessaging.onMessage.listen((event) {
      LocalNotification.showNotification(event);
    });
    return GetMaterialApp(
      title: 'Bind Up',
      debugShowCheckedModeBanner: false,
      debugShowMaterialGrid: false,
      theme: ThemeData(
        primarySwatch: mCustomMainColor,
      ),
      home: const BottomBar(),
      // home: const LoginScreen()
      // home: const TestScreen(),
      // home: const SignUpScreen(),
    );
  }
}

