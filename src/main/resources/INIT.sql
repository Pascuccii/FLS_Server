CREATE DATABASE FLSDB;
USE FLSDB;

CREATE TABLE `FLSDB`.`users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  `password` VARCHAR(64) NOT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `access_mode` INT(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;
DROP TRIGGER IF EXISTS `FLSDB`.`users_AFTER_INSERT`;

DELIMITER $$
USE `FLSDB`$$
CREATE DEFINER = CURRENT_USER TRIGGER `FLSDB`.`users_AFTER_INSERT` AFTER INSERT ON `users` FOR EACH ROW
BEGIN
	INSERT INTO `user_configs` (`userId`, `theme`,`language`) VALUES (NEW.id, 'Light', 'English');
END$$
DELIMITER ;


CREATE TABLE `FLSDB`.`user_configs` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `theme` VARCHAR(20) NULL DEFAULT 'Dark',
  `language` VARCHAR(20) NULL DEFAULT 'English',
  `userId` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_grade_id_idx` (`userId` ASC) VISIBLE,
  CONSTRAINT `fk_grade_id`
    FOREIGN KEY (`userId`)
    REFERENCES `FLSDB`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


INSERT INTO `FLSDB`.`users` (`name`, `password`, `access_mode`) VALUES ('gleb', 'qZWb9gue6JnNlzkbLF61VcPc8nH/J31bzwb7VTapJho=', '1');

