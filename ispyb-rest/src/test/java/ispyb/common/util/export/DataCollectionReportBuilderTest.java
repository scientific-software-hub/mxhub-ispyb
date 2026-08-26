package ispyb.common.util.export;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ispyb.common.util.export.dto.DataCollectionReportRow;
import ispyb.server.mx.services.ws.rest.datacollectiongroup.DataCollectionSummary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Verifies {@link DataCollectionReportBuilder} maps the raw
 * {@code v_datacollection_summary} row (as produced by
 * {@code getViewDataCollectionBySessionIdHavingImages}) into the curated
 * {@link DataCollectionReportRow} DTO expected by the CSV export, in
 * particular that Processed Space Group / Processed Resolution come out of
 * {@link AutoProcBestResultExtractor} the same way they do for the PDF.
 */
public class DataCollectionReportBuilderTest {

	private static final String SPACE_GROUP = "P 41 21 2";

	private final DataCollectionReportBuilder builder = new DataCollectionReportBuilder();

	private Map<String, Object> representativeRow() {
		Map<String, Object> row = new HashMap<>();
		row.put("Protein_acronym", "HEWL");
		row.put("BLSample_name", "s1");
		row.put("DataCollectionGroup_experimentType", "OSC");
		row.put("DataCollection_comments", "no ice");
		row.put("DataCollection_resolution", "1.62");
		row.put("DataCollectionGroup_comments", "MXCuBE comment");

		// order per shell: [inner, outer, overall] — same representative
		// autoproc data as AutoProcBestResultExtractorTest
		row.put("scalingStatisticsTypes", "innerShell,outerShell,overall");
		row.put("AutoProc_spaceGroups", SPACE_GROUP + "," + SPACE_GROUP + "," + SPACE_GROUP);
		row.put("completenessList", "99.6,100.0,100.0");
		row.put("resolutionsLimitLow", "39.4,1.61,39.4");
		row.put("resolutionsLimitHigh", "6.0,1.56,1.6");
		row.put("rMerges", "1.7,73.5,3.7");
		row.put("Autoprocessing_anomalous", "0,0,0");
		row.put("Autoprocessing_cell_a", "78.81,78.81,78.81");
		row.put("Autoprocessing_cell_b", "78.81,78.81,78.81");
		row.put("Autoprocessing_cell_c", "37.10,37.10,37.10");
		row.put("Autoprocessing_cell_alpha", "90,90,90");
		row.put("Autoprocessing_cell_beta", "90,90,90");
		row.put("Autoprocessing_cell_gamma", "90,90,90");
		return row;
	}

	private Map<String, Integer> spgMap() {
		Map<String, Integer> spgMap = new HashMap<>();
		spgMap.put(SPACE_GROUP, 92);
		return spgMap;
	}

	@Test
	public void build_representativeRow_populatesProcessedSpaceGroupAndResolution() throws Exception {
		List<DataCollectionReportRow> rows = builder.build(Arrays.asList(DataCollectionSummary.from(representativeRow())), spgMap());

		assertEquals(1, rows.size());
		DataCollectionReportRow row = rows.get(0);

		assertEquals("HEWL", row.proteinAcronym());
		assertEquals("s1", row.sampleName());
		assertEquals("OSC", row.status());
		assertEquals("no ice", row.diffractionNote());
		assertEquals("1.62", row.recordedResolution());
		assertEquals("MXCuBE comment", row.comment());

		// the two fields this refactor exists for
		assertEquals(SPACE_GROUP, row.processedSpaceGroup());
		assertEquals("1.56", row.processedResolution());
	}

	@Test
	public void build_rowWithNoAutoprocData_leavesProcessedColumnsBlank() throws Exception {
		Map<String, Object> row = new HashMap<>();
		row.put("Protein_acronym", "HEWL");
		row.put("BLSample_name", "s2");
		row.put("DataCollectionGroup_experimentType", "Characterization");

		List<DataCollectionReportRow> rows = builder.build(Arrays.asList(DataCollectionSummary.from(row)), spgMap());

		assertEquals(1, rows.size());
		DataCollectionReportRow dto = rows.get(0);
		assertEquals("Characterization", dto.status());
		assertTrue(dto.processedSpaceGroup().isEmpty());
		assertTrue(dto.processedResolution().isEmpty());
	}

	@Test
	public void build_nullList_returnsEmptyList() throws Exception {
		assertEquals(0, builder.build(null, spgMap()).size());
	}

	@Test
	public void build_emptyList_returnsEmptyList() throws Exception {
		assertEquals(0, builder.build(Collections.emptyList(), spgMap()).size());
	}
}
