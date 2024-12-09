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
package ispyb.server.common.services.admin;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.log4j.Logger;

import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.vos.admin.AdminActivity3VO;

/**
 * <p>
 * This session bean handles ISPyB AdminActivity3.
 * </p>
 */
@Stateless
public class AdminActivity3ServiceBean implements AdminActivity3Service, AdminActivity3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(AdminActivity3ServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public AdminActivity3ServiceBean() {
	};

	/**
	 * Create new AdminActivity3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public AdminActivity3VO create(final AdminActivity3VO vo) throws Exception {
		
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the AdminActivity3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public AdminActivity3VO update(final AdminActivity3VO vo) throws Exception {
		
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the AdminActivity3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		AdminActivity3VO vo = findByPk(pk, false, false);
		delete(vo);
	}

	/**
	 * Remove the AdminActivity3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final AdminActivity3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * // Generic HQL request to find instances of AdminActivity3 by pk
	 * @param pk
	 *            the primary key
	 * @return the AdminActivity3 value object
	 */
	public AdminActivity3VO findByPk(final Integer pk, final boolean withLink1, final boolean withLink2) throws Exception {
		checkCreateChangeRemoveAccess();
		try {
			return entityManager.find(AdminActivity3VO.class, pk);
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * loads the vo with all the linked object in eager fetch mode
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public AdminActivity3VO loadEager(AdminActivity3VO vo) throws Exception {
		AdminActivity3VO newVO = this.findByPk(vo.getAdminActivityId(), true, true);
		return newVO;
	}

	// TODO remove following method if not adequate
	/**
	 * Find all AdminActivity3s and set linked value objects if necessary
	 *
	 * 	// Generic HQL request to find all instances of AdminActivity3
	 */
	@SuppressWarnings("unchecked")
	public List<AdminActivity3VO> findAll() throws Exception {

		List<AdminActivity3VO> foundEntities = entityManager.createQuery("SELECT vo from AdminActivity3VO vo ").getResultList();
		return foundEntities;
	}


	/**
	 * Find AdminActivity3s and according to the following parameters
	 * 
	 * @param action
	 */
	@SuppressWarnings("unchecked")
	public List<AdminActivity3VO> findByAction(final String action) throws Exception {

		EntityManager entityManager = this.entityManager;  // Assuming EntityManager is injected or retrieved beforehand
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<AdminActivity3VO> cq = cb.createQuery(AdminActivity3VO.class);
		Root<AdminActivity3VO> root = cq.from(AdminActivity3VO.class);

		if (action != null && !action.isEmpty()) {
			cq.where(cb.equal(root.get("action"), action));
		}

		cq.distinct(true);  // Ensures that the results returned are distinct

// Execute the query
		List<AdminActivity3VO> foundEntities = entityManager.createQuery(cq).getResultList();
		return foundEntities;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AdminActivity3VO> findByUsername(final String username) throws Exception {
		EntityManager entityManager = this.entityManager;  // Assumed to be injected or retrieved somehow
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<AdminActivity3VO> cq = cb.createQuery(AdminActivity3VO.class);
		Root<AdminActivity3VO> root = cq.from(AdminActivity3VO.class);

		if (username != null && !username.isEmpty()) {
			String usernameLower = username.toLowerCase();
			cq.where(cb.equal(cb.lower(root.get("username")), usernameLower));
			cq.orderBy(cb.desc(root.get("action")));
		}

		cq.distinct(true);  // Ensure distinct results are returned

// Execute the query
		List<AdminActivity3VO> foundEntities = entityManager.createQuery(cq).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove AdminActivity3 entities. If not set rollback only
	 * and throw AccessDeniedException
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
	private void checkAndCompleteData(AdminActivity3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getAdminActivityId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getAdminActivityId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}