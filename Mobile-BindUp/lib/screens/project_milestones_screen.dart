import 'package:android/controller/ProjectController.dart';
import 'package:android/services/ProjectService.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

import '../controller/UserController.dart';
import '../models/Project.dart';

class ProjectMilestonesScreen extends StatefulWidget {
  const ProjectMilestonesScreen({Key? key}) : super(key: key);

  @override
  State<ProjectMilestonesScreen> createState() =>
      _ProjectMilestonesScreenState();
}

class _ProjectMilestonesScreenState extends State<ProjectMilestonesScreen> {
  final ProjectController projectController = Get.put(ProjectController());
  final UserController userController = Get.put(UserController());
  int _currentStep = 0;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    _currentStep = projectController.milestone.value;
  }

  @override
  Widget build(BuildContext context) {

    return SingleChildScrollView(
      scrollDirection: Axis.vertical,
      child: Container(
        child:  Stepper(
            currentStep: _currentStep,
          onStepContinue: () {
            if (_currentStep <3){
              setState(() {
                _currentStep++;
              });

              ProjectService.putProject(new Project(
                name: projectController.name.value,
                id: projectController.id.value,
                description: projectController.description.value,
                source: projectController.source.value,
                summary: projectController.source.value,
                milestone: projectController.milestone.value + 1,
              ));
              projectController.milestone.value++;
            }
          },
          onStepCancel: () {
            if (_currentStep >0){
              setState(() {
                _currentStep--;
              });

              ProjectService.putProject(new Project(
                name: projectController.name.value,
                id: projectController.id.value,
                description: projectController.description.value,
                source: projectController.source.value,
                summary: projectController.source.value,
                milestone: projectController.milestone.value - 1,
              ));
              projectController.milestone.value--;
            }
          },
            onStepTapped: (int value ){
              setState(() {
                _currentStep = value;
              });
            },
          controlsBuilder: (BuildContext ctx, ControlsDetails dtl){
            return Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: <Widget>[
                Visibility(
                  visible: (userController.id.value == projectController.founder.value.id && _currentStep != 0),
                  child: TextButton(
                    onPressed: dtl.onStepCancel,
                    child: Text('BACK'),
                  ),
                ),
                Visibility(
                  visible: (userController.id.value == projectController.founder.value.id && _currentStep != 3),
                  child: TextButton(
                    onPressed: dtl.onStepContinue,
                    child: Text('NEXT'),
                  ),
                ),
              ],
            );
          },
            steps:  [
              Step(
                title: Text('Idea'),
                content: Text('Brainstorm ideas, find ways to solve problems'),
                isActive: projectController.milestone.value >=0,
                state: projectController.milestone.value >=0 ? StepState.complete :StepState.indexed
              ),
              Step(
                title: Text('Upcoming'),
                content: Text('Recruit more members, mentors, create demo products'),
                  isActive: projectController.milestone.value >=1,
                  state: projectController.milestone.value >=1 ? StepState.complete :StepState.indexed
              ),
              Step(
                title: Text('Launching'),
                content: Text('Deploy to platforms, support customer care'),
                  isActive: projectController.milestone.value >=2,
                  state: projectController.milestone.value >=2 ? StepState.complete :StepState.indexed
              ),
              Step(
                title: Text('Finished'),
                content: Text('Completed projects'),
                  isActive: projectController.milestone.value >=3,
                  state: projectController.milestone.value >=3 ? StepState.complete :StepState.indexed
              )
            ]
          ),
      ),
    );
  }
}

