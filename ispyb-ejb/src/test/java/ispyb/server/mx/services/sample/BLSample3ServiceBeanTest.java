package ispyb.server.mx.services.sample;

import ispyb.TestBase;
import ispyb.common.util.Constants;
import jakarta.inject.Inject;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class BLSample3ServiceBeanTest extends TestBase {

    @Inject BLSample3Service service;

@Ignore
    @Test
    public void findForWSSampleInfoLight() throws Exception {
        var result = service.findForWSSampleInfoLight(20010001, null, "P11", Constants.PROCESSING_STATUS);
        assertNotNull(result);
    }
}