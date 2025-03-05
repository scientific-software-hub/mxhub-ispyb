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
package ispyb.server.mx.services.sample;

import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;

import java.util.ArrayList;
import java.util.List;

import ispyb.server.mx.vos.sample.Protein3VO;
import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;

import jakarta.persistence.criteria.*;
import org.apache.log4j.Logger;

import java.math.BigInteger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.apache.axis.utils.StringUtils;

/**
 * <p>
 * This session bean handles ISPyB Crystal3.
 * </p>
 */
@Stateless
public class Crystal3ServiceBean implements Crystal3Service, Crystal3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Crystal3ServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Resource
	private SessionContext context;

	public Crystal3ServiceBean() {
	};

	/**
	 * Create new Crystal3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Crystal3VO create(final Crystal3VO vo) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the Crystal3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Crystal3VO update(final Crystal3VO vo) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the Crystal3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		Crystal3VO vo = findByPk(pk, false);
		// TODO Edit this business code
		delete(vo);
	}


	/**
	 * Remove the Crystal3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Crystal3VO vo) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		entityManager.remove(entityManager.merge(vo));
	}


	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param fetchSamples
	 * @return the Crystal3 value object
	 */
	public Crystal3VO findByPk(final Integer pk, final boolean fetchSamples) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		try {
			String qlString = "SELECT DISTINCT(vo) from Crystal3VO vo "
					+ (fetchSamples ? "left join fetch vo.sampleVOs " : "")
					+ "where vo.crystalId = :pk";
			return (Crystal3VO) entityManager.createQuery(qlString)
					.setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Crystal3VO loadEager(Crystal3VO vo) throws Exception {
		Crystal3VO newVO = this.findByPk(vo.getCrystalId(), true);
		return newVO;
	}

	// TODO remove following method if not adequate
	/**
	 * Find all Crystal3s and set linked value objects if necessary
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<Crystal3VO> findAll(final boolean withLink1, final boolean withLink2) throws Exception {

		List<Crystal3VO> foundEntities = entityManager.createQuery("SELECT vo from Crystal3VO vo ").getResultList();
		return foundEntities;
	}

	@SuppressWarnings("unchecked")
	public List<Crystal3VO> findFiltered(final Integer proposalId, final Integer proteinId, final String acronym,
			final String spaceGroup) throws Exception {

		// Assuming entityManager is properly instantiated and available
		EntityManager entityManager = this.entityManager;
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Crystal3VO> cq = cb.createQuery(Crystal3VO.class);
		Root<Crystal3VO> crystal = cq.from(Crystal3VO.class);

// Joining with ProteinVO and ProposalVO (nested join)
		Join<Crystal3VO, Protein3VO> protein = crystal.join("proteinVO");
		Join<Protein3VO, Proposal3VO> proposal = protein.join("proposalVO", JoinType.LEFT); // Optional join

		List<Predicate> predicates = new ArrayList<>();

// Conditions on Crystal3VO
		if (spaceGroup != null && !spaceGroup.isEmpty()) {
			predicates.add(cb.like(crystal.get("spaceGroup"), spaceGroup));
		}

// Conditions on ProteinVO
		if (acronym != null && !acronym.isEmpty()) {
			predicates.add(cb.like(cb.upper(protein.get("acronym")), acronym.toUpperCase()));
		}

		if (proteinId != null) {
			predicates.add(cb.equal(protein.get("proteinId"), proteinId));
		}

// Condition on ProposalVO
		if (proposalId != null) {
			predicates.add(cb.equal(proposal.get("proposalId"), proposalId));
		}

		cq.where(predicates.toArray(new Predicate[0]));
		cq.orderBy(cb.asc(protein.get("acronym")), cb.desc(crystal.get("crystalId")));
		cq.distinct(true);

// Fetch associated Structure3VOs
		crystal.fetch("structure3VOs", JoinType.LEFT);

// Execute the query
		List<Crystal3VO> foundEntities = entityManager.createQuery(cq).getResultList();
		return foundEntities;
	}

	public List<Crystal3VO> findByProposalId(final Integer proposalId) throws Exception {
		return this.findFiltered(proposalId, null, null, null);
	}

	@Override
	public List<Crystal3VO> findByProteinId(Integer proteinId) throws Exception {
		return this.findFiltered(null, proteinId, null, null);
	}

	@SuppressWarnings("unchecked")
	public Crystal3VO findByAcronymAndCellParam(final String acronym, final Crystal3VO currentCrystal,
			final Integer proposalId) throws Exception {
		
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		
		List<Crystal3VO> list = (List<Crystal3VO>) template.execute(new EJBAccessCallback() {
			// Assuming entityManager is properly instantiated and available
			final EntityManager entityManager = Crystal3ServiceBean.this.entityManager;
			final CriteriaBuilder cb = entityManager.getCriteriaBuilder();


			// Helper method to handle nullability and equality of cell parameters
			private void addCellParameterPredicate(List<Predicate> predicates, Root<Crystal3VO> crystal, String attributeName, Double attributeValue) {
				if (attributeValue != null) {
					predicates.add(cb.equal(crystal.get(attributeName), attributeValue));
				} else {
					predicates.add(cb.isNull(crystal.get(attributeName)));
				}
			}

			public Object doInEJBAccess(Object parent) throws Exception {


				CriteriaQuery<Crystal3VO> cq = cb.createQuery(Crystal3VO.class);
				Root<Crystal3VO> crystal = cq.from(Crystal3VO.class);

// Joining with ProteinVO
				Join<Crystal3VO, Protein3VO> protein = crystal.join("proteinVO");
// Conditional join with ProposalVO
				Join<Protein3VO, Proposal3VO> proposal = protein.join("proposalVO", JoinType.LEFT);

				List<Predicate> predicates = new ArrayList<>();

// Conditions on the acronym of the ProteinVO
				if (acronym != null && !acronym.isEmpty()) {
					predicates.add(cb.like(cb.upper(protein.get("acronym")), acronym.toUpperCase()));
				}

// Condition on proposalId of the ProposalVO
				if (proposalId != null) {
					predicates.add(cb.equal(proposal.get("proposalId"), proposalId));
				}

// Additional conditions on Crystal3VO fields
				if (currentCrystal.getSpaceGroup() != null) {
					if (!StringUtils.isEmpty(currentCrystal.getSpaceGroup())) {
						predicates.add(cb.like(crystal.get("spaceGroup"), currentCrystal.getSpaceGroup()));
					} else {
						predicates.add(cb.isNull(crystal.get("spaceGroup")));
					}
				}

// Conditions for cell parameters
				addCellParameterPredicate(predicates, crystal, "cellA", currentCrystal.getCellA());
				addCellParameterPredicate(predicates, crystal, "cellB", currentCrystal.getCellB());
				addCellParameterPredicate(predicates, crystal, "cellC", currentCrystal.getCellC());
				addCellParameterPredicate(predicates, crystal, "cellAlpha", currentCrystal.getCellAlpha());
				addCellParameterPredicate(predicates, crystal, "cellBeta", currentCrystal.getCellBeta());
				addCellParameterPredicate(predicates, crystal, "cellGamma", currentCrystal.getCellGamma());

// Apply all predicates
				cq.where(predicates.toArray(new Predicate[0]));
				cq.orderBy(cb.desc(crystal.get("crystalId")));
				cq.distinct(true);

// Execute the query
				List<Crystal3VO> foundEntities = entityManager.createQuery(cq).getResultList();
				return foundEntities;


			}
		});
			
		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}


	public Integer countSamples(final Integer crystalId)throws Exception{

		String countSample = "SELECT count(DISTINCT(bls.blSampleId)) samplesNumber "
				+ "FROM Crystal c LEFT JOIN BLSample bls ON bls.crystalId = c.crystalId "
				+ "WHERE c.crystalId = ?1 ";
		Query query = entityManager.createNativeQuery(countSample)
				.setParameter(1, crystalId);
		try{
			BigInteger res = (BigInteger) query.getSingleResult();
			return res.intValue();
		}catch(NoResultException e){
			System.out.println("ERROR in countSamples - NoResultException: "+crystalId);
			e.printStackTrace();
			return 0;
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * returns the pdb full path for a given acronym/spaceGroup
	 * @param proteinAcronym
	 * @param spaceGroup
	 * @return
	 * @throws Exception
	 */
	public String findPdbFullPath(final String proteinAcronym, final String spaceGroup) throws Exception{
		EntityManager entityManager = this.entityManager;
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Crystal3VO> cq = cb.createQuery(Crystal3VO.class);
		Root<Crystal3VO> crystalRoot = cq.from(Crystal3VO.class);

		List<Predicate> predicates = new ArrayList<>();

// Condition on spaceGroup, assuming spaceGroup is not null when proteinAcronym is checked
		if (proteinAcronym != null && !spaceGroup.isEmpty()) {
			predicates.add(cb.like(crystalRoot.get("spaceGroup"), spaceGroup));
		}

// Joining with ProteinVO
		Join<Crystal3VO, Protein3VO> proteinJoin = crystalRoot.join("proteinVO");

// Condition on the acronym of the ProteinVO
		if (proteinAcronym != null && !proteinAcronym.isEmpty()) {
			predicates.add(cb.like(cb.upper(proteinJoin.get("acronym")), proteinAcronym.toUpperCase()));
		}

// Apply all predicates
		cq.where(predicates.toArray(new Predicate[0]));
		cq.orderBy(cb.asc(proteinJoin.get("acronym")), cb.desc(crystalRoot.get("crystalId")));
		cq.distinct(true);

// Execute the query
		List<Crystal3VO> foundEntities = entityManager.createQuery(cq).getResultList();
		String fileFullPath = "";
		if (foundEntities != null && !foundEntities.isEmpty()) {
			Crystal3VO crystal = foundEntities.get(0);
			if (crystal.getPdbFilePath() != null && crystal.getPdbFileName() != null) {
				fileFullPath = crystal.getPdbFilePath() + crystal.getPdbFileName();
			}
		}
		return fileFullPath;
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
	private void checkAndCompleteData(Crystal3VO vo, boolean create) throws Exception {

//		if (create) {
//			if (vo.getCrystalId() != null) {
//				throw new IllegalArgumentException(
//						"Primary key is already set! This must be done automatically. Please, set it to null!");
//			}
//		} else {
//			if (vo.getCrystalId() == null) {
//				throw new IllegalArgumentException("Primary key is not set for update!");
//			}
//		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
	
}