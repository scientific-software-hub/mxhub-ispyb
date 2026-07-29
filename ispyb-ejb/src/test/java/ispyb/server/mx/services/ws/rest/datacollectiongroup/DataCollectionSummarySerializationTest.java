package ispyb.server.mx.services.ws.rest.datacollectiongroup;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link DataCollectionGroupRestWsService} is a {@code @Remote} EJB business
 * interface, so OpenEJB/CXF marshals every returned value via Java
 * serialization ({@code ObjectOutputStream}) even for in-VM calls — unlike
 * {@code @Local}, which passes by reference. A prior version of
 * {@link DataCollectionSummary} was not {@code Serializable} and broke every
 * production call to a {@code getViewDataCollection*} method with a
 * {@code NotSerializableException}, invisible to the rest of the test suite
 * because {@code DataCollectionReportCsvTest} obtains the service via
 * {@code @Inject}, which does not go through the {@code @Remote} proxy's
 * marshaling path. This test exercises actual Java (de)serialization to
 * cover exactly the gap that let that regression through.
 */
class DataCollectionSummarySerializationTest {

	private Map<String, Object> representativeRow() {
		Map<String, Object> row = new LinkedHashMap<>();
		row.put("DataCollectionGroup_dataCollectionGroupId", 42);
		row.put("Protein_acronym", "HEWL");
		row.put("BLSample_name", "s1");
		return row;
	}

	@SuppressWarnings("unchecked")
	private <T> T roundTrip(T value) throws Exception {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
			out.writeObject(value);
		}
		try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
			return (T) in.readObject();
		}
	}

	@Test
	void isSerializable() {
		assertInstanceOf(Serializable.class, DataCollectionSummary.from(representativeRow()));
	}

	@Test
	void singleSummary_survivesJavaSerializationRoundTrip() throws Exception {
		DataCollectionSummary summary = DataCollectionSummary.from(representativeRow());

		DataCollectionSummary restored = roundTrip(summary);

		assertEquals(summary, restored);
		assertEquals("HEWL", restored.proteinAcronym());
	}

	/**
	 * Mirrors the actual failure mode: a {@code List<DataCollectionSummary>}
	 * returned by a {@code @Remote} EJB method, serialized as a whole
	 * (matching {@code ArrayList.writeObject} in the reported stack trace).
	 */
	@Test
	void listOfSummaries_survivesJavaSerializationRoundTrip() throws Exception {
		List<DataCollectionSummary> summaries = new ArrayList<>(Arrays.asList(
				DataCollectionSummary.from(representativeRow()), DataCollectionSummary.from(representativeRow())));

		List<DataCollectionSummary> restored = roundTrip(summaries);

		assertEquals(2, restored.size());
		assertTrue(restored.stream().allMatch(s -> "HEWL".equals(s.proteinAcronym())));
	}
}
