/*************************************************************************************************
 * This file is part of ISPyB.
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
package ispyb.server.em.services.collections;

import ispyb.common.util.StringUtils;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.em.vos.CTF;
import ispyb.server.em.vos.MotionCorrection;
import ispyb.server.em.vos.Movie;
import ispyb.server.em.vos.ParticlePicker;
import ispyb.server.em.vos.ParticleClassification;
import ispyb.server.em.vos.ParticleClassificationGroup;
import ispyb.server.mx.vos.autoproc.AutoProcProgram3VO;
import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;
import ispyb.server.mx.services.collections.BeamLineSetup3Service;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.collections.DataCollectionGroup3Service;
import ispyb.server.mx.services.sample.BLSample3Service;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.services.ws.rest.WsServiceBean;
import ispyb.server.mx.vos.collections.BeamLineSetup3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB AutoProc3.
 * </p>
 */
@Stateless
public class EM3ServiceBean extends WsServiceBean implements EM3Service, EM3ServiceLocal {

	protected final Logger LOG = Logger.getLogger(EM3ServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@EJB
	private DataCollection3Service dataCollection3Service;

	@EJB
	private Session3Service session3Service;

	@EJB
	private Proposal3Service proposal3Service;
	
	@EJB
	private Protein3Service protein3Service;
	
	@EJB
	private Crystal3Service crystal3Service;

	@EJB
	private DataCollectionGroup3Service dataCollectionGroup3Service;
	
	@EJB
	private BLSample3Service sample3Service;
	
	@EJB
	private BeamLineSetup3Service beamLineSetup3Service;

	@Deprecated(forRemoval = true)
	@Override
	public List<Map<String, Object>> getMoviesDataByDataCollectionId(int proposalId, int dataCollectionId) {
		String byDataCollectionId = "select * from v_em_movie"
				+ " where Movie_dataCollectionId = ?1 and Proposal_proposalId=?2";
		Query query = entityManager.createNativeQuery(byDataCollectionId, Map.class);;
		query.setParameter(1, dataCollectionId);
		query.setParameter(2, proposalId);
		return (List<Map<String, Object>>) query.getResultList();
	}
	
	private Integer getProposalId(String proposal, String beamlineName) throws Exception {
		List<Proposal3VO> proposals = proposal3Service.findProposalByLoginName(proposal);
		LOG.info(String.format("Found %d proposals for loginName = %s. technique=EM beamlineName=%s", proposals.size(), proposal, beamlineName));

		if (proposals.size() == 1) {
			return proposals.get(0).getProposalId();
		}
		return null;
	}
	
	
	private Session3VO getSession(String proposal, String beamlineName) throws Exception {
		Integer proposalId = this.getProposalId(proposal, beamlineName);
		if (proposalId != null) {
			/** Looking for a session on Today **/
			List<Session3VO> sessions = session3Service.findSessionByDateProposalAndBeamline(proposalId, beamlineName, Calendar.getInstance().getTime());

			if (sessions.size() > 0) {
				/** Session found **/
				Session3VO session = sessions.get(0);
				LOG.info(String.format("Existing session found. technique=EM sessionId=%d beamlineName=%s", session.getSessionId(), session.getBeamlineName()));
				return session;
			} else {
				LOG.info(String.format("No sessions found for proposal %s and time %s.", proposal, Calendar.getInstance().getTime()));
				Session3VO session = new Session3VO();
				session.setBeamlineName(beamlineName);
				session.setComments("Session created automatically by ISPyB");
				session.setStartDate(Calendar.getInstance().getTime());
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, 1);
				session.setEndDate(calendar.getTime());
				session.setProposalVO(proposal3Service.findByPk(proposalId));
				
				BeamLineSetup3VO beamlineSetup = new BeamLineSetup3VO();
				try{
				    beamlineSetup = beamLineSetup3Service.create(beamlineSetup);
				}
				catch(Exception exp){
					LOG.info("BeamlineSetup could not persist");
				}
				if (beamlineSetup.getBeamLineSetupId() != null){
					session.setBeamLineSetupVO(beamlineSetup);
				}
				LOG.info(String.format("Creating session  proposal=%s. technique=EM proposal=%s", proposal, proposal));
				session = session3Service.create(session);
				LOG.info(String.format("Session created for  proposal=%s. technique=EM proposal=%s", proposal, proposal));
				return session;
			}
		} 
		else{
			LOG.error(String.format("No proposal found for proposal=%s. technique=EM proposal=%s", proposal, proposal));
			throw new Exception("No proposal found for proposal=" + proposal);
		}

	}

