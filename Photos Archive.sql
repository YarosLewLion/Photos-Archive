CREATE DATABASE photo_archive_db;
USE photo_archive_db;
CREATE TABLE photos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    data LONGBLOB
);
ALTER TABLE photos MODIFY COLUMN data LONGBLOB;
DESCRIBE photos;
SET GLOBAL max_allowed_packet=67108864;
SHOW VARIABLES LIKE 'max_allowed_packet'