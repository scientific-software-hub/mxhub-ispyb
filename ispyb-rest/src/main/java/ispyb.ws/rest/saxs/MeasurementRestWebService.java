package ispyb.ws.rest.saxs;

import ispyb.server.biosaxs.services.ExperimentSerializer;
import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Specimen3VO;
import ispyb.server.biosaxs.vos.datacollection.MeasurementTodataCollection3VO;
import ispyb.server.biosaxs.vos.utils.comparator.SaxsDataCollectionComparator;

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
public class MeasurementRestWebService extends SaxsRestWebService {
	private final static Logger logger = Logger.getLogger(MeasurementRestWebService.class);

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposal}/saxs/measurement/{measurementId}/remove")
	@Produces()
	public Response removeMeasurement(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("measurementId") int measurementId) {

		String methodName = "removeMeasurement";
		long id = this.logInit(methodName, logger, token, proposal, measurementId);
		try {
			Experiment3VO experiment = getExperiment3Service().findByMeasurementId(measurementId);
			Measurement3VO measurement3VO = experiment.getMeasurementById(measurementId);
			List<MeasurementTodataCollection3VO> measurementsDC = getMeasurementToDataCollectionService()
					.findByMeasurementId(measurement3VO.getMeasurementId());

			/** Getting specimens involved in this datacollection **/
			List<Specimen3VO> specimens = new ArrayList<Specimen3VO>();
			for (MeasurementTodataCollection3VO measurementTodataCollection3VO : measurementsDC) {
				specimens = getWebUserInterfaceService().getSpecimenByDataCollectionId(
						measurementTodataCollection3VO.getDataCollectionId());
			}
			/** Removing data collection, MeasurementtoDc and Measurement **/
			getWebUserInterfaceService().removeDataCollectionByMeasurement(measurement3VO);

			/** Removing specimen if possible measurementCount == 0 **/
			for (Specimen3VO specimen3vo : specimens) {
				getWebUserInterfaceService().remove(specimen3vo);
			}
			this.logFinish("removeMeasurement", id, logger);
			return this.sendResponse("Ok");
			
		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@POST
	@Path("{token}/proposal/{proposal}/saxs/measurement/save")
	@Produces({ "application/json" })
	public Response saveMeasurement(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@FormParam("measurement") String measurement)  {

		String methodName = "saveMeasurement";
		long start = this.logInit(methodName, logger, token, proposal, measurement);
		try {
			Measurement3VO measurement3VO = newGson().fromJson(measurement, Measurement3VO.class);
			measurement3VO = getWebUserInterfaceService().merge(measurement3VO);
			this.logFinish("saveMeasurement", start, logger);
			return this.sendResponse(measurement3VO);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "LocalContact"})
	@GET
	@Path("{token}/proposal/{proposalId}/saxs/measurement/experiment/{experimentId}/type/{type}/sort")
	@Produces({ "application/json" })
	public Response sortMeasurements(@PathParam("token") String token, @PathParam("proposalId") String proposal,
			@PathParam("experimentId") String experimentId, @PathParam("type") String type) throws Exception {

		String methodName = "sortMeasurements";
		long start = this.logInit(methodName, logger, token, proposal, experimentId, type);
		try {
			Integer proposalId = this.getProposalId(proposal);

			Experiment3VO experiment = null;
			if (type.equals("FIFO")) {
				SaxsDataCollectionComparator[] options = { SaxsDataCollectionComparator.BY_MEASUREMENT_ID };
				experiment = getExperiment3Service().setPriorities(Integer.parseInt(experimentId), proposalId, options);
			} else {
				SaxsDataCollectionComparator[] options = SaxsDataCollectionComparator.defaultComparator;
				experiment = getExperiment3Service().setPriorities(Integer.parseInt(experimentId), proposalId, options);
			}

			String json = ExperimentSerializer.serializeExperiment(experiment, ExperimentScope.MEDIUM);
			this.logFinish("sortMeasurements", start, logger);
			return this.sendResponse(json);

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}

	}

}
