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

import ispyb.server.mx.vos.autoproc.PhasingAnalysis3VO;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.mx.vos.autoproc.PreparePhasingData3VO;

/**
 * <p>
 *  This session bean handles ISPyB PreparePhasingData3.
 * </p>
 */
@Stateless
public class PreparePhasingData3ServiceBean implements PreparePhasingData3Service,
		PreparePhasingData3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public PreparePhasingData3ServiceBean() {
	};

	/**
	 * Create new PreparePhasingData3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public PreparePhasingData3VO create(final PreparePhasingData3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the PreparePhasingData3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public PreparePhasingData3VO update(final PreparePhasingData3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the PreparePhasingData3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		PreparePhasingData3VO vo = findByPk(pk);
		delete(vo);
	}

	/**
	 * Remove the PreparePhasingData3
	 * @param vo the entity to remove.
	 */
	public void delete(final PreparePhasingData3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @return the PreparePhasingData3 value object
	 */
	public PreparePhasingData3VO findByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		try {
			return entityManager.find(PreparePhasingData3VO.class, pk);
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Find all PreparePhasingData3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<PreparePhasingData3VO> findAll()
			throws Exception {

		List<PreparePhasingData3VO> foundEntities = entityManager.createQuery("SELECT vo from PreparePhasingData3VO vo ").getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove PreparePhasingData3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {

				//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
				//autService.checkUserRightToChangeAdminData();
		
	}

	@SuppressWarnings("unchecked")
	public List<PreparePhasingData3VO> findFiltered(final Integer phasingAnalysisId) throws Exception {

		EntityManager em = this.entityManager; // Make sure your EntityManager is properly initialized

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PreparePhasingData3VO> cq = cb.createQuery(PreparePhasingData3VO.class);
		Root<PreparePhasingData3VO> root = cq.from(PreparePhasingData3VO.class);

// Joining with PhasingAnalysisVO if phasingAnalysisId is provided
		if (phasingAnalysisId != null) {
			Join<PreparePhasingData3VO, PhasingAnalysis3VO> phasingAnalysisJoin = root.join("phasingAnalysisVO", JoinType.INNER);
			Predicate phasingAnalysisIdCondition = cb.equal(phasingAnalysisJoin.get("phasingAnalysisId"), phasingAnalysisId);
			cq.where(phasingAnalysisIdCondition);
			cq.orderBy(cb.asc(phasingAnalysisJoin.get("phasingAnalysisId")));
		}

// Execute the query and get the results
		TypedQuery<PreparePhasingData3VO> query = em.createQuery(cq);
		List<PreparePhasingData3VO> foundEntities = query.getResultList();
		return foundEntities;
	}

	
	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * @param vo the data to check
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @exception VOValidateException if data is not correct
	 */
	private void checkAndCompleteData(PreparePhasingData3VO vo, boolean create)
			throws Exception {

		if (create) {
			if (vo.getPreparePhasingDataId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getPreparePhasingDataId() == null) {
				throw new IllegalArgumentException(
						"Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}