package com.foxminded.school.dao.group;

import com.foxminded.school.dao.ConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupDaoImpl implements GroupDao {

    private static final String FIND_GROUPS_BY_STUDENTS_COUNT_SQL =
        "SELECT g.group_name FROM groups AS g WHERE " +
            "(select count(*) FROM students AS st WHERE g.group_id = st.group_id) <= (?);";
    private static final Logger log = LogManager.getLogger(GroupDaoImpl.class);

    @Override
    public List<String> findGroupsByStudentsCount(int studentsCount) {
        log.info("Finding groups by student count = {}", studentsCount);
        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
            log.trace("Open connection");
            try (PreparedStatement statement =
                     connection.prepareStatement(FIND_GROUPS_BY_STUDENTS_COUNT_SQL)) {
                log.trace("Create prepared statement for finding groups");
                statement.setInt(1, studentsCount);
                statement.execute();
                try {
                    log.trace("Getting result set");
                    ResultSet resultSet = statement.getResultSet();
                    List<String> result = new ArrayList<>();
                    while (resultSet.next()) {
                        result.add(resultSet.getString("group_name"));
                    }
                    return result;
                } catch (SQLException e) {
                    log.warn("Cannot get result set", e);
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                log.warn("Cannot execute prepared statement", e);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            log.warn("Cannot open connection", e);
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
