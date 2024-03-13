CREATE DATABASE `go_nature`
 /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */
 /*!80016 DEFAULT ENCRYPTION='N' */;
 use go_nature;
 CREATE TABLE `parks`(
    `parkName` varchar(255),
    `capacity` int,
    `diff` int,
    `visitTimeInMinutes` int,
    `currentVisitors` int,
    primary key (parkName));
INSERT INTO Parks (parkName, capacity, diff, visitTimeInMinutes, currentVisitors) VALUES
    ('Central Park', 1000, 100, 4*60, 0),
    ('Hyde Park', 800, 40, 4*60, 0),
    ('Regent\'s Park', 500, 20, 4*60, 0);
 CREATE TABLE `orders` (
  `orderId` int NOT NULL AUTO_INCREMENT,
  `visitor_id` int DEFAULT NULL,
  `park_name` varchar(90) DEFAULT NULL,
  `time_of_visit` datetime DEFAULT NULL,
  `visitor_number` int DEFAULT NULL,
  `visitor_email` varchar(90) DEFAULT NULL,
  `visitor_phone` varchar(90) DEFAULT NULL,
  PRIMARY KEY (`orderId`),
  FOREIGN KEY (`park_name`) REFERENCES `parks`(`parkName`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci; 
CREATE TABLE `users` (
  `username` varchar(80) NOT NULL,
  `password` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
