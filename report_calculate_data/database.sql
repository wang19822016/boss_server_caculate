DROP TABLE IF EXISTS device_base;
CREATE TABLE device_base
(
  deviceId VARCHAR(40),
  deviceType VARCHAR(10),
  country VARCHAR(10),
  serverTime DATETIME,
  PRIMARY KEY(deviceID)
)DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS user_base;
CREATE TABLE user_base
(
  userId BIGINT(20) DEFAULT 0,
  deviceID VARCHAR(40),
  deviceType VARCHAR(10),
  country VARCHAR(10),
  serverTime DATETIME,
  PRIMARY KEY (userId)
) DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS user_login;
CREATE TABLE user_login
(
  userId BIGINT(20) DEFAULT 0,
  serverTime DATETIME,
  INDEX (userId)
) DEFAULT CHARSET=utf8;
