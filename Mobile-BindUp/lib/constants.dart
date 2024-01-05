import 'package:flutter/material.dart';

import 'models/responsetest.dart';

const server = "http://fhunt-env.eba-pr2amuxm.ap-southeast-1.elasticbeanstalk.com/api/v1/";
const imageDefault = imageDemo;

const kTextColor = Color(0xFF535353);
const kTextLightColor = Color(0xFFACACAC);

const cCustomMainColor= Color(0xFF00B1FF);
const cCustomMainColor2= Color(0xFF1939FF);

MaterialColor mCustomMainColor = MaterialColor(
  0xFF00B1FF,
  <int, Color>{
    50: Color(0xFFE1F5FE),
    100: Color(0xFFB3E5FC),
    200: Color(0xFF81D4FA),
    300: Color(0xFF4FC3F7),
    400: Color(0xFF29B6F6),
    500: cCustomMainColor,
    600: Color(0xFF039BE5),
    700: Color(0xFF0288D1),
    800: Color(0xFF0277BD),
    900: Color(0xFF01579B),
  },
);


const kDefaultPadding = 20.0;

// const imageDemo = "https://static.wikia.nocookie.net/leagueoflegends/images/0/04/Chibi_Ahri_Base_Tier_1.png/revision/latest?cb=20230208203819";
const imageDemo = "https://cdn.pixabay.com/photo/2012/04/26/19/43/profile-42914_1280.png";
const imageUnknowPerson = "assets/images/imageunknown.jpg";

const List<String> imageUrlsDemo = [
  imageDemo,imageDemo,imageDemo,imageDemo,imageDemo,imageDemo,imageDemo,imageDemo,
  imageDemo,imageDemo,imageDemo,imageDemo,imageDemo,imageDemo,imageDemo,imageDemo,
  imageDemo,imageDemo,imageDemo,imageDemo,imageDemo,imageDemo,imageDemo,imageDemo,
];




List<ResponseTest> listTest = [
   ResponseTest(
    createdDate: "20:00, 20/20/2020",
    description: "Heloo Description",
    id: "1",
    link: imageDemo,
    title: "Thanh Huy"
  ),
  ResponseTest(
      createdDate: "20:00, 20/20/2020",
      description: "Heloo Description",
      id: "1",
      link: imageDemo,
      title: "Thanh Huy"
  ),
  ResponseTest(
      createdDate: "20:00, 20/20/2020",
      description: "Heloo Description",
      id: "1",
      link: imageDemo,
      title: "Thanh Huy"
  ),
  ResponseTest(
      createdDate: "20:00, 20/20/2020",
      description: "Heloo Description",
      id: "1",
      link: imageDemo,
      title: "Thanh Huy"
  ),
  ResponseTest(
      createdDate: "20:00, 20/20/2020",
      description: "Heloo Description",
      id: "1",
      link: imageDemo,
      title: "Thanh Huy"
  ),
];