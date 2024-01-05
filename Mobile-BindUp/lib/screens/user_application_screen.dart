import 'package:android/constants.dart';
import 'package:android/models/Application.dart';
import 'package:android/services/ApplicationService.dart';
import 'package:flutter/material.dart';

import '../widget/ApplicationCard.dart';

class UserApplicationScreen extends StatefulWidget {
  const UserApplicationScreen({Key? key}) : super(key: key);

  @override
  State<UserApplicationScreen> createState() => _UserApplicationScreenState();
}

class _UserApplicationScreenState extends State<UserApplicationScreen> {
  List<Application> applications = List<Application>.empty(growable: true);

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    ApplicationService.fetchApplicationListByUser().then((data){
      setState(() {
        applications = data;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: BackButton(),
        title: Text("Application"),
      ),
      body: applications.length== 0 ?

        Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(Icons.add_card_rounded,size: 100,),
              SizedBox(height: 10,),
              Text("No Applications"),
            ],
          ),
        )

      :ListView.builder(
          itemCount: applications.length,
          itemBuilder: (context,index) {
            return ApplicationCard(
              id: applications[index].id!,
              name: applications[index].project!.name!,
              job: applications[index].job!.name!,
              status: applications[index].status!,
              image: imageDemo
            );
          }
      ),
    );
  }
}
