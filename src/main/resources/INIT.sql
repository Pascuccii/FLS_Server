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
	INSERT INTO `user_configs` (`userId`, `theme`,`language`) VALUES (NEW.id, 'Dark', 'English');
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



CREATE TABLE `flsdb`.`subject` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `hours` INT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;

CREATE TABLE `flsdb`.`teacher` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `patronymic` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;

CREATE TABLE `flsdb`.`group` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `Level` ENUM('A', 'B', 'C') NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;

CREATE TABLE `flsdb`.`student` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `patronymic` VARCHAR(45) NOT NULL,
  `groupId` INT NOT NULL,
  `email` VARCHAR(45) NULL,
  `phone` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_student_groupId_idx` (`groupId` ASC) VISIBLE,
  CONSTRAINT `fk_student_groupId`
    FOREIGN KEY (`groupId`)
    REFERENCES `flsdb`.`group` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;

CREATE TABLE `flsdb`.`teacher_subject` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `teacherId` INT NOT NULL,
  `subjectId` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_teacherSubject_teacherId_idx` (`teacherId` ASC) VISIBLE,
  INDEX `fk_teacherSubject_subjectId_idx` (`subjectId` ASC) VISIBLE,
  CONSTRAINT `fk_teacherSubject_teacherId`
    FOREIGN KEY (`teacherId`)
    REFERENCES `flsdb`.`teacher` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_teacherSubject_subjectId`
    FOREIGN KEY (`subjectId`)
    REFERENCES `flsdb`.`subject` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;

CREATE TABLE `flsdb`.`lesson` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `groupId` INT NOT NULL,
  `teacher_subjectId` INT NOT NULL,
  `cabinet` INT NOT NULL,
  `date` DATE NOT NULL,
  `time` TIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_lesson_groupId_idx` (`groupId` ASC) VISIBLE,
  INDEX `fk_lesson_teacher_subjectId_idx` (`teacher_subjectId` ASC) VISIBLE,
  CONSTRAINT `fk_lesson_groupId`
    FOREIGN KEY (`groupId`)
    REFERENCES `flsdb`.`group` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_lesson_teacher_subjectId`
    FOREIGN KEY (`teacher_subjectId`)
    REFERENCES `flsdb`.`teacher_subject` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


INSERT INTO `FLSDB`.`users` (`name`, `password`, `access_mode`) VALUES ('gleb', 'qZWb9gue6JnNlzkbLF61VcPc8nH/J31bzwb7VTapJho=', '1');

INSERT INTO `flsdb`.`subject` (`name`, `hours`) VALUES ('English', '50');
INSERT INTO `flsdb`.`subject` (`name`, `hours`) VALUES ('Spanish', '70');
INSERT INTO `flsdb`.`subject` (`name`, `hours`) VALUES ('Chineese', '110');
INSERT INTO `flsdb`.`subject` (`name`, `hours`) VALUES ('Polish', '60');

INSERT INTO `flsdb`.`teacher` (`name`, `surname`, `patronymic`) VALUES ('Дмитрий', 'Иванов', 'Петрович');
INSERT INTO `flsdb`.`teacher` (`name`, `surname`, `patronymic`) VALUES ('Александр', 'Степнов', 'Дмитриевич');
INSERT INTO `flsdb`.`teacher` (`name`, `surname`, `patronymic`) VALUES ('Анастасия', 'Антипова', 'Сергеевна');

INSERT INTO `flsdb`.`group` (`Level`) VALUES ('B');
INSERT INTO `flsdb`.`group` (`Level`) VALUES ('C');
INSERT INTO `flsdb`.`group` (`Level`) VALUES ('C');
INSERT INTO `flsdb`.`group` (`Level`) VALUES ('B');
INSERT INTO `flsdb`.`group` (`Level`) VALUES ('C');
INSERT INTO `flsdb`.`group` (`Level`) VALUES ('A');
INSERT INTO `flsdb`.`group` (`Level`) VALUES ('A');

INSERT INTO `flsdb`.`student` (`name`, `surname`, `patronymic`, `groupId`, `email`, `phone`) VALUES ('Глеб', 'Скачко', 'Дмитриевич', '2', 'skachko42@gmail.com', '+375445435390');
INSERT INTO `flsdb`.`student` (`name`, `surname`, `patronymic`, `groupId`) VALUES ('Александр', 'Сохоневич', 'Сергеевич', '4');
INSERT INTO `flsdb`.`student` (`name`, `surname`, `patronymic`, `groupId`, `email`) VALUES ('Иван', 'Петров', 'Анатольевич', '6', 'ivan.55@gmail.com');
INSERT INTO `flsdb`.`student` (`name`, `surname`, `patronymic`, `groupId`) VALUES ('Вероника', 'Михайлова', 'Николаевна', '1');

INSERT INTO `flsdb`.`teacher_subject` (`teacherId`, `subjectId`) VALUES ('1', '1');
INSERT INTO `flsdb`.`teacher_subject` (`teacherId`, `subjectId`) VALUES ('3', '3');
INSERT INTO `flsdb`.`teacher_subject` (`teacherId`, `subjectId`) VALUES ('1', '4');
INSERT INTO `flsdb`.`teacher_subject` (`teacherId`, `subjectId`) VALUES ('2', '3');

INSERT INTO `flsdb`.`lesson` (`groupId`, `teacher_subjectId`, `cabinet`, `date`, `time`) VALUES ('1', '1', '201', '2019-11-15', '13:25');
INSERT INTO `flsdb`.`lesson` (`groupId`, `teacher_subjectId`, `cabinet`, `date`, `time`) VALUES ('4', '2', '109', '2019-11-16', '15:20');
INSERT INTO `flsdb`.`lesson` (`groupId`, `teacher_subjectId`, `cabinet`, `date`, `time`) VALUES ('3', '3', '116', '2019-11-17', '9:45');
INSERT INTO `flsdb`.`lesson` (`groupId`, `teacher_subjectId`, `cabinet`, `date`, `time`) VALUES ('2', '2', '306', '2019-11-20', '8:00');





