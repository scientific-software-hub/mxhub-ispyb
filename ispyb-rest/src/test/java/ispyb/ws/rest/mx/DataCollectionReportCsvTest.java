package ispyb.ws.rest.mx;

import ispyb.TestBase;
import ispyb.common.util.export.DataCollectionReportBuilder;
import ispyb.common.util.export.DataCollectionReportCsvSerializer;
import ispyb.common.util.export.dto.DataCollectionReportRow;
import ispyb.server.mx.services.autoproc.SpaceGroup3Service;
import ispyb.server.mx.services.ws.rest.datacollectiongroup.DataCollectionGroupRestWsService;
import ispyb.server.mx.services.ws.rest.datacollectiongroup.DataCollectionSummary;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test for the session data-collection CSV export
 * ({@code DataCollectionRestWebService#getDataCollectionsReportBySessionIdCSV}),
 * run against a real Testcontainers MariaDB instead of hand-fed maps.
 * <p>
 * Exercises the same chain the REST endpoint does — real EJB query against
 * {@code v_datacollection_summary} ({@link DataCollectionGroupRestWsService})
 * → {@link DataCollectionReportBuilder} → {@link DataCollectionReportCsvSerializer}
 * — without going through JAX-RS/HTTP (no such test infra exists here). For
 * the same reason, the streamed {@code StreamingOutput} path the endpoint now
 * uses (see {@link #reportCsv_session1_streamingPathMatchesEagerPath}) is
 * verified by driving {@link DataCollectionReportBuilder#buildRowUnchecked}
 * and {@link DataCollectionReportCsvSerializer#writeCsv} directly against the
 * same real query results, rather than through JAX-RS.
 * <p>
 * This is the piece the unit tests ({@code AutoProcBestResultExtractorTest},
 * {@code DataCollectionReportBuilderTest}) cannot cover: the real query's
 * {@code GROUP_CONCAT(... SEPARATOR ', ')} output (comma-<em>space</em>, not
 * plain comma) flowing through {@link ispyb.common.util.export.AutoProcBestResultExtractor}'s
 * parsing. Fixture data ({@code test-data-autoproc.sql}, wired into
 * {@link TestBase}) reproduces the "P 41 21 2" / outer-resolution "1.56"
 * example already pinned by the unit tests, so the same numbers are asserted
 * here coming out of the real view.
 */
public class DataCollectionReportCsvTest extends TestBase {

	@Inject
	private DataCollectionGroupRestWsService dataCollectionGroupRestWsService;

	@Inject
	private SpaceGroup3Service spaceGroup3Service;

	@Test
	public void reportCsv_session1_containsProcessedSpaceGroupAndResolution() throws Exception {
		// proposalId=8425, sessionId=1 — seeded by test-data-proposals.sql /
		// test-data-sessions.sql / test-data-collections.sql / test-data-autoproc.sql
		List<DataCollectionSummary> dataCollections = dataCollectionGroupRestWsService
				.getViewDataCollectionBySessionIdHavingImages(8425, 1);

		assertFalse(dataCollections.isEmpty(), "expected at least one seeded data collection row");

		List<DataCollectionReportRow> rows = new DataCollectionReportBuilder()
				.build(dataCollections, spaceGroup3Service);
		assertEquals(1, rows.size());

		DataCollectionReportRow row = rows.get(0);
		assertEquals("P 41 21 2", row.processedSpaceGroup());
		assertEquals("1.56", row.processedResolution());

		String csv = new DataCollectionReportCsvSerializer().toCsv(rows);
		String[] lines = csv.split("\r\n");
		assertEquals("Protein,Sample,Status,Diffraction Note,Observed Resolution,Recorded Resolution,"
				+ "Processed Space Group,Processed Resolution,Comment", lines[0]);
		assertEquals(2, lines.length, "expected a header line plus exactly one data row");
		assertTrue(lines[1].contains("P 41 21 2"), "data row should contain the processed space group: " + lines[1]);
		assertTrue(lines[1].contains("1.56"), "data row should contain the processed resolution: " + lines[1]);
	}

	/**
	 * Same query results as above, but driven through the lazy, per-row
	 * streaming path ({@code DataCollectionReportBuilder#buildRowUnchecked} +
	 * {@code DataCollectionReportCsvSerializer#writeCsv}) that
	 * {@code getDataCollectionsReportBySessionIdCSV} now uses via
	 * {@code StreamingOutput} instead of collecting a
	 * {@code List<DataCollectionReportRow>} and a full CSV {@code String}
	 * up front. Asserts byte-for-byte parity with the eager
	 * {@link DataCollectionReportBuilder#build(List, SpaceGroup3Service)} /
	 * {@link DataCollectionReportCsvSerializer#toCsv(List)} path against the
	 * same real, DB-backed data — the streaming refactor must not change
	 * what's produced, only when/how it's written.
	 */
	@Test
	public void reportCsv_session1_streamingPathMatchesEagerPath() throws Exception {
		List<DataCollectionSummary> dataCollections = dataCollectionGroupRestWsService
				.getViewDataCollectionBySessionIdHavingImages(8425, 1);
		assertFalse(dataCollections.isEmpty(), "expected at least one seeded data collection row");

		DataCollectionReportBuilder builder = new DataCollectionReportBuilder();

		List<DataCollectionReportRow> eagerRows = builder.build(dataCollections, spaceGroup3Service);
		String eagerCsv = new DataCollectionReportCsvSerializer().toCsv(eagerRows);

		Map<String, Integer> spgMap = builder.spaceGroupMap(spaceGroup3Service);
		StringWriter streamedOut = new StringWriter();
		new DataCollectionReportCsvSerializer().writeCsv(streamedOut,
				dataCollections.stream().map(row -> builder.buildRowUnchecked(row, spgMap)));

		assertEquals(eagerCsv, streamedOut.toString());
	}
}
