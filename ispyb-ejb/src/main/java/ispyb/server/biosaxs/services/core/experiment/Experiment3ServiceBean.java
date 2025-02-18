/*******************************************************************************
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
 ******************************************************************************************************************************/

package ispyb.server.biosaxs.services.core.experiment;


import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import jakarta.persistence.*;

import org.apache.log4j.Logger;

import ispyb.server.biosaxs.services.core.ExperimentScope;
import ispyb.server.biosaxs.services.core.plateType.PlateType3Service;
import ispyb.server.biosaxs.services.core.proposal.SaxsProposal3Service;
import ispyb.server.biosaxs.vos.assembly.Stoichiometry3VO;
import ispyb.server.biosaxs.vos.assembly.Structure3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Buffer3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Experiment3VO;
import ispyb.server.biosaxs.vos.dataAcquisition.Measurement3VO;
import ispyb.server.biosaxs.vos.datacollection.MeasurementTodataCollection3VO;
import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;
import ispyb.server.biosaxs.vos.utils.comparator.SaxsDataCollectionComparator;
import ispyb.server.biosaxs.vos.utils.parser.RobotXMLParser;
import ispyb.server.mx.services.ws.rest.WsServiceBean;



@Stateless
public class Experiment3ServiceBean  extends WsServiceBean implements Experiment3Service, Experiment3ServiceLocal {

	private final static Logger log = Logger.getLogger(Experiment3ServiceBean.class);


	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@EJB
	private SaxsProposal3Service saxsProposal3Service;
	
	@EJB
	private PlateType3Service plateType3Service;
	
