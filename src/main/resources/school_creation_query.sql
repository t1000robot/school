CREATE TABLE groups (
	group_id SMALLSERIAL NOT NULL PRIMARY KEY,
	group_name VARCHAR(6)
);

CREATE TABLE students (
	student_id SMALLSERIAL NOT NULL PRIMARY KEY,
	group_id SMALLINT,
	first_name VARCHAR(30),
	last_name VARCHAR(30),
	CONSTRAINT groups_group_id_fk FOREIGN KEY (group_id) REFERENCES groups (group_id)
);

CREATE TABLE courses (
	course_id SMALLSERIAL NOT NULL PRIMARY KEY,
	course_name VARCHAR(30),
	course_description VARCHAR(30)
);

CREATE TABLE students_courses (
	student_id SMALLINT,
	course_id SMALLINT,
	CONSTRAINT students_student_id FOREIGN KEY (student_id) REFERENCES students (student_id),
	CONSTRAINT courses_course_id FOREIGN KEY (course_id) REFERENCES courses (course_id)
);
