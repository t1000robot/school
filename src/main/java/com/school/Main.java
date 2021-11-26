package com.school;

import com.school.controller.ApplicationMenu;
import com.school.dao.QueryPathSupplier;
import com.school.dao.SchoolTablesDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    static {
        try {
            log.info("Creating tables static block launched");
            new SchoolTablesDao(new QueryPathSupplier()).createTables();
        } catch (IOException e) {
            log.warn("Cannot create tables", e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        log.info("School application launched");
        new ApplicationMenu().runConsoleUI();
    }
}
