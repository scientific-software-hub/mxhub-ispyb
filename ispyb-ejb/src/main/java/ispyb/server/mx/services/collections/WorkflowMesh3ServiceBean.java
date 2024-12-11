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

import ispyb.server.mx.vos.collections.Workflow3VO;
import ispyb.server.mx.vos.collections.WorkflowMesh3VO;

import java.util.List;

import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.criteria.*;

/**
 * <p>
 *  This session bean handles ISPyB WorkflowMesh3.
 * </p>
 */
@Stateless
public class WorkflowMesh3ServiceBean implements WorkflowMesh3Service,
		WorkflowMesh3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	@Resource
	private SessionContext context;

	public WorkflowMesh3ServiceBean() {
	};

	/**
	 * Create new WorkflowMesh3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public WorkflowMesh3VO create(final WorkflowMesh3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the WorkflowMesh3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public WorkflowMesh3VO update(final WorkflowMesh3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the WorkflowMesh3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		WorkflowMesh3VO vo = findByPk(pk);
		delete(vo);
	}

	/**
	 * Remove the WorkflowMesh3
	 * @param vo the entity to remove.
	 */
	public void delete(final WorkflowMesh3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the WorkflowMesh3 value object
	 */
	public WorkflowMesh3VO findByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		try {
			return entityManager.find(WorkflowMesh3VO.class, pk);
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Find all WorkflowMesh3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<WorkflowMesh3VO> findAll()throws Exception {

		List<WorkflowMesh3VO> foundEntities = (List<WorkflowMesh3VO>) entityManager.createQuery(
				"SELECT vo from WorkflowMesh3VO vo ").getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove WorkflowMesh3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
	
		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
	}

	/**
	 * find the workflow mesh for a specified workflow
	 * @param workflowId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<WorkflowMesh3VO> findByWorkflowId(final Integer workflowId) throws Exception{

		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();

		CriteriaQuery<WorkflowMesh3VO> cq = cb.createQuery(WorkflowMesh3VO.class);
		Root<WorkflowMesh3VO> workflowMeshRoot = cq.from(WorkflowMesh3VO.class);

// Join to workflowVO if workflowId is provided
		if (workflowId != null) {
			Join<WorkflowMesh3VO, Workflow3VO> workflowJoin = workflowMeshRoot.join("workflowVO", JoinType.INNER);
			cq.where(cb.equal(workflowJoin.get("workflowId"), workflowId));
		}

		cq.orderBy(cb.asc(workflowMeshRoot.get("workflowMeshId")));

// Execute the query
		List<WorkflowMesh3VO> foundEntities = this.entityManager.createQuery(cq).getResultList();
		return foundEntities;
	}
	

	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * @param vo the data to check
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @exception VOValidateException if data is not correct
	 */
	private void checkAndCompleteData(WorkflowMesh3VO vo, boolean create)
			throws Exception {

		if (create) {
			if (vo.getWorkflowMeshId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getWorkflowMeshId() == null) {
				throw new IllegalArgumentException(
						"Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
}
