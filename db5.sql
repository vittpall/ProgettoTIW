CREATE DATABASE  IF NOT EXISTS `tiw_project` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `tiw_project`;
-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: tiw_project
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `album`
--

DROP TABLE IF EXISTS `album`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `album` (
  `Username` varchar(255) DEFAULT NULL,
  `User_id` int NOT NULL,
  `Title` varchar(255) NOT NULL,
  `Creation_Date` date NOT NULL,
  KEY `fk_Album_1_idx` (`User_id`,`Title`),
  CONSTRAINT `fk_Album_2` FOREIGN KEY (`User_id`) REFERENCES `user` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `album`
--

LOCK TABLES `album` WRITE;
/*!40000 ALTER TABLE `album` DISABLE KEYS */;
INSERT INTO `album` VALUES ('b',10,'dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd','2024-05-07'),('b',10,'aaaaa','2024-05-07'),('b',10,'serf ','2024-05-07'),('b',10,'sssssss','2024-05-08'),('b',10,'aldo','2024-05-08'),('b',10,'tttttttt','2024-05-08'),('b',10,'88888','2024-05-08'),('b',10,'aaaaaaaaaaaaaaaa','2024-05-08'),('b',10,'wwwwwwwwwwwwww','2024-05-08'),('b',10,'rrrrrrrrrrrr','2024-05-08'),('b',10,'qqqqqqqqqqqqqqq','2024-05-08'),('b',10,'francesco','2024-05-08'),('b',10,'francesca','2024-05-08'),('b',10,'rrrresswsdxqas','2024-05-08'),('b',10,'lokokoko','2024-05-08'),('b',10,'cicci','2024-05-08'),('b',10,'provasort','2024-05-08'),('b',10,'sasasasasasasasasa','2024-05-08'),('b',10,'aldopiero','2024-05-08'),('b',10,'gatto di pietro','2024-05-08');
/*!40000 ALTER TABLE `album` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `Image_Id` int NOT NULL,
  `Id` int NOT NULL,
  `Publication_date` datetime NOT NULL,
  `Text` varchar(255) NOT NULL,
  PRIMARY KEY (`Image_Id`,`Id`,`Publication_date`),
  KEY `fk_Comment_1_idx` (`Id`),
  CONSTRAINT `fk_Comment_1` FOREIGN KEY (`Id`) REFERENCES `user` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_Comment_2` FOREIGN KEY (`Image_Id`) REFERENCES `image` (`Image_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (178,10,'2024-05-08 05:02:33','franco gay'),(178,10,'2024-05-08 05:07:33','cicci polipetto'),(179,10,'2024-05-07 22:08:12','s'),(179,10,'2024-05-08 05:02:16','franco gay'),(184,10,'2024-05-07 22:18:02','hh'),(184,10,'2024-05-08 08:49:48','aramdno'),(185,10,'2024-05-08 08:58:48','ciao');
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contains_images`
--

DROP TABLE IF EXISTS `contains_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contains_images` (
  `Image_Id` int NOT NULL,
  `title` varchar(255) NOT NULL,
  `User_Id` int NOT NULL,
  `Order_Index` int DEFAULT '0',
  PRIMARY KEY (`Image_Id`,`title`,`User_Id`),
  KEY `fk_Contains_Images_1_idx` (`User_Id`,`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contains_images`
--

LOCK TABLES `contains_images` WRITE;
/*!40000 ALTER TABLE `contains_images` DISABLE KEYS */;
INSERT INTO `contains_images` VALUES (32,'2a',1,0),(32,'3a',1,0),(32,'6images',1,0),(32,'armando',1,0),(32,'prova2',1,0),(33,'3a',1,0),(33,'6images',1,0),(33,'armando',1,0),(33,'prova2',1,0),(34,'4a',1,0),(34,'6images',1,0),(34,'armando',1,0),(34,'prova2',1,0),(35,'5a',1,0),(35,'6images',1,0),(35,'armando',1,0),(35,'prova2',1,0),(36,'6a',1,0),(36,'6images',1,0),(36,'armando',1,0),(36,'prova2',1,0),(37,'6images',1,0),(37,'armando',1,0),(37,'prova2',1,0),(38,'armando',1,0),(38,'prova2',1,0),(39,'1a',10,0),(39,'5a',10,0),(39,'prova',10,0),(39,'title',10,0),(40,'2a',10,0),(40,'5a',10,0),(40,'title',10,0),(41,'3a',10,0),(41,'5a',10,0),(41,'title',10,0),(42,'4a',10,0),(42,'5a',10,0),(42,'title',10,0),(43,'5a',10,0),(43,'title',10,0),(44,'prova',10,0),(45,'titttttttttttttttttt',10,0),(46,'aaaaaaaaaaaaaa',10,0),(47,'fabio',10,0),(48,'gatto',10,0),(49,'loko',10,0),(50,'lako',10,0),(51,'sergioaldo',10,0),(52,'paletta',10,0),(53,'robee',10,0),(54,'francofranco',10,0),(55,'2a',10,0),(56,'3a',10,0),(57,'4a',10,0),(58,'5a',10,0),(59,'6a',10,0),(60,'7a',10,0),(61,'8a',10,0),(62,'xxxxxxxxxxxxxxxxxxx',10,0),(63,'9a',10,0),(64,'1b',10,0),(65,'2b',10,0),(66,'3b',10,0),(67,'4b',10,0),(68,'5b',10,0),(69,'6b',10,0),(70,'7b',10,0),(71,'8b',10,0),(72,'9b',10,0),(73,'1c',10,0),(74,'2c',10,0),(75,'3c',10,0),(76,'4c',10,0),(77,'5c',10,0),(78,'6c',10,0),(79,'7c',10,0),(80,'8c',10,0),(81,'9c',10,0),(82,'1d',10,0),(83,'2d',10,0),(84,'3d',10,0),(85,'4d',10,0),(86,'5d',10,0),(87,'9d',10,0),(88,'1e',10,0),(89,'2e',10,0),(90,'4e',10,0),(91,'10s',10,0),(92,'11a',10,0),(93,'dddd',10,0),(94,'tryyyyyy',10,0),(95,'234',10,0),(96,'11111111111',10,0),(97,'222222222222',10,0),(98,'3333333333',10,0),(99,'sergio',10,0),(100,'444444444444',10,0),(101,'6666666666666666',10,0),(102,'77777777777777',10,0),(103,'88888888888888',10,0),(104,'8999999999999999',10,0),(105,'991111111111111',10,0),(106,'565656565656',10,0),(107,'aaaaaaaaaaaa',10,0),(108,'carlo',10,0),(109,'dddddddddddddddddddd',10,0),(110,'gggggggggggggg',10,0),(111,'ppppppppppppppppppppppppp',10,0),(112,'aldo',10,0),(113,'lolorolorlrolror',10,0),(114,'golroe',10,0),(115,'sarettfre',10,0),(116,'orcapaletta',10,0),(117,'localhost',10,0),(118,'localeeeeeeee',10,0),(119,'sergioooooooooooooooooooooo',10,0),(120,'9939393939393939939393',10,0),(121,'fffffffffffffffffffffffffffffffffffffff',10,0),(122,'pietroepaolo',10,0),(123,'multipartconfig',10,0),(124,'configmultipart',10,0),(125,'albawert',10,0),(126,'tmmmmmmmmmmmmmmmm',10,0),(127,'francofrancofranco',10,0),(128,'sergiosergiosergio',10,0),(129,'aldoaldoaldo',10,0),(130,'ciaoooooooooooo',10,0),(131,'reowedjdsww',10,0),(132,'frcccccccccccc',10,0),(133,'aldofrncosergio',10,0),(134,'hhhhhhhhhhhhhhhhhhhhhhhh',10,0),(135,'sergio',10,0),(136,'tttttttttttttttttttttt',10,0),(137,'totototototot',10,0),(138,'jkjkjkjkjkjk',10,0),(139,'rhrhrhhrhrh',10,0),(140,'reeeeeeeeeeeeeeee',10,0),(141,'gtgtgtgt',10,0),(142,'hshshshhsh',10,0),(143,'lololkujujjjjjjj',10,0),(144,'1a',1,0),(145,'2a',10,0),(145,'armando',10,0),(146,'armando',10,0),(146,'franco',10,0),(147,'Franco',10,0),(148,'ciao',10,0),(149,'aldofranco',10,0),(150,'sergio',10,0),(151,'azzxsza',10,0),(152,'granchio',10,0),(153,'tututut',10,0),(154,'tretretre',10,0),(155,'ewe',10,0),(156,'ssssssssss',10,0),(157,'erdaswed',10,0),(158,'swdwert',10,0),(159,'wwwwwwwwwwwwww',10,0),(160,'sssssssssssssssssssssssssssssssssssssssssssssssss',10,0),(161,'rrrrrrrrrrrrr',10,0),(162,'serfffffff',10,0),(163,'1a',9,0),(164,'sssssss',10,0),(165,'greto',10,0),(165,'gretotumb',10,0),(165,'ttttttttttttttttttttttkkkkkkk',10,0),(166,'gretotumb',10,0),(166,'ttttttttttttttttttttttkkkkkkk',10,0),(167,'2a',10,0),(167,'3a',10,0),(168,'3a',10,0),(168,'eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee',10,0),(169,'4a',10,0),(169,'rrrrrrrrrrrr',10,0),(170,'10a',10,0),(171,'12a',10,0),(172,'14a',10,0),(172,'pradella',10,0),(173,'16a',10,0),(173,'pradella',10,0),(174,'18a',10,0),(175,'19a',10,0),(176,'23a',10,0),(176,'pradella',10,0),(176,'wwwwwwwwwwwwww',10,2),(177,'123s',10,0),(177,'aldo',10,0),(177,'fra',10,0),(177,'pradella',10,0),(178,'1234a',10,0),(178,'88888',10,0),(178,'aaaaa',10,0),(178,'aaaaaaaaaaaaaaaa',10,0),(178,'aldopiero',10,1),(178,'cicci',10,0),(178,'eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee',10,0),(178,'fra',10,0),(178,'francesca',10,0),(178,'francesco',10,0),(178,'lokokoko',10,0),(178,'provasort',10,0),(178,'qqqqqqqqqqqqqqq',10,0),(178,'rrrresswsdxqas',10,0),(178,'sasasasasasasasasa',10,1),(178,'serf ',10,0),(178,'sssssss',10,0),(178,'tnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn',10,0),(178,'tttttttt',10,0),(179,'12345x',10,0),(179,'88888',10,0),(179,'aaaaaaaaaaaaaaaa',10,0),(179,'aldo',10,0),(179,'aldopiero',10,0),(179,'cicci',10,0),(179,'eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee',10,0),(179,'fra',10,0),(179,'francesca',10,0),(179,'francesco',10,2),(179,'hhhhhhhhhhhhhh',10,0),(179,'jjjjjjjjjjjjjjjjjjjjjjj',10,0),(179,'lokokoko',10,0),(179,'provasort',10,0),(179,'qqqqqqqqqqqqqqq',10,2),(179,'rrrresswsdxqas',10,0),(179,'rrrrrrrrrrrr',10,0),(179,'sasasasasasasasasa',10,0),(179,'serf ',10,0),(179,'sssssss',10,0),(179,'tttttttt',10,2),(179,'wwwwwwwwwwwwww',10,3),(180,'dddddddddddddddddddddddddddddddddddddddd',10,0),(180,'eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee',10,0),(180,'eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee',10,0),(180,'fra',10,0),(180,'hhhhhhhhhhhhhh',10,0),(180,'qwsaer',10,0),(180,'tnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn',10,0),(180,'wwwwwwwwww',10,0),(181,'aaa',10,0),(181,'dddddddddddddddddddddddddddddddddddddddd',10,0),(181,'eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee',10,0),(181,'eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee',10,0),(181,'fra',10,0),(181,'pradella',10,0),(181,'tnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn',10,0),(181,'ttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt',10,0),(182,'dddddddddddddddddddddddddddddddddddddd',10,0),(182,'dddddddddddddddddddddddddddddddddddddddd',10,0),(182,'eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee',10,0),(182,'fra',10,0),(182,'pradella',10,0),(182,'tnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn',10,0),(183,'peitro',1,0),(184,'88888',10,0),(184,'aaaaaaaaaaaaaaaa',10,0),(184,'aldo',10,0),(184,'aldopiero',10,2),(184,'cicci',10,0),(184,'dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd',10,0),(184,'francesca',10,0),(184,'francesco',10,1),(184,'lokokoko',10,0),(184,'provasort',10,0),(184,'qqqqqqqqqqqqqqq',10,1),(184,'rrrresswsdxqas',10,0),(184,'rrrrrrrrrrrr',10,0),(184,'sasasasasasasasasa',10,2),(184,'serf ',10,0),(184,'sssssss',10,0),(184,'tttttttt',10,1),(184,'wwwwwwwwwwwwww',10,1),(185,'gatto di pietro',10,0);
/*!40000 ALTER TABLE `contains_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image` (
  `Image_id` int NOT NULL AUTO_INCREMENT,
  `Title` varchar(255) DEFAULT NULL,
  `Creation_Date` date DEFAULT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `System_Path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Image_id`)
) ENGINE=InnoDB AUTO_INCREMENT=186 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image`
--

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
INSERT INTO `image` VALUES (144,'Screenshot 2023-05-14 180817.png','2024-05-04',NULL,'/images/1714815746355_Screenshot 2023-05-14 180817.png'),(145,'Screenshot 2023-05-14 180817.png','2024-05-04',NULL,'/images/1714818081893_Screenshot 2023-05-14 180817.png'),(146,'Screenshot 2023-05-14 180903.png','2024-05-04',NULL,'/images/1714818166553_Screenshot 2023-05-14 180903.png'),(147,'Screenshot 2023-05-14 180817.png','2024-05-04',NULL,'/images/1714818540826_Screenshot 2023-05-14 180817.png'),(148,'Screenshot 2023-05-14 180903.png','2024-05-04',NULL,'/images/1714824559337_Screenshot 2023-05-14 180903.png'),(149,'Screenshot 2023-05-14 180817.png','2024-05-04',NULL,'/images/1714825180609_Screenshot 2023-05-14 180817.png'),(150,'Screenshot 2023-05-14 180903.png','2024-05-04',NULL,'/images/1714825719481_Screenshot 2023-05-14 180903.png'),(151,'Screenshot 2023-05-14 180903.png','2024-05-04',NULL,'/images/1714825893032_Screenshot 2023-05-14 180903.png'),(152,'Screenshot 2023-05-14 180903.png','2024-05-04',NULL,'/images/1714827270315_Screenshot 2023-05-14 180903.png'),(153,'Screenshot 2023-05-14 180903.png','2024-05-04',NULL,'/images/1714828264668_Screenshot 2023-05-14 180903.png'),(154,'Screenshot 2023-05-14 180903.png','2024-05-04',NULL,'/Images/1714829211588_Screenshot 2023-05-14 180903.png'),(155,'Screenshot 2023-05-14 180817.png','2024-05-04',NULL,'/Images/1714829624213_Screenshot 2023-05-14 180817.png'),(156,'Screenshot 2023-05-14 180903.png','2024-05-04',NULL,'/Images/1714830148635_Screenshot 2023-05-14 180903.png'),(157,'Screenshot 2023-05-14 180903.png','2024-05-04',NULL,'/Images/1714830552902_Screenshot 2023-05-14 180903.png'),(158,'Screenshot 2023-05-14 180903.png','2024-05-04',NULL,'/Images/1714830697296_Screenshot 2023-05-14 180903.png'),(159,'Screenshot 2023-05-14 180903.png','2024-05-04',NULL,'/Images/1714830986215_Screenshot 2023-05-14 180903.png'),(160,'Screenshot 2023-05-14 180903.png','2024-05-04',NULL,'/Images/1714831208502_Screenshot 2023-05-14 180903.png'),(161,'Screenshot 2023-05-14 180903.png','2024-05-04',NULL,'/Images/1714831809006_Screenshot 2023-05-14 180903.png'),(162,'Screenshot 2023-05-14 180903.png','2024-05-04',NULL,'/Images/1714834323941_Screenshot 2023-05-14 180903.png'),(163,'Screenshot 2023-05-14 180903.png','2024-05-05',NULL,'/images/1714924020429_Screenshot 2023-05-14 180903.png'),(164,'Screenshot 2023-05-14 180817.png','2024-05-05',NULL,'/images/1714924246469_Screenshot 2023-05-14 180817.png'),(165,'Screenshot 2023-10-07 211131.png','2024-05-05',NULL,'/images/1714924554052_Screenshot 2023-10-07 211131.png'),(166,'Screenshot 2023-10-07 211840.png','2024-05-05',NULL,'/images/1714924575410_Screenshot 2023-10-07 211840.png'),(167,'Screenshot 2024-04-22 194112.png','2024-05-05',NULL,'/images/1714925440524_Screenshot 2024-04-22 194112.png'),(168,'Screenshot_20221108_081548.png','2024-05-05',NULL,'/images/1714925464097_Screenshot_20221108_081548.png'),(169,'Screenshot 2023-05-14 180903.png','2024-05-05',NULL,'/images/1714926349950_Screenshot 2023-05-14 180903.png'),(170,'Screenshot 2023-05-14 180903.png','2024-05-05',NULL,'/images/1714926490684_Screenshot 2023-05-14 180903.png'),(171,'Screenshot 2023-05-14 180903.png','2024-05-05',NULL,'/images/1714926782292_Screenshot 2023-05-14 180903.png'),(172,'Screenshot 2023-05-14 180903.png','2024-05-05',NULL,'/images/1714926951345_Screenshot 2023-05-14 180903.png'),(173,'Screenshot 2023-05-14 180903.png','2024-05-05',NULL,'/images/1714927382447_Screenshot 2023-05-14 180903.png'),(174,'Screenshot 2023-05-14 180817.png','2024-05-05',NULL,'/images/1714927781379_Screenshot 2023-05-14 180817.png'),(175,'Screenshot 2023-05-14 180903.png','2024-05-05',NULL,'/images/1714927869891_Screenshot 2023-05-14 180903.png'),(176,'Screenshot 2023-05-14 180903.png','2024-05-05',NULL,'/images/1714928217100_Screenshot 2023-05-14 180903.png'),(177,'Screenshot 2023-05-14 180903.png','2024-05-06',NULL,'/images/1714948838571_Screenshot 2023-05-14 180903.png'),(178,'Screenshot 2023-05-14 180903.png','2024-05-06',NULL,'/images/1714949657270_Screenshot 2023-05-14 180903.png'),(179,'sergiooooooo.jpeg','2024-05-06',NULL,'/images/1714950467114_sergiooooooo.jpeg'),(183,'Zanichelli tavola periodica - (1) Per gli elementi radioattivi che non hanno isotopi stabili, il - StuDocu.png','2024-05-06',NULL,'/images/1714995868701_Zanichelli tavola periodica - (1) Per gli elementi radioattivi che non hanno isotopi stabili, il - StuDocu.png'),(184,'Screenshot_20230118_191633.png','2024-05-07',NULL,'/images/1715103904771_Screenshot_20230118_191633.png'),(185,'orario poli.png','2024-05-08',NULL,'/images/1715158686432_orario poli.png');
/*!40000 ALTER TABLE `image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `publish_image`
--

DROP TABLE IF EXISTS `publish_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `publish_image` (
  `image_Id` int NOT NULL,
  `User_Id` int NOT NULL,
  PRIMARY KEY (`image_Id`,`User_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publish_image`
--

LOCK TABLES `publish_image` WRITE;
/*!40000 ALTER TABLE `publish_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `publish_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(255) DEFAULT NULL,
  `Email` varchar(255) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  `Reg_Date` date DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Pietro','a@gmail.com','sergio','2023-01-02'),(9,'a','a@gmail.com','a','2024-04-19'),(10,'b','b@gmail.com','a','2024-04-19'),(11,'UobinoPino','UobinoPino@gmail.com','UobinoPino','2024-04-26');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-08 12:06:29
