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
package ispyb.server.common.services.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ispyb.server.common.vos.config.MenuGroup3VO;
import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;

import jakarta.persistence.criteria.*;
import org.apache.log4j.Logger;

import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.vos.config.Menu3VO;

/**
 * <p>
 *  This session bean handles ISPyB Menu3.
 * </p>
 */
@Stateless
public class Menu3ServiceBean implements Menu3Service,
		Menu3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Menu3ServiceBean.class);


	@PersistenceContext(unitName = "ispyb_config")
	private EntityManager entityManager;

	@PersistenceUnit(unitName = "ispyb_config")
	private EntityManagerFactory entitymanagerFactory;

	public Menu3ServiceBean() {
	};

	/**
	 * Create new Menu3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public Menu3VO create(final Menu3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the Menu3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public Menu3VO update(final Menu3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the Menu3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		Menu3VO vo = findByPk(pk, false);			
		delete(vo);
		}

	/**
	 * Remove the Menu3
	 * @param vo the entity to remove.
	 */
	public void delete(final Menu3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @return the Menu3 value object
	 *
	 * 	// Generic HQL request to find instances of Menu3 by pk
	 */
	public Menu3VO findByPk(final Integer pk, final boolean withMenuGroup) throws Exception {
		
		checkCreateChangeRemoveAccess();
		try {
			entityManager = entitymanagerFactory.createEntityManager();
			return (Menu3VO) entityManager.createQuery("select vo from Menu3VO vo "
							+ (withMenuGroup ? "left join fetch vo.menuGroupVOs " : "")
							+ "where vo.menuId = :pk")
					.setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			entityManager.close();
		}
	}

	/**
	 * loads the vo with all the linked object in eager fetch mode
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Menu3VO loadEager(Menu3VO vo) throws Exception {
		
		Menu3VO newVO = this.findByPk(vo.getMenuId(), true);
		return newVO;
	}

	// TODO remove following method if not adequate
	/**
	 * Find all Menu3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 *
	 * 	// Generic HQL request to find all instances of Menu3
	 */
	@SuppressWarnings("unchecked")
	public List<Menu3VO> findAll(final boolean withMenuGroup, final boolean detachLight) throws Exception {
		
		entityManager = entitymanagerFactory.createEntityManager();
		try {
			Collection<Menu3VO> foundEntities = entityManager.createQuery("select vo from Menu3VO vo "
							+ (withMenuGroup ? "left join fetch vo.menuGroupVOs " : ""))
					.getResultList();
			List<Menu3VO> vos;
			if (detachLight){
				vos = getLightMenu3VOs(foundEntities);
			}
			else{
				vos = getMenu3VOs(foundEntities);
			}
			return vos;
			
		} finally {
			entityManager.close();
		}
	}	
				
	/**
	 * Check if user has access rights to create, change and remove Menu3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		
				//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
				//autService.checkUserRightToChangeAdminData();
	}

	/**
	 * Get all Menu3 entity VOs from a collection of Menu3 local entities.
	 * @param localEntities
	 * @return
	 */
	private List<Menu3VO> getMenu3VOs(Collection<Menu3VO> entities) {
		
		List<Menu3VO> results = new ArrayList<Menu3VO>(entities.size());
		for (Menu3VO vo : entities) {
			results.add(vo);
		}
		return results;
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private List<Menu3VO> getLightMenu3VOs(Collection<Menu3VO> entities)
			throws CloneNotSupportedException {
		List<Menu3VO> results = new ArrayList<Menu3VO>(entities.size());
		for (Menu3VO vo : entities) {
			Menu3VO otherVO = getLightMenu3VO(vo);
			results.add(otherVO);
		}
		return results;
	}

	/**
	 * Get a clone of an entity witout linked collections
	 * used for webservices
	 * 
	 * @param localEntity
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private Menu3VO getLightMenu3VO(Menu3VO vo)
			throws CloneNotSupportedException {
		Menu3VO otherVO = vo.clone();
		//otherVO.setXxxxxxx(null);
		//otherVO.setYyyyyyy(null);
		return otherVO;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Menu3VO> findFiltered(final Integer parentId, final Integer menuGroupId, final String proposalCode)throws Exception{
		//TODO NPE on this.entityManager
		try(EntityManager em = entitymanagerFactory.createEntityManager()) {
			// Create a CriteriaBuilder instance for creating the CriteriaQuery object
			CriteriaBuilder cb = em.getCriteriaBuilder();

// Create a query object for Menu3VO
			CriteriaQuery<Menu3VO> cq = cb.createQuery(Menu3VO.class);

// Define the root of the query indicating the query is from the Menu3VO entity
			Root<Menu3VO> menuRoot = cq.from(Menu3VO.class);

// Join with the MenuGroupVOs if needed
			Join<Menu3VO, MenuGroup3VO> menuGroupJoin = menuRoot.join("menuGroupVOs", JoinType.LEFT);

// Prepare the conditions (predicates for the criteria query)
			List<Predicate> predicates = new ArrayList<>();

			if (proposalCode != null) {
				predicates.add(
						cb.or(
								cb.equal(menuRoot.get("expType"), "MB"),
								cb.equal(menuRoot.get("expType"), proposalCode)
						)
				);
			}

			if (menuGroupId != null) {
				predicates.add(cb.equal(menuGroupJoin.get("menuGroupId"), menuGroupId));
			}

			if (parentId != null) {
				predicates.add(cb.equal(menuRoot.get("parentId"), parentId));
			}

// Apply the predicates to the query
			cq.select(menuRoot).where(predicates.toArray(new Predicate[0]));

// Order by parentId and sequence
			cq.orderBy(cb.asc(menuRoot.get("parentId")), cb.asc(menuRoot.get("sequence")));

// Create the query and get the results
			List<Menu3VO> vos = em.createQuery(cq).getResultList();

// If there's a method to transform results further
// List<Menu3VO> transformedVos = getMenu3VOs(vos);

			return vos;
			
		}
	}

	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * 
	 * @param vo
	 *            the data to check
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like testing the primary
	 *            key
	 * @exception VOValidateException
	 *                if data is not correct
	 */
	private void checkAndCompleteData(Menu3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getMenuId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getMenuId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
}

