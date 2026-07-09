package ispyb.server.mx.services.sample;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Protein3ServiceBeanTest extends TestBase {

    @Inject private Protein3Service service;

    @Test
    public void findByPk() throws Exception {
        // Protein id=1 has Crystal id=1 (withLink1=true loads crystals)
        var result = service.findByPk(1, true);
        assertNotNull(result);
    }

    @Test
    public void findByPkNoCrystals() throws Exception {
        // Protein id=2 has no crystals
        var result = service.findByPk(2, false);
        assertNotNull(result);
    }

    @Test
    public void findAll() throws Exception {
        var result = service.findAll(true);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void findByProposalId() throws Exception {
        // Protein id=2 belongs to proposal 8425
        var result = service.findByProposalId(8425);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void getStatsByProposal() {
        var result = service.getStatsByProposal(8425);
        assertNotNull(result);
    }
}
