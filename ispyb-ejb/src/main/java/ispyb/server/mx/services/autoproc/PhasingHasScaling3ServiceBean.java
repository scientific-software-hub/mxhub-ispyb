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

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.log4j.Logger;

import ispyb.server.common.exceptions.AccessDeniedException;

import ispyb.server.mx.vos.autoproc.PhasingHasScaling3VO;

/**
 * <p>
 *  This session bean handles ISPyB PhasingHasScaling3.
 * </p>
 */
@Stateless
public class PhasingHasScaling3ServiceBean implements PhasingHasScaling3Service,PhasingHasScaling3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(PhasingHasScaling3ServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public PhasingHasScaling3ServiceBean() {
	};

	/**
	 * Create new PhasingHasScaling3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public PhasingHasScaling3VO create(final PhasingHasScaling3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the PhasingHasScaling3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public PhasingHasScaling3VO update(final PhasingHasScaling3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the PhasingHasScaling3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		PhasingHasScaling3VO vo = findByPk(pk);			
		delete(vo);
	}

	/**
	 * Remove the PhasingHasScaling3
	 * @param vo the entity to remove.
	 */
	public void delete(final PhasingHasScaling3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @return the PhasingHasScaling3 value object
	 */
	public PhasingHasScaling3VO findByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		try {
			return entityManager.find(PhasingHasScaling3VO.class, pk);
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Find all PhasingHasScaling3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	public List<PhasingHasScaling3VO> findAll()throws Exception {

		String qlString = "SELECT vo from PhasingHasScaling3VO vo ";
        return entityManager.createQuery(qlString, PhasingHasScaling3VO.class).getResultList();
	}

	/**
	 * Check if user has access rights to create, change and remove PhasingHasScaling3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
	}

	@SuppressWarnings("unchecked")
	public List<PhasingHasScaling3VO> findFiltered(final Integer autoProcScalingId)throws Exception {
		// Get the CriteriaBuilder from the EntityManager
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

// Create a CriteriaQuery object for PhasingHasScaling3VO
		CriteriaQuery<PhasingHasScaling3VO> criteriaQuery = criteriaBuilder.createQuery(PhasingHasScaling3VO.class);

// Define the root of the query (the main entity to query from)
		Root<PhasingHasScaling3VO> root = criteriaQuery.from(PhasingHasScaling3VO.class);

// Optionally join related entities and add conditions
		if (autoProcScalingId != null) {
			Predicate condition = criteriaBuilder.equal(root.join("autoProcScalingVO").get("autoProcScalingId"), autoProcScalingId);
			criteriaQuery.where(condition);
			criteriaQuery.orderBy(criteriaBuilder.asc(root.join("autoProcScalingVO").get("autoProcScalingId")));
		}

// Prepare the query to be executed
		criteriaQuery.select(root);

// Execute the query
		List<PhasingHasScaling3VO> result = entityManager.createQuery(criteriaQuery).getResultList();

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<PhasingHasScaling3VO> findByAutoProc(final Integer autoProcId) throws Exception {
		
		String query = "SELECT * "
				+ "FROM Phasing_has_Scaling, AutoProcScaling "
				+ "WHERE Phasing_has_Scaling.autoProcScalingId = AutoProcScaling.autoProcScalingId AND "
				+ "AutoProcScaling.autoProcId = ?1 ";
		List<PhasingHasScaling3VO> listVOs = this.entityManager.createNativeQuery(query, "phasingHasScalingNativeQuery")
				.setParameter(1, autoProcId).getResultList();
		if (listVOs == null || listVOs.isEmpty())
			listVOs = null;
		List<PhasingHasScaling3VO> foundEntities = listVOs;
		return foundEntities;
	}
	
	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * @param vo the data to check
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @exception VOValidateException if data is not correct
	 */
	private void checkAndCompleteData(PhasingHasScaling3VO vo, boolean create)
			throws Exception {

		if (create) {
			if (vo.getPhasingHasScalingId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getPhasingHasScalingId() == null) {
				throw new IllegalArgumentException(
						"Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}