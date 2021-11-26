package com.school.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Group {

    private int id;
    private String name;
    private static final Logger log = LogManager.getLogger(Group.class);

    public Group(int id, String name) {
        log.trace("Group constructor is launched");
        this.id = id;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
        Group group = (Group) obj;
        if (group.id != this.id) {
            return false;
        }
        return (group.name.equals(this.name));
    }

    @Override
    public int hashCode() {
        return id * 31 + name.hashCode();
    }
}

