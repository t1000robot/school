package com.school.dao.student;

import com.school.dao.ConnectionFactory;
import com.school.domain.Course;
import com.school.domain.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentDaoImpl implements StudentDao {

    private static final String FIND_STUDENTS_BY_COURSE_SQL =
        "SELECT st.first_name, st.last_name FROM students AS st " +
            "INNER JOIN students_courses AS sc ON st.student_id = sc.student_id " +
            "INNER JOIN courses AS c ON sc.course_id = c.course_id WHERE c.course_id = (?);";
    private static final String CREATE_SQL =
        "INSERT INTO students (group_id, first_name, last_name) VALUES ((?), (?), (?));";
    private static final String DELETE_SQL =
        "DELETE FROM students_courses WHERE student_id = (?); " +
            "DELETE FROM students WHERE student_id = (?);";
    private static final String ADD_STUDENT_TO_COURSE_SQL =
        "INSERT INTO students_courses (student_id, course_id) VALUES ((?), (?));";
    private static final String DELETE_STUDENT_FROM_COURSE_SQL =
        "DELETE FROM students_courses WHERE student_id = (?) AND course_id = (?);";
    private static final Logger log = LogManager.getLogger(StudentDaoImpl.class);

    @Override
    public List<String> findStudentsByCourse(Course course) {
        log.info("Finding student by course: {}", course);
        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
            log.trace("Open connection");
            try (PreparedStatement statement =
                     connection.prepareStatement(FIND_STUDENTS_BY_COURSE_SQL)) {
                log.trace("Create prepared statement for finding students");
                statement.setInt(1, course.getId());
                try {
                    log.trace("Getting result set");
                    ResultSet resultSet = statement.executeQuery();
                    List<String> result = new ArrayList<>();
                    while (resultSet.next()) {
                        result.add(resultSet.getString("first_name") +
                            " " + resultSet.getString("last_name"));
                    }
                    return result;
                } catch (SQLException e) {
                    log.warn("Cannot get result set", e);
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                log.warn("Cannot execute prepared statement", e);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            log.warn("Cannot open connection", e);
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public void create(Student student) {
        log.info("Creating student with " +
            "first name: {}, last name: {}, id: {}, group id: {}",
            student.getFirstName(), student.getLastName(), student.getId(), student.getGroupId());
        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
            log.trace("Open connection");
            try (PreparedStatement statement = connection.prepareStatement(CREATE_SQL)) {
                log.trace("Create prepared statement for creating student");
                statement.setInt(1, student.getGroupId());
                statement.setString(2, student.getFirstName());
                statement.setString(3, student.getLastName());
                statement.execute();
                System.out.println("Student was successfully added");
            } catch (SQLException e) {
                log.warn("Cannot execute prepared statement", e);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            log.warn("Cannot open connection", e);
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Student student) {
        log.info("Deleting student with id: {}", student.getId());
        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
            log.trace("Open connection");
            try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
                log.trace("Create prepared statement for deleting student");
                statement.setInt(1, student.getId());
                statement.setInt(2, student.getId());
                statement.execute();
                System.out.println("Student successfully deleted");
            } catch (SQLException e) {
                log.warn("Cannot execute prepared statement", e);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            log.warn("Cannot open connection", e);
            e.printStackTrace();
        }
    }

    @Override
    public void addStudentToCourse(Student student, Course course) {
        log.info("Adding student with id: {} to course: {}",
            student.getId(), course.getName());
        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
            log.trace("Open connection");
            try (PreparedStatement statement =
                     connection.prepareStatement(ADD_STUDENT_TO_COURSE_SQL)) {
                log.trace("Create prepared statement for adding student to course");
                statement.setInt(1, student.getId());
                statement.setInt(2, course.getId());
                statement.execute();
                System.out.println("Student was successfully added to course");
            } catch (SQLException e) {
                log.warn("Cannot execute prepared statement", e);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            log.warn("Cannot open connection", e);
            e.printStackTrace();
        }
    }

    @Override
    public void removeStudentFromCourse(Student student, Course course) {
        log.info("Removing student with id: {} from course: {}",
            student.getId(), course.getName());
        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
            log.trace("Open connection");
            try (PreparedStatement statement =
                     connection.prepareStatement(DELETE_STUDENT_FROM_COURSE_SQL)) {
                log.trace("Create prepared statement for removing student from course");
                statement.setInt(1, student.getId());
                statement.setInt(2, course.getId());
                statement.execute();
                System.out.println("Student was successfully removed from course");
            } catch (SQLException e) {
                log.warn("Cannot execute prepared statement", e);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            log.warn("Cannot open connection", e);
            e.printStackTrace();
        }
    }
}
