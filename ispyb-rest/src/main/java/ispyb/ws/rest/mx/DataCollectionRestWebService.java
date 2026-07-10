package ispyb.ws.rest.mx;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.annotation.security.RolesAllowed;
import javax.naming.NamingException;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;

import org.apache.cxf.annotations.GZIP;
import org.apache.log4j.Logger;

import ispyb.common.util.Constants;
import ispyb.common.util.export.DataCollectionReportBuilder;
import ispyb.common.util.export.DataCollectionReportCsvSerializer;
import ispyb.common.util.export.ExiPdfRtfExporter;
import ispyb.common.util.export.dto.DataCollectionReportRow;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.services.ws.rest.datacollectiongroup.DataCollectionSummary;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.Session3VO;

@Path("/")
@GZIP(threshold = 1024)
public class DataCollectionRestWebService extends MXRestWebService {

	private final static Logger logger = Logger.getLogger(DataCollectionRestWebService.class);

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/{dataCollectionIdList}/list")
	@Produces({ "application/json" })
	public Response getDataCollectionById(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionIdList") String dataCollectionIdList) {

		String methodName = "getDataCollectionById";
		long start = this.logInit(methodName, logger, token, proposal, dataCollectionIdList);
		try {
			
			List<Integer> ids = this.parseToInteger(dataCollectionIdList);
			List<DataCollectionSummary> dataCollections = new ArrayList<DataCollectionSummary>();

			for (Integer id : ids) {
				int propId = this.getProposalId(proposal);
				dataCollections.addAll(this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionByDataCollectionId(
						propId, id));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/{dataCollectionId}/wilson")
	@Produces("image/png")
	public Response getWilsonPlot(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId) {

		String methodName = "getWilsonPlot";
		long start = this.logInit(methodName, logger, token, proposal, dataCollectionId);
		try {
			DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false);
			this.logFinish(methodName, start, logger);
			if (dataCollection != null) {
				return this.sendImage(dataCollection.getBestWilsonPlotPath());
			}

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
		return null;
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/{dataCollectionId}/qualityindicatorplot")
	@Produces("image/png")
	public Response getQualityIndicatorsPlot(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId) {
		try {
			DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false);
			if (dataCollection != null) {
				return this.sendImage(dataCollection.getImageQualityIndicatorsPlotPath());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@POST
	@Path("{token}/proposal/{proposal}/mx/datacollection/{dataCollectionId}/comments/save")
	@Produces("image/png")
	public Response saveDataCollectionComments(
			@PathParam("token") String token, 
			@PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId,
			@FormParam("comments") String comments) {
		
		String methodName = "saveDataCollectionComments";
		long id = this.logInit(methodName, logger, token, proposal, dataCollectionId, comments);
		
		try {
			DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false);
			dataCollection.setComments(comments);
			this.getDataCollection3Service().update(dataCollection);

		} catch (Exception e) {
			e.printStackTrace();
			return this.logError(methodName, e, id, logger);
		}
		return this.sendResponse(true);
	}
	
	
	

	@Path("{token}/proposal/{proposal}/mx/xrfscan/xrfscanId/{xrfscanId}/qualityindicatorcsv")
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Produces("text/plain")
	public Response getCSVFile(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId) {

		String methodName = "getQualityIndicatorsCSV";
		long id = this.logInit(methodName, logger, token, proposal, dataCollectionId);
		try {
			DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false);
			if (dataCollection != null) {
				if (dataCollection.getImageQualityIndicatorsCSVPath() != null) {
					/** Converting to csv **/
					if (new File(dataCollection.getImageQualityIndicatorsCSVPath()).exists()) {
						this.logFinish(methodName, id, logger);
						return this.sendResponse(new String(
								Files.readAllBytes(Paths.get(dataCollection.getImageQualityIndicatorsCSVPath()))));
					}
				}

			}

		} catch (Exception e) {
			return this.logError(methodName, e, id, logger);
		}
		return null;

	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/{dataCollectionId}/crystalsnaphot/{id}/get")
	@Produces("image/png")
	public Response getCrystalSnapshot(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("dataCollectionId") int dataCollectionId, @PathParam("id") int id) {

//		String methodName = "getCrystalSnapshot";
//		long start = this.logInit(methodName, logger, token, proposal, dataCollectionId, id);
		try {
			DataCollection3VO dataCollection = this.getDataCollection3Service().findByPk(dataCollectionId, false, false);
//			this.logFinish(methodName, start, logger);
			if (dataCollection != null) {
				if (id == 1) {
					return this.sendImage(dataCollection.getXtalSnapshotFullPath1());
				}
				if (id == 2) {
					return this.sendImage(dataCollection.getXtalSnapshotFullPath2());
				}
				if (id == 3) {
					return this.sendImage(dataCollection.getXtalSnapshotFullPath3());
				}
				if (id == 4) {
					return this.sendImage(dataCollection.getXtalSnapshotFullPath4());
				}
			}

		} catch (Exception e) {
//			return this.logError(methodName, e, start, logger);
			e.printStackTrace();
		}
		return null;
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionIdList}/list")
	@Produces({ "application/json" })
	public Response getViewDataCollectionBySessionId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("sessionIdList") String sessionIdList) {

		String methodName = "getDataCollectionNativelyBySessionId";
		long start = this.logInit(methodName, logger, token, proposal, sessionIdList);
		try {
			List<Integer> ids = this.parseToInteger(sessionIdList);
			List<DataCollectionSummary> dataCollections = new ArrayList<DataCollectionSummary>();

			for (Integer id : ids) {
				dataCollections.addAll(
						this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionBySessionId(
							this.getProposalId(proposal), id));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	/** reports section **/
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionId}/report/pdf")
	@Produces({ "application/pdf" })
	public Response getDataCollectionsReportBySessionIdPDF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") String sessionId, @QueryParam("nbRows") String nbRows) throws NamingException {

		String methodName = "getDataCollectionReportyBySessionIdPdf";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			byte[] byteToExport = this.getPdfRtf(sessionId, proposal, nbRows, false, false).toByteArray();
			this.logFinish(methodName, start, logger);
			Session3VO ses = this.getSession3Service().findByPk(new Integer(sessionId), false, false, false);
			if (ses != null)
				return this.downloadFile(byteToExport, "Report_" + proposal + "_"+ ses.getBeamlineName()+ "_" + ses.getStartDate() + ".pdf");
			else
				return this.downloadFile(byteToExport, "No_session.pdf");

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionId}/report/csv")
	@Produces({ "text/csv" })
	public Response getDataCollectionsReportBySessionIdCSV(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") String sessionId) throws NamingException {

		String methodName = "getDataCollectionReportyBySessionIdCsv";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			Integer id = Integer.parseInt(sessionId);
			// Runs once, eagerly: v_datacollection_summary is an aggregating
			// (GROUP_CONCAT + GROUP BY + ORDER BY) view, so the DB must compute
			// the whole result set before row 1 is available regardless of how
			// the response is produced. What we stream below is everything
			// downstream of this query - row-DTO building and CSV
			// serialization - so the response body is written to the client
			// incrementally instead of being fully buffered in memory first.
			List<DataCollectionSummary> dataCollections = this.getWebServiceDataCollectionGroup3Service()
					.getViewDataCollectionBySessionIdHavingImages(this.getProposalId(proposal), id);

			DataCollectionReportBuilder builder = new DataCollectionReportBuilder();
			Map<String, Integer> spgMap = builder.spaceGroupMap(this.getSpaceGroup3Service());
			DataCollectionReportCsvSerializer serializer = new DataCollectionReportCsvSerializer();

			StreamingOutput streamingOutput = output -> {
				try (Writer writer = new OutputStreamWriter(output, StandardCharsets.UTF_8)) {
					Stream<DataCollectionReportRow> rows = dataCollections.stream()
							.map(row -> builder.buildRowUnchecked(row, spgMap));
					serializer.writeCsv(writer, rows);
				} catch (DataCollectionReportBuilder.RowBuildException e) {
					// Surfaces as a truncated download rather than an error
					// response, since the 200 + headers are already committed
					// by the time this runs - see handoff/plan notes.
					logger.error("Failed to build CSV row for session " + sessionId, e.getCause());
					throw new IOException("Failed to build CSV row for session " + sessionId, e.getCause());
				}
			};

			this.logFinish(methodName, start, logger);
			Session3VO ses = this.getSession3Service().findByPk(id, false, false, false);
			String fileName = ses != null
					? "Report_" + proposal + "_" + ses.getBeamlineName() + "_" + ses.getStartDate() + ".csv"
					: "No_session.csv";
			return this.downloadStream(streamingOutput, fileName, "text/csv");

		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/filterParam/{filterParam}/report/pdf")
	@Produces({ "application/pdf" })
	public Response getDataCollectionsReportByfilterParamPDF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("filterParam") String filterParam) throws NamingException {

		String methodName = "getDataCollectionReportyByfilterParamPdf";
		long start = this.logInit(methodName, logger, token, proposal, filterParam);
		try {
			byte[] byteToExport = this.getPdfRtf(filterParam, proposal, false, false);
			this.logFinish(methodName, start, logger);
			
			if (filterParam != null)
				return this.downloadFile(byteToExport, "Report_" + proposal + "_"+ filterParam + ".pdf");
			else 
				return this.downloadFile(byteToExport, "No_data.pdf");
						
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/filterParam/{filterParam}/report/rtf")
	@Produces({ "application/rtf" })
	public Response getDataCollectionsReportByfilterParamRTF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("filterParam") String filterParam) throws NamingException {

		String methodName = "getDataCollectionReportyByfilterParamPdf";
		long start = this.logInit(methodName, logger, token, proposal, filterParam);
		try {
			byte[] byteToExport = this.getPdfRtf(filterParam, proposal, true, false);
			this.logFinish(methodName, start, logger);
			
			if (filterParam != null)
				return this.downloadFile(byteToExport, "Report_" + proposal + "_"+ filterParam + ".rtf");
			else 
				return this.downloadFile(byteToExport, "No_data.rtf");
						
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionId}/report/rtf")
	@Produces({ "application/rtf" })
	public Response getDataCollectionsReportBySessionIdRTF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") String sessionId, @QueryParam("nbRows") String nbRows) throws NamingException {

		String methodName = "getDataCollectionReportyBySessionIdRtf";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			byte[] byteToExport = this.getPdfRtf(sessionId, proposal, nbRows, true, false).toByteArray();
			this.logFinish(methodName, start, logger);
			Session3VO ses = this.getSession3Service().findByPk(new Integer(sessionId), false, false, false);
			if (ses != null)
				return this.downloadFile(byteToExport, "Report_" + proposal + "_"+ ses.getBeamlineName()+ "_" + ses.getStartDate() + ".rtf");
			else
				return this.downloadFile(byteToExport, "No_session.pdf");
						
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionId}/analysisreport/pdf")
	@Produces({ "application/pdf" })
	public Response getDataCollectionsAnalysisReportBySessionIdPDF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") String sessionId, @QueryParam("nbRows") String nbRows) throws NamingException {

		String methodName = "getDataCollectionAnalysisReportyBySessionIdPdf";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			byte[] byteToExport = this.getPdfRtf(sessionId, proposal, nbRows, false, true).toByteArray();
			this.logFinish(methodName, start, logger);
			Session3VO ses = this.getSession3Service().findByPk(new Integer(sessionId), false, false, false);
			if (ses !=null)
				return this.downloadFile(byteToExport, "AnalysisReport_" + proposal + "_"+ ses.getBeamlineName()+ "_" + ses.getStartDate() + ".pdf");
			else
				return this.downloadFile(byteToExport, "No_session.pdf");
						
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/filterParam/{filterParam}/analysisreport/pdf")
	@Produces({ "application/pdf" })
	public Response getDataCollectionsAnalysisReportByFilterParamPDF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("filterParam") String filterParam)  throws NamingException {

		String methodName = "getDataCollectionAnalysisReportyByFilterParamPdf";
		long start = this.logInit(methodName, logger, token, proposal, filterParam);
		try {
			byte[] byteToExport = this.getPdfRtf(filterParam, proposal, false, true);
			this.logFinish(methodName, start, logger);
			if (filterParam !=null)
				return this.downloadFile(byteToExport, "AnalysisReport_" + proposal + "_"+ filterParam + ".pdf");
			else
				return this.downloadFile(byteToExport, "No_data.pdf");
						
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/filterParam/{filterParam}/analysisreport/rtf")
	@Produces({ "application/rtf" })
	public Response getDataCollectionsAnalysisReportByFilterParamRTF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("filterParam") String filterParam) throws NamingException {

		String methodName = "getDataCollectionReportyByFilterParamRtf";
		long start = this.logInit(methodName, logger, token, proposal, filterParam);
		try {
			byte[] byteToExport = this.getPdfRtf(filterParam, proposal, true, true);
			this.logFinish(methodName, start, logger);

			if (filterParam !=null)
				return this.downloadFile(byteToExport, "AnalysisReport_" + proposal + "_"+ filterParam + ".rtf");
			else
				return this.downloadFile(byteToExport, "No_data.rtf");		
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionId}/analysisreport/rtf")
	@Produces({ "application/rtf" })
	public Response getDataCollectionsAnalysisReportBySessionIdRTF(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") String sessionId, @QueryParam("nbRows") String nbRows) throws NamingException {

		String methodName = "getDataCollectionReportyBySessionIdRtf";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			byte[] byteToExport = this.getPdfRtf(sessionId, proposal, nbRows, true, true).toByteArray();
			this.logFinish(methodName, start, logger);
			Session3VO ses = this.getSession3Service().findByPk(new Integer(sessionId), false, false, false);
			if (ses !=null)
				return this.downloadFile(byteToExport, "AnalysisReport_" + proposal + "_"+ ses.getBeamlineName()+ "_" + ses.getStartDate() + ".rtf");
			else
				return this.downloadFile(byteToExport, "No_session.rtf");		
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	/** end of reports section **/
		
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/workflowstep/{workflowstepId}/list")
	@Produces({ "application/json" })
	public Response getViewDataCollectionByWorkflowStepId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("workflowstepId") String workflowstepId) {

		String methodName = "getViewDataCollectionByWorkflowStepId";
		long start = this.logInit(methodName, logger, token, proposal, workflowstepId);
		try {
			List<Integer> ids = this.parseToInteger(workflowstepId);
			List<DataCollectionSummary> dataCollections = new ArrayList<DataCollectionSummary>();

			for (Integer id : ids) {
				dataCollections.addAll(this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionByWorkflowId(this.getProposalId(proposal), id));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/datacollectiongroupid/{datacollectiongroupids}/list")
	@Produces({ "application/json" })
	public Response getViewDataCollectionByDataCollectionId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("datacollectiongroupids") String datacollectiongroupids) {

		String methodName = "getViewDataCollectionByWorkflowStepId";
		long start = this.logInit(methodName, logger, token, proposal, datacollectiongroupids);
		try {
			List<Integer> ids = this.parseToInteger(datacollectiongroupids);
			List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

			for (Integer id : ids) {
				dataCollections.addAll(this.getWebServiceDataCollection3Service().getDataCollectionByDataCollectionGroupId(this.getProposalId(proposal), id));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/workflow/{workflowIdList}/list")
	@Produces({ "application/json" })
	public Response getDataCollectionsByWorkflowId(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("workflowIdList") String workflowIdList) {

		String methodName = "getDataCollectionsByWorkflowId";
		long start = this.logInit(methodName, logger, token, proposal, workflowIdList);
		try {
			List<Integer> ids = this.parseToInteger(workflowIdList);
			List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

			for (Integer id : ids) {
				dataCollections.addAll(this.getWebServiceDataCollection3Service().getViewDataCollectionsByWorkflowId(
						this.getProposalId(proposal), id));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/protein_acronym/{protein_acronyms}/list")
	@Produces({ "application/json" })
	public Response getViewDataCollectionByProteinAcronym(@PathParam("token") String token, @PathParam("proposal") String proposal,
			@PathParam("protein_acronyms") String proteinAcronyms) {

		String methodName = "getViewDataCollectionByProteinAcronym";
		long start = this.logInit(methodName, logger, token, proposal, proteinAcronyms);
		try {
			List<String> acronyms = this.parseToString(proteinAcronyms);
			List<DataCollectionSummary> dataCollections = new ArrayList<DataCollectionSummary>();

			for (String acronym : acronyms) {
				dataCollections.addAll(this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionByProteinAcronym(
						this.getProposalId(proposal), acronym));
			}
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}

	@RolesAllowed({ "User", "Manager", "Industrial", "Localcontact" })
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/sample/{sampleId}/list")
	@Produces({ "application/json" })
	public Response getViewDataCollectionBySampleId(@PathParam("token") String token, @PathParam("proposal") String proposal,
														  @PathParam("sampleId") Integer sampleId) {

		String methodName = "getViewDataCollectionBySampleId";
		long start = this.logInit(methodName, logger, token, proposal, sampleId);
		try {
			List<DataCollectionSummary> dataCollections = new ArrayList<DataCollectionSummary>();

			dataCollections.addAll(this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionBySampleId(
						this.getProposalId(proposal), sampleId));
			this.logFinish(methodName, start, logger);
			return this.sendResponse(dataCollections, false);
		} catch (Exception e) {
			return this.logError(methodName, e, start, logger);
		}
	}
	
	@RolesAllowed({"User", "Manager", "Industrial", "Localcontact"})
	@GET
	@Path("{token}/proposal/{proposal}/mx/datacollection/session/{sessionId}/report/send/pdf")
	@Produces({ "application/pdf" })
	public void exportReportAndSendAsPdf(@PathParam("token") String token,
			@PathParam("proposal") String proposal,
			@PathParam("sessionId") String sessionId) throws NamingException {

		String methodName = "exportReportAndSendAsPdf";
		long start = this.logInit(methodName, logger, token, proposal, sessionId);
		try {
			
			ByteArrayOutputStream baos = this.getPdfRtf(sessionId, proposal, null, false, false);
			this.logFinish(methodName, start, logger);
			
			if (sessionId != null) {
							
				Session3VO ses = this.getSession3Service().findByPk(new Integer(sessionId), false, false, false);

					Proposal3VO pv = ses.getProposalVO();
					// String mpEmail = personService.findByPk(pv.getPersonVOId(), false).getEmailAddress();
					String mpEmail = pv.getPersonVO().getEmailAddress();
					String from = Constants.getProperty("mail.report.from.mxind");
					String bcc = null;

					SimpleDateFormat simple1 = new SimpleDateFormat("dd/MM/yyyy");
					String date = simple1.format(ses.getStartDate());
					String subject = "MXpress FX " + proposal + " - " + date + " on " + ses.getBeamlineName();

					SimpleDateFormat simple = new SimpleDateFormat("ddMMyyyy");
					date = simple.format(ses.getStartDate());
					String attachName = proposal + "-" + date + "-" + ses.getBeamlineName() + ".pdf";
					String mimeType = "application/pdf";

					String to = Constants.getProperty("mail.report.to.test");
					String cc = Constants.getProperty("mail.report.cc.test");
					String body = Constants.getProperty("mail.report.body.test");

					if (Constants.IS_INDUSTRY_MAILING_IN_PROD()) {
						to = mpEmail;
						cc = Constants.getProperty("mail.report.cc");
						body = Constants.getProperty("mail.report.body");
					}

					if (baos != null) {
//						SendMailUtils.sendMail(from, to, cc, bcc, subject, body, attachName, baos, mimeType, true);
						this.logFinish(methodName, start, logger);						
					}	
			}
						
		} catch (Exception e) {
			this.logError(methodName, e, start, logger);
		}	
		return;
	}
	
	private ByteArrayOutputStream getPdfRtf(String sessionId, String proposal, String nbRows, boolean isRtf, boolean isAnalysis) throws NamingException, Exception {
		
		Integer id = new Integer(sessionId);
		
		// ExiPdfRtfExporter (PDF/RTF export, deprecated) still consumes the raw
		// row shape, so the typed summaries are adapted back to maps here
		// rather than migrating the exporter itself.
		List<Map<String, Object>> dataCollections =
				this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionBySessionIdHavingImages(this.getProposalId(proposal), id)
						.stream().map(DataCollectionSummary::asMap).collect(Collectors.toList());

		List<Map<String, Object>> energyScans = this.getWebServiceEnergyScan3Service().getViewBySessionId(this.getProposalId(proposal), id);
		
		List<Map<String, Object>> xrfSpectrums = this.getWebServiceXFEFluorescenSpectrum3Service().getViewBySessionId(this.getProposalId(proposal), id);
		
		Integer nbRowsMax = dataCollections.size();
				
		if (nbRows != null && !nbRows.isEmpty()) {
			nbRowsMax = new Integer(nbRows);
		}
		 
		ExiPdfRtfExporter pdf = new ExiPdfRtfExporter(this.getProposalId(proposal), proposal, id , dataCollections, energyScans, xrfSpectrums, nbRowsMax);

		ByteArrayOutputStream baosToExport = null;
		
		if (isAnalysis)
			baosToExport = pdf.exportDataCollectionAnalysisReport(isRtf);
		else
			baosToExport = pdf.exportDataCollectionReport(isRtf);
		
		return baosToExport;
	}
	
	private byte [] getPdfRtf(String filterParam, String proposal, boolean isRtf, boolean isAnalysis) throws NamingException, Exception {


		List<DataCollectionSummary> dataCollectionSummaries =
				this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionByProteinAcronym(this.getProposalId(proposal), filterParam);

		if (dataCollectionSummaries == null || dataCollectionSummaries.isEmpty()) {
			dataCollectionSummaries =
				this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionBySampleName(this.getProposalId(proposal), filterParam);
		}

		if (dataCollectionSummaries == null || dataCollectionSummaries.isEmpty()) {
			dataCollectionSummaries =
				this.getWebServiceDataCollectionGroup3Service().getViewDataCollectionByImagePrefix(this.getProposalId(proposal), filterParam);
		}

		// ExiPdfRtfExporter (PDF/RTF export, deprecated) still consumes the
		// raw row shape, so the typed summaries are adapted back to maps
		// here rather than migrating the exporter itself.
		List<Map<String, Object>> dataCollections =
				dataCollectionSummaries.stream().map(DataCollectionSummary::asMap).collect(Collectors.toList());

		List<Map<String, Object>> energyScans = null;

		List<Map<String, Object>> xrfSpectrums = null;

		Integer nbRowsMax = dataCollections.size();

		Integer id = null;

		ExiPdfRtfExporter pdf = new ExiPdfRtfExporter(this.getProposalId(proposal), proposal, id , filterParam, dataCollections, energyScans, xrfSpectrums, nbRowsMax);
		byte [] byteToExport;
		
		if (isAnalysis)
			byteToExport = pdf.exportDataCollectionAnalysisReport(isRtf).toByteArray();
		else
			byteToExport = pdf.exportDataCollectionReport(isRtf).toByteArray();
		
		return byteToExport;
	}
}