	private DataCollectionGroup3VO getDataCollectionGroupBySessionId(List<DataCollectionGroup3VO> dataCollectionGroup, Session3VO session) throws Exception{
		for (DataCollectionGroup3VO dataCollectionGroup3VO : dataCollectionGroup) {
			LOG.info(String.format("Comparing session of dataCollectionGroup = %d sessionId=%d", dataCollectionGroup3VO.getSessionVO().getSessionId(), session.getSessionId()));
			if (dataCollectionGroup3VO.getSessionVO().getSessionId().equals(session.getSessionId())){
				return dataCollectionGroup3VO;
			}
		}
		return null;
	}
	
	private DataCollectionGroup3VO createDataCollectionGroup(Session3VO session) throws Exception{
		DataCollectionGroup3VO group = new DataCollectionGroup3VO();
		group.setSessionVO(session);
		group.setExperimentType("EM");
		group.setStartTime(Calendar.getInstance().getTime());					
		LOG.info(String.format("Creating dataCollectionGroup. technique=EM sessionId=%d", session.getSessionId()));
		group = dataCollectionGroup3Service.create(group);
		LOG.info(String.format("Created dataCollectionGroup. technique=EM sessionId=%d dataCollectionGroupId=%d", session.getSessionId(),
				group.getDataCollectionGroupId()));
		return group;
	}
	
	private DataCollectionGroup3VO createDataCollectionGroup(Session3VO session, BLSample3VO sample) throws Exception{
		DataCollectionGroup3VO group = this.createDataCollectionGroup(session);
		group.setBlSampleVO(sample);
		return dataCollectionGroup3Service.update(group);
	}
	
	
	private DataCollectionGroup3VO getDataCollectionBySampleAndSession(String sampleAcronym, int proposalId, Session3VO session) throws Exception{
		List<BLSample3VO> samples = sample3Service.findByAcronymAndProposalId(sampleAcronym, proposalId, null);
		LOG.info(String.format("%d samples found for sample acronym = %s proposalId = %d sessionId = %d ", samples.size(), sampleAcronym, proposalId, session.getSessionId()));
		
		/** Get data collection groups for that sample acronym **/
		List<DataCollectionGroup3VO> groups = new ArrayList<DataCollectionGroup3VO>();
		for (BLSample3VO blSample3VO : samples) {
			groups.addAll(dataCollectionGroup3Service.findBySampleId(blSample3VO.getBlSampleId(), false, false));
		}
		LOG.info(String.format("%d data collections found for sample acronym = %s proposalId = %d sessionId = %d ", groups.size(), sampleAcronym, proposalId, session.getSessionId()));
		
		
		/** Look for a DCGroup for that session **/
		for (DataCollectionGroup3VO dataCollectionGroup3VO : groups) {
			if (dataCollectionGroup3VO.getSessionVOId().equals(session.getSessionId())){
				LOG.info(String.format("Data collection group found for sample acronym = %s proposalId = %d sessionId = %d ", sampleAcronym, proposalId, session.getSessionId()));
				return dataCollectionGroup3VO;
			}
		}
		
		return null;
				
	}
	
	
	private DataCollectionGroup3VO getDataCollectionGroup(String sampleAcronym, String proposal, String beamlineName, Session3VO session, String proteinAcronym) throws Exception{
		int proposalId = this.getProposalId(proposal, beamlineName);
		
				
		
		DataCollectionGroup3VO group = this.getDataCollectionBySampleAndSession(sampleAcronym, proposalId, session);
		if (group != null){
			return group;
		}
		LOG.info(String.format("No sample acronym found for sampleAcronym = %s and proposal = %d sessionId = %d ", sampleAcronym, proposal, session.getSessionId()));
		/** Samples are null or 0 **/
		
		List<Protein3VO> proteins = protein3Service.findByAcronymAndProposalId(proposalId, proteinAcronym);
		Protein3VO protein = new Protein3VO();
		if (proteins.size() == 0){
			LOG.warn(String.format("Protein %s does not exist on ISPyB for proposal=%s. ", proteinAcronym, proposal));
			/** It creates the protein **/
			
			protein.setProposalVO(proposal3Service.findByPk(proposalId));
			protein.setAcronym(proteinAcronym);
			protein.setName(proteinAcronym);
			LOG.info(String.format("Creating Protein with proteinAcronym = %s and proposal = %s ", proteinAcronym, proposal));
			protein = protein3Service.create(protein);
			LOG.info(String.format("Created Protein with proteinAcronym = %s and proteinId = %d and proposal = %s ", proteinAcronym, protein.getProteinId(), proposal));

			/** It created the crystal form **/
			Crystal3VO crystal = new Crystal3VO();
			crystal.setProteinVO(protein);
			crystal.setComments("Crystal created automatically from ISPyB for EM proteins");
			crystal.setName(proteinAcronym);
			LOG.info(String.format("Creating Crystal with proteinAcronym = %s and proposal = %s ", proteinAcronym, proposal));
			crystal = crystal3Service.create(crystal);
			LOG.info(String.format("Created Crystal with proteinAcronym = %s and proteinId = %d and proposal = %s ", proteinAcronym, protein.getProteinId(), proposal));
			
			Set<Crystal3VO> crystals = new HashSet<Crystal3VO>();
			crystals.add(crystal);
			protein.setCrystalVOs(crystals);
			LOG.info(String.format("Adding Crystal to Protein with proteinAcronym = %s and proposal = %s ", proteinAcronym, proposal));
			protein = protein3Service.update(protein);
			LOG.info(String.format("Added Crystal to Protein with proteinAcronym = %s and proposal = %s ", proteinAcronym, proposal));
		}
		else{
			protein = proteins.get(0);
		}
		
		
		BLSample3VO sample = new BLSample3VO();
		sample.setName(sampleAcronym);
		LOG.info(String.format("Find cyrstal for proteinId = %d", protein.getProteinId()));
		List<Crystal3VO> crystals = crystal3Service.findByProteinId(protein.getProteinId());
		LOG.info(String.format("Found %d crystals", crystals.size()));
		sample.setCrystalVO(crystals.get(0));
		LOG.info(String.format("Creating Sample with sampleAcronym = %s and proposal = %s ", sampleAcronym, proposal));
		sample = sample3Service.create(sample);
		LOG.info(String.format("Created Sample with sampleAcronym = %s and proposal = %s ", sampleAcronym, proposal));
		return this.createDataCollectionGroup(session, sample);
    }
	
