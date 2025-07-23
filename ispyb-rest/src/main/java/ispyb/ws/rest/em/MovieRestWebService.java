package ispyb.ws.rest.em;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.em.services.collections.EM3Service;
import ispyb.server.em.vos.Movie;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.ws.rest.RestWebService;

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
public class MovieRestWebService extends RestWebService {

	protected Logger log = Logger.getLogger(MovieRestWebService.class);

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private EM3Service getEMService() throws NamingException {
		return (EM3Service) ejb3ServiceLocator.getLocalService(EM3Service.class);

	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/em/datacollection/{dataCollectionId}/dose")
	@Produces("text/plain")
	public Response getDoseByDataCollectionId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId) throws Exception {

		log.info(String.format("getDoseByMovieId. technique=EM proposal=%s dataCollectionId=%d", proposal, dataCollectionId));
		List<String> doses = getEMService().getDoseByDataCollectionId(this.getProposalId(proposal), dataCollectionId);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < doses.size(); i++) {
			sb.append(i).append(",").append(doses.get(i)).append("\n");
		}
		return this.sendResponse(sb.toString());

	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/em/datacollection/{dataCollectionId}/movie")
	@Produces({ "application/json" })
	public Response getMoviesByDataCollectionId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId) throws Exception {

		log.info(String.format("getMoviesByDataCollectionId. technique=EM proposal=%s dataCollectionId=%d", proposal, dataCollectionId));
		return this.sendResponse(getEMService().getMoviesByDataCollectionId(this.getProposalId(proposal), dataCollectionId));

	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/em/datacollection/{dataCollectionId}/movie/all")
	@Produces({ "application/json" })
	public Response getMoviesDataByDataCollectionId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId) throws Exception {

		log.info(String.format("getMoviesDataByDataCollectionId. technique=EM proposal=%s dataCollectionId=%d", proposal, dataCollectionId));
		return this.sendResponse(getEMService().getMoviesDataByDataCollectionId(this.getProposalId(proposal), dataCollectionId), false);

	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/em/datacollection/{dataCollectionId}/movie/{movieId}/thumbnail")
	@Produces("image/png")
	public Response getMovieMicrographThumbnail(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId, @PathParam("movieId") int movieId) throws NamingException, Exception {

		log.info(String.format("getMovieMicrographThumbnail. technique=EM proposal=%s dataCollectionId=%d movieId=%d", proposal, dataCollectionId, movieId));
		Movie movie = getEMService().getMovieByDataCollectionId(this.getProposalId(proposal), dataCollectionId, movieId);
		if (movie != null) {
			if (new File(movie.getThumbnailMicrographPath()).exists()) {
				return this.sendImage(movie.getThumbnailMicrographPath());
			} else {
				log.error(String.format("getMovieMicrographThumbnail Path %s does not exist. technique=EM ", movie.getThumbnailMicrographPath()));
			}
		}
		return null;
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/em/datacollection/{dataCollectionId}/movie/{movieId}/metadata/xml")
	@Produces({ "application/json" })
	public Response getMovieMetadataXML(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId, @PathParam("movieId") int movieId) throws NamingException, Exception {

		log.info(String.format("getMovieMetadataXML. technique=EM proposal=%s dataCollectionId=%d movieId=%d", proposal, dataCollectionId, movieId));
		Movie movie = getEMService().getMovieByDataCollectionId(this.getProposalId(proposal), dataCollectionId, movieId);
		if (movie != null) {
			if (new File(movie.getThumbnailMicrographPath()).exists()) {
				return this.downloadFileAsAttachment(movie.getXmlMetaDataPath());
			} else {
				log.error(String.format("getMovieMetadataXML Path %s does not exist. technique=EM ", movie.getThumbnailMicrographPath()));
			}
		}
		return null;
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/em/datacollection/{dataCollectionId}/movie/{movieId}/mrc")
	@Produces({ "application/json" })
	public Response getMovieMRC(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId, @PathParam("movieId") int movieId) throws NamingException, Exception {

		log.info(String.format("getMovieMetadataXML. technique=EM proposal=%s dataCollectionId=%d movieId=%d", proposal, dataCollectionId, movieId));
		Movie movie = getEMService().getMovieByDataCollectionId(this.getProposalId(proposal), dataCollectionId, movieId);
		if (movie != null) {
			if (new File(movie.getMicrographPath()).exists()) {
				return this.downloadFileAsAttachment(movie.getMicrographPath());
			} else {
				log.error(String.format("getMovieMetadataXML Path %s does not exist. technique=EM ", movie.getMicrographPath()));
			}
		}
		return null;
	}
	

}
