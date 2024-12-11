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
package ispyb.server.common.services.proposals;

import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.log4j.Logger;

import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.PersonWS3VO;
	

/**
 * <p>
 * This session bean handles ISPyB Person3.
 * </p>
 */
@Stateless
public class Person3ServiceBean implements Person3Service, Person3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Person3ServiceBean.class);


	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	public Person3ServiceBean() {
	};


	@Override
	public Person3VO merge(Person3VO detachedInstance) {
		try {
			Person3VO result = entityManager.merge(detachedInstance);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	/**
	 * Create new Person3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Person3VO create(final Person3VO vo)  {
		
		return this.merge(vo);
	}


	/**
	 * Remove the Person3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		Person3VO vo = findByPk(pk);

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		delete(vo);
	}

	/**
	 * Remove the Person3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Person3VO vo) throws Exception {
		entityManager.remove(vo);
	}

	/**
	 * <p>
	 * Returns the Person3VO instance matching the given primary key.
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
	 *
	 *  // Generic HQL request to find instances of Person3 by pk
	 */
	public Person3VO findByPk(Integer pk) {
		try {
			return entityManager.find(Person3VO.class, pk);
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * find a Person with a sessionId
	 */
	@SuppressWarnings("unchecked")
	public Person3VO findPersonBySessionId(Integer sessionId) {
		String query = "SELECT p.personId, p.laboratoryId, p.siteId, p.personUUID, "
				+ "p.familyName, p.givenName, p.title, p.emailAddress, p.phoneNumber, p.login, p.faxNumber, p.externalId FROM Person p, Proposal pro, BLSession ses "
				+ "WHERE p.personId = pro.personId AND pro.proposalId = ses.proposalId AND ses.sessionId = ?1 ";
		try {
			return (Person3VO) this.entityManager.createNativeQuery(query, Person3VO.class)
					.setParameter(1, sessionId)
					.getSingleResult();
		} catch (NoResultException noResultException) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Person3VO findPersonByProposalCodeAndNumber(String code, String number) {
		String query = "SELECT p.personId, p.laboratoryId, p.siteId, p.personUUID, "
				+ "p.familyName, p.givenName, p.title, p.emailAddress, p.phoneNumber, p.login, p.faxNumber, p.externalId " + " FROM Person p, Proposal pro "
				+ "WHERE p.personId = pro.personId AND pro.proposalCode like ?1 AND pro.proposalNumber = ?2 ";
		try {
			return (Person3VO) this.entityManager.createNativeQuery(query, Person3VO.class)
					.setParameter(1, code)
					.setParameter(2, number)
					.getSingleResult();
		} catch (NoResultException noResultException){
			return null;
		}
	}

	
	@SuppressWarnings("unchecked")
	public Person3VO findPersonByProteinAcronym(Integer proposalId, String acronym) {
		String query = "SELECT p.personId, p.laboratoryId, p.siteId, p.personUUID, "
				+ "p.familyName, p.givenName, p.title, p.emailAddress, p.phoneNumber, p.login, p.faxNumber, p.externalId " + " FROM Person p, Protein prot "
				+ "WHERE p.personId = prot.personId AND prot.proposalId = ?1 AND prot.acronym = ?2 ";
		try {
			return (Person3VO) this.entityManager.createNativeQuery(query, "personNativeQuery")
					.setParameter(1, proposalId)
					.setParameter(2, acronym)
					.getSingleResult();
		} catch (NoResultException noResultException) {
			return null;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<Person3VO> findFiltered(String familyName, String givenName, String login) {
		EntityManager em = this.entityManager; // Ensure your EntityManager is properly initialized

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Person3VO> cq = cb.createQuery(Person3VO.class);
		Root<Person3VO> person = cq.from(Person3VO.class);

		List<Predicate> predicates = new ArrayList<>();

		if (givenName != null) {
			Predicate givenNamePredicate = cb.like(person.get("givenName"), givenName);
			predicates.add(givenNamePredicate);
		}

		if (familyName != null) {
			Predicate familyNamePredicate = cb.like(person.get("familyName"), familyName);
			predicates.add(familyNamePredicate);
		}

		if (login != null && !login.isEmpty()) {
			Predicate loginPredicate = cb.like(person.get("login"), login);
			predicates.add(loginPredicate);
		}

		cq.where(cb.and(predicates.toArray(new Predicate[0])));
		cq.orderBy(cb.desc(person.get("personId")));

		TypedQuery<Person3VO> query = em.createQuery(cq);
		List<Person3VO> results = query.getResultList();
		return results;


	}

	@SuppressWarnings("unchecked")
	public Person3VO findPersonByLastNameAndFirstNameLetter(String lastName, String firstNameLetter) {
		EntityManager em = this.entityManager; // Ensure your EntityManager is properly initialized

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Person3VO> cq = cb.createQuery(Person3VO.class);
		Root<Person3VO> person = cq.from(Person3VO.class);

		List<Predicate> predicates = new ArrayList<>();

		if (lastName != null) {
			Predicate lastNamePredicate = cb.like(person.get("familyName"), lastName);
			predicates.add(lastNamePredicate);
		}

		if (firstNameLetter != null) {
			String firstNameLikePattern = firstNameLetter.replace('*', '%');
			Predicate firstNamePredicate = cb.like(person.get("givenName"), firstNameLikePattern);
			predicates.add(firstNamePredicate);
		}

		cq.where(cb.and(predicates.toArray(new Predicate[0])));
		cq.select(person); // selects the root, i.e., Person3VO

		TypedQuery<Person3VO> query = em.createQuery(cq);
		List<Person3VO> results = query.getResultList();
		if (results == null || results.isEmpty())
			return null;
		return results.get(0); // Return the first element of the list, if available
	}
	
	@SuppressWarnings("unchecked")	
	public Person3VO findBySiteId(String siteId) {
		try {
			return entityManager.createQuery("from Person3VO vo where vo.siteId = :siteId order by vo.personId desc", Person3VO.class)
					.setParameter("siteId", siteId)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Person3VO findByLogin(String login) {
		String query = "SELECT p.personId, p.laboratoryId, p.siteId, p.personUUID, "
				+ "p.familyName, p.givenName, p.title, p.emailAddress, p.phoneNumber, p.login, p.faxNumber, p.externalId " + " FROM Person p WHERE p.login = ?1 ";
		try {
			return (Person3VO) this.entityManager.createNativeQuery(query, "personNativeQuery")
					.setParameter(1, login)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	

	/**
	 * Find a Person for a specified sessionId
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public PersonWS3VO findForWSPersonBySessionId(final Integer sessionId) throws Exception {
		Person3VO foundEntities = findPersonBySessionId(sessionId);
				PersonWS3VO personWS = getWSPersonVO(foundEntities);
				return personWS;
	}
	
	/**
	 * Find a Person for a specified protein acronym
	 * 
	 * @param proposalId
	 * @param acronym
	 * @return
	 * @throws Exception
	 */
	public PersonWS3VO findForWSPersonByProteinAcronym(final Integer proposalId, final String acronym) throws Exception {
		Person3VO foundEntities = findPersonByProteinAcronym(proposalId, acronym);
				PersonWS3VO personWS = getWSPersonVO(foundEntities);
				return personWS;
	}
	
	/**
	 * Find a Person for a specified code and proposal number
	 * 
	 * @param code
	 * @param number
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	public PersonWS3VO findForWSPersonByProposalCodeAndNumber(final String code, final String number) throws Exception {
		Person3VO foundEntities = findPersonByProposalCodeAndNumber(code, number);
				PersonWS3VO personWS = getWSPersonVO(foundEntities);
				return personWS;
	}

	/**
	 * Find a Person for a specified code and proposal number
	 * 
	 * @param code
	 * @param number
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Person3VO> findByFamilyAndGivenName(final String familyName, final String givenName) throws Exception {
		return findFiltered(familyName, givenName, null);
	}
	
	/**
	 * Find a Person for a specified code and proposal number
	 * 
	 * @param code
	 * @param number
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PersonWS3VO> findPersonWS3VOByLogin(final String login) throws Exception {
		List<Person3VO> foundEntities = findFiltered(null, null, login);
		List<PersonWS3VO> foundEntitiesWS = new ArrayList<PersonWS3VO>();
		for (Person3VO person : foundEntities){
					PersonWS3VO personWS = getWSPersonVO(person);
					foundEntitiesWS.add(personWS);
		}
		return foundEntitiesWS;
	}
	
	public PersonWS3VO findForWSPersonByLastNameAndFirstNameLetter(final String lastName, final String firstNameLetter) throws Exception{
		Person3VO foundEntities = findPersonByLastNameAndFirstNameLetter(lastName, firstNameLetter);
				PersonWS3VO personWS = getWSPersonVO(foundEntities);
				return personWS;
	}
	
	public PersonWS3VO findForWSPersonByLogin(final String login) throws Exception {
		Person3VO foundEntities = findByLogin(login);
				PersonWS3VO personWS = getWSPersonVO(foundEntities);
				return personWS;
	}
	
	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private Person3VO getLightPersonVO(Person3VO vo) throws CloneNotSupportedException {
		if (vo == null)
			return null;
		Person3VO otherVO = (Person3VO) vo.clone();
		otherVO.setProposalVOs(null);
		return otherVO;
	}
	
	private PersonWS3VO getWSPersonVO(Person3VO vo) throws CloneNotSupportedException {
		if(vo == null)
			return null;
		Person3VO otherVO = getLightPersonVO(vo);
		Integer laboratoryId = null;
		laboratoryId = otherVO.getLaboratoryVOId();
		otherVO.setLaboratoryVO(null);
		PersonWS3VO wsPerson = new PersonWS3VO(otherVO);
		wsPerson.setLaboratoryId(laboratoryId);
		return wsPerson;
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
	private void checkAndCompleteData(Person3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getPersonId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getPersonId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}


}