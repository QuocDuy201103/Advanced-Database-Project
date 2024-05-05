-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 06, 2023 at 03:40 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `fabric`
--

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

CREATE TABLE `accounts` (
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(60) NOT NULL,
  `role` varchar(20) NOT NULL DEFAULT 'user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `accounts`
--

INSERT INTO `accounts` (`username`, `password_hash`, `role`) VALUES
('admin', 'c4ca4238a0b923820dcc509a6f75849b', 'admin'),
('admin1', '827ccb0eea8a706c4c34a16891f84e7b', 'admin'),
('hocsinh', 'c4ca4238a0b923820dcc509a6f75849b', 'user'),
('OfficeStaff', 'c4ca4238a0b923820dcc509a6f75849b', 'OfficeStaff'),
('OperationStaff', 'c4ca4238a0b923820dcc509a6f75849b', 'OperationStaff'),
('partner_staff', 'c4ca4238a0b923820dcc509a6f75849b', 'partner_staff');

-- --------------------------------------------------------

--
-- Table structure for table `bolts`
--

CREATE TABLE `bolts` (
  `bolt_id` int(11) NOT NULL,
  `category_id` int(11) DEFAULT NULL,
  `order_id` int(11) DEFAULT NULL,
  `length` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bolts`
--

INSERT INTO `bolts` (`bolt_id`, `category_id`, `order_id`, `length`) VALUES
(1, 4, 1, 5.50),
(2, 5, 2, 7.20),
(3, 6, 1, 6.00),
(4, 7, 3, 8.50),
(5, 8, 2, 5.80);

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL,
  `supplier_id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `color` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`category_id`, `supplier_id`, `name`, `color`) VALUES
(4, 1, 'Silk', 'Cream'),
(5, 2, 'Khaki', 'Khaki'),
(6, 3, 'Crewel', 'Multicolor'),
(7, 1, 'Jacquard', 'Gold'),
(8, 2, 'Faux Silk Damask', 'Silver'),
(10, 7, 'zdfj', 'sdf'),
(11, 8, 'Cat', 'brow');

-- --------------------------------------------------------

--
-- Table structure for table `currentprice`
--

CREATE TABLE `currentprice` (
  `category_id` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `currentprice`
--

INSERT INTO `currentprice` (`category_id`, `price`, `date`) VALUES
(4, 12.99, '2023-02-15'),
(5, 9.75, '2023-02-15'),
(6, 15.50, '2023-02-15'),
(7, 18.99, '2023-02-15'),
(8, 22.50, '2023-02-15');

-- --------------------------------------------------------

--
-- Table structure for table `customerphonenum`
--

CREATE TABLE `customerphonenum` (
  `customer_id` int(11) NOT NULL,
  `phoneNumber` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customerphonenum`
--

INSERT INTO `customerphonenum` (`customer_id`, `phoneNumber`) VALUES
(1, '123-456-7890'),
(1, '555-555-5555'),
(2, '987-654-3210'),
(3, '111-222-3333');

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `customer_id` int(11) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `arrearage` decimal(10,2) DEFAULT NULL,
  `warningTime` datetime DEFAULT NULL,
  `offemployee_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`customer_id`, `first_name`, `last_name`, `address`, `arrearage`, `warningTime`, `offemployee_id`) VALUES
(1, 'Alice', 'Johnson', '123 Main St', 0.00, NULL, 4),
(2, 'Bob', 'Williams', '456 Oak St', 50.25, NULL, 4),
(3, 'Charlie', 'Smith', '789 Elm St', 30.75, NULL, 4),
(4, 'Marh', 'Hont', '756 Oik St', 100.25, NULL, 4);

--
-- Triggers `customers`
--
DELIMITER $$
CREATE TRIGGER `update_warning_time` AFTER UPDATE ON `customers` FOR EACH ROW BEGIN
    IF NEW.arrearage > 2000 AND NEW.warningTime IS NULL THEN
        UPDATE customer
        SET warningTime = CURRENT_DATE
        WHERE customer_id = NEW.customer_id;
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `update_warning_time_null` AFTER UPDATE ON `customers` FOR EACH ROW BEGIN
    IF NEW.arrearage < 2000 AND NEW.warningTime IS NOT NULL THEN
        UPDATE customer
        SET warningTime = NULL
        WHERE customer_id = NEW.customer_id;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `employees`
--

CREATE TABLE `employees` (
  `employeeID` int(11) NOT NULL,
  `Fname` varchar(255) DEFAULT NULL,
  `Lname` varchar(255) DEFAULT NULL,
  `Gender` varchar(10) DEFAULT NULL,
  `Address` varchar(255) DEFAULT NULL,
  `PhoneNumber` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employees`
--

INSERT INTO `employees` (`employeeID`, `Fname`, `Lname`, `Gender`, `Address`, `PhoneNumber`) VALUES
(1, 'John', 'Doe', 'Male', '123 Main St', '123-456-7890'),
(2, 'Jane', 'Smith', 'Female', '456 Oak St', '987-654-3210'),
(3, 'Mary', 'Johnson', 'Female', '789 Maple St', '555-123-4567'),
(4, 'Alexandra', 'Williams', 'Female', '321 Birch St', '777-888-9999'),
(5, 'Emily', 'Jones', 'Female', '654 Cedar St', '111-222-3333');

-- --------------------------------------------------------

--
-- Table structure for table `inventoryreceipt`
--

CREATE TABLE `inventoryreceipt` (
  `category_id` int(11) NOT NULL,
  `date` date NOT NULL,
  `purchase` decimal(10,2) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `inventoryreceipt`
--

INSERT INTO `inventoryreceipt` (`category_id`, `date`, `purchase`, `quantity`) VALUES
(4, '2023-02-20', 120.50, 100),
(5, '2023-02-20', 90.75, 80),
(6, '2023-02-20', 150.25, 120),
(7, '2023-02-20', 200.00, 150),
(8, '2023-02-20', 250.75, 200);

-- --------------------------------------------------------

--
-- Table structure for table `manager`
--

CREATE TABLE `manager` (
  `employee_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `manager`
--

INSERT INTO `manager` (`employee_id`) VALUES
(1);

-- --------------------------------------------------------

--
-- Table structure for table `officestaff`
--

CREATE TABLE `officestaff` (
  `employee_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `officestaff`
--

INSERT INTO `officestaff` (`employee_id`) VALUES
(4);

-- --------------------------------------------------------

--
-- Table structure for table `operationalstaff`
--

CREATE TABLE `operationalstaff` (
  `employee_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `operationalstaff`
--

INSERT INTO `operationalstaff` (`employee_id`) VALUES
(3);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `operational_staffID` int(11) DEFAULT NULL,
  `orderDate` date DEFAULT NULL,
  `orderTime` time DEFAULT NULL,
  `total_price` decimal(10,2) DEFAULT NULL,
  `order_status` varchar(50) DEFAULT NULL,
  `cancelReason` varchar(255) DEFAULT NULL,
  `processEmployeeID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `customer_id`, `operational_staffID`, `orderDate`, `orderTime`, `total_price`, `order_status`, `cancelReason`, `processEmployeeID`) VALUES
(1, 1, 3, '2023-02-01', '14:00:00', 100.50, 'Pending', NULL, 2),
(2, 2, 3, '2023-02-05', '10:30:00', 75.20, 'Completed', NULL, 1),
(3, 3, 3, '2023-02-10', '12:45:00', 50.00, 'Canceled', 'Out of stock', 4);

--
-- Triggers `orders`
--
DELIMITER $$
CREATE TRIGGER `update_arrearage` AFTER INSERT ON `orders` FOR EACH ROW BEGIN
    UPDATE customer SET arrearage = arrearage + NEW.total_price WHERE customer_id = NEW.customer_id;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `partialpayment`
--

CREATE TABLE `partialpayment` (
  `order_id` int(11) NOT NULL,
  `date` date NOT NULL,
  `amoutOfMoney` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `partialpayment`
--

INSERT INTO `partialpayment` (`order_id`, `date`, `amoutOfMoney`) VALUES
(1, '2023-02-25', 50.00),
(1, '2023-03-05', 30.00),
(2, '2023-02-28', 40.00),
(3, '2023-03-10', 25.00),
(3, '2023-03-15', 15.00);

--
-- Triggers `partialpayment`
--
DELIMITER $$
CREATE TRIGGER `update_arrearage2` AFTER INSERT ON `partialpayment` FOR EACH ROW BEGIN 
    UPDATE customers 
    SET arrearage = arrearage - NEW.amoutOfMoney 
    WHERE customer_id = (SELECT customer_id FROM `orders` WHERE order_id = NEW.order_id); 
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `partnerstaff`
--

CREATE TABLE `partnerstaff` (
  `employee_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `partnerstaff`
--

INSERT INTO `partnerstaff` (`employee_id`) VALUES
(2);

-- --------------------------------------------------------

--
-- Table structure for table `supplierphonenumber`
--

CREATE TABLE `supplierphonenumber` (
  `supplier_id` int(11) NOT NULL,
  `phoneNumber` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `supplierphonenumber`
--

INSERT INTO `supplierphonenumber` (`supplier_id`, `phoneNumber`) VALUES
(1, '123-456-7890'),
(1, '555-555-5555'),
(2, '987-654-3210'),
(3, '111-222-3333'),
(7, '111-222-333-444'),
(8, '0392585825'),
(9, '2132');

-- --------------------------------------------------------

--
-- Table structure for table `suppliers`
--

CREATE TABLE `suppliers` (
  `supplier_id` int(11) NOT NULL,
  `parEmployeeID` int(11) DEFAULT NULL,
  `taxcode` char(20) DEFAULT NULL,
  `name` char(255) DEFAULT NULL,
  `address` char(255) DEFAULT NULL,
  `bankAccount` char(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `suppliers`
--

INSERT INTO `suppliers` (`supplier_id`, `parEmployeeID`, `taxcode`, `name`, `address`, `bankAccount`) VALUES
(1, 2, 'ABC123', 'Supplier A', '123 Supplier St', '9876543210'),
(2, 2, 'XYZ456', 'Supplier B', '456 Vendor St', '1234567890'),
(3, 2, 'LMN789', 'Supplier C', '789 Distributor St', '4567890123'),
(4, 2, 'sđá', 'duy', 'hcm', '637128378'),
(7, 2, '2', 'asaaa', 'hcm', '123131'),
(8, 2, '17', 'Đức', '58 Nhiêu Tâm', '321439243'),
(9, 2, '123', 'Duy', '12 Dao tan', '3213');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `bolts`
--
ALTER TABLE `bolts`
  ADD PRIMARY KEY (`bolt_id`),
  ADD KEY `category_id` (`category_id`),
  ADD KEY `order_id` (`order_id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`),
  ADD KEY `supplier_id` (`supplier_id`);

--
-- Indexes for table `currentprice`
--
ALTER TABLE `currentprice`
  ADD PRIMARY KEY (`category_id`,`price`,`date`);

--
-- Indexes for table `customerphonenum`
--
ALTER TABLE `customerphonenum`
  ADD PRIMARY KEY (`customer_id`,`phoneNumber`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`customer_id`),
  ADD KEY `offemployee_id` (`offemployee_id`);

--
-- Indexes for table `employees`
--
ALTER TABLE `employees`
  ADD PRIMARY KEY (`employeeID`);

--
-- Indexes for table `inventoryreceipt`
--
ALTER TABLE `inventoryreceipt`
  ADD PRIMARY KEY (`category_id`,`date`,`purchase`,`quantity`);

--
-- Indexes for table `manager`
--
ALTER TABLE `manager`
  ADD PRIMARY KEY (`employee_id`);

--
-- Indexes for table `officestaff`
--
ALTER TABLE `officestaff`
  ADD PRIMARY KEY (`employee_id`);

--
-- Indexes for table `operationalstaff`
--
ALTER TABLE `operationalstaff`
  ADD PRIMARY KEY (`employee_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `customer_id` (`customer_id`),
  ADD KEY `operational_staffID` (`operational_staffID`),
  ADD KEY `processEmployeeID` (`processEmployeeID`);

--
-- Indexes for table `partialpayment`
--
ALTER TABLE `partialpayment`
  ADD PRIMARY KEY (`order_id`,`date`,`amoutOfMoney`),
  ADD KEY `order_id` (`order_id`);

--
-- Indexes for table `partnerstaff`
--
ALTER TABLE `partnerstaff`
  ADD PRIMARY KEY (`employee_id`);

--
-- Indexes for table `supplierphonenumber`
--
ALTER TABLE `supplierphonenumber`
  ADD PRIMARY KEY (`supplier_id`,`phoneNumber`);

--
-- Indexes for table `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`supplier_id`),
  ADD KEY `parEmployeeID` (`parEmployeeID`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bolts`
--
ALTER TABLE `bolts`
  ADD CONSTRAINT `bolts_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`),
  ADD CONSTRAINT `bolts_ibfk_2` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`);

--
-- Constraints for table `categories`
--
ALTER TABLE `categories`
  ADD CONSTRAINT `categories_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`);

--
-- Constraints for table `currentprice`
--
ALTER TABLE `currentprice`
  ADD CONSTRAINT `currentprice_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`);

--
-- Constraints for table `customerphonenum`
--
ALTER TABLE `customerphonenum`
  ADD CONSTRAINT `customerphonenum_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`);

--
-- Constraints for table `customers`
--
ALTER TABLE `customers`
  ADD CONSTRAINT `customers_ibfk_1` FOREIGN KEY (`offemployee_id`) REFERENCES `officestaff` (`employee_id`);

--
-- Constraints for table `inventoryreceipt`
--
ALTER TABLE `inventoryreceipt`
  ADD CONSTRAINT `inventoryreceipt_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`);

--
-- Constraints for table `manager`
--
ALTER TABLE `manager`
  ADD CONSTRAINT `manager_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employeeID`);

--
-- Constraints for table `officestaff`
--
ALTER TABLE `officestaff`
  ADD CONSTRAINT `officestaff_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employeeID`);

--
-- Constraints for table `operationalstaff`
--
ALTER TABLE `operationalstaff`
  ADD CONSTRAINT `operationalstaff_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employeeID`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`),
  ADD CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`operational_staffID`) REFERENCES `operationalstaff` (`employee_id`),
  ADD CONSTRAINT `orders_ibfk_3` FOREIGN KEY (`processEmployeeID`) REFERENCES `employees` (`employeeID`);

--
-- Constraints for table `partnerstaff`
--
ALTER TABLE `partnerstaff`
  ADD CONSTRAINT `partnerstaff_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employeeID`);

--
-- Constraints for table `supplierphonenumber`
--
ALTER TABLE `supplierphonenumber`
  ADD CONSTRAINT `supplierphonenumber_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`);

--
-- Constraints for table `suppliers`
--
ALTER TABLE `suppliers`
  ADD CONSTRAINT `suppliers_ibfk_1` FOREIGN KEY (`parEmployeeID`) REFERENCES `partnerstaff` (`employee_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
