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
package ispyb.server.mx.services.collections;

import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.vos.collections.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.Protein3VO;
import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import jakarta.persistence.criteria.*;
import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB DataCollection3.
 * </p>
 */
@Stateless
public class DataCollection3ServiceBean implements DataCollection3Service, DataCollection3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(DataCollection3ServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	public DataCollection3ServiceBean() {
	};

	/**
	 * Create new DataCollection3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public DataCollection3VO create(final DataCollection3VO vo) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);
		// // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the DataCollection3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public DataCollection3VO update(final DataCollection3VO vo) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);
		// // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the DataCollection3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);
		// // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		DataCollection3VO vo = findByPk(pk, false, false);
		// TODO Edit this business code
		delete(vo);
	}

	/**
	 * Remove the DataCollection3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final DataCollection3VO vo) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);
		// // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		entityManager.remove(entityManager.merge(vo));
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects
	 * if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the DataCollection3 value object
	 */
	public DataCollection3VO findByPk(final Integer pk, final boolean withImage, final boolean withAutoProcIntegration) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);
		// // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		try {
			String qlString = "SELECT vo from DataCollection3VO vo "
					+ (withImage ? "left join fetch vo.imageVOs " : "")
					+ (withAutoProcIntegration ? "left join fetch vo.autoProcIntegrationVOs " : "") + "where vo.dataCollectionId = :pk";
			return (DataCollection3VO) entityManager.createQuery(qlString)
					.setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Find a dataCollection by its primary key -- webservices object
	 * 
	 * @param pk
	 * @param withLink1
	 * @param withLink2
	 * @return
	 * @throws Exception
	 */
	public DataCollectionWS3VO findForWSByPk(final Integer pk, final boolean withImage, final boolean withAutoProcIntegration) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);
		// // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		try {
			String qlString = "SELECT vo from DataCollection3VO vo "
					+ (withImage ? "left join fetch vo.imageVOs " : "")
					+ (withAutoProcIntegration ? "left join fetch vo.autoProcIntegrationVOs " : "") + "where vo.dataCollectionId = :pk";
			DataCollection3VO found = (DataCollection3VO) entityManager.createQuery(qlString)
					.setParameter("pk", pk)
					.getSingleResult();
			DataCollectionWS3VO sesLight = getWSDataCollectionVO(found);
			return sesLight;
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private DataCollection3VO getLightDataCollection3VO(DataCollection3VO vo) throws CloneNotSupportedException {
		if (vo == null)
			return null;
		DataCollection3VO otherVO = (DataCollection3VO) vo.clone();
		// otherVO.set(null);
		return otherVO;
	}

	private DataCollectionWS3VO[] getWSDataCollectionVOs(List<DataCollection3VO> entities) throws CloneNotSupportedException {
		if (entities == null)
			return null;
		ArrayList<DataCollectionWS3VO> results = new ArrayList<DataCollectionWS3VO>(entities.size());
		for (DataCollection3VO vo : entities) {
			DataCollectionWS3VO otherVO = getWSDataCollectionVO(vo);
			if (otherVO != null)
				results.add(otherVO);
		}
		DataCollectionWS3VO[] tmpResults = new DataCollectionWS3VO[results.size()];
		return results.toArray(tmpResults);
	}

	private DataCollectionWS3VO getWSDataCollectionVO(DataCollection3VO vo) throws CloneNotSupportedException {
		if (vo != null) {
			DataCollection3VO otherVO = getLightDataCollection3VO(vo);
			Integer dataCollectionGroupId = null;
			Integer strategySubWedgeOrigId = null;
			Integer detectorId = null;
			Integer blSubSampleId = null;
			dataCollectionGroupId = otherVO.getDataCollectionGroupVOId();
			strategySubWedgeOrigId = otherVO.getStrategySubWedgeOrigVOId();
			detectorId = otherVO.getDetectorVOId();
			blSubSampleId = otherVO.getBlSubSampleVOId();
			otherVO.setDataCollectionGroupVO(null);
			otherVO.setStrategySubWedgeOrigVO(null);
			otherVO.setDetectorVO(null);
			otherVO.setBlSubSampleVO(null);
			DataCollectionWS3VO wsDataCollection = new DataCollectionWS3VO(otherVO);
			wsDataCollection.setDataCollectionGroupId(dataCollectionGroupId);
			wsDataCollection.setStrategySubWedgeOrigId(strategySubWedgeOrigId);
			wsDataCollection.setDetectorId(detectorId);
			wsDataCollection.setBlSubSampleId(blSubSampleId);
			return wsDataCollection;
		} else {
			return null;
		}
	}

	/**
	 * Find all DataCollection3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findAll() throws Exception {

		List<DataCollection3VO> foundEntities = entityManager.createQuery("from DataCollection3VO vo ").getResultList();
		return foundEntities;
	}

	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findByShippingId(final Integer shippingId) throws Exception {

		String query = "select * from DataCollection, DataCollectionGroup, BLSample, Container, Dewar"
				+ " where DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId AND "
				+ "DataCollectionGroup.blSampleId = BLSample.blSampleId  "
				+ " and BLSample.containerId = Container.containerId "
				+ " and Container.dewarId = Dewar.dewarId "
				+ " and Dewar.shippingId = ?1 ";
		List<DataCollection3VO> col = this.entityManager.createNativeQuery(query, "dataCollectionNativeQuery")
				.setParameter(1, shippingId)
				.getResultList();
		return col;
	}

	/**
	 * Find the dataCollections for a given sample
	 * 
	 * @param blSampleId
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	public DataCollectionWS3VO[] findForWSLastDataCollectionBySample(final Integer blSampleId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public DataCollectionWS3VO[] doInEJBAccess(Object parent) throws Exception {
				String query = "select * from DataCollection, DataCollectionGroup, BLSample "
						+ " where DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId AND "
						+ "DataCollectionGroup.blSampleId = BLSample.blSampleId  "
						+ " and BLSample.blSampleId = ?1 ORDER BY DataCollection.endTime ASC ";
				List<DataCollection3VO> listVOs = entityManager.createNativeQuery(query, "dataCollectionNativeQuery")
						.setParameter(1, blSampleId)
						.getResultList();
				if (listVOs == null || listVOs.isEmpty())
					listVOs = null;
				List<DataCollection3VO> foundEntities = listVOs;
				DataCollectionWS3VO[] vos = getWSDataCollectionVOs(foundEntities);
				return vos;
			};
		};
		DataCollectionWS3VO[] ret = (DataCollectionWS3VO[]) template.execute(callBack);

		return ret;
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	@SuppressWarnings("unused")
	private List<DataCollection3VO> getLightDataCollectionVOs(List<DataCollection3VO> entities) throws CloneNotSupportedException {
		List<DataCollection3VO> results = new ArrayList<DataCollection3VO>(entities.size());
		for (DataCollection3VO vo : entities) {
			DataCollection3VO otherVO = getLightDataCollectionVO(vo);
			results.add(otherVO);
		}
		return results;
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private DataCollection3VO getLightDataCollectionVO(DataCollection3VO vo) throws CloneNotSupportedException {
		DataCollection3VO otherVO = (DataCollection3VO) vo.clone();
		otherVO.setImageVOs(null);
		otherVO.setAutoProcIntegrationVOs(null);
		return otherVO;
	}

	/**
	 * Find a dataCollection with the image directory, the image prefix and the
	 * dataCollection Number
	 * 
	 * @param imageDirectory
	 * @param imagePrefix
	 * @param dataCollectionNumber
	 * @return
	 * @throws Exception
	 */
	public DataCollectionWS3VO findForWSDataCollectionFromImageDirectoryAndImagePrefixAndNumber(final String imageDirectory, final String imagePrefix,
			final Integer dataCollectionNumber) throws Exception {

		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();

		CriteriaQuery<DataCollection3VO> cq = cb.createQuery(DataCollection3VO.class);
		Root<DataCollection3VO> dataCollectionRoot = cq.from(DataCollection3VO.class);

// Create the conditions (predicates) based on provided inputs
		List<Predicate> predicates = new ArrayList<>();

		if (imageDirectory != null) {
			predicates.add(cb.like(dataCollectionRoot.get("imageDirectory"), imageDirectory));
		}

		if (imagePrefix != null) {
			predicates.add(cb.like(dataCollectionRoot.get("imagePrefix"), imagePrefix));
		}

		if (dataCollectionNumber != null) {
			predicates.add(cb.equal(dataCollectionRoot.get("dataCollectionNumber"), dataCollectionNumber));
		}

// Apply all collected predicates into the where clause of the query
		cq.where(cb.and(predicates.toArray(new Predicate[0])));

// Order by startTime in descending order
		cq.orderBy(cb.desc(dataCollectionRoot.get("startTime")));

// Ensure DISTINCT results
		cq.distinct(true);

// Execute the query and collect results
		List<DataCollection3VO> foundEntities = this.entityManager.createQuery(cq).getResultList();

// Transform to DataCollectionWS3VO if necessary
		DataCollectionWS3VO[] vos = getWSDataCollectionVOs(foundEntities);
		if (vos == null || vos.length == 0)
			return null;
		return vos[0];
	}

	/**
	 * Find a dataCollection with the image fileLocation and image fileName
	 * 
	 * @param fileLocation
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public DataCollectionWS3VO findForWSDataCollectionIdFromFileLocationAndFileName(final String fileLocation, final String fileName) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public DataCollectionWS3VO[] doInEJBAccess(Object parent) throws Exception {
				String query = "select * from DataCollection, Image "
						+ " where DataCollection.dataCollectionId = Image.dataCollectionId  "
						+ " and Image.fileLocation like ?1 AND Image.fileName like ?2 ORDER BY DataCollection.endTime ASC ";
				List<DataCollection3VO> listVOs = entityManager.createNativeQuery(query, "dataCollectionNativeQuery")
						.setParameter(1, fileLocation)
						.setParameter(2, fileName)
						.getResultList();
				if (listVOs == null || listVOs.isEmpty())
					listVOs = null;

				List<DataCollection3VO> foundEntities = listVOs;
				DataCollectionWS3VO[] vos = getWSDataCollectionVOs(foundEntities);
				return vos;
			};
		};
		DataCollectionWS3VO[] ret = (DataCollectionWS3VO[]) template.execute(callBack);
		if (ret == null || ret.length == 0)
			return null;
		return ret[0];
	}

	/**
	 * Find a dataCollection with the image fileLocation and image fileName
	 * 
	 * @param fileLocation
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public DataCollection3VO findForDataCollectionIdFromFileLocationAndFileName(final String fileLocation, final String fileName) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		EJBAccessCallback callBack = new EJBAccessCallback() {

			public List<DataCollection3VO> doInEJBAccess(Object parent) throws Exception {
				String query = "select * from DataCollection, Image "
						+ " where DataCollection.dataCollectionId = Image.dataCollectionId  "
						+ " and Image.fileLocation like ?1 AND Image.fileName like ?2 ORDER BY DataCollection.endTime ASC ";
				List<DataCollection3VO> listVOs = entityManager.createNativeQuery(query, "dataCollectionNativeQuery")
						.setParameter(1, fileLocation)
						.setParameter(2, fileName)
						.getResultList();
				if (listVOs == null || listVOs.isEmpty())
					listVOs = null;

				List<DataCollection3VO> foundEntities = listVOs;
				return foundEntities;
			};
		};
		List<DataCollection3VO> ret = (List<DataCollection3VO>) template.execute(callBack);
		if (ret == null || ret.size() == 0)
			return null;
		return ret.get(0);
	}

	/**
	 * 
	 * @param imageDirectory
	 * @param imagePrefix
	 * @param dataCollectionNumber
	 * @param sessionId
	 * @param printableForReport
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findFiltered(final String imageDirectory, final String imagePrefix, final Integer dataCollectionNumber,
			final Integer sessionId, final Byte printableForReport, final Integer dataCollectionGroupId) throws Exception {

		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();

		CriteriaQuery<DataCollection3VO> cq = cb.createQuery(DataCollection3VO.class);
		Root<DataCollection3VO> dataCollectionRoot = cq.from(DataCollection3VO.class);

// Joining with DataCollectionGroupVO
		Join<DataCollection3VO, DataCollectionGroup3VO> groupJoin = dataCollectionRoot.join("dataCollectionGroupVO", JoinType.INNER);

// Adding conditions
		List<Predicate> predicates = new ArrayList<>();

		if (sessionId != null) {
			// Further joining through DataCollectionGroupVO to SessionVO
			Join<DataCollectionGroup3VO, Session3VO> sessionJoin = groupJoin.join("sessionVO", JoinType.INNER);
			predicates.add(cb.equal(sessionJoin.get("sessionId"), sessionId));
		}

		if (dataCollectionGroupId != null) {
			predicates.add(cb.equal(groupJoin.get("dataCollectionGroupId"), dataCollectionGroupId));
		}

		if (imageDirectory != null) {
			predicates.add(cb.like(dataCollectionRoot.get("imageDirectory"), imageDirectory));
		}

		if (imagePrefix != null) {
			predicates.add(cb.like(dataCollectionRoot.get("imagePrefix"), imagePrefix));
		}

		if (dataCollectionNumber != null) {
			predicates.add(cb.equal(dataCollectionRoot.get("dataCollectionNumber"), dataCollectionNumber));
		}

		if (printableForReport != null) {
			predicates.add(cb.equal(dataCollectionRoot.get("printableForReport"), printableForReport));
		}

// Apply the conditions to the Criteria Query
		cq.where(cb.and(predicates.toArray(new Predicate[0])));

// Order by startTime in descending order
		cq.orderBy(cb.desc(dataCollectionRoot.get("startTime")));

// Make sure the results are distinct
		cq.distinct(true);

// Execute the query
		List<DataCollection3VO> foundEntities = this.entityManager.createQuery(cq).getResultList();
		return foundEntities;
	}

	/**
	 * 
	 * @param proposalId
	 * @param sampleName
	 * @param proteinAcronym
	 * @param beamlineName
	 * @param experimentDateStart
	 * @param experimentDateEnd
	 * @param minNumberOfImages
	 * @param maxNumberOfImages
	 * @param onlyPrintableForReport
	 * @param maxRecords
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findByCustomQuery(final Integer proposalId, final String sampleName, final String proteinAcronym, final String beamlineName,
			final Date experimentDateStart, final Date experimentDateEnd, final Integer minNumberOfImages, final Integer maxNumberOfImages,
			final String imagePrefix, final Byte onlyPrintableForReport, final Integer maxRecords) throws Exception {

		EntityManager em = this.entityManager;
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<DataCollection3VO> cq = cb.createQuery(DataCollection3VO.class);
		Root<DataCollection3VO> dataCollectionRoot = cq.from(DataCollection3VO.class);

// Join structures
		Join<DataCollection3VO, DataCollectionGroup3VO> groupJoin = dataCollectionRoot.join("dataCollectionGroupVO", JoinType.INNER);
		Join<DataCollectionGroup3VO, Session3VO> sessionJoin = groupJoin.join("sessionVO", JoinType.INNER);
		Join<Session3VO, Proposal3VO> proposalJoin = sessionJoin.join("proposalVO", JoinType.LEFT);

// Sample and protein criteria are conditional
		Join<DataCollectionGroup3VO, BLSample3VO> sampleJoin = null;
		Join<BLSample3VO, Crystal3VO> crystalJoin = null;
		Join<Crystal3VO, Protein3VO> proteinJoin = null;

		List<Predicate> predicates = new ArrayList<>();

		if (proposalId != null) {
			predicates.add(cb.equal(proposalJoin.get("proposalId"), proposalId));
		}

		if (sampleName != null && !sampleName.isEmpty()) {
			sampleJoin = groupJoin.join("blSampleVO", JoinType.INNER);
			predicates.add(cb.like(sampleJoin.get("name"), sampleName.replace('*', '%')));
		}

		if (proteinAcronym != null && !proteinAcronym.isEmpty()) {
			if (sampleJoin == null) sampleJoin = groupJoin.join("blSampleVO", JoinType.INNER);
			crystalJoin = sampleJoin.join("crystalVO", JoinType.INNER);
			proteinJoin = crystalJoin.join("proteinVO", JoinType.INNER);
			predicates.add(cb.like(proteinJoin.get("acronym"), proteinAcronym.replace('*', '%')));
		}

		if (beamlineName != null && !beamlineName.isEmpty()) {
			predicates.add(cb.like(sessionJoin.get("beamlineName"), beamlineName.replace('*', '%')));
		}

		if (experimentDateStart != null) {
			predicates.add(cb.greaterThanOrEqualTo(sessionJoin.<Date>get("startTime"), experimentDateStart));
		}

		if (experimentDateEnd != null) {
			predicates.add(cb.lessThanOrEqualTo(sessionJoin.<Date>get("startTime"), experimentDateEnd));
		}

		if (minNumberOfImages != null) {
			predicates.add(cb.ge(dataCollectionRoot.<Integer>get("numberOfImages"), minNumberOfImages));
		}

		if (maxNumberOfImages != null) {
			predicates.add(cb.le(dataCollectionRoot.<Integer>get("numberOfImages"), maxNumberOfImages));
		}

		if (imagePrefix != null && !imagePrefix.isEmpty()) {
			predicates.add(cb.like(dataCollectionRoot.get("imagePrefix"), imagePrefix.replace('*', '%')));
		}

		if (onlyPrintableForReport != null) {
			predicates.add(cb.equal(dataCollectionRoot.get("printableForReport"), onlyPrintableForReport));
		}

		cq.where(cb.and(predicates.toArray(new Predicate[0])));
		cq.orderBy(cb.desc(dataCollectionRoot.get("startTime")));

// Applying DISTINCT
		cq.distinct(true);

		TypedQuery<DataCollection3VO> query = em.createQuery(cq);
		if (maxRecords != null) {
			query.setMaxResults(maxRecords);
		}

		List<DataCollection3VO> foundEntities = query.getResultList();
		return foundEntities;

	}

	/**
	 * 
	 * @param proteinAcronym
	 * @param printableForReport
	 * @param proposalId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findByProtein(String proteinAcronym, final Byte printableForReport, final Integer proposalId) throws Exception {

		EntityManager em = this.entityManager;
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<DataCollection3VO> cq = cb.createQuery(DataCollection3VO.class);
		Root<DataCollection3VO> dataCollectionRoot = cq.from(DataCollection3VO.class);

// Joining related entities
		Join<DataCollection3VO, DataCollectionGroup3VO> groupJoin = dataCollectionRoot.join("dataCollectionGroupVO", JoinType.LEFT);
		Join<DataCollectionGroup3VO, Session3VO> sessionJoin = groupJoin.join("sessionVO", JoinType.LEFT);
		Join<Session3VO, Proposal3VO> proposalJoin = sessionJoin.join("proposalVO", JoinType.LEFT);

// Conditionally joining sample and protein based on proteinAcronym presence
		Join<DataCollectionGroup3VO, BLSample3VO> sampleJoin = null;
		Join<BLSample3VO, Crystal3VO> crystalJoin = null;
		Join<Crystal3VO, Protein3VO> proteinJoin = null;

		List<Predicate> predicates = new ArrayList<>();

// Adding conditions
		if (proposalId != null) {
			predicates.add(cb.equal(proposalJoin.get("proposalId"), proposalId));
		}

		if (proteinAcronym != null && !proteinAcronym.isEmpty()) {
			proteinAcronym = proteinAcronym.replace('*', '%');  // Handling wildcard character
			sampleJoin = groupJoin.join("blSampleVO", JoinType.LEFT);
			crystalJoin = sampleJoin.join("crystalVO", JoinType.LEFT);
			proteinJoin = crystalJoin.join("proteinVO", JoinType.LEFT);
			predicates.add(cb.like(proteinJoin.get("acronym"), proteinAcronym));
		}

		if (printableForReport != null) {
			predicates.add(cb.equal(dataCollectionRoot.get("printableForReport"), printableForReport));
		}

		cq.select(dataCollectionRoot)
				.where(cb.and(predicates.toArray(new Predicate[0])))
				.orderBy(cb.desc(dataCollectionRoot.get("startTime")));

// Applying DISTINCT to ensure unique results
		cq.distinct(true);

		List<DataCollection3VO> foundEntities = em.createQuery(cq).getResultList();
		return foundEntities;

	}

	/**
	 * 
	 * @param blSampleId
	 * @param printableForReport
	 * @param proposalId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findBySample(final Integer blSampleId, String sampleName, final Byte printableForReport, final Integer proposalId)
			throws Exception {

		EntityManager em = this.entityManager;
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<DataCollection3VO> cq = cb.createQuery(DataCollection3VO.class);
		Root<DataCollection3VO> dataCollectionRoot = cq.from(DataCollection3VO.class);

// Joining related entities
		Join<DataCollection3VO, DataCollectionGroup3VO> groupJoin = dataCollectionRoot.join("dataCollectionGroupVO", JoinType.LEFT);
		Join<DataCollectionGroup3VO, BLSample3VO> sampleJoin = groupJoin.join("blSampleVO", JoinType.LEFT);
		Join<BLSample3VO, Session3VO> sessionJoin = groupJoin.join("sessionVO", JoinType.LEFT);
		Join<Session3VO, Proposal3VO> proposalJoin = sessionJoin.join("proposalVO", JoinType.LEFT);

		List<Predicate> predicates = new ArrayList<>();

// Adding conditions
		if (proposalId != null) {
			predicates.add(cb.equal(proposalJoin.get("proposalId"), proposalId));
		}

		if (blSampleId != null) {
			predicates.add(cb.equal(sampleJoin.get("blSampleId"), blSampleId));
		}

		if (sampleName != null && !sampleName.isEmpty()) {
			sampleName = sampleName.replace('*', '%'); // Handling wildcard character
			predicates.add(cb.like(sampleJoin.get("name"), sampleName));
		}

		if (printableForReport != null) {
			predicates.add(cb.equal(dataCollectionRoot.get("printableForReport"), printableForReport));
		}

		cq.select(dataCollectionRoot)
				.where(cb.and(predicates.toArray(new Predicate[0])))
				.orderBy(cb.desc(dataCollectionRoot.get("startTime")));

// Applying DISTINCT to ensure unique results
		cq.distinct(true);

		List<DataCollection3VO> foundEntities = em.createQuery(cq).getResultList();
		return foundEntities;

	}

	public DataCollection3VO loadEager(DataCollection3VO vo) throws Exception {
		DataCollection3VO newVO = this.findByPk(vo.getDataCollectionId(), true, true);
		return newVO;
	}

	private static Double getDoubleValue(Float f) {
		if (f != null)
			return Double.parseDouble(f.toString());
		return null;
	}

	/**
	 * returns the XDSInfo for a given dataCollectionId
	 * 
	 * @param dataCollectionId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public XDSInfo findForWSXDSInfo(final Integer dataCollectionId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		List orders = (List) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				Query q = entityManager.createNativeQuery("SELECT BLSession.beamLineSetupId, DataCollection.detectorId,  "
						+ "DataCollection.axisRange, DataCollection.axisStart, DataCollection.axisEnd, DataCollection.detectorDistance, "
						+ "DataCollection.fileTemplate, DataCollection.imageDirectory, DataCollection.imageSuffix, "
						+ "DataCollection.numberOfImages, DataCollection.startImageNumber, " + "DataCollection.phiStart, DataCollection.kappaStart, "
						+ "DataCollection.wavelength, DataCollection.xbeam, DataCollection.ybeam " + "FROM DataCollection, DataCollectionGroup, BLSession "
						+ "WHERE DataCollection.dataCollectionId = " + dataCollectionId + " AND "
						+ "DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId AND "
						+ "DataCollectionGroup.sessionId = BLSession.sessionId ");
				List orders = q.getResultList();
				return orders;

			}
		});

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		XDSInfo xds = null;

		if (orders == null || orders.size() < 1)
			return null;
		int nb = orders.size();
		for (int i = 0; i < nb; i++) {
			Object[] o = (Object[]) orders.get(i);
			int j = 0;
			Integer beamLineSetupId = (Integer) o[j++];
			Integer detectorId = (Integer) o[j++];
			Double axisRange = getDoubleValue((Float) o[j++]);
			Double axisStart = getDoubleValue((Float) o[j++]);
			Double axisEnd = getDoubleValue((Float) o[j++]);
			Double detectorDistance = getDoubleValue((Float) o[j++]);
			String fileTemplate = (String) o[j++];
			String imageDirectory = (String) o[j++];
			String imageSuffix = (String) o[j++];
			Integer numberOfImages = (Integer) o[j++];
			Integer startImageNumber = (Integer) o[j++];
			Double phiStart = getDoubleValue((Float) o[j++]);
			Double kappaStart = getDoubleValue((Float) o[j++]);
			Double wavelength = getDoubleValue((Float) o[j++]);
			Double xbeam = getDoubleValue((Float) o[j++]);
			Double ybeam = getDoubleValue((Float) o[j++]);

			Double polarisation = null;
			BeamLineSetup3Service beamLineSetupService = (BeamLineSetup3Service) ejb3ServiceLocator.getLocalService(BeamLineSetup3Service.class);
			BeamLineSetup3VO beamLineSetup = null;
			if (beamLineSetupId != null)
				beamLineSetup = beamLineSetupService.findByPk(beamLineSetupId);
			if (beamLineSetup != null)
				polarisation = beamLineSetup.getPolarisation();

			Double detectorPixelSizeHorizontal = null;
			Double detectorPixelSizeVertical = null;
			String detectorManufacturer = null;
			String detectorModel = null;
			Detector3Service detectorService = (Detector3Service) ejb3ServiceLocator.getLocalService(Detector3Service.class);
			Detector3VO detector = null;
			if (detectorId != null)
				detector = detectorService.findByPk(detectorId);
			if (detector != null) {
				detectorPixelSizeHorizontal = detector.getDetectorPixelSizeHorizontal();
				detectorPixelSizeVertical = detector.getDetectorPixelSizeVertical();
				detectorManufacturer = detector.getDetectorManufacturer();
				detectorModel = detector.getDetectorModel();
			}

			xds = new XDSInfo(polarisation, axisRange, axisStart, axisEnd, detectorDistance, fileTemplate, imageDirectory, imageSuffix, numberOfImages,
					startImageNumber, phiStart, kappaStart, wavelength, xbeam, ybeam, detectorPixelSizeHorizontal, detectorPixelSizeVertical,
					detectorManufacturer, detectorModel);

		}

		return xds;
	}

	/**
	 * returns the pdb full path for a given dataCollectionId
	 * 
	 * @param dataCollectionId
	 * @return
	 * @throws Exception
	 */
	public String findPdbFullPath(final Integer dataCollectionId) throws Exception {

		Query query = entityManager.createNativeQuery("SELECT c.pdbFileName, c.pdbFilePath " + "FROM DataCollection d, DataCollectionGroup g, BLSample s, Crystal c "
				+ "WHERE d.dataCollectionId = :dataCollectionId AND " + "d.dataCollectionGroupId = g.dataCollectionGroupId AND "
				+ "g.blSampleId = s.blSampleId AND " + "s.crystalId = c.crystalId ").setParameter("dataCollectionId", dataCollectionId);
		List orders = query.getResultList();
		if (orders == null || orders.size() < 1)
			return null;
		int nb = orders.size();
		String res = null;
		for (int i = 0; i < nb; i++) {
			Object[] o = (Object[]) orders.get(i);
			int j = 0;
			String fileName = (String) o[j++];
			String filePath = (String) o[j++];
			if (fileName != null && filePath != null)
				res = filePath + fileName;
		}
		String foundEntities = res;
		return foundEntities;
	}

	/**
	 * returns the list of dataCollections with startTime > startDate
	 * 
	 * @param startDate
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findLastCollect(final Date startDate, final Date endDate, final String[] beamline) throws Exception {

		EntityManager em = this.entityManager;
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<DataCollection3VO> cq = cb.createQuery(DataCollection3VO.class);
		Root<DataCollection3VO> dataCollectionRoot = cq.from(DataCollection3VO.class);

// Optional joins based on the presence of beamline criteria
		Join<DataCollection3VO, DataCollectionGroup3VO> groupJoin = null;
		Join<DataCollectionGroup3VO, Session3VO> sessionJoin = null;

		List<Predicate> predicates = new ArrayList<>();

// Condition on start time
		if (startDate != null) {
			predicates.add(cb.greaterThanOrEqualTo(dataCollectionRoot.get("startTime"), startDate));
		}

// Condition on end time
		if (endDate != null) {
			predicates.add(cb.lessThanOrEqualTo(dataCollectionRoot.get("endTime"), endDate));
		}

// Condition on beamline names if provided
		if (beamline != null && beamline.length > 0) {
			groupJoin = dataCollectionRoot.join("dataCollectionGroupVO", JoinType.LEFT);
			sessionJoin = groupJoin.join("sessionVO", JoinType.LEFT);
			predicates.add(sessionJoin.get("beamlineName").in((Object[]) beamline)); // Cast to Object array for safety with varargs
		}

// Apply all conditions
		cq.select(dataCollectionRoot)
				.where(predicates.toArray(new Predicate[0]))
				.orderBy(cb.desc(dataCollectionRoot.get("startTime")));

		List<DataCollection3VO> foundEntities = em.createQuery(cq).getResultList();
		return foundEntities;
	}

	/**
	 * get the number of datcollections which have more then 4 images
	 * 
	 * @param dcgId
	 * @return
	 * @throws Exception
	 */
	public Integer getNbOfCollects(final Integer dcgId) throws Exception {

		String sqlString = "SELECT count(*) FROM DataCollection "
				+ " WHERE  DataCollection.numberOfImages >4 and DataCollection.dataCollectionGroupId = ?1 ";
		Query query = entityManager.createNativeQuery(sqlString)
				.setParameter(1, dcgId);
		try {
			BigInteger res = (BigInteger) query.getSingleResult();

			return res.intValue();
		} catch (NoResultException e) {
			System.out.println("ERROR in getNbOfCollects - NoResultException: " + dcgId);
			e.printStackTrace();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * get the number of datacollections which have less/or 4 images
	 * 
	 * @param dcgId
	 * @return
	 * @throws Exception
	 */
	public Integer getNbOfTests(final Integer dcgId) throws Exception {

		String sqlString = "SELECT count(*) FROM DataCollection "
				+ " WHERE  DataCollection.numberOfImages <=4 and DataCollection.dataCollectionGroupId = ?1 ";
		Query query = entityManager.createNativeQuery(sqlString)
				.setParameter(1, dcgId);
		try {
			BigInteger res = (BigInteger) query.getSingleResult();

			return res.intValue();
		} catch (NoResultException e) {
			System.out.println("ERROR in getNbOfTests - NoResultException: " + dcgId);
			e.printStackTrace();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * 
	 * @param vo
	 *            the data to check
	 * @param create
	 *            should be true if the value object is just being created in
	 *            the DB, this avoids some checks like testing the primary key
	 * @exception VOValidateException
	 *                if data is not correct
	 */
	private void checkAndCompleteData(DataCollection3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getDataCollectionId() != null) {
				throw new IllegalArgumentException("Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getDataCollectionId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findByProposalId(int proposalId) throws Exception {
		EntityManager em = this.entityManager;
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<DataCollection3VO> cq = cb.createQuery(DataCollection3VO.class);
		Root<DataCollection3VO> dataCollectionRoot = cq.from(DataCollection3VO.class);

// Navigating through nested associations
		Join<DataCollection3VO, DataCollectionGroup3VO> groupJoin = dataCollectionRoot.join("dataCollectionGroupVO");
		Join<DataCollectionGroup3VO, Session3VO> sessionJoin = groupJoin.join("sessionVO");
		Join<Session3VO, Proposal3VO> proposalJoin = sessionJoin.join("proposalVO");

// Applying condition on proposalId
		Predicate condition = cb.equal(proposalJoin.get("proposalId"), proposalId);
		cq.select(dataCollectionRoot)
				.where(condition);

		List<DataCollection3VO> foundEntities = em.createQuery(cq).getResultList();
		return foundEntities;
	}
	
	@SuppressWarnings("unchecked")
	public List<DataCollection3VO> findByProposalId(int proposalId, int dataCollectionId) throws Exception {
		EntityManager em = this.entityManager;
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<DataCollection3VO> cq = cb.createQuery(DataCollection3VO.class);
		Root<DataCollection3VO> dataCollectionRoot = cq.from(DataCollection3VO.class);

// Navigating through nested associations and adding conditions on both DataCollection and Proposal
		Join<DataCollection3VO, DataCollectionGroup3VO> groupJoin = dataCollectionRoot.join("dataCollectionGroupVO");
		Join<DataCollectionGroup3VO, Session3VO> sessionJoin = groupJoin.join("sessionVO");
		Join<Session3VO, Proposal3VO> proposalJoin = sessionJoin.join("proposalVO");

// Conditions
		Predicate dataCollectionCondition = cb.equal(dataCollectionRoot.get("dataCollectionId"), dataCollectionId);
		Predicate proposalCondition = cb.equal(proposalJoin.get("proposalId"), proposalId);

		cq.select(dataCollectionRoot)
				.where(cb.and(dataCollectionCondition, proposalCondition));

		List<DataCollection3VO> foundEntities = em.createQuery(cq).getResultList();
		return foundEntities;
	}

}