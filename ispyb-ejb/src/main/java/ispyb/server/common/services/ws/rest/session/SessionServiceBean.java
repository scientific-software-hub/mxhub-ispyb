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

package ispyb.server.common.services.ws.rest.session;

import ispyb.server.mx.services.ws.rest.WsServiceBean;
import ispyb.server.mx.vos.collections.DataCollectionGroup3VO;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.Query;


@Stateless
public class SessionServiceBean extends WsServiceBean  implements SessionService, SessionServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	private String getViewTableQuery(){
		return this.getQueryFromResourceFile("/queries/session/getViewTableQuery.sql");
	}
	
	
	@Override
	public List<Map<String, Object>> getSessionViewBySessionId(int proposalId, int sessionId) {
		String session = getViewTableQuery() + " where v_session.sessionId = ?1 and proposalId = ?2 order by v_session.sessionId DESC";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, sessionId)
				.setParameter(2, proposalId);
		List<Map<String, Object>> result = (List<Map<String, Object>>) query.getResultList();
		enrichWithDelays(result);
		return result;
    }


	@Override
	public List<Map<String, Object>> getSessionViewByProposalId(int proposalId) {
		String session = getViewTableQuery() + " where v_session.proposalId = ?1 order by v_session.sessionId DESC";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, proposalId);
		List<Map<String, Object>> result = (List<Map<String, Object>>) query.getResultList();
		enrichWithDelays(result);
		return result;
    }

	@Override
	public List<Map<String, Object>> getSessionViewByDates(String startDate, String endDate) {
		String session = getViewTableQuery() + " where " + "((BLSession_startDate >= ?1 and BLSession_startDate <= ?2) "
				+ "or "
				+ " (BLSession_endDate >= ?1 and BLSession_endDate <= ?2)"
				+ "or "
				+ " (BLSession_endDate >= ?2 and BLSession_startDate <= ?1)"
				+ "or "
				+ " (BLSession_endDate <= ?2 and BLSession_startDate >= ?1))"
				+ " order by v_session.sessionId DESC";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, startDate)
				.setParameter(2, endDate);
		List<Map<String, Object>> result = (List<Map<String, Object>>) query.getResultList();
		enrichWithDelays(result);
		return result;
    }

	@Override
	public List<Map<String, Object>> getSessionViewByProposalAndDates(int proposalId, String startDate, String endDate) {
		String session = getViewTableQuery() + " where v_session.proposalId = ?1 and " + "((BLSession_startDate >= ?2 and BLSession_startDate <= ?3) "
				+ "or "
				+ " (BLSession_endDate >= ?2 and BLSession_endDate <= ?3)"
				+ "or "
				+ " (BLSession_endDate >= ?3 and BLSession_startDate <= ?2)"
				+ "or "
				+ " (BLSession_endDate <= ?3 and BLSession_startDate >= ?2))"
				+ " order by v_session.sessionId DESC";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(2, startDate)
				.setParameter(3, endDate)
				.setParameter(1, String.valueOf(proposalId));
		List<Map<String, Object>> result = (List<Map<String, Object>>) query.getResultList();
		enrichWithDelays(result);
		return result;
    }

	@Override
	public List<Map<String, Object>> getSessionViewByBeamlineOperator(String beamlineOperator) {
		String session = getViewTableQuery()
				+ " where v_session.beamLineOperator LIKE ?1 order by v_session.sessionId DESC";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, "%" + beamlineOperator + "%");
		List<Map<String, Object>> result = (List<Map<String, Object>>) query.getResultList();
		enrichWithDelays(result);
		return result;
    }

	@Override
	public List<Map<String, Object>> getSessionViewByDates(String startDate, String endDate, String siteId) {
		String session = getViewTableQuery() + " where " + "((BLSession_startDate >= ?1 and BLSession_startDate <= ?2) "
				+ "or "
				+ " (BLSession_endDate >= ?1 and BLSession_endDate <= ?2)"
				+ "or "
				+ " (BLSession_endDate >= ?2 and BLSession_startDate <= ?1)"
				+ "or "
				+ " (BLSession_endDate <= ?2 and BLSession_startDate >= ?1))"
				+ " and v_session.operatorSiteNumber=?3 order by v_session.sessionId DESC";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, startDate)
				.setParameter(2, endDate)
				.setParameter(3, siteId);
		List<Map<String, Object>> result = (List<Map<String, Object>>) query.getResultList();
		enrichWithDelays(result);
		return result;
    }

	private void enrichWithDelays(List<Map<String, Object>> sessions) {
		if (sessions == null || sessions.isEmpty()) return;

		List<Integer> ids = sessions.stream()
				.map(s -> ((Number) s.get("sessionId")).intValue())
				.collect(Collectors.toList());

		Map<Integer, List<DataCollectionGroup3VO>> bySession = entityManager
				.createQuery(
						"SELECT dcg FROM DataCollectionGroup3VO dcg " +
						"WHERE dcg.sessionVO.sessionId IN :ids " +
						"ORDER BY dcg.dataCollectionGroupId",
						DataCollectionGroup3VO.class)
				.setParameter("ids", ids)
				.getResultList()
				.stream()
				.collect(Collectors.groupingBy(DataCollectionGroup3VO::getSessionVOId));

		sessions.forEach(session -> {
			int sid = ((Number) session.get("sessionId")).intValue();
			session.put("delays", computeDelays(bySession.getOrDefault(sid, Collections.emptyList())));
		});
	}

	private List<Map<String, Object>> computeDelays(List<DataCollectionGroup3VO> dcgs) {
		return IntStream.range(1, dcgs.size())
				.mapToObj(i -> {
					Date prevEnd = dcgs.get(i - 1).getEndTime();
					Date currStart = dcgs.get(i).getStartTime();
					if (prevEnd == null || currStart == null) return null;
					long gapSeconds = (currStart.getTime() - prevEnd.getTime()) / 1000;
					if (gapSeconds <= 900) return null;
					Map<String, Object> delay = new LinkedHashMap<>();
					delay.put("start", new Date(prevEnd.getTime()));
					delay.put("end", new Date(currStart.getTime()));
					delay.put("durationSeconds", gapSeconds);
					return delay;
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

}