package ispyb.server.mx.services.ws.rest.dewar;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DewarRestWsServiceBeanTest extends TestBase {

    @Inject private DewarRestWsService service;

    @Test
    public void getDewarViewBySessionId() {
        // Session id=1 is linked to Shipping 309231 via ShippingHasSession
        var result = service.getDewarViewBySessionId(1, 8425);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void getDewarViewByProposalId() {
        // Proposal 8425 has Shipping 309231 → Dewar 1 → Container 13
        var result = service.getDewarViewByProposalId(8425);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
