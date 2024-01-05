import 'package:android/screens/project_detail_screen.dart';
import 'package:android/widget/ProjectTypeWidget.dart';
import 'package:flutter/material.dart';

class ChangeLogCard extends StatelessWidget {
  final String title;
  final String description;
  final String time;
  const ChangeLogCard(
      { Key? key,
        required this.title,
        required this.description,
        required this.time
      }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: (){},
      child: Container(
        margin: EdgeInsets.all(5),
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
              width: MediaQuery.of(context).size.width * 0.9,
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
                  const SizedBox(
                    height: 15,
                  )
                ],
              ),
            ),
            Positioned(
                bottom: 5,
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
