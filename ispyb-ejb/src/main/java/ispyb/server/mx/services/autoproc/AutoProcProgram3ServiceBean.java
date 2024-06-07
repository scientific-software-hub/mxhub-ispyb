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

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;

import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.vos.autoproc.AutoProcProgram3VO;
import ispyb.server.mx.vos.collections.DataCollection3VO;

import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.apache.log4j.Logger;

/**
 * <p>
 *  This session bean handles ISPyB AutoProcProgram3.
 * </p>
 */
@Stateless
public class AutoProcProgram3ServiceBean implements AutoProcProgram3Service,
		AutoProcProgram3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(AutoProcProgram3ServiceBean.class);

	// Generic HQL request to find instances of AutoProcProgram3 by pk
	// TODO choose between left/inner join

	// Generic HQL request to find all instances of AutoProcProgram3
	// TODO choose between left/inner join

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public AutoProcProgram3ServiceBean() {
	};

	/**
	 * Create new AutoProcProgram3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public AutoProcProgram3VO create(final AutoProcProgram3VO vo) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the AutoProcProgram3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public AutoProcProgram3VO update(final AutoProcProgram3VO vo) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the AutoProcProgram3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		AutoProcProgram3VO vo = findByPk(pk, false);
		delete(vo);
	}

	/**
	 * Remove the AutoProcProgram3
	 * @param vo the entity to remove.
	 */
	public void delete(final AutoProcProgram3VO vo) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the AutoProcProgram3 value object
	 */
	public AutoProcProgram3VO findByPk(final Integer pk, final boolean withAttachment
			) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		try{
			String qlString = "SELECT vo from AutoProcProgram3VO vo "
					+ (withAttachment ? "left join fetch vo.attachmentVOs " : "")
					+ "where vo.autoProcProgramId = :pk";
			return entityManager.createQuery(qlString, AutoProcProgram3VO.class)
					.setParameter("pk", pk)
					.getSingleResult();
			}catch(NoResultException e){
				return null;
			}
	}

	// TODO remove following method if not adequate
	/**
	 * Find all AutoProcProgram3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	public List<AutoProcProgram3VO> findAll(final boolean withAttachment)
			throws Exception {

		String qlString = "SELECT vo from AutoProcProgram3VO vo "
				+ (withAttachment ? "left join fetch vo.attachmentVOs " : "");
        return entityManager.createQuery(qlString, AutoProcProgram3VO.class).getResultList();
	}

	/**
	 * Find all dataCollection linked to this autoProcProgramId
	 * @param autoProcProgramId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<DataCollection3VO> findCollects(final Integer autoProcProgramId) throws Exception{
		try{
			String findCollectAp = "SELECT c.dataCollectionId "
			+ "FROM DataCollection c, AutoProcIntegration api, AutoProcScaling_has_Int apshi, AutoProcScaling aps, AutoProc ap "
			+ " WHERE c.dataCollectionId = api.dataCollectionId AND  "
			+ "api.autoProcIntegrationId =  apshi.autoProcIntegrationId AND "
			+ "apshi.autoProcScalingId =  aps.autoProcScalingId AND "
			+ "aps.autoProcId =  ap.autoProcId AND "
			+ "ap.autoProcProgramId = ?1 "
			+ " ORDER BY c.dataCollectionId ASC ";
			Query query1 = entityManager.createNativeQuery(findCollectAp)
					.setParameter(1, autoProcProgramId);
			List resultList1 = query1.getResultList();

			String findCollectApi = "SELECT c.dataCollectionId "
			+ "FROM DataCollection c, AutoProcIntegration api "
			+ " WHERE c.dataCollectionId = api.dataCollectionId AND  "
			+ "api.autoProcProgramId = ?1 "
			+ " ORDER BY c.dataCollectionId ASC ";
			Query query2 = entityManager.createNativeQuery(findCollectApi)
					.setParameter(1, autoProcProgramId);
			List resultList2 = query2.getResultList();
			
			List results = new ArrayList();
			if (resultList1 != null){
				results.addAll(resultList1);
			}
			if (resultList2 != null){
				results.addAll(resultList2);
			}
			
			List foundEntities =  results;
			
			List<DataCollection3VO> listVOs = null;
			
			if (foundEntities != null ){
				int nb = foundEntities.size();
				if (nb > 0){
					listVOs = new ArrayList<DataCollection3VO>();
				}
				Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
				DataCollection3Service dataCollectionService = (DataCollection3Service) ejb3ServiceLocator.getLocalService(DataCollection3Service.class);
				for (int i = 0; i < nb; i++) {
					Integer dataCollectionId = (Integer) foundEntities.get(i);
					if (dataCollectionId != null){
						DataCollection3VO vo = dataCollectionService.findByPk(dataCollectionId, false, false);
						listVOs.add(vo);
					}
				}
			
			}return listVOs;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
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
	private void checkAndCompleteData(AutoProcProgram3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getAutoProcProgramId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getAutoProcProgramId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
}
