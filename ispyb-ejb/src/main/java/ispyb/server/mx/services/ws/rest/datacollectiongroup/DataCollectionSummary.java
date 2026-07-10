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

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Typed facade over one raw {@code v_datacollection_summary} row (as returned
 * by the {@code getViewDataCollection*} family of methods), which the
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
 * <p>
 * <b>Implements {@code Map<String, Object>}</b> — delegating every method to
 * the backing row — rather than merely holding one, so this stays a drop-in
 * replacement for the raw map on the REST endpoints that serialize it
 * straight to JSON (via {@code ParentWebService#sendResponse}, backed by Gson
 * configured with {@code excludeFieldsWithModifiers(Modifier.PRIVATE)}).
 * Gson's {@code MapTypeAdapterFactory} matches any runtime type assignable to
 * {@code Map} and serializes it by iterating {@code entrySet()}, so this
 * produces byte-identical JSON to the raw map it replaces instead of the
 * {@code {}} a plain (non-{@code Map}) wrapper with a private field would
 * yield under that field-exclusion config. Mutability (e.g.
 * {@code StatsWebService} does {@code result.put("stats", ...)} on a row
 * before serializing it) is preserved by delegating writes to the backing
 * row too.
 * <p>
 * <b>Implements {@code Serializable}</b> because {@link DataCollectionGroupRestWsService}
 * is a {@code @Remote} EJB business interface: OpenEJB/CXF marshals
 * {@code @Remote} return values via Java serialization even for in-VM calls
 * (unlike {@code @Local}, which passes by reference), so every element of a
 * returned {@code List} must itself be {@code Serializable} — as the raw
 * {@code HashMap} rows were before this class replaced them.
 */
public class DataCollectionSummary implements Map<String, Object>, Serializable {

	private static final long serialVersionUID = 1L;

	private final Map<String, Object> row;

	private DataCollectionSummary(Map<String, Object> row) {
		this.row = row;
	}

	public static DataCollectionSummary from(Map<String, Object> row) {
		return new DataCollectionSummary(row);
	}

	/**
	 * The full row as a plain {@code Map}, for callers not migrated to typed
	 * getters (e.g. the deprecated PDF/RTF exporter, or the autoprocessing
	 * best-result extractor, which reads the {@code GROUP_CONCAT} list
	 * columns). Since this class already implements {@code Map}, this simply
	 * returns {@code this}.
	 */
	public Map<String, Object> asMap() {
		return this;
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

	// -- Map<String, Object>, delegating to the backing row --

	@Override
	public int size() {
		return row.size();
	}

	@Override
	public boolean isEmpty() {
		return row.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return row.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return row.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return row.get(key);
	}

	@Override
	public Object put(String key, Object value) {
		return row.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return row.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ?> m) {
		row.putAll(m);
	}

	@Override
	public void clear() {
		row.clear();
	}

	@Override
	public Set<String> keySet() {
		return row.keySet();
	}

	@Override
	public Collection<Object> values() {
		return row.values();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return row.entrySet();
	}

	@Override
	public boolean equals(Object o) {
		return row.equals(o);
	}

	@Override
	public int hashCode() {
		return row.hashCode();
	}
}
