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