SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE groups RESTART IDENTITY;
TRUNCATE TABLE students RESTART IDENTITY;
INSERT INTO groups (group_name, specialty) VALUES ('xx-zz', 'math sciences');
INSERT INTO groups (group_name, specialty) VALUES ('zz-cc', 'sport');
INSERT INTO groups (group_name, specialty) VALUES ('cc-vv', 'humanitarian sciences');
SET REFERENTIAL_INTEGRITY TRUE;