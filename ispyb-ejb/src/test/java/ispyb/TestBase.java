package ispyb;

import jakarta.ejb.embeddable.EJBContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.Properties;

public class TestBase {

    protected static EJBContainer ejbContainer;

    @BeforeAll
    public static void beforeClass() {
        Properties properties = new Properties();
//        properties.put("jdbc/ispyb", "new://Resource?type=DataSource");
        properties.put("ispyb", "new://Resource?type=DataSource");
        properties.put("ispyb.JdbcDriver", "org.mariadb.jdbc.Driver");
        properties.put("ispyb.JdbcUrl", "jdbc:mariadb://localhost:3306/pydb");
        properties.put("ispyb.UserName", "pxadmin");
        properties.put("ispyb.Password", "pxadmin");
        ejbContainer = EJBContainer.createEJBContainer(properties);
    }

    @AfterAll
    public static void afterClass() {
        if(ejbContainer != null)
            ejbContainer.close();
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
