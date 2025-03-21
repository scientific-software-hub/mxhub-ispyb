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
package ispyb.server.common.services.sessions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import ispyb.server.common.vos.proposals.Person3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import jakarta.annotation.Resource;
import jakarta.ejb.*;
import jakarta.jws.WebMethod;
import jakarta.persistence.*;

import jakarta.persistence.criteria.*;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import ispyb.common.util.Constants;
import ispyb.common.util.beamlines.ESRFBeamlineEnum;
import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.services.AuthorisationServiceLocal;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.SessionWS3VO;

/**
 * <p>
 * This session bean handles ISPyB Session3.
 * </p>
 */
@Stateless
//@TransactionTimeout(3600)
public class Session3ServiceBean implements Session3Service, Session3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Session3ServiceBean.class);

	//private final String[] beamlinesToProtect = { "ID29", "ID23-1", "ID23-2", "ID30A-1", "ID30A-2","ID30A-3", "ID30B" };
	private final String[] beamlinesToProtect = ESRFBeamlineEnum.getBeamlineNamesToBeProtected();
	
	private final String[] account_not_to_protect = { "OPID", "OPD", "MXIHR" };
	
	
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	@EJB
	private AuthorisationServiceLocal autService;

	@Resource
	private SessionContext context;

	public Session3ServiceBean() {
	};

	/**
	 * Create new Session3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Session3VO create(final Session3VO vo) throws Exception {
		entityManager.persist(vo);
		return vo;

	}

	/**
	 * Update the Session3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Session3VO update(final Session3VO vo) throws AccessDeniedException, Exception {
		checkChangeRemoveAccess(vo);
		entityManager.merge(vo);
		return vo;
	}

	/**
	 * Remove the Session3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws AccessDeniedException,Exception {
		Session3VO vo = this.findByPk(pk, false, false, false);
		entityManager.remove(vo);

	}

	/**
	 * Remove the Session3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Session3VO vo) throws AccessDeniedException,Exception {
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
	 * @return the Session3 value object
	 */
	public Session3VO findByPk(Integer pk, boolean fetchDataCollectionGroup, boolean fetchEnergyScan, boolean fetchXFESpectrum) throws AccessDeniedException,Exception {
		try {
			Session3VO vo = (Session3VO) entityManager.createQuery("select distinct vo from Session3VO vo "
							+ (fetchDataCollectionGroup ? "left join fetch vo.dataCollectionGroupVOs " : "")
							+ (fetchEnergyScan ? "left join fetch vo.energyScanVOs " : "")
							+ (fetchXFESpectrum ? "left join fetch vo.xfeSpectrumVOs " : "")
							+ "where vo.sessionId = :pk")
					.setParameter("pk", pk)
					.getSingleResult();
			checkChangeRemoveAccess(vo);
			return vo;
		} catch (NoResultException e) {
			return null;
		}
	}

	public SessionWS3VO findForWSByPk(final Integer pk, final boolean withDataCollectionGroup, final boolean withEnergyScan,
			final boolean withXFESpectrum) throws Exception {
		Session3VO found = findByPk(pk, withDataCollectionGroup, withEnergyScan, withXFESpectrum);
		SessionWS3VO sesLight = getWSSessionVO(found);
		return sesLight;
	}

	/**
	 * Find all Session3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Session3VO> findAll(boolean fetchDataCollectionGroup, boolean fetchEnergyScan, boolean fetchXFESpectrum)
			throws Exception {
		return entityManager.createQuery("select vo from Session3VO vo "
						+ (fetchDataCollectionGroup ? "left join fetch vo.dataCollectionGroupVOs " : "")
				+ (fetchEnergyScan ? "left join fetch vo.energyScanVOs " : "")
				+ (fetchXFESpectrum ? "left join fetch vo.xfeSpectrumVOs " : ""))
				.getResultList();
	}

	public Integer updateUsedSessionsFlag(Integer proposalId) throws Exception {


			Query query = entityManager.createNativeQuery(" UPDATE BLSession SET usedFlag = 1 WHERE BLSession.proposalId = ?1"
					+ " and (BLSession.usedFlag is null OR BLSession.usedFlag = 0) "
					+ " and (BLSession.sessionId IN (select c.sessionId from DataCollectionGroup c) "
					+ " or BLSession.sessionId IN (select e.sessionId from EnergyScan e) "
					+ " or BLSession.sessionId IN (select x.sessionId from XFEFluorescenceSpectrum x)) ")
					.setParameter(1, proposalId);
			return query.executeUpdate();
	}

	public Integer hasDataCollectionGroups(Integer sessionId) throws Exception {

		Query query = entityManager.createNativeQuery("SELECT COUNT(*) FROM DataCollectionGroup WHERE sessionId = ?1 ")
				.setParameter(1, sessionId);
		try {
			BigInteger res = (BigInteger) query.getSingleResult();

			return res.intValue();
		} catch (NoResultException e) {
			System.out.println("ERROR in hasDataCollectionGroups - NoResultException: " + sessionId);
			e.printStackTrace();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	@WebMethod
	public List<Session3VO> findFiltered(Integer proposalId, Integer nbMax, String beamline, Date date1, Date date2, Date dateEnd,
			boolean usedFlag,  String operatorSiteNumber) {

		return findFiltered(proposalId, nbMax, beamline,  date1, date2,  dateEnd,
				usedFlag, null,  operatorSiteNumber);
	}
	


	@SuppressWarnings("unchecked")
	public List<Session3VO> findByShippingId(Integer shippingId) {
		String query = "select * from BLSession, ShippingHasSession "
				+ " where BLSession.sessionId =  ShippingHasSession.sessionId "
				+ " and ShippingHasSession.shippingId = ?1 ";
		List<Session3VO> col = this.entityManager.createNativeQuery(query, Session3VO.class)
				.setParameter(1, shippingId)
				.getResultList();
		return col;
	}

	@SuppressWarnings("unchecked")
	public List<Session3VO> findByStartDateAndBeamLineNameAndNbShifts(final Integer proposalId, final Date startDateBegin,
			final Date startDateEnd, final String beamlineName, final Integer nbShifts) throws Exception {
		List<Session3VO> foundEntities = findFiltered(proposalId, null/* nbMax */, beamlineName, startDateBegin,
						startDateEnd, null/* endDate */, false/* usedFlag */, nbShifts, null);
		return foundEntities;
	}
	
	@SuppressWarnings("unchecked")
	public List<Session3VO> findSessionByDateProposalAndBeamline(int proposalId, String beamlineName, Date date) {
		List<Session3VO> sessions = new ArrayList<Session3VO>();
		sessions.addAll(this.findFiltered(proposalId, null, beamlineName, null, date, date, false, null));
		sessions.addAll(this.findFiltered(proposalId, null, beamlineName, null, date, date, true, null));
		return sessions;
	}

	public Set<Session3VO> findSessionsByLoginName(String loginName) {
		String queryPersonString = "SELECT DISTINCT person FROM Person3VO person WHERE person.login=:loginName";
		TypedQuery<Person3VO> queryPerson = this.entityManager.createQuery(queryPersonString, Person3VO.class);
		queryPerson.setParameter("loginName", loginName);
		try {
			Person3VO person = queryPerson.getSingleResult();
			return person.getSessionVOs();
		} catch (Exception e) {
			throw new RuntimeException(e);//TODO should we return empty set here?
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Session3VO> findFiltered(Integer nbMax, String beamline, Date date1, Date date2, Date dateEnd,
			boolean usedFlag, String operatorSiteNumber) {
		// Get the CriteriaBuilder from the EntityManager
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

// Create a CriteriaQuery object for Session3VO
		CriteriaQuery<Session3VO> criteriaQuery = criteriaBuilder.createQuery(Session3VO.class);

// Define the root of the query (the main entity to query from)
		Root<Session3VO> root = criteriaQuery.from(Session3VO.class);

// List to hold Predicate objects for query conditions
		List<Predicate> predicates = new ArrayList<>();

// Add conditions based on method parameters
		if (beamline != null) {
			predicates.add(criteriaBuilder.like(root.get("beamlineName"), "%" + beamline + "%"));
		}

		if (date1 != null) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), date1));
		}
		if (date2 != null) {
			predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), date2));
		}

		if (dateEnd != null) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), dateEnd));
		}

		if (usedFlag) {
			// Assuming usedFlag is a boolean that if true applies a special filter
			Predicate usedFlagPredicate = criteriaBuilder.equal(root.get("usedFlag"), 1);
			Predicate endDatePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), new Date()); // Adjust the date as per your application logic
			predicates.add(criteriaBuilder.or(usedFlagPredicate, endDatePredicate));
		}

		if (operatorSiteNumber != null) {
			predicates.add(criteriaBuilder.equal(root.get("operatorSiteNumber"), operatorSiteNumber));
		}

