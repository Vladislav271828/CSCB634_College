-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: college
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Table structure for table `_user`
--

DROP TABLE IF EXISTS `_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `_user` (
  `id` int NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('USER','STUDENT','PROFESSOR','ADMIN','RECTOR') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_k11y3pdtsrjgy8w9b6q4bjwrx` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `_user_seq`
--

DROP TABLE IF EXISTS `_user_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `_user_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `absence`
--

DROP TABLE IF EXISTS `absence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `absence` (
  `date` date NOT NULL,
  `enrollment_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk6fagwjxoy5ns0lvnv4x3s44u` (`enrollment_id`),
  CONSTRAINT `FKk6fagwjxoy5ns0lvnv4x3s44u` FOREIGN KEY (`enrollment_id`) REFERENCES `enrollments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `college`
--

DROP TABLE IF EXISTS `college`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `college` (
  `rector_id` int DEFAULT NULL,
  `college_id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`college_id`),
  UNIQUE KEY `UK_nyc2rxbj71rdhcw055436agb5` (`name`),
  KEY `FK5optlcsnq81nogvcpgt6xrqqx` (`rector_id`),
  CONSTRAINT `FK5optlcsnq81nogvcpgt6xrqqx` FOREIGN KEY (`rector_id`) REFERENCES `_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `credits` int DEFAULT NULL,
  `course_id` bigint NOT NULL AUTO_INCREMENT,
  `major_id` bigint DEFAULT NULL,
  `course_name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `signature` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`course_id`),
  KEY `FK7vf5q4vds30s891y7hdk54ice` (`major_id`),
  CONSTRAINT `FK7vf5q4vds30s891y7hdk54ice` FOREIGN KEY (`major_id`) REFERENCES `majors` (`major_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `departments`
--

DROP TABLE IF EXISTS `departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departments` (
  `department_head_user_id` int DEFAULT NULL,
  `department_id` bigint NOT NULL AUTO_INCREMENT,
  `faculty_faculty_id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`department_id`),
  KEY `FK4b4nwhrl1wlc09a42o4g85cw` (`department_head_user_id`),
  KEY `FKc9hlk81iy64ep1te52fd2osc6` (`faculty_faculty_id`),
  CONSTRAINT `FK4b4nwhrl1wlc09a42o4g85cw` FOREIGN KEY (`department_head_user_id`) REFERENCES `professors` (`user_id`),
  CONSTRAINT `FKc9hlk81iy64ep1te52fd2osc6` FOREIGN KEY (`faculty_faculty_id`) REFERENCES `faculty` (`faculty_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `enrollment_grade`
--

DROP TABLE IF EXISTS `enrollment_grade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enrollment_grade` (
  `score` int DEFAULT NULL,
  `enrollment_id` bigint NOT NULL,
  `grade_id` bigint NOT NULL,
  PRIMARY KEY (`enrollment_id`,`grade_id`),
  KEY `FKhy8u16qop6uax5k3w3qipk6yr` (`grade_id`),
  CONSTRAINT `FKaqt925909jsb5fogctjg76tce` FOREIGN KEY (`enrollment_id`) REFERENCES `enrollments` (`id`),
  CONSTRAINT `FKhy8u16qop6uax5k3w3qipk6yr` FOREIGN KEY (`grade_id`) REFERENCES `grade` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `enrollments`
--

DROP TABLE IF EXISTS `enrollments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enrollments` (
  `autumn` bit(1) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `final_grade` int DEFAULT NULL,
  `professors_user_id` int DEFAULT NULL,
  `students_user_id` int DEFAULT NULL,
  `course_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `room` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKho8mcicp4196ebpltdn9wl6co` (`course_id`),
  KEY `FKkb86seijrq5tdbhe3cwi2hci3` (`professors_user_id`),
  KEY `FKepwh6grwp9deeelgnnnt7brp5` (`students_user_id`),
  CONSTRAINT `FKepwh6grwp9deeelgnnnt7brp5` FOREIGN KEY (`students_user_id`) REFERENCES `students` (`user_id`),
  CONSTRAINT `FKho8mcicp4196ebpltdn9wl6co` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`),
  CONSTRAINT `FKkb86seijrq5tdbhe3cwi2hci3` FOREIGN KEY (`professors_user_id`) REFERENCES `professors` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `faculty`
--

DROP TABLE IF EXISTS `faculty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `faculty` (
  `college_college_id` bigint DEFAULT NULL,
  `faculty_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`faculty_id`),
  KEY `FKdiacf9mjm3xdpoqg17po5cqi6` (`college_college_id`),
  CONSTRAINT `FKdiacf9mjm3xdpoqg17po5cqi6` FOREIGN KEY (`college_college_id`) REFERENCES `college` (`college_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `grade`
--

DROP TABLE IF EXISTS `grade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grade` (
  `year` int NOT NULL,
  `course_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs1eygu2rhlb4606ky1bvqo9j8` (`course_id`),
  CONSTRAINT `FKs1eygu2rhlb4606ky1bvqo9j8` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `majors`
--

DROP TABLE IF EXISTS `majors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `majors` (
  `department_id` bigint DEFAULT NULL,
  `major_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`major_id`),
  KEY `FKrywn9bjkn1xb5oxfen31few10` (`department_id`),
  CONSTRAINT `FKrywn9bjkn1xb5oxfen31few10` FOREIGN KEY (`department_id`) REFERENCES `departments` (`department_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `professor_qualification`
--

DROP TABLE IF EXISTS `professor_qualification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `professor_qualification` (
  `professors_user_id` int NOT NULL,
  `courses_course_id` bigint NOT NULL,
  PRIMARY KEY (`professors_user_id`,`courses_course_id`),
  KEY `FKbuihdv1sp1v9lnwjc5k04snf4` (`courses_course_id`),
  CONSTRAINT `FKbuihdv1sp1v9lnwjc5k04snf4` FOREIGN KEY (`courses_course_id`) REFERENCES `courses` (`course_id`),
  CONSTRAINT `FKyvscf8fchrs2s9yh1atdbub0` FOREIGN KEY (`professors_user_id`) REFERENCES `professors` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `professors`
--

DROP TABLE IF EXISTS `professors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `professors` (
  `user_id` int NOT NULL,
  `department_id` bigint DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `FKpguqb9fhf7yoygkexhjw1viwj` (`department_id`),
  CONSTRAINT `FK3pwaa96yrqrg7i9pxmil7he5c` FOREIGN KEY (`user_id`) REFERENCES `_user` (`id`),
  CONSTRAINT `FKpguqb9fhf7yoygkexhjw1viwj` FOREIGN KEY (`department_id`) REFERENCES `departments` (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `program`
--

DROP TABLE IF EXISTS `program`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `program` (
  `autumn` bit(1) DEFAULT NULL,
  `education_year` int DEFAULT NULL,
  `year` int DEFAULT NULL,
  `course_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `FKmlot89u0wboodnl9tc6dvy1xm` (`course_id`),
  CONSTRAINT `FKmlot89u0wboodnl9tc6dvy1xm` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `program_professors`
--

DROP TABLE IF EXISTS `program_professors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `program_professors` (
  `professors_user_id` int NOT NULL,
  `program_id` bigint NOT NULL,
  KEY `FKdet5yhx5lkbxo0nrj94me1n7s` (`professors_user_id`),
  KEY `FKja5uf308g989wjoo4kvv7ukxq` (`program_id`),
  CONSTRAINT `FKdet5yhx5lkbxo0nrj94me1n7s` FOREIGN KEY (`professors_user_id`) REFERENCES `professors` (`user_id`),
  CONSTRAINT `FKja5uf308g989wjoo4kvv7ukxq` FOREIGN KEY (`program_id`) REFERENCES `program` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `education_year` int DEFAULT NULL,
  `user_id` int NOT NULL,
  `major_id` bigint DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `FKi04cc0278a1f49g0995mnuo63` (`major_id`),
  CONSTRAINT `FKi04cc0278a1f49g0995mnuo63` FOREIGN KEY (`major_id`) REFERENCES `majors` (`major_id`),
  CONSTRAINT `FKktwd8s3pmysrb25jil461o739` FOREIGN KEY (`user_id`) REFERENCES `_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `token` (
  `expired` bit(1) NOT NULL,
  `id` int NOT NULL,
  `revoked` bit(1) NOT NULL,
  `user_id` int DEFAULT NULL,
  `expiration_time` datetime(6) DEFAULT NULL,
  `issued_at` datetime(6) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `token_type` enum('BEARER','ACCESS_TOKEN','REFRESH_TOKEN') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKiblu4cjwvyntq3ugo31klp1c6` (`user_id`),
  CONSTRAINT `FKiblu4cjwvyntq3ugo31klp1c6` FOREIGN KEY (`user_id`) REFERENCES `_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `token_seq`
--

DROP TABLE IF EXISTS `token_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `token_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-27  0:37:15
