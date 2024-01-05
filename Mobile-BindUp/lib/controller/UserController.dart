import 'package:android/constants.dart';
import 'package:get/get.dart';

import '../models/User.dart';

class UserController extends GetxController{
  RxString id = "".obs;
  RxString name = "".obs;
  RxInt gender = 0.obs;
  RxString headline = "".obs;
  RxString description = "".obs;
  RxString phone = "".obs;
  RxString email = "".obs;
  RxString avatar = "".obs;
  RxString address = "".obs;

  void AddUser(User user){
    this.id = RxString(user.id!);
    this.description = RxString(user.description!);
    this.name = RxString(user.name!);
    this.gender = RxInt(user.gender!);
    this.headline = RxString(user.headline!);

    this.phone = RxString(user.phone!) ;
    this.email = RxString(user.email!);
    this.avatar = RxString(user.avatar!);
    this.address = RxString(user.address!);
  }
  
  void RemoveUser(){
    this.id = RxString("");
  }


}