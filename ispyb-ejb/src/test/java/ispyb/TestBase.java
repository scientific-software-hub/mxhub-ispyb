package ispyb;

import jakarta.ejb.embeddable.EJBContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MariaDBContainer;

import java.io.File;
import java.util.Properties;

public class TestBase {

    // Resolve ispyb-database/schema/ relative to the multi-module project root.
    // maven.multiModuleProjectDirectory is set by Maven; fall back to ".." when
    // running tests directly from the ispyb-ejb directory (e.g. from an IDE).
    private static final File SCHEMA_DIR = new File(
            System.getProperty("maven.multiModuleProjectDirectory", ".."),
            "ispyb-database/schema"
    ).getAbsoluteFile();

    // One container shared across all test classes for the entire JVM lifetime.
    private static final MariaDBContainer<?> MARIA_DB = new MariaDBContainer<>("mariadb:11")
            .withDatabaseName("pydb")
            .withUsername("pxadmin")
            .withPassword("pxadmin")
            // Full schema (tables, views, routines) — source of truth in ispyb-database
            .withFileSystemBind(path("pydb_empty.sql"),          "/docker-entrypoint-initdb.d/01-schema.sql",    BindMode.READ_ONLY)
            // Per-test-class fixture data (ordered by FK dependency)
            .withFileSystemBind(path("test-data-proposals.sql"), "/docker-entrypoint-initdb.d/02-proposals.sql", BindMode.READ_ONLY)
            .withFileSystemBind(path("test-data-shipping.sql"),  "/docker-entrypoint-initdb.d/03-shipping.sql",  BindMode.READ_ONLY)
            .withFileSystemBind(path("test-data-proteins.sql"),  "/docker-entrypoint-initdb.d/04-proteins.sql",  BindMode.READ_ONLY)
            .withFileSystemBind(path("test-data-blsample.sql"),  "/docker-entrypoint-initdb.d/05-blsample.sql",  BindMode.READ_ONLY)
            .withFileSystemBind(path("test-data-sessions.sql"),  "/docker-entrypoint-initdb.d/06-sessions.sql",  BindMode.READ_ONLY);

    private static String path(String filename) {
        return new File(SCHEMA_DIR, filename).getAbsolutePath();
    }

    static {
        MARIA_DB.start();
    }

    protected static EJBContainer ejbContainer;

    @BeforeAll
    public static void beforeClass() {
        if (ejbContainer == null) {
            Properties properties = new Properties();
            properties.put("ispyb", "new://Resource?type=DataSource");
            properties.put("ispyb.JdbcDriver", "org.mariadb.jdbc.Driver");
            properties.put("ispyb.JdbcUrl", MARIA_DB.getJdbcUrl());
            properties.put("ispyb.UserName", MARIA_DB.getUsername());
            properties.put("ispyb.Password", MARIA_DB.getPassword());
            ejbContainer = EJBContainer.createEJBContainer(properties);
        }
    }

    @AfterAll
    public static void afterClass() {
        // EJBContainer and MariaDBContainer are shared across all test classes;
        // they will be cleaned up when the JVM exits.
    }

    @BeforeEach
    public void before() throws Exception {
        ejbContainer.getContext().bind("inject", this);
    }

    @AfterEach
    public void after() throws Exception {
        ejbContainer.getContext().unbind("inject");
    }
}
