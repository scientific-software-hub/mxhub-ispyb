/*******************************************************************************
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
 ******************************************************************************************************************************/

package ispyb.server.common.services.proposals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.ejb.*;
import jakarta.jws.WebMethod;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.log4j.Logger;

import ispyb.server.biosaxs.services.sql.SqlTableMapper;
import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.services.AuthorisationServiceLocal;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.proposals.ProposalWS3VO;

/**
 * <p>
 * This session bean handles Proposal table.
 * </p>
 * 
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@TransactionAttribute(value=TransactionAttributeType.NEVER)
public class Proposal3ServiceBean implements Proposal3Service, Proposal3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	private final static Logger LOG = Logger.getLogger(Proposal3ServiceBean.class);
	
	@EJB
	private AuthorisationServiceLocal autService;

	@Resource
	private SessionContext context;

	public Proposal3ServiceBean() {
	};

	/**
	 * Create new Proposal.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Proposal3VO create(final Proposal3VO vo) throws Exception {
			entityManager.persist(vo);
			return vo;
	}

	/**
	 * Update the Proposal data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Proposal3VO update(final Proposal3VO vo) throws Exception {
		checkChangeRemoveAccess(vo);
		entityManager.merge(vo);
		return vo;

	}

	/**
	 * Remove the Proposal from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		Proposal3VO vo = this.findByPk(pk, false, false, false);
		checkChangeRemoveAccess(vo);
		entityManager.remove(vo);
	}

	/**
	 * Remove the Proposal
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Proposal3VO vo) throws Exception {
		checkChangeRemoveAccess(vo);
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	@WebMethod
	public Proposal3VO findByPk(final Integer pk) throws Exception {

		Proposal3VO result = entityManager.find(Proposal3VO.class, pk);
		checkChangeRemoveAccess(result);
		return result;
	}
	
	@WebMethod
	public Proposal3VO findWithParticipantsByPk(final Integer pk) throws Exception {

		Query query = entityManager.createQuery("SELECT vo FROM Proposal3VO vo " +
						"LEFT JOIN FETCH vo.participants WHERE vo.proposalId = :pk", Proposal3VO.class)
				.setParameter("pk", pk);
		Proposal3VO result = (Proposal3VO) query.getSingleResult();

		checkChangeRemoveAccess( result);
		return result;
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	@WebMethod
	public Proposal3VO findByPk(final Integer pk, final boolean fetchSessions, final boolean fetchProteins,
			final boolean fetchShippings) throws Exception {

		String qlString = "SELECT vo FROM Proposal3VO vo  "
				+ (fetchSessions ? " LEFT JOIN FETCH vo.sessionVOs " : "")
				+ (fetchShippings ? " LEFT JOIN FETCH vo.shippingVOs " : "")
				+ (fetchProteins ? " LEFT JOIN FETCH vo.proteinVOs " : "")
				+ "WHERE vo.proposalId = :pk";
		Query query = entityManager.createQuery(qlString, Proposal3VO.class)
				.setParameter("pk", pk);
		Proposal3VO result = (Proposal3VO) query.getSingleResult();
		
		checkChangeRemoveAccess( result);
		return result;
	}

	@SuppressWarnings("unchecked")
	@WebMethod
	public List<Proposal3VO> findByCodeAndNumber(final String code, final String number, final boolean fetchSessions,
			final boolean fetchProteins, final boolean detachLight) throws Exception {

		Query query;
		if (number == null) {
			String qlString = "SELECT vo from Proposal3VO vo"
					+ (fetchSessions ? " left join fetch vo.sessionVOs " : "")
					+ (fetchProteins ? " left join fetch vo.proteinVOs " : "")
					+ " where vo.proposalCode LIKE :code ";
			query = entityManager.createQuery(qlString)
					.setParameter("code", code);
		} else {
			String qlString = "SELECT vo from Proposal3VO vo"
					+ (fetchSessions ? " left join fetch vo.sessionVOs " : "")
					+ (fetchProteins ? " left join fetch vo.proteinVOs " : "")
					+ " where vo.proposalCode LIKE :code AND vo.proposalNumber = :number ";
			query = entityManager.createQuery(qlString)
					.setParameter("code", code)
					.setParameter("number", number);
		}

		List<Proposal3VO> foundEntities = query.getResultList();
		List<Proposal3VO> vos;
		if (detachLight)
				vos = getLightProposalVOs(foundEntities);
		else
				vos = getProposalVOs(foundEntities);
		return vos;

	}
	
	@SuppressWarnings("unchecked")
	@WebMethod
	public List<Proposal3VO> findByLoginName(final String loginName) throws Exception {

		String queryPerson = "SELECT person FROM Person3VO person WHERE person.login=:loginName";
		Query EJBQueryPerson = this.entityManager.createQuery(queryPerson, Proposal3VO.class)
				.setParameter("loginName", loginName);
		List<Person3VO> persons = EJBQueryPerson.getResultList();
		List<Proposal3VO> proposals = new ArrayList<Proposal3VO>();
		for (Person3VO person3vo : persons) {
			if (person3vo.getProposalVOs() != null) {
				proposals.addAll(person3vo.getProposalVOs());
				proposals.addAll(person3vo.getProposalDirectVOs());
			}
		}
		return proposals;
	}

	public ProposalWS3VO findForWSByCodeAndNumber(final String code, final String number) throws Exception {
		List<Proposal3VO> foundEntities = findByCodeAndNumber(code, number, false, false, true);
		ProposalWS3VO[] ret = getWSProposalVOs(foundEntities);
		if(ret != null && ret.length > 0)
			return ret[0];
		return null;
	}

	private ProposalWS3VO[] getWSProposalVOs(List<Proposal3VO> entities) throws CloneNotSupportedException {
		if (entities == null)
			return null;
		ArrayList<ProposalWS3VO> results = new ArrayList<ProposalWS3VO>(entities.size());
		for (Proposal3VO vo : entities) {
			ProposalWS3VO otherVO = getWSProposalVO(vo);
			results.add(otherVO);
		}
		ProposalWS3VO[] tmpResults = new ProposalWS3VO[results.size()];
		return (ProposalWS3VO[]) results.toArray(tmpResults);
	}

	private ProposalWS3VO getWSProposalVO(Proposal3VO vo) throws CloneNotSupportedException {
		Proposal3VO otherVO = getLightProposalVO(vo);
		Integer personId = null;
		personId = otherVO.getPersonVOId();
		otherVO.setPersonVO(null);
		ProposalWS3VO wsProposal = new ProposalWS3VO(otherVO);
		wsProposal.setPersonId(personId);
		return wsProposal;
	}

	/**
	 * Find all Scientists and set linked value objects if necessary
	 * 
	 */
	@SuppressWarnings("unchecked")
	@WebMethod
	public List<Proposal3VO> findAll(final boolean detachLight) throws Exception {

		Query query = entityManager.createQuery("from Proposal3VO vo ");
		List<Proposal3VO> vos;
		List<Proposal3VO> foundEntities = query.getResultList();
		if (detachLight)
			vos = getLightProposalVOs(foundEntities);
		else
			vos = getProposalVOs(foundEntities);
		return vos;

	}

	/**
	 * Finds a Proposal by its code and number and title (if title is null only search by code and number).
	 * 
	 * @param code
	 * @param number
	 * @param title
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@WebMethod
	public List<Proposal3VO> findFiltered(final String code, final String number, final String title) throws Exception {
		// Get the CriteriaBuilder from the EntityManager
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

// Create a CriteriaQuery object for Proposal3VO
		CriteriaQuery<Proposal3VO> criteriaQuery = criteriaBuilder.createQuery(Proposal3VO.class);

// Define the root of the query (the main entity to query from)
		Root<Proposal3VO> root = criteriaQuery.from(Proposal3VO.class);

// List to hold Predicate objects for query conditions
		List<Predicate> predicates = new ArrayList<>();

// Add conditions based on method parameters
		if (code != null && !code.isEmpty()) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("code")), "%" + code.toUpperCase() + "%"));
		}
		if (number != null && !number.isEmpty()) {
			predicates.add(criteriaBuilder.equal(root.get("number"), number));
		}
		if (title != null && !title.isEmpty()) {
			predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
		}

// Apply the predicates to the CriteriaQuery
		criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

// Make sure results are distinct
		criteriaQuery.distinct(true);

// Order the results
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("proposalId")));

// Prepare the query to be executed
		List<Proposal3VO> result = entityManager.createQuery(criteriaQuery).getResultList();

		return result;
	}

	public Integer[] updateProposalFromIds(final Integer newProposalId, final Integer oldProposalId) throws Exception {
		Integer[] nbUpdated = new Integer[3];
		String sqlUpdateBLSession = "UPDATE BLSession SET proposalId = ?1  WHERE proposalId = ?2";
		Query query = entityManager.createNativeQuery(sqlUpdateBLSession)
				.setParameter(1, newProposalId)
				.setParameter(2, oldProposalId);
		nbUpdated[0] = query.executeUpdate();

		String sqlUpdateShipping = "UPDATE Shipping  SET proposalId = ?1 WHERE proposalId = ?2";
		query = entityManager.createNativeQuery(sqlUpdateShipping)
				.setParameter(1, newProposalId)
				.setParameter(2, oldProposalId);
		nbUpdated[1] = query.executeUpdate();

		String sqlUpdateProtein = "UPDATE Protein  SET proposalId = ?1 WHERE proposalId = ?2";
		query = entityManager.createNativeQuery(sqlUpdateProtein)
				.setParameter(1, newProposalId)
				.setParameter(2, oldProposalId);
		nbUpdated[2] = query.executeUpdate();

		return nbUpdated;
	}

	public Integer[] updateProposalFromDesc(String newPropDesc, String oldPropDesc) throws Exception {
		String code = newPropDesc.substring(0, 2);
		String number = newPropDesc.substring(2);
		Integer newPropId = this.findByCodeAndNumber(code, number, false, false, false).get(0).getProposalId();

		Integer oldPropId = this
				.findByCodeAndNumber(oldPropDesc.substring(0, 2), oldPropDesc.substring(2), false, false,
						false).get(0).getProposalId();
		return this.updateProposalFromIds(newPropId, oldPropId);

	}
	
	
	@Override
	public Proposal3VO findProposalById(int proposalId) {
		return entityManager.find(Proposal3VO.class, proposalId);
	}
	
	@Override
	public List<Proposal3VO> findProposalByLoginName(String loginName) {

		String userName = loginName;
		List<Proposal3VO> proposals = new ArrayList<Proposal3VO>();
		/**
		 * If user is a proposal it is linked by proposalCode and proposalNumber in the proposal table
		 */
		//TODO here is the bug
		proposals.addAll(this.findProposalByCodeAndNumber(userName));

		/**
		 * In case login name is a user we look for it on Persons though proposalHasPerson
		 */
		proposals.addAll(this.findProposalByPerson(loginName));

		/**
		 * Removing duplicated proposals
		 */
		List<Proposal3VO> result = new ArrayList<Proposal3VO>();
		HashSet<Integer> proposalsId = new HashSet<Integer>();
		for (Proposal3VO proposal : proposals) {
			if (!proposalsId.contains(proposal.getProposalId())) {
				result.add(proposal);
				proposalsId.add(proposal.getProposalId());
			}
		}		
		return result;
	}

	@Override
	public List<String> findProposalNamesByLoginName(String loginName) {
		List<Proposal3VO> proposals = this.findProposalByLoginName(loginName);
		return this.getProposalAccounts(proposals);
	}

	@SuppressWarnings("unchecked")
	private List<Proposal3VO> findProposalByCodeAndNumber(String codeAndNumber) {
		String query = "SELECT proposal FROM Proposal3VO proposal WHERE concat(proposal.proposalCode, proposal.proposalNumber)=:codeAndNumber";
		Query EJBQuery = this.entityManager.createQuery(query, Proposal3VO.class);
		EJBQuery.setParameter("codeAndNumber", codeAndNumber);
		return EJBQuery.getResultList();
	}

	@SuppressWarnings("unchecked")
	private List<Proposal3VO> findProposalByPerson(String loginName) {
		String queryPerson = "SELECT person FROM Person3VO person WHERE person.login=:loginName";
		Query EJBQueryPerson = this.entityManager.createQuery(queryPerson, Proposal3VO.class);
		EJBQueryPerson.setParameter("loginName", loginName);
		@SuppressWarnings("unchecked")
		List<Person3VO> persons = EJBQueryPerson.getResultList();
		List<Proposal3VO> proposals = new ArrayList<Proposal3VO>();
		if (persons != null) {
			if (persons.size() > 0) {
				for (Person3VO person3vo : persons) {
					if (person3vo.getProposalVOs() != null) {
						if (person3vo.getProposalVOs().size() > 0) {
							proposals.addAll(person3vo.getProposalVOs());
						}
						if (person3vo.getProposalDirectVOs().size() > 0) {
							proposals.addAll(person3vo.getProposalDirectVOs());
						}
					}
				}
			}
		}
		return proposals;
	}

	/*    coming from SaxsProposalService  */
	
	
	/**
	 * It looks for proposal based on the login name It looks for proposal in: - Proposal table concatenating proposalCode and
	 * proposalNumber - Person table y the column login Then both systems, by proposal and by user are allowed
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Proposal3VO> findAllProposals() {
		String query = "SELECT proposal FROM Proposal3VO proposal";
		Query EJBQuery = this.entityManager.createQuery(query, Proposal3VO.class);
		return EJBQuery.getResultList();
	}
	
	@Override
	public List<Map<String, Object>> findProposals() {
		Query query = this.entityManager.createNativeQuery("select " + SqlTableMapper.getProposalTable() + " from Proposal", Map.class);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> findProposals(String loginName) {
		List<Proposal3VO> proposals = this.findProposalByLoginName(loginName);
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		for (Proposal3VO proposal : proposals) {
			result.addAll(findProposalByProposalId(proposal.getProposalId()));
		}
		return 	result;
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> findProposalById(Integer proposalId) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		result.addAll(findProposalByProposalId(proposalId));
		return 	result;
	}
	
	private List<String> getProposalAccounts(List<Proposal3VO> proposals) {		
		List<String> proposalsStr = new ArrayList<String>();
		if (proposals != null) {
			for (Iterator<Proposal3VO> iterator = proposals.iterator(); iterator.hasNext();) {
				Proposal3VO proposal3vo = (Proposal3VO) iterator.next();
				proposalsStr.add(proposal3vo.getProposalAccount());
			}
		}
		return proposalsStr;
	}

	
	private List<Map<String, Object>> findProposalByProposalId(Integer proposalId){
		String sqlQuery = "SELECT " + SqlTableMapper.getProposalTable()
				+ " from Proposal where proposalId= ?1";
		Query query = this.entityManager.createNativeQuery(sqlQuery, Map.class)
				.setParameter(1, proposalId);
		return query.getResultList();
	}
	
	/*    end of coming from SaxsProposalService  */	
	

	/**
	 * Get all entity VOs from a collection of local entities.
	 * 
	 * @param localEntities
	 * @return
	 */
	private List<Proposal3VO> getProposalVOs(List<Proposal3VO> entities) {
		List<Proposal3VO> results = new ArrayList<Proposal3VO>(entities.size());
		for (Proposal3VO vo : entities) {
			vo.getProteinVOs();
			results.add(vo);
		}
		return results;
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private List<Proposal3VO> getLightProposalVOs(List<Proposal3VO> entities) throws CloneNotSupportedException {
		List<Proposal3VO> results = new ArrayList<Proposal3VO>(entities.size());
		for (Proposal3VO vo : entities) {
			Proposal3VO otherVO = getLightProposalVO(vo);
			results.add(otherVO);
		}
		return results;
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private Proposal3VO getLightProposalVO(Proposal3VO vo) throws CloneNotSupportedException {
		if (vo == null)
			return null;
		Proposal3VO otherVO = vo.clone();
		otherVO.setProteinVOs(null);
		otherVO.setSessionVOs(null);
		otherVO.setShippingVOs(null);
		otherVO.setParticipants(null);
		return otherVO;
	}

	/**
	 * Check if user has access rights to change and remove Proposal3 entities. If not set throw
	 * AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkChangeRemoveAccess(Proposal3VO vo) throws AccessDeniedException {
		if (vo == null) return;
		autService.checkUserRightToAccessProposal(vo);				
	}


}
