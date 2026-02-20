package ispyb.server.mx.services.ws.rest.energyscan;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnergyScanRestWsServiceTest extends TestBase {

    @Inject private EnergyScanRestWsService service;

    @Test
    public void getViewBySessionId() {
        // EnergyScan id=1 belongs to BLSession id=1 (proposalId=8425)
        var result = service.getViewBySessionId(8425, 1);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void getViewById() {
        // EnergyScan id=1 linked to BLSession with proposalId=8425
        var result = service.getViewById(8425, 1);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
