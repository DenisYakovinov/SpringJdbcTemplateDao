CREATE TABLE teachers (
    teacher_id SERIAL NOT NULL PRIMARY KEY,
    first_name varchar(50),
    last_name varchar(50),
    acadeic_degree varchar(20),
    email varchar(50)
);

CREATE TABLE audiences (
    audience_id SERIAL NOT NULL PRIMARY KEY,
    audience_type VARCHAR(15),
    audience_number INTEGER,
    open_time TIME,
    closing_time TIME
);

CREATE TABLE groups (
    group_id SERIAL NOT NULL PRIMARY KEY,
    group_name varchar(15),
    specialty varchar(50)
);

CREATE TABLE students (
    student_id SERIAL NOT NULL PRIMARY KEY,
    first_name varchar(50),
    last_name varchar(50),
    year_number SMALLINT CHECK (year_number BETWEEN 1 AND 5),
    admission DATE,
    email varchar(50),
    group_id INTEGER REFERENCES groups
);

CREATE TABLE subjects (
    subject_id SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(50),
    description VARCHAR(50)
);

CREATE TABLE timetable_lines (
    timetable_line_id SERIAL NOT NULL PRIMARY KEY,
    day_of_week SMALLINT CHECK (day_of_week BETWEEN 1 AND 7),
    class_number SMALLINT,
    subject_id INTEGER REFERENCES subjects,
    group_id INTEGER REFERENCES groups,
    audience_id INTEGER REFERENCES audiences,
    teacher_id INTEGER REFERENCES teachers
);