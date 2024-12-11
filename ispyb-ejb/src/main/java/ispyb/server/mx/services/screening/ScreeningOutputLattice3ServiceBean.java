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
package ispyb.server.mx.services.screening;

import ispyb.server.mx.vos.collections.DataCollection3VO;
import ispyb.server.mx.vos.screening.Screening3VO;
import ispyb.server.mx.vos.screening.ScreeningOutput3VO;
import ispyb.server.mx.vos.screening.ScreeningOutputLattice3VO;
import ispyb.server.mx.vos.screening.ScreeningOutputLatticeWS3VO;

import java.util.List;

import jakarta.annotation.Resource;

import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

/**
 * <p>
 * This session bean handles ISPyB ScreeningOutputLattice3.
 * </p>
 */
@Stateless
public class ScreeningOutputLattice3ServiceBean implements ScreeningOutputLattice3Service,
		ScreeningOutputLattice3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	public ScreeningOutputLattice3ServiceBean() {
	};

	/**
	 * Create new ScreeningOutputLattice3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public ScreeningOutputLattice3VO create(final ScreeningOutputLattice3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the ScreeningOutputLattice3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public ScreeningOutputLattice3VO update(final ScreeningOutputLattice3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the ScreeningOutputLattice3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
	
		checkCreateChangeRemoveAccess();
		ScreeningOutputLattice3VO vo = findByPk(pk);
		delete(vo);
	}

	/**
	 * Remove the ScreeningOutputLattice3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final ScreeningOutputLattice3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}
		
	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the ScreeningOutputLattice3 value object
	 */
	public ScreeningOutputLattice3VO findByPk(final Integer pk)
			throws Exception {

		checkCreateChangeRemoveAccess();
		try{
			return entityManager.find(ScreeningOutputLattice3VO.class, pk);
		}catch(NoResultException e){
			return null;
		}
	}

	/**
	 * Find all ScreeningOutputLattice3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<ScreeningOutputLattice3VO> findAll() throws Exception {

		List<ScreeningOutputLattice3VO> foundEntities = entityManager.createQuery("SELECT vo from ScreeningOutputLattice3VO vo ").getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove ScreeningOutputLattice3 entities. If not set
	 * rollback only and throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
	
		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
	}
	
	@SuppressWarnings("unchecked")
	public List<ScreeningOutputLattice3VO> findFiltered(final Integer dataCollectionId) throws Exception{

		// Assume entityManager is already injected or created
		EntityManager entityManager = this.entityManager;

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ScreeningOutputLattice3VO> cq = cb.createQuery(ScreeningOutputLattice3VO.class);
		Root<ScreeningOutputLattice3VO> root = cq.from(ScreeningOutputLattice3VO.class);

// Setup joins through the entity graph
		Join<ScreeningOutputLattice3VO, ScreeningOutput3VO> joinScreeningOutput = root.join("screeningOutputVO");
		Join<ScreeningOutput3VO, Screening3VO> joinScreening = joinScreeningOutput.join("screeningVO");
		Join<Screening3VO, DataCollection3VO> joinDataCollection = joinScreening.join("dataCollectionVO");

// Applying condition
		if (dataCollectionId != null) {
			cq.where(cb.equal(joinDataCollection.get("dataCollectionId"), dataCollectionId));
		}

// Adding order
		cq.orderBy(cb.desc(root.get("screeningOutputLatticeId")));

// Perform query
		List<ScreeningOutputLattice3VO> foundEntities = entityManager.createQuery(cq).getResultList();
		return foundEntities;
	}
	
	
	public ScreeningOutputLatticeWS3VO findByDataCollectionId(final Integer dataCollectionId) throws Exception{
		List<ScreeningOutputLattice3VO> list = findFiltered(dataCollectionId);
		if (list == null || list.size() == 0)
			return null;
		
		return getWSScreeningOutputLatticeVO(list.get(0));
	}

	
	private ScreeningOutputLatticeWS3VO getWSScreeningOutputLatticeVO(ScreeningOutputLattice3VO vo) throws CloneNotSupportedException {
		ScreeningOutputLattice3VO otherVO = getLightScreeningOutputLattice3VO(vo);
		Integer screeningOutputId = null;
		screeningOutputId = otherVO.getScreeningOutputVOId();
		otherVO.setScreeningOutputVO(null);
		ScreeningOutputLatticeWS3VO wsScreeningOutputLattice = new ScreeningOutputLatticeWS3VO(otherVO);
		wsScreeningOutputLattice.setScreeningOutputId(screeningOutputId);
		return wsScreeningOutputLattice;
	}
	
	private ScreeningOutputLattice3VO getLightScreeningOutputLattice3VO(ScreeningOutputLattice3VO vo) throws CloneNotSupportedException {
		ScreeningOutputLattice3VO otherVO = (ScreeningOutputLattice3VO) vo.clone();
		// otherVO.set(null);
		return otherVO;
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
	private void checkAndCompleteData(ScreeningOutputLattice3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getScreeningOutputLatticeId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getScreeningOutputLatticeId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}
