CREATE TABLE `friends` (
    `usera` INT(10) UNSIGNED NOT NULL, 
    `userb` INT(10) UNSIGNED NOT NULL,
    PRIMARY KEY(`usera`, `userb`),
    CONSTRAINT `usera`
        FOREIGN KEY (`usera`) 
        REFERENCES `user` (`id`) 
        ON DELETE CASCADE,
    CONSTRAINT `userb`
        FOREIGN KEY (`userb`) 
        REFERENCES `user` (`id`) 
        ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;