	@Override
	public Movie addMovie(String proposal, String proteinAcronym, String sampleAcronym, String movieDirectory, String moviePath, String movieNumber, String micrographPath,
			String thumbnailMicrographPath, String xmlMetaDataPath, String voltage, String sphericalAberration, String amplitudeContrast, String magnification,
			String scannedPixelSize, String noImages, String dosePerImage, String positionX, String positionY, String beamlineName, Date startTime, String gridSquareSnapshotFullPath)
			throws Exception {
		
		// we need to retrieve the session to update the lastUpdate field used by dataconfidentiality
		Session3VO session = this.getSession(proposal, beamlineName);

		/**
		 * Look for a data collection that represents the gridSquare. We take the latest dataCollection
		 **/
		List<DataCollection3VO> dataCollectionList = dataCollection3Service.findFiltered(movieDirectory, null, null, null, null, null);
		LOG.info(String.format("Found %d dataCollection(s) for movieDirectory %s. technique=EM ", dataCollectionList.size(), movieDirectory));
		DataCollection3VO gridSquare = null;

		/**
		 * As it is sorted startTime DESC we take the first in case of having
		 * several
		 **/
		if (dataCollectionList.size() > 0) {
			/** There are  DataCollections then we are adding a movie to an existing gridSquare **/
			gridSquare = dataCollectionList.get(0);
			LOG.info(String.format("Found dataCollection. technique=EM dataCollectionId=%s", gridSquare.getDataCollectionId()));
			
			//TODO : check if can be removed because should be the session loaded by this.getSession(proposal, beamlineName);
			session = gridSquare.getDataCollectionGroupVO().getSessionVO();
			
		} else {
			
			/** There are not DataCollections then we are not adding a movie to an existing gridSquare **/
		

			/** 
			 *  Data Collection Group with a BLSample represents a GRID 
			 *  Uniqueness of BLSample acronym per grid is assumed 
			 * **/
			DataCollectionGroup3VO group = this.getDataCollectionGroup(sampleAcronym, proposal, beamlineName, session, proteinAcronym);
			
															
			/** Creating data Collection GridSquare**/
			LOG.info(String.format("Creating dataCollection for dataCollectionGroup. technique=EM sessionId=%d dataCollectionGroupId=%d", session.getSessionId(), group.getDataCollectionGroupId()));
			DataCollection3VO dataCollection = new DataCollection3VO();
			dataCollection.setDataCollectionGroupVO(group);
			dataCollection.setImageDirectory(movieDirectory);
			dataCollection.setNumberOfImages(Integer.parseInt(noImages));
			dataCollection.setStartTime(startTime);
			dataCollection.setXtalSnapshotFullPath1(gridSquareSnapshotFullPath);
			try{
				dataCollection.setXbeamPix(Double.valueOf(scannedPixelSize));
			}
			catch(Exception exp){
				LOG.error(String.format("scannedPixelSize %d can not be converted into a double. technique=EM scannedPixelSize=%d", scannedPixelSize, scannedPixelSize));
			}
			
			try{
				dataCollection.setVoltage(Float.valueOf(voltage));
			}
			catch(Exception exp){
				LOG.error(String.format("Voltage %s can not be converted into a double. technique=EM voltage=%s", voltage, voltage));
			}
			
			if (session.getBeamLineSetupVO() != null){
				try{
					session.getBeamLineSetupVO().setCS(Float.valueOf(sphericalAberration));
				}
				catch(Exception exp){
					LOG.info(String.format("Spherical Abberation %s can not be converted into a double. technique=EM voltage=%s", sphericalAberration, voltage));
				}
			}
			
			if (magnification != null){
				try{
					dataCollection.setMagnification(Integer.valueOf(magnification));
				}
				catch(Exception exp){
					LOG.info(String.format("Magnification %s can not be converted into a Integer. technique=EM voltage=%s", magnification, voltage));
				}
			}
						
//			amplitudeContrast
//			scannedPixelSize						
			gridSquare = dataCollection3Service.create(dataCollection);

		}

		/** Adding movie **/
		Movie movie = new Movie();
		LOG.info("DataCollectionId:" + gridSquare.getDataCollectionId());
		movie.setDataCollectionId(gridSquare.getDataCollectionId());
		movie.setMicrographPath(micrographPath);
		movie.setMoviePath(moviePath);
		movie.setThumbnailMicrographPath(thumbnailMicrographPath);
		movie.setPositionX(positionX);
		movie.setPositionY(positionY);
		movie.setMovieNumber(Integer.parseInt(movieNumber));
		movie.setDosePerImage(dosePerImage);
		movie.setXmlMetaDataPath(xmlMetaDataPath);

		LOG.info(String.format("Creating movie. technique=EM moviePath=%s", moviePath));
		movie = entityManager.merge(movie);
		LOG.info(String.format("Created movie. technique=EM moviePath=%s movieId=%d", moviePath, movie.getMovieId()));
		
		this.updateSessionLastUpdate(session);

		return movie;
	}

