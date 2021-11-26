package com.foxminded.school.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TestConnectionFactory {

    private static TestConnectionFactory instance;
    private Connection connection;
    private static final String PROPERTIES_PATH =
        "src/main/resources/databaseProperties.properties";
    private static final Properties databaseProperties = new Properties();

    static {
        try (FileInputStream inputStream = new FileInputStream(PROPERTIES_PATH)) {
            databaseProperties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TestConnectionFactory() throws SQLException {
        this.connection = DriverManager.getConnection(
            databaseProperties.getProperty("url"),
            databaseProperties.getProperty("username"),
            databaseProperties.getProperty("password"));
    }

    public Connection getTestConnection() {
        return connection;
    }

    public static TestConnectionFactory getInstance() throws SQLException {
        if (instance == null || instance.getTestConnection().isClosed()) {
            instance = new TestConnectionFactory();
        }
        return instance;
    }
}
