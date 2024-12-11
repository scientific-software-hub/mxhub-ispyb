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

import ispyb.server.mx.vos.autoproc.AutoProcIntegration3VO;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * <p>
 *  This session bean handles ISPyB AutoProcIntegration3.
 * </p>
 */
@Stateless
public class AutoProcIntegration3ServiceBean implements AutoProcIntegration3Service,
		AutoProcIntegration3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(AutoProcIntegration3ServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public AutoProcIntegration3ServiceBean() {
	};

	/**
	 * Create new AutoProcIntegration3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public AutoProcIntegration3VO create(final AutoProcIntegration3VO vo) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the AutoProcIntegration3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public AutoProcIntegration3VO update(final AutoProcIntegration3VO vo) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the AutoProcIntegration3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		AutoProcIntegration3VO vo = findByPk(pk);
		delete(vo);
	}

	/**
	 * Remove the AutoProcIntegration3
	 * @param vo the entity to remove.
	 */
	public void delete(final AutoProcIntegration3VO vo) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @return the AutoProcIntegration3 value object
	 */
	public AutoProcIntegration3VO findByPk(final Integer pk) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		try{
			return entityManager.find(AutoProcIntegration3VO.class, pk);
		}catch(NoResultException e){
			return null;
		}
	}

	// TODO remove following method if not adequate
	/**
	 * Find all AutoProcIntegration3s and set linked value objects if necessary
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcIntegration3VO> findAll() throws Exception {

		String qlString = "SELECT vo from AutoProcIntegration3VO vo ";
        return entityManager.createQuery(qlString, AutoProcIntegration3VO.class).getResultList();
	}

	/**
	 * 
	 * @param autoProcId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcIntegration3VO> findByAutoProcId(final Integer autoProcId) throws Exception{
		String query = "SELECT DISTINCT * " +
				"FROM AutoProcIntegration api, AutoProcScaling_has_Int aphi, AutoProcScaling aps " +
				"WHERE aps.autoProcId = ?1 AND aps.autoProcScalingId = aphi.autoProcScalingId AND " +
				"api.autoProcIntegrationId= aphi.autoProcIntegrationId ";
		List<AutoProcIntegration3VO> listVOs = this.entityManager.createNativeQuery(query, "autoProcIntegrationNativeQuery")
				.setParameter(1, autoProcId).getResultList();
		return listVOs;
	}

	/**
	 * get the autoProcStatus for a given dataCollectionId and a given processingProgram (fastProc or parallelProc or edna-fastproc)
	 * true if we can find at least one autoProc with XSCALE file
	 * @param dataCollectionId
	 * @param processingProgram
	 * @return
	 * @throws Exception
	 */
	public Boolean getAutoProcStatus(final Integer dataCollectionId, final String processingProgram) throws Exception {
		String query = "SELECT app.processingPrograms, api.autoProcIntegrationId, apppa.fileName " +
				"FROM `AutoProcIntegration` api, AutoProcProgram app, AutoProcProgramAttachment apppa " +
				"WHERE api.dataCollectionId = ?1 AND " +
				"api.autoProcProgramId = app.autoProcProgramId AND " +
				"app.processingPrograms like ?2 AND  " +
				"apppa.autoProcProgramId = app.autoProcProgramId AND " +
				"apppa.fileName like '%XSCALE%' ";
		try{
			List res = this.entityManager.createNativeQuery(query)
					.setParameter(1, dataCollectionId)
					.setParameter(2, processingProgram)
					.getResultList();
			if (res == null || res.isEmpty()){
				return false;
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * get the autoProcStatus for a XIA2_DIALS
	 * @param dataCollectionId
	 * @param processingProgram
	 * @return
	 * @throws Exception
	 */
	public Boolean getAutoProcXia2DialsStatus(final Integer dataCollectionId, final String processingProgram) throws Exception {
		String query = "SELECT app.processingPrograms, api.autoProcIntegrationId, apppa.fileName " +
				"FROM `AutoProcIntegration` api, AutoProcProgram app, AutoProcProgramAttachment apppa " +
				"WHERE api.dataCollectionId = ?1 AND " +
				"api.autoProcProgramId = app.autoProcProgramId AND " +
				"app.processingPrograms like ?2 AND  " +
				"apppa.autoProcProgramId = app.autoProcProgramId AND " +
				"apppa.fileName like 'di%.mtz' ";
		try{
			List res = this.entityManager.createNativeQuery(query)
					.setParameter(1, dataCollectionId)
					.setParameter(2, processingProgram)
					.getResultList();
			if (res == null || res.isEmpty()){
				return false;
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * get the autoProcStatus for a FAST DP
	 * @param dataCollectionId
	 * @param processingProgram
	 * @return
	 * @throws Exception
	 */
	public Boolean getAutoProcFastDPStatus(final Integer dataCollectionId, final String processingProgram) throws Exception {
		String query = "SELECT app.processingPrograms, api.autoProcIntegrationId, apppa.fileName " +
				"FROM `AutoProcIntegration` api, AutoProcProgram app, AutoProcProgramAttachment apppa " +
				"WHERE api.dataCollectionId = ?1 AND " +
				"api.autoProcProgramId = app.autoProcProgramId AND " +
				"app.processingPrograms like ?2 AND  " +
				"apppa.autoProcProgramId = app.autoProcProgramId AND " +
				"apppa.fileName like '%fast_dp%.mtz.gz' ";
		try{
			List res = this.entityManager.createNativeQuery(query)
					.setParameter(1, dataCollectionId)
					.setParameter(2, processingProgram)
					.getResultList();
			if (res == null || res.isEmpty()){
				return false;
			}
			return true;
		}catch(Exception e){
			return false;
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
	private void checkAndCompleteData(AutoProcIntegration3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getAutoProcIntegrationId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getAutoProcIntegrationId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}