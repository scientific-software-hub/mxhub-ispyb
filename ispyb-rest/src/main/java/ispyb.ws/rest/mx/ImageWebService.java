package ispyb.ws.rest.mx;

import ispyb.server.mx.vos.collections.Image3VO;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/")
public class ImageWebService extends MXRestWebService {

	private final static Logger logger = Logger
			.getLogger(ImageWebService.class);

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/image/{imageId}/get")
	@Produces({ "application/json" })
	public Response getImageById(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("imageId") int imageId) {

		String methodName = "getImageById";
		long start = this.logInit(methodName, logger, token, proposal, imageId);
		try {
			Image3VO image = this.getImage3Service().findByPk(imageId);
			this.logFinish(methodName, start, logger);
			return this.sendImage(image.getJpegFileFullPath());
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/image/{imageId}/thumbnail")
	@Produces({ "application/json" })
	public Response getThumbNailImageById(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("imageId") int imageId) {

		try {
			Image3VO image = this.getImage3Service().findByPk(imageId);
			return this.sendImage(image.getJpegThumbnailFileFullPath());
		} catch (Exception e) {
			return null;
		}
	}

}
