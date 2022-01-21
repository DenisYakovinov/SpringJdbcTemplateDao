SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE timetable_classes RESTART IDENTITY;
INSERT INTO timetable_classes (day_of_week, class_number, subject_id,  group_id, audience_id, teacher_id) VALUES (1, 1, 1, 1, 1, 1);
INSERT INTO timetable_classes (day_of_week, class_number, subject_id, group_id, audience_id, teacher_id) VALUES (1, 2, 2, 1, 2, 3);
INSERT INTO timetable_classes (day_of_week, class_number, subject_id, group_id, audience_id, teacher_id) VALUES (3, 1, 2, 2, 1, 1);
SET REFERENTIAL_INTEGRITY TRUE;