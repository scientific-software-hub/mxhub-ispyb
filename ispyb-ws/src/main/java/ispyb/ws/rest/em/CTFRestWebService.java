package ispyb.ws.rest.em;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.em.services.collections.EM3Service;
import ispyb.server.em.vos.CTF;
import ispyb.server.em.vos.MotionCorrection;
import ispyb.ws.rest.RestWebService;
import ispyb.ws.soap.em.ToolsForEMDataCollection;

import java.io.File;

import jakarta.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import org.apache.cxf.annotations.GZIP;
import org.apache.log4j.Logger;

@Path("/")
@GZIP(threshold = 1024)
public class CTFRestWebService extends RestWebService {

	protected Logger log = Logger.getLogger(ToolsForEMDataCollection.class);

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private EM3Service getEMService() throws NamingException {
		return (EM3Service) ejb3ServiceLocator.getLocalService(EM3Service.class);

	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET

	@Path("{token}/proposal/{proposal}/em/datacollection/{dataCollectionId}/movie/{movieId}/ctf/thumbnail")
	@Produces("image/png")
	public Response getSpectraThumbnailByMovieId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId,
			@PathParam("movieId") int movieId) throws Exception {

		log.info(String.format("getSpectraThumbnailByMovieId. technique=EM proposal=%s dataCollectionId=%d movieId=%d", proposal, dataCollectionId, movieId));
		CTF ctf = getEMService().getCTFByMovieId(this.getProposalId(proposal), dataCollectionId, movieId);
		if (ctf != null){
			if (new File(ctf.getSpectraImageThumbnailFullPath()).exists()){
				return this.sendImage(ctf.getSpectraImageThumbnailFullPath());
			}
			else{
				log.error(String.format("getSpectraThumbnailByMovieId Path %s does not exist. technique=EM ", ctf.getSpectraImageThumbnailFullPath()));
			}
		}
		return null;

	}

}
