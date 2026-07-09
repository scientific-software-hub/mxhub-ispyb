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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ispyb.common.util.export.dto.DataCollectionReportRow;
import ispyb.server.mx.services.autoproc.SpaceGroup3Service;

/**
 * Builds curated {@link DataCollectionReportRow} DTOs out of the raw
 * {@code List<Map<String, Object>>} rows returned by
 * {@code WebServiceDataCollectionGroup3Service.getViewDataCollectionBySessionIdHavingImages}
 * (the same data source the PDF/RTF exporter uses).
 * <p>
 * Unlike {@link ExiPdfRtfExporter}, this builder only surfaces the columns
 * needed for the industrial-client CSV summary; it reuses
 * {@link AutoProcBestResultExtractor} for the "best" autoprocessing result so
 * the Processed Space Group / Processed Resolution values are computed the
 * same way as in the PDF.
 * <p>
 * Note: the raw {@code List<Map<String, Object>>} row shape (untyped view
 * columns like {@code "Protein_acronym"}, {@code "DataCollection_comments"})
 * is itself a candidate for its own DTO; kept out of scope here as a larger,
 * separate refactor since it also underlies {@link ExiPdfRtfExporter}.
 */
public class DataCollectionReportBuilder {

	private final AutoProcBestResultExtractor autoProcBestResultExtractor = new AutoProcBestResultExtractor();

	/**
	 * Builds one {@link DataCollectionReportRow} per data-collection-group map.
	 *
	 * @param dataCollections  rows as returned by
	 *                         {@code getViewDataCollectionBySessionIdHavingImages}
	 * @param spaceGroupService supplied by the caller (e.g. the REST resource's
	 *                          own inherited {@code getSpaceGroup3Service()}) —
	 *                          this builder is a plain utility and doesn't
	 *                          fetch services itself.
	 */
	public List<DataCollectionReportRow> build(List<Map<String, Object>> dataCollections, SpaceGroup3Service spaceGroupService) throws Exception {
		return build(dataCollections, autoProcBestResultExtractor.buildSpaceGroupNumberMap(spaceGroupService));
	}

	/**
	 * Package-private seam for tests: same as {@link #build(List, SpaceGroup3Service)}
	 * but takes an already-built {@code spgMap} (see
	 * {@link AutoProcBestResultExtractor#buildSpaceGroupNumberMap}) directly,
	 * instead of a real {@code SpaceGroup3Service}, so tests don't need to
	 * mock one.
	 */
	List<DataCollectionReportRow> build(List<Map<String, Object>> dataCollections, Map<String, Integer> spgMap) throws Exception {

		if (dataCollections == null) {
			return Collections.emptyList();
		}

		try {
			return dataCollections.stream()
					.map(dataCollectionMapItem -> buildRowUnchecked(dataCollectionMapItem, spgMap))
					.collect(Collectors.toList());
		} catch (RowBuildException e) {
			throw e.cause;
		}
	}

	/**
	 * {@link Collectors#toList()} can't propagate the checked {@link Exception}
	 * thrown by {@link #buildRow}, so wrap it in this unchecked carrier and
	 * unwrap it in {@link #build}.
	 */
	private static final class RowBuildException extends RuntimeException {
		private final Exception cause;

		private RowBuildException(Exception cause) {
			this.cause = cause;
		}
	}

	private DataCollectionReportRow buildRowUnchecked(Map<String, Object> dataCollectionMapItem, Map<String, Integer> spgMap) {
		try {
			return buildRow(dataCollectionMapItem, spgMap);
		} catch (Exception e) {
			throw new RowBuildException(e);
		}
	}

	private DataCollectionReportRow buildRow(Map<String, Object> dataCollectionMapItem, Map<String, Integer> spgMap) throws Exception {

		String[] bestAutoproc = autoProcBestResultExtractor.extractBestAutoproc(dataCollectionMapItem, spgMap);

		return new DataCollectionReportRow(
				getCellParam(dataCollectionMapItem, "Protein_acronym"),
				getCellParam(dataCollectionMapItem, "BLSample_name"),
				getCellParam(dataCollectionMapItem, "DataCollectionGroup_experimentType"),
				getCellParam(dataCollectionMapItem, "DataCollection_comments"),
				getCellParam(dataCollectionMapItem, "DataCollection_resolution"),
				bestAutoproc != null ? nullToEmpty(bestAutoproc[0]) : "",
				extractOuterHighResolution(bestAutoproc),
				getCellParam(dataCollectionMapItem, "DataCollectionGroup_comments"));
	}

	/**
	 * The "smallest number" the client wants for Processed Resolution: the
	 * high-resolution limit of the outer shell. {@code bestAutoproc[13]} is
	 * formatted by {@link AutoProcBestResultExtractor} as {@code "low/high"};
	 * take the high part rather than re-deriving it, so this always matches
	 * what the PDF's own data (not its swapped column labels) would show.
	 */
	private String extractOuterHighResolution(String[] bestAutoproc) {
		if (bestAutoproc == null || bestAutoproc[13] == null || bestAutoproc[13].isEmpty()) {
			return "";
		}
		String[] lowHigh = bestAutoproc[13].split("/");
		return lowHigh.length == 2 ? lowHigh[1].trim() : "";
	}

	/**
	 * Null-safe scalar accessor, mirroring
	 * {@code ExiPdfRtfExporter.getCellParam(map, param, null)}.
	 */
	private String getCellParam(Map<String, Object> dataCollectionMap, String param) {
		Object value = dataCollectionMap.get(param);
		return value != null ? value.toString() : "";
	}

	private String nullToEmpty(String value) {
		return value != null ? value : "";
	}
}
