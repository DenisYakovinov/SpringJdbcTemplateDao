SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE subjects RESTART IDENTITY;
INSERT INTO subjects (name, description) VALUES ('math', 'base course');
INSERT INTO subjects (name, description) VALUES ('history', 'base course');
INSERT INTO subjects (name, description) VALUES ('economy', 'base course');
SET REFERENTIAL_INTEGRITY TRUE;