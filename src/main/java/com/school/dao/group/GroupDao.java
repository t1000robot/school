package com.school.dao.group;

import java.util.List;

public interface GroupDao {
    List<String> findGroupsByStudentsCount(int studentsCount);
}
