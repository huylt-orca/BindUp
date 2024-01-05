import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ProjectTypeWidget extends StatelessWidget {
  final String text;
  final Color boxColor ;
  final Color textColor ;
  const ProjectTypeWidget({Key? key, required this.text, this.boxColor = Colors.black , this.textColor =Colors.white }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      child: Row(children: [
        Container(
          margin: EdgeInsets.symmetric(horizontal: 5),
          padding: EdgeInsets.symmetric(vertical: 5,horizontal: 10),
          decoration: BoxDecoration(
            color: this.boxColor,
            borderRadius: BorderRadius.circular(20),
          ),
          child: Text(this.text,style: TextStyle(
              color: this.textColor,
              fontSize: 10,
          )),
        )
      ],),
    );
  }
}
