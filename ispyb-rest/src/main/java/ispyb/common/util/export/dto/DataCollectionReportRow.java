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

package ispyb.common.util.export.dto;

/**
 * One row of the session data-collection report, curated for the CSV export
 * (see {@code ispyb.common.util.export.DataCollectionReportBuilder}).
 * <p>
 * This mirrors the subset of {@code ExiPdfRtfExporter}'s per-row data that the
 * CSV export needs, decoupled from the raw {@code Map<String, Object>} rows
 * produced by the {@code v_datacollection_summary} view and from the PDF's
 * rendering concerns.
 *
 * @param proteinAcronym      protein/target name ({@code Protein_acronym})
 * @param sampleName           sample/crystal name ({@code BLSample_name})
 * @param status               experiment kind, e.g. "OSC" or "Characterization"
 *                             ({@code DataCollectionGroup_experimentType})
 * @param diffractionNote      per-collection comment editable in EXI's "Data
 *                             collections" tab ({@code DataCollection_comments})
 * @param recordedResolution   resolution recorded on the data collection
 *                             ({@code DataCollection_resolution}); the manual
 *                             "Observed Resolution" column is left blank by
 *                             the client and is not part of this DTO
 * @param processedSpaceGroup  space group of the best autoprocessing result
 * @param processedResolution  outer-shell high-resolution limit of the best
 *                             autoprocessing result
 * @param comment              MXCuBE comment box ({@code DataCollectionGroup_comments})
 */
public record DataCollectionReportRow(
		String proteinAcronym,
		String sampleName,
		String status,
		String diffractionNote,
		String recordedResolution,
		String processedSpaceGroup,
		String processedResolution,
		String comment) {
}
