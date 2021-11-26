package com.school.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Student {

    private int id;
    private int groupId;
    private String firstName;
    private String lastName;
    private static final Logger log = LogManager.getLogger(Student.class);

    public Student() {
        log.trace("Student empty constructor is launched");
    }

    public Student(int groupId, String firstName, String lastName) {
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Student(int id, int groupId, String firstName, String lastName) {
        log.trace("Student constructor without student id is launched");
        this.id = id;
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Student student = (Student) obj;
        if (student.id != this.id) {
            return false;
        }
        if (student.groupId != this.groupId) {
            return false;
        }
        if (!student.firstName.equals(this.firstName)) {
            return false;
        }
        return (student.lastName.equals(this.lastName));
    }

    @Override
    public int hashCode() {
        return id * 31 + groupId * 31 + firstName.hashCode() + lastName.hashCode();
    }
}
