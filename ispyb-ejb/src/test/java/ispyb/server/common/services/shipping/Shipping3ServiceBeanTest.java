package ispyb.server.common.services.shipping;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Shipping3ServiceBeanTest extends TestBase {

    @Inject
    private Shipping3Service service;

    @Test public void findByPk() throws  Exception{
        var result = service.findByPk(309231, true);
        assertNotNull(result);
        var otherResult = service.findByPk(309231, true, true);
        assertNotNull(otherResult);
        var yetAnotherResult = service.findByPk(309231, true, true, true);
        assertNotNull(yetAnotherResult);
        var finalResult = service.findByPk(309231, true, true, true, true);
        assertNotNull(finalResult);
    }

    @Test
    public void findSerialShippingByPk() throws Exception {
        var result = service.findByProposal(8425, true, true, true);
        assertNotNull(result);
    }

    @Test public void countShippingInfo() throws Exception{
        var result = service.countShippingInfo(309231);
        assertNotNull(result);
    }
}