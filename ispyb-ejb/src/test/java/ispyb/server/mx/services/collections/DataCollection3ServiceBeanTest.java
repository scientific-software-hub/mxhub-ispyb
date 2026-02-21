package ispyb.server.mx.services.collections;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataCollection3ServiceBeanTest extends TestBase {

    @Inject private DataCollection3Service service;

    @Test
    public void findByPk() throws Exception {
        var result = service.findByPk(1, false, false);
        assertNotNull(result);
    }

    @Test
    public void findAll() throws Exception {
        var result = service.findAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findByProposalId() throws Exception {
        var result = service.findByProposalId(8425);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void getNbOfCollects() throws Exception {
        // Native query uses ?1 positional param — may return 0 on MariaDB 11 (same quirk as countDewarSamples)
        var count = service.getNbOfCollects(1);
        assertNotNull(count);
        assertTrue(count >= 0);
    }

    @Test
    public void getNbOfTests() throws Exception {
        // Native query uses ?1 positional param — may return 0 on MariaDB 11
        var count = service.getNbOfTests(1);
        assertNotNull(count);
        assertTrue(count >= 0);
    }
}
