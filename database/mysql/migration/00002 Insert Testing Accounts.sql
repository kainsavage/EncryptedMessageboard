/* Inserts some testing user accounts.  Delete these in a production environment. */

INSERT INTO `user` (userusername, userfirstname, userlastname, userinitials, useremail, userpassword, enabled) VALUES ('user', 'first', 'last', 'fl', 'none-user@techempower.com', 'test', 1);
INSERT INTO `user` (userusername, userfirstname, userlastname, userinitials, useremail, userpassword, enabled) VALUES ('admin', 'first', 'last', 'fl', 'none-admin@techempower.com', 'test', 1);

INSERT INTO `usertogroup` (userid, groupid) VALUES (1, 1);
INSERT INTO `usertogroup` (userid, groupid) VALUES (2, 1);
INSERT INTO `usertogroup` (userid, groupid) VALUES (2, 1000);