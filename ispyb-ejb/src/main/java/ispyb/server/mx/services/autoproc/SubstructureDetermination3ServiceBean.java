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

import ispyb.server.mx.vos.autoproc.PhasingAnalysis3VO;
import ispyb.server.mx.vos.autoproc.SubstructureDetermination3VO;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.criteria.*;

/**
 * <p>
 *  This session bean handles ISPyB SubstructureDetermination3.
 * </p>
 */
@Stateless
public class SubstructureDetermination3ServiceBean implements SubstructureDetermination3Service,
		SubstructureDetermination3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public SubstructureDetermination3ServiceBean() {
	};

	/**
	 * Create new SubstructureDetermination3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public SubstructureDetermination3VO create(final SubstructureDetermination3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}
	
	/**
	 * Update the SubstructureDetermination3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public SubstructureDetermination3VO update(final SubstructureDetermination3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the SubstructureDetermination3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		SubstructureDetermination3VO vo = findByPk(pk);
		delete(vo);
	}

	/**
	 * Remove the SubstructureDetermination3
	 * @param vo the entity to remove.
	 */
	public void delete(final SubstructureDetermination3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @return the SubstructureDetermination3 value object
	 */
	public SubstructureDetermination3VO findByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		try {
			return entityManager.find(SubstructureDetermination3VO.class, pk);
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Find all SubstructureDetermination3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<SubstructureDetermination3VO> findAll()
			throws Exception {

		List<SubstructureDetermination3VO> foundEntities = entityManager.createQuery(
				"SELECT vo from SubstructureDetermination3VO vo ").getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove SubstructureDetermination3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {

				//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
				//autService.checkUserRightToChangeAdminData();
	}

	@SuppressWarnings("unchecked")
	public List<SubstructureDetermination3VO> findFiltered(final Integer phasingAnalysisId) throws Exception {

		EntityManager entityManager = this.entityManager;
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SubstructureDetermination3VO> cq = cb.createQuery(SubstructureDetermination3VO.class);
		Root<SubstructureDetermination3VO> root = cq.from(SubstructureDetermination3VO.class);

		if (phasingAnalysisId != null) {
			// Create a join with PhasingAnalysisVO
			Join<SubstructureDetermination3VO, PhasingAnalysis3VO> phasingAnalysisJoin = root.join("phasingAnalysisVO");
			// Add condition on phasingAnalysisId
			Predicate condition = cb.equal(phasingAnalysisJoin.get("phasingAnalysisId"), phasingAnalysisId);
			cq.where(condition);
			// Order by phasingAnalysisId
			cq.orderBy(cb.asc(phasingAnalysisJoin.get("phasingAnalysisId")));
		}

		cq.distinct(true);  // Ensure distinct results

// Execute the query
		List<SubstructureDetermination3VO> foundEntities = entityManager.createQuery(cq).getResultList();
		return foundEntities;
	}

	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * @param vo the data to check
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @exception VOValidateException if data is not correct
	 */
	private void checkAndCompleteData(SubstructureDetermination3VO vo, boolean create)
			throws Exception {

		if (create) {
			if (vo.getSubstructureDeterminationId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getSubstructureDeterminationId() == null) {
				throw new IllegalArgumentException(
						"Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}