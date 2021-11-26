package com.foxminded.school.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Course {

    private int id;
    private String name;
    private String description;
    private static final Logger log = LogManager.getLogger(Course.class);

    public Course() {
        log.trace("Course empty constructor is launched");
    }

    public Course(int id, String name, String description) {
        log.trace("Course constructor is launched");
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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
        Course course = (Course) obj;
        if (course.id != this.id) {
            return false;
        }
        if (!course.name.equals(this.name)) {
            return false;
        }
        return (course.description.equals(this.description));
    }

    @Override
    public int hashCode() {
        return id * 31 + name.hashCode() + description.hashCode();
    }
}
