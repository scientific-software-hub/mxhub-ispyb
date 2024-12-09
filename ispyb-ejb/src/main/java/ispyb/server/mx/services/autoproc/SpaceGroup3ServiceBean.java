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

import ispyb.server.mx.vos.autoproc.GeometryClassname3VO;
import ispyb.server.mx.vos.autoproc.SpaceGroup3VO;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.criteria.*;

/**
 * <p>
 * This session bean handles ISPyB SpaceGroup3.
 * </p>
 */
@Stateless
public class SpaceGroup3ServiceBean implements SpaceGroup3Service, SpaceGroup3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public SpaceGroup3ServiceBean() {
	};

	/**
	 * Create new SpaceGroup3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public SpaceGroup3VO create(final SpaceGroup3VO vo) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the SpaceGroup3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public SpaceGroup3VO update(final SpaceGroup3VO vo) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the SpaceGroup3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		SpaceGroup3VO vo = findByPk(pk);
		delete(vo);
	}

	/**
	 * Remove the SpaceGroup3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final SpaceGroup3VO vo) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the SpaceGroup3 value object
	 */
	public SpaceGroup3VO findByPk(final Integer pk) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		try{
			return entityManager.find(SpaceGroup3VO.class, pk);
		}catch(NoResultException e){
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<SpaceGroup3VO> findBySpaceGroupShortName(final String currSpaceGroup) throws Exception {

		EntityManager em = this.entityManager; // Assume EntityManager is already initialized
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SpaceGroup3VO> cq = cb.createQuery(SpaceGroup3VO.class);
		Root<SpaceGroup3VO> spaceGroup = cq.from(SpaceGroup3VO.class);

// Adding conditions
		if (currSpaceGroup != null) {
			Predicate likeCondition = cb.like(spaceGroup.get("spaceGroupShortName"), currSpaceGroup);
			cq.where(likeCondition);
		}

// Ensuring distinct results
		cq.select(spaceGroup).distinct(true);

// Execute the query and return the list
		List<SpaceGroup3VO> results = em.createQuery(cq).getResultList();
		return results;

	}

	// TODO remove following method if not adequate
	/**
	 * Find all SpaceGroup3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<SpaceGroup3VO> findAll() throws Exception {

		List<SpaceGroup3VO> foundEntities = entityManager.createQuery("SELECT vo from SpaceGroup3VO vo ").getResultList();
		return foundEntities;
	}

	/**
	 * returns the list of space groups allowed / used in MX (not all the spaceGroup table, only the mxUsed)
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<SpaceGroup3VO> findAllowedSpaceGroups() throws Exception{

		EntityManager em = this.entityManager; // Assume EntityManager is already initialized
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SpaceGroup3VO> cq = cb.createQuery(SpaceGroup3VO.class);
		Root<SpaceGroup3VO> spaceGroup = cq.from(SpaceGroup3VO.class);

// Joining with the geometryClassnameVO
		Join<SpaceGroup3VO, GeometryClassname3VO> geometryClassname = spaceGroup.join("geometryClassnameVO", JoinType.LEFT);

// Adding condition
		cq.where(cb.equal(spaceGroup.get("mxUsed"), 1));

// Ensuring distinct results
		cq.select(spaceGroup).distinct(true);

// Ordering
		cq.orderBy(cb.asc(geometryClassname.get("geometryOrder")), cb.asc(spaceGroup.get("spaceGroupShortName")));

// Execute the query and return the list
		List<SpaceGroup3VO> results = em.createQuery(cq).getResultList();
		return results;

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
	private void checkAndCompleteData(SpaceGroup3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getSpaceGroupId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getSpaceGroupId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
}
