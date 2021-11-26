package com.foxminded.school.dao.group;

import static org.junit.jupiter.api.Assertions.*;
import static com.foxminded.school.controller.ApplicationMenu.dataGenerator;

import com.foxminded.school.dao.QueryPathSupplier;
import com.foxminded.school.dao.SchoolTablesDao;
import com.foxminded.school.dao.TestDataCreatorDao;
import com.foxminded.school.domain.Group;
import com.foxminded.school.domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

class GroupDaoImplTest {
    Random random = new Random();
    GroupDaoImpl groupDao = new GroupDaoImpl();

    @BeforeEach
    void createTestData() {
        try {
            new SchoolTablesDao(new QueryPathSupplier()).createTables();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TestDataCreatorDao testData = new TestDataCreatorDao();
        testData.createTestData();
    }

    @Test
    void findGroupsByStudentsCount_shouldReturnProperGroupFormat_whenStudentsCountIsMoreThanThirty() {
        List<String> resultList =
            groupDao.findGroupsByStudentsCount(random.nextInt(10) + 30);
        assertFalse(resultList.isEmpty());
        for (String groupName : resultList) {
            assertFalse(groupName.isEmpty());
            int firstLetterValue = groupName.charAt(0);
            int secondLetterValue = groupName.charAt(1);
            int groupNumberValue = Integer.parseInt(groupName.substring(3, 5));
            assertTrue(firstLetterValue >= 65 && firstLetterValue <= 74);
            assertTrue(secondLetterValue >= 65 && secondLetterValue <= 74);
            assertEquals("-", groupName.substring(2, 3));
            assertTrue(groupNumberValue > 10 && groupNumberValue < 100);
        }
    }

    @Test
    void findGroupsByStudentsCount_shouldNotReturnNull_whenStudentsCountIsThirty() {
        assertNotNull(groupDao.findGroupsByStudentsCount(30));
    }

    @Test
    void findGroupsByStudentsCount_shouldReturnProperResult_withRandomCountOfStudents() {
        int studentCount = random.nextInt((20)) + 20;
        List<String> expected = groupDao.findGroupsByStudentsCount(studentCount);
        List<String> actual = new ArrayList<>();
        List<Student> students = dataGenerator.getStudents();
        List<Group> groups = dataGenerator.getGroups();
        int currentCount = 0;
        for (Group currentGroup : groups) {
            for (Student currentStudent : students) {
                if (currentGroup.getId() == (currentStudent.getGroupId())) {
                    currentCount++;
                }
            }
            if (currentCount <= studentCount) {
                actual.add(currentGroup.getName());
            }
            currentCount = 0;
        }
        assertEquals(expected, actual);
    }
}
