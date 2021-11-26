package com.school.dao;

import com.school.domain.Course;
import com.school.domain.Group;
import com.school.domain.Student;
import com.school.controller.ApplicationMenu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class TestDataCreatorDao {

    private List<Group> groups;
    private List<Course> courses;
    private List<Student> students;
    private Random random = new Random();

    private Map<Integer, List<Integer>> coursesTestMap = new HashMap<>();

    public TestDataCreatorDao() {
        groups = ApplicationMenu.dataGenerator.getGroups();
        courses = ApplicationMenu.dataGenerator.getCourses();
        students = ApplicationMenu.dataGenerator.getStudents();
    }

    public Map<Integer, List<Integer>> getCoursesTestMap() {
        return coursesTestMap;
    }

    public void createTestData() {
        createGroups();
        createCourses();
        createStudents();
        setCourses();
    }

    private void createGroups() {
        String sqlQuery = "INSERT INTO groups (group_name) VALUES (?);";
        try (Connection testConnection = TestConnectionFactory.getInstance().getTestConnection()) {
            try (PreparedStatement statement = testConnection.prepareStatement(sqlQuery)) {
                for (Group group : groups) {
                    statement.setString(1, group.getName());
                    statement.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createCourses() {
        String sqlQuery = "INSERT INTO courses (course_name, course_description) VALUES (?, ?);";
        try (Connection testConnection = TestConnectionFactory.getInstance().getTestConnection()) {
            try (PreparedStatement statement = testConnection.prepareStatement(sqlQuery)) {
                for (Course course : courses) {
                    statement.setString(1, course.getName());
                    statement.setString(2, course.getDescription());
                    statement.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createStudents() {
        String sqlQuery =
            "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?);";
        try (Connection testConnection = TestConnectionFactory.getInstance().getTestConnection()) {
            try (PreparedStatement statement = testConnection.prepareStatement(sqlQuery)) {
                for (Student student : students) {
                    statement.setInt(1, student.getGroupId());
                    statement.setString(2, student.getFirstName());
                    statement.setString(3, student.getLastName());
                    statement.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setCourses() {
        for (int courseQuantity = 10; courseQuantity > 0; courseQuantity--) {
            coursesTestMap.put(courseQuantity, new ArrayList<>());
        }
        String sqlQuery = "INSERT INTO students_courses (student_id, course_id) VALUES (?, ?);";
        try (Connection testConnection = TestConnectionFactory.getInstance().getTestConnection()) {
            try (PreparedStatement statement = testConnection.prepareStatement(sqlQuery)) {
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
                        coursesTestMap.get(coursesIds.get(courseNumber)).add(studentIndex);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
