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
package ispyb.server.common.services.shipping;

import java.util.List;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import org.apache.log4j.Logger;


import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.vos.shipping.DewarLocation3VO;

/**
 * <p>
 * This session bean handles ISPyB DewarLocation3.
 * </p>
 */
@Stateless
public class DewarLocation3ServiceBean implements DewarLocation3Service, DewarLocation3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(DewarLocation3ServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public DewarLocation3ServiceBean() {
	};

	/**
	 * Create new DewarLocation3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public DewarLocation3VO create(final DewarLocation3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the DewarLocation3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public DewarLocation3VO update(final DewarLocation3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the DewarLocation3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		DewarLocation3VO vo = findByPk(pk, false, false);
		delete(vo);
	}

	/**
	 * Remove the DewarLocation3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final DewarLocation3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @return the DewarLocation3 value object
	 * 	// Generic HQL request to find instances of DewarLocation3 by pk
	 */
	public DewarLocation3VO findByPk(final Integer pk, final boolean withLink1, final boolean withLink2) throws Exception {
		
		checkCreateChangeRemoveAccess();
		try{
			return entityManager.find(DewarLocation3VO.class, pk);
		}catch(NoResultException e){
			return null;
		}
	}


	/**
	 * Find all DewarLocation3s and set linked value objects if necessary
	 *
	 * 	// Generic HQL request to find all instances of DewarLocation3
	 */
	@SuppressWarnings("unchecked")
	public List<DewarLocation3VO> findAll(final boolean withLink1, final boolean withLink2) throws Exception {

		List<DewarLocation3VO> foundEntities = entityManager.createQuery("select vo from DewarLocation3VO vo ").getResultList();
		return foundEntities;
	}

	// --------------------------------------------------------------------------------------------------------------
	/**
	 * Check if user has access rights to create, change and remove DewarLocation3 entities. If not set rollback only
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
	private void checkAndCompleteData(DewarLocation3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getEventId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getEventId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

}