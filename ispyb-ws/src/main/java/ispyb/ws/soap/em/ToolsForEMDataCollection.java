/** This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ****************************************************************************************************/

package ispyb.ws.soap.em;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.em.services.collections.EM3Service;
import ispyb.server.em.vos.CTF;
import ispyb.server.em.vos.MotionCorrection;
import ispyb.server.em.vos.Movie;
import ispyb.server.em.vos.ParticlePicker;
import ispyb.server.em.vos.ParticleClassification;
import ispyb.server.em.vos.ParticleClassificationGroup;
import ispyb.server.mx.services.collections.DataCollection3Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;

import org.apache.log4j.Logger;
	

@WebService(name = "ToolsForEMWebService", serviceName = "ispybWS", targetNamespace = "http://ispyb.ejb3.webservices.em")
@SOAPBinding(style = Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Stateless
@RolesAllowed({ "WebService", "User", "Industrial" })
//@SecurityDomain("ispyb")
//@WebContext(authMethod = "BASIC", secureWSDLAccess = false, transportGuarantee = "NONE")
public class ToolsForEMDataCollection{

	protected Logger log = Logger.getLogger(ToolsForEMDataCollection.class);

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

	/**
	 * 
	 * @param proposal
	 * @param sampleAcronym
	 * @param movieDirectory
	 * @param movieFullPath
	 * @param movieNumber
	 * @param micrographFullPath
	 * @param micrographSnapshotFullPath
	 * @param xmlMetaDataFullPath
	 * @param voltage STORED ON DATACOLLECTION.WAVELENGTH
	 * @param sphericalAberration STORED ON BEAMLINESETUP.CS
	 * @param amplitudeContrast 
	 * @param magnification STORED ON DATACOLLECTION.MAGNIFICATION
	 * @param scannedPixelSize
	 * @param imagesCount
	 * @param dosePerImage
	 * @param positionX
	 * @param positionY
	 * @param beamlineName
	 * @param gridSquareSnapshotFullPath
	 * @return
	 */
	@WebMethod(operationName = "addMovie")
	public Movie addMovie(
			@WebParam(name = "proposal") String proposal,
			@WebParam(name = "proteinAcronym") String proteinAcronym,
			@WebParam(name = "sampleAcronym") String sampleAcronym,
			@WebParam(name = "movieDirectory") String movieDirectory,
			@WebParam(name = "movieFullPath") String movieFullPath,
			@WebParam(name = "movieNumber") String movieNumber,
			@WebParam(name = "micrographFullPath") String micrographFullPath,
			@WebParam(name = "micrographSnapshotFullPath") String micrographSnapshotFullPath,
			@WebParam(name = "xmlMetaDataFullPath") String xmlMetaDataFullPath,
			@WebParam(name = "voltage") String voltage,
			@WebParam(name = "sphericalAberration") String sphericalAberration,
			@WebParam(name = "amplitudeContrast") String amplitudeContrast,
			@WebParam(name = "magnification") String magnification,
			@WebParam(name = "scannedPixelSize") String scannedPixelSize,
			@WebParam(name = "imagesCount") String imagesCount,
			@WebParam(name = "dosePerImage") String dosePerImage,
			@WebParam(name = "positionX") String positionX,
			@WebParam(name = "positionY") String positionY,
			@WebParam(name = "beamlineName") String beamlineName,
			@WebParam(name = "gridSquareSnapshotFullPath") String gridSquareSnapshotFullPath,
			@WebParam(name = "startTime") String stringStartTime
			
			)	
	{
		Date startTime = Calendar.getInstance().getTime();
		try{
			SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss"); 
			startTime = dt.parse(stringStartTime); 
		}
		catch(Exception exp){
			/** No action to be done **/
		}
		try {
			log.info(String.format("addMovie. technique=EM proposal=%s proteinAcronym=%s sampleAcronym=%s movieDirectory=%s moviePath=%s movieNumber=%s micrographPath=%s thumbnailMicrographPath=%s xmlMetaDataPath=%s voltage=%s sphericalAberration=%s magnification=%s scannedPixelSize=%s imagesCount=%s dosePerImage=%s positionX=%s positionY=%s beamLineName=%s startTime=%s gridSquareSnapshotFullPath=%s", proposal, proteinAcronym, sampleAcronym, movieDirectory, movieFullPath, movieNumber, micrographFullPath, micrographSnapshotFullPath, xmlMetaDataFullPath, voltage,sphericalAberration,magnification,scannedPixelSize,imagesCount,dosePerImage,positionX, positionY,beamlineName, startTime, gridSquareSnapshotFullPath));
			EM3Service service = (EM3Service) ejb3ServiceLocator.getLocalService(EM3Service.class);
			return service.addMovie(proposal, proteinAcronym, sampleAcronym, movieDirectory, movieFullPath, movieNumber, micrographFullPath, micrographSnapshotFullPath, xmlMetaDataFullPath, voltage, sphericalAberration, amplitudeContrast, magnification, scannedPixelSize, imagesCount, dosePerImage, positionX, positionY, beamlineName.toUpperCase(),startTime, gridSquareSnapshotFullPath);
		} catch (Exception exp) {
			exp.printStackTrace();
			log.error(String.format("Error addMovie: %s. technique=EM proposal=%s proteinAcronym=%s  sampleAcronym=%s movieDirectory=%s moviePath=%s movieNumber=%s micrographPath=%s thumbnailMicrographPath=%s xmlMetaDataPath=%s voltage=%s sphericalAberration=%s magnification=%s scannedPixelSize=%s imagesCount=%s dosePerImage=%s positionX=%s positionY=%s beamLineName=%s startTime=%s gridSquareSnapshotFullPath=%s cause=%s",exp.getMessage(),  proposal, proteinAcronym, sampleAcronym, movieDirectory, movieFullPath, movieNumber, micrographFullPath, micrographSnapshotFullPath, xmlMetaDataFullPath, voltage,sphericalAberration,magnification,scannedPixelSize,imagesCount,dosePerImage,positionX, positionY,beamlineName, startTime, gridSquareSnapshotFullPath, exp.getCause()));
		}
		return null;
	}
	
	
	@WebMethod(operationName = "addMotionCorrection")
	public MotionCorrection addMotionCorrection(
			@WebParam(name = "proposal") String proposal,
			@WebParam(name = "movieFullPath") String movieFullPath,
			@WebParam(name = "firstFrame") String firstFrame,
			@WebParam(name = "lastFrame") String lastFrame,
			@WebParam(name = "dosePerFrame") String dosePerFrame,
			@WebParam(name = "doseWeight") String doseWeight,
			@WebParam(name = "totalMotion") String totalMotion,
			@WebParam(name = "averageMotionPerFrame") String averageMotionPerFrame,
			@WebParam(name = "driftPlotFullPath") String driftPlotFullPath,
			@WebParam(name = "micrographFullPath") String micrographFullPath,
			@WebParam(name = "micrographSnapshotFullPath") String micrographSnapshotFullPath,
			@WebParam(name = "correctedDoseMicrographFullPath") String correctedDoseMicrographFullPath,
			@WebParam(name = "logFileFullPath") String logFileFullPath
			
			)	
	{
		try {
			log.info(String.format(
					"addMotionCorrection. technique=EM proposal=%s movieFullPath=%s firstFrame=%s lastFrame=%s dosePerFrame=%s doseWeight=%s totalMotion=%s averageMotionPerFrame=%s driftPlotFullPath=%s micrographFullPath=%s micrographSnapshotFullPath=%s correctedDoseMicrographFullPath=%s logFileFullPath=%s",
					proposal, movieFullPath, firstFrame, lastFrame, dosePerFrame, doseWeight, totalMotion, averageMotionPerFrame, driftPlotFullPath,
					micrographFullPath, micrographSnapshotFullPath, correctedDoseMicrographFullPath, logFileFullPath));

			EM3Service service = (EM3Service) ejb3ServiceLocator.getLocalService(EM3Service.class);
			return service.addMotionCorrection(proposal, movieFullPath, firstFrame, lastFrame, dosePerFrame, doseWeight, totalMotion, averageMotionPerFrame, driftPlotFullPath, micrographFullPath,
					micrographSnapshotFullPath, correctedDoseMicrographFullPath, logFileFullPath);
		} catch (Exception exp) {
			exp.printStackTrace();
				log.warn(String.format(
						"addMotionCorrection. technique=EM proposal=%s movieFullPath=%s firstFrame=%s lastFrame=%s dosePerFrame=%s doseWeight=%s totalMotion=%s averageMotionPerFrame=%s driftPlotFullPath=%s micrographFullPath=%s micrographSnapshotFullPath=%s correctedDoseMicrographFullPath=%s logFileFullPath=%s cause=%s",
						proposal, movieFullPath, firstFrame, lastFrame, dosePerFrame, doseWeight, totalMotion, averageMotionPerFrame, driftPlotFullPath,
						micrographFullPath, micrographSnapshotFullPath, correctedDoseMicrographFullPath, logFileFullPath, exp.getCause()));

		}
		return null;
	}
	
	@WebMethod(operationName = "addCTF")
	public CTF addCTF(
			@WebParam(name = "proposal") String proposal,
			@WebParam(name = "movieFullPath") String movieFullPath,
			@WebParam(name = "spectraImageSnapshotFullPath") String spectraImageSnapshotFullPath,
			@WebParam(name = "spectraImageFullPath") String spectraImageFullPath,
			@WebParam(name = "defocusU") String defocusU,
			@WebParam(name = "defocusV") String defocusV,
			@WebParam(name = "angle") String angle,
			@WebParam(name = "crossCorrelationCoefficient") String crossCorrelationCoefficient,
			@WebParam(name = "resolutionLimit") String resolutionLimit,
			@WebParam(name = "estimatedBfactor") String estimatedBfactor,
			@WebParam(name = "logFilePath") String logFilePath	
			)	
	{
		String message = String.format(
				"addCTF. technique=EM proposal=%s movieFullPath=%s spectraImageSnapshotFullPath=%s defocusU=%s defocusV=%s angle=%s crossCorrelationCoefficient=%s resolutionLimit=%s estimatedBfactor=%s logFilePath=%s",
				proposal, movieFullPath, spectraImageSnapshotFullPath, defocusU, defocusV, angle, crossCorrelationCoefficient, resolutionLimit, estimatedBfactor, logFilePath);
		try {
			log.info(message);

			EM3Service service = (EM3Service) ejb3ServiceLocator.getLocalService(EM3Service.class);
			return service.addCTF(proposal, movieFullPath, spectraImageSnapshotFullPath, spectraImageFullPath, defocusU, defocusV, angle, crossCorrelationCoefficient, resolutionLimit, estimatedBfactor, logFilePath);
		} catch (Exception exp) {
			exp.printStackTrace();
			log.error(message, exp);
		}
		return null;
	}
	
	@WebMethod(operationName = "addParticlePicker")
	public ParticlePicker addParticlePicker(
			@WebParam(name = "proposal") String proposal,
			@WebParam(name = "firstMovieFullPath") String firstMovieFullPath,
			@WebParam(name = "lastMovieFullPath") String lastMovieFullPath,
			@WebParam(name = "pickingProgram") String pickingProgram,
			@WebParam(name = "particlePickingTemplate") String particlePickingTemplate,
			@WebParam(name = "particleDiameter") String particleDiameter,
			@WebParam(name = "numberOfParticles") String numberOfParticles,
			@WebParam(name = "fullPathToParticleFile") String fullPathToParticleFile
			)	
	{
		String message = String.format(
				"addParticlePicker. technique=EM proposal=%s firstMovieFullPath=%s lastMovieFullPath=%s pickingProgram=%s particlePickingTemplate=%s particleDiameter=%s numberOfParticles=%s fullPathToParticleFile=%s",
				proposal, firstMovieFullPath, lastMovieFullPath, pickingProgram, particlePickingTemplate, particleDiameter, numberOfParticles, fullPathToParticleFile);
		try {
			log.info(message);

			EM3Service service = (EM3Service) ejb3ServiceLocator.getLocalService(EM3Service.class);
			return service.addParticlePicker(proposal, firstMovieFullPath, lastMovieFullPath, pickingProgram, particlePickingTemplate, particleDiameter, numberOfParticles, fullPathToParticleFile);
		} catch (Exception exp) {
			exp.printStackTrace();
			log.error(message, exp);
		}
		return null;
	}

	@WebMethod(operationName = "addParticleClassificationGroup")
	public ParticleClassificationGroup addParticleClassificationGroup(
			@WebParam(name = "particlePickerId") String particlePickerId,
			@WebParam(name = "type") String type,
			@WebParam(name = "batchNumber") String batchNumber,
			@WebParam(name = "numberOfParticlesPerBatch") String numberOfParticlesPerBatch,
			@WebParam(name = "numberOfClassesPerBatch") String numberOfClassesPerBatch,
			@WebParam(name = "symmetry") String symmetry,
			@WebParam(name = "classificationProgram") String classificationProgram
			)	
	{
		String message = String.format(
				"addParticleClassificationGroup. technique=EM particlePickerId=%s type=%s batchNumber=%s " +
						"numberOfParticlesPerBatch=%s numberOfClassesPerBatch=%s symmetry=%s classificationProgram=%s",
				particlePickerId, type, batchNumber, numberOfParticlesPerBatch, numberOfClassesPerBatch,
				symmetry, classificationProgram);
		try {
			log.info(message);

			EM3Service service = (EM3Service) ejb3ServiceLocator.getLocalService(EM3Service.class);
			return service.addParticleClassificationGroup(particlePickerId, type, batchNumber, numberOfParticlesPerBatch, numberOfClassesPerBatch, 
					 symmetry, classificationProgram);
		} catch (Exception exp) {
			exp.printStackTrace();
			log.info(message, exp);
		}
		return null;
	}
	@WebMethod(operationName = "addParticleClassification")
	public ParticleClassification addParticleClassification(
			@WebParam(name = "particleClassificationGroupId") String particleClassificationGroupId,
			@WebParam(name = "classNumber") String classNumber,
			@WebParam(name = "classImageFullPath") String classImageFullPath,
			@WebParam(name = "particlesPerClass") String particlesPerClass,
			@WebParam(name = "classDistribution") String classDistribution,
			@WebParam(name = "rotationAccuracy") String rotationAccuracy,
			@WebParam(name = "translationAccuracy") String translationAccuracy,
			@WebParam(name = "estimatedResolution") String estimatedResolution,
			@WebParam(name = "overallFourierCompleteness") String overallFourierCompleteness
			)	
	{
		String message = String.format(
				"addParticleClassification. technique=EM particleClassificationGroupId=%s classNumber=%s " +
						"classImageFullPath=%s particlesPerClass=%s classDistribution=%s rotationAccuracy=%s translationAccuracy=%s estimatedResolution=%s " +
						"overallFourierCompleteness=%s",
				particleClassificationGroupId, classNumber, classImageFullPath, particlesPerClass, classDistribution, rotationAccuracy,
				translationAccuracy, estimatedResolution, overallFourierCompleteness);
		try {
			log.info(message);
			EM3Service service = (EM3Service) ejb3ServiceLocator.getLocalService(EM3Service.class);
			return service.addParticleClassification(particleClassificationGroupId, classNumber, classImageFullPath,
					 particlesPerClass, classDistribution, classDistribution=rotationAccuracy, translationAccuracy, estimatedResolution, overallFourierCompleteness);
		} catch (Exception exp) {
			exp.printStackTrace();
			log.error(message, exp);
		}
		return null;
	}

	@WebMethod(operationName = "getDataCollectionsByWorkingFolder")
	public CTF getDataCollectionsByWorkingFolder(
			@WebParam(name = "proposal") String proposal,
			@WebParam(name = "workingFolder") String workingFolder
			)	
	{
		try {
			throw new Exception("Not implemented yet");
		} catch (Exception exp) {
			exp.printStackTrace();
			log.error(String.format("getDataCollectionsByWorkingFolder. technique=EM proposal=%s workingFolder=%s ", proposal, workingFolder) ,exp);
		}
		return null;
	}

	
}