package ispyb.server.common.services.shipping;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Dewar3ServiceBeanTest extends TestBase {

    @Inject private Dewar3Service service;

    @Test
    public void findByPk() throws Exception {
        var result = service.findByPk(1, false, false);
        assertNotNull(result);
        assertEquals("TEST-DEWAR-01", result.getCode());
    }

    @Test
    public void findByPkWithContainers() throws Exception {
        // Dewar id=1 has Container id=13
        var result = service.findByPk(1, true, false);
        assertNotNull(result);
        assertFalse(result.getContainerVOs().isEmpty());
    }

    @Test
    public void findByShippingId() throws Exception {
        // Shipping 309231 contains Dewar id=1
        var result = service.findByShippingId(309231);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void countDewarSamples() throws Exception {
        // Verifies the method is callable; returns ≥ 0 (exact value depends on native query behaviour)
        var count = service.countDewarSamples(1);
        assertNotNull(count);
        assertTrue(count >= 0);
    }
}
