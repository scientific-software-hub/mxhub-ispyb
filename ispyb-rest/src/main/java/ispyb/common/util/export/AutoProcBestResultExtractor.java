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
 ******************************************************************************/

package ispyb.common.util.export;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import ispyb.server.mx.services.autoproc.SpaceGroup3Service;
import ispyb.server.mx.vos.autoproc.SpaceGroup3VO;

/**
 * Picks the "best" autoprocessing result (space group, Rmerge, completeness
 * and resolution for the Overall/Inner/Outer resolution shells, plus unit
 * cell) out of a data-collection-group summary row.
 * <p>
 * Extracted verbatim (behavior-identical) from
 * {@code ExiPdfRtfExporter#extractBestAutoproc(Map)} so the selection logic
 * can be shared between the PDF/RTF exporter and the CSV report builder
 * ({@link DataCollectionReportBuilder}) instead of being duplicated.
 * <p>
 * The index-tracking selection loops below (running {@code indexRmergeMin} /
 * {@code outerIndex} / {@code overallIndex} across several parallel lists)
 * are left as classic for-loops rather than streams: they carry mutable
 * cross-iteration state and correlate positions across multiple lists, which
 * streams would obscure rather than clarify.
 */
public class AutoProcBestResultExtractor {

	private final Logger logger = Logger.getLogger(AutoProcBestResultExtractor.class);

	public static final double MIN_RMERGE = 10;

	/**
	 * Space-group-name -&gt; space-group-number lookup used to prefer the
	 * highest-symmetry autoprocessing result. Mirrors the loop previously
	 * inlined in {@code ExiPdfRtfExporter.init()}.
	 * <p>
	 * Takes {@code spacegroupService} as a parameter rather than fetching it
	 * itself (neither via {@code Ejb3ServiceLocator} nor {@code @Inject}) —
	 * this class is a plain, stateless utility instantiated with {@code new}
	 * per report; obtaining the service is the caller's responsibility (e.g.
	 * via the REST resource's own inherited {@code getSpaceGroup3Service()}).
	 */
	public Map<String, Integer> buildSpaceGroupNumberMap(SpaceGroup3Service spacegroupService) throws Exception {
		Map<String, Integer> spgMap = new HashMap<String, Integer>();
		List<SpaceGroup3VO> spaceGroups = spacegroupService.findAll();
		spaceGroups.forEach(spg -> spgMap.put(spg.getSpaceGroupName(), spg.getSpaceGroupNumber()));
		return spgMap;
	}

