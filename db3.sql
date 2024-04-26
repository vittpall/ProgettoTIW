CREATE DATABASE  IF NOT EXISTS `TIW_Project` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `TIW_Project`;
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
  `Publication_date` date NOT NULL,
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
INSERT INTO `comment` VALUES (32,1,'2024-04-26','comment');
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
  PRIMARY KEY (`Image_Id`,`title`,`User_Id`),
  KEY `fk_Contains_Images_1_idx` (`User_Id`,`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contains_images`
--

LOCK TABLES `contains_images` WRITE;
/*!40000 ALTER TABLE `contains_images` DISABLE KEYS */;
INSERT INTO `contains_images` VALUES (32,'2a',1),(32,'3a',1),(33,'3a',1),(34,'4a',1),(35,'5a',1),(36,'6a',1),(32,'6images',1),(33,'6images',1),(34,'6images',1),(35,'6images',1),(36,'6images',1),(37,'6images',1),(32,'prova2',1),(33,'prova2',1),(34,'prova2',1),(35,'prova2',1),(36,'prova2',1),(37,'prova2',1),(38,'prova2',1);
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
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image`
--

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
INSERT INTO `image` VALUES (32,'Screenshot 2023-05-14 180903.png','2024-04-26',NULL,'/images/1714118594646_Screenshot 2023-05-14 180903.png'),(33,'Screenshot 2023-05-14 180817.png','2024-04-26',NULL,'/images/1714118611078_Screenshot 2023-05-14 180817.png'),(34,'Screenshot 2023-05-24 124321.png','2024-04-26',NULL,'/images/1714118662373_Screenshot 2023-05-24 124321.png'),(35,'Screenshot 2023-06-27 112812.png','2024-04-26',NULL,'/images/1714118671918_Screenshot 2023-06-27 112812.png'),(36,'Screenshot 2023-06-27 113011.png','2024-04-26',NULL,'/images/1714118681794_Screenshot 2023-06-27 113011.png'),(37,'Screenshot 2023-06-27 113417.png','2024-04-26',NULL,'/images/1714118703433_Screenshot 2023-06-27 113417.png'),(38,'Screenshot 2023-08-22 181930.png','2024-04-26',NULL,'/images/1714119018230_Screenshot 2023-08-22 181930.png');
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Pietro','a@gmail.com','sergio','2023-01-02'),(9,'a','a@gmail.com','a','2024-04-19'),(10,'b','b@gmail.com','a','2024-04-19');
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

-- Dump completed on 2024-04-26 10:12:15
