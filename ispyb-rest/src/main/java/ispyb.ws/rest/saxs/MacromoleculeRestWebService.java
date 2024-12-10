package ispyb.ws.rest.saxs;

import file.FileUploadForm;
import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.assembly.Macromolecule3VO;
import ispyb.server.biosaxs.vos.assembly.Structure3VO;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.apache.log4j.Logger;

@Path("/")
public class MacromoleculeRestWebService extends SaxsRestWebService {
	private final static Logger logger = Logger.getLogger(MacromoleculeRestWebService.class);

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/macromolecule/list")
	@Produces({ "application/json" })
	public Response getMacromoleculeList(@PathParam("token") String token, @PathParam("proposal") String proposal)
			throws Exception {

		String methodName = "getMacromoleculeList";
		long id = this.logInit(methodName, logger, token, proposal);
		try {
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			SaxsProposal3Service saxsProposalService = (SaxsProposal3Service) ejb3ServiceLocator
					.getLocalService(SaxsProposal3Service.class);
			List<Macromolecule3VO> macromolecules = new ArrayList<Macromolecule3VO>();
			macromolecules.addAll(saxsProposalService.findMacromoleculesByProposalId(this.getProposalId(proposal)));
			this.logFinish(methodName, id, logger);
			return sendResponse(macromolecules);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@POST
	@Path("{token}/proposal/{proposal}/saxs/macromolecule/save")
	@Produces({ "application/json" })
	public Response saveMacromolecule(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@FormParam("macromolecule") String macromolecule) throws Exception {

		String methodName = "saveMacromolecule";
		long id = this.logInit(methodName, logger, token, proposal, macromolecule);
		try {
			SaxsProposal3Service saxsProposalService = this.getSaxsProposal3Service();
			Macromolecule3VO macromolecule3VO = saxsProposalService.merge(this.getGson().fromJson(macromolecule, Macromolecule3VO.class));
			this.logFinish(methodName, id, logger);
			return this.sendResponse(macromolecule3VO);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}
	
	
	
	
	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@POST
	@Path("{token}/proposal/{proposal}/saxs/macromolecule/{macromoleculeId}/contactfile/upload")
	@Consumes("multipart/form-data")
	@Produces({ "application/json" })
	public Response uploadContactDescriptionFile(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("macromoleculeId") int macromoleculeId,
			MultipartBody multipartBody) throws IllegalStateException, IOException{

		FileUploadForm form = new FileUploadForm();
		form.setInputStream(multipartBody.getAttachment("file").getDataHandler().getInputStream());
		form.setType(multipartBody.getAttachment("type").getDataHandler().getContent().toString());
		form.setGroupName(multipartBody.getAttachment("groupName").getDataHandler().getContent().toString());


		String methodName = "uploadContactDescriptionFile";
		long id = this.logInit(methodName, logger, token, proposal, form);
		try {
			if (form.getInputStream() != null){
				String filePath = this.copyFileToDisk(proposal, form);
				
				/** If file has been upload then we macromolecule is updated **/
				Macromolecule3VO macromolecule3VO = this.getSaxsProposal3Service().findMacromoleculesById(macromoleculeId);
				if (macromolecule3VO != null){
					macromolecule3VO.setContactsDescriptionFilePath(filePath);
					macromolecule3VO = this.getSaxsProposal3Service().merge(macromolecule3VO);
					logger.info("Added contacts " + macromolecule3VO.getContactsDescriptionFilePath());
				}
				else{
					throw new Exception("Macromolecule with id: " + macromoleculeId + " does not exist");
				}
				this.logFinish(methodName, id, logger);
				return this.sendResponse(macromolecule3VO);
			}
			else{
				throw new Exception("File is empty");
			}
			
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/macromolecule/{macromoleculeId}/contactfile/remove")
	@Produces({ "application/json" })
	public Response removeContactDescriptionFile(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("macromoleculeId") int macromoleculeId) throws IllegalStateException, IOException{
				
		String methodName = "removeContactDescriptionFile";
		long id = this.logInit(methodName, logger, token, proposal);
		try {
			Macromolecule3VO macromolecule3VO = this.getSaxsProposal3Service().findMacromoleculesById(macromoleculeId);
			if (macromolecule3VO != null){
				macromolecule3VO.setContactsDescriptionFilePath(new String());
				macromolecule3VO = this.getSaxsProposal3Service().merge(macromolecule3VO);
			}
			else{
				throw new Exception("Macromolecule with id: " + macromoleculeId + " does not exist");
			}
			this.logFinish(methodName, id, logger);
			return this.sendResponse(macromolecule3VO);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}
	
	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@POST
	@Path("{token}/proposal/{proposal}/saxs/macromolecule/{macromoleculeId}/pdb/upload")
	@Consumes("multipart/form-data")
	@Produces({ "application/json" })
	public Response uploadPDBFile(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("macromoleculeId") int macromoleculeId,
			MultipartBody multipartBody) throws IllegalStateException, IOException{

		FileUploadForm form = new FileUploadForm();
		form.setInputStream(multipartBody.getAttachment("file").getDataHandler().getInputStream());
		form.setType(multipartBody.getAttachment("type").getDataHandler().getContent().toString());
		form.setGroupName(multipartBody.getAttachment("groupName").getDataHandler().getContent().toString());

		String methodName = "uploadPDBFile";
		long id = this.logInit(methodName, logger, token, proposal, form, macromoleculeId);
		try {
			if (form.getInputStream() != null){
				String filePath = this.copyFileToDisk(proposal, form);
				Macromolecule3VO macromolecule3VO = this.getSaxsProposal3Service().findMacromoleculesById(macromoleculeId);
				if (macromolecule3VO != null){
					this.getExperiment3Service().saveStructure(macromoleculeId, new File(filePath).getName(), filePath, "PDB", "P1", "1");
				}
				else{
					throw new Exception("Macromolecule with id: " + macromoleculeId + " does not exist");
				}
				this.logFinish(methodName, id, logger);
				return this.sendResponse(macromolecule3VO);
			}
			else{
				throw new Exception("File is empty");
			}
			
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/macromolecule/{macromoleculeId}/pdb/{structureId}/remove")
	public Response removeStructure(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("macromoleculeId") int macromoleculeId,
			@PathParam("structureId") int structureId) throws IllegalStateException, IOException{
				
		String methodName = "removeStructure";
		long id = this.logInit(methodName, logger, token, proposal, macromoleculeId, structureId);
		try {
			/** TODO: check that structure belongs to macromolecule id **/
			/** TODO: check that macromolecule belongs to proposal **/
			this.getExperiment3Service().removeStructure(structureId);
			this.logFinish(methodName, id, logger);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
		return this.sendResponse("ok");
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@POST
	@Path("{token}/proposal/{proposal}/saxs/macromolecule/{macromoleculeId}/pdb/{structureId}/save")
	public Response saveStructure(
			@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("macromoleculeId") int macromoleculeId,
			@PathParam("structureId") int structureId,
			@FormParam("symmetry") String symmetry,
			@FormParam("multiplicity") String multiplicity) throws IllegalStateException, IOException{
				
		String methodName = "saveStructure";
		long id = this.logInit(methodName, logger, token, proposal, structureId, macromoleculeId, structureId, symmetry, multiplicity);
		try {
			/** TODO: check that structure belongs to macromolecule id **/
			/** TODO: check that macromolecule belongs to proposal **/
			Structure3VO structure = this.getExperiment3Service().findStructureById(structureId);
			structure.setSymmetry(symmetry);
			structure.setMultiplicity(multiplicity);
			this.getExperiment3Service().saveStructure(structure);
			this.logFinish(methodName, id, logger);
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
		return this.sendResponse("ok");
	}
	
}
