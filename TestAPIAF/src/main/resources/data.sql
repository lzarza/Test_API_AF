DROP TABLE IF EXISTS SETTINGS_COUNTRY;

CREATE TABLE SETTINGS_COUNTRY (
  ID INT AUTO_INCREMENT PRIMARY KEY,
  COUNTRY_NAME VARCHAR(250) NOT NULL UNIQUE,
  MINIMUM_AGE number NOT NULL
);

insert into SETTINGS_COUNTRY (COUNTRY_NAME, MINIMUM_AGE) values ('France',18)