// Apply the predicates to the CriteriaQuery
		criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

// Make sure results are distinct
		criteriaQuery.distinct(true);

// Order the results
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("startDate")));

// Prepare the query to be executed
		List<Session3VO> result;
		if (nbMax != null) {
			result = entityManager.createQuery(criteriaQuery)
					.setMaxResults(nbMax)
					.getResultList();
		} else {
			result = entityManager.createQuery(criteriaQuery).getResultList();
		}

		return result;
	}

	/**
	 * returns the session for a specified proposal with endDate > today and startDate <= today
	 * 
	 * @param code
	 * @param number
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Session3VO> findSessionByProposalCodeAndNumber(String code, String number, String beamLineName) {
		String query = null;
		List<Session3VO> listVOs = null;
		if (beamLineName == null || beamLineName.equals("")) {

			query = "select * "
					+ " FROM BLSession ses, Proposal pro "
					+ "WHERE ses.proposalId = pro.proposalId AND pro.proposalCode like ?1 AND pro.proposalNumber = ?2 "
					+ "AND ses.endDate >= " + Constants.MYSQL_ORACLE_CURRENT_DATE + "  AND ses.startDate <= "
					+ Constants.MYSQL_ORACLE_CURRENT_DATE + "  ORDER BY sessionId DESC ";

			listVOs = this.entityManager.createNativeQuery(query, Session3VO.class)
					.setParameter(1, code)
					.setParameter(2, number)
					.getResultList();
		} else {
			query = "select * " + " FROM BLSession ses, Proposal pro "
					+ "WHERE ses.proposalId = pro.proposalId AND pro.proposalCode like ?1 AND pro.proposalNumber = ?2 "
					+ "AND ses.beamLineName like ?3 " + "AND ses.endDate >= " + Constants.MYSQL_ORACLE_CURRENT_DATE + " "
					+ "AND DATE(ses.startDate) <= DATE(" + Constants.MYSQL_ORACLE_CURRENT_DATE + ")  ORDER BY startDate DESC ";

			listVOs = this.entityManager.createNativeQuery(query, Session3VO.class)
					.setParameter(1, code)
					.setParameter(2, number)
					.setParameter(3, beamLineName)
					.getResultList();
		}
		if (listVOs == null || listVOs.isEmpty())
			return null;
		return listVOs;
	}
	
	/**
	 * returns the session for a specified proposal with endDate > today or null
	 * 
	 * @param code
	 * @param number
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	public SessionWS3VO[] findForWSByProposalCodeAndNumber(final String code, final String number, final String beamLineName)
			throws Exception {
		List<Session3VO> foundEntities = findSessionByProposalCodeAndNumber(code, number, beamLineName);
		SessionWS3VO[] ret = getWSSessionVOs(foundEntities);
		LOG.info("findForWSByProposalCodeAndNumber : code= " + code + ", number= " + number + ", beamlineName= " + beamLineName);
		return ret;
	}

	/**
	 * returns the list of sessions which have to be protected
	 * 
	 * @return
	 * @throws Exception
	 */
	public SessionWS3VO[] findForWSToBeProtected(final Integer delay, final Integer window) throws Exception {

		List<Session3VO> foundEntities = findSessionToBeProtected(delay, window);
		if (foundEntities == null)
			return null;
		SessionWS3VO[] ret;
		ret = getWSSessionVOs(foundEntities);
		String listSessionIds = "";
		if (ret != null) {
			for (int i = 0; i < ret.length; i++) {
				listSessionIds = ret[i].getSessionId() + ", ";
			}
		}
		LOG.info("findForWSToBeProtected : " + listSessionIds);
		return ret;
	}
	

	/**
	 * returns the list of sessions which have to be protected
	 * 
	 * @return
	 * @throws Exception
	 */
	public SessionWS3VO[] findForWSNotProtectedToBeProtected(final Date date1, final Date date2) throws Exception {
		LOG.info("findForWSNotProtectedToBeProtected");		
		List<Session3VO> foundEntities = findSessionNotProtectedToBeProtected(date1, date2);
		if (foundEntities == null)
			return null;
		SessionWS3VO[] ret = getWSSessionVOs(foundEntities);
		return ret;
	}
	
	/**
	 * get the number of datcollections which have more then 4 images
	 * 
	 * @param sesId
	 * @return
	 * @throws Exception
	 */
	public Integer getNbOfCollects(Integer sessionId) throws Exception {

		Query query = entityManager.createNativeQuery("SELECT count(*) FROM DataCollection, DataCollectionGroup "
				+ " WHERE DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId"
				+ " and DataCollection.numberOfImages >4 and DataCollectionGroup.sessionId  = ?1")
				.setParameter(1, sessionId);
		try {
			BigInteger res = (BigInteger) query.getSingleResult();

			return res.intValue();
		} catch (NoResultException e) {
			System.out.println("ERROR in getNbOfCollects - NoResultException: " + sessionId);
			e.printStackTrace();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * get the number of datacollections which have less/or 4 images
	 * 
	 * @param dcgId
	 * @return
	 * @throws Exception
	 */
	public Integer getNbOfTests(Integer sessionId) throws Exception {

		Query query = entityManager.createNativeQuery("SELECT count(*) FROM DataCollection, DataCollectionGroup "
				+ " WHERE DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId"
				+ " and DataCollection.numberOfImages <=4 and DataCollectionGroup.sessionId  = ?1 ")
				.setParameter(1, sessionId);
		try {
			BigInteger res = (BigInteger) query.getSingleResult();

			return res.intValue();
		} catch (NoResultException e) {
			System.out.println("ERROR in getNbOfTests - NoResultException: " + sessionId);
			e.printStackTrace();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * update the proposalId, returns the nb of rows updated
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 * @throws Exception
	 *
	 * 	// Be careful, when JBoss starts, the property file is not loaded, and it tries to initialize the class and fails.
	 * 	// private static String FIND_BY_PROPOSAL_CODE_NUMBER = getProposalCodeNumberQuery();
	 *
	 * 	// private static String FIND_BY_PROPOSAL_CODE_NUMBER_OLD = getProposalCodeNumberOldQuery();
	 */
	public Integer updateProposalId(Integer newProposalId, Integer oldProposalId) throws Exception {
			Query query = entityManager.createNativeQuery(" update BLSession  set proposalId = ?1 "
					+ " WHERE proposalId = ?2")
					.setParameter(1, newProposalId)
					.setParameter(2, oldProposalId);
			return query.executeUpdate();
	}

	/**
	 * returns the session with the given expSessionPk
	 * 
	 * @param expSessionPk
	 * @return
	 */
	public Session3VO findByExpSessionPk(final Long expSessionPk) throws Exception {
		// Get the EntityManager and CriteriaBuilder
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

// Create a CriteriaQuery object for Session3VO
		CriteriaQuery<Session3VO> criteriaQuery = criteriaBuilder.createQuery(Session3VO.class);

// Define the root of the query (the main entity to query from)
		Root<Session3VO> root = criteriaQuery.from(Session3VO.class);

// Condition to filter by expSessionPk if not null
		if (expSessionPk != null) {
			Predicate expSessionPkPredicate = criteriaBuilder.equal(root.get("expSessionPk"), expSessionPk);
			criteriaQuery.where(expSessionPkPredicate);
		}

// Order the results by startDate in descending order
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("startDate")));

// Execute the query and retrieve the list
		List<Session3VO> foundEntities = entityManager.createQuery(criteriaQuery).getResultList();

// Return the first element if list is not empty, otherwise return null
		return foundEntities.isEmpty() ? null : foundEntities.get(0);
		
	}

	/**
	 * returns the session linked to the given autoProcScaling
	 * 
	 * @param autoProcScalingId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Session3VO findByAutoProcScalingId(final Integer autoProcScalingId) throws Exception {
		String query = "select s.* from BLSession s, "
				+ " DataCollectionGroup g, DataCollection c, AutoProcIntegration api, AutoProcScaling_has_Int apshi, AutoProcScaling aps "
				+ " where s.sessionId = g.sessionId and  " + " g.dataCollectionGroupId = c.dataCollectionGroupId and "
				+ " c.dataCollectionId = api.dataCollectionId and " + " api.autoProcIntegrationId = apshi.autoProcIntegrationId and "
				+ " apshi.autoProcScalingId = aps.autoProcScalingId and " + " aps.autoProcScalingId = ?1 ";
		try {
			return (Session3VO) this.entityManager.createNativeQuery(query, Session3VO.class)
					.setParameter(1, autoProcScalingId)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public Session3VO findByAutoProcProgramAttachmentId(final Integer autoProcProgramAttachmentId) throws Exception {
		String query = "select s.* from BLSession s, "
				+ " DataCollectionGroup g, DataCollection c, AutoProcIntegration api, AutoProcProgram autoprocProgram, AutoProcProgramAttachment autoProcProgramAttachment"
				+ " where s.sessionId = g.sessionId and  g.dataCollectionGroupId = c.dataCollectionGroupId and autoprocProgram.autoProcProgramId = api.autoProcProgramId"
				+ " and c.dataCollectionId = api.dataCollectionId and autoprocProgram.autoProcProgramId = autoProcProgramAttachment.autoProcProgramId "
				+ " and autoProcProgramAttachment.autoProcProgramAttachmentId = ?1";
		try {
			return (Session3VO) this.entityManager.createNativeQuery(query, Session3VO.class)
						.setParameter(1, autoProcProgramAttachmentId)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	@Override
	public Session3VO findByAutoProcProgramId(int autoProcProgramId) {
		String query = "select s.* from BLSession s, "
				+ " DataCollectionGroup g, DataCollection c, AutoProcIntegration api, AutoProcProgram autoprocProgram "
				+ " where s.sessionId = g.sessionId and  g.dataCollectionGroupId = c.dataCollectionGroupId and autoprocProgram.autoProcProgramId = api.autoProcProgramId"
				+ " and c.dataCollectionId = api.dataCollectionId and autoprocProgram.autoProcProgramId = ?1 ";
		try {
			return (Session3VO) this.entityManager.createNativeQuery(query, Session3VO.class)
					.setParameter(1, autoProcProgramId)
					.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	/**
	 * launch the data confidentiality for the specified session
	 */
	public void protectSession(Integer sessionId) throws Exception {
		if (sessionId != null) {

			Session3VO sessionVO = this.findByPk(sessionId, false, false, false);
			LOG.info("session to be protected = " + sessionId);			

			// Check if the session exists
			if (sessionVO == null) {
				LOG.info("session does not exist");				
			} 
			// Check if the beamline shall be protected
			else if (ESRFBeamlineEnum.retrieveBeamlineWithName(sessionVO.getBeamlineName()) == null ) {				
				LOG.info("beamline shall not be protected :  " + sessionVO.getBeamlineName());
			}
			// Check if the beamline shall be protected
			else if (!ESRFBeamlineEnum.retrieveBeamlineWithName(sessionVO.getBeamlineName()).isToBeProtected()) {				
				LOG.info("beamline shall not be protected :  " + sessionVO.getBeamlineName());
			}
			// Check if the session is already protected
			else if (sessionVO.getProtectedData() != null && sessionVO.getProtectedData().equals("OK")) {
				LOG.info("session is already protected :  " + sessionVO.getProtectedData());
			} else {				

				// Check the minimum delay to protect : 2 hours
				Date lastUpdate = sessionVO.getLastUpdate();
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				Integer delayToTrigger = 2;
				cal.add(Calendar.HOUR_OF_DAY, -delayToTrigger);
				Date limitDate = cal.getTime();

				if (lastUpdate.before(limitDate)) {

					try {
						// proposal account
						String proposalAccount = sessionVO.getProposalVO().getProposalAccount();
						// beamline
						ESRFBeamlineEnum abl = ESRFBeamlineEnum.retrieveBeamlineWithName(sessionVO.getBeamlineName());
						String beamline = abl == null ? "" : abl.getDirectoryName();
						// mx
						String isMx = "true";
						// directory
						Date folderDate = sessionVO.getStartDate();
						SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd");
						String directory = "";
						if (folderDate != null)
							directory = dt.format(folderDate);
						// call data protection tool
						//TODO put back when runtime error found
						HttpClient client = new DefaultHttpClient();
						List<NameValuePair> qparams = new ArrayList<NameValuePair>();
						qparams.add(new BasicNameValuePair("user", proposalAccount));
						qparams.add(new BasicNameValuePair("bl", beamline));
						qparams.add(new BasicNameValuePair("dir", directory));
						qparams.add(new BasicNameValuePair("mx", isMx));
						LOG.debug("post user = " + proposalAccount + ", beamline = " + beamline + ", directory = " + directory);
						URI uri = URIUtils.createURI("http", "dch.esrf.fr", -1, "/protect.php",
								URLEncodedUtils.format(qparams, "UTF-8"), null);
						HttpPost post = new HttpPost(uri);
						HttpResponse response = client.execute(post);
						BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
						String line = "";
						String protectedData = "";
						while ((line = rd.readLine()) != null) {
							System.out.println(line);
							protectedData += line;
						}

						// result to log into ispyb
						if (protectedData.length() > 1024)
							protectedData = protectedData.substring(0, 1024);
						sessionVO.setProtectedData(protectedData);
						this.update(sessionVO);
						
						LOG.info("end of session protection");
						
						if (client != null && client.getConnectionManager() != null)
					        client.getConnectionManager().closeExpiredConnections();
						
					} catch (IOException e) {
						//
						LOG.error("WS ERROR IOException: getDataToBeProtected " + sessionVO.getSessionId());
						e.printStackTrace();
					} catch (Exception e) {
						//
						LOG.error("WS ERROR: getDataToBeProtected " + sessionVO.getSessionId());
						e.printStackTrace();
					} 

				} else {
					LOG.info("session not protected because too recent");
				}
			}
		}
	}
	
	
	//******************************     PRIVATE METHODS  ********************************************************

	private List<Session3VO> findSessionToBeProtected(Integer delay, Integer window) {
		// Get the EntityManager and CriteriaBuilder
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

// Create a CriteriaQuery object for Session3VO
		CriteriaQuery<Session3VO> cq = cb.createQuery(Session3VO.class);

// Define the root of the query (the main entity to query from)
		Root<Session3VO> root = cq.from(Session3VO.class);

// List to hold all conditions (Predicates)
		List<Predicate> predicates = new ArrayList<>();

// Calculate date ranges based on delay and window
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR_OF_DAY, -delay); // Adjusted by delay
		Date date2 = cal.getTime();
		cal.add(Calendar.HOUR_OF_DAY, -window); // Adjusted by window
		Date date1 = cal.getTime();

// Add date range conditions
		predicates.add(cb.greaterThanOrEqualTo(root.get("lastUpdate"), date1));
		predicates.add(cb.lessThanOrEqualTo(root.get("lastUpdate"), date2));

// Add beamline name condition, protected beamlines
		String[] beamlinesToProtect = ESRFBeamlineEnum.getBeamlineNamesToBeProtected();
		predicates.add(root.get("beamlineName").in((Object[]) beamlinesToProtect));

// Add subquery to filter out specific proposal codes
		Join<Session3VO, Proposal3VO> proposalJoin = root.join("proposalVO");
		String[] accountNotToProtect = new String[] {"opid*", "opd*", "mxihr*"}; // example pattern codes
		predicates.add(cb.not(proposalJoin.get("code").in((Object[]) accountNotToProtect)));

// Set where clause with combined predicates
		cq.where(cb.and(predicates.toArray(new Predicate[0])));

// Order by lastUpdate ascending
		cq.orderBy(cb.asc(root.get("lastUpdate")));

// Execute the query
		List<Session3VO> results = entityManager.createQuery(cq).getResultList();
		return results;
	}
	
	private List<Session3VO> findSessionNotProtectedToBeProtected(Date date1, Date date2) {
		// Assume entityManager is already injected or created
		EntityManager entityManager = this.entityManager;

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Session3VO> cq = cb.createQuery(Session3VO.class);
		Root<Session3VO> root = cq.from(Session3VO.class);

		List<Predicate> predicates = new ArrayList<>();

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int delayToTrigger = 8;
		int windowForTrigger = 14 * 24;

// Calculate the dates
		cal.add(Calendar.HOUR_OF_DAY, -delayToTrigger);
		// launch the protection of sessions which have not been protected during the last 14 days.
		if (date2 == null)
			date2 = cal.getTime();

		if (date1 == null) {
			cal.setTime(date2);
			cal.add(Calendar.HOUR_OF_DAY, -windowForTrigger);
			date1 = cal.getTime();
		}

// Adding date conditions
		if (date1 != null)
			predicates.add(cb.greaterThanOrEqualTo(root.get("lastUpdate"), date1));
		if (date2 != null)
			predicates.add(cb.lessThanOrEqualTo(root.get("lastUpdate"), date2));

// Filter by beamline names that need protection
		predicates.add(root.get("beamlineName").in(beamlinesToProtect));

// Sessions without protected data
		predicates.add(cb.isNull(root.get("protectedData")));

// Filter out specific account codes
		Join<Session3VO, Proposal3VO> joinProposal = root.join("proposalVO");
		predicates.add(cb.not(joinProposal.get("code").in(account_not_to_protect)));

		cq.where(cb.and(predicates.toArray(new Predicate[0])));
		cq.orderBy(cb.asc(root.get("lastUpdate")));

		List<Session3VO> resultList = entityManager.createQuery(cq).getResultList();

// Log information about the query
		LOG.info("find not protected sessions between " + date1 + " and  " + date2);
		if (resultList != null) {
			String sessionsIds = resultList.stream().map(s -> s.getSessionId().toString()).collect(Collectors.joining(", "));
			LOG.info(resultList.size() + " sessions found: " + sessionsIds);
		}

		return resultList;
	}
	
	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private Session3VO getLightSession3VO(Session3VO vo) throws CloneNotSupportedException {
		if (vo == null)
			return null;
		Session3VO otherVO = (Session3VO) vo.clone();
		otherVO.setDataCollectionGroupVOs(null);
		otherVO.setEnergyScanVOs(null);
		otherVO.setXfeSpectrumVOs(null);
		return otherVO;
	}

	private SessionWS3VO[] getWSSessionVOs(List<Session3VO> entities) throws CloneNotSupportedException {
		if (entities == null)
			return null;
		ArrayList<SessionWS3VO> results = new ArrayList<SessionWS3VO>(entities.size());
		for (Session3VO vo : entities) {
			SessionWS3VO otherVO = getWSSessionVO(vo);
			if (otherVO != null)
				results.add(otherVO);
		}
		SessionWS3VO[] tmpResults = new SessionWS3VO[results.size()];
		return results.toArray(tmpResults);
	}

	private SessionWS3VO getWSSessionVO(Session3VO vo) throws CloneNotSupportedException {
		if (vo == null)
			return null;
		Session3VO otherVO = getLightSession3VO(vo);
		Integer beamLineSetupId = null;
		Integer proposalId = null;
		String proposalName = null;
		if (vo.getProposalVO() != null) {
			proposalName = vo.getProposalVO().getProposalAccount();
		}
		beamLineSetupId = otherVO.getBeamLineSetupVOId();
		proposalId = otherVO.getProposalVOId();
		otherVO.setBeamLineSetupVO(null);
		otherVO.setProposalVO(null);
		SessionWS3VO wsSession = new SessionWS3VO(otherVO);
		wsSession.setBeamLineSetupId(beamLineSetupId);
		wsSession.setProposalId(proposalId);
		wsSession.setProposalName(proposalName);
		return wsSession;
	}
	
	@SuppressWarnings("unchecked")
	@WebMethod
	private List<Session3VO> findFiltered(Integer proposalId, Integer nbMax, String beamline, Date date1, Date date2, Date dateEnd,
			boolean usedFlag, Integer nbShifts, String operatorSiteNumber) {

		// Assume entityManager is already injected or created
		EntityManager entityManager = this.entityManager;

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Session3VO> cq = cb.createQuery(Session3VO.class);
		Root<Session3VO> session = cq.from(Session3VO.class);

		List<Predicate> predicates = new ArrayList<>();

// Adding conditions based on method parameters
		if (proposalId != null) {
			Join<Session3VO, Proposal3VO> proposalJoin = session.join("proposalVO");
			predicates.add(cb.equal(proposalJoin.get("proposalId"), proposalId));
		}
		if (beamline != null) {
			predicates.add(cb.like(session.get("beamlineName"), beamline));
		}
		if (date1 != null) {
			predicates.add(cb.greaterThanOrEqualTo(session.<Date>get("startDate"), date1));
		}
		if (date2 != null) {
			predicates.add(cb.lessThanOrEqualTo(session.<Date>get("startDate"), date2));
		}
		if (dateEnd != null) {
			predicates.add(cb.greaterThanOrEqualTo(session.<Date>get("endDate"), dateEnd));
		}
		if (usedFlag) {
			// Assuming Constants.MYSQL_ORACLE_CURRENT_DATE is a static import or available as a constant
			predicates.add(cb.or(
					cb.equal(session.get("usedFlag"), 1),
					cb.greaterThanOrEqualTo(session.<Date>get("endDate"), cb.currentDate())
			));
		}
		if (nbShifts != null) {
			predicates.add(cb.equal(session.get("nbShifts"), nbShifts));
		}
		if (operatorSiteNumber != null) {
			predicates.add(cb.equal(session.get("operatorSiteNumber"), operatorSiteNumber));
		}

		cq.where(cb.and(predicates.toArray(new Predicate[0])));
		cq.orderBy(cb.desc(session.get("startDate")));

// Set the maximum results if specified
		if (nbMax != null) {
			return entityManager.createQuery(cq).setMaxResults(nbMax).getResultList();
		} else {
			return entityManager.createQuery(cq).getResultList();
		}
	}
	
	/**
	 * Check if user has access rights to change and remove Session3 entities. If not set rollback only and throw
	 * AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkChangeRemoveAccess(Session3VO vo) throws AccessDeniedException {
		if (vo == null) return;
		autService.checkUserRightToAccessSession(vo);				
	}
}