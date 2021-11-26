package com.school.dao.student;

import com.school.domain.Course;
import com.school.domain.Student;
import java.util.List;

public interface StudentDao {

    void create(Student student);

    List<String> findStudentsByCourse(Course course);

    void addStudentToCourse(Student student, Course course);

    void delete(Student student);

    void removeStudentFromCourse(Student student, Course course);
}
