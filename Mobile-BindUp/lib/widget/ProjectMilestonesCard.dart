import 'package:flutter/material.dart';

class ProjectMilestonesCard extends StatelessWidget {
  final String title;
  final String description;
  const ProjectMilestonesCard({
    Key? key,
    required this.title,
    required this.description
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return  Card(
      child: Padding(
        padding: EdgeInsets.all(10),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('$title',
              style: TextStyle(
                  fontSize: 16,
                  color: Colors.black
              ),
            ),
            SizedBox(
              height: 5,
            ),
            Text(
              description,
              style: TextStyle(
                  fontSize: 14,
                  color: Colors.grey
              ),)
          ],
        ),
      ),
    );
    //   Container(
    //   margin: EdgeInsets.all(10),
    //   // height: 100,
    //   decoration: BoxDecoration(
    //       color: Colors.white,
    //       borderRadius: BorderRadius.circular(5),
    //       border: Border.all(
    //           color: Colors.black,
    //           width: 1,
    //           style: BorderStyle.solid
    //       )
    //   ),
    //   child: Container(
    //         margin: EdgeInsets.all(10),
    //         decoration: BoxDecoration(
    //             color: Colors.white
    //         ),
    //         child: Column(
    //           crossAxisAlignment: CrossAxisAlignment.start,
    //           children: [
    //             Text(
    //               this.title,
    //               style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
    //               maxLines: 1,
    //               overflow: TextOverflow.ellipsis ,
    //             ),
    //             Text(
    //               this.description,
    //               style: TextStyle(fontSize: 12),
    //               maxLines: 3,
    //               overflow: TextOverflow.ellipsis ,
    //             ),
    //           ],
    //         ),
    //       ),
    // );
  }
}
