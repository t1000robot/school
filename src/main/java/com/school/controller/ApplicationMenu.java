package com.school.controller;

import static com.school.domain.service.ConsoleInputReader.reader;

import com.school.dao.group.GroupDaoImpl;
import com.school.dao.SchoolDataCreatorDao;
import com.school.dao.student.StudentDaoImpl;
import com.school.domain.Course;
import com.school.domain.Student;
import com.school.domain.service.SchoolDataGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ApplicationMenu {

    public static final SchoolDataGenerator dataGenerator = new SchoolDataGenerator();
    private static final Logger log = LogManager.getLogger(ApplicationMenu.class);

    private static final String CHOICE_1 = "1";
    private static final String CHOICE_2 = "2";
    private static final String CHOICE_3 = "3";
    private static final String CHOICE_4 = "4";
    private static final String CHOICE_5 = "5";
    private static final String CHOICE_6 = "6";

    private static final String OPTION_1 =
        "1. Find all groups with less or equals student count.\n";
    private static final String OPTION_2 =
        "2. Find all students related to course with given name.\n";
    private static final String OPTION_3 =
        "3. Add new student.\n";
    private static final String OPTION_4 =
        "4. Delete student by STUDENT_ID.\n";
    private static final String OPTION_5 =
        "5. Add a student to the course (from a list).\n";
    private static final String OPTION_6 =
        "6. Remove the student from one of his or her courses.\n";

    public void runConsoleUI() {
        log.info("School console UI launched");
        new SchoolDataCreatorDao().createData();
        System.out.println(OPTION_1 + OPTION_2 + OPTION_3 + OPTION_4 + OPTION_5 + OPTION_6);
        String inputLine;
        while (true) {
            System.out.print("\nChoose number of option! To finish session enter \"exit\"\n");
            inputLine = reader.nextLine();
            if (inputLine.equals("exit")) {
                return;
            }
            if (inputLine.equals(CHOICE_1)) {
                System.out.print("You have chosen - " + OPTION_1);
                findGroupsByStudentsCount();
                continue;
            }
            if (inputLine.equals(CHOICE_2)) {
                System.out.print("You have chosen - " + OPTION_2);
                findStudentsByCourse();
                continue;
            }
            if (inputLine.equals(CHOICE_3)) {
                System.out.print("You have chosen - " + OPTION_3);
                create();
                continue;
            }
            if (inputLine.equals(CHOICE_4)) {
                System.out.print("You have chosen - " + OPTION_4);
                delete();
                continue;
            }
            if (inputLine.equals(CHOICE_5)) {
                System.out.print("You have chosen - " + OPTION_5);
                addStudentToCourse();
                continue;
            }
            if (inputLine.equals(CHOICE_6)) {
                System.out.print("You have chosen - " + OPTION_6);
                removeStudentFromCourse();
            } else {
                System.out.print("The data you entered is not valid");
            }
        }
    }

    private void findGroupsByStudentsCount() {
        log.info("findGroupsByStudentCount method in SchoolUI class launched");
        System.out.println("Enter count of students!");
        int count;
        try {
            count = Integer.parseInt(reader.nextLine());
        } catch (NumberFormatException e) {
            log.info("Incorrect number format entered", e);
            System.out.println("In is not a number!");
            return;
        }
        if (count > 0 && count < 50) {
            List<String> result = new GroupDaoImpl().findGroupsByStudentsCount(count);
            System.out.println("Result is - ");
            result.forEach(System.out::println);
        } else {
            log.info("Incorrect number was entered");
            System.out.println("Number you entered is not valid!");
        }
    }

    private void findStudentsByCourse() {
        log.info("findStudentsByCourse method in SchoolUI class launched");
        System.out.println("Enter course id. Courses available -");
        AtomicInteger atomicInteger = new AtomicInteger(1);
        dataGenerator
            .getCourses()
            .forEach(course -> System.out.println(
                "id " + atomicInteger.getAndIncrement() + " - " + course.getName()));
        int courseId;
        try {
            courseId = Integer.parseInt(reader.nextLine());
        } catch (NumberFormatException e) {
            log.info("Incorrect number format entered", e);
            System.out.println("In is not a number!");
            return;
        }
        if (!checkCourseId(courseId)) {
            return;
        }
        Course course = dataGenerator.getCourses().get(courseId);
        List<String> result = new StudentDaoImpl().findStudentsByCourse(course);
        System.out.println("Result is - ");
        result.forEach(System.out::println);
    }

    private void create() {
        log.info("create method in SchoolUI class launched");
        System.out.println("Enter student's first name!");
        String firstName = reader.nextLine();
        if (!checkName(firstName)) {
            return;
        }
        System.out.println("Enter student's last name!");
        String lastName = reader.nextLine();
        if (!checkName(lastName)) {
            return;
        }
        System.out.println("Enter group id from 1 to 10!");
        int groupId;
        try {
            groupId = Integer.parseInt(reader.nextLine());
        } catch (NumberFormatException e) {
            log.info("Incorrect number format entered", e);
            System.out.println("In is not a number!");
            return;
        }
        if (groupId > 0 && groupId <= 10) {
            Student student = new Student(groupId, firstName, lastName);
            dataGenerator.getStudents().add(student);
            new StudentDaoImpl().create(student);
        } else {
            log.info("Incorrect group id was entered");
            System.out.println("Group id you entered does not exist!");
        }
    }

    private void delete() {
        log.info("delete method in SchoolUI class launched");
        System.out.println("Enter student's id!");
        int studentId;
        try {
            studentId = Integer.parseInt(reader.nextLine());
        } catch (NumberFormatException e) {
            log.info("Incorrect number format entered", e);
            System.out.println("In is not a number!");
            return;
        }
        if (studentId > 0 && studentId <= 250) {
            Student student = dataGenerator
                .getStudents()
                .stream()
                .filter(currentStudent -> currentStudent.getId() == studentId)
                .findFirst()
                .orElse(new Student());
            new StudentDaoImpl().delete(student);
            dataGenerator.getStudents().remove(student);
        } else {
            log.info("Incorrect student id was entered");
            System.out.println("Student id you entered does not exist!");
        }
    }

    private void addStudentToCourse() {
        log.info("addStudentToCourse method in SchoolUI class launched");
        int studentId;
        System.out.println("Enter student's id!");
        try {
            studentId = Integer.parseInt(reader.nextLine());
        } catch (NumberFormatException e) {
            log.info("Incorrect number format entered", e);
            System.out.println("In is not a number!");
            return;
        }
        if (studentId < 1 || studentId > 250) {
            log.info("Incorrect student id was entered");
            System.out.println("Student id you entered does not exist");
            return;
        }
        Student student = dataGenerator
            .getStudents()
            .stream()
            .filter(st -> (st.getId() == studentId))
            .findFirst()
            .orElse(new Student());
        System.out.println("Enter course id. Courses available -");
        AtomicInteger atomicInteger = new AtomicInteger(1);
        dataGenerator
            .getCourses()
            .forEach(course -> System.out.println(
                "id " + atomicInteger.getAndIncrement() + " - " + course.getName()));
        int courseId;
        try {
            courseId = Integer.parseInt(reader.nextLine());
        } catch (NumberFormatException e) {
            log.info("Incorrect number format entered", e);
            System.out.println("In is not a number!");
            return;
        }
        if (!checkCourseId(courseId)) {
            return;
        }
        Course course = dataGenerator
            .getCourses()
            .stream()
            .filter(currentCourse -> (currentCourse.getId() == courseId))
            .findFirst()
            .orElse(new Course());
        new StudentDaoImpl().addStudentToCourse(student, course);
    }

    private void removeStudentFromCourse() {
        log.info("removeStudentFromCourse method in SchoolUI class launched");
        System.out.println("Enter student's id!");
        int studentId;
        try {
            studentId = Integer.parseInt(reader.nextLine());
        } catch (NumberFormatException e) {
            log.info("Incorrect number format entered", e);
            System.out.println("In is not a number!");
            return;
        }
        if (studentId < 1 || studentId > 250) {
            log.info("Incorrect student id was entered");
            System.out.println("Student id you entered does not exist");
            return;
        }
        Student student = dataGenerator
            .getStudents()
            .stream()
            .filter(currentStudent -> (currentStudent.getId() == studentId))
            .findFirst()
            .orElse(new Student());
        System.out.println("Enter course id. Courses available -");
        AtomicInteger atomicInteger = new AtomicInteger(1);
        dataGenerator
            .getCourses()
            .forEach(course -> System.out.println(
                "id " + atomicInteger.getAndIncrement() + " - " + course.getName()));
        int courseId;
        try {
            courseId = Integer.parseInt(reader.nextLine());
        } catch (NumberFormatException e) {
            log.info("Incorrect number format entered", e);
            System.out.println("In is not a number!");
            return;
        }
        if (!checkCourseId(courseId)) {
            return;
        }
        Course course = dataGenerator
            .getCourses()
            .stream()
            .filter(currentCourse -> (currentCourse.getId() == courseId))
            .findFirst()
            .orElse(new Course());
        new StudentDaoImpl().removeStudentFromCourse(student, course);

    }

    private boolean checkName(String name) {
        char[] nameArray = name.toCharArray();
        if (!Character.isUpperCase(nameArray[0])) {
            System.out.println("First & last name should start with upper case!");
            return false;
        }
        for (char nameLetter : nameArray) {
            if (!Character.isLetter(nameLetter)) {
                System.out.println("First & last name should consist only of letters");
                return false;
            }
        }
        return true;
    }

    private boolean checkCourseId(int courseId) {
        if (courseId < 1 || courseId > 10) {
            log.info("Incorrect course id was entered");
            System.out.println("Course id you entered does not exist!");
            return false;
        }
        return true;
    }
}
