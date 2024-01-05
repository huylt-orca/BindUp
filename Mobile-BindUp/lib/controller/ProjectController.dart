import 'package:android/models/Application.dart';
import 'package:android/models/Changelog.dart';
import 'package:android/models/Job.dart';
import 'package:android/models/Member.dart';
import 'package:android/models/Project.dart';
import 'package:android/models/ProjectImage.dart';
import 'package:android/models/Topic.dart';
import 'package:android/services/ProjectService.dart';
import 'package:get/get.dart';

import '../models/Mentor.dart';
import '../models/User.dart';


class ProjectController extends GetxController{
    RxString id = "".obs;
    RxString name = "".obs;
    RxString logo = "".obs;
    RxString summary = "".obs;
    RxString description = "".obs;
    RxInt voteQuantity = 0.obs;
    RxInt milestone = 0.obs;
    RxString status = "".obs;
    RxString source = "".obs;

    RxList<ProjectImage> images = List<ProjectImage>.empty(growable: true).obs;
    RxList<Mentor> mentors = List<Mentor>.empty(growable: true).obs;
    RxList<Member> members = List<Member>.empty(growable: true).obs;
    RxList<Job> jobs = List<Job>.empty(growable: true).obs;
    RxList<Application> applications = List<Application>.empty(growable: true).obs;
    RxList<Changelog> changelogs = List<Changelog>.empty(growable: true).obs;
    RxList<Topic> topics = List<Topic>.empty(growable: true).obs;

    var founder = User().obs;


    void AddProject(Project project){
      this.id = RxString(project.id!);
      this.description = RxString(project.description!) ;
      this.logo = RxString(project.logo!);
      this.milestone = RxInt(project.milestone!);
      this.name = RxString(project.name!);
      this.source = RxString(project.source!) ;
      this.status = RxString(project.status!) ;
      this.summary = RxString(project.summary!);
      this.voteQuantity = RxInt(project.voteQuantity!);
    }

    void AddListImage(List<ProjectImage> images){
      // this.images.clear();
      // this.images.addAll(images);
      this.images = RxList(images);
    }

    void RemoveAllInformationProject(){
      this.images.clear();
      this.mentors.clear();
      this.members.clear();
      this.jobs.clear();
      this.applications.clear();
      this.changelogs.clear();
      this.topics.clear();
    }

    //
    // static Future<void> AddProjectFromAPI(String projectId)async{
    //   final ProjectController projectController = Get.put(ProjectController());
    //   Project projectDetail = await ProjectService.fetchProjectDetail(projectId);
    //
    //   projectController.AddProject(projectDetail);
    //   List<ProjectImage> images = await ProjectService.fetchProjectImageList(projectId);
    //   projectController.AddListImage(images);
    // }

}