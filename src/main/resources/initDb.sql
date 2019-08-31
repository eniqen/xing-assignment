DROP TABLE IF EXISTS USER_INFO;

CREATE TABLE IF NOT EXISTS USER_INFO (
  ID               INT         NOT NULL,
  JOB_TITLE        VARCHAR(50) NOT NULL,
  START            DATE        NOT NULL,
  END              DATE        NULL
);

CREATE INDEX IF NOT EXISTS id_start_index ON USER_INFO (ID, START);
CREATE INDEX IF NOT EXISTS user_info_id_index ON USER_INFO (ID);

INSERT INTO USER_INFO VALUES (42, 'Scala developer', '2018-05-15', NULL);
INSERT INTO USER_INFO VALUES (42, 'J2EE developer', '2017-01-15', '2018-02-15');
INSERT INTO USER_INFO VALUES (42, 'Web developer', '2015-05-15', '2016-11-15');
INSERT INTO USER_INFO VALUES (76, 'Data Scientist', '2017-01-15', NULL);
INSERT INTO USER_INFO VALUES (76, 'Web developer', '2013-12-15', '2016-12-15');
INSERT INTO USER_INFO VALUES (103, 'J2EE developer', '2016-12-15', NULL);
INSERT INTO USER_INFO VALUES (103, 'Web developer', '2011-07-15', '2015-07-15');