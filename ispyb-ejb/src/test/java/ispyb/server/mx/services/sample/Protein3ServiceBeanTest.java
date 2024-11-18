package ispyb.server.mx.services.sample;

import ispyb.TestBase;
import jakarta.inject.Inject;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore
public class Protein3ServiceBeanTest extends TestBase {

    @Inject private Protein3Service service;

    @Test
    public void findByPk() throws Exception {
        var result = service.findByPk(376497, true);
        assertNotNull(result);
    }
    @Test
    public void findByPkNoCrystals() throws Exception {
        var result = service.findByPk(7, false);
        assertNotNull(result);
    }

    @Test
    public void findAll() throws Exception {
        var result = service.findAll(true);
        assertNotNull(result);
    }

    @Test
    public void findByProposalId() throws Exception {
        var result = service.findByProposalId(8425);
        assertNotNull(result);
    }

    @Test public void getStatsByProposal() {
        var result = service.getStatsByProposal(8425);
        assertNotNull(result);
    }
}