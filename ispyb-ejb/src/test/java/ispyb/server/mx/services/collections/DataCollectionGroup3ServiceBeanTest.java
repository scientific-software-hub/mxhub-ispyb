package ispyb.server.mx.services.collections;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataCollectionGroup3ServiceBeanTest extends TestBase {

    @Inject private DataCollectionGroup3Service service;

    @Test
    public void findByPk() throws Exception {
        var result = service.findByPk(1, false, false);
        assertNotNull(result);
    }

    @Test
    public void findByPkWithDataCollections() throws Exception {
        var result = service.findByPk(1, true, false);
        assertNotNull(result);
        assertFalse(result.getDataCollectionVOs().isEmpty());
    }

    @Test
    public void findAll() throws Exception {
        var result = service.findAll(false);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findFiltered() throws Exception {
        var result = service.findFiltered(1, false, false);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findBySampleId() throws Exception {
        var result = service.findBySampleId(1, false, false);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
