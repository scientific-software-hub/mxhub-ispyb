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
package ispyb.server.common.services.shipping;

import ispyb.server.biosaxs.services.sql.SqlTableMapper;

import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;

import java.lang.reflect.Modifier;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ispyb.server.mx.vos.collections.Session3VO;
import jakarta.ejb.*;
import jakarta.persistence.*;

import jakarta.persistence.criteria.*;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * <p>
 * This session bean handles ISPyB Shipping3.
 * </p>
 */
@Stateless
public class Shipping3ServiceBean implements Shipping3Service, Shipping3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Shipping3ServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public Shipping3ServiceBean() {
	};

	/**
	 * Create new Shipping3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Shipping3VO create(final Shipping3VO vo) throws Exception {

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
	 * Update the Shipping3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Shipping3VO update(final Shipping3VO vo) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the Shipping3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		Shipping3VO vo = findByPk(pk, true);
		delete(vo);
	}

	/**
	 * Remove the Shipping3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Shipping3VO vo) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Shipping3 value object
	 */
	public Shipping3VO findByPk(final Integer pk, final boolean withDewars) throws Exception {
		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		return findByPk(pk, withDewars, false, false, false);
	}
	
	public Shipping3VO findByPk(final Integer pk, final boolean withDewars, final boolean withcontainers, final boolean withSamples) throws Exception {
		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		return findByPk(pk, withDewars, withcontainers, withSamples, false);
	}

	public String findSerialShippingByPk(final Integer pk, final boolean withDewars, final boolean withcontainers, final boolean withSamples, final boolean withSubSamples) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		Shipping3VO vo = this.findByPk(pk, withDewars, withcontainers, withSamples, withSubSamples);
		if (vo != null ) {
			Gson gson =  new GsonBuilder()
					.excludeFieldsWithModifiers(Modifier.PRIVATE)
					.serializeNulls()
					.create();
			return gson.toJson(vo);
		}
		return null;
	}

	public Shipping3VO findByPk(final Integer pk, final boolean withDewars, final boolean withcontainers, final boolean withSamples, final boolean withSubSamples) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		try {
			String qlString = "SELECT DISTINCT(vo) FROM Shipping3VO vo "
						+ (withDewars ? "LEFT JOIN FETCH vo.dewarVOs LEFT JOIN vo.dewarVOs dewars ": "")
						+ (withDewars && withcontainers ? "LEFT JOIN FETCH dewars.containerVOs LEFT JOIN dewars.containerVOs co " : "")
						+ (withDewars && withcontainers && withSamples ? "LEFT JOIN FETCH co.sampleVOs LEFT JOIN co.sampleVOs sa LEFT JOIN FETCH sa.blsampleImageVOs " : "")
						+ (withDewars && withcontainers && withSamples && withSubSamples ? "LEFT JOIN FETCH sa.blSubSampleVOs " : "")
						+ "WHERE vo.shippingId = :pk";
			return entityManager.createQuery(qlString, Shipping3VO.class)
					.setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}		
	}
	
	@Override
	public List<Map<String, Object>> getShippingById(final Integer shippingId) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		String mySQLQuery = "select  " +
				SqlTableMapper.getShippingTable() + ", (select count(*) from BLSample where BLSample.containerId = Container.containerId) as sampleCount, " +
				SqlTableMapper.getContainerTable() + " , (select count(*) from StockSolution where Dewar.dewarId = StockSolution.boxId) as stockSolutionCount, " +
				SqlTableMapper.getDewarTable() +
				" from Shipping \r\n"
				+ " left join Dewar on Dewar.shippingId = Shipping.shippingId \r\n"
				+ " left join Container on Dewar.dewarId = Container.dewarId \r\n"
				+ " where Shipping.shippingId = ?1";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter(1, shippingId);
		List<Map<String, Object>> aliasToValueMapList = query.getResultList();
		return aliasToValueMapList;
	}
	
	
	@Override
	public List<Map<String, Object>> getShippingByProposalId(final Integer proposalId) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		String mySQLQuery = "select  " +
				SqlTableMapper.getShippingTable() + ", (select count(*) from BLSample where BLSample.containerId = Container.containerId) as sampleCount, " +
				SqlTableMapper.getContainerTable() + " , (select count(*) from StockSolution where Dewar.dewarId = StockSolution.boxId) as stockSolutionCount, " +
				SqlTableMapper.getDewarTable() +
				" from Shipping \r\n"
				+ " left join Dewar on Dewar.shippingId = Shipping.shippingId \r\n"
				+ " left join Container on Dewar.dewarId = Container.dewarId \r\n"
				+ " where Shipping.proposalId = ?1";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter(1, proposalId);
		List<Map<String, Object>> aliasToValueMapList = query.getResultList();
		return aliasToValueMapList;
	}

	public Integer getSessionIdFromShippingId(final Integer shippingId) throws Exception {
		try {
			String mySQLQuery = "select sessionId from ShippingHasSession where ShippingHasSession.shippingId = ?1";
			return (Integer) entityManager.createNativeQuery(mySQLQuery, Integer.class)
					.setParameter(1, shippingId)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Integer> getAllShipmentIdsForSessionByShippingId(final Integer shippingId) throws Exception {
		try {
			Integer sessionId = getSessionIdFromShippingId(shippingId);
			String mySQLQuery = "select * from ShippingHasSession where ShippingHasSession.sessionId = ?1";
			return   entityManager.createNativeQuery(mySQLQuery, Integer.class)
					.setParameter(1, sessionId)
					.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Find all Shipping3s and set linked value objects if necessary
	 * 		// Generic HQL request to find all instances of Shipping3
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Shipping3VO> findAll() throws Exception {

        return (List<Shipping3VO>) entityManager.createQuery("SELECT vo from Shipping3VO vo ").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Shipping3VO> findFiltered(final Integer proposalId, final String type) throws Exception {

		EntityManager em = this.entityManager; // Ensure your EntityManager is properly initialized

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Proposal3VO> cq = cb.createQuery(Proposal3VO.class);
		Root<Proposal3VO> proposal = cq.from(Proposal3VO.class);

		List<Predicate> predicates = new ArrayList<>();

		if (type != null && !type.isEmpty()) {
			predicates.add(cb.like(proposal.get("shippingType"), type));
		}

		if (proposalId != null) {
			predicates.add(cb.equal(proposal.get("proposalId"), proposalId));
		}

		cq.select(proposal).distinct(true); // Ensures distinct results
		cq.where(cb.and(predicates.toArray(new Predicate[0])));
		cq.orderBy(cb.desc(proposal.get("shippingId"))); // Adjust the attribute to match your entity structure

		//TODO may produce class cast
		Query query = em.createQuery(cq);
		List<Shipping3VO> foundEntities = query.getResultList();
		return foundEntities;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByStatus(final String status, final java.util.Date dateStart,final boolean withDewars) throws Exception {

		EntityManager em = this.entityManager; // Ensure your EntityManager is properly initialized

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Shipping3VO> cq = cb.createQuery(Shipping3VO.class);
		Root<Shipping3VO> shipping = cq.from(Shipping3VO.class);

		List<Predicate> predicates = new ArrayList<>();

		if (status != null && !status.isEmpty()) {
			predicates.add(cb.like(shipping.get("shippingStatus"), "%" + status + "%"));
		}

		if (dateStart != null) {
			predicates.add(cb.greaterThanOrEqualTo(shipping.<Date>get("creationDate"), dateStart));
		}

		if (withDewars) {
			shipping.fetch("dewarVOs", JoinType.LEFT); // Assuming 'dewarVOs' is the correct attribute name
		}

		cq.select(shipping).distinct(true); // Ensures distinct results
		cq.where(cb.and(predicates.toArray(new Predicate[0])));
		cq.orderBy(cb.desc(shipping.get("shippingId"))); // Adjust the attribute name if necessary

		TypedQuery<Shipping3VO> query = em.createQuery(cq);
		return query.getResultList();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByProposal(final Integer userProposalId, final boolean withDewars) throws Exception {

		EntityManager em = this.entityManager; // Ensure your EntityManager is properly initialized

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Shipping3VO> cq = cb.createQuery(Shipping3VO.class);
		Root<Shipping3VO> shipping = cq.from(Shipping3VO.class);

		List<Predicate> predicates = new ArrayList<>();

		if (userProposalId != null) {
			Join<Shipping3VO, Proposal3VO> proposalJoin = shipping.join("proposalVO", JoinType.INNER); // Replace "proposalVO" with the actual field name in Shipping3VO
			predicates.add(cb.equal(proposalJoin.get("proposalId"), userProposalId));
		}

		if (withDewars) {
			shipping.fetch("dewarVOs", JoinType.LEFT); // Assuming 'dewarVOs' is the correct attribute name for the relationship
		}

		cq.select(shipping).distinct(true); // Ensures distinct results
		cq.where(cb.and(predicates.toArray(new Predicate[0])));
		cq.orderBy(cb.desc(shipping.get("creationDate")), cb.desc(shipping.get("shippingId"))); // Adjust the attribute names if necessary

		TypedQuery<Shipping3VO> query = em.createQuery(cq);
		return query.getResultList();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByProposal(final Integer proposalId,final boolean fetchDewars, final boolean fetchContainers, final boolean fetchSamples) throws Exception {

		try {
			String qlString = "SELECT vo FROM Shipping3VO vo "
						+ (fetchDewars ? "LEFT JOIN FETCH vo.dewarVOs LEFT JOIN vo.dewarVOs dewars ": "")
						+ (fetchDewars && fetchContainers ? "LEFT JOIN FETCH dewars.containerVOs LEFT JOIN dewars.containerVOs co " : "")
						+ (fetchDewars && fetchContainers && fetchSamples ? "LEFT JOIN FETCH co.sampleVOs LEFT JOIN co.sampleVOs sa LEFT JOIN FETCH sa.blsampleImageVOs LEFT JOIN FETCH sa.blSubSampleVOs " : "")
						+ "LEFT JOIN FETCH vo.sessions LEFT JOIN vo.sessions se "
						+ "LEFT JOIN FETCH se.proposalVO LEFT JOIN se.proposalVO proposal "
						+ "WHERE proposal.proposalId = :proposalId";
			return  entityManager.createQuery(qlString, Shipping3VO.class)
					.setParameter("proposalId", proposalId)
					.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByCreationDate(final Date firstDate, final boolean withDewars) throws Exception {

		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Shipping3VO> cq = cb.createQuery(Shipping3VO.class);
		Root<Shipping3VO> shipping = cq.from(Shipping3VO.class);

		List<Predicate> predicates = new ArrayList<>();

		if (firstDate != null) {
			predicates.add(cb.greaterThanOrEqualTo(shipping.get("creationDate"), firstDate));
		}

		if (withDewars) {
			shipping.fetch("dewarVOs", JoinType.LEFT); // Assuming 'dewarVOs' is the correct attribute name for the relationship
		}

		cq.select(shipping).distinct(true); // Ensures distinct results
		cq.where(cb.and(predicates.toArray(new Predicate[0])));
		cq.orderBy(cb.desc(shipping.get("creationDate")), cb.desc(shipping.get("shippingId"))); // Adjust attribute names if necessary

		TypedQuery<Shipping3VO> query = this.entityManager.createQuery(cq);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByCreationDateInterval(final Date firstDate, final Date endDate) throws Exception {

		EntityManager em = this.entityManager; // Assume EntityManager is initialized properly

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Shipping3VO> cq = cb.createQuery(Shipping3VO.class);
		Root<Shipping3VO> shipping = cq.from(Shipping3VO.class);

		List<Predicate> predicates = new ArrayList<>();

// Add predicates based on the date filters
		if (firstDate != null) {
			predicates.add(cb.greaterThanOrEqualTo(shipping.<Date>get("creationDate"), firstDate));
		}

		if (endDate != null) {
			predicates.add(cb.lessThanOrEqualTo(shipping.<Date>get("creationDate"), endDate));
		}

		cq.select(shipping).distinct(true); // Ensures distinct results
		cq.where(predicates.toArray(new Predicate[0]));

// Set order by conditions
		cq.orderBy(cb.desc(shipping.get("creationDate")), cb.desc(shipping.get("shippingId")));

		TypedQuery<Shipping3VO> query = em.createQuery(cq);
		return query.getResultList();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByBeamLineOperator(final String beamlineOperatorSiteNumber, final boolean withDewars)
			throws Exception {

		EntityManager em = this.entityManager; // Assume EntityManager is initialized properly

		if (beamlineOperatorSiteNumber != null) {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Shipping3VO> cq = cb.createQuery(Shipping3VO.class);
			Root<Shipping3VO> shipping = cq.from(Shipping3VO.class);

			// Create a join to sessions assuming "sessions" is the correct mapping name
			Join<Shipping3VO, Session3VO> sessionJoin = shipping.join("sessions", JoinType.LEFT);

			// Adding condition on sessions
			Predicate condition = cb.equal(sessionJoin.get("operatorSiteNumber"), beamlineOperatorSiteNumber);
			cq.where(condition);

			// Fetch dewars if required
			if (withDewars) {
				shipping.fetch("dewarVOs", JoinType.LEFT);
			}

			// Order by creationDate and shippingId
			cq.orderBy(cb.desc(shipping.get("creationDate")), cb.desc(shipping.get("shippingId")));
			cq.select(shipping).distinct(true); // Ensure distinct results

			TypedQuery<Shipping3VO> query = em.createQuery(cq);
			return query.getResultList();
		} else {
			return null; // No operation if beamlineOperatorSiteNumber is null
		}

	}

	@Override
	public List<Shipping3VO> findByProposalCode(final String proposalCode, final boolean withDewars) throws Exception {
		
		return this.findFiltered(null, null, proposalCode, null, null, null, null, null, withDewars);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findFiltered(final Integer proposalId, final String shippingName, final String proposalCode,
			final String proposalNumber, final String mainProposer, final Date date, final Date date2,
			final String operatorSiteNumber, final boolean withDewars) throws Exception {

		EntityManager em = this.entityManager; // Assume EntityManager is properly initialized
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Shipping3VO> cq = cb.createQuery(Shipping3VO.class);
		Root<Shipping3VO> shipping = cq.from(Shipping3VO.class);

		List<Predicate> predicates = new ArrayList<>();

// Checking if shippingName is specified
		if ((shippingName != null) && (!shippingName.isEmpty())) {
			predicates.add(cb.like(shipping.get("shippingName"), "%" + shippingName + "%"));
		}

// Date range conditions
		if (date != null) {
			predicates.add(cb.greaterThanOrEqualTo(shipping.get("creationDate"), date));
		}
		if (date2 != null) {
			predicates.add(cb.lessThanOrEqualTo(shipping.get("creationDate"), date2));
		}

// Handling proposal related conditions
		if (proposalId != null || proposalCode != null || mainProposer != null) {
			Join<Shipping3VO, Proposal3VO> proposalJoin = shipping.join("proposalVO", JoinType.LEFT);

			if (proposalId != null) {
				predicates.add(cb.equal(proposalJoin.get("proposalId"), proposalId));
			}
			if (proposalId == null && proposalCode != null && !proposalCode.isEmpty()) {
				predicates.add(cb.like(cb.lower(proposalJoin.get("code")), "%" + proposalCode.toLowerCase() + "%"));
			}
			if ((mainProposer != null) && (!mainProposer.isEmpty())) {
				Join<Proposal3VO, Person3VO> personJoin = proposalJoin.join("personVO", JoinType.LEFT);
				predicates.add(cb.like(cb.lower(personJoin.get("familyName")), "%" + mainProposer.toLowerCase() + "%"));
			}
		}

// Operator site number condition
		if (operatorSiteNumber != null) {
			Join<Shipping3VO, Session3VO> sessionJoin = shipping.join("sessions", JoinType.LEFT);
			predicates.add(cb.equal(sessionJoin.get("operatorSiteNumber"), operatorSiteNumber));
		}

// Fetching dewars if necessary
		if (withDewars) {
			shipping.fetch("dewarVOs", JoinType.LEFT);
		}

		cq.where(cb.and(predicates.toArray(new Predicate[0])));
		cq.orderBy(cb.desc(shipping.get("creationDate")), cb.desc(shipping.get("shippingId")));
		cq.distinct(true);

		return em.createQuery(cq).getResultList();

	}

	@Override
	public List<Shipping3VO> findByCustomQuery(final Integer proposalId, final String shippingName, final String proposalCode,
			final String proposalNumber, final String mainProposer, final Date date, final Date date2, final String operatorSiteNumber)
			throws Exception {

		return this.findFiltered(proposalId, shippingName, proposalCode, proposalNumber, mainProposer, date, date2,
				operatorSiteNumber, false);
	}

	/**
	 * update the proposalId, returns the nb of rows updated
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 * @throws Exception
	 */
	public Integer updateProposalId(final Integer newProposalId, final Integer oldProposalId) throws Exception {

		String sqlString = "UPDATE Shipping  SET proposalId = ?1 WHERE proposalId = ?2";
		Query query = entityManager.createNativeQuery(sqlString)
				.setParameter(1, newProposalId)
				.setParameter(2, oldProposalId);// 2 old value to be replaced
		return query.executeUpdate();
	}

	public int deleteAllSamplesAndContainersForShipping(Integer shippingId) throws Exception {
		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		Dewar3Service dewarService = (Dewar3Service) ejb3ServiceLocator.getLocalService(Dewar3Service.class);
		Container3Service containerService = (Container3Service) ejb3ServiceLocator.getLocalService(Container3Service.class);

		int nb = 0;
		Shipping3VO ship = this.findByPk(shippingId, true);
		Set<Dewar3VO> dewars = ship.getDewarVOs();
		for (Dewar3VO dewar3vo : dewars) {
			Dewar3VO dewar = dewarService.findByPk(dewar3vo.getDewarId(), true, true);
			Set<Container3VO> containers = dewar.getContainerVOs();
			for (Container3VO container3vo : containers) {
				containerService.deleteByPk(container3vo.getContainerId());
				nb = nb + 1;
			}
		}
		return nb;
	}

	@Override
	public List<Shipping3VO> findByProposalType(String proposalType) throws Exception {
		return this.findFiltered(null, null, proposalType, null, null, null, null, null, false);
	}

	@Override
	public List<Shipping3VO> findByProposalCodeAndDates(String proposalType, Date firstDate, Date endDate) throws Exception {
		return this.findFiltered(null, null, proposalType, null, null, firstDate, endDate, null, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shipping3VO> findByIsStorageShipping(final Boolean isStorageShipping) throws Exception {

		EntityManager em = this.entityManager; // Assume EntityManager is properly initialized
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Shipping3VO> cq = cb.createQuery(Shipping3VO.class);
		Root<Shipping3VO> shipping = cq.from(Shipping3VO.class);

// Add condition for isStorageShipping
		Predicate condition = cb.equal(shipping.get("isStorageShipping"), isStorageShipping);
		cq.where(condition);

// Execute the query and return the results
		List<Shipping3VO> results = em.createQuery(cq).getResultList();
		return results;

	}

	public Shipping3VO loadEager(Shipping3VO vo) throws Exception {
		Shipping3VO newVO = this.findByPk(vo.getShippingId(), true);
		return newVO;
	}

	public Integer[] countShippingInfo(final Integer shippingId) throws Exception {
		Query query = entityManager.createNativeQuery("SELECT COUNT(DISTINCT(histo.dewarTransportHistoryId)) eventsNumber, "
				+ " count(DISTINCT(bls.blSampleId)) samplesNumber "
				+ "FROM Shipping s  "
				+ " LEFT JOIN Dewar d ON (d.shippingId=s.shippingId) "
				+ "  LEFT JOIN Container c ON c.dewarId = d.dewarId "
				+ "	 LEFT JOIN BLSample bls ON bls.containerId = c.containerId "
				+ "  LEFT JOIN DewarTransportHistory histo ON (histo.dewarId = d.dewarId) "
				+
				// TODO use the Constants -- problem while deploying app.
				// "AND (histo.dewarStatus='"+ Constants.SHIPPING_STATUS_AT_ESRF + "' " +
				// " OR histo.dewarStatus='" + Constants.SHIPPING_STATUS_SENT_TO_USER + "')" +
				"AND (histo.dewarStatus='atESRF' "
				+ "OR histo.dewarStatus='sent to User') "
				+ "WHERE s.shippingId = ?1 GROUP BY s.shippingId ")
				.setParameter(1, shippingId);
		List orders = query.getResultList();
		int nb = orders.size();
		Integer nbSamples = 0;
		Integer nbDewarHistory = 0;
		Integer[] tab = new Integer[2];
		if (nb > 0) {
			Object[] o = (Object[]) orders.get(0);
			nbDewarHistory = ((Long) o[0]).intValue();
			nbSamples = ((Long) o[1]).intValue();
		}
		tab[0] = nbDewarHistory;
		tab[1] = nbSamples;
		return tab;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ispyb.server.common.services.shipping.Shipping3Service#findByPk(java.lang.Integer, boolean, boolean)
	 */
	@Override
	public Shipping3VO findByPk(final Integer pk, final boolean withDewars, final boolean withSession) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		try {
				String pk1 = "SELECT DISTINCT(vo) FROM Shipping3VO vo "
							+ (withDewars ? "LEFT JOIN FETCH vo.dewarVOs LEFT JOIN vo.dewarVOs dewars " : "")
							+ (withDewars && withSession ? "LEFT JOIN FETCH dewars.sessionVO " : "")
							+ "WHERE vo.shippingId = :pk";
				return entityManager.createQuery(pk1, Shipping3VO.class)
						.setParameter("pk", pk)
						.getSingleResult();
		} catch (NoResultException e) {
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
	private void checkAndCompleteData(Shipping3VO vo, boolean create) throws Exception {
		if (create) {
			if (vo.getShippingId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getShippingId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}