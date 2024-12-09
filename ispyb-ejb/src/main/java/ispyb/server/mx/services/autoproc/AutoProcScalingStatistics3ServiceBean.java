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

import java.util.Iterator;
import java.util.List;

import ispyb.server.mx.vos.autoproc.AutoProc3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScaling3VO;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import ispyb.server.common.exceptions.AccessDeniedException;

import ispyb.server.mx.vos.autoproc.AutoProcScalingStatistics3VO;

/**
 * <p>
 * This session bean handles ISPyB AutoProcScalingStatistics3.
 * </p>
 */
@Stateless
public class AutoProcScalingStatistics3ServiceBean implements AutoProcScalingStatistics3Service,
		AutoProcScalingStatistics3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public AutoProcScalingStatistics3ServiceBean() {
	};

	/**
	 * Create new AutoProcScalingStatistics3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public AutoProcScalingStatistics3VO create(final AutoProcScalingStatistics3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the AutoProcScalingStatistics3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public AutoProcScalingStatistics3VO update(final AutoProcScalingStatistics3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the AutoProcScalingStatistics3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		AutoProcScalingStatistics3VO vo = findByPk(pk);
		delete(vo);
	}

	/**
	 * Remove the AutoProcScalingStatistics3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final AutoProcScalingStatistics3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the AutoProcScalingStatistics3 value object
	 */
	public AutoProcScalingStatistics3VO findByPk(final Integer pk)
			throws Exception {

		checkCreateChangeRemoveAccess();
		try{
			return entityManager.find(AutoProcScalingStatistics3VO.class, pk);
		}catch(NoResultException e){
			return null;
		}
	}

	/**
	 * Find all AutoProcScalingStatistics3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcScalingStatistics3VO> findAll()
			throws Exception {

		List<AutoProcScalingStatistics3VO> foundEntities = entityManager.createQuery("SELECT vo from AutoProcScalingStatistics3VO vo ").getResultList();
		return foundEntities;
	}
	
	@SuppressWarnings("unchecked")
	public List<AutoProcScalingStatistics3VO> findByAutoProcId(final Integer autoProcId, final String string)
			throws Exception {

		EntityManager em = this.entityManager; // Ensure your EntityManager is injected or created as needed

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<AutoProcScalingStatistics3VO> cq = cb.createQuery(AutoProcScalingStatistics3VO.class);
		Root<AutoProcScalingStatistics3VO> root = cq.from(AutoProcScalingStatistics3VO.class);

// Joining with AutoProcScalingVO and AutoProcVO conditionally if autoProcId is provided
		if (autoProcId != null) {
			Join<AutoProcScalingStatistics3VO, AutoProcScaling3VO> autoProcScalingJoin = root.join("autoProcScalingVO", JoinType.INNER);
			Join<AutoProcScaling3VO, AutoProc3VO> autoProcJoin = autoProcScalingJoin.join("autoProcVO", JoinType.INNER);
			cq.where(cb.equal(autoProcJoin.get("autoProcId"), autoProcId));
		}

// Applying filter on the 'scalingStatisticsType' if string is not null
		if (string != null) {
			cq.where(cb.like(root.get("scalingStatisticsType"), string));
		}

// Setting distinct true to emulate Criteria.DISTINCT_ROOT_ENTITY
		cq.select(root).distinct(true);

// Preparing and executing the query
		TypedQuery<AutoProcScalingStatistics3VO> query = em.createQuery(cq);
		List<AutoProcScalingStatistics3VO> foundEntities = query.getResultList();
		return foundEntities;

	}

	public AutoProcScalingStatistics3VO getBestAutoProcScalingStatistic(List<AutoProcScalingStatistics3VO> values) {
		AutoProcScalingStatistics3VO answer = null;
		for (Iterator<AutoProcScalingStatistics3VO> k = values.iterator(); k.hasNext();) {
			AutoProcScalingStatistics3VO currStatistic = (AutoProcScalingStatistics3VO) k.next();
			if (answer == null ) { // sort by rMerge
				answer = currStatistic;
			}else if (currStatistic.getRmerge() != null && currStatistic.getRmerge() < answer.getRmerge()){
				answer = currStatistic;
			}
		}
		return answer;
	}

	/**
	 * Check if user has access rights to create, change and remove AutoProcScalingStatistics3 entities. If not set
	 * rollback only and throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {

				// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
				// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
				// to the one checking the needed access rights
				// autService.checkUserRightToChangeAdminData();
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
	private void checkAndCompleteData(AutoProcScalingStatistics3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getAutoProcScalingStatisticsId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getAutoProcScalingStatisticsId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

}