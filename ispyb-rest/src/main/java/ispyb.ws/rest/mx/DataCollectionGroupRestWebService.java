package ispyb.ws.rest.mx;

import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;

import java.io.File;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import org.apache.cxf.annotations.GZIP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
@GZIP(threshold = 1024)
public class DataCollectionGroupRestWebService extends MXRestWebService {


	protected Logger log = LoggerFactory.getLogger(DataCollectionGroupRestWebService.class);
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@POST
	@Path("{token}/proposal/{proposal}/mx/datacollectiongroup/{dataCollectionGroupId}/comments/save")
	@Produces("image/png")
	public Response saveDataCollectionComments(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("dataCollectionGroupId") int dataCollectionGroupId,
			@FormParam("comments") String comments) {
		
		try {
			DataCollectionGroup3VO dataCollectionGroup = this.getDataCollectionGroup3Service().findByPk(dataCollectionGroupId, false, false);
			dataCollectionGroup.setComments(comments);
			this.getDataCollectionGroup3Service().update(dataCollectionGroup);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.sendResponse(true);
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollectiongroup/{dataCollectionGroupId}/xtal/thumbnail")
	@Produces("image/png")
	public Response getXtalThumbnailByDataCollectionGroupId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionGroupId") int dataCollectionGroupId) throws Exception {

		DataCollectionGroup3VO dataCollectionGroup = this.getDataCollectionGroup3Service().findByPk(dataCollectionGroupId, false, false);
		if (dataCollectionGroup != null){
			if (new File(dataCollectionGroup.getXtalSnapshotFullPath()).exists()){
				return this.sendImage(dataCollectionGroup.getXtalSnapshotFullPath());
			}
			else{
				log.error("getXtalThumbnailByDataCollectionGroupId Path {} does not exist. technique=EM ", dataCollectionGroup.getXtalSnapshotFullPath());
			}
		}
		return null;

	}

}
