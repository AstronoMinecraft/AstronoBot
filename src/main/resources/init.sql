CREATE TABLE `Astrono`.`verified_users` (
    `uuid` CHAR(36) NOT NULL PRIMARY KEY,
    `username` VARCHAR(16),
    `discordId` BIGINT(32) DEFAULT 0
);