	@Override
	public void persist(Experiment3VO transientInstance) {
		try {
			entityManager.persist(transientInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public Experiment3VO merge(Experiment3VO detachedInstance) {
		try {
			Experiment3VO result = entityManager.merge(detachedInstance);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}


	@Override
	public List<Experiment3VO> findByProposalId(int proposalId, ExperimentScope scope) {
		String ejbQLQuery = getQueryByScope(scope)
				+ "WHERE experiment.proposalId = :proposalId";
		TypedQuery<Experiment3VO> query = entityManager.createQuery(ejbQLQuery, Experiment3VO.class)
				.setParameter("proposalId", proposalId);
		return query.getResultList();
	}

	@Override
	public Experiment3VO findById(Integer experimentId, ExperimentScope scope, Integer proposalId) {
		try {
			String ejbQLQuery = getQueryByScope(scope) +
					"WHERE experiment.experimentId = :experimentId ";
			// if coming from manager account, proposalId can be null
			if (proposalId != null)  {
				ejbQLQuery += " AND experiment.proposalId = :proposalId";
			}
			TypedQuery<Experiment3VO> query = entityManager.createQuery(ejbQLQuery, Experiment3VO.class)
					.setParameter("experimentId", experimentId);
			if (proposalId != null) {
				query.setParameter("proposalId", proposalId);
			}
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Experiment3VO initPlates(Experiment3VO vo){
		try {
			vo.setPlatetype3VOs(this.plateType3Service.findAll());
			} catch (Exception e) {
				e.printStackTrace();
			}
		return vo;
	}

	//TODO this method produces invalid query that fails with "Unknown column 't7.id' in 'field list'"
	private String getQueryByScope(ExperimentScope scope){
		StringBuilder ejbQLQuery = new StringBuilder();
		ejbQLQuery.append("SELECT DISTINCT(experiment) FROM Experiment3VO experiment ");

		switch (scope) {
			case MINIMAL:
				// No additional joins are appended.
				break;
			case MEDIUM:
				ejbQLQuery.append("LEFT JOIN FETCH experiment.samples LEFT JOIN experiment.samples samples ");

				ejbQLQuery.append("LEFT JOIN FETCH samples.macromolecule3VO LEFT JOIN samples.macromolecule3VO macromolecule ");
				ejbQLQuery.append("LEFT JOIN FETCH macromolecule.stoichiometry ");
				ejbQLQuery.append("LEFT JOIN FETCH macromolecule.structure3VOs ");

				ejbQLQuery.append("LEFT JOIN FETCH samples.measurements LEFT JOIN samples.measurements specimens ");
				ejbQLQuery.append("LEFT JOIN FETCH specimens.merge3VOs ");
				ejbQLQuery.append("LEFT JOIN FETCH specimens.run3VO ");

				ejbQLQuery.append("LEFT JOIN FETCH experiment.samplePlate3VOs LEFT JOIN experiment.samplePlate3VOs samplePlates ");
				ejbQLQuery.append("LEFT JOIN FETCH samplePlates.plategroup3VO ");
				ejbQLQuery.append("LEFT JOIN FETCH samplePlates.sampleplateposition3VOs ");


				break;

			case PREPARE_EXPERIMENT:
				ejbQLQuery.append("LEFT JOIN FETCH experiment.samples LEFT JOIN experiment.samples samples ");
				ejbQLQuery.append("LEFT JOIN FETCH samples.macromolecule3VO ");
				ejbQLQuery.append("LEFT JOIN FETCH samples.measurements LEFT JOIN samples.measurements specimens ");

				ejbQLQuery.append("LEFT JOIN FETCH experiment.samplePlate3VOs LEFT JOIN experiment.samplePlate3VOs samplePlates ");
				ejbQLQuery.append("LEFT JOIN FETCH samplePlates.plategroup3VO ");
				ejbQLQuery.append("LEFT JOIN FETCH samplePlates.sampleplateposition3VOs ");

				ejbQLQuery.append("LEFT JOIN FETCH experiment.dataCollections LEFT JOIN experiment.dataCollections dataCollections ");
				ejbQLQuery.append("LEFT JOIN FETCH dataCollections.measurementtodatacollection3VOs ");
				break;

			default:
				// No additional actions for default case
				break;
		}

		return ejbQLQuery.toString();
	}


	@Override
	public Experiment3VO findByMeasurementId(int measurementId){
//		String ejbQLQuery = getQueryByScope(ExperimentScope.MEDIUM)
//				+ " WHERE specimens.measurementId = :measurementId";

		String sqlQLQuery = 
		  	"""
			SELECT DISTINCT
		   		experiment.experimentId,
		   		specimen.specimenId, specimen.bufferId, specimen.code, specimen.comments, specimen.concentration, specimen.experimentId, specimen.safetyLevelId, specimen.stockSolutionId, specimen.volume,
		   		macromolecule.macromoleculeId, macromolecule.acronym, macromolecule.comments, macromolecule.contactsDescriptionFilePath, macromolecule.creationDate, macromolecule.electronDensity, macromolecule.extintionCoefficient, macromolecule.molecularMass, macromolecule.name, macromolecule.proposalId, macromolecule.refractiveIndex, macromolecule.safetyLevelId, macromolecule.sequence, macromolecule.solventViscosity, macromolecule.symmetry,
		   		samplePlatePosition.samplePlatePositionId, samplePlatePosition.columnNumber, samplePlatePosition.rowNumber, samplePlatePosition.samplePlateId, samplePlatePosition.volume,
		   		measurement.specimenId, measurement.code, measurement.comments, measurement.exposureTemperature, measurement.extraFlowTime, measurement.flow, measurement.imageDirectory, measurement.measurementId, measurement.pathToH5, measurement.priorityLevelId, measurement.transmission, measurement.viscosity, measurement.volumeToLoad, measurement.waitTime,
		   		run.runId, run.beamCenterX, run.beamCenterY, run.creationDate, run.energy, run.exposureTemperature, run.normalization, run.pixelSizeX, run.pixelSizeY, run.radiationAbsolute, run.radiationRelative, run.spectrophotometer, run.storageTemperature, run.timeEnd, run.timePerFrame, run.timeStart, run.transmission
	   		FROM
		   		Experiment experiment
		   		LEFT JOIN Specimen specimen ON experiment.experimentId = specimen.experimentId
		   		LEFT JOIN Macromolecule macromolecule ON specimen.macromoleculeId = macromolecule.macromoleculeId
		   		LEFT JOIN SamplePlatePosition samplePlatePosition ON specimen.samplePlatePositionId = samplePlatePosition.samplePlatePositionId
		   		LEFT JOIN Measurement measurement ON specimen.specimenId = measurement.specimenId
		   		LEFT JOIN Run run ON measurement.runId = run.runId
	   		WHERE
		   		measurement.measurementId = ?1
	   		ORDER BY
		   		experiment.experimentId ASC, specimen.specimenId ASC
		  	""";

		return (Experiment3VO) entityManager.createNativeQuery(sqlQLQuery, Experiment3VO.class)
//				.setParameter("measurementId", measurementId);
				.setParameter(1, measurementId)
				.getSingleResult();
	}
	
	
	@Override
	public List<Experiment3VO> test(String ejbQL) {
		try {
			TypedQuery<Experiment3VO> query = entityManager.createQuery(ejbQL, Experiment3VO.class);
			return query.getResultList();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	@Override
	public Experiment3VO findById(Integer experimentId, ExperimentScope scope) {
		String ejbQLQuery = getQueryByScope(scope)
				+ "WHERE experiment.experimentId = :experimentId";
		TypedQuery<Experiment3VO> query = entityManager.createQuery(ejbQLQuery, Experiment3VO.class)
				.setParameter("experimentId", experimentId);
		List<Experiment3VO> results = query.getResultList();
		if (results.isEmpty()) {
		    return null; // handle no-results case
		} else {
		    return results.get(0);
		}
	}
	
	@Deprecated(forRemoval = true)
	@Override
	public List<Map<String, Object>> getExperimentDescription(Integer experimentId) {
		String session = "select * from v_saxs_datacollection  where experimentId = ?1";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, experimentId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	
	

	/**
	 * It returns the xml as String of the robot.xml file
	 * This xml format is used on BsxCube for load sample changer experiments
	 * 
	 */
	@Override
	public String toRobotXML(Integer experimentId, int proposalId, final SaxsDataCollectionComparator... multipleOptions) {
		this.setPriorities(experimentId, proposalId, multipleOptions);
		return this.toRobotXML(experimentId, proposalId);
	}
	
	@Override
	public String toRobotXML(Integer experimentId, int proposalId) {
		Experiment3VO experiment = this.findById(experimentId, ExperimentScope.MEDIUM);
		List<Buffer3VO> buffers = saxsProposal3Service.findBuffersByProposalId(proposalId);
		return RobotXMLParser.toRobotXML(experiment, experiment.getSamplePlate3VOs(), buffers);
	}

	

	@Override
	public void remove(Experiment3VO experiment) {
		Experiment3VO result = entityManager.merge(experiment);
		this.entityManager.remove(result);
	}

	/**
	 * Sets the order for each data collection based on SaxsDataCollectionComparator
	 */
	@Override
	public Experiment3VO setPriorities(int experimentId, int proposalId, SaxsDataCollectionComparator[] multipleOptions) {
		Experiment3VO experiment = this.findById(experimentId, ExperimentScope.MEDIUM, proposalId);
		List<SaxsDataCollection3VO> dataCollectionList = experiment.getDataCollectionList();
		/** Sort data collections by the priority order **/
		Collections.sort(dataCollectionList, SaxsDataCollectionComparator.compare(experiment, SaxsDataCollectionComparator.getComparator(multipleOptions)));
		
		int priority = 1;
		for (int i = 0; i < dataCollectionList.size(); i++) {
			SaxsDataCollection3VO datacollection = dataCollectionList.get(i);
			List<MeasurementTodataCollection3VO> measurementsToDatacollection = this.getMeasurementToDataCollectionOrdered(datacollection);
			for (MeasurementTodataCollection3VO m : measurementsToDatacollection) {
				Measurement3VO measurement = experiment.getMeasurementById(m.getMeasurementId());
				measurement.setPriority(priority);
				measurement = this.entityManager.merge(measurement);
				priority ++;
			}
		}
		return experiment;
		
	}
	/** Sort two measurements by their dataCollectionOrder field **/
	private static Comparator<MeasurementTodataCollection3VO> MeasurementTodataCollectionComparatorOrder = new Comparator<MeasurementTodataCollection3VO>(){
	    public int compare(MeasurementTodataCollection3VO o1, MeasurementTodataCollection3VO o2) {
	        return o1.getDataCollectionOrder() - o2.getDataCollectionOrder();
	    }
	};
	
	/** Sort a data collection by its dataCollectionOrder field **/
	private List<MeasurementTodataCollection3VO> getMeasurementToDataCollectionOrdered(SaxsDataCollection3VO dataCollection){
		Set<MeasurementTodataCollection3VO> measurements = dataCollection.getMeasurementtodatacollection3VOs();
		List<MeasurementTodataCollection3VO> list = Arrays.asList(measurements.toArray(new MeasurementTodataCollection3VO[measurements.size()]));
		Collections.sort(list, MeasurementTodataCollectionComparatorOrder);
		return list;
	}

	@Override
	public void saveStructure(Integer macromoleculeId, String fileName, String filePath, String type, String symmetry, String multiplicity) {
		this.saveStructure(macromoleculeId, null, fileName, filePath, type, symmetry, multiplicity);
	}
		
	public Structure3VO saveStructure(Integer macromoleculeId, Integer crystalId, String fileName, String filePath, String type, String symmetry, String multiplicity) {
		Structure3VO structure = new Structure3VO();
		structure.setMacromoleculeId(macromoleculeId);
		structure.setFilePath(filePath);
		structure.setName(fileName);
		structure.setType(type);
		structure.setSymmetry(symmetry);
		structure.setMultiplicity(multiplicity);
		structure.setCreationDate(GregorianCalendar.getInstance().getTime());
		return entityManager.merge(structure);
	}
	

	@Override
	public void removeStructure(int structureId) {
		Structure3VO structure = this.entityManager.find(Structure3VO.class, structureId);
		this.entityManager.remove(structure);
	}

	@Override
	public Structure3VO findStructureById(int structureId) {
		return this.entityManager.find(Structure3VO.class, structureId);
	}

	@Override
	public void removeStoichiometry(int stoichiometryId) {
		Stoichiometry3VO st = this.entityManager.find(Stoichiometry3VO.class, stoichiometryId);
		this.entityManager.remove(st);
	}

	@Override
	public void saveStoichiometry(int macromoleculeId, int hostmacromoleculeId, String ratio, String comments) {
		Stoichiometry3VO stoi = new Stoichiometry3VO();
		stoi.setMacromolecule3VOByHostMacromoleculeId(macromoleculeId);
		stoi.setHostmacromoleculeId(hostmacromoleculeId);
		stoi.setRatio(ratio);
		this.entityManager.merge(stoi);
	}

//	@Override
//	public Macromolecule3VO findMacromoleculeById(Integer macromoleculeId) {
//		StringBuilder ejbQLQuery = new StringBuilder();
//		ejbQLQuery.append("SELECT DISTINCT(macromolecule) FROM Macromolecule3VO macromolecule ");
//		ejbQLQuery.append("LEFT JOIN FETCH macromolecule.stoichiometry st ");
//		ejbQLQuery.append("LEFT JOIN FETCH st.macromolecule3VO ");
//		ejbQLQuery.append("LEFT JOIN FETCH macromolecule.structure3VOs ");
//		ejbQLQuery.append("WHERE macromolecule.macromoleculeId = :macromoleculeId");
//		TypedQuery<Macromolecule3VO> query = entityManager.createQuery(ejbQLQuery.toString(), Macromolecule3VO.class);
//		query.setParameter("macromoleculeId", macromoleculeId);
//		return query.getSingleResult();
//	}

	@Override
	public Structure3VO findStructureByFilePathId(String filePath, int experimentId) {
//		Experiment3VO experiment = this.findById(experimentId, ExperimentScope.MINIMAL);
//		List<Macromolecule3VO> macromolecules = saxsProposal3Service.findMacromoleculesByProposalId(experiment.getProposalId());
//		for (Macromolecule3VO macromolecule3vo : macromolecules) {
//			
//		}
		return null;
	}

	@Override
	public Structure3VO saveStructure(Structure3VO structure3vo) {
		return this.entityManager.merge(structure3vo);
		
	}
}
