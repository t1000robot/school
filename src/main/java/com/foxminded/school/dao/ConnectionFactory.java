package com.foxminded.school.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static ConnectionFactory instance;
    private Connection connection;
    private static final String PROPERTIES_PATH =
        "src/main/resources/databaseProperties.properties";
    private static final Properties databaseProperties = new Properties();
    public static final Logger log = LogManager.getLogger(ConnectionFactory.class);

    static {
        log.info("Database properties loading static block launched");
        try (FileInputStream inputStream = new FileInputStream(PROPERTIES_PATH)) {
            log.trace("Loading properties");
            databaseProperties.load(inputStream);
        } catch (IOException e) {
            log.warn("Cannot load database properties", e);
            e.printStackTrace();
        }
    }

    private ConnectionFactory() throws SQLException {
        log.trace("ConnectionFactory constructor launched");
        this.connection = DriverManager.getConnection(databaseProperties.getProperty("url"),
            databaseProperties.getProperty("username"), databaseProperties.getProperty("password"));
    }

    public Connection getConnection() {
        log.trace("Connection getter launched");
        return connection;
    }

    public static ConnectionFactory getInstance() throws SQLException {
        log.info("Connection factory instance is going to be return");
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new ConnectionFactory();
        }
        return instance;
    }
}
