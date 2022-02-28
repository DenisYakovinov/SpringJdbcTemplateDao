SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE audiences RESTART IDENTITY;
INSERT INTO audiences (audience_type, audience_number, open_time, closing_time) VALUES ('lecture', 1, '08:30:00', '20:00:00' );
INSERT INTO audiences (audience_type, audience_number, open_time, closing_time) VALUES ('seminar', 2, '08:30:00', '18:00:00' );
INSERT INTO audiences (audience_type, audience_number, open_time, closing_time) VALUES ('interactive', 3, '08:30:00', '18:00:00' );
SET REFERENTIAL_INTEGRITY TRUE;