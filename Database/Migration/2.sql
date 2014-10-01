CREATE TABLE `em`.`message` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `read` TINYINT(1) NOT NULL,
  `message` TEXT NOT NULL,
  `created` DATETIME NOT NULL,
  `sender` INT(10) UNSIGNED NOT NULL,
  `recipient` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `sender`
    FOREIGN KEY (`id`)
    REFERENCES `em`.`user` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `recipient`
    FOREIGN KEY (`id`)
    REFERENCES `em`.`user` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB DEFAULT CHARSET=utf8;
