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

import ispyb.server.mx.vos.autoproc.AutoProcProgramAttachment3VO;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import org.apache.log4j.Logger;

/**
 * <p>
 *  This session bean handles ISPyB AutoProcProgramAttachment3.
 * </p>
 */
@Stateless
public class AutoProcProgramAttachment3ServiceBean implements AutoProcProgramAttachment3Service,
		AutoProcProgramAttachment3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(AutoProcProgramAttachment3ServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public AutoProcProgramAttachment3ServiceBean() {
	};

	/**
	 * Create new AutoProcProgramAttachment3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public AutoProcProgramAttachment3VO create(final AutoProcProgramAttachment3VO vo) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the AutoProcProgramAttachment3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public AutoProcProgramAttachment3VO update(final AutoProcProgramAttachment3VO vo) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the AutoProcProgramAttachment3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		AutoProcProgramAttachment3VO vo = findByPk(pk);
		// TODO Edit this business code				
		delete(vo);
	}

	/**
	 * Remove the AutoProcProgramAttachment3
	 * @param vo the entity to remove.
	 */
	public void delete(final AutoProcProgramAttachment3VO vo) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		entityManager.remove(vo);
	}


	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @return the AutoProcProgramAttachment3 value object
	 */
	public AutoProcProgramAttachment3VO findByPk(final Integer pk) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		try{
			return entityManager.find(AutoProcProgramAttachment3VO.class, pk);
		}catch(NoResultException e){
			return null;
		}
	}

	/**
	 * Find all AutoProcProgramAttachment3s and set linked value objects if necessary
	 */
	public List<AutoProcProgramAttachment3VO> findAll()
			throws Exception {

		String qlString = "SELECT vo from AutoProcProgramAttachment3VO vo ";
        return entityManager.createQuery(qlString, AutoProcProgramAttachment3VO.class).getResultList();
	}

	/**
	 * find xscale files attachments
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcProgramAttachment3VO> findXScale(final Integer autoProcProgramId) throws Exception {
		
		String query = "SELECT * " +
				"FROM AutoProcProgramAttachment  " +
				"WHERE  autoProcProgramId = ?1 AND " +
				"fileName like '%XSCALE%' ";
		try{
			
			List<AutoProcProgramAttachment3VO> listVOs = this.entityManager.createNativeQuery(query, AutoProcProgramAttachment3VO.class)
					.setParameter(1, autoProcProgramId)
					.getResultList();
			return listVOs;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * find noanom correct files attachments
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcProgramAttachment3VO> findNoanomCorrect(final Integer autoProcProgramId) throws Exception {

		String query = "SELECT * " +
				"FROM AutoProcProgramAttachment  " +
				"WHERE  autoProcProgramId = ?1 AND " +
				"fileName like '%_noanom_aimless%' ";
		try{

			List<AutoProcProgramAttachment3VO> listVOs = this.entityManager.createNativeQuery(query, "autoProcProgramAttachmentNativeQuery")
					.setParameter(1, autoProcProgramId)
					.getResultList();
			return listVOs;
		}catch(Exception e){
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
	private void checkAndCompleteData(AutoProcProgramAttachment3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getAutoProcProgramAttachmentId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getAutoProcProgramAttachmentId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

}