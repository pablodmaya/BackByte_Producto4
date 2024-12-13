CREATE DATABASE  IF NOT EXISTS `backbyte` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `backbyte`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: backbyte
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `alquiler`
--

DROP TABLE IF EXISTS `alquiler`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alquiler` (
  `id_Alquiler` int NOT NULL AUTO_INCREMENT,
  `id_Cliente` int NOT NULL,
  `id_Vehiculo` int NOT NULL,
  `fecha_inicio` datetime(6) DEFAULT NULL,
  `fecha_fin` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id_Alquiler`),
  KEY `alquiler_cliente_idx` (`id_Cliente`),
  KEY `alquiler_vehiculo_idx` (`id_Vehiculo`),
  CONSTRAINT `alquiler_cliente` FOREIGN KEY (`id_Cliente`) REFERENCES `cliente` (`id_Cliente`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `alquiler_vehiculo` FOREIGN KEY (`id_Vehiculo`) REFERENCES `vehiculo` (`id_Vehiculo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alquiler`
--

LOCK TABLES `alquiler` WRITE;
/*!40000 ALTER TABLE `alquiler` DISABLE KEYS */;
/*!40000 ALTER TABLE `alquiler` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`BackByte`@`%`*/ /*!50003 TRIGGER `actualizar_localizacion_vehiculo` AFTER UPDATE ON `alquiler` FOR EACH ROW BEGIN
  IF OLD.fecha_fin < NOW() AND NEW.fecha_fin IS NOT NULL THEN
    UPDATE `vehiculo`
    SET `localizacion` = (SELECT `localizacion` FROM `alquiler` WHERE `id_Alquiler` = NEW.id_Alquiler)
    WHERE `id_Vehiculo` = NEW.id_Vehiculo;
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `id_Cliente` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `apellido` varchar(255) DEFAULT NULL,
  `dni` varchar(255) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `id_Usuario` int DEFAULT NULL,
  PRIMARY KEY (`id_Cliente`),
  KEY `usuario_cliente_idx` (`id_Usuario`),
  CONSTRAINT `usuario_cliente` FOREIGN KEY (`id_Usuario`) REFERENCES `usuario` (`id_Usuario`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES (5,'Marc','Belmonte','47966233G','Pompeu Fabra 14','656706663',NULL),(6,'Alejandro','Alonso','11122233A','Virgen de Guadalupe 11','630656860',NULL),(7,'Pablo','de Maya','99988877B','Rosario 12','666777888',NULL),(8,'Felix','Pasadas','44455566D','Dalí 13','666333222',NULL),(9,'user','user','111222333A','user','111222333',2);
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coche`
--

DROP TABLE IF EXISTS `coche`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coche` (
  `id_Vehiculo` int NOT NULL,
  `plazas` int DEFAULT NULL,
  `color` varchar(255) DEFAULT NULL,
  `puertas` int DEFAULT NULL,
  KEY `coche_vehiculo_idx` (`id_Vehiculo`),
  CONSTRAINT `coche_vehiculo` FOREIGN KEY (`id_Vehiculo`) REFERENCES `vehiculo` (`id_Vehiculo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coche`
--

LOCK TABLES `coche` WRITE;
/*!40000 ALTER TABLE `coche` DISABLE KEYS */;
INSERT INTO `coche` VALUES (1,5,'Rojo',4),(2,5,'Azul',4),(5,5,'Gris',4),(6,5,'Blanco',4),(7,5,'Negro',4),(8,5,'Verde',4),(11,5,'Plateado',4),(12,5,'Azul',4),(13,5,'Blanco',4),(14,5,'Rojo',4),(15,5,'Negro',4),(16,5,'Azul',4),(17,5,'Amarillo',4),(18,5,'Verde',4),(19,5,'Rojo',4),(20,5,'Blanco',4),(21,4,'Negro',4),(22,5,'Gris',5),(23,4,'Blanco',5),(24,5,'Verde',5),(25,4,'Naranja',5),(26,4,'Azul',5),(27,5,'Blanco',5),(29,5,'Negro',5);
/*!40000 ALTER TABLE `coche` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `moto`
--

DROP TABLE IF EXISTS `moto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `moto` (
  `id_Vehiculo` int NOT NULL,
  `cc` int DEFAULT NULL,
  KEY `moto_vehiculo_idx` (`id_Vehiculo`),
  CONSTRAINT `moto_vehiculo` FOREIGN KEY (`id_Vehiculo`) REFERENCES `vehiculo` (`id_Vehiculo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `moto`
--

LOCK TABLES `moto` WRITE;
/*!40000 ALTER TABLE `moto` DISABLE KEYS */;
INSERT INTO `moto` VALUES (3,300),(4,1200),(9,600),(10,800),(28,250),(30,600),(31,125),(32,150),(33,200),(34,450);
/*!40000 ALTER TABLE `moto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id_Usuario` int NOT NULL AUTO_INCREMENT,
  `nombre_Usuario` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `password_encriptada` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `tipo_Usuario` enum('admin','user') DEFAULT 'user',
  `es_Cliente` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id_Usuario`),
  UNIQUE KEY `unique_nombre_usuario` (`nombre_Usuario`),
  UNIQUE KEY `unique_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'admin','admin','$2a$10$qpZvFwRu6TJbBcb2JJPr3uPDeTYCOEOGq5X0Atii5DXcKxM7Yu7s2','admin@admin.com','admin',1),(2,'user','user','$2a$10$XCfeTupwTGbzFyHlKUZP1.icUOWjWpQWDfbIZJWuguyUqolX2w5Ee','user@user.com','user',1);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehiculo`
--

DROP TABLE IF EXISTS `vehiculo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehiculo` (
  `id_Vehiculo` int NOT NULL AUTO_INCREMENT,
  `marca` varchar(255) NOT NULL,
  `modelo` varchar(255) NOT NULL,
  `matricula` varchar(255) NOT NULL,
  `precio_dia` double NOT NULL,
  `localizacion` enum('Madrid','Barcelona','Bilbao','Sevilla','Valencia') NOT NULL,
  `tipo_Vehiculo` enum('Coche','Moto') NOT NULL,
  PRIMARY KEY (`id_Vehiculo`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehiculo`
--

LOCK TABLES `vehiculo` WRITE;
/*!40000 ALTER TABLE `vehiculo` DISABLE KEYS */;
INSERT INTO `vehiculo` VALUES (1,'Ford','Fiesta','1234AAA',25,'Madrid','Coche'),(2,'Kia','Ceed','2345BBB',30,'Barcelona','Coche'),(3,'Honda','CRF','3456CCC',18,'Bilbao','Moto'),(4,'Ducati','Multistrada','4567DDD',35,'Sevilla','Moto'),(5,'Dacia','Sandero','5678EEE',20,'Valencia','Coche'),(6,'Toyota','Corolla','6789FFF',22,'Madrid','Coche'),(7,'Renault','Clio','7890GGG',24,'Barcelona','Coche'),(8,'Peugeot','208','8901HHH',27,'Bilbao','Coche'),(9,'Yamaha','YZF','9012III',33,'Sevilla','Moto'),(10,'BMW','GS','0123JJJ',40,'Valencia','Moto'),(11,'Audi','A4','1234KKK',50,'Madrid','Coche'),(12,'Nissan','Qashqai','2345LLL',45,'Barcelona','Coche'),(13,'Mercedes','Clase A','3456MMM',60,'Bilbao','Coche'),(14,'Volkswagen','Golf','4567NNN',55,'Sevilla','Coche'),(15,'Mazda','CX-5','5678OOO',65,'Valencia','Coche'),(16,'Chevrolet','Spark','6789PPP',28,'Madrid','Coche'),(17,'Hyundai','i30','7890QQQ',30,'Barcelona','Coche'),(18,'Fiat','Punto','8901RRR',20,'Bilbao','Coche'),(19,'Opel','Astra','9012SSS',25,'Sevilla','Coche'),(20,'Hyundai','i30','1234KKK',26,'Madrid','Coche'),(21,'Mercedes','A-Class','2345LLL',40,'Barcelona','Coche'),(22,'Audi','A3','3456MMM',38,'Valencia','Coche'),(23,'BMW','1 Series','4567NNN',42,'Sevilla','Coche'),(24,'Peugeot','208','5678OOO',23,'Bilbao','Coche'),(25,'Nissan','Micra','6789PPP',19,'Madrid','Coche'),(26,'Mazda','3','7890QQQ',29,'Barcelona','Coche'),(27,'Citroën','C3','8901RRR',25,'Valencia','Coche'),(28,'Chevrolet','Spark','9012SSS',17,'Sevilla','Coche'),(29,'Fiat','500','0123TTT',15,'Bilbao','Coche'),(30,'Suzuki','Swift','1234UUU',22,'Madrid','Coche'),(31,'Subaru','Impreza','2345VVV',34,'Barcelona','Coche'),(32,'Jeep','Renegade','3456WWW',36,'Valencia','Coche'),(33,'Mini','Cooper','4567XXX',37,'Sevilla','Coche'),(34,'Tesla','Model 3','5678YYY',50,'Bilbao','Coche');
/*!40000 ALTER TABLE `vehiculo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'backbyte'
--

--
-- Dumping routines for database 'backbyte'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-24 22:49:06
