CREATE TABLE `usermessages` (
    `user` INT(10) UNSIGNED NOT NULL, 
    `message` INT(10) UNSIGNED NOT NULL,
    PRIMARY KEY(`user`, `message`),
    CONSTRAINT `user`
        FOREIGN KEY (`user`) 
        REFERENCES `user` (`id`) 
        ON DELETE CASCADE,
    CONSTRAINT `message`
        FOREIGN KEY (`message`) 
        REFERENCES `message` (`id`) 
        ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

ALTER TABLE `message` RENAME TO  `sent` ;
ALTER TABLE `sent` DROP FOREIGN KEY `sender`;
ALTER TABLE `sent` DROP COLUMN `sender`;

CREATE TABLE `received` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `read` TINYINT(1) NOT NULL,
  `message` TEXT NOT NULL,
  `created` DATETIME NOT NULL,
  `sender` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `sender`
    FOREIGN KEY (`id`)
    REFERENCES `user` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `usermessages` DROP FOREIGN KEY `message`;
ALTER TABLE `usermessages` CHANGE COLUMN `message` `sent` INT(10) UNSIGNED NOT NULL , RENAME TO  `usersentmessages` ;
ALTER TABLE `usersentmessages` ADD CONSTRAINT `message`
  FOREIGN KEY (`sent`)
  REFERENCES `sent` (`id`)
  ON DELETE CASCADE;
ALTER TABLE `usersentmessages` 
DROP INDEX `message` , ADD INDEX `sent` (`sent` ASC), ADD INDEX `user` (`user` ASC);

CREATE TABLE `userreceivedmessages` (
  `user` INT(10) UNSIGNED NOT NULL,
  `received` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`user`, `received`),
  INDEX `user` (`user` ASC),
  INDEX `received` (`received` ASC),
  CONSTRAINT `user_received`
    FOREIGN KEY (`user`)
    REFERENCES `user` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `message_received`
    FOREIGN KEY (`received`)
    REFERENCES `received` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB DEFAULT CHARSET=utf8;