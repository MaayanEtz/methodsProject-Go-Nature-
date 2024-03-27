DROP SCHEMA `go_nature`;
CREATE DATABASE `go_nature` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

use go_nature;

CREATE TABLE `guides` (
	`visitor_id` varchar(9) NOT NULL,
	PRIMARY KEY (`visitor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `parks` (
	`parkName` varchar(255) NOT NULL,
	`capacity` int DEFAULT NULL,
	`diff` int DEFAULT NULL,
	`visitTimeInMinutes` int DEFAULT NULL,
	`currentVisitors` int DEFAULT NULL,
    `newDiff` int DEFAULT NULL,
    `newVisitTimeInMinutes` int DEFAULT NULL,
	PRIMARY KEY (`parkName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users` (
  `username` varchar(80) NOT NULL,
  `password` varchar(80) DEFAULT NULL,
  `type` varchar(80) DEFAULT NULL,
  `parkName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`username`),
  KEY `fk_parkName` (`parkName`),
  CONSTRAINT `fk_parkName` FOREIGN KEY (`parkName`) REFERENCES `parks` (`parkName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `orders` (
	`orderId` int NOT NULL AUTO_INCREMENT,
	`visitor_id` varchar(9) DEFAULT NULL,
	`parkName` varchar(90) DEFAULT NULL,
	`time_of_visit` datetime DEFAULT NULL,
	`visitor_number` int DEFAULT NULL,
	`visitor_email` varchar(90) DEFAULT NULL,
	`visitor_phone` varchar(90) DEFAULT NULL,
	`status` ENUM('Active', 'Cancelled', 'WaitList') DEFAULT NULL,
    `paid` BOOLEAN DEFAULT NULL,
	`reminderMsgSend` BOOLEAN DEFAULT NULL,
	`visitorConfirmedOrder` BOOLEAN DEFAULT NULL,
	PRIMARY KEY (`orderId`),
	KEY `parkName` (`parkName`),
	CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`parkName`) REFERENCES `parks` (`parkName`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `visits` (
	`visitId` INT NOT NULL AUTO_INCREMENT,
    `parkName` varchar(90) DEFAULT NULL,
    `timeOfEntrence` DATETIME DEFAULT NULL,
    `timeOfExit` DATETIME DEFAULT NULL,
    `numberOfVisitors` INT DEFAULT NULL,
    `isGroup` BOOLEAN,
    PRIMARY KEY(`visitId`),
    KEY `parkName` (`parkName`),
	CONSTRAINT `visits_ibfk_1` FOREIGN KEY (`parkName`) REFERENCES `parks` (`parkName`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `numberOfVisitorsReport` (
	`month` ENUM('1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'),
    `year` VARCHAR(4) NOT NULL,
    `parkName` VARCHAR(90) NOT NULL,
    `amountOfNonGroup` INT DEFAULT NULL,
    `amountOfGroup` INT DEFAULT NULL,
    PRIMARY KEY (`month`, `year`, `parkName`),
    KEY `parkName` (`parkName`),
	CONSTRAINT `numReport_ibfk_1` FOREIGN KEY (`parkName`) REFERENCES `parks` (`parkName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `parks` (`parkName`, `capacity`, `diff`, `visitTimeInMinutes`, `currentVisitors`) VALUES
    ('Central Park', 1000, 100, 4*60, 0),
    ('Hyde Park', 800, 40, 4*60, 0),
    ('Regent\'s Park', 500, 20, 4*60, 0);
    
INSERT INTO `orders` (`visitor_id`, `parkName`, `time_of_visit`, `visitor_number`, `visitor_email`, `visitor_phone`, `status`)
VALUES
	(123456789, 'Hyde Park', '2024-03-30 10:00:00', 7, 'example@example.com', '+0132456798', 'Active'),
	(123456789, 'Hyde Park', '2024-03-30 10:00:00', 7, 'example@example.com', '+0132456798', 'WaitList'),
	(123456789, 'Central Park', '2024-04-01 10:00:00', 7, 'example@example.com', '+0132456798', 'Active'),
	(123456789, 'Central Park', '2024-04-01 10:00:00', 7, 'example@example.com', '+0132456798', 'Cancelled'),
	(123412341, 'Hyde Park', '2024-04-01 10:00:00', 7, 'example@example.com', '+0132456798', 'Active'),
	(123412341, 'Regent\'s Park', '2024-04-01 10:00:00', 7, 'example@example.com', '+0132456798', 'Active'),
	(123456789, 'Hyde Park', '2024-03-30 10:00:00', 20, 'example@example.com', '+0132456798', 'Active'),
	(123456789, 'Hyde Park', '2024-03-30 10:00:00', 7, 'example@example.com', '+0132456798', 'Active'),
	(123456789, 'Hyde Park', '2024-03-30 10:00:00', 9, 'example@example.com', '+0132456798', 'Active'),
	(123456789, 'Regent\'s Park', '2024-03-30 10:00:00', 7, 'example@example.com', '+0132456798', 'WaitList');

INSERT INTO `guides` (`visitor_id`) VALUE ('123412341');

INSERT INTO `users` (`username`, `password`, `type`) VALUES
	('yossi', '123456', 'ServiceWorker'),
    ('moshe', '123456', 'DepartmentManager');
    
INSERT INTO `users` VALUE ('danny', '123456', 'ParkManager', 'Hyde Park');
    
INSERT INTO `visits` (`parkName`,`timeOfEntrence`, `timeOfExit`, `numberOfVisitors`, `isGroup`) VALUES
	('Hyde Park', '2024-03-30 10:00:00', '2024-03-30 12:00:00', 15, 0),
    ('Hyde Park', '2024-03-30 10:15:00', '2024-03-30 13:00:20', 10, 1),
    ('Regent\'s Park', '2024-03-30 09:00:00', '2024-03-30 12:00:00', 62, 0),
    ('Regent\'s Park', '2024-03-31 10:30:00', '2024-03-31 12:00:00', 8, 1),
    ('Central Park', '2024-04-01 10:00:00', '2024-03-30 12:00:00', 20, 0),
    ('Central Park', '2024-04-01 10:57:00', '2024-03-30 14:50:00', 73, 0);