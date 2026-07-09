package ispyb.server.mx.services.sample;

import ispyb.TestBase;
import ispyb.common.util.Constants;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BLSample3ServiceBeanTest extends TestBase {

    @Inject BLSample3Service service;

    @Test
    public void findForWSSampleInfoLight() throws Exception {
        var result = service.findForWSSampleInfoLight(20010001, null, "P11", Constants.PROCESSING_STATUS);
        assertNotNull(result);
    }
}