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
package ispyb.server.mx.services.collections;

import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;

import ispyb.server.mx.vos.collections.IspybCrystalClass3VO;

import java.util.List;

import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * <p>
 *  This session bean handles ISPyB IspybCrystalClass3.
 * </p>
 */
@Stateless
public class IspybCrystalClass3ServiceBean implements IspybCrystalClass3Service,
		IspybCrystalClass3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(IspybCrystalClass3ServiceBean.class);
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	public IspybCrystalClass3ServiceBean() {
	};

	/**
	 * Create new IspybCrystalClass3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public IspybCrystalClass3VO create(final IspybCrystalClass3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the IspybCrystalClass3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public IspybCrystalClass3VO update(final IspybCrystalClass3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the IspybCrystalClass3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		IspybCrystalClass3VO vo = findByPk(pk);
		delete(vo);
	}

	/**
	 * Remove the IspybCrystalClass3
	 * @param vo the entity to remove.
	 */
	public void delete(final IspybCrystalClass3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the IspybCrystalClass3 value object
	 */
	public IspybCrystalClass3VO findByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		try{
			return entityManager.find(IspybCrystalClass3VO.class, pk);
		}catch(NoResultException e){
			return null;
		}
	}

	/**
	 * Find all IspybCrystalClass3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<IspybCrystalClass3VO> findAll()
			throws Exception {

		List<IspybCrystalClass3VO> foundEntities = entityManager.createQuery("SELECT vo from IspybCrystalClass3VO vo ").getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove IspybCrystalClass3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		template.execute(new EJBAccessCallback() {

			public Object doInEJBAccess(Object parent) throws Exception {
				//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
				//autService.checkUserRightToChangeAdminData();
				return null;
			}

		});
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
	private void checkAndCompleteData(IspybCrystalClass3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getCrystalClassId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getCrystalClassId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

}