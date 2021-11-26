package com.school.domain.service;

import com.school.domain.Course;
import com.school.domain.Group;
import com.school.domain.Student;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SchoolDataGenerator {

    private static List<Group> groups;
    private static List<Course> courses;
    private static List<Student> students;
    private static Random random = new Random();
    private static final Logger log = LogManager.getLogger(SchoolDataGenerator.class);

    static {
        log.trace("SchoolDataGenerator static initializer block launched");
        generateGroupsData();
        generateCoursesData();
        generateStudentsData();
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Student> getStudents() {
        return students;
    }

    private static void generateGroupsData() {
        log.info("Generating group data");
        List<Integer> integers = random
            .ints(20, 11, 100)
            .boxed()
            .distinct()
            .limit(10)
            .collect(Collectors.toList());
        List<String> strings = Stream
            .generate(SchoolDataGenerator::getRandomString)
            .distinct()
            .limit(10)
            .collect(Collectors.toList());
        AtomicInteger intIndex = new AtomicInteger(0);
        AtomicInteger groupIndex = new AtomicInteger(1);
        groups = strings
            .stream()
            .map(string -> string + "-" + integers.get(intIndex.getAndIncrement()))
            .map(string -> new Group(groupIndex.getAndIncrement(), string))
            .collect(Collectors.toList());
    }

    private static void generateCoursesData() {
        log.info("Generating course data");
        Map<String, String> coursesNames = new HashMap<>();
        coursesNames.put("Math", "Calculus etc");
        coursesNames.put("History", "Middle ages history");
        coursesNames.put("English", "Grammar & english literature");
        coursesNames.put("Literature", "Modern literature of USA");
        coursesNames.put("Arts", "Renaissance sculpture");
        coursesNames.put("Biology", "Anatomy");
        coursesNames.put("Geography", "Geography of Asia");
        coursesNames.put("Psychology", "Developmental psychology");
        coursesNames.put("Law", "Roman law");
        coursesNames.put("IT", "Java Core");
        AtomicInteger courseIndex = new AtomicInteger(1);
        courses = Stream.of(coursesNames)
            .flatMap(map -> map.entrySet().stream())
            .map(entry -> new Course(
                courseIndex.getAndIncrement(), entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    private static void generateStudentsData() {
        log.info("Generating student data");
        Set<String> studentNames = new HashSet<>();
        while (studentNames.size() < 200) {
            studentNames.add(generateFirstName() + " " + generateLastName());
        }
        AtomicInteger studentIndex = new AtomicInteger(1);
        students = studentNames
            .stream()
            .map(name -> new Student(studentIndex.getAndIncrement(),
                random.nextInt((10)) + 1,
                name.substring(0, name.indexOf(" ")),
                name.substring(name.indexOf(" ") + 1)))
            .collect(Collectors.toList());
    }

    private static String getRandomString() {
        return String.valueOf((char) (random.nextInt(10) + 65)) +
            (char) (random.nextInt(10) + 65);
    }

    private static String generateFirstName() {
        List<String> names = Arrays.asList(
            "Brian", "Vincent", "Casper", "Marvin", "Jack",
            "Nancy", "Matilda", "Rebecca", "Samantha", "Holly",
            "Mason", "Oliver", "Lucas", "Henry", "Ethan",
            "Evelyn", "Mia", "Sophia", "Emma", "Emily");
        return names.get(random.nextInt(20));
    }

    private static String generateLastName() {
        List<String> surnames = Arrays.asList(
            "Smith", "Bukowski", "Carrey", "Anderson", "Bartok",
            "Obama", "Rodriguez", "Salazar", "White", "Hernandez",
            "Bennett", "Biffle", "Butler", "Coleman", "Davis",
            "Evans", "Foster", "Garcia", "Gray", "Hill");
        return surnames.get(random.nextInt(20));
    }
}
