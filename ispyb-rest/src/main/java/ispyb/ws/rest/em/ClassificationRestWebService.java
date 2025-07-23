package ispyb.ws.rest.em;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.em.services.collections.EM3Service;
import ispyb.server.em.vos.ParticleClassification;
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
public class ClassificationRestWebService extends RestWebService {

	protected Logger log = LoggerFactory.getLogger(ClassificationRestWebService.class);

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	private EM3Service getEMService() throws NamingException {
		return (EM3Service) ejb3ServiceLocator.getLocalService(EM3Service.class);

	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/em/classification/{classificationId}/thumbnail")
	@Produces("image/png")
	public Response getClassificationThumbnailByClassificationId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("classificationId") int classificationId) throws Exception {

		log.info("getClassificationThumbnailByClassificationId. technique=EM proposal={} classificationId={}", proposal, classificationId);
		ParticleClassification particleClassification = getEMService().getClassificationByClassificationId(this.getProposalId(proposal), classificationId);
		if (particleClassification != null){
			if (new File(particleClassification.getClassImageFullPath()).exists()){
				return this.sendImage(particleClassification.getClassImageFullPath());
			}
			else{
				log.error("getClassificationThumbnailByClassificationId Path {} does not exist. technique=EM ", particleClassification.getClassImageFullPath());
			}
		}
		return null;

	}

}
