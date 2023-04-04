-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema pixels_rover
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema pixels_rover
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `pixels_rover` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;
USE `pixels_rover` ;

-- -----------------------------------------------------
-- Table `pixels_rover`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pixels_rover`.`user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(16) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `affiliation` VARCHAR(128) NOT NULL,
  `email` VARCHAR(128) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL,
  `password` VARCHAR(32) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL,
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
