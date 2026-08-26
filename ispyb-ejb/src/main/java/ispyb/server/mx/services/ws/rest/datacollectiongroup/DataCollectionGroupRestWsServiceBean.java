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

package ispyb.server.mx.services.ws.rest.datacollectiongroup;

import ispyb.server.mx.services.ws.rest.WsServiceBean;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.Query;


@Stateless
public class DataCollectionGroupRestWsServiceBean extends WsServiceBean implements DataCollectionGroupRestWsService, DataCollectionGroupRestWsServiceLocal  {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;


	private String getViewTableQuery(){
		return this.getQueryFromResourceFile("/queries/DataCollectionGroupRestWsServiceBean/getViewTableQuery.sql");
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DataCollectionSummary> getViewDataCollectionBySessionId(int proposalId, int sessionId) {
		String mySQLQuery = getViewTableQuery()
				+ " where DataCollectionGroup_sessionId = ?1 and BLSession_proposalId = ?2 "
				+ " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId order by DataCollection_startTime desc ";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter(1, sessionId)
				.setParameter(2, proposalId);
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) ((Query) query).getResultList();
        return resultList.stream().map(DataCollectionSummary::from).collect(Collectors.toList());
    }
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DataCollectionSummary> getViewDataCollectionBySessionIdHavingImages(int proposalId, int sessionId) {
		String mySQLQuery = getViewTableQuery()
				+ " where DataCollectionGroup_sessionId = ?1 and BLSession_proposalId = ?2 "
				+ " and DataCollection_numberOfImages is not null "
				+ " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId order by DataCollection_startTime desc ";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter(1, sessionId)
				.setParameter(2, proposalId);
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) ((Query) query).getResultList();
        return resultList.stream().map(DataCollectionSummary::from).collect(Collectors.toList());
    }
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DataCollectionSummary> getViewDataCollectionByProteinAcronym(int proposalId, String proteinAcronym) {
		String mySQLQuery = getViewTableQuery()
				+ " where BLSession_proposalId = ?1 and Protein_acronym = ?2"
				+ " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId, v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter(1, proposalId)
				.setParameter(2, proteinAcronym);
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) ((Query) query).getResultList();
        return resultList.stream().map(DataCollectionSummary::from).collect(Collectors.toList());
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<DataCollectionSummary> getViewDataCollectionBySampleId(int proposalId, int sampleId) {
		String mySQLQuery = getViewTableQuery()
				+ " where BLSession_proposalId = ?1 and DataCollectionGroup_blSampleId = ?2"
				+ " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId, v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter(1, proposalId)
				.setParameter(2, sampleId);
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) ((Query) query).getResultList();
        return resultList.stream().map(DataCollectionSummary::from).collect(Collectors.toList());
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<DataCollectionSummary> getViewDataCollectionBySampleName(int proposalId, String name) {
		String mySQLQuery = getViewTableQuery()
				+ " where BLSession_proposalId = ?1 and BLSample_name = ?2"
				+ " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId, v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter(1, proposalId)
				.setParameter(2, name);
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) ((Query) query).getResultList();
        return resultList.stream().map(DataCollectionSummary::from).collect(Collectors.toList());
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<DataCollectionSummary> getViewDataCollectionByImagePrefix(int proposalId, String prefix) {
		String mySQLQuery = getViewTableQuery()
				+ " where BLSession_proposalId = ?1 and DataCollection_imagePrefix = ?2"
				+ " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId, v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter(1, proposalId)
				.setParameter(2, prefix);
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) ((Query) query).getResultList();
        return resultList.stream().map(DataCollectionSummary::from).collect(Collectors.toList());
    }

	@Override
	@SuppressWarnings("unchecked")
	public Collection<DataCollectionSummary> getViewDataCollectionByDataCollectionId(int proposalId, int dataCollectionId) {
		String mySQLQuery = getViewTableQuery()
				+ " where DataCollection_dataCollectionId = ?1 and BLSession_proposalId = ?2 "
				+ " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId, v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter(1, dataCollectionId)
				.setParameter(2, proposalId);
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) ((Query) query).getResultList();
        return resultList.stream().map(DataCollectionSummary::from).collect(Collectors.toList());
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<DataCollectionSummary> getViewDataCollectionByWorkflowId(Integer proposalId, Integer workflowId) {
		String mySQLQuery = getViewTableQuery()
				+ " where Workflow_workflowId = ?1 and BLSession_proposalId = ?2 "
				+ " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId, v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter(1, workflowId)
				.setParameter(2, proposalId);
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) ((Query) query).getResultList();
        return resultList.stream().map(DataCollectionSummary::from).collect(Collectors.toList());
    }

	
	

	
}
