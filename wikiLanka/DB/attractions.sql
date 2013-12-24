-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 24, 2013 at 03:32 PM
-- Server version: 5.5.27-log
-- PHP Version: 5.3.20

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `MegaMind`
--

-- --------------------------------------------------------

--
-- Table structure for table `attractions`
--

CREATE TABLE IF NOT EXISTS `attractions` (
  `index` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `lattitude` varchar(11) DEFAULT NULL,
  `longitude` varchar(11) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`index`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=60 ;

--
-- Dumping data for table `attractions`
--

INSERT INTO `attractions` (`index`, `name`, `lattitude`, `longitude`, `type`) VALUES
(1, 'Negombo', '7.233061', '79.841766', 'Beach Holidays'),
(2, 'Pasikudah', '7.928675', '81.581726', 'Beach Holidays'),
(3, 'wadduwa', '6.658469', '79.981327', 'Beach Holidays'),
(4, 'kosgoda', '6.350104', '80.018578', 'Beach Holidays'),
(5, 'Beruwala', '6.478726', '79.981327', 'Beach Holidays'),
(6, 'Bentota', '6.414248', '79.994888', 'Beach Holidays'),
(7, 'Trincomalee', '8.598674', '81.233597', 'Beach Holidays'),
(8, 'Nilaveli', '8.695409', '81.197891', 'Beach Holidays'),
(9, 'Tangalle', '6.011764', '80.785732', 'Beach Holidays'),
(10, 'Dondra Head', '5.921276', '80.594072', 'Beach Holidays'),
(11, 'Mirissa', '5.943643', '80.454297', 'Beach Holidays'),
(12, 'Weligama', '5.957388', '80.413055', 'Beach Holidays'),
(13, 'Unawatuna', '6.00724', '80.242939', 'Beach Holidays'),
(14, 'Hikkaduwa', '6.136885', '80.098829', 'Beach Holidays'),
(15, 'Arugambay', '6.839915', '81.836901', 'Beach Holidays'),
(16, 'Anuradhapura', '8.35036', '80.396662', 'Discover the past'),
(17, 'Polonnaruwa', '7.966163', '81.005545', 'Discover the past'),
(18, 'Temple of the Tooth Relic, Kandy', '7.292299', '80.642717', 'Discover the past'),
(19, 'Aluviharaya temple', '7.497584', '80.621962', 'Discover the past'),
(20, 'Lankathilake temple', '7.233785', '80.565212', 'Discover the past'),
(21, 'Gadaladeniya', '7.257136', '80.556457', 'Discover the past'),
(22, 'Dambulla Cave Temple', '7.854348', '80.651858', 'Discover the past'),
(23, 'Mihintale', '8.350658', '80.51683', 'Discover the past'),
(24, 'Weherahena temple', '5.952991', '80.575683', 'Pilgrimage'),
(25, 'Wevurukannala temple', '5.977086', '80.69973', 'Pilgrimage'),
(26, 'Kalutara Bodiya', '6.586554', '79.960513', 'Pilgrimage'),
(27, 'Gangaramaya temple', '6.916351', '79.856915', 'Pilgrimage'),
(28, 'Kelaniya temple', '7.958024', '79.918692', 'Pilgrimage'),
(29, 'Nallur Kovil', '9.674707', '80.029564', 'Pilgrimage'),
(30, 'Adam''s Peak', '6.80964', '80.499895', 'Pilgrimage'),
(31, 'Bogoda Bridge & Temple', '6.993861', '80.994987', 'Pilgrimage'),
(32, 'Buduruwagala', '6.692569', '81.11412', 'Pilgrimage'),
(33, 'dowa Cave Temple', '6.856107', '81.022239', 'Pilgrimage'),
(34, 'Wilpattu national park', '8.409885', '80.091705', 'Wild Safari'),
(35, 'Minneriya national Park', '7.993957', '80.843582', 'Wild Safari'),
(36, 'Kaudulla national  Park', '8.122452', '80.908127', 'Wild Safari'),
(37, 'Yala National Park', '6.571846', '81.375732', 'Wild Safari'),
(38, 'Bundala National park', '6.175713', '81.204071', 'Wild Safari'),
(39, 'Wasgamuwa National park', '7.753814', '80.92128', 'Wild Safari'),
(40, 'Horton Plains National Park', '6.798262', '80.763245', 'Wild Safari'),
(41, 'Udawalawe National Park', '6.507721', '80.879974', 'Wild Safari'),
(42, 'Kumana National park', '6.559568', '81.67511', 'Wild Safari'),
(43, 'Galoya National Park', '7.152675', '81.448517', 'Wild Safari'),
(44, 'Namal Uyana pink quarts', '7.909887', '80.580361', 'Nature Trails / Attractions / Activity'),
(45, 'Ritigala forest', '8.113912', '80.649304', 'Nature Trails / Attractions / Activity'),
(46, 'Hunnas range', '7.401388', '80.693271', 'Nature Trails / Attractions / Activity'),
(47, 'Udawatte Kelle', '7.299195', '80.64276', 'Nature Trails / Attractions / Activity'),
(48, 'Singharaja forest', '6.389961', '80.501719', 'Nature Trails / Attractions / Activity'),
(49, 'whale watching Mirissa', '5.937454', '80.463223', 'Nature Trails / Attractions / Activity'),
(50, 'Blow holes', '5.977235', '80.738869', 'Nature Trails / Attractions / Activity'),
(51, 'Stilt fisherman, weligama', '5.964559', '80.390224', 'Nature Trails / Attractions / Activity'),
(52, 'Kottawa forest reserve', '6.094726', '80.324414', 'Nature Trails / Attractions / Activity'),
(53, 'Kosgoda turtles', '6.333672', '80.02804', 'Nature Trails / Attractions / Activity'),
(54, 'Waterworld Kelaniya', '6.944043', '79.942575', 'Nature Trails / Attractions / Activity'),
(55, 'Leisure world Kaluaggala', '6.922806', '80.105535', 'Nature Trails / Attractions / Activity'),
(56, 'Pinnawela Elephant Orphanage', '7.300982', '80.387027', 'Nature Trails / Attractions / Activity'),
(57, 'Hot water wells Trincomalee', '8.603681', '81.171284', 'Nature Trails / Attractions / Activity'),
(58, 'Demodara railway station', '6.902058', '81.06245', 'Nature Trails / Attractions / Activity'),
(59, 'Little adams peak', '6.12353', '80.230064', 'Nature Trails / Attractions / Activity');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
