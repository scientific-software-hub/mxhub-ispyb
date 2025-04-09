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

package ispyb.server.mx.services.ws.rest.sample;

import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;


@Stateless
public class SampleRestWsServiceBean implements SampleRestWsService, SampleRestWsServiceLocal {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;


	@Override
	public List<Map<String, Object>> getSamplesByProposalId(int proposalId) {
		String session = "select *, (SELECT \n" +
				"            MAX(`DataCollectionGroup`.`dataCollectionGroupId`)\n" +
				"        FROM\n" +
				"            `DataCollectionGroup`\n" +
				"        WHERE\n" +
				"            (`DataCollectionGroup`.`blSampleId` = `v_mx_sample`.`BLSample_blSampleId`)) AS `DataCollectionGroup_dataCollectionGroupId`" +
				" from v_mx_sample" +
				" where v_mx_sample.Protein_proposalId = ?1";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, proposalId);
		List<Map<String, Object>> aliasToValueMapList = query.getResultList();
		return aliasToValueMapList;
	}
	
	@Override
	public List<Map<String, Object>> getSamplesBySessionId(int proposalId,int sessionId) {
		String session = "select *"
				+ " from v_mx_sample"
				+ " where v_mx_sample.sessionId = ?2 and v_mx_sample.Protein_proposalId = ?1";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, proposalId)
				.setParameter(2, sessionId);
		List<Map<String, Object>> aliasToValueMapList = query.getResultList();
		return aliasToValueMapList;
	}
	
	@Override
	public List<Map<String, Object>> getSamplesByContainerId(int proposalId,int containerId) {
		String session = "select *, (SELECT \n" +
				"            MAX(`DataCollectionGroup`.`dataCollectionGroupId`)\n" +
				"        FROM\n" +
				"            `DataCollectionGroup`\n" +
				"        WHERE\n" +
				"            (`DataCollectionGroup`.`blSampleId` = `v_mx_sample`.`BLSample_blSampleId`)) AS `DataCollectionGroup_dataCollectionGroupId`"
				+ " from v_mx_sample"
				+ " where v_mx_sample.Container_containerId = ?1 and v_mx_sample.Protein_proposalId = ?2";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(2, proposalId)
				.setParameter(1, containerId);
		List<Map<String, Object>> aliasToValueMapList = query.getResultList();
		return aliasToValueMapList;
	}

	@Override
	public List<Map<String, Object>> getSamplesByShipmentId(int proposalId,int shippingId) {
		String session = "select *, (SELECT \n" +
				"            MAX(`DataCollectionGroup`.`dataCollectionGroupId`)\n" +
				"        FROM\n" +
				"            `DataCollectionGroup`\n" +
				"        WHERE\n" +
				"            (`DataCollectionGroup`.`blSampleId` = `v_mx_sample`.`BLSample_blSampleId`)) AS `DataCollectionGroup_dataCollectionGroupId`"
				+ " from v_mx_sample"
				+ " where v_mx_sample.Shipping_shippingId = ?1 and v_mx_sample.Protein_proposalId = ?2";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(2, proposalId)
				.setParameter(1, shippingId);
		
		List<Map<String, Object>> aliasToValueMapList = query.getResultList();
		return aliasToValueMapList;
	}
	
	@Override
	public List<Map<String, Object>> getSamplesByDewarId(int proposalId, int dewarId) {
		String session = "select *, (SELECT \n" +
				"            MAX(`DataCollectionGroup`.`dataCollectionGroupId`)\n" +
				"        FROM\n" +
				"            `DataCollectionGroup`\n" +
				"        WHERE\n" +
				"            (`DataCollectionGroup`.`blSampleId` = `v_mx_sample`.`BLSample_blSampleId`)) AS `DataCollectionGroup_dataCollectionGroupId`"
				+ " from v_mx_sample"
				+ " where v_mx_sample.Dewar_dewarId = ?1 and v_mx_sample.Protein_proposalId = ?2";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(2, proposalId)
				.setParameter(1, dewarId);
		List<Map<String, Object>> aliasToValueMapList = query.getResultList();
		return aliasToValueMapList;
	}

}
