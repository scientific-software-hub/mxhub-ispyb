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

package ispyb.server.mx.services.ws.rest.datacollectiongroup;

import java.util.Map;

/**
 * Typed facade over one raw {@code v_datacollection_summary} row (as returned
 * by {@code getViewDataCollectionBySessionIdHavingImages}), which the
 * underlying native query still produces as an untyped
 * {@code Map<String, Object>} — the view is queried with {@code select *}
 * plus several {@code GROUP_CONCAT} derived columns, over 200 keys in total,
 * so this class does not attempt to model every column as a typed field.
 * <p>
 * Instead it wraps the raw map and exposes typed, null-safe getters for the
 * columns actually consumed by typed callers (currently the CSV export path,
 * see {@code ispyb.common.util.export.DataCollectionReportBuilder}), while
 * {@link #asMap()} keeps the full raw row available for callers that are not
 * (yet) migrated off the map — e.g. the deprecated PDF/RTF exporter, or the
 * autoprocessing {@code GROUP_CONCAT} list columns not yet promoted to typed
 * getters here.
 */
public class DataCollectionSummary {

	private final Map<String, Object> row;

	private DataCollectionSummary(Map<String, Object> row) {
		this.row = row;
	}

	public static DataCollectionSummary from(Map<String, Object> row) {
		return new DataCollectionSummary(row);
	}

	/**
	 * The full raw row, for callers not migrated to typed getters (e.g. the
	 * deprecated PDF/RTF exporter, or the autoprocessing best-result
	 * extractor, which reads the {@code GROUP_CONCAT} list columns).
	 */
	public Map<String, Object> asMap() {
		return row;
	}

	public String dataCollectionGroupId() {
		return getString("DataCollectionGroup_dataCollectionGroupId");
	}

	public String dataCollectionId() {
		return getString("DataCollection_dataCollectionId");
	}

	public String proteinAcronym() {
		return getString("Protein_acronym");
	}

	public String sampleName() {
		return getString("BLSample_name");
	}

	public String experimentType() {
		return getString("DataCollectionGroup_experimentType");
	}

	public String dataCollectionComments() {
		return getString("DataCollection_comments");
	}

	public String dataCollectionResolution() {
		return getString("DataCollection_resolution");
	}

	public String dataCollectionGroupComments() {
		return getString("DataCollectionGroup_comments");
	}

	/**
	 * Null-safe scalar accessor, mirroring
	 * {@code ExiPdfRtfExporter.getCellParam(map, param, null)}.
	 */
	private String getString(String key) {
		Object value = row.get(key);
		return value != null ? value.toString() : "";
	}
}
