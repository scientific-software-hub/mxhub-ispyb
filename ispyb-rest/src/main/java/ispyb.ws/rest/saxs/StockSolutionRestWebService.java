package ispyb.ws.rest.saxs;

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

import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.dataAcquisition.StockSolution3VO;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.vos.proposals.Proposal3VO;

@Path("/")
public class StockSolutionRestWebService extends SaxsRestWebService {
	
	private final static Logger logger = Logger.getLogger(StockSolutionRestWebService.class);
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposalId}/saxs/stocksolution/list")
	@Produces({ "application/json" })
	public Response getStockSolutions(
			@PathParam("token") String token, 
			@PathParam("proposalId") String login) throws Exception {
	    
		String methodName = "getStockSolutions";
		long start = this.logInit(methodName, logger, token, login);
		try {
        		SaxsProposal3Service saxsProposalService = this.getSaxsProposal3Service();
        		Proposal3Service proposalService = this.getProposal3Service();
        			List<Proposal3VO> proposals = proposalService.findProposalByLoginName(login);
        		List<StockSolution3VO> stockSolution3VO = new ArrayList<StockSolution3VO>();
        		for (Proposal3VO proposal3vo : proposals) {
        		    stockSolution3VO.addAll(saxsProposalService.findStockSolutionsByProposalId(proposal3vo.getProposalId()));
        		}
        		this.logFinish(methodName, start, logger);
        		return this.sendResponse(stockSolution3VO);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@POST
	@Path("{token}/proposal/{proposalId}/saxs/stocksolution/save")
	@Produces({ "application/json" })
	public Response saveStockSolution(
			@PathParam("token") String token, 
			@PathParam("proposalId") String proposalId,
			@FormParam("stocksolution") String stocksolution) throws Exception {
		
		
		String methodName = "saveStockSolution";
		long start = this.logInit(methodName, logger, token, stocksolution);
		try {
			SaxsProposal3Service saxsProposalService = this.getSaxsProposal3Service();
			StockSolution3VO stockSolution3VO = this.getGson().fromJson(stocksolution, StockSolution3VO.class);
			this.logFinish(methodName, start, logger);
			return this.sendResponse(saxsProposalService.merge(stockSolution3VO));
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	
}
