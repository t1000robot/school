package com.foxminded.school.dao;

import java.nio.file.Path;
import java.nio.file.Paths;

public class QueryPathSupplier {
    private static final String SCHOOL_CREATION_QUERY_PATH =
        "src/main/resources/school_creation_query.sql";
    private static final String DROP_TABLES_QUERY_PATH =
        "src/main/resources/tables_drop_query.sql";
    private Path sqlCreateScriptPath;
    private Path sqlDropScriptPath;

    public QueryPathSupplier() {
        this.sqlCreateScriptPath = Paths.get(SCHOOL_CREATION_QUERY_PATH);
        this.sqlDropScriptPath = Paths.get(DROP_TABLES_QUERY_PATH);
    }

    public Path getSqlCreateScriptPath() {
        return sqlCreateScriptPath;
    }

    public Path getSqlDropScriptPath() {
        return sqlDropScriptPath;
    }
}
