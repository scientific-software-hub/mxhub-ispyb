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

import java.util.ArrayList;
import java.util.List;

import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScaling3VO;
import ispyb.server.mx.vos.autoproc.PhasingHasScaling3VO;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.criteria.*;

import ispyb.server.common.exceptions.AccessDeniedException;

import ispyb.server.mx.vos.autoproc.PhasingStatistics3VO;

/**
 * <p>
 *  This session bean handles ISPyB PhasingStatistics3.
 * </p>
 */
@Stateless
public class PhasingStatistics3ServiceBean implements PhasingStatistics3Service,PhasingStatistics3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public PhasingStatistics3ServiceBean() {
	};

	/**
	 * Create new PhasingStatistics3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public PhasingStatistics3VO create(final PhasingStatistics3VO vo) throws Exception {

				checkCreateChangeRemoveAccess();
				this.checkAndCompleteData(vo, true);
				this.entityManager.persist(vo);
				return vo;
	}


	/**
	 * Update the PhasingStatistics3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public PhasingStatistics3VO update(final PhasingStatistics3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the PhasingStatistics3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		PhasingStatistics3VO vo = findByPk(pk);
		delete(vo);
	}

	/**
	 * Remove the PhasingStatistics3
	 * @param vo the entity to remove.
	 */
	public void delete(final PhasingStatistics3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @return the PhasingStatistics3 value object
	 */
	public PhasingStatistics3VO findByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		try {
			return entityManager.find(PhasingStatistics3VO.class, pk);
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Find all PhasingStatistics3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<PhasingStatistics3VO> findAll()throws Exception {

		List<PhasingStatistics3VO> foundEntities = entityManager.createQuery(
				"SELECT vo from PhasingStatistics3VO vo ").getResultList();
		return foundEntities;
	}
	
	/**
	 * Check if user has access rights to create, change and remove PhasingStatistics3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
	
				//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
				//autService.checkUserRightToChangeAdminData();
	}

	/**
	 * Find all PhasingStatistics3 related to a given autoProc
	 */
	@SuppressWarnings("unchecked")
	public List<PhasingStatistics3VO> findFiltered(final Integer autoProcId, final String metric, final Boolean withOverall)throws Exception{

		EntityManager em = this.entityManager; // Assuming EntityManager is already provided
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PhasingStatistics3VO> cq = cb.createQuery(PhasingStatistics3VO.class);
		Root<PhasingStatistics3VO> root = cq.from(PhasingStatistics3VO.class);

// Establishing the joins with the nested entities
		Join<PhasingStatistics3VO, PhasingHasScaling3VO> phasingHasScalingJoin = root.join("phasingHasScaling1VO");
		Join<PhasingHasScaling3VO, AutoProcScaling3VO> autoProcScalingJoin = phasingHasScalingJoin.join("autoProcScalingVO");
		Join<AutoProcScaling3VO, AutoProc3VO> autoProcJoin = autoProcScalingJoin.join("autoProcVO");

// Applying conditions
		List<Predicate> predicates = new ArrayList<>();

		if (autoProcId != null) {
			predicates.add(cb.equal(autoProcJoin.get("autoProcId"), autoProcId));
		}

		if (metric != null && !metric.trim().isEmpty()) {
			predicates.add(cb.equal(root.get("metric"), metric));
		}

		if (withOverall != null && !withOverall) {
			predicates.add(cb.notEqual(root.get("binNumber"), 999));
		}

		cq.where(cb.and(predicates.toArray(new Predicate[0])));

// Ordering
		cq.orderBy(cb.desc(root.get("phasingStatisticsId")));

// Selecting distinct results
		cq.select(root).distinct(true);

// Execute the query and return the results
		List<PhasingStatistics3VO> foundEntities = em.createQuery(cq).getResultList();
		return foundEntities;

	}

	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * @param vo the data to check
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @exception VOValidateException if data is not correct
	 */
	private void checkAndCompleteData(PhasingStatistics3VO vo, boolean create)
			throws Exception {

		if (create) {
			if (vo.getPhasingStatisticsId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getPhasingStatisticsId() == null) {
				throw new IllegalArgumentException(
						"Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}
