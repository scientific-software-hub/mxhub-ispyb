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

import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;
import ispyb.server.mx.vos.collections.Image3VO;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.criteria.*;
import org.apache.log4j.Logger;

/**
 * <p>
 *  This session bean handles ISPyB Image3.
 * </p>
 */
@Stateless
public class Image3ServiceBean implements Image3Service,
		Image3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(Image3ServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	public Image3ServiceBean() {
	};

	/**
	 * Create new Image3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public Image3VO create(final Image3VO vo) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}


	/**
	 * Update the Image3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public Image3VO update(final Image3VO vo) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the Image3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		Image3VO vo = findByPk(pk);
		// TODO Edit this business code				
		delete(vo);
	}

	/**
	 * Remove the Image3
	 * @param vo the entity to remove.
	 */
	public void delete(final Image3VO vo) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Image3 value object
	 */
	public Image3VO findByPk(final Integer pk) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		try{
			String qlString = "SELECT vo from Image3VO vo " + "where vo.imageId = :pk";
			return entityManager.createQuery(qlString, Image3VO.class)
					.setParameter("pk", pk)
					.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

	// TODO remove following method if not adequate
	/**
	 * Find all Image3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	public List<Image3VO> findAll()
			throws Exception {

		String qlString = "SELECT vo from Image3VO vo ";
        return entityManager.createQuery(qlString, Image3VO.class).getResultList();
	}

	/**
	 * 
	 * @param fileLocation
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Image3VO> findFiltered(final String fileLocation, final String fileName) throws Exception{

		// Assuming entityManager is properly instantiated and available
		EntityManager entityManager = this.entityManager;
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Image3VO> cq = cb.createQuery(Image3VO.class);
		Root<Image3VO> image = cq.from(Image3VO.class);

		List<Predicate> predicates = new ArrayList<>();

// Applying conditions based on method arguments.
		if (fileLocation != null && !fileLocation.isEmpty()) {
			predicates.add(cb.like(image.get("fileLocation"), fileLocation));
		}

		if (fileName != null && !fileName.isEmpty()) {
			predicates.add(cb.like(image.get("fileName"), fileName));
		}

		cq.where(predicates.toArray(new Predicate[0]));
		cq.orderBy(cb.desc(image.get("imageId")));

// Execute the query
		List<Image3VO> foundEntities = entityManager.createQuery(cq).getResultList();
		return foundEntities;

	}
	
	@SuppressWarnings("unchecked")
	public List<Image3VO> findByDataCollectionId(final Integer dataCollectionId) throws Exception {

		// Assuming entityManager is properly instantiated and available
		EntityManager entityManager = this.entityManager;
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Image3VO> cq = cb.createQuery(Image3VO.class);
		Root<Image3VO> image = cq.from(Image3VO.class);

// Joining with DataCollectionVO
		Join<Image3VO, DataCollection3VO> dataCollection = image.join("dataCollectionVO");

		List<Predicate> predicates = new ArrayList<>();

// Condition based on dataCollectionId
		if (dataCollectionId != null) {
			predicates.add(cb.equal(dataCollection.get("dataCollectionId"), dataCollectionId));
		}

		cq.where(predicates.toArray(new Predicate[0]));
		cq.orderBy(cb.desc(image.get("imageId")));

// Execute the query
		List<Image3VO> foundEntities = entityManager.createQuery(cq).getResultList();
		return foundEntities;
	}
	
	@SuppressWarnings("unchecked")
	public List<Image3VO> findByDataCollectionGroupId(final Integer dataCollectionGroupId) throws Exception {

		// Assuming entityManager is properly instantiated and available
		EntityManager entityManager = this.entityManager;
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Image3VO> cq = cb.createQuery(Image3VO.class);
		Root<Image3VO> image = cq.from(Image3VO.class);

// Joining with DataCollectionVO and then DataCollectionGroupVO
		Join<Image3VO, DataCollection3VO> dataCollection = image.join("dataCollectionVO");
		Join<DataCollection3VO, DataCollectionGroup3VO> dataCollectionGroup = dataCollection.join("dataCollectionGroupVO");

		List<Predicate> predicates = new ArrayList<>();

// Condition based on dataCollectionGroupId
		if (dataCollectionGroupId != null) {
			predicates.add(cb.equal(dataCollectionGroup.get("dataCollectionGroupId"), dataCollectionGroupId));
		}

		cq.where(predicates.toArray(new Predicate[0]));
		cq.orderBy(cb.desc(image.get("imageId")));

// Execute the query
		List<Image3VO> foundEntities = entityManager.createQuery(cq).getResultList();
		return foundEntities;
	}

	
	/**
	 * two variables to guarantee the user fecths only its own images
	 * @param imageId
	 * @param proposalId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Image3VO> findByImageIdAndProposalId (final Integer imageId, final Integer proposalId)throws Exception{

		String query = "SELECT DISTINCT i.imageId, i.dataCollectionId, i.imageNumber, " +
				"i.fileName, i.fileLocation, i.measuredIntensity, i.jpegFileFullPath, i.jpegThumbnailFileFullPath, " +
				"i.temperature, i.cumulativeIntensity, i.synchrotronCurrent, i.comments, i.machineMessage " +
				" FROM Image i, DataCollection dc, DataCollectionGroup dcg, BLSession ses, Proposal pro "
				+ "WHERE  i.imageId  = ?1 AND i.dataCollectionId = dc.dataCollectionId AND " +
				"dc.dataCollectionGroupId = dcg.dataCollectionGroupId AND " +
				"dcg.sessionId = ses.sessionId AND ses.proposalId = pro.proposalId AND pro.proposalId = ?2";
		List<Image3VO> listVOs = this.entityManager.createNativeQuery(query, "imageNativeQuery")
				.setParameter(1, imageId)
				.setParameter(2, proposalId)
				.getResultList();
		if (listVOs == null || listVOs.isEmpty())
			listVOs = null;
		List<Image3VO> foundEntities = listVOs;
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
	private void checkAndCompleteData(Image3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getImageId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getImageId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

}