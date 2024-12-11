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
package ispyb.server.mx.services.autoproc;

import java.util.List;

import ispyb.server.mx.vos.autoproc.PhasingProgramRun3VO;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.criteria.*;

import ispyb.server.common.exceptions.AccessDeniedException;

import ispyb.server.mx.vos.autoproc.PhasingProgramAttachment3VO;

/**
 * <p>
 *  This session bean handles ISPyB PhasingProgramAttachment3.
 * </p>
 */
@Stateless
public class PhasingProgramAttachment3ServiceBean implements PhasingProgramAttachment3Service,PhasingProgramAttachment3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public PhasingProgramAttachment3ServiceBean() {
	};

	/**
	 * Create new PhasingProgramAttachment3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public PhasingProgramAttachment3VO create(final PhasingProgramAttachment3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the PhasingProgramAttachment3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public PhasingProgramAttachment3VO update(final PhasingProgramAttachment3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the PhasingProgramAttachment3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		PhasingProgramAttachment3VO vo = findByPk(pk);
		delete(vo);
	}

	/**
	 * Remove the PhasingProgramAttachment3
	 * @param vo the entity to remove.
	 */
	public void delete(final PhasingProgramAttachment3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @return the PhasingProgramAttachment3 value object
	 */
	public PhasingProgramAttachment3VO findByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		try {
			return entityManager.find(PhasingProgramAttachment3VO.class, pk);
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * Find all PhasingProgramAttachment3s and set linked value objects if necessary
	 */
	@SuppressWarnings("unchecked")
	public List<PhasingProgramAttachment3VO> findAll()throws Exception {

		List<PhasingProgramAttachment3VO> foundEntities = entityManager.createQuery("SELECT vo from PhasingProgramAttachment3VO vo ").getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove PhasingProgramAttachment3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
	
		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
	}

	@SuppressWarnings("unchecked")
	public List<PhasingProgramAttachment3VO> findFiltered(final Integer phasingProgramRunId)throws Exception {

		EntityManager em = this.entityManager; // Assuming EntityManager is already provided
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PhasingProgramAttachment3VO> cq = cb.createQuery(PhasingProgramAttachment3VO.class);
		Root<PhasingProgramAttachment3VO> root = cq.from(PhasingProgramAttachment3VO.class);

// Joining with PhasingProgramRunVO
		Join<PhasingProgramAttachment3VO, PhasingProgramRun3VO> phasingProgramRun = root.join("phasingProgramRunVO");

// Conditional check
		if (phasingProgramRunId != null) {
			Predicate condition = cb.equal(phasingProgramRun.get("phasingProgramRunId"), phasingProgramRunId);
			cq.where(condition);
			cq.orderBy(cb.asc(phasingProgramRun.get("phasingProgramRunId")));
		}

// Selecting the root ensures distinct results implicitly and fetches the entities
		cq.select(root);

// Execute the query and return the results
		List<PhasingProgramAttachment3VO> foundEntities = em.createQuery(cq).getResultList();
		return foundEntities;

	}

	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * @param vo the data to check
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @exception VOValidateException if data is not correct
	 */
	private void checkAndCompleteData(PhasingProgramAttachment3VO vo, boolean create)
			throws Exception {

		if (create) {
			if (vo.getPhasingProgramAttachmentId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getPhasingProgramAttachmentId() == null) {
				throw new IllegalArgumentException(
						"Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}