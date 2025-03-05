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

import java.util.List;

import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.Workflow3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.apache.log4j.Logger;

import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroupWS3VO;

/**
 * <p>
 * This session bean handles ISPyB DataCollectionGroup3.
 * </p>
 */
@Stateless
public class DataCollectionGroup3ServiceBean implements DataCollectionGroup3Service, DataCollectionGroup3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(DataCollectionGroup3ServiceBean.class);

	// Generic HQL request to find instances of DataCollectionGroup3 by pk

	// Generic HQL request to find all instances of DataCollectionGroup3

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	public DataCollectionGroup3ServiceBean() {
	};

	/**
	 * Create new DataCollectionGroup3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public DataCollectionGroup3VO create(final DataCollectionGroup3VO vo) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator
		// .getInstance().getService(
		// AuthorizationServiceLocalHome.class); // TODO change method to the one checking the needed access
		// rights
		// autService.checkUserRightToChangeAdminData();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}


	/**
	 * Update the DataCollectionGroup3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public DataCollectionGroup3VO update(final DataCollectionGroup3VO vo) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator
		// .getInstance().getService(
		// AuthorizationServiceLocalHome.class); // TODO change method to the one checking the needed access
		// rights
		// autService.checkUserRightToChangeAdminData();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the DataCollectionGroup3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator
		// .getInstance().getService(
		// AuthorizationServiceLocalHome.class); // TODO change method to the one checking the needed access
		// rights
		// autService.checkUserRightToChangeAdminData();
		DataCollectionGroup3VO vo = findByPk(pk, false, false);
		// TODO Edit this business code
		delete(vo);
	}

	/**
	 * Remove the DataCollectionGroup3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final DataCollectionGroup3VO vo) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator
		// .getInstance().getService(
		// AuthorizationServiceLocalHome.class); // TODO change method to the one checking the needed access
		// rights
		// autService.checkUserRightToChangeAdminData();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the DataCollectionGroup3 value object
	 */
	public DataCollectionGroup3VO findByPk(final Integer pk, final boolean withDataCollection, final boolean withScreening) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator
		// .getInstance().getService(
		// AuthorizationServiceLocalHome.class); // TODO change method to the one checking the needed access
		// rights
		// autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		try {
			return (DataCollectionGroup3VO) entityManager.createQuery("SELECT DISTINCT(vo) from DataCollectionGroup3VO vo "
							+ (withDataCollection ? "left join fetch vo.dataCollectionVOs " : "")
							+ (withScreening ? "left join fetch vo.screeningVOs " : "")
							+ "where vo.dataCollectionGroupId = :pk")
					.setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Find a dataCollectionGroup by its primary key -- webservices object
	 * 
	 * @param pk
	 * @param withLink1
	 * @param withLink2
	 * @return
	 * @throws Exception
	 */
	public DataCollectionGroupWS3VO findForWSByPk(final Integer pk, final boolean withDataCollection, final boolean withScreening) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator
		// .getInstance().getService(
		// AuthorizationServiceLocalHome.class); // TODO change method to the one checking the needed access
		// rights
		// autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		try {
			DataCollectionGroup3VO found = (DataCollectionGroup3VO) entityManager.createQuery("SELECT DISTINCT(vo) from DataCollectionGroup3VO vo "
							+ (withDataCollection ? "left join fetch vo.dataCollectionVOs " : "")
							+ (withScreening ? "left join fetch vo.screeningVOs " : "")
							+ "where vo.dataCollectionGroupId = :pk")
					.setParameter("pk", pk).getSingleResult();
			DataCollectionGroupWS3VO sesLight = getWSDataCollectionGroupVO(found);
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
	private DataCollectionGroup3VO getLightDataCollectionGroup3VO(DataCollectionGroup3VO vo)
			throws CloneNotSupportedException {
		DataCollectionGroup3VO otherVO = (DataCollectionGroup3VO) vo.clone();
		otherVO.setDataCollectionVOs(null);
		otherVO.setScreeningVOs(null);
		return otherVO;
	}

	private DataCollectionGroupWS3VO getWSDataCollectionGroupVO(DataCollectionGroup3VO vo)
			throws CloneNotSupportedException {
		DataCollectionGroup3VO otherVO = getLightDataCollectionGroup3VO(vo);
		Integer sessionId = null;
		Integer blSampleId = null;
		sessionId = otherVO.getSessionVOId();
		blSampleId = otherVO.getBlSampleVOId();
		otherVO.setSessionVO(null);
		otherVO.setBlSampleVO(null);
		DataCollectionGroupWS3VO wsDataCollectionGroup = new DataCollectionGroupWS3VO(otherVO);
		wsDataCollectionGroup.setSessionId(sessionId);
		wsDataCollectionGroup.setBlSampleId(blSampleId);
		return wsDataCollectionGroup;
	}

	// TODO remove following method if not adequate
	/**
	 * Find all DataCollectionGroup3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	public List<DataCollectionGroup3VO> findAll(final boolean withDataCollection) throws Exception {

        return entityManager.createQuery("SELECT vo from DataCollectionGroup3VO vo "
				+ (withDataCollection ? "left join fetch vo.dataCollectionVOs " : ""), DataCollectionGroup3VO.class)
				.getResultList();
	}


	/**
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public DataCollectionGroup3VO loadEager(DataCollectionGroup3VO vo) throws Exception {
		DataCollectionGroup3VO newVO = this.findByPk(vo.getDataCollectionGroupId(), true, true);
		return newVO;
	}

	@SuppressWarnings("unchecked")
	public List<DataCollectionGroup3VO> findFiltered(final Integer sessionId, final boolean withDataCollection, final boolean withScreenings)
			throws Exception {

		EntityManager em = this.entityManager; // Assuming entityManager is already injected or retrieved

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DataCollectionGroup3VO> cq = cb.createQuery(DataCollectionGroup3VO.class);
		Root<DataCollectionGroup3VO> root = cq.from(DataCollectionGroup3VO.class);

// Handling the condition to filter by sessionId
		if (sessionId != null) {
			Join<DataCollectionGroup3VO, Session3VO> sessionJoin = root.join("sessionVO", JoinType.INNER);
			cq.where(cb.equal(sessionJoin.get("sessionId"), sessionId));
		}

// Fetch related entities if required
		if (withDataCollection) {
			root.fetch("dataCollectionVOs", JoinType.LEFT);
		}
		if (withScreenings) {
			root.fetch("screeningVOs", JoinType.LEFT);
		}

// Order by startTime in descending order
		cq.orderBy(cb.desc(root.get("startTime")));

// Create the query
		TypedQuery<DataCollectionGroup3VO> query = em.createQuery(cq);

		// TODO understand why crit.setMaxResults does not work ???
// Note: For setting maximum results, you would do this on the TypedQuery
// For example, to set max results to 10 you would uncomment the following:
// query.setMaxResults(10);

// Execute the query and get the result list
		List<DataCollectionGroup3VO> foundEntities = query.getResultList();
		return foundEntities;

	}

	/**
	 * finds groups for a given workflow
	 * 
	 * @param workflowId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollectionGroup3VO> findByWorkflow(final Integer workflowId) throws Exception {

		EntityManager em = this.entityManager; // Ensure your EntityManager is injected or created as needed

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DataCollectionGroup3VO> cq = cb.createQuery(DataCollectionGroup3VO.class);
		Root<DataCollectionGroup3VO> root = cq.from(DataCollectionGroup3VO.class);

// Handling the condition to filter by workflowId
		if (workflowId != null) {
			Join<DataCollectionGroup3VO, Workflow3VO> workflowJoin = root.join("workflowVO", JoinType.INNER); // Adjust join type as needed
			cq.where(cb.equal(workflowJoin.get("workflowId"), workflowId));
		}

// Ordering by startTime in descending order
		cq.orderBy(cb.desc(root.get("startTime")));

// Create a distinct true query to mimic 'DISTINCT_ROOT_ENTITY'
		cq.select(root).distinct(true);

// Create the query and get results
		TypedQuery<DataCollectionGroup3VO> query = em.createQuery(cq);

		// TODO understand why crit.setMaxResults does not work ???
// If you need to limit the results you can uncomment the following line
// query.setMaxResults(10); // Set the maximum number of results you want to fetch

		List<DataCollectionGroup3VO> foundEntities = query.getResultList();
		return foundEntities;

	}


	/**
	 * find groups for a given blSampleId
	 * 
	 * @param sampleId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DataCollectionGroup3VO> findBySampleId(final Integer sampleId, final boolean withDataCollection, final boolean withScreenings)
			throws Exception {

		EntityManager em = this.entityManager; // Ensure your EntityManager is injected or created as needed

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DataCollectionGroup3VO> cq = cb.createQuery(DataCollectionGroup3VO.class);
		Root<DataCollectionGroup3VO> root = cq.from(DataCollectionGroup3VO.class);

// Handling fetch joins if needed
		if (withDataCollection) {
			root.fetch("dataCollectionVOs", JoinType.LEFT); // Adjust join type based on your needs
		}
		if (withScreenings) {
			root.fetch("screeningVOs", JoinType.LEFT); // Adjust join type based on your needs
		}

// Conditionally adding a filter for sampleId
		if (sampleId != null) {
			Join<DataCollectionGroup3VO, BLSample3VO> sampleJoin = root.join("blSampleVO", JoinType.INNER); // Adjust join type as needed
			cq.where(cb.equal(sampleJoin.get("blSampleId"), sampleId));
		}

// Ordering by startTime in descending order
		cq.orderBy(cb.desc(root.get("startTime")));

// Ensuring distinct results
		cq.select(root).distinct(true);

// Prepare the query
		TypedQuery<DataCollectionGroup3VO> query = em.createQuery(cq);

		// TODO understand why crit.setMaxResults does not work ???

// Execute the query and get results
		List<DataCollectionGroup3VO> foundEntities = query.getResultList();
		return foundEntities;

	}


	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * 
	 * @param vo
	 *            the data to check
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like
	 *            testing the primary key
	 * @exception VOValidateException
	 *                if data is not correct
	 */
	private void checkAndCompleteData(DataCollectionGroup3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getDataCollectionGroupId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getDataCollectionGroupId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}