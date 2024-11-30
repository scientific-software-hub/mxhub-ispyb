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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import jakarta.persistence.criteria.*;
import org.apache.log4j.Logger;

import ispyb.common.util.StringUtils;
import ispyb.server.common.services.shipping.Container3Service;
import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.BLSampleImage3VO;
import ispyb.server.mx.vos.sample.BLSampleInfo;
import ispyb.server.mx.vos.sample.BLSampleWS3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.sample.DiffractionPlanWS3VO;
import ispyb.server.mx.vos.sample.Protein3VO;
import ispyb.server.mx.vos.sample.SampleInfo;

/**
 * <p>
 * This session bean handles ISPyB BLSample3.
 * </p>
 */
@Stateless
public class BLSample3ServiceBean implements BLSample3Service, BLSample3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(BLSample3ServiceBean.class);

	// Generic HQL request to find instances of BLSample3 by pk

	// Generic HQL request to find all instances of BLSample3

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	@Resource
	private SessionContext context;

	public BLSample3ServiceBean() {
	};

	/**
	 * Create new BLSample3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public BLSample3VO create(final BLSample3VO vo) throws Exception {

		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the BLSample3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public BLSample3VO update(final BLSample3VO vo) throws Exception {

		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the BLSample3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		BLSample3VO vo = findByPk(pk, false, false, false);
		delete(vo);
	}

	/**
	 * Remove the BLSample3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final BLSample3VO vo) throws Exception {

		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the BLSample3 value object
	 */
	public BLSample3VO findByPk(final Integer pk, final boolean withEnergyScan, final boolean withSubSamples, final boolean withSampleImages) throws Exception {
	
		try {
			String qlString = "SELECT vo from BLSample3VO vo "
					+ (withEnergyScan ? "left join fetch vo.energyScanVOs " : "")
					+ (withSubSamples ? "left join fetch vo.blSubSampleVOs " : "")
					+ (withSampleImages ? "left join fetch vo.blsampleImageVOs " : "")
					+ "where vo.blSampleId = :pk";
			return entityManager.createQuery(qlString, BLSample3VO.class).setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Find all BLSample3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<BLSample3VO> findAll(final boolean withEnergyScan, final boolean withSubSamples) throws Exception {

		String qlString = "SELECT vo from BLSample3VO vo "
				+ (withEnergyScan ? "left join fetch vo.energyScanVOs " : "")
				+ (withSubSamples ? "left join fetch vo.blSubSampleVOs " : "");
        return entityManager.createQuery(qlString, BLSample3VO.class).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<BLSample3VO> findByShippingDewarContainer(final Integer shippingId, final List<Integer> dewarIds,
			final Integer containerId, final String dmCode, Integer sortView) throws Exception {


		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<BLSample3VO> cq = cb.createQuery(BLSample3VO.class);
		Root<BLSample3VO> sample = cq.from(BLSample3VO.class);
		Join<BLSample3VO, Container3VO> container = sample.join("containerVO");
		Join<Container3VO, Dewar3VO> dewar = container.join("dewarVO");

		if (!StringUtils.isEmpty(dmCode)) {
			cq.where(cb.like(sample.get("code"), dmCode));
		}

		if (containerId != null) {
			cq.where(cb.equal(container.get("containerId"), containerId));
			cq.orderBy(cb.asc(sample.get("location"))); // Handling CastDecimalOrder in JPA might need custom function or logic.
		}

		if (dewarIds != null) {
			cq.where(dewar.get("dewarId").in(dewarIds));
			if (sortView != null && sortView.equals(2)) {
				cq.orderBy(cb.asc(dewar.get("code")), cb.asc(container.get("code")), cb.asc(sample.get("location")));
			} else {
				Join<BLSample3VO, Crystal3VO> crystal = sample.join("crystalVO");
				Join<Crystal3VO, Protein3VO> protein = crystal.join("proteinVO");
				cq.orderBy(cb.asc(protein.get("acronym")));
			}
		}

		if (shippingId != null) {
			Join<Dewar3VO, Shipping3VO> shipping = dewar.join("shippingVO");
			cq.where(cb.equal(shipping.get("shippingId"), shippingId));
			if (sortView != null && sortView.equals(2)) {
				cq.orderBy(cb.asc(dewar.get("code")), cb.asc(container.get("code")), cb.asc(sample.get("location")));
			} else {
				Join<BLSample3VO, Crystal3VO> crystal = sample.join("crystalVO");
				Join<Crystal3VO, Protein3VO> protein = crystal.join("proteinVO");
				cq.orderBy(cb.asc(protein.get("acronym")), cb.asc(sample.get("name")));
			}
		}

		return entityManager.createQuery(cq).getResultList();
	}
	
	public List<BLSample3VO> findByShippingId(final Integer shippingId, final Integer sortView) throws Exception {
		return this.findByShippingDewarContainer(shippingId, null, null, null, sortView);
	}
	
	@SuppressWarnings("rawtypes")
	public List<BLSample3VO> findByShippingIdOrder(final Integer shippingId, final Integer sortView) throws Exception{
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		List orders = (List) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				// Obtain an instance of CriteriaBuilder from the EntityManager
				CriteriaBuilder cb = entityManager.getCriteriaBuilder();

// Create an instance of CriteriaQuery for the BLSample3VO
				CriteriaQuery<BLSample3VO> cq = cb.createQuery(BLSample3VO.class);

// Define the root of the query from which paths originate
				Root<BLSample3VO> sample = cq.from(BLSample3VO.class);

// Define joins across the entity associations
				Join<BLSample3VO, Container3VO> container = sample.join("containerVO");
				Join<Container3VO, Dewar3VO> dewar = container.join("dewarVO");
				Join<Dewar3VO, Shipping3VO> shipping = dewar.join("shippingVO");

// Add the condition (where clause)
				cq.where(cb.equal(shipping.get("shippingId"), shippingId));

// Prepare the query to execute
				TypedQuery<BLSample3VO> query = entityManager.createQuery(cq);

// Execute the query and get the results
				List<BLSample3VO> foundIds = query.getResultList();

				return foundIds;
			}
		});

		List<BLSample3VO> sampleList = null;
		int nb = orders.size();
		if (nb > 0)
			sampleList = new ArrayList<BLSample3VO>();

		for (int i = 0; i < nb; i++) {
			BLSample3VO blSample = new BLSample3VO();
			Integer blSampleId = (Integer) orders.get(i);
			// load VOs
			blSample = this.findByPk(blSampleId, false, false, false);
			sampleList.add(blSample);
		}
		return sampleList;
	}

	public List<BLSample3VO> findByDewarId(final List<Integer> dewarIds, final Integer sortView) throws Exception {
		return this.findByShippingDewarContainer(null, dewarIds, null, null, sortView);

	}

	public List<BLSample3VO> findByDewarId(final Integer dewarId, final Integer sortView) throws Exception {
		List<Integer> dewarIds = Collections.singletonList(dewarId); 
		return this.findByShippingDewarContainer(null, dewarIds, null, null, sortView);

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BLSample3VO> findByContainerId(final Integer containerId) throws Exception {
		// Obtain the CriteriaBuilder from the EntityManager
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

// Create a CriteriaQuery for BLSample3VO
		CriteriaQuery<BLSample3VO> cq = cb.createQuery(BLSample3VO.class);

// Define the root of the query, starting from BLSample3VO
		Root<BLSample3VO> sampleRoot = cq.from(BLSample3VO.class);

// Perform a join to navigate the association from BLSample3VO to ContainerVO
		Join<BLSample3VO, Container3VO> containerJoin = sampleRoot.join("containerVO");

// Specify the distinct true to get distinct results
		cq.select(sampleRoot).distinct(true);

// Add a where clause to the CriteriaQuery
		cq.where(cb.equal(containerJoin.get("containerId"), containerId));

// Create the TypedQuery
		TypedQuery<BLSample3VO> query = entityManager.createQuery(cq);

// Execute the query and get the result list
		List<BLSample3VO> results = query.getResultList();

		return results;
	}
	
	public List<BLSample3VO> findByCodeAndShippingId(final String dmCode, final Integer shippingId) throws Exception {
		return this.findByShippingDewarContainer(shippingId, null, null, dmCode, null);
	}

	/**
	 * find the sample info (Tuple of BLSample, Container, Crystal, Protein, DiffractionPlan) for a given blsampleId
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public BLSampleInfo findSampleInfoForPk(final Integer blSampleId) throws Exception {

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

		Crystal3Service crystalService = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
		Container3Service containerService = (Container3Service) ejb3ServiceLocator
				.getLocalService(Container3Service.class);
		Protein3Service proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
		DiffractionPlan3Service diffractionPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
				.getLocalService(DiffractionPlan3Service.class);

		BLSample3VO blSample = new BLSample3VO();
		BLSampleWS3VO wsSample = new BLSampleWS3VO();
		Container3VO container = new Container3VO();
		Crystal3VO crystal = new Crystal3VO();
		Protein3VO protein = new Protein3VO();
		DiffractionPlan3VO diffractionPlan = new DiffractionPlan3VO();

		// sample
		blSample = this.findByPk(blSampleId, false, false, true);
		wsSample = getWSBLSampleVO(blSample);

		// crystal
		if (blSample != null){
			Integer crystalId = blSample.getCrystalVOId();
			crystal = crystalService.findByPk(crystalId, false);
		}
		// protein
		if (crystal != null){
			Integer proteinId = crystal.getProteinVOId();
			protein = proteinService.findByPk(proteinId, false);
		}
		// Integer diffractionPlanId = (Integer) o[3];
		if (blSample != null){
			Integer containerId = blSample.getContainerVOId();
			container = containerService.findByPk(containerId, false);
		}
		if (blSample != null && blSample.getDiffractionPlanVOId() != null) {
			diffractionPlan = diffractionPlanService.findByPk(blSample.getDiffractionPlanVOId(), false, false);
		} else if (crystal != null && crystal.getDiffractionPlanVOId() != null)
			diffractionPlan = diffractionPlanService.findByPk(crystal.getDiffractionPlanVOId(), false, false);

		BLSampleInfo sampleInfo = new BLSampleInfo(wsSample, protein, crystal, diffractionPlan, container);

		return getWSBLSampleInfoVO(sampleInfo);

	}

	/**
	 * find all sample info (Tuple of BLSample, Container, Crystal, Protein, DiffractionPlan) for a specified proposal
	 * and a specified beamlineLocation and a given status(blSample or container)
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public BLSampleInfo[] findSampleInfoForProposal(final Integer proposalId, final String beamlineLocation,
			final String status) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		List orders = (List) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				Query q = entityManager
						.createNativeQuery("SELECT BLSample.blSampleId, Crystal.crystalId, Protein.proteinId,  Container.containerId "
								+ "FROM BLSample, Crystal, Protein,Container "
								+ "WHERE BLSample.crystalId=Crystal.crystalId AND "
								+ "Crystal.proteinId=Protein.proteinId AND "
								+ "BLSample.containerId=Container.containerId AND "
								+ "Protein.proposalId = "
								+ proposalId
								+ " AND "
								+ "(Container.containerStatus LIKE '"
								+ status
								+ "' OR BLSample.blSampleStatus LIKE '"
								+ status
								+ "') AND "
								+ "(Container.beamlineLocation like '"
								+ beamlineLocation
								+ "' OR (Container.beamlineLocation IS NULL OR Container.beamlineLocation like ''))");
				List orders = q.getResultList();
				return orders;
			}
		});

		List<BLSampleInfo> listVOs = null;
		int nb = orders.size();
		if (nb > 0)
			listVOs = new ArrayList<BLSampleInfo>();

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();

		Crystal3Service crystalService = (Crystal3Service) ejb3ServiceLocator.getLocalService(Crystal3Service.class);
		Container3Service containerService = (Container3Service) ejb3ServiceLocator
				.getLocalService(Container3Service.class);
		Protein3Service proteinService = (Protein3Service) ejb3ServiceLocator.getLocalService(Protein3Service.class);
		DiffractionPlan3Service diffractionPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
				.getLocalService(DiffractionPlan3Service.class);

		for (int i = 0; i < nb; i++) {
			BLSample3VO blSample = new BLSample3VO();
			BLSampleWS3VO wsSample = new BLSampleWS3VO();
			Container3VO container = new Container3VO();
			Crystal3VO crystal = new Crystal3VO();
			Protein3VO protein = new Protein3VO();
			DiffractionPlan3VO diffractionPlan = new DiffractionPlan3VO();
			Object[] o = (Object[]) orders.get(i);
			Integer blSampleId = (Integer) o[0];
			Integer crystalId = (Integer) o[1];
			Integer proteinId = (Integer) o[2];
			// Integer diffractionPlanId = (Integer) o[3];
			Integer containerId = (Integer) o[3];

			// load VOs
			blSample = this.findByPk(blSampleId, false, false, true);
			String blSampleImage = getSampleImagepath(blSampleId);
			wsSample = getWSBLSampleVO(blSample);

			container = containerService.findByPk(containerId, false);

			crystal = crystalService.findByPk(crystalId, false);

			protein = proteinService.findByPk(proteinId, false);

			if (blSample.getDiffractionPlanVOId() != null) {
				diffractionPlan = diffractionPlanService.findByPk(blSample.getDiffractionPlanVOId(), false, false);
			} else if (crystal.getDiffractionPlanVOId() != null)
				diffractionPlan = diffractionPlanService.findByPk(crystal.getDiffractionPlanVOId(), false, false);

			BLSampleInfo sampleInfo = new BLSampleInfo(wsSample, protein, crystal, diffractionPlan, container);
			listVOs.add(sampleInfo);
		}

		return getWSBLSampleInfoVOs(listVOs);

	}
	
	/**
	 * find all sample info (Tuple of SampleInfo) for a specified proposal
	 * and a specified beamlineLocation and a given status(blSample or container)
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public SampleInfo[] findForWSSampleInfoLight(final Integer proposalId, final Integer crystalFormId, final String beamlineLocation,
			final String status) throws Exception {
		
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		List<SampleInfo> orders = (List<SampleInfo>)template.execute(new EJBAccessCallback() {
			public List<SampleInfo> doInEJBAccess(Object parent) throws Exception {
				if (beamlineLocation == null && status == null && crystalFormId == null){
					Query q =  entityManager.createNativeQuery(" SELECT BLSample.blSampleId as sampleId, BLSample.name as sampleName, BLSample.code,  "
							+ "BLSample.holderLength, BLSample.location as sampleLocation, BLSample.SMILES as smiles, BLSample.diffractionPlanId as blDiffractionPlanId, Protein.acronym as proteinAcronym, "
							+ "Crystal.crystalId, Crystal.spaceGroup as crystalSpaceGroup, Crystal.cell_a as cellA, Crystal.cell_b as cellB, Crystal.cell_c as cellC, "
							+ "Crystal.cell_alpha as cellAlpha, Crystal.cell_beta as cellBeta, Crystal.cell_gamma as cellGamma, "
							+ "Crystal.diffractionPlanId as crystalDiffractionPlanId, "
							+ "Container.sampleChangerLocation as containerSampleChangerLocation, Container.code as containerCode "
							+ "FROM BLSample, Crystal, Protein,Container "
							+ "WHERE BLSample.crystalId=Crystal.crystalId AND "
							+ "Crystal.proteinId=Protein.proteinId AND "
							+ "BLSample.containerId=Container.containerId "
							+ " AND Protein.proposalId = " + proposalId, SampleInfo.class);
					return		q.getResultList();
				}
				else if (crystalFormId != null){
					Query q = entityManager
							.createNativeQuery(" SELECT BLSample.blSampleId as sampleId, BLSample.name as sampleName, BLSample.code,  "
									+ "BLSample.holderLength, BLSample.location as sampleLocation, BLSample.SMILES as smiles, BLSample.diffractionPlanId as blDiffractionPlanId, Protein.acronym as proteinAcronym, "
									+ "Crystal.crystalId, Crystal.spaceGroup as crystalSpaceGroup, Crystal.cell_a as cellA, Crystal.cell_b as cellB, Crystal.cell_c as cellC, "
									+ "Crystal.cell_alpha as cellAlpha, Crystal.cell_beta as cellBeta, Crystal.cell_gamma as cellGamma, "
									+ "Crystal.diffractionPlanId as crystalDiffractionPlanId, "
									+ "Container.sampleChangerLocation as containerSampleChangerLocation, Container.code as containerCode "
									+ "FROM BLSample, Crystal, Protein,Container "
									+ "WHERE BLSample.crystalId=Crystal.crystalId AND "
									+ "Crystal.proteinId=Protein.proteinId AND "
									+ "BLSample.containerId=Container.containerId "
									+ " AND Crystal.crystalId = " + crystalFormId, SampleInfo.class);
					return q.getResultList();
				}
					Query q = entityManager
					.createNativeQuery(" SELECT BLSample.blSampleId as sampleId, BLSample.name as sampleName, BLSample.code,  "
							+ "BLSample.holderLength, BLSample.location as sampleLocation, BLSample.SMILES as smiles, BLSample.diffractionPlanId as blDiffractionPlanId, Protein.acronym as proteinAcronym, "
							+ "Crystal.crystalId, Crystal.spaceGroup as crystalSpaceGroup, Crystal.cell_a as cellA, Crystal.cell_b as cellB, Crystal.cell_c as cellC, "
							+ "Crystal.cell_alpha as cellAlpha, Crystal.cell_beta as cellBeta, Crystal.cell_gamma as cellGamma, "
							+ "Crystal.diffractionPlanId as crystalDiffractionPlanId, "
							+ "Container.sampleChangerLocation as containerSampleChangerLocation, Container.code as containerCode "
							+ "FROM BLSample, Crystal, Protein,Container "
							+ "WHERE BLSample.crystalId=Crystal.crystalId AND "
							+ "Crystal.proteinId=Protein.proteinId AND "
							+ "BLSample.containerId=Container.containerId " + " AND Protein.proposalId = " + proposalId
							+ " AND " + "(Container.containerStatus LIKE '" + status
							+ "' OR BLSample.blSampleStatus LIKE '" + status + "') AND "
							+ "(Container.beamlineLocation like '" + beamlineLocation
							+ "' OR (Container.beamlineLocation IS NULL OR Container.beamlineLocation like ''))", SampleInfo.class);

					return q.getResultList();
			}
		});

		if(orders.isEmpty()) return null;

		orders.forEach(sampleInfo -> {
			String blSampleImage = getSampleImagepath(sampleInfo.sampleId);
			sampleInfo.blsampleImagePath = blSampleImage;
			DiffractionPlanWS3VO diffPlanws = getDiffractionPlan(sampleInfo);
			sampleInfo.diffractionPlan = diffPlanws;
		});

		return orders.toArray(new SampleInfo[0]);
	}

	private DiffractionPlanWS3VO getDiffractionPlan(SampleInfo sampleInfo) {
		try {
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			DiffractionPlan3Service diffractionPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
					.getLocalService(DiffractionPlan3Service.class);
			DiffractionPlan3VO diffractionPlan = new DiffractionPlan3VO();
			if (sampleInfo.blDiffractionPlanId!= null) {
				diffractionPlan = diffractionPlanService.findByPk(sampleInfo.blDiffractionPlanId, false, false);
			} else if (sampleInfo.crystalDiffractionPlanId != null)
				diffractionPlan = diffractionPlanService.findByPk(sampleInfo.crystalDiffractionPlanId, false, false);
			Double minimalResolution = diffractionPlan.getMinimalResolution();
			sampleInfo.minimalResolution = minimalResolution;
			String experimentType = diffractionPlan.getExperimentKind();
			sampleInfo.experimentType = experimentType;
			return new DiffractionPlanWS3VO(diffractionPlan);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * find only sample info for a specified proposal and a specified beamlineLocation and a given status(blSample or
	 * container)
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public BLSampleWS3VO[] findForWSSampleInfoForProposalLight(final Integer proposalId, final String beamlineLocation,
			final String status) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		List listIds = (List) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				Query q = entityManager
						.createNativeQuery("SELECT BLSample.blSampleId, Crystal.crystalId, Protein.proteinId,  Container.containerId "
								+ "FROM BLSample, Crystal, Protein,Container "
								+ "WHERE BLSample.crystalId=Crystal.crystalId AND "
								+ "Crystal.proteinId=Protein.proteinId AND "
								+ "BLSample.containerId=Container.containerId AND "
								+ "Protein.proposalId = "
								+ proposalId
								+ " AND "
								+ "(Container.containerStatus LIKE '"
								+ status
								+ "' OR BLSample.blSampleStatus LIKE '"
								+ status
								+ "') AND "
								+ "(Container.beamlineLocation like '"
								+ beamlineLocation
								+ "' OR (Container.beamlineLocation IS NULL OR Container.beamlineLocation like ''))");
				List orders = q.getResultList();
				return orders;
			}
		});

		BLSampleWS3VO[] tab = null;
		int nb = listIds.size();
		if (nb > 0) {

			tab = new BLSampleWS3VO[nb];
			for (int i = 0; i < nb; i++) {
				Object[] o = (Object[]) listIds.get(i);
				Integer blSampleId = (Integer) o[0];
				// Integer crystalId = (Integer) o[1];
				// Integer proteinId = (Integer) o[2];
				// Integer containerId = (Integer) o[3];
				BLSample3VO blSample = this.findByPk(blSampleId, false, false, true);
				BLSampleWS3VO wsSample = getWSBLSampleVO(blSample);
				tab[i] = wsSample;
			}
		}

		return tab;

	}

	/**
	 * Get all BLSample3 entity VOs from a collection of BLSample3 local entities.
	 * 
	 * @param localEntities
	 * @return
	 */
	@SuppressWarnings({ "unused" })
	private BLSample3VO[] getBLSample3VOs(List<BLSample3VO> entities) {
		ArrayList<BLSample3VO> results = new ArrayList<BLSample3VO>(entities.size());
		for (BLSample3VO vo : entities) {
			results.add(vo);
		}
		BLSample3VO[] tmpResults = new BLSample3VO[results.size()];
		return results.toArray(tmpResults);
	}

	/**
	 * find all sample info (Tuple of BLSample, Container, Crytsal, Protein, DiffractionPlan) for a specified proposal
	 * and a specified beamlineLocation and a given status(blSample or container)
	 * 
	 * @param proposalId
	 * @param beamlineLocation
	 * @param status
	 * @param detachLight
	 * @return
	 * @throws Exception
	 */
	public BLSampleInfo[] findForWSSampleInfoForProposal(final Integer proposalId, final String beamlineLocation,
			final String status) throws Exception {
		BLSampleInfo[] ret = this.findSampleInfoForProposal(proposalId, beamlineLocation, status);

		return ret;
	}

	/**
	 * Get all entity VOs from a collection of local entities.
	 * 
	 * @param localEntities
	 * @return
	 */
	@SuppressWarnings("unused")
	private BLSample3VO[] getBLSampleVOs(List<BLSample3VO> entities) {
		ArrayList<BLSample3VO> results = new ArrayList<BLSample3VO>(entities.size());
		for (BLSample3VO vo : entities) {
			results.add(vo);
		}
		BLSample3VO[] tmpResults = new BLSample3VO[results.size()];
		return results.toArray(tmpResults);
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	@SuppressWarnings({ "unused" })
	private BLSample3VO[] getLightBLSampleVOs(List<BLSample3VO> entities) throws CloneNotSupportedException {
		ArrayList<BLSample3VO> results = new ArrayList<BLSample3VO>(entities.size());
		for (BLSample3VO vo : entities) {
			BLSample3VO otherVO = getLightBLSampleVO(vo);
			results.add(otherVO);
		}
		BLSample3VO[] tmpResults = new BLSample3VO[results.size()];
		return results.toArray(tmpResults);
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private BLSample3VO getLightBLSampleVO(BLSample3VO vo) throws CloneNotSupportedException {
		BLSample3VO otherVO = (BLSample3VO) vo.clone();
		//otherVO.setEnergyScanVOs(null);
		return otherVO;
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private BLSampleInfo getLightBLSampleInfo(BLSampleInfo vo) throws CloneNotSupportedException {
		BLSampleInfo otherVO = (BLSampleInfo) vo.clone();
		otherVO.getContainer().setSampleVOs(null);
		otherVO.getCrystal().setSampleVOs(null);
		otherVO.getProtein().setCrystalVOs(null);
		//otherVO.getBlSample().setEnergyScanVOs(null);
		otherVO.getDiffractionPlan().setExperimentKindVOs(null);
		return otherVO;
	}

	/**
	 * Find a sample by its primary key -- webservices object
	 * 
	 * @param pk
	 * @param withLink1
	 * @param withLink2
	 * @return
	 * @throws Exception
	 */
	public BLSampleWS3VO findForWSByPk(final Integer pk, final boolean withEnergyScan, final boolean withSubSamples, final boolean withSampleImages) throws Exception {
	
		try {
			String qlString = "SELECT vo from BLSample3VO vo "
					+ (withEnergyScan ? "left join fetch vo.energyScanVOs " : "")
					+ (withSubSamples ? "left join fetch vo.blSubSampleVOs " : "")
					+ (withSampleImages ? "left join fetch vo.blsampleImageVOs " : "")
					+ "where vo.blSampleId = :pk";
			BLSample3VO found = (BLSample3VO) entityManager.createQuery(qlString).setParameter("pk", pk)
					.getSingleResult();
			BLSampleWS3VO sampleLight = getWSBLSampleVO(found);
			return sampleLight;
		} catch (NoResultException e) {
			return null;
		}
	}

	private BLSampleWS3VO getWSBLSampleVO(BLSample3VO vo) throws CloneNotSupportedException {
		if (vo == null)
			return null;
		BLSample3VO otherVO = getLightBLSampleVO(vo);
		Integer crystalId = null;
		Integer containerId = null;
		crystalId = otherVO.getCrystalVOId();
		containerId = otherVO.getContainerVOId();
		otherVO.setCrystalVO(null);
		otherVO.setContainerVO(null);
		BLSampleWS3VO wsSample = new BLSampleWS3VO(otherVO);
		wsSample.setCrystalId(crystalId);
		wsSample.setContainerId(containerId);
		return wsSample;
	}

	private BLSampleInfo[] getWSBLSampleInfoVOs(List<BLSampleInfo> entities) throws CloneNotSupportedException {
		if (entities == null)
			return null;
		ArrayList<BLSampleInfo> results = new ArrayList<BLSampleInfo>(entities.size());
		for (BLSampleInfo vo : entities) {
			BLSampleInfo otherVO = getWSBLSampleInfoVO(vo);
			results.add(otherVO);
		}
		BLSampleInfo[] tmpResults = new BLSampleInfo[results.size()];
		return results.toArray(tmpResults);
	}

	private BLSampleInfo getWSBLSampleInfoVO(BLSampleInfo vo) throws CloneNotSupportedException {
		BLSampleInfo otherVO = getLightBLSampleInfo(vo);
		otherVO.getBlSample().setCrystalVO(null);
		otherVO.getContainer().setDewarVO(null);
		otherVO.getCrystal().setProteinVO(null);
		otherVO.getProtein().setProposalVO(null);
		return otherVO;
	}

	public BLSample3VO loadEager(BLSample3VO vo) throws Exception {
		BLSample3VO newVO = this.findByPk(vo.getBlSampleId(), true, true, true);
		return newVO;
	}

	@SuppressWarnings("unchecked")
	public List<BLSample3VO> findByProposalIdAndDewarNull(final Integer proposalId) throws Exception {

		// Obtain the EntityManager and CriteriaBuilder
		EntityManager em = this.entityManager;
		CriteriaBuilder cb = em.getCriteriaBuilder();

// Create a CriteriaQuery for BLSample3VO
		CriteriaQuery<BLSample3VO> cq = cb.createQuery(BLSample3VO.class);

// Define the root of the query, starting from BLSample3VO
		Root<BLSample3VO> sampleRoot = cq.from(BLSample3VO.class);

// Specify that the results should be distinct
		cq.select(sampleRoot).distinct(true);

// Add condition to check if 'containerVO' is null
		sampleRoot.join("containerVO", JoinType.LEFT); // Create a left join
		Predicate containerIsNull = cb.isNull(sampleRoot.get("containerVO"));
		cq.where(containerIsNull);

// Check if proposalId is not null and then add necessary joins and conditions
		if (proposalId != null) {
			Join<BLSample3VO, Crystal3VO> crystalJoin = sampleRoot.join("crystalVO");
			Join<Crystal3VO, Protein3VO> proteinJoin = crystalJoin.join("proteinVO");
			Join<Protein3VO, Proposal3VO> proposalJoin = proteinJoin.join("proposalVO");

			// Add a condition on proposalId
			Predicate proposalIdEquals = cb.equal(proposalJoin.get("proposalId"), proposalId);
			cq.where(proposalIdEquals);
		}

// Create the TypedQuery
		TypedQuery<BLSample3VO> query = em.createQuery(cq);

// Execute the query and get the result list
		List<BLSample3VO> results = query.getResultList();

		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BLSample3VO> findFiltered(final Integer proposalId, final Integer proteinId, final String acronym,
			final Integer crystalId, final String name, final String code, final String status,
			final Byte isInSampleChanger, final Integer shippingId, final String sortType) throws Exception {

		EntityManager em = this.entityManager;
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<BLSample3VO> cq = cb.createQuery(BLSample3VO.class);
		Root<BLSample3VO> sampleRoot = cq.from(BLSample3VO.class);

		List<Predicate> predicates = new ArrayList<>();

		if (proposalId != null) {
			Join<BLSample3VO, Crystal3VO> crystalJoin = sampleRoot.join("crystalVO");
			Join<Crystal3VO, Protein3VO> proteinJoin = crystalJoin.join("proteinVO");
			Join<Protein3VO, Proposal3VO> proposalJoin = proteinJoin.join("proposalVO");
			predicates.add(cb.equal(proposalJoin.get("proposalId"), proposalId));
		}

		if (proteinId != null || acronym != null) {
			Join<BLSample3VO, Crystal3VO> crystalJoin = sampleRoot.join("crystalVO", JoinType.LEFT);
			Join<Crystal3VO, Protein3VO> proteinJoin = crystalJoin.join("proteinVO", JoinType.LEFT);
			if (proteinId != null)
				predicates.add(cb.equal(proteinJoin.get("proteinId"), proteinId));
			if (acronym != null)
				predicates.add(cb.like(proteinJoin.get("acronym"), acronym));
		}

		if (crystalId != null) {
			Join<BLSample3VO, Crystal3VO> crystalJoin = sampleRoot.join("crystalVO", JoinType.LEFT);
			predicates.add(cb.equal(crystalJoin.get("crystalId"), crystalId));
		}

		if (name != null && !name.isEmpty()) {
			predicates.add(cb.like(sampleRoot.get("name"), name));
		}

		if (code != null && !code.isEmpty()) {
			predicates.add(cb.like(sampleRoot.get("code"), code));
		}

		if (status != null && !status.isEmpty()) {
			predicates.add(cb.equal(sampleRoot.get("blSampleStatus"), status));
		}

		if (isInSampleChanger != null) {
			predicates.add(cb.equal(sampleRoot.get("isInSampleChanger"), isInSampleChanger));
		}

		if (shippingId != null) {
			Join<BLSample3VO, Container3VO> containerJoin = sampleRoot.join("containerVO");
			Join<Container3VO, Dewar3VO> dewarJoin = containerJoin.join("dewarVO");
			Join<Dewar3VO, Shipping3VO> shippingJoin = dewarJoin.join("shippingVO");
			predicates.add(cb.equal(shippingJoin.get("shippingId"), shippingId));
		}

		cq.where(cb.and(predicates.toArray(new Predicate[0])));

		if (sortType != null) {
			if ("container".equals(sortType)) {
				Join<BLSample3VO, Container3VO> containerJoin = sampleRoot.join("containerVO", JoinType.LEFT);
				Join<Container3VO, Dewar3VO> dewarJoin = containerJoin.join("dewarVO", JoinType.LEFT);
				cq.orderBy(cb.asc(dewarJoin.get("code")), cb.asc(containerJoin.get("code")));
			} else if ("name".equals(sortType)) {
				cq.orderBy(cb.asc(sampleRoot.get("name")));
			}
		}

		TypedQuery<BLSample3VO> query = em.createQuery(cq);
		return query.getResultList();
	}
	
	public List<BLSample3VO> findByCrystalNameCode(final Integer crystalId, final String name, final String code)
			throws Exception {

		return this.findFiltered(null, null, null, crystalId, name, code, null, null, null, null);

	}

	public List<BLSample3VO> findByProposalIdAndBlSampleStatus(final Integer proposalId, final String status)
			throws Exception {
		return this.findFiltered(proposalId, null, null, null, null, null, status, null, null, null);

	}

	public List<BLSample3VO> findByProposalId(final Integer proposalId) throws Exception {
		return this.findFiltered(proposalId, null, null, null, null, null, null, null, null, null);

	}

	public List<BLSample3VO> findByNameAndCodeAndProposalId(final String name, final String code,
			final Integer proposalId) throws Exception {
		return this.findFiltered(proposalId, null, null, null, name, code, null, null, null, null);

	}

	@Override
	public List<BLSample3VO> findByCrystalId(Integer crystalId) throws Exception {
		return this.findFiltered(null, null, null, crystalId, null, null, null, null, null, null);
	}

	@Override
	public List<BLSample3VO> findByName(String name) throws Exception {
		return this.findFiltered(null, null, null, null, name, null, null, null, null, null);
	}

	@Override
	public List<BLSample3VO> findByCode(String code) throws Exception {
		return this.findFiltered(null, null, null, null, null, code, null, null, null, null);
	}

	@Override
	public List<BLSample3VO> findByStatus(String status) throws Exception {
		return this.findFiltered(null, null, null, null, null, null, status, null, null, null);
	}

	public List<BLSample3VO> findByProteinId(final Integer proteinId) throws Exception {
		return this.findFiltered(null, proteinId, null, null, null, null, null, null, null, null);
	}

	public List<BLSample3VO> findByProposalIdAndIsInSampleChanger(final Integer proposalId, final Byte isInSampleChanger)
			throws Exception {
		return this.findFiltered(proposalId, null, null, null, null, null, null, isInSampleChanger, null, null);
	}

	public List<BLSample3VO> findByAcronymAndProposalId(final String name, final Integer proposalId, final String sortType)
			throws Exception {
		LOG.info("Name: " + name + " proposalId: " + proposalId);
		return this.findFiltered(proposalId, null, null, null, name, null, null, null, null, sortType);
	}

	public List<BLSample3VO> findByNameAndProteinId(final String name, final Integer proteinId, final Integer shippingId)
			throws Exception {
		
		return this.findFiltered(null, proteinId, null, null, name, null, null, null, shippingId, null);

	}

	public List<BLSample3VO> findByCodeAndProposalId(final String dmCode, final Integer proposalId) throws Exception {
		return this.findFiltered(proposalId, null, null, null, null, dmCode, null, null, null, null);

	}

	public void resetIsInSampleChanger(final Integer proposalId) throws Exception {
		List<BLSample3VO> list = this.findByProposalId(proposalId);
		for (BLSample3VO vo : list) {
			vo.setIsInSampleChanger(new Byte((new Integer(0)).byteValue()));
			this.update(vo);
		}

	}
	
	@SuppressWarnings("rawtypes")
	public SampleInfo findForWSSampleInfo(final Integer sampleId) throws Exception {
		EJBAccessTemplate template = new EJBAccessTemplate(LOG, context, this);
		List orders = (List) template.execute(new EJBAccessCallback() {
			public Object doInEJBAccess(Object parent) throws Exception {
				Query q = entityManager
						.createNativeQuery(" SELECT BLSample.blSampleId, BLSample.name, BLSample.code,  "
								+ "BLSample.holderLength, BLSample.location, BLSample.SMILES, BLSample.diffractionPlanId as BLSampleDiffractionPlanId, Protein.acronym, "
								+ "Crystal.crystalId, Crystal.spaceGroup, Crystal.cell_a, Crystal.cell_b, Crystal.cell_c, "
								+ "Crystal.cell_alpha, Crystal.cell_beta, Crystal.cell_gamma, "
								+ "Crystal.diffractionPlanId as CrystalDiffractionPlanId, "
								+ "Container.sampleChangerLocation, Container.code as containerCode "
								+ "FROM BLSample, Crystal, Protein,Container "
								+ "WHERE BLSample.crystalId=Crystal.crystalId AND "
								+ "Crystal.proteinId=Protein.proteinId AND "
								+ "BLSample.containerId=Container.containerId "
								+ " AND BLSample.blSampleId = " + sampleId);
				List orders = q.getResultList();
				return orders;
			}
		});

		SampleInfo sampleInfo = null;

		Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
		DiffractionPlan3Service diffractionPlanService = (DiffractionPlan3Service) ejb3ServiceLocator
		.getLocalService(DiffractionPlan3Service.class);

		if (orders != null && orders.size() > 0){
			Object[] o = (Object[]) orders.get(0);
			int j=0;
			Integer blSampleId = (Integer) o[j++];
			String sampleName = (String) o[j++];
			String sampleCode = (String) o[j++];
			Double holderLength = (Double) o[j++];
			String sampleLocation = (String) o[j++];
			String smiles = (String) o[j++];
			Integer sampleDiffractionPlanId = (Integer) o[j++];
			String proteinAcronym = (String) o[j++];
			Integer crystalId = (Integer) o[j++];
			String crystalSpaceGroup = (String) o[j++];
			Double cellA = (Double) o[j++];
			Double cellB = (Double) o[j++];
			Double cellC = (Double) o[j++];
			Double cellAlpha = (Double) o[j++];
			Double cellBeta = (Double) o[j++];
			Double cellGamma = (Double) o[j++];
			Integer crystalDiffractionPlanId = (Integer) o[j++];
			String containerSCLocation = (String) o[j++];
			String containerCode = (String) o[j++];
			DiffractionPlan3VO diffractionPlan = new DiffractionPlan3VO();
			String blSampleImage = getSampleImagepath(blSampleId);
			
			if (sampleDiffractionPlanId != null) {
				diffractionPlan = diffractionPlanService.findByPk(sampleDiffractionPlanId, false, false);
			} else if (crystalDiffractionPlanId != null)
				diffractionPlan = diffractionPlanService.findByPk(crystalDiffractionPlanId, false, false);
			Double minimalResolution = diffractionPlan.getMinimalResolution();
			String experimentType = diffractionPlan.getExperimentKind();
			DiffractionPlanWS3VO diffPlanws = new DiffractionPlanWS3VO(diffractionPlan);
			sampleInfo = new SampleInfo(blSampleId, sampleName, sampleCode,
						 holderLength,  sampleLocation,  smiles, proteinAcronym, crystalId,
						 crystalSpaceGroup, cellA, cellB, cellC, cellAlpha, cellBeta, cellGamma, minimalResolution,
						 experimentType, containerSCLocation, containerCode, diffPlanws, blSampleImage) ;
		}
		return sampleInfo ;
	}
	

	/* Private methods ------------------------------------------------------ */

	private String getSampleImagepath(Integer blSampleId) {
		
		// we suppose that only 1 image is created for now for 1 BLSample 
		try {
			Set<BLSampleImage3VO> blsampleImageVOs = this.findByPk(blSampleId, false, false, true).getBlsampleImageVOs();
			if (blsampleImageVOs != null && !blsampleImageVOs.isEmpty()) {	 
				BLSampleImage3VO vo = (BLSampleImage3VO) blsampleImageVOs.toArray()[0];	
				return vo.getImageFullPath();
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
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
	private void checkAndCompleteData(BLSample3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getBlSampleId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getBlSampleId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

}
