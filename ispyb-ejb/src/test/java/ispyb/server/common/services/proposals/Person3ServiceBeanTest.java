package ispyb.server.common.services.proposals;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Person3ServiceBeanTest extends TestBase {

    @Inject private Person3Service service;

    @Test
    public void findByPk() {
        var result = service.findByPk(1001);
        assertNotNull(result);
        assertEquals("ispybdev", result.getLogin());
    }

    @Test
    public void findByLogin() throws Exception {
        var result = service.findByLogin("ispybdev");
        assertNotNull(result);
        assertEquals("ispybdev", result.getLogin());
    }

    @Test
    public void findByFamilyAndGivenName() throws Exception {
        var result = service.findByFamilyAndGivenName("Dev", "ISPyB");
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
