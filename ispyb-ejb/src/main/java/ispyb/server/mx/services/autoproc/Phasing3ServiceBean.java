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

import jakarta.persistence.criteria.*;

import ispyb.server.common.exceptions.AccessDeniedException;

import ispyb.server.mx.vos.autoproc.Phasing3VO;

/**
 * <p>
 *  This session bean handles ISPyB Phasing3.
 * </p>
 */
@Stateless
public class Phasing3ServiceBean implements Phasing3Service,Phasing3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public Phasing3ServiceBean() {
	};

	/**
	 * Create new Phasing3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public Phasing3VO create(final Phasing3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the Phasing3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public Phasing3VO update(final Phasing3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the Phasing3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		Phasing3VO vo = findByPk(pk);
		delete(vo);
	}

	/**
	 * Remove the Phasing3
	 * @param vo the entity to remove.
	 */
	public void delete(final Phasing3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}
	
	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @return the Phasing3 value object
	 */
	public Phasing3VO findByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		try {
			return entityManager.find(Phasing3VO.class, pk);
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Find all Phasing3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Phasing3VO> findAll()throws Exception {

		List<Phasing3VO> foundEntities = entityManager.createQuery("SELECT vo from Phasing3VO vo ").getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove Phasing3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
	
		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
	}

	@SuppressWarnings("unchecked")
	public List<Phasing3VO> findFiltered(final Integer phasingAnalysisId) throws Exception {

		EntityManager em = this.entityManager; // Assuming EntityManager is already provided
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Phasing3VO> cq = cb.createQuery(Phasing3VO.class);
		Root<Phasing3VO> root = cq.from(Phasing3VO.class);

// Establishing the join to navigate to the related PhasingAnalysisVO entity
		Join<Phasing3VO, PhasingAnalysis3VO> phasingAnalysisJoin = root.join("phasingAnalysisVO", JoinType.INNER);

// Applying conditions
		if (phasingAnalysisId != null) {
			cq.where(cb.equal(phasingAnalysisJoin.get("phasingAnalysisId"), phasingAnalysisId));
			cq.orderBy(cb.asc(phasingAnalysisJoin.get("phasingAnalysisId")));
		}

// Selecting distinct results
		cq.select(root).distinct(true);

// Execute the query and return the results
		List<Phasing3VO> foundEntities = em.createQuery(cq).getResultList();
		return foundEntities;

	}

	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * @param vo the data to check
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @exception VOValidateException if data is not correct
	 */
	private void checkAndCompleteData(Phasing3VO vo, boolean create)
			throws Exception {

		if (create) {
			if (vo.getPhasingId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getPhasingId() == null) {
				throw new IllegalArgumentException(
						"Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}
