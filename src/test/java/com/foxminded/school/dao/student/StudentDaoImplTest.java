package com.foxminded.school.dao.student;

import static org.junit.jupiter.api.Assertions.*;
import static com.foxminded.school.controller.ApplicationMenu.dataGenerator;

import com.foxminded.school.dao.QueryPathSupplier;
import com.foxminded.school.dao.SchoolTablesDao;
import com.foxminded.school.dao.TestConnectionFactory;
import com.foxminded.school.dao.TestDataCreatorDao;
import com.foxminded.school.domain.Course;
import com.foxminded.school.domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

class StudentDaoImplTest {
    Random random = new Random();
    private Map<Integer, List<Integer>> studentsCoursesTestMap;

    @BeforeEach
    void createTestData() {
        try {
            new SchoolTablesDao(new QueryPathSupplier()).createTables();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TestDataCreatorDao testData = new TestDataCreatorDao();
        testData.createTestData();
        studentsCoursesTestMap = testData.getCoursesTestMap();
    }

    @Test
    void findStudentsByCourse_shouldReturnProperStudentNameFormat_whenCourseValueIsCorrect() {
        List<String> studentList = new StudentDaoImpl()
            .findStudentsByCourse(dataGenerator.getCourses().get(random.nextInt(10)));
        assertFalse(studentList.isEmpty());
        for (String studentName : studentList) {
            String firstName = studentName.substring(0, studentName.indexOf(" "));
            String lastName = studentName.substring(studentName.indexOf(" ") + 1);
            char[] firstNameArray = firstName.toCharArray();
            char[] lastNameArray = lastName.toCharArray();
            assertTrue(Character.isUpperCase(firstNameArray[0]) &&
                Character.isUpperCase(lastNameArray[0]));
            for (char firstNameLetter : firstNameArray) {
                assertTrue(Character.isLetter(firstNameLetter));
            }
            for (char lastNameLetter : lastNameArray) {
                assertTrue(Character.isLetter(lastNameLetter));
            }
        }
    }

    @Test
    void findStudentsByCourse_shouldReturnProperResult_withRandomCourse() {
        int courseId = random.nextInt(10) + 1;
        Course course = dataGenerator
            .getCourses()
            .stream()
            .filter(x -> x.getId() == courseId)
            .findFirst()
            .get();
        List<String> expected = new StudentDaoImpl().findStudentsByCourse(course);
        List<Student> studentsOnCourse = new ArrayList<>();
        List<Integer> studentOnCourseIds = studentsCoursesTestMap.get(courseId);
        for (Student student : dataGenerator.getStudents()) {
            if (studentOnCourseIds.contains(student.getId())) {
                studentsOnCourse.add(student);
            }
        }
        List<String> actual = studentsOnCourse
            .stream()
            .map(student -> student.getFirstName() + " " + student.getLastName())
            .collect(Collectors.toList());
        for (String currentName : expected) {
            assertTrue(actual.contains(currentName));
        }
    }

    @Test
    void create_shouldCreateNewStudentInDataBase_whenInputDataAreValid() {
        Student actualStudent = new Student();
        int groupId = random.nextInt(10) + 1;
        String firstName = "Vadim";
        String lastName = "Mishukov";
        actualStudent.setGroupId(groupId);
        actualStudent.setFirstName(firstName);
        actualStudent.setLastName(lastName);
        new StudentDaoImpl().create(actualStudent);
        Student expectedStudent = new Student();
        String sqlQuery =
            "SELECT group_id, first_name, last_name FROM students WHERE group_id = " + groupId +
                " AND first_name = '" + firstName + "' AND last_name = '" + lastName + "';";
        try (Connection connection = TestConnectionFactory.getInstance().getTestConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                expectedStudent.setGroupId(resultSet.getInt("group_id"));
                expectedStudent.setFirstName(resultSet.getString("first_name"));
                expectedStudent.setLastName(resultSet.getString("last_name"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void delete_shouldDeleteStudentFromDataBase_ifInputDataAreValid() {
        Student student = dataGenerator.getStudents().stream().findFirst().get();
        new StudentDaoImpl().delete(student);
        String sqlQuery = "SELECT * FROM students WHERE group_id = " + student.getGroupId() +
            " AND first_name = '" + student.getFirstName() +
            "' AND last_name = '" + student.getLastName() + "';";
        try (Connection connection = TestConnectionFactory.getInstance().getTestConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                ResultSet resultSet = statement.executeQuery();
                assertFalse(resultSet.next());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addStudentToCourse_shouldAddStudentToCourse_whenInputDataAreValid() {
        Student actualStudent = dataGenerator.getStudents().stream().findFirst().get();
        Course actualCourse = dataGenerator.getCourses().stream().findFirst().get();
        new StudentDaoImpl().addStudentToCourse(actualStudent, actualCourse);
        String sqlQuery = "SELECT * FROM students_courses WHERE student_id = " +
            actualStudent.getId() + "AND course_id = " + actualCourse.getId() + ";";
        int expectedStudentId = 0;
        int expectedCourseId = 0;
        try (Connection connection = TestConnectionFactory.getInstance().getTestConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                expectedStudentId = resultSet.getInt("student_id");
                expectedCourseId = resultSet.getInt("course_id");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals(expectedStudentId, actualStudent.getId());
        assertEquals(expectedCourseId, actualCourse.getId());
    }

    @Test
    void removeStudentFromCourse() {
        int testCourseId = random.nextInt(10) + 1;
        int testStudentId = studentsCoursesTestMap
            .get(testCourseId)
            .stream()
            .findFirst()
            .get();
        Course testCourse = dataGenerator
            .getCourses()
            .stream()
            .filter(course -> course.getId() == testCourseId)
            .findFirst()
            .get();
        Student testStudent = dataGenerator
            .getStudents()
            .stream()
            .filter(student -> student.getId() == testStudentId)
            .findFirst()
            .get();
        new StudentDaoImpl().removeStudentFromCourse(testStudent, testCourse);
        String sqlQuery = "SELECT * FROM students_courses WHERE student_id = " +
            testStudentId + " AND course_id = " + testCourseId + ";";
        try (Connection connection = TestConnectionFactory.getInstance().getTestConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                ResultSet resultSet = statement.executeQuery();
                assertFalse(resultSet.next());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
