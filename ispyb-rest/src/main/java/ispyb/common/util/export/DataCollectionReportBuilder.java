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
import ispyb.server.mx.services.ws.rest.datacollectiongroup.DataCollectionSummary;

/**
 * Builds curated {@link DataCollectionReportRow} DTOs out of the
 * {@link DataCollectionSummary} rows returned by
 * {@code WebServiceDataCollectionGroup3Service.getViewDataCollectionBySessionIdHavingImages}
 * (the same data source the PDF/RTF exporter uses, via its own raw-map path).
 * <p>
 * Unlike {@link ExiPdfRtfExporter}, this builder only surfaces the columns
 * needed for the industrial-client CSV summary; it reuses
 * {@link AutoProcBestResultExtractor} for the "best" autoprocessing result so
 * the Processed Space Group / Processed Resolution values are computed the
 * same way as in the PDF.
 */
public class DataCollectionReportBuilder {

	private final AutoProcBestResultExtractor autoProcBestResultExtractor = new AutoProcBestResultExtractor();

	/**
	 * Builds one {@link DataCollectionReportRow} per data-collection-group
	 * summary.
	 *
	 * @param dataCollections  rows as returned by
	 *                         {@code getViewDataCollectionBySessionIdHavingImages}
	 * @param spaceGroupService supplied by the caller (e.g. the REST resource's
	 *                          own inherited {@code getSpaceGroup3Service()}) —
	 *                          this builder is a plain utility and doesn't
	 *                          fetch services itself.
	 */
	public List<DataCollectionReportRow> build(List<DataCollectionSummary> dataCollections, SpaceGroup3Service spaceGroupService) throws Exception {
		return build(dataCollections, spaceGroupMap(spaceGroupService));
	}

	/**
	 * Builds the space-group-number lookup once, so a caller that wants to
	 * map many rows lazily (e.g. via {@code Stream.map(row -> buildRow(row, spgMap))}
	 * for a streamed CSV export) doesn't recompute it per row.
	 */
	public Map<String, Integer> spaceGroupMap(SpaceGroup3Service spaceGroupService) throws Exception {
		return autoProcBestResultExtractor.buildSpaceGroupNumberMap(spaceGroupService);
	}

	/**
	 * Package-private seam for tests: same as {@link #build(List, SpaceGroup3Service)}
	 * but takes an already-built {@code spgMap} (see
	 * {@link AutoProcBestResultExtractor#buildSpaceGroupNumberMap}) directly,
	 * instead of a real {@code SpaceGroup3Service}, so tests don't need to
	 * mock one.
	 */
	List<DataCollectionReportRow> build(List<DataCollectionSummary> dataCollections, Map<String, Integer> spgMap) throws Exception {

		if (dataCollections == null) {
			return Collections.emptyList();
		}

		try {
			return dataCollections.stream()
					.map(dataCollectionSummary -> buildRowUnchecked(dataCollectionSummary, spgMap))
					.collect(Collectors.toList());
		} catch (RowBuildException e) {
			throw (Exception) e.getCause();
		}
	}

	/**
	 * Unchecked carrier for the checked {@link Exception} that {@link #buildRow}
	 * declares, so it can cross lambda boundaries (e.g. {@link java.util.stream.Stream#map})
	 * where checked exceptions aren't allowed — used internally by {@link #build}
	 * and available to callers building their own lazy row stream (see
	 * {@link #buildRowUnchecked}) for a streamed export, so they can catch and
	 * unwrap it at their own error-handling boundary (e.g. converting to
	 * {@code IOException} inside a JAX-RS {@code StreamingOutput}).
	 */
	public static final class RowBuildException extends RuntimeException {
		private RowBuildException(Exception cause) {
			super(cause);
		}
	}

	/**
	 * Same as {@link #buildRow} but wraps the checked {@link Exception} in the
	 * unchecked {@link RowBuildException} so it can be used directly as a
	 * {@code Stream.map} mapper — e.g. {@code dataCollections.stream().map(m ->
	 * builder.buildRowUnchecked(m, spgMap))} for a lazily-built, streamed CSV
	 * export. Public for that reuse; {@link #build} also uses it internally.
	 */
	public DataCollectionReportRow buildRowUnchecked(DataCollectionSummary dataCollectionSummary, Map<String, Integer> spgMap) {
		try {
			return buildRow(dataCollectionSummary, spgMap);
		} catch (Exception e) {
			throw new RowBuildException(e);
		}
	}

	/**
	 * Builds a single {@link DataCollectionReportRow} from one
	 * {@link DataCollectionSummary}. Public so a streaming caller can map a
	 * {@code Stream<DataCollectionSummary>} lazily (one row built and
	 * serialized at a time) instead of collecting the whole
	 * {@code List<DataCollectionReportRow>} up front via {@link #build(List, Map)}.
	 */
	public DataCollectionReportRow buildRow(DataCollectionSummary dataCollectionSummary, Map<String, Integer> spgMap) throws Exception {

		String[] bestAutoproc = autoProcBestResultExtractor.extractBestAutoproc(dataCollectionSummary, spgMap);

		return new DataCollectionReportRow(
				dataCollectionSummary.proteinAcronym(),
				dataCollectionSummary.sampleName(),
				dataCollectionSummary.experimentType(),
				dataCollectionSummary.dataCollectionComments(),
				dataCollectionSummary.dataCollectionResolution(),
				bestAutoproc != null ? nullToEmpty(bestAutoproc[0]) : "",
				extractOuterHighResolution(bestAutoproc),
				dataCollectionSummary.dataCollectionGroupComments());
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

	private String nullToEmpty(String value) {
		return value != null ? value : "";
	}
}
