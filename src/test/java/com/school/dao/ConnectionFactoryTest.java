package com.school.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;

class ConnectionFactoryTest {

    Connection connection = ConnectionFactory.getInstance().getConnection();

    ConnectionFactoryTest() throws SQLException {
    }

    @Test
    void getInstance_shouldNotReturnNull_inAnyCase() throws SQLException {
        assertNotNull(connection);
    }

    @Test
    void getInstance_shouldReturnValidConnection_inAnyCase() throws SQLException {
        assertTrue(connection.isValid(0));
    }
}