	/**
	 * @param dataCollectionMapItem one row of {@code v_datacollection_summary}
	 *                              (GROUP_CONCAT'd autoproc columns)
	 * @param spgMap                space-group-name -&gt; number, see
	 *                              {@link #buildSpaceGroupNumberMap}
	 * @return an 18-element array:
	 *         <pre>
	 *         [0]  space group (inner/best)      [1]  Rmerge inner        [2]  completeness inner    [3]  resolution inner  (low/high)
	 *         [4]  cell a                        [5]  cell b              [6]  cell c
	 *         [7]  cell alpha                    [8]  cell beta           [9]  cell gamma
	 *         [10] space group outer             [11] Rmerge outer        [12] completeness outer    [13] resolution outer  (low/high)
	 *         [14] space group overall           [15] Rmerge overall      [16] completeness overall  [17] resolution overall (low/high)
	 *         </pre>
	 *         or {@code null} if the row carries no autoprocessing statistics.
	 */
	public String[] extractBestAutoproc(Map<String, Object> dataCollectionMapItem, Map<String, Integer> spgMap) throws Exception {

		DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df2.applyPattern("#####0.00");

		String[] bestRmerge = null;
		String listString = (String) dataCollectionMapItem.get("completenessList");

		if (dataCollectionMapItem.get("completenessList") != null && !listString.isEmpty() && dataCollectionMapItem.get("AutoProc_spaceGroups") != null) {

			listString.trim();
			List<String> completenessList = new ArrayList<String>(Arrays.asList((listString.split(","))));
			logger.debug("completenessList = " + completenessList.toString());
			List<String> spaceGroupsList = new ArrayList<String>(Arrays.asList(((String) dataCollectionMapItem.get("AutoProc_spaceGroups")).trim().split(",")));
			logger.debug("spaceGroupsList = " + spaceGroupsList.size() + spaceGroupsList.toString());
			List<String> resolutionsLimitLowList = new ArrayList<String>(Arrays.asList(((String) dataCollectionMapItem.get("resolutionsLimitLow")).trim().split(",")));
			logger.debug("resolutionsLimitLowList = " + resolutionsLimitLowList.size() + resolutionsLimitLowList.toString());
			List<String> resolutionsLimitHighList = new ArrayList<String>(Arrays.asList(((String) dataCollectionMapItem.get("resolutionsLimitHigh")).trim().split(",")));
			logger.debug("resolutionsLimitHighList = " + resolutionsLimitHighList.toString());
			List<String> rmergesList = new ArrayList<String>(Arrays.asList(((String) dataCollectionMapItem.get("rMerges")).trim().split(",")));
			logger.debug("rmergesList = " + rmergesList.size() + rmergesList.toString());
			List<String> scalingStatisticsTypesList = new ArrayList<String>(Arrays.asList(((String) dataCollectionMapItem.get("scalingStatisticsTypes")).trim().split(",")));
			logger.debug("scalingStatisticsTypesList = " + scalingStatisticsTypesList.size() + scalingStatisticsTypesList.toString());
			List<String> anomalousList = new ArrayList<String>(Arrays.asList(((String) dataCollectionMapItem.get("Autoprocessing_anomalous")).trim().split(",")));
			logger.debug("anomalousList = " + anomalousList.size() + anomalousList.toString());

			bestRmerge = new String[18];
			int i = 0;
			Double rmergeMin = 1000.000;
			int indexRmergeMin = 0;
			Set<Integer> indexSet = new HashSet<Integer>();

			for (Iterator<String> iterator = scalingStatisticsTypesList.iterator(); iterator.hasNext();) {
				String type = (String) iterator.next();
				// select also no anom
				if (type.contains("innerShell") && Integer.parseInt(anomalousList.get(i).trim()) < 1
						&& (rmergesList.size() >= i)) {
					double rm = Double.parseDouble(rmergesList.get(i));
					logger.debug("rm = " + rm);
					if (rm > 0 && rm < MIN_RMERGE) {
						indexSet.add(i);
						logger.debug("index kept: " + i);
					} else if (rm > 0 && rm < rmergeMin) {
						rmergeMin = rm;
						indexRmergeMin = i;
					}
				}
				i = i + 1;
			}

			// select higher symmetry for rMerge < 10
			if (!indexSet.isEmpty()) {
				String spgTemp;
				int spgNb = 0;
				double rMergeMin = 10;

				for (Iterator<Integer> iterator = indexSet.iterator(); iterator.hasNext();) {
					Integer index = (Integer) iterator.next();
					spgTemp = spaceGroupsList.get(index).trim();
					double rMergeMinTemp = Double.parseDouble(rmergesList.get(index));
					logger.debug("index : " + index + " spgtemp: " + spgTemp);

					if (spgMap.get(spgTemp) != null && spgNb == spgMap.get(spgTemp).intValue()) {
						if (rMergeMinTemp < rMergeMin) {
							rMergeMin = rMergeMinTemp;
							spgNb = spgMap.get(spgTemp).intValue();
							logger.debug("index : " + index + " spgNb: " + spgMap.get(spgTemp));
							indexRmergeMin = index;
						}
					} else if (spgMap.get(spgTemp) != null && spgNb < spgMap.get(spgTemp).intValue()) {

						spgNb = spgMap.get(spgTemp).intValue();
						rMergeMin = Double.parseDouble(rmergesList.get(index));
						logger.debug("index : " + index + " spgNb: " + spgMap.get(spgTemp));
						indexRmergeMin = index;
					}
				}
			}

			bestRmerge[0] = spaceGroupsList.get(indexRmergeMin);
			bestRmerge[1] = rmergesList.get(indexRmergeMin);
			bestRmerge[2] = completenessList.get(indexRmergeMin);
			bestRmerge[3] = getDecimalFormat(resolutionsLimitLowList.get(indexRmergeMin), df2) + "/"
					+ getDecimalFormat(resolutionsLimitHighList.get(indexRmergeMin), df2);

			List<String> tmpList = new ArrayList<String>(Arrays.asList(((String) dataCollectionMapItem.get("Autoprocessing_cell_a")).trim().split(",")));
			bestRmerge[4] = tmpList.get(indexRmergeMin);
			tmpList = new ArrayList<String>(Arrays.asList(((String) dataCollectionMapItem.get("Autoprocessing_cell_b")).trim().split(",")));
			bestRmerge[5] = tmpList.get(indexRmergeMin);
			tmpList = new ArrayList<String>(Arrays.asList(((String) dataCollectionMapItem.get("Autoprocessing_cell_c")).trim().split(",")));
			bestRmerge[6] = tmpList.get(indexRmergeMin);
			tmpList = new ArrayList<String>(Arrays.asList(((String) dataCollectionMapItem.get("Autoprocessing_cell_alpha")).trim().split(",")));
			bestRmerge[7] = tmpList.get(indexRmergeMin);
			tmpList = new ArrayList<String>(Arrays.asList(((String) dataCollectionMapItem.get("Autoprocessing_cell_beta")).trim().split(",")));
			bestRmerge[8] = tmpList.get(indexRmergeMin);
			tmpList = new ArrayList<String>(Arrays.asList(((String) dataCollectionMapItem.get("Autoprocessing_cell_gamma")).trim().split(",")));
			bestRmerge[9] = tmpList.get(indexRmergeMin);

			// outer
			int outerIndex = -1;
			if (scalingStatisticsTypesList.get(indexRmergeMin + 1).contains("outerShell")) {
				outerIndex = indexRmergeMin + 1;
			} else if (indexRmergeMin + 2 < scalingStatisticsTypesList.size() && scalingStatisticsTypesList.get(indexRmergeMin + 2).contains("outerShell")) {
				outerIndex = indexRmergeMin + 2;
			}
			if (outerIndex > -1) {
				bestRmerge[10] = spaceGroupsList.get(outerIndex);
				bestRmerge[11] = rmergesList.get(outerIndex);
				bestRmerge[12] = completenessList.get(outerIndex);
				bestRmerge[13] = getDecimalFormat(resolutionsLimitLowList.get(outerIndex), df2) + "/"
						+ getDecimalFormat(resolutionsLimitHighList.get(outerIndex), df2);
			}

			// overall
			int overallIndex = -1;
			if (indexRmergeMin - 1 >= 0 && scalingStatisticsTypesList.get(indexRmergeMin - 1).contains("overall")) {
				overallIndex = indexRmergeMin - 1;
			} else if (indexRmergeMin - 2 >= 0 && scalingStatisticsTypesList.get(indexRmergeMin - 2).contains("overall")) {
				overallIndex = indexRmergeMin - 2;
			} else if (indexRmergeMin + 2 < scalingStatisticsTypesList.size() && scalingStatisticsTypesList.get(indexRmergeMin + 2).contains("overall")) {
				overallIndex = indexRmergeMin + 2;
			} else if (indexRmergeMin + 3 < scalingStatisticsTypesList.size() && scalingStatisticsTypesList.get(indexRmergeMin + 3).contains("overall")) {
				overallIndex = indexRmergeMin + 3;
			}

			if (overallIndex > -1) {
				bestRmerge[14] = spaceGroupsList.get(overallIndex);
				bestRmerge[15] = rmergesList.get(overallIndex);
				bestRmerge[16] = completenessList.get(overallIndex);
				bestRmerge[17] = getDecimalFormat(resolutionsLimitLowList.get(overallIndex), df2) + "/"
						+ getDecimalFormat(resolutionsLimitHighList.get(overallIndex), df2);
			}
			logger.info("bestRmerge = " + bestRmerge[0] + "- " + bestRmerge[1] + "- " + bestRmerge[2] + "- " + bestRmerge[3]);
		}

		return bestRmerge;
	}

	private String getDecimalFormat(String value, DecimalFormat df) {
		if (value == null || value.isEmpty()) {
			return "";
		}
		try {
			return df.format(Double.parseDouble(value));
		} catch (NumberFormatException e) {
			return "";
		}
	}
}
