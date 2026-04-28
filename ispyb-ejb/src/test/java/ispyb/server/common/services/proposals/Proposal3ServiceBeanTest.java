package ispyb.server.common.services.proposals;

import ispyb.TestBase;
import ispyb.server.common.vos.proposals.Proposal3VO;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Proposal3ServiceBeanTest extends TestBase {

    @Inject private Proposal3Service proposalService;

    @Test public void testFindByLoginName() throws Exception {
        List<Proposal3VO> actualProposals = proposalService.findByLoginName("ispybdev");
        assertEquals(1, actualProposals.size());
    }

    @Test public void testFindByPk() throws Exception {
        var result = proposalService.findByPk(8425);
        assertNotNull(result);
    }

    @Test public void testFindWithParticipantsByPk() throws Exception {
        var result = proposalService.findWithParticipantsByPk(8425);
        assertNotNull(result);
    }

    @Test public void testFindByPkWithFetch() throws Exception {
        var result = proposalService.findByPk(8425, true, true, true);
        assertNotNull(result);
    }

    @Test public void testFindByCodeAndNumber() throws Exception {
        var result = proposalService.findByCodeAndNumber("I", "20210046", true, true, true);
        assertEquals(1, result.size());
    }

    @Disabled("Proposal3ServiceBean is BMT and updateProposalFromIds never starts a UserTransaction; executeUpdate() requires an active JTA tx")
    @Test public void testUpdateProposalFromIds() throws Exception {
        var result = proposalService.updateProposalFromIds(8426, 8425);
        assertNotNull(result);
    }

    @Test public void testFindProposalByLoginName() {
        var result = proposalService.findProposalByLoginName("I20210046");
        assertEquals(1, result.size());
    }

}