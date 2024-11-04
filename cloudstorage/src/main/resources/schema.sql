CREATE TABLE IF NOT EXISTS USERS (
  userid INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(20) UNIQUE NOT NULL,
  salt VARCHAR(255),
  password VARCHAR(255) NOT NULL,
  firstname VARCHAR(20),
  lastname VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS NOTES (
    noteid INT PRIMARY KEY AUTO_INCREMENT,
    notetitle VARCHAR(20) NOT NULL,
    notedescription VARCHAR(1000),
    userid INT,
    FOREIGN KEY (userid) REFERENCES USERS(userid)
);

CREATE TABLE IF NOT EXISTS FILES (
    fileId INT PRIMARY KEY AUTO_INCREMENT,
    filename VARCHAR(255) NOT NULL,
    contenttype VARCHAR(255),
    filesize INT,
    userid INT,
    filedata BLOB,
    FOREIGN KEY (userid) REFERENCES USERS(userid)
);

CREATE TABLE IF NOT EXISTS CREDENTIALS (
    credentialid INT PRIMARY KEY AUTO_INCREMENT,
    url VARCHAR(100) NOT NULL,
    username VARCHAR(30),
    `key` VARCHAR(255),
    password VARCHAR(255),
    userid INT,
    FOREIGN KEY (userid) REFERENCES USERS(userid)
);
