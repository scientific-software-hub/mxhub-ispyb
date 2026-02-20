package ispyb.server.mx.services.sample;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Crystal3ServiceBeanTest extends TestBase {

    @Inject private Crystal3Service service;

    @Test
    public void findByProteinId() throws Exception {
        // Crystal id=1 belongs to Protein id=1
        var result = service.findByProteinId(1);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
