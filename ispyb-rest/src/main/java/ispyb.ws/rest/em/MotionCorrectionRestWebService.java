package ispyb.ws.rest.em;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.em.services.collections.EM3Service;
import ispyb.server.em.vos.MotionCorrection;
import ispyb.ws.rest.RestWebService;

import java.io.File;

import jakarta.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import org.apache.cxf.annotations.GZIP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
@GZIP(threshold = 1024)
public class MotionCorrectionRestWebService extends RestWebService {

	protected Logger log = LoggerFactory.getLogger(MotionCorrectionRestWebService.class);

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private EM3Service getEMService() throws NamingException {
		return (EM3Service) ejb3ServiceLocator.getLocalService(EM3Service.class);

	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/em/datacollection/{dataCollectionId}/movie/{movieId}/motioncorrection/thumbnail")
	@Produces("image/png")
	public Response getMotionCorrectionThumbnailByMovieId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId,
			@PathParam("movieId") int movieId) throws Exception {

		log.info("getMotionCorrectionThumbnailByMovieId. technique=EM proposal={} dataCollectionId={} movieId={}", proposal, dataCollectionId, movieId);
		MotionCorrection motion = getEMService().getMotionCorrectionByMovieId(this.getProposalId(proposal), dataCollectionId, movieId);
		if (motion != null){
			if (new File(motion.getMicrographSnapshotFullPath()).exists()){
				return this.sendImage(motion.getMicrographSnapshotFullPath());
			}
			else{
				log.error("getMotionCorrectionThumbnailByMovieId Path {} does not exist. technique=EM ", motion.getMicrographSnapshotFullPath());
			}
		}
		return null;

	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/em/datacollection/{dataCollectionId}/movie/{movieId}/motioncorrection/drift")
	@Produces("image/png")
	public Response getMotionCorrectionDriftByMovieId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId,
			@PathParam("movieId") int movieId) throws Exception {

		log.info("getMotionCorrectionDriftByMovieId. technique=EM proposal={} dataCollectionId={} movieId={}", proposal, dataCollectionId, movieId);
		MotionCorrection motion = getEMService().getMotionCorrectionByMovieId(this.getProposalId(proposal), dataCollectionId, movieId);
		if (motion != null){
			if (new File(motion.getDriftPlotFullPath()).exists()){
				return this.sendImage(motion.getDriftPlotFullPath());
			}
			else{
				log.error("getMotionCorrectionDriftByMovieId Path {} does not exist. technique=EM ", motion.getDriftPlotFullPath());
			}
		}
		return null;

	}

}
