ALTER TABLE `user` 
ADD COLUMN `publickey` VARCHAR(2048) NOT NULL AFTER `userlastpasswordchange`;
ALTER TABLE `user` 
ADD COLUMN `friendsonlymessaging` TINYINT(1) NOT NULL AFTER `publickey`;
