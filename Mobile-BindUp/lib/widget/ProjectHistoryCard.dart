import 'package:android/screens/project_detail_screen.dart';
import 'package:android/widget/ProjectTypeWidget.dart';
import 'package:flutter/material.dart';

class ProjectHistoryCard extends StatelessWidget {
  final String title;
  final String description;
  final String image;
  final String time;
  const ProjectHistoryCard(
      { Key? key,
        required this.title,
        required this.description,
        required this.image,
        required this.time
      }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: (){},
      child: Container(
        margin: EdgeInsets.all(10),
        // height: 100,
        decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(5),
            border: Border.all(
                color: Colors.black,
                width: 1,
                style: BorderStyle.solid
            )
        ),
        child: Stack(
          children: [

            Container(
              child: Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Container(
                    margin: EdgeInsets.all(5),
                    decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(10)
                    ),
                    child: ClipRRect(
                      borderRadius: BorderRadius.circular(10),
                      child: Image.network(
                        this.image,
                        width: 80,
                        height: 80,
                        fit: BoxFit.cover,
                      ),
                    ),
                  ),
                  Container(
                    width: 200,
                    margin: EdgeInsets.all(10),
                    decoration: BoxDecoration(
                        color: Colors.white
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          this.title,
                          style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis ,
                        ),
                        Text(
                          this.description,
                          style: TextStyle(fontSize: 12),
                          maxLines: 3,
                          overflow: TextOverflow.ellipsis ,
                        ),
                      ],
                    ),
                  )
                ],
              ),
            ),
            Positioned(
                top: 10,
                right: 10,
                child: Text('$time',style: TextStyle(
                    fontSize: 12
                ),)
            ),
          ],
        ),
      ),
    );
  }
}
