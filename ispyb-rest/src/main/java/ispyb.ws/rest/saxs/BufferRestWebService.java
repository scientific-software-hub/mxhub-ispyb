package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.vos.proposals.Proposal3VO;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class BufferRestWebService extends SaxsRestWebService {
	
	private final static Logger logger = Logger.getLogger(BufferRestWebService.class);
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/buffer/list")
	@Produces({ "application/json" })
	public Response getBuffers(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal) {

		String methodName = "getBuffers";
		long start = this.logInit(methodName, logger, token, proposal);
		try{
			SaxsProposal3Service saxsProposalService = this.getSaxsProposal3Service();
			Proposal3Service proposalService = this.getProposal3Service();
			List<Proposal3VO> proposals = proposalService.findProposalByLoginName(proposal);
			List<Buffer3VO> buffers = new ArrayList<Buffer3VO>();
			for (Proposal3VO proposal3vo : proposals) {
				buffers.addAll(saxsProposalService.findBuffersByProposalId(proposal3vo.getProposalId()));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(buffers);
		}
		catch(Exception e){
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@POST
	@Path("{token}/proposal/{proposal}/saxs/buffer/save")
	@Produces({ "application/json" })
	public Response saveBuffer(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@FormParam("buffer") String buffer) throws Exception {
		
		String methodName = "saveBuffer";
		long start = this.logInit(methodName, logger, token, proposal, buffer);
		try {
			SaxsProposal3Service saxsProposalService = this.getSaxsProposal3Service();
			Buffer3VO buffer3VO = this.getGson().fromJson(buffer, Buffer3VO.class);
			this.logFinish(methodName, start, logger);
			return this.sendResponse(saxsProposalService.merge(buffer3VO));
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
}
