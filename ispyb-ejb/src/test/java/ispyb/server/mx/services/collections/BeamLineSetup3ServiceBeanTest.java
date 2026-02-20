package ispyb.server.mx.services.collections;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BeamLineSetup3ServiceBeanTest extends TestBase {

    @Inject private BeamLineSetup3Service service;

    @Test
    public void findByPk() throws Exception {
        var result = service.findByPk(1);
        assertNotNull(result);
    }

    @Test
    public void findByScreeningInputId() throws Exception {
        // No ScreeningInput(1234) exists — service returns null for unknown ID
        var result = service.findByScreeningInputId(1234, false);
        assertNull(result);
    }
}
