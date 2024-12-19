package ispyb.server.common.services.shipping;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class Container3ServiceBeanTest extends TestBase {

    @Inject
    private Container3Service service;

    @Ignore("Requires db to be up and running")
    @Test
    public void findByPk() throws Exception {
        var result = service.findByPk(13, false);
        assertNotNull(result);
    }

}
