--
-- Create schema performance
--

CREATE DATABASE IF NOT EXISTS performance;
USE performance;

--
-- Definition of table `auditperformance`
--

DROP TABLE IF EXISTS `auditperformance`;
CREATE TABLE `auditperformance` (
  `Id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `transactionId` varchar(45) NOT NULL,
  `txTimeStamp` varchar(45) NOT NULL,
  `interface` varchar(45) NOT NULL,
  `attempts` bigint(20) unsigned NOT NULL,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

