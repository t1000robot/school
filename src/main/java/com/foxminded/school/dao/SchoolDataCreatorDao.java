package com.foxminded.school.dao;

import static com.foxminded.school.controller.ApplicationMenu.dataGenerator;

import com.foxminded.school.domain.Course;
import com.foxminded.school.domain.Group;
import com.foxminded.school.domain.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SchoolDataCreatorDao {

    private List<Group> groups;
    private List<Course> courses;
    private List<Student> students;
    private Random random = new Random();
    private static final Logger log = LogManager.getLogger(SchoolDataCreatorDao.class);

    public SchoolDataCreatorDao() {
        log.info("SchoolDataCreatorDao constructor launched");
        groups = dataGenerator.getGroups();
        courses = dataGenerator.getCourses();
        students = dataGenerator.getStudents();
    }

    public void createData() {
        log.info("createData method launched");
        createGroups();
        createCourses();
        createStudents();
        setCourses();
    }

    private void createGroups() {
        log.info("Creating group data for school database");
        String sqlQuery = "INSERT INTO groups (group_name) VALUES (?);";
        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
            log.trace("Open connection");
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                log.trace("Create prepared statement");
                for (Group group : groups) {
                    statement.setString(1, group.getName());
                    statement.execute();
                }
            } catch (SQLException e) {
                log.warn("Cannot execute creating group data query", e);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            log.warn("Cannot open connection", e);
            e.printStackTrace();
        }
    }

    private void createCourses() {
        log.info("Creating course data for school database");
        String sqlQuery = "INSERT INTO courses (course_name, course_description) VALUES (?, ?);";
        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
            log.trace("Open connection");
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                log.trace("Create prepared statement");
                for (Course course : courses) {
                    statement.setString(1, course.getName());
                    statement.setString(2, course.getDescription());
                    statement.execute();
                }
            } catch (SQLException e) {
                log.warn("Cannot execute creating course data query", e);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            log.warn("Cannot open connection", e);
            e.printStackTrace();
        }
    }

    private void createStudents() {
        log.info("Creating student data for school database");
        String sqlQuery =
            "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?);";
        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
            log.trace("Open connection");
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                log.trace("Create prepared statement");
                for (Student student : students) {
                    statement.setInt(1, student.getGroupId());
                    statement.setString(2, student.getFirstName());
                    statement.setString(3, student.getLastName());
                    statement.execute();
                }
            } catch (SQLException e) {
                log.warn("Cannot execute creating student data query", e);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            log.warn("Cannot open connection", e);
            e.printStackTrace();
        }
    }

    private void setCourses() {
        log.info("Adding students to courses in school database");
        String sqlQuery = "INSERT INTO students_courses (student_id, course_id) VALUES (?, ?);";
        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
            log.trace("Open connection");
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                log.trace("Create prepared statement");
                for (int studentIndex = 1; studentIndex <= 200; studentIndex++) {
                    List<Integer> coursesIds = random
                        .ints(10, 1, 11)
                        .boxed()
                        .distinct()
                        .limit(3)
                        .collect(Collectors.toList());
                    for (int courseNumber = random.nextInt(3);
                         courseNumber >= 0; courseNumber--) {
                        statement.setInt(1, studentIndex);
                        statement.setInt(2, coursesIds.get(courseNumber));
                        statement.execute();
                    }
                }
            } catch (SQLException e) {
                log.warn("Cannot execute creating adding students to courses query", e);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            log.warn("Cannot open connection", e);
            e.printStackTrace();
        }
    }
}
