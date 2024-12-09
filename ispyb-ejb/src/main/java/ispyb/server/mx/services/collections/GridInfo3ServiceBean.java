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

import ispyb.server.mx.vos.collections.GridInfo3VO;

import java.util.List;

import ispyb.server.mx.vos.collections.WorkflowMesh3VO;
import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

/**
 * <p>
 *  This session bean handles ISPyB GridInfo3.
 * </p>
 */
@Stateless
public class GridInfo3ServiceBean implements GridInfo3Service,
		GridInfo3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	public GridInfo3ServiceBean() {
	};

	/**
	 * Create new GridInfo3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public GridInfo3VO create(final GridInfo3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the GridInfo3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public GridInfo3VO update(final GridInfo3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the GridInfo3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		GridInfo3VO vo = findByPk(pk);
		delete(vo);
	}


	/**
	 * Remove the GridInfo3
	 * @param vo the entity to remove.
	 */
	public void delete(final GridInfo3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the GridInfo3 value object
	 */
	public GridInfo3VO findByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		try {
			return entityManager.find(GridInfo3VO.class, pk);
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Find all GridInfo3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<GridInfo3VO> findAll()
			throws Exception {

		List<GridInfo3VO> foundEntities = (List<GridInfo3VO>) entityManager.createQuery(
				"SELECT vo from GridInfo3VO vo ").getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove GridInfo3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
	
		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
	}
	
	/**
	 * find the grid info for a specified workflow
	 * @param workflowId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<GridInfo3VO> findByWorkflowMeshId(final Integer workflowMeshId) throws Exception{

		EntityManager entityManager = this.entityManager;  // Assuming EntityManager is injected or retrieved beforehand
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<GridInfo3VO> cq = cb.createQuery(GridInfo3VO.class);
		Root<GridInfo3VO> root = cq.from(GridInfo3VO.class);

		if (workflowMeshId != null) {
			Join<GridInfo3VO, WorkflowMesh3VO> workflowMeshJoin = root.join("workflowMeshVO");
			cq.where(cb.equal(workflowMeshJoin.get("workflowMeshId"), workflowMeshId));
		}

		cq.distinct(true);  // Ensures that the results returned are distinct

// Execute the query
		List<GridInfo3VO> foundEntities = entityManager.createQuery(cq).getResultList();
		return foundEntities;
	}


	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * @param vo the data to check
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @exception VOValidateException if data is not correct
	 */
	private void checkAndCompleteData(GridInfo3VO vo, boolean create)
			throws Exception {

		if (create) {
			if (vo.getGridInfoId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getGridInfoId() == null) {
				throw new IllegalArgumentException(
						"Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	

}
