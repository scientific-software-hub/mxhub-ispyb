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

import org.apache.log4j.Logger;

import ispyb.server.mx.vos.autoproc.GeometryClassname3VO;

/**
 * <p>
 *  This session bean handles ISPyB GeometryClassname3.
 * </p>
 */
@Stateless
public class GeometryClassname3ServiceBean implements GeometryClassname3Service, GeometryClassname3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(GeometryClassname3ServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public GeometryClassname3ServiceBean() {
	};

	/**
	 * Create new GeometryClassname3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public GeometryClassname3VO create(final GeometryClassname3VO vo) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the GeometryClassname3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public GeometryClassname3VO update(final GeometryClassname3VO vo) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the GeometryClassname3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		GeometryClassname3VO vo = findByPk(pk, false);
		delete(vo);
	}

	/**
	 * Remove the GeometryClassname3
	 * @param vo the entity to remove.
	 */
	public void delete(final GeometryClassname3VO vo) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @return the GeometryClassname3 value object
	 */
	public GeometryClassname3VO findByPk(final Integer pk, final boolean fetchSpaceGroups) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		try {
			return entityManager
					.createQuery("SELECT DISTINCT(vo) from GeometryClassname3VO vo "
							+ (fetchSpaceGroups ? "left join fetch vo.spaceGroupVOs " : "")
							+ "where vo.geometryClassnameId = :pk", GeometryClassname3VO.class)
					.setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * Find all GeometryClassname3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	public List<GeometryClassname3VO> findAll(final boolean fetchSpaceGroups)
			throws Exception {

        return entityManager.createQuery("SELECT vo from GeometryClassname3VO vo "
				+ (fetchSpaceGroups ? "left join fetch vo.spaceGroupVOs " : "")
				+ " ORDER BY vo.geometryOrder", GeometryClassname3VO.class)
				.getResultList();
	}

	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * @param vo the data to check
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @exception VOValidateException if data is not correct
	 */
	private void checkAndCompleteData(GeometryClassname3VO vo, boolean create)
			throws Exception {

		if (create) {
			if (vo.getGeometryClassnameId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getGeometryClassnameId() == null) {
				throw new IllegalArgumentException(
						"Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

}