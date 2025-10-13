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

import ispyb.server.mx.vos.collections.InputParameterWorkflow;
import ispyb.server.mx.vos.collections.Workflow3VO;

import java.math.BigInteger;
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
 *  This session bean handles ISPyB Workflow3.
 * </p>
 */
@Stateless
public class Workflow3ServiceBean implements Workflow3Service,
		Workflow3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(Workflow3ServiceBean.class);


	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	@Resource
	private SessionContext context;

	public Workflow3ServiceBean() {
	};

	/**
	 * Create new Workflow3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public Workflow3VO create(final Workflow3VO vo) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();

		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the Workflow3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public Workflow3VO update(final Workflow3VO vo) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();

		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the Workflow3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();

		Workflow3VO vo = findByPk(pk);
		// TODO Edit this business code				
		delete(vo);
	}

	/**
	 * Remove the Workflow3
	 * @param vo the entity to remove.
	 */
	public void delete(final Workflow3VO vo) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();

		// TODO Edit this business code
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @return the Workflow3 value object
	 */
	public Workflow3VO findByPk(final Integer pk) throws Exception {

		//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
		//autService.checkUserRightToChangeAdminData();

		// TODO Edit this business code
		try {
			return entityManager
					.find(Workflow3VO.class, pk);
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * Find all Workflow3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Workflow3VO> findAll()
			throws Exception {

		List<Workflow3VO> foundEntities = (List<Workflow3VO>) entityManager.createQuery(
				"from Workflow3VO vo ").getResultList();
		return foundEntities;
	}

	public Integer countWF(final String year, final String beamline, final String workflowType, final String status) throws Exception{
	
		try {
			String query = "SELECT count(w.workflowId) as nbW "+
			"FROM Workflow w, DataCollectionGroup g, BLSession s, Proposal p "+
			"WHERE w.workflowId = g.workflowId  and "+
			"      g.sessionId = s.sessionId and "+
			"      s.proposalId = p.proposalId and "+
			"      s.proposalId = p.proposalId and "+
			"      p.proposalCode != 'opid' and "+
			"      p.proposalId != 1170 and "+
			"     YEAR(g.startTime) = ?1 AND "+
			"     s.beamlineName like ?2 AND " +
			"     w.workflowType = ?3 AND "+
			"     w.status = ?4 ";
			BigInteger ret = (BigInteger)this.entityManager.createNativeQuery(query)
					.setParameter(1, year)
					.setParameter(2, beamline)
					.setParameter(4, status)
					.setParameter(3, workflowType)
					.getSingleResult();
			return ret.intValue();
		} catch (NoResultException e) {
			return null;
		}
		
	}

	
	public List getWorkflowResult(final String year, final String beamline, final String workflowType) throws Exception{
		try {
			String query = "SELECT w.logFilePath  "+
					"FROM Workflow w, DataCollectionGroup g, BLSession s, Proposal p "+
					"WHERE w.workflowId = g.workflowId  and "+
					"      g.sessionId = s.sessionId and "+
					"      s.proposalId = p.proposalId and "+
					"      s.proposalId = p.proposalId and "+
					"      p.proposalCode != 'opid' and "+
					"      p.proposalId != 1170 and "+
					"     YEAR(g.startTime) = ?1 AND "+
					"     s.beamlineName like ?2 AND " +
					"     w.workflowType = ?3 AND "+
					"     w.status = 'Failure' ";
			List ret = this.entityManager.createNativeQuery(query)
					.setParameter(1, year)
					.setParameter(2, beamline)
					.setParameter(3, workflowType).getResultList();
			return ret;
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Workflow3VO> findAllByStatus(final String status) throws Exception{
		try {
			String query = "SELECT vo FROM Workflow3VO vo WHERE vo.status = :status ";
			List<Workflow3VO> ret = this.entityManager.createQuery(query, Workflow3VO.class)
					.setParameter("status", status)
					.getResultList();
			return ret;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<InputParameterWorkflow> findInputParametersByWorkflowId(final int workflowId) throws Exception{
		try {
			String query = "SELECT vo FROM InputParameterWorkflow vo WHERE vo.workflowId= :workflowId ";
			List<Workflow3VO> ret = this.entityManager.createQuery(query, Workflow3VO.class)
					.setParameter("workflowId", workflowId).getResultList();
			List foundEntities = ret;//TODO type safety
			return foundEntities;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	

	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * @param vo the data to check
	 * @param create should be true if the value object is just being created in the DB, this avoids some checks like testing the primary key
	 * @exception VOValidateException if data is not correct
	 */
	private void checkAndCompleteData(Workflow3VO vo, boolean create)
			throws Exception {

		if (create) {
			if (vo.getWorkflowId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getWorkflowId() == null) {
				throw new IllegalArgumentException(
						"Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}
