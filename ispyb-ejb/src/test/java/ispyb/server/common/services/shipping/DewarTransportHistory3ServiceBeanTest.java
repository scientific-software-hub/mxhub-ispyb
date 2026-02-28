package ispyb.server.common.services.shipping;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DewarTransportHistory3ServiceBeanTest extends TestBase {

    @Inject private DewarTransportHistory3Service service;

    @Test
    public void findAll() throws Exception {
        var result = service.findAll(false, false);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void findByDewarId() throws Exception {
        var result = service.findByDewarId(1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
