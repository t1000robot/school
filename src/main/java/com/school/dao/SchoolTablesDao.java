package com.school.dao;

import com.school.dao.exception.InvalidFileDataException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class SchoolTablesDao {

    private static final Logger log = LogManager.getLogger(SchoolTablesDao.class);
    private Path sqlCreateScript;
    private Path sqlDropScript;

    public SchoolTablesDao(QueryPathSupplier filesKeeper) {
        sqlCreateScript = filesKeeper.getSqlCreateScriptPath();
        sqlDropScript = filesKeeper.getSqlDropScriptPath();
    }

    public void createTables() throws IOException {
        log.info("Creating tables for school database");
        if (Files.readAllLines(sqlCreateScript).isEmpty()) {
            throw new InvalidFileDataException("Query script file is empty!");
        }
        if (Files.readAllLines(sqlDropScript).isEmpty()) {
            throw new InvalidFileDataException("Query script file is empty!");
        }
        String createScript = String.join("\n", Files.readAllLines(sqlCreateScript));
        String dropScript = String.join("\n", Files.readAllLines(sqlDropScript));

        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
            log.trace("Open connection");
            try (PreparedStatement dropStatement = connection.prepareStatement(dropScript)) {
                log.trace("Create prepared statement for drop query");
                dropStatement.execute();
                try (PreparedStatement createStatement =
                         connection.prepareStatement(createScript)) {
                    log.trace("Create prepared statement for create query");
                    createStatement.execute();
                } catch (SQLException e) {
                    log.warn("Cannot create tables", e);
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                log.warn("Cannot execute drop query", e);
                e.printStackTrace();
            }
        } catch (SQLException e) {
            log.warn("Cannot open connection", e);
            e.printStackTrace();
        }
    }
}
