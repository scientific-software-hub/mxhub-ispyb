package ispyb.server.common.services.sessions;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Session3ServiceBeanTest extends TestBase {

    @Inject private Session3Service service;

    @Test
    public void findByPk() throws Exception {
        var result = service.findByPk(1, false, false, false);
        assertNotNull(result);
        assertEquals("P11", result.getBeamlineName());
    }

    @Test
    public void findByShippingId() throws Exception {
        // ShippingHasSession links Shipping 309231 to Session 1
        var result = service.findByShippingId(309231);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void hasDataCollectionGroups() throws Exception {
        var count = service.hasDataCollectionGroups(1);
        assertNotNull(count);
        assertEquals(Integer.valueOf(0), count);
    }

    @Test
    public void getNbOfCollects() throws Exception {
        var count = service.getNbOfCollects(1);
        assertEquals(Integer.valueOf(0), count);
    }
}
