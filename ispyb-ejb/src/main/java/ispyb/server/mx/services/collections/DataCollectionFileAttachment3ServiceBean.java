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

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import ispyb.server.mx.vos.collections.DataCollectionFileAttachment3VO;

	

/**
 * <p>
 * This session bean handles ISPyB DataCollectionFileAttachment.
 * </p>
 */
@Stateless
public class DataCollectionFileAttachment3ServiceBean implements DataCollectionFileAttachment3Service, DataCollectionFileAttachment3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(DataCollectionFileAttachment3ServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	public DataCollectionFileAttachment3ServiceBean() {
	};


	@Override
	public DataCollectionFileAttachment3VO merge(DataCollectionFileAttachment3VO detachedInstance) {
		try {
			DataCollectionFileAttachment3VO result = entityManager.merge(detachedInstance);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	/**
	 * Create new DataCollectionFileAttachment3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public DataCollectionFileAttachment3VO create(final DataCollectionFileAttachment3VO vo)  {
		
		return this.merge(vo);
	}


	/**
	 * Remove the DataCollectionFileAttachment3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		DataCollectionFileAttachment3VO vo = findByPk(pk);

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		delete(vo);
	}

	/**
	 * Remove the DataCollectionFileAttachment3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final DataCollectionFileAttachment3VO vo) throws Exception {
		entityManager.remove(vo);
	}

	/**
	 * <p>
	 * Returns the DataCollectionFileAttachment3VO instance matching the given primary key.
	 * </p>
	 * <p>
	 * <u>Please note</u> that the booleans to fetch relationships are needed <u>ONLY</u> if the value object has to be
	 * used out the EJB container.
	 * </p>
	 * 
	 * @param pk
	 *            the primary key of the object to load.
	 * @param fetchRelation1
	 *            if true, the linked instances by the relation "relation1" will be set.
	 */
	public DataCollectionFileAttachment3VO findByPk(Integer pk) {
		try {
			String qlString = "SELECT vo from DataCollectionFileAttachment3VO vo "
					+ "where vo.dataCollectionFileAttachmentId = :pk";
			return entityManager.createQuery(qlString,DataCollectionFileAttachment3VO.class)
					.setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * find a DataCollectionFileAttachment with a sessionId
	 */
	public DataCollectionFileAttachment3VO findDataCollectionFileAttachmentByDataCollectionId(Integer dcId) {
		String query = "SELECT vo from DataCollectionFileAttachment3VO vo where vo.dataCollectionVO.dataCollectionId = :dcId order by vo.dataCollectionFileAttachmentId desc";
		try {
			return this.entityManager.createQuery(query, DataCollectionFileAttachment3VO.class)
					.setParameter("dcId", dcId).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
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
	private void checkAndCompleteData(DataCollectionFileAttachment3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getDataCollectionFileAttachmentId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getDataCollectionFileAttachmentId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

	@Override
	public DataCollectionFileAttachment3VO update(DataCollectionFileAttachment3VO vo) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("DataCollectionFileAttachment3VO.update is not supported");
	}

}