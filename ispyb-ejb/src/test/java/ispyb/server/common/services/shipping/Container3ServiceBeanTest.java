package ispyb.server.common.services.shipping;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Container3ServiceBeanTest extends TestBase {

    @Inject
    private Container3Service service;

    @Test
    public void findByPk() throws Exception {
        var result = service.findByPk(13, false);
        assertNotNull(result);
    }

    @Test
    public void testUpdate_setNull() throws Exception {
        var vo = service.findByPk(13, false);
        vo.setSampleChangerLocation(null);
        var result = service.update(vo);
        assertNotNull(result);
        assertNull(result.getSampleChangerLocation());
    }

}