	public Movie findMovieByMovieFullPath(String movieFullPath) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
		Root<Movie> movie = cq.from(Movie.class);

		// Applying conditions
		cq.select(movie).where(cb.equal(movie.get("movieFullPath"), movieFullPath));
		cq.orderBy(cb.desc(movie.get("movieId"))); // Ordering

		List<Movie> movies = entityManager.createQuery(cq).getResultList();

		if (!movies.isEmpty()) {
			return movies.get(0); // Return the first movie
		} else {
			LOG.error("Found no movies for movieFullPath: " + movieFullPath);
			return null; // Return null if no movies found
		}
	}

	public MotionCorrection findMotionCorrectionByMovieFullPath(String movieFullPath) {
		Movie movie = this.findMovieByMovieFullPath(movieFullPath);
		if (movie != null) {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<MotionCorrection> cq = cb.createQuery(MotionCorrection.class);
			Root<MotionCorrection> motionCorrection = cq.from(MotionCorrection.class);

			// Applying conditions
			cq.select(motionCorrection).where(cb.equal(motionCorrection.get("movieId"), movie.getMovieId()));
			cq.orderBy(cb.desc(motionCorrection.get("motionCorrectionId"))); // Ordering

			List<MotionCorrection> motions = entityManager.createQuery(cq).getResultList();

			if (!motions.isEmpty()) {
				return motions.get(0); // Return the first motion correction
			} else {
				LOG.error("Found no motionCorrection for movieFullPath: " + movieFullPath);
				return null; // Return null if no motion correction found
			}
		}
		LOG.error(String.format("Found no movies for movieFullPath %s. movieFullPath=%s", movieFullPath, movieFullPath));
		return null;
	}

	@Override
	public MotionCorrection addMotionCorrection(String proposal, String movieFullPath, String firstFrame, String lastFrame, String dosePerFrame,
			String doseWeight, String totalMotion, String averageMotionPerFrame, String driftPlotFullPath, String micrographFullPath,
			String micrographSnapshotFullPath, String correctedDoseMicrographFullPath, String logFileFullPath) throws Exception {

		LOG.info(String.format("Looking for movie. proposal=%s movieFullPath=%s", proposal, movieFullPath));
		Movie movie = this.findMovieByMovieFullPath(movieFullPath);
		MotionCorrection motion = null;
		
		if (movie != null) {
			motion = new MotionCorrection();
			motion.setMovieId(movie.getMovieId());
			motion.setFirstFrame(firstFrame);
			motion.setLastFrame(lastFrame);
			motion.setDosePerFrame(dosePerFrame);
			motion.setDoseWeight(doseWeight);
			motion.setTotalMotion(totalMotion);
			motion.setAverageMotionPerFrame(averageMotionPerFrame);
			motion.setDriftPlotFullPath(driftPlotFullPath);
			motion.setMicrographFullPath(micrographFullPath);
			motion.setMicrographSnapshotFullPath(micrographSnapshotFullPath);
			motion.setCorrectedDoseMicrographFullPath(correctedDoseMicrographFullPath);
			motion.setLogFileFullPath(logFileFullPath);
			try {
				LOG.info(String.format("Creating motion Correction. technique=EM movieFullPath=%s", movieFullPath));
				motion = this.entityManager.merge(motion);
				LOG.info(String.format("Created motion Correction. technique=EM movieFullPath=%s", movieFullPath));
				
				// update the session linked to the gridsquare of this movie
				DataCollection3VO gridSquare = dataCollection3Service.findByPk(movie.getDataCollectionId(), false/*withImage*/, false/*withAutoProcIntegration*/);
				this.updateSessionLastUpdate(gridSquare.getDataCollectionGroupVO().getSessionVO());
				
			} catch (Exception exp) {
				throw exp;
			}			
			
		}
		return motion;
	}

	@Override
	public CTF addCTF(String proposal, String movieFullPath, String spectraImageSnapshotFullPath, String spectraImageFullPath, String defocusU, String defocusV, String angle,
			String crossCorrelationCoefficient, String resolutionLimit, String estimatedBfactor, String logFilePath) {

		LOG.info(String.format("Looking for motion correction. proposal=%s movieFullPath=%s", proposal, movieFullPath));
		MotionCorrection motion = this.findMotionCorrectionByMovieFullPath(movieFullPath);
		if (motion != null) {
			CTF ctf = new CTF();
			ctf.setMotionCorrectionId(motion.getMotionCorrectionId());
			ctf.setSpectraImageThumbnailFullPath(spectraImageSnapshotFullPath);
			ctf.setSpectraImageFullPath(spectraImageFullPath);
			ctf.setDefocusU(defocusU);
			ctf.setDefocusV(defocusV);
			ctf.setAngle(angle);
			ctf.setCrossCorrelationCoefficient(crossCorrelationCoefficient);
			ctf.setResolutionLimit(resolutionLimit);
			ctf.setEstimatedBfactor(estimatedBfactor);
			ctf.setLogFilePath(logFilePath);
			try {
				LOG.info(String.format("Creating CTF. technique=EM movieFullPath=%s", movieFullPath));
				ctf = this.entityManager.merge(ctf);
				LOG.info(String.format("Created CTF. technique=EM movieFullPath=%s", movieFullPath));
				return ctf;
			} catch (Exception exp) {
				throw exp;
			}
		}

		return null;
	}
	
	@Override
	public ParticlePicker addParticlePicker(String proposal, String firstMoviePath, String lastMoviePath,
			String pickingProgram, String particlePickingTemplate, String particleDiameter, String numberOfParticles,
			String fullPathToParticleFile) {

		LOG.info(String.format("Looking for first motion correction. proposal=%s movieFullPath=%s", proposal, firstMoviePath));
		MotionCorrection motionFirst = this.findMotionCorrectionByMovieFullPath(firstMoviePath);
		if (motionFirst != null) {
			ParticlePicker particlePicker = new ParticlePicker();
			AutoProcProgram3VO autoProcProgram = new AutoProcProgram3VO();
			autoProcProgram.setProcessingPrograms(pickingProgram);
			autoProcProgram = this.entityManager.merge(autoProcProgram);
			AutoProcProgramAttachment3VO autoProcProgramAttachment = new AutoProcProgramAttachment3VO();
			autoProcProgramAttachment.setAutoProcProgramVO(autoProcProgram);
			autoProcProgramAttachment.setFileName("particleFile");
			autoProcProgramAttachment.setFilePath(fullPathToParticleFile);
			autoProcProgramAttachment.setFileType("Result");
			autoProcProgramAttachment = this.entityManager.merge(autoProcProgramAttachment);
			particlePicker.setProgramId(autoProcProgram.getAutoProcProgramId());
			particlePicker.setFirstMotionCorrectionId(motionFirst.getMotionCorrectionId());
			particlePicker.setParticlePickingTemplate(particlePickingTemplate);
			particlePicker.setParticleDiameter(particleDiameter);
			particlePicker.setNumberOfParticles(numberOfParticles);
			try {
				LOG.info("Creating ParticlePicker. technique=EM");
				particlePicker = this.entityManager.merge(particlePicker);
				LOG.info("Created ParticlePicker. technique=EM");
				return particlePicker;
			} catch (Exception exp) {
				throw exp;
			}
		}

		return null;
	}

	@Override
	public ParticleClassificationGroup addParticleClassificationGroup(String particlePickerId, String type,
			String batchNumber, String numberOfParticlesPerBatch, String numberOfClassesPerBatch, 
			String symmetry, String classificationProgram) {

		ParticleClassificationGroup particleClassificationGroup = new ParticleClassificationGroup();
		AutoProcProgram3VO autoProcProgram = new AutoProcProgram3VO();
		autoProcProgram.setProcessingPrograms(classificationProgram);
		autoProcProgram = this.entityManager.merge(autoProcProgram);
		particleClassificationGroup.setProgramId(autoProcProgram.getAutoProcProgramId());
		particleClassificationGroup.setParticlePickerId(Integer.parseInt(particlePickerId));
		particleClassificationGroup.setType(type);
		particleClassificationGroup.setBatchNumber(batchNumber);
		particleClassificationGroup.setNumberOfParticlesPerBatch(numberOfParticlesPerBatch);
		particleClassificationGroup.setNumberOfClassesPerBatch(numberOfClassesPerBatch);
		particleClassificationGroup.setSymmetry(symmetry);
		try {
			LOG.info("Creating ParticleClassificationGroup technique=EM");
			particleClassificationGroup = this.entityManager.merge(particleClassificationGroup);
			LOG.info("Created ParticleClassificationGroup technique=EM");
			return particleClassificationGroup;
		} catch (Exception exp) {
			throw exp;
		}
	}

	@Override
	public ParticleClassification addParticleClassification(String particleClassificationGroupId, String classNumber, 
			String classImageFullPath, String particlesPerClass, String classDistribution, String rotationAccuracy,
			String translationAccuracy, String estimatedResolution,	String overallFourierCompleteness) {

		ParticleClassification particleClassification = new ParticleClassification();
		particleClassification.setParticleClassificationGroupId(Integer.parseInt(particleClassificationGroupId));
		particleClassification.setClassNumber(classNumber);
		particleClassification.setClassImageFullPath(classImageFullPath);
		particleClassification.setParticlesPerClass(particlesPerClass);
		particleClassification.setClassDistribution(classDistribution);
		particleClassification.setRotationAccuracy(rotationAccuracy);
		particleClassification.setTranslationAccuracy(translationAccuracy);
		particleClassification.setEstimatedResolution(estimatedResolution);
		particleClassification.setOverallFourierCompleteness(overallFourierCompleteness);
		try {
			LOG.info("Creating ParticleClassification. technique=EM");
			particleClassification = this.entityManager.merge(particleClassification);
			LOG.info("Created ParticleClassification. technique=EM");
			return particleClassification;
		} catch (Exception exp) {
			throw exp;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Movie> getMoviesByDataCollectionId(int proposalId, int dataCollectionId) throws Exception{
		List<DataCollection3VO> dataCollections =  dataCollection3Service.findByProposalId(proposalId, dataCollectionId);
		if (dataCollections.size() == 1){
			// Get the CriteriaBuilder from the EntityManager, which is used to create a CriteriaQuery
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();

			// Create a CriteriaQuery object for Movie class
			CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);

			// Declare a range variable, root of the query, corresponding to the Movie class
			Root<Movie> movie = cq.from(Movie.class);

			// Specify the criteria: where the dataCollectionId of the movie equals the provided dataCollectionId
			cq.select(movie)
					.where(cb.equal(movie.get("dataCollectionId"), dataCollectionId))
					.orderBy(cb.desc(movie.get("movieId"))); // Order by movieId in descending order

			// Execute the query and get the list of results
			return entityManager.createQuery(cq).getResultList();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Movie getMovieByDataCollectionId(int proposalId, int dataCollectionId, int movieId) throws Exception{
		List<DataCollection3VO> dataCollections =  dataCollection3Service.findByProposalId(proposalId, dataCollectionId);
		if (dataCollections.size() == 1){
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
			Root<Movie> movie = cq.from(Movie.class);

			// Adding conditions to match both dataCollectionId and movieId
			cq.select(movie)
					.where(cb.and(
							cb.equal(movie.get("dataCollectionId"), dataCollectionId),
							cb.equal(movie.get("movieId"), movieId)
					))
					.orderBy(cb.asc(movie.get("movieId"))); // Sorting by movieId in ascending order

			// Execute the query expecting a single result
			return entityManager.createQuery(cq).setMaxResults(1).getSingleResult();
		}
		return null;
	}
	
	
	@Override
	public List<String> getDoseByDataCollectionId(int proposalId, int dataCollectionId) throws Exception{
		List<Movie> movies = this.getMoviesByDataCollectionId(proposalId, dataCollectionId);
		List<String> doses = new ArrayList<String>();
		for (Movie movie : movies) {
			doses.add(movie.getDosePerImage());
		}
		return doses;
	}

	@Override
	public MotionCorrection getMotionCorrectionByMovieId(int proposalId, int dataCollectionId, int movieId) throws Exception {
		Movie movie = this.getMovieByDataCollectionId(proposalId, dataCollectionId, movieId);
		if (movie != null){
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<MotionCorrection> cq = cb.createQuery(MotionCorrection.class);
			Root<MotionCorrection> motionCorrection = cq.from(MotionCorrection.class);

			// Condition to match movieId
			cq.select(motionCorrection)
					.where(cb.equal(motionCorrection.get("movieId"), movieId))
					.orderBy(cb.desc(motionCorrection.get("motionCorrectionId"))); // Sorting by motionCorrectionId in descending order

			// Execute the query and get the first result
			List<MotionCorrection> results = entityManager.createQuery(cq).setMaxResults(1).getResultList();
			if (!results.isEmpty()) {
				return results.get(0); // Returns the first (and only) result
			}
		}
		return null;
	}

	@Override
	public CTF getCTFByMovieId(int proposalId, int dataCollectionId, int movieId) throws Exception {
		MotionCorrection motion = this.getMotionCorrectionByMovieId(proposalId, dataCollectionId, movieId);
		if (motion != null){
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<CTF> cq = cb.createQuery(CTF.class);
			Root<CTF> ctf = cq.from(CTF.class);

			// Condition to match motionCorrectionId
			cq.select(ctf)
					.where(cb.equal(ctf.get("motionCorrectionId"), motion.getMotionCorrectionId()))
					.orderBy(cb.desc(ctf.get("CTFid"))); // Sorting by CTFid in descending order

			// Execute the query and get the first result
			List<CTF> results = entityManager.createQuery(cq).setMaxResults(1).getResultList();
			if (!results.isEmpty()) {
				return results.get(0); // Returns the first (and only) result
			}

		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getStatsByDataCollectionIds(int proposalId, String dataCollectionIdList) {


		String queryString = ("select dataCollectionId, \n" +
				"(select count(*) from Movie where Movie.dataCollectionId = DataCollection.dataCollectionId) as movieCount,\n" +
				"\n" +
				"(select count(*) from MotionCorrection \n" +
				"inner join Movie on Movie.movieId =  MotionCorrection.movieId \n" +
				"where Movie.dataCollectionId = DataCollection.dataCollectionId) as motionCorrectionCount,\n" +
				"\n" +
				"(select count(*) from CTF \n" +
				"inner join MotionCorrection on MotionCorrection.motionCorrectionId =  CTF.motionCorrectionId \n" +
				"inner join Movie on Movie.movieId =  MotionCorrection.movieId \n" +
				"where Movie.dataCollectionId = DataCollection.dataCollectionId) as ctfCorrectionCount\n" +
				"\n" +
				"\n" +
				"from DataCollection\n" +
				"INNER JOIN DataCollectionGroup on  DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId\n" +
				"INNER JOIN BLSession on  BLSession.sessionId = DataCollectionGroup.sessionId" + " where dataCollectionId in (?1) and BLSession.proposalId=?2");
		LOG.debug(queryString);

		// Execute the native SQL query
		List<Map<String, Object>> results = entityManager.createNativeQuery(queryString, Map.class)
				.setParameter(1, dataCollectionIdList)
				.setParameter(2, proposalId)
				.getResultList();

		return results;

	}

	@Override
	public Collection<? extends Map<String, Object>> getStatsByDataDataCollectionGroupId(Integer dataCollectionGroupId) {
		String queryString = ("select dataCollectionId, \n" +
				"(select count(*) from Movie where Movie.dataCollectionId = DataCollection.dataCollectionId) as movieCount,\n" +
				"\n" +
				"(select count(*) from MotionCorrection \n" +
				"inner join Movie on Movie.movieId =  MotionCorrection.movieId \n" +
				"where Movie.dataCollectionId = DataCollection.dataCollectionId) as motionCorrectionCount,\n" +
				"\n" +
				"(select count(*) from CTF \n" +
				"inner join MotionCorrection on MotionCorrection.motionCorrectionId =  CTF.motionCorrectionId \n" +
				"inner join Movie on Movie.movieId =  MotionCorrection.movieId \n" +
				"where Movie.dataCollectionId = DataCollection.dataCollectionId) as ctfCorrectionCount\n" +
				"\n" +
				"\n" +
				"from DataCollection\n" +
				"INNER JOIN DataCollectionGroup on  DataCollectionGroup.dataCollectionGroupId = DataCollection.dataCollectionGroupId\n" +
				"INNER JOIN BLSession on  BLSession.sessionId = DataCollectionGroup.sessionId" + " where DataCollection.dataCollectionGroupId=?1");

		// Execute the native SQL query
		List<Map<String, Object>> results = entityManager.createNativeQuery(queryString, Map.class)
				.setParameter(1, dataCollectionGroupId)
				.getResultList();

		return results;
	}

	@Deprecated(forRemoval = true)
	@Override
	public List<Map<String, Object>> getStatsBySessionId(int proposalId, int sessionId) {
		String queryString = "select * from v_em_stats where sessionId = ?1 and proposalId=?2";
		Query query = entityManager.createNativeQuery(queryString, Map.class)
				.setParameter(1, sessionId)
				.setParameter(2, proposalId);

		// Execute the query and retrieve the list of results
		List<Map<String, Object>> results = query.getResultList();

		return results;
	}

	@Deprecated(forRemoval = true)
	@Override
	public List<Map<String, Object>> getClassificationBySessionId(int proposalId, int sessionId) {
		// Assume getClassificationBySessionId is a SQL string
//		System.out.println(getClassificationBySessionId); // Optionally log the query

		String queryString = "select * from v_em_classification where sessionId = ?1 and proposalId=?2";
		// Create a native query using the EntityManager
		Query query = entityManager.createNativeQuery(queryString, Tuple.class)
				.setParameter(1, sessionId)
				.setParameter(2, proposalId);

		// Execute the query and retrieve the list of results
		List<Tuple> results = query.getResultList();

		// Transform the Tuple results into a List of Maps
		return results.stream()
				.map(tuple -> tuple.getElements().stream()
						.collect(Collectors.toMap(
								tupleElement -> tupleElement.getAlias(),
								tupleElement -> tuple.get(tupleElement.getAlias()),
								(existing, replacement) -> existing)))
				.collect(Collectors.toList());
	}

	@Override
	public ParticleClassification getClassificationByClassificationId(int proposalId, int particleClassificationId) {
		// Obtain the CriteriaBuilder from the EntityManager
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		// Create a CriteriaQuery object for ParticleClassification
		CriteriaQuery<ParticleClassification> query = cb.createQuery(ParticleClassification.class);

		// Define the root of the query (i.e., ParticleClassification)
		Root<ParticleClassification> root = query.from(ParticleClassification.class);

		// Add a condition (where clause)
		query.select(root).where(cb.equal(root.get("particleClassificationId"), particleClassificationId));

		// Execute the query and get the result list
		List<ParticleClassification> results = entityManager.createQuery(query).getResultList();

		// Return the first element if the result list is not empty, otherwise return null
		return results.isEmpty() ? null : results.get(0);
	}
	
	private void updateSessionLastUpdate(Session3VO vo) throws Exception {
		try {			
			Timestamp currentTimeStamp = StringUtils.getCurrentTimeStamp();
			
			if (vo == null || (vo.getLastUpdate() != null && vo.getLastUpdate().after(currentTimeStamp))) {
				// do nothing
				return;
			}									
			vo.setLastUpdate(currentTimeStamp);
			session3Service.update(vo);						
			LOG.debug("Session updated " + vo.getSessionId());	
			return ;

		} catch (Exception e) {
			LOG.error("ERROR: updateSessionLastUpdate - " + StringUtils.getCurrentDate() + " - " + vo.toWSString());
			throw e;
		}
		
	}


}

