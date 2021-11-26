package com.foxminded.school.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.powermock.api.mockito.PowerMockito.when;

import com.foxminded.school.dao.exception.InvalidFileDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

class SchoolTablesDaoTest {

    private QueryPathSupplier filesKeeperMock;
    private SchoolTablesDao schoolTablesDao;
    private Path sqlCreateScriptPath;
    private Path sqlDropScriptPath;
    private Path emptyFile;
    private Path nonExistingFile;

    @BeforeEach
    void initializeObjects() {
        emptyFile = Paths.get("src/test/resources/test_empty_file.sql");
        nonExistingFile = Paths.get("src/test/resources/test_empty_directory/file");
        filesKeeperMock = Mockito.mock(QueryPathSupplier.class);
        QueryPathSupplier filesKeeper = new QueryPathSupplier();
        sqlCreateScriptPath = filesKeeper.getSqlCreateScriptPath();
        sqlDropScriptPath = filesKeeper.getSqlDropScriptPath();
    }

    @Test
    void createTables_shouldThrowNullPointerException_whenCreationQueryPathIsNull() {
        when(filesKeeperMock.getSqlCreateScriptPath()).thenReturn(null);
        when(filesKeeperMock.getSqlDropScriptPath()).thenReturn(sqlDropScriptPath);
        schoolTablesDao = new SchoolTablesDao(filesKeeperMock);
        assertThrows(NullPointerException.class, () -> schoolTablesDao.createTables());
    }

    @Test
    void createTables_shouldThrowNullPointerException_whenDropQueryPathIsNull() {
        when(filesKeeperMock.getSqlCreateScriptPath()).thenReturn(sqlCreateScriptPath);
        when(filesKeeperMock.getSqlDropScriptPath()).thenReturn(null);
        schoolTablesDao = new SchoolTablesDao(filesKeeperMock);
        assertThrows(NullPointerException.class, () -> schoolTablesDao.createTables());
    }

    @Test
    void createTables_shouldThrowInvalidFileDataException_whenCreateScriptFileIsEmpty() {
        when(filesKeeperMock.getSqlCreateScriptPath()).thenReturn(emptyFile);
        when(filesKeeperMock.getSqlDropScriptPath()).thenReturn(sqlDropScriptPath);
        schoolTablesDao = new SchoolTablesDao(filesKeeperMock);
        assertThrows(InvalidFileDataException.class, () -> schoolTablesDao.createTables());
    }

    @Test
    void createTables_shouldThrowInvalidFileDataException_whenDropScriptFileIsEmpty() {
        when(filesKeeperMock.getSqlCreateScriptPath()).thenReturn(sqlCreateScriptPath);
        when(filesKeeperMock.getSqlDropScriptPath()).thenReturn(emptyFile);
        schoolTablesDao = new SchoolTablesDao(filesKeeperMock);
        assertThrows(InvalidFileDataException.class, () -> schoolTablesDao.createTables());
    }

    @Test
    void createTables_shouldThrowNoSuchFileException_whenCreateScriptFileDoesNotExist() {
        when(filesKeeperMock.getSqlCreateScriptPath()).thenReturn(nonExistingFile);
        when(filesKeeperMock.getSqlDropScriptPath()).thenReturn(sqlDropScriptPath);
        schoolTablesDao = new SchoolTablesDao(filesKeeperMock);
        assertThrows(NoSuchFileException.class, () -> schoolTablesDao.createTables());
    }

    @Test
    void createTables_shouldThrowNoSuchFileException_whenDropScriptFileDoesNotExist() {
        when(filesKeeperMock.getSqlCreateScriptPath()).thenReturn(sqlCreateScriptPath);
        when(filesKeeperMock.getSqlDropScriptPath()).thenReturn(nonExistingFile);
        schoolTablesDao = new SchoolTablesDao(filesKeeperMock);
        assertThrows(NoSuchFileException.class, () -> schoolTablesDao.createTables());
    }
}
