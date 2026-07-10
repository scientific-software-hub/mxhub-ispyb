package ispyb.server.mx.services.ws.rest.datacollectiongroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Proves {@link DataCollectionSummary} serializes to exactly the same JSON as
 * the raw {@code Map<String, Object>} row it wraps, under the same Gson
 * configuration {@code ispyb.ws.ParentWebService#sendResponse} uses
 * ({@code excludeFieldsWithModifiers(Modifier.PRIVATE)}, with and without
 * {@code serializeNulls()}).
 * <p>
 * This is the load-bearing assumption behind migrating the JSON-passthrough
 * {@code getViewDataCollection*} methods from {@code List<Map<String, Object>>}
 * to {@code List<DataCollectionSummary>}: a plain wrapper class with a
 * private backing-map field would serialize to {@code {}} under
 * {@code excludeFieldsWithModifiers(Modifier.PRIVATE)} (its only field is
 * excluded), but {@link DataCollectionSummary} implements {@code Map} itself,
 * so Gson's {@code MapTypeAdapterFactory} — which matches any type assignable
 * to {@code Map} ahead of the reflective bean adapter — serializes it by
 * iterating {@code entrySet()} instead, unaffected by field exclusion.
 */
class DataCollectionSummaryGsonParityTest {

	// Mirrors ispyb.ws.ParentWebService#newGson(boolean) for serializeNull=false.
	private Gson gsonWithoutNulls() {
		return new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).serializeSpecialFloatingPointValues()
				.create();
	}

	// Mirrors ispyb.ws.ParentWebService#newGson() (serializeNulls=true path).
	private Gson gsonWithNulls() {
		return new GsonBuilder().serializeNulls().excludeFieldsWithModifiers(Modifier.PRIVATE)
				.serializeSpecialFloatingPointValues().create();
	}

	private Map<String, Object> representativeRow() {
		Map<String, Object> row = new LinkedHashMap<>();
		row.put("DataCollectionGroup_dataCollectionGroupId", 42);
		row.put("Protein_acronym", "HEWL");
		row.put("BLSample_name", "s1");
		row.put("DataCollection_comments", null);
		row.put("transmission", 12.5);
		return row;
	}

	@Test
	void singleRow_serializesIdenticallyToRawMap_withoutNulls() {
		Map<String, Object> row = representativeRow();
		Gson gson = gsonWithoutNulls();

		assertEquals(gson.toJson(row), gson.toJson(DataCollectionSummary.from(row)));
	}

	@Test
	void singleRow_serializesIdenticallyToRawMap_withNulls() {
		Map<String, Object> row = representativeRow();
		Gson gson = gsonWithNulls();

		assertEquals(gson.toJson(row), gson.toJson(DataCollectionSummary.from(row)));
	}

	@Test
	void listOfRows_serializesIdenticallyToRawMapList() {
		List<Map<String, Object>> rawRows = Arrays.asList(representativeRow(), representativeRow());
		List<DataCollectionSummary> summaries = Arrays.asList(
				DataCollectionSummary.from(representativeRow()), DataCollectionSummary.from(representativeRow()));
		Gson gson = gsonWithoutNulls();

		assertEquals(gson.toJson(rawRows), gson.toJson(summaries));
	}

	/**
	 * {@code StatsWebService.getViewDataCollectionBySessionId} mutates a row
	 * ({@code result.put("stats", ...)}) before serializing it; confirms the
	 * facade's {@code put} writes through to the backing row rather than
	 * being a read-only view.
	 */
	@Test
	void put_writesThroughToBackingRow_andIsReflectedInSerialization() {
		Map<String, Object> row = representativeRow();
		DataCollectionSummary summary = DataCollectionSummary.from(row);

		summary.put("stats", "some-stats-payload");

		assertEquals("some-stats-payload", row.get("stats"));
		Gson gson = gsonWithoutNulls();
		assertEquals(gson.toJson(row), gson.toJson(summary));
	}
}
