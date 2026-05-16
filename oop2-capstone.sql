-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 16, 2026 at 04:25 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `oop2-capstone`
--

-- --------------------------------------------------------

--
-- Table structure for table `blockedusers`
--

CREATE TABLE `blockedusers` (
  `blockedUsersID` int(11) NOT NULL,
  `userID` int(11) DEFAULT NULL,
  `blockedUntil` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `equipment`
--

CREATE TABLE `equipment` (
  `equipmentID` int(11) NOT NULL,
  `equipmentName` varchar(255) NOT NULL,
  `category` varchar(100) NOT NULL,
  `modelNo` varchar(255) NOT NULL,
  `serialNo` varchar(255) NOT NULL,
  `condition` varchar(255) NOT NULL,
  `totalQty` int(11) NOT NULL,
  `availableQty` int(11) NOT NULL,
  `imagePath` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `equipment`
--

INSERT INTO `equipment` (`equipmentID`, `equipmentName`, `category`, `modelNo`, `serialNo`, `condition`, `totalQty`, `availableQty`, `imagePath`) VALUES
(1, 'Digital Multimeter', 'Engineering', 'Fluke-115', 'EN-FLK-9921', 'Good', 15, 12, ''),
(3, 'Oscilloscope', 'Engineering', 'TDS2024C', 'EN-OSC-4402', 'New', 5, 5, ''),
(4, 'Soldering Station', 'Engineering', 'WE1010NA', 'EN-SLD-1188', 'Used', 20, 18, ''),
(5, 'Function Generator', 'Engineering', 'AFG1022', 'EN-FGEN-339', 'Good', 8, 8, ''),
(6, 'Laser Level', 'Engineering', 'GLL3-330', 'EN-LLV-7710', 'New', 4, 3, ''),
(7, 'Digital Balances', 'Chemistry', 'ENT-220', 'CH-BAL-8821', 'Good', 10, 9, ''),
(8, 'Magnetic Stirrer', 'Chemistry', 'MS-H280', 'CH-STR-1104', 'New', 12, 12, ''),
(9, 'Spectrophotometer', 'Chemistry', 'GEN-10S', 'CH-SPEC-553', 'Good', 3, 2, ''),
(10, 'Centrifuge', 'Chemistry', '5424-R', 'CH-CEN-0091', 'Good', 5, 4, ''),
(11, 'pH Meter', 'Chemistry', 'HI-98103', 'CH-PHM-2234', 'New', 15, 14, ''),
(12, 'Stopwatch', 'Physical Education', 'SL-800', 'PE-STW-1122', 'Good', 30, 25, ''),
(13, 'Volleyball Net', 'Physical Education', 'VBN-PRO', 'PE-VBN-4400', 'New', 4, 4, ''),
(14, 'Plyometric Box Set', 'Physical Education', 'PB-3IN1', 'PE-PLY-6601', 'Good', 5, 5, ''),
(15, 'Agility Ladder', 'Physical Education', 'AG-SPEED', 'PE-AGL-1122', 'Used', 15, 13, ''),
(16, 'Battle Ropes', 'Physical Education', 'BR-50FT', 'PE-BRP-3311', 'Good', 6, 4, ''),
(17, 'DSLR Camera', 'Multi Media', 'EOS-90D', 'MM-CAM-7721', 'New', 6, 5, ''),
(18, 'Tripod', 'Multi Media', '190XPRO', 'MM-TRI-0044', 'Good', 10, 10, ''),
(19, 'Shotgun Mic', 'Multi Media', 'VideoMic-NTG', 'MM-MIC-8812', 'Good', 8, 4, ''),
(20, 'LED Video Light', 'Multi Media', 'VL-200', 'MM-LIT-2290', 'New', 12, 10, ''),
(21, 'Graphic Tablet', 'Multi Media', 'Intuos-Pro', 'MM-TAB-5561', 'Good', 15, 12, ''),
(22, 'Microscope', 'Medical Sciences', 'CX23', 'MD-MIC-3301', 'Good', 20, 12, ''),
(23, 'Sphygmomanometer', 'Medical Sciences', 'BP-100', 'MD-SPH-4492', 'New', 15, 15, ''),
(24, 'Pulse Oximeter', 'Medical Sciences', 'PO-30', 'MD-PLX-1120', 'Good', 25, 18, ''),
(25, 'Anatomy Manikin', 'Medical Sciences', 'PO-30', 'MD-MAN-5588', 'Good', 3, 3, ''),
(26, 'Stethoscope', 'Medical Sciences', 'Littmann-C3', 'MD-STT-9904', 'New', 20, 16, ''),
(27, 'Raspberry Pi 4', 'Information Technology', 'RPI4-8GB', 'IT-RPI-1102', 'New', 30, 28, ''),
(28, 'VR Headset', 'Information Technology', 'Quest-2', 'IT-VRH-6677', 'Good', 5, 4, ''),
(29, 'Network Switch', 'Information Technology', 'Catalyst-2960', 'IT-SWT-4431', 'Used', 6, 6, ''),
(30, 'Arduino Starter Kit', 'Information Technology', 'Uno-R3', 'IT-ARD-8819', 'New', 25, 25, ''),
(31, 'External Hard Drive', 'Information Technology', 'HD-1TB', 'IT-EHD-2250', 'Good', 10, 6, ''),
(32, 'Drafting Table', 'Architecture', 'DT-PRO', 'AR-DFT-3310', 'Good', 20, 6, ''),
(33, 'Scale Ruler', 'Architecture', 'TRI-SC-12', 'AR-SCL-5502', 'New', 50, 46, ''),
(34, 'T-Square', 'Architecture', 'TSQ-36', 'AR-TSQ-9912', 'Good', 30, 26, ''),
(35, '3D Printer', 'Architecture', 'Ender-3', 'AR-3DP-4481', 'Good', 4, 2, ''),
(36, 'Plotter Printer', 'Architecture', 'DesignJet-T650', 'AR-PLT-1166', 'New', 2, 2, ''),
(37, 'Soil Moisture Sensor', 'Agriculture', 'SMS-500', 'AG-SMS-7711', 'New', 20, 20, ''),
(38, 'Hand Trowel', 'Agriculture', 'HT-ALUM', 'AG-HTR-2299', 'Good', 40, 35, ''),
(39, 'Digital Grain Scale', 'Agriculture', 'DGS-10', 'AG-DGS-4450', 'Good', 5, 2, ''),
(40, 'Pruning Shears', 'Agriculture', 'PS-BYPASS', 'AG-PRS-1123', 'Good', 12, 11, ''),
(41, 'pH Soil Tester', 'Agriculture', 'ST-LUSTER', 'AG-STT-6644', 'New', 10, 10, '');

-- --------------------------------------------------------

--
-- Table structure for table `transaction`
--

CREATE TABLE `transaction` (
  `transactionID` int(11) NOT NULL,
  `equipmentID` int(11) DEFAULT NULL,
  `userID` int(11) DEFAULT NULL,
  `dateBorrowed` date DEFAULT NULL,
  `dateReturned` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `userType` varchar(255) DEFAULT NULL,
  `isBlocked` tinyint(1) DEFAULT NULL,
  `profilePhotoPath` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `password`, `userType`, `isBlocked`, `profilePhotoPath`) VALUES
(1, 'Admin', 'admin@cit.edu', '$2a$10$F8KENnm9mHA7gadGdm4lLOhPSiRBbAhbcc3lfRBnUyTbmDmJcwGpq', 'admin', NULL, '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `blockedusers`
--
ALTER TABLE `blockedusers`
  ADD PRIMARY KEY (`blockedUsersID`),
  ADD KEY `userID` (`userID`);

--
-- Indexes for table `equipment`
--
ALTER TABLE `equipment`
  ADD PRIMARY KEY (`equipmentID`);

--
-- Indexes for table `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`transactionID`),
  ADD KEY `equipmentID` (`equipmentID`),
  ADD KEY `userID` (`userID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `blockedusers`
--
ALTER TABLE `blockedusers`
  MODIFY `blockedUsersID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `equipment`
--
ALTER TABLE `equipment`
  MODIFY `equipmentID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT for table `transaction`
--
ALTER TABLE `transaction`
  MODIFY `transactionID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `blockedusers`
--
ALTER TABLE `blockedusers`
  ADD CONSTRAINT `blockedusers_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`id`);

--
-- Constraints for table `transaction`
--
ALTER TABLE `transaction`
  ADD CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`equipmentID`) REFERENCES `equipment` (`equipmentID`),
  ADD CONSTRAINT `transaction_ibfk_2` FOREIGN KEY (`userID